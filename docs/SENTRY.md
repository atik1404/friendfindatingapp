# Sentry — Observability & Monitoring

FriendFin uses **Sentry (Android SDK `8.48.0`)** for crash reporting, performance
tracing, profiling, session replay, structured logs, and breadcrumbs.

> **Platform note:** this is a native **Android (Kotlin)** app — there is no
> Flutter/Dart. The original request referenced "uncaught Flutter/Dart errors";
> that has been implemented with the Android equivalents: uncaught JVM
> exceptions, native (NDK) crashes, coroutine/async exceptions (surfaced through
> the uncaught handler), and ANRs.

Like the analytics layer, **no UI or feature code calls the Sentry SDK
directly.** Everything goes through two abstractions in `core:common`:

| Abstraction | Purpose | Impl (app) |
|-------------|---------|-----------|
| `AnalyticsService` | business events, user context, screen breadcrumbs | fanned out to Clarity **and** Sentry via `CompositeAnalyticsService` |
| `MonitoringService` | exceptions, structured logs, tags, custom spans | `SentryMonitoringService` |

---

## 1. Architecture & integration points

```
core:common (contracts)                     app (Sentry implementation)
┌──────────────────────────┐   ┌────────────────────────────────────────────┐
│ AnalyticsService         │◄──│ SentryAnalyticsManager  (init + event→Sentry)│
│ MonitoringService        │◄──│ SentryMonitoringService (exceptions/logs/spans)
│ LogSeverity / keys       │   │ CompositeAnalyticsService (Clarity + Sentry) │
└──────────────────────────┘   │ SentryPiiScrubber       (beforeSend/breadcrumb)
        ▲                        │ AnalyticsModule         (DI wiring)          │
        │                        └────────────────────────────────────────────┘
core:di  OkHttpModule → SentryOkHttpInterceptor + SentryOkHttpEventListener
core:data NetworkBoundResource → MonitoringService (API failures + exceptions)
app      BaseApplication.onCreate() → analytics.initialize() (Sentry first, earliest)
```

| Where | What was added | Why |
|-------|----------------|-----|
| `BaseApplication.onCreate()` | `analytics.initialize()` moved to the **very first** lines | Initialize Sentry before anything else so startup crashes are captured. |
| `SentryAnalyticsManager.initialize()` | `SentryAndroid.init { … }` with all options | Single, config-driven SDK initialization. |
| `core:di` `OkHttpModule` | `SentryOkHttpInterceptor` + `SentryOkHttpEventListener` | Automatic HTTP breadcrumbs + client spans (DNS/SSL/req/resp) for tracing. |
| `core:data` `NetworkBoundResource` | `MonitoringService.captureException` + `log` | The single network executor → one place to report API failures (status code + path only) and network exceptions. |
| `CompositeAnalyticsService` | fan-out of `AnalyticsService` | Business events / user context / screens reach **both** Clarity and Sentry with **zero ViewModel changes**. |
| `AnalyticsModule` | `@Provides` composite + monitoring | DI so everyone depends only on the abstractions. |

Because the ViewModels were already wired to `AnalyticsService` (login,
registration, send/delete/forward message, profile update, logout, VIP
purchase, screen views, etc.), all of those business events automatically become
**Sentry breadcrumbs** — no additional wiring was required.

---

## 2. Configuration (environment variables / project config)

All settings resolve from **environment variable → Gradle property → default**
(`sentryConfig(...)` in `app/build.gradle.kts`) and are exposed as `BuildConfig`
fields, so Development / Staging / Production differ purely by configuration.

| BuildConfig field | Env var / `-P` property | Debug default | Release default |
|-------------------|-------------------------|---------------|-----------------|
| `SENTRY_DSN` | `SENTRY_DSN` | `""` (disabled) | `""` (set in CI) |
| `SENTRY_ENVIRONMENT` | `SENTRY_ENVIRONMENT` | `development` | `production` |
| `SENTRY_TRACES_SAMPLE_RATE` | `SENTRY_TRACES_SAMPLE_RATE` | `1.0` | `0.2` |
| `SENTRY_PROFILE_SAMPLE_RATE` | `SENTRY_PROFILE_SAMPLE_RATE` | `1.0` | `0.2` |
| `SENTRY_REPLAY_SESSION_SAMPLE_RATE` | same | `1.0` | `0.1` |
| `SENTRY_REPLAY_ON_ERROR_SAMPLE_RATE` | same | `1.0` | `1.0` |
| `SENTRY_SEND_PII` | `SENTRY_SEND_PII` | `true` (dev only) | `false` |
| `SENTRY_RELEASE` | derived | `applicationId@versionName+versionCode` | same |
| `GIT_COMMIT` | derived (`git rev-parse --short HEAD`) | commit sha | commit sha |

**Empty DSN ⇒ Sentry is a safe no-op**, so local builds without credentials run
normally. For **Staging**, build release with
`SENTRY_ENVIRONMENT=staging ./gradlew :app:assembleRelease` (and set `SENTRY_DSN`).

Set the DSN without committing it, e.g. in `~/.gradle/gradle.properties`:

```properties
SENTRY_DSN=https://<key>@o<org>.ingest.sentry.io/<project>
```

or export `SENTRY_DSN` in your CI environment.

---

## 3. Features & where they come from

| Feature | Implementation |
|---------|----------------|
| **Crash reporting** (fatal + native + ANR) | Auto by SDK; `isAnrEnabled`, `isAttachStacktrace`, `isAttachViewHierarchy`. Uncaught JVM + NDK crashes captured automatically. |
| **Performance tracing** | `tracesSampleRate`, `isEnableAutoActivityLifecycleTracing` (app start + screens), `isEnableUserInteractionTracing`, OkHttp spans, and `MonitoringService.trace(name, op) { … }` for custom business transactions. |
| **App metrics** | App-start & frame metrics via activity lifecycle tracing; API latency via OkHttp spans; ANR events; custom metrics via `trace`/logs. |
| **Profiling** | `profileSessionSampleRate`, `profileLifecycle = TRACE`, `isStartProfilerOnAppStart`. |
| **Session Replay** | `sessionReplay.sessionSampleRate` / `onErrorSampleRate`; **`setMaskAllText(true)` + `setMaskAllImages(true)`** so all sensitive content is masked. |
| **Structured logs** | `options.logs.isEnabled = true`; `MonitoringService.log(level, msg, category, attributes)` → `Sentry.logger()` with `SentryLogLevel` (Debug/Info/Warning/Error/Fatal). |
| **User context** | `AnalyticsService.setAuthenticatedUser()` → `Sentry.setUser(id, username[, email])`; `clearUser()` on logout → `Sentry.setUser(null)`. |
| **Breadcrumbs** | Automatic (navigation, http, lifecycle, user interaction) + manual via analytics events and `MonitoringService.addBreadcrumb`. |
| **Custom events** | Every `AnalyticsService.logEvent(...)` becomes a `business` breadcrumb (Login, Registration, Logout, Profile Update, Send/Delete/Forward Message, VIP purchase, …). |
| **Release/env/commit** | `release`, `environment`, and `git_commit` tag from `BuildConfig`. |
| **Error context** | Device/OS/app version attached automatically by the SDK; plus `module`, `endpoint`, `status_code`, `error_type`, `network_status`, `screen`, `user_type` tags. |
| **PII protection** | `isSendDefaultPii=false` in prod, `SentryPiiScrubber` in `beforeSend`/`beforeBreadcrumb`, replay masking, email gated behind `SENTRY_SEND_PII`. |
| **Offline support** | Enabled by default — events are cached to disk and flushed when connectivity returns. |

---

## 4. PII protection & masked fields

- **`SentryPiiScrubber`** redacts the *value* of any tag / extra / breadcrumb-data
  key matching: `pass`, `pwd`, `token`, `secret`, `authorization`, `auth`,
  `api_key`, `cookie`, `session`, `card`, `cvv`, `pan`, `iban`, `ssn`, `otp`,
  `pin`, `email`, `phone` → `"[Filtered]"`.
- **`isSendDefaultPii = false`** in Staging/Production (no auto device/PII data).
- **User email/IP** are stripped unless `SENTRY_SEND_PII=true` (dev only).
- **Session Replay** masks **all** text and images by default.
- **API failures** report **status code + endpoint path only** — never bodies.
- **Screenshots on crash are disabled** (`isAttachScreenshot=false`); the masked
  Session Replay provides visual context instead.

To mask an extra Compose element in replay:
`Modifier.sentryReplayMask()` (`io.sentry.android.replay.sentryReplayMask`).

---

## 5. Adding new instrumentation

- **Business event** → already covered: call
  `analytics.logEvent(AnalyticsEvent.X, params)` (becomes a Sentry breadcrumb).
- **Capture a caught error** →
  `monitoring.captureException(e, mapOf(ObservabilityKey.MODULE to "chat"))`.
- **Structured log** →
  `monitoring.log(LogSeverity.WARNING, "Retry exhausted", category = "chat")`.
- **Custom transaction/span** →
  `monitoring.trace(name = "sync_messages", operation = "task") { … }`.
- **New tag** → `monitoring.setTag(ObservabilityKey.FEATURE, "vip")`.

Never reference the Sentry SDK outside the `app/observability` package.

---

## 6. Verifying in the Sentry dashboard

Dashboard verification requires a real DSN and a device/emulator (it can't be
asserted from a local build). Steps:

1. Set `SENTRY_DSN` (see §2) and install a build:
   `SENTRY_DSN=… ./gradlew :app:installDebug`.
2. **Crashes:** trigger a test crash (e.g. `throw RuntimeException("Sentry test")`
   from a debug menu) → appears under **Issues**.
3. **Traces/Profiling:** cold-start the app and navigate → **Performance**
   shows app-start + screen transactions; **Profiling** shows flame charts.
4. **HTTP:** perform an API call → HTTP spans + breadcrumbs on the transaction.
5. **Session Replay:** reproduce an error → **Replays** shows a masked recording.
6. **Logs:** open **Logs** and confirm entries from `MonitoringService.log`.
7. **Custom events:** perform Login / Send Message / VIP purchase → breadcrumbs
   on the next event, and the `git_commit` / `environment` / `release` tags set.

> **Release health / source context:** optionally add the Sentry Gradle plugin
> (`id("io.sentry.android.gradle") version "…"`) with a `SENTRY_AUTH_TOKEN` to
> upload ProGuard mappings and source context. It was intentionally left out so
> builds don't require a Sentry auth token; enable it in CI when desired.

---

## 7. Performance overhead

Kept minimal and production-tuned: reduced sampling in release
(`traces`/`profile` `0.2`, `replay session 0.1`), replay only fully captured on
error (`onErrorSampleRate 1.0`), screenshots disabled, and all SDK calls are
guarded/non-throwing. Offline caching batches uploads. Sampling rates are
overridable per environment via the env vars in §2.

# Analytics & Session Recording (Microsoft Clarity)

FriendFin uses **Microsoft Clarity** for user-behavior analytics and session
recording. All analytics flow through a single abstraction, `AnalyticsService`,
so the app never calls the Clarity SDK directly from UI or feature code.

---

## 1. Architecture

```
core:common                         app
┌───────────────────────────┐       ┌────────────────────────────────────┐
│ AnalyticsService (iface)  │◄──────│ ClarityAnalyticsManager (impl)     │
│ AnalyticsEvent (consts)   │       │ AnalyticsModule (@Binds)           │
│ AnalyticsParam (consts)   │       │ ScreenNameMapper (NavKey → name)   │
│ AnalyticsScreen (consts)  │       │ BaseApplication (init + lifecycle) │
└───────────────────────────┘       │ MainActivity (deep link / notif)   │
        ▲                            └────────────────────────────────────┘
        │ depends on
   feature:* ViewModels, core:data (NetworkBoundResource)
```

- **Contract lives in `core:common`** (`com.friend.common.analytics`) so every
  feature module and the network layer can depend on it with no cycles.
- **Implementation lives in `app`** (`com.friendfinapp.dating.analytics`) which
  is the only place that references the Clarity SDK.
- **Dependency injection:** `AnalyticsModule` binds `ClarityAnalyticsManager`
  (a `@Singleton`) to `AnalyticsService` in the `SingletonComponent`, so the
  same instance is injected everywhere — ViewModels (`@HiltViewModel`
  constructor injection), infrastructure, the `Application`, and the `Activity`.

### Key design points
- **Single initialization.** `BaseApplication.onCreate()` calls
  `analytics.initialize()` exactly once. The manager guards re-entry with an
  `AtomicBoolean`, so it is safe even if called from multiple entry points.
- **Thread-safe & crash-safe.** Every Clarity call is marshalled to the main
  thread (Clarity requires it) and wrapped in try/catch — analytics can be
  called from any thread and will never crash the app.
- **Screen tracking is centralized and deduplicated** in `AppNavConfiguration`
  (see §4).

---

## 2. Integration steps (how it was wired)

1. **Dependency** — `com.microsoft.clarity:clarity-compose` is declared in
   `gradle/libs.versions.toml` (`crarity-compose`, version `clarityCompose`) and
   added to `app/build.gradle.kts`.
2. **Project ID** — configured in `ClarityAnalyticsManager.CLARITY_PROJECT_ID`.
3. **Initialize once** — `BaseApplication.onCreate()` →
   `analytics.initialize()`, then registers a `ProcessLifecycleOwner` observer
   for foreground/background + session events.
4. **Screen tracking** — `AppNavConfiguration` observes the top of the nav back
   stack and calls `analytics.trackScreen(name)`.
5. **Events** — ViewModels inject `AnalyticsService` and call `logEvent(...)`
   (or the semantic helpers) at the relevant success points.
6. **Diagnostics** — `NetworkBoundResource` (the single network executor)
   reports API failures and errors (status code + endpoint path only).

To test locally, temporarily set `logLevel = LogLevel.Verbose` in
`ClarityAnalyticsManager.initialize()` and watch Logcat (tag `Analytics`).
Sessions can take up to ~2 hours to appear on the Clarity dashboard (in-progress
sessions are visible in real time).

---

## 3. Naming conventions

- **Event names** (`AnalyticsEvent`): `Title Case, human readable`
  (e.g. `"Send Message"`). They surface verbatim on the Clarity dashboard.
- **Parameter / tag keys** (`AnalyticsParam`): `lower_snake_case`
  (e.g. `status_code`).
- **Screen names** (`AnalyticsScreen`): `Title Case`, decoupled from route class
  names so navigation refactors don't silently change analytics.
- **Never** hard-code a string at a call site — always reference a constant.

---

## 4. Screen tracking

Screen views are tracked **centrally** in `AppNavConfiguration`:

```kotlin
val currentKey = backStack.lastOrNull()
LaunchedEffect(currentKey) {
    currentKey?.let { analytics.trackScreen(it.toAnalyticsScreenName()) }
}
```

- `NavKey.toAnalyticsScreenName()` (`ScreenNameMapper`) maps each destination to
  a stable, human-readable name.
- **No duplicates on config change / recomposition:** the back stack is
  saved/restored, so the `LaunchedEffect` key only changes on real navigation.
  The manager additionally ignores consecutive identical screen names.
- `trackScreen` calls Clarity's `setCurrentScreenName()` (the intended
  navigation API — Clarity starts a new page only when the name changes), emits
  a `Screen View` event with a `screen_name` param, and increments the
  "screens visited" session counter.

---

## 5. Tracked events

### Required user actions
| Event | Where |
|-------|-------|
| `User Login` | `LoginViewModel` (password & Google) |
| `User Registration` | `RegistrationViewModel` |
| `Send Message` | `ConversationViewModel` (`trackMessageSent`) |
| `Delete Message` | `ConversationViewModel` |
| `Forward Message` | `ForwardMessageViewModel` |
| `Update Profile` | `PersonalSettingViewmodel` |
| `Logout` | `ProfileOverviewViewModel` |
| `VIP Membership Activated` | `BillingViewModel` |

### App / session lifecycle
`App Launch`, `First App Open`, `App Foregrounded`, `App Backgrounded`,
`Session Start`, `Session End` — all in `BaseApplication` / the manager.

### High-value interactions
`Screen View`, `Profile Viewed`, `Search Performed`, `Conversation Opened`,
`Message Attachment Sent`, `Notification Opened`, `Deep Link Opened`.

### Monetization
`Subscription Screen Viewed`, `Purchase Started`, `Purchase Completed`,
`Purchase Failed` — all in `BillingViewModel`.

### Reliability / diagnostics (non-sensitive)
`Error Occurred`, `API Failure` (status code + endpoint path only),
`Network Connectivity Changed`.

> The full list is the single source of truth in
> [`AnalyticsEvent`](../core/common/src/main/kotlin/com/friend/common/analytics/AnalyticsEvent.kt).

---

## 6. Session & user context (Clarity custom tags)

Set once per session (re-applied inside Clarity's `onSessionStarted` callback):

| Tag | Source |
|-----|--------|
| `anonymous_user_id` | generated UUID, persisted in SharedPreferences |
| `user_id` | username/handle after auth (**never** email/name) |
| `user_type` | `Free` / `VIP` / `Anonymous` |
| `app_version`, `build_number` | `PackageManager` |
| `platform` | `Android` |
| `device_language` | `Locale.getDefault().language` |
| `os_version` | `Build.VERSION.RELEASE` |
| `session_duration_sec` | computed on background |
| `screens_visited` | incremented per screen |
| `messages_sent` | incremented per sent message |

---

## 7. Adding a new event

1. Add a constant to `AnalyticsEvent` (Title Case).
2. Add any new parameter keys to `AnalyticsParam` (lower_snake_case).
3. Inject `AnalyticsService` into the ViewModel (constructor param) and call:
   ```kotlin
   analytics.logEvent(
       AnalyticsEvent.MY_NEW_EVENT,
       mapOf(AnalyticsParam.SOURCE to "Home"),
   )
   ```
4. For a **new screen**, add a name to `AnalyticsScreen` and a branch to
   `ScreenNameMapper` — screen tracking then happens automatically.
5. **Do not** call the Clarity SDK directly and **do not** put PII in event
   names or parameters.

---

## 8. Privacy considerations & masked fields

- **On-device masking:** Clarity masks all captured **text and images by
  default**. Masking strictness is managed from the Clarity dashboard (no PII
  leaves the device unmasked). For a specific view that must always be masked,
  use Clarity's masking (`clarity-compose` mask modifier / `Clarity.maskView`).
- **No PII in tags or events.** We deliberately never send:
  - email addresses, full names, passwords, phone numbers;
  - message bodies, search text, or any free-text the user typed;
  - request/response bodies.
- **User identity** uses the non-PII username/handle for `user_id` (via
  `setCustomUserId`), never the email.
- **API failures** report **status code + endpoint path only** — never the
  request or response body (`NetworkBoundResource`).
- **Errors** report the exception **type name** and code only.
- **Attachments** report only a type label (`Image` / `Video` / `Audio` /
  `File`), never the file or its contents.
- Remember to keep the app's Terms & Privacy Policy updated to disclose Clarity
  session recording.

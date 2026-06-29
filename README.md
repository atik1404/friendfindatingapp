# FriendFin — Technical Documentation

> Native Android dating application built with Kotlin, Jetpack Compose, and a modular Clean-Architecture codebase.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Core Features](#2-core-features)
3. [Tech Stack & Dependencies](#3-tech-stack--dependencies)
4. [System Architecture & Design Patterns](#4-system-architecture--design-patterns)
5. [Project Structure](#5-project-structure)
6. [Setup, Installation & Environment Configuration](#6-setup-installation--environment-configuration)
7. [Build, Run & Deployment](#7-build-run--deployment)
8. [API & Integrations](#8-api--integrations)
9. [State Management](#9-state-management)
10. [Database & Storage](#10-database--storage)
11. [Authentication & Authorization Flow](#11-authentication--authorization-flow)
12. [Testing Strategy](#12-testing-strategy)
13. [Coding Conventions & Best Practices](#13-coding-conventions--best-practices)
14. [CI/CD Pipeline](#14-cicd-pipeline)
15. [Known Limitations & Future Improvements](#15-known-limitations--future-improvements)
16. [Contribution Guidelines](#16-contribution-guidelines)
17. [Appendix: Module Reference](#17-appendix-module-reference)

---

## 1. Project Overview

**FriendFin** (`com.friendfinapp.dating`) is a social/dating Android application that lets users discover other members, view profiles, chat in real time, manage their own profile, and optionally subscribe to a VIP membership through Google Play Billing.

| Attribute              | Value                                |
|------------------------|--------------------------------------|
| Application ID         | `com.friendfinapp.dating`            |
| Version Name           | `1.1.172`                            |
| Version Code           | `173`                                |
| `minSdk`               | 23 (Android 6.0)                     |
| `targetSdk` / `compileSdk` | 36                               |
| Language               | Kotlin (JVM 17)                      |
| UI Toolkit             | Jetpack Compose                      |
| Architecture           | Clean Architecture + MVVM            |
| Modularization         | Multi-module Gradle (core + feature) |

The codebase is laid out as a multi-module Gradle project. The `:app` module is a thin shell that wires together a set of independent feature modules through a Navigation 3 backstack and a Hilt dependency-injection graph defined in the `core:di` modules.

---

## 2. Core Features

The feature surface of the app maps 1-to-1 to the `:feature:*` Gradle modules and to the navigation graph in `app/src/main/.../navigation/`.

### Authentication
- **Splash screen** with auto-routing based on stored login state.
- **Email/password login** with client-side form validation.
- **Google Sign-In** (One Tap / Credential Manager based) — falls back to registration if the email is unknown.
- **Registration** with profile bootstrap.
- **Forgot password** flow via email.
- **Refresh-token** based session continuation (HTTP 401 trigger).

### Home / Discovery
- Friend suggestion feed with filtering (gender, location, etc.) via `FilterUserBottomSheet`.
- Address / location filters using country/state/city dropdowns.
- Banner advertisements for non-premium users.
- Shimmer loading placeholders.

### Chat / Messaging
- **Chat list** with paging.
- **Conversation screen** supporting:
   - Text, image, audio (recording) and video messages (multipart upload).
   - In-conversation search.
   - Message forwarding to one or many users.
   - Delete-message bottom sheet.
   - Block/Report user actions.
- Push notifications via Firebase Cloud Messaging (`PushNotificationService`).

### Profile Management
- **My profile** view & inline edit.
- **Profile overview** dashboard for the logged-in user.
- **Profile completion** wizard for new users.
- **Other profile** view with block/report/abuse-report flows.
- **Personal settings** (preferences, notifications, account actions).
- **Change password**.
- **VIP membership** purchase screen backed by Google Play Billing.

### Common / Cross-cutting
- **Privacy Policy** screen.
- **Report abuse** flow.
- **Video player** (ExoPlayer).
- **Image preview** & cropping (Canhub `image-cropper`).

---

## 3. Tech Stack & Dependencies

### Languages & Toolchain
- **Kotlin** `2.3.20`
- **Java target** 17 (`sourceCompatibility` / `targetCompatibility` = `VERSION_17`)
- **Android Gradle Plugin** `9.1.0`
- **KSP** `2.3.4`
- **Gradle Wrapper** (see `gradle/wrapper/gradle-wrapper.properties`)

### UI
| Library | Purpose |
|---------|---------|
| Jetpack Compose BOM `2026.03.01` | Compose UI |
| Material 3 `1.4.0` | Design system |
| Compose Material Icons (extended) | Iconography |
| ConstraintLayout-Compose `1.1.1` | Complex layouts |
| Lottie `6.7.1` | Vector animations |
| Coil `3.4.0` (+ Glide compose beta) | Image loading |
| Canhub `image-cropper` `4.7.0` | Image picker / cropper |
| ExoPlayer (Media3) `1.10.0` | Video & audio playback |
| `view-pager-dot-indicator` `0.36.0` | Pager indicators |

### Architecture / DI
| Library | Purpose |
|---------|---------|
| Hilt `2.59.2` (+ `hilt-navigation-compose` `1.3.0`) | DI |
| `javax.inject` | Inject annotations |
| Kotlinx Coroutines `1.10.2` & Flow | Async / streams |
| RxJava 3 `3.1.12` (legacy adapter) | Retrofit RxJava bridge |
| Kotlinx Serialization `1.11.0` | NavKey / typed nav args |
| Navigation 3 (`androidx.navigation3:*` `1.1.0`) | New navigation library |
| Navigation Compose `2.9.7` | Compose nav fallback |
| `lifecycle-viewmodel-navigation3` `2.10.0` | ViewModel scoping per nav entry |

### Networking
| Library | Purpose |
|---------|---------|
| Retrofit `3.0.0` | REST client |
| OkHttp `5.3.2` + Logging Interceptor | HTTP |
| Gson `2.13.2` | JSON |
| RxJava3 CallAdapter | Legacy adapter retained for future use |

### Persistence
| Library | Purpose |
|---------|---------|
| Room `2.8.4` | Local DB (configured but currently disabled) |
| AndroidX Preference / SharedPreferences | Lightweight key-value storage |

### Firebase / Google
| Library | Purpose |
|---------|---------|
| Firebase BOM `34.12.0` | Analytics, Crashlytics, Performance, Cloud Messaging |
| Google Play Services Ads `25.1.0` | AdMob banner + App-Open ads |
| Google Play Services Auth `21.5.1` | Sign-in |
| Google Credential Manager `1.6.0` | Google account chooser |
| Play In-App Update `2.0.1` | Force/flexible updates |
| **Play Billing** `8.3.0` | VIP subscriptions |

### Observability
- **Timber** `5.0.1` for logs.
- **LeakCanary** `2.14` (debug only).
- **Firebase Crashlytics** + **Firebase Performance** (release).

### Testing
- JUnit 4 `4.13.2`, AndroidX Test Ext JUnit `1.3.0`, Espresso `3.7.0`.

The single source of truth for versions is `gradle/libs.versions.toml` (Gradle Version Catalog).

---

## 4. System Architecture & Design Patterns

### High-Level Architecture

The project follows **Clean Architecture** with strict layering, applied per-feature on top of a shared multi-module foundation.

```
┌──────────────────────────────────────────────────────────────────┐
│                          :app                                    │
│  ─ MainActivity (entry point, Hilt @AndroidEntryPoint)           │
│  ─ AppNavConfiguration (Navigation 3 NavDisplay)                 │
│  ─ ApplicationModule / RepositoryModule (Hilt @Binds, @Provides) │
└──────────────────────────────────────────────────────────────────┘
                              │ depends on
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│                       :feature:* (UI layer)                      │
│  Each module = one screen / flow:                                │
│   ─ <Feature>ScreenRoute  (Composable, collects state, navigates)│
│   ─ <Feature>Screen        (stateless / hoisted Composables)     │
│   ─ <Feature>ViewModel     (HiltViewModel, StateFlow, Channel)   │
│   ─ UiState / UiAction / UiEvent (state, intent, one-shot event) │
│   ─ components/  (presentational @Composable building blocks)    │
└──────────────────────────────────────────────────────────────────┘
                              │ uses
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│                  :core:domain  (business logic)                  │
│   ─ ApiUseCaseParams / ApiUseCaseNonParams                       │
│   ─ apiusecase/<feature>/...                                     │
│   ─ repository/remote/*Repository (interfaces)                   │
│   ─ validator/  FormValidator + LoginIoResult etc.               │
│   ─ base/ApiResult<T>  (sealed: Loading | Success | Error)       │
└──────────────────────────────────────────────────────────────────┘
                              │ implemented by
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│            :core:data  (data layer / repositories)               │
│   ─ NetworkBoundResource  (Flow<ApiResult<T>> wrapper)           │
│   ─ apiservice/*ApiServices (Retrofit interfaces)                │
│   ─ repoimpl/remote/*RepoImpl  (implements domain repository)    │
│   ─ mapper/<feature>/*ApiMapper (DTO → Entity)                   │
└──────────────────────────────────────────────────────────────────┘
                              │ wires
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│      :core:di      :core:sharedpref      :core:cache             │
│  ─ OkHttp + Retrofit + Gson modules                              │
│  ─ AuthenticationRefreshToken (401 → refresh)                    │
│  ─ Qualifiers (AppBaseUrl, AppOpenAdId, GoogleWebClientId, …)    │
│  ─ SharedPrefHelper / SpKey (auth tokens, user profile cache)    │
│  ─ Room AppDatabase (declared, not yet wired into :app)          │
└──────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│  :core:model:apiresponse  (raw DTOs from network)                │
│  :core:model:entity       (domain entities used by UI)           │
│  :core:common             (constants, utils, BaseViewModel,      │
│                            extensions, BillingManager, Google    │
│                            sign-in helper, date parser)          │
│  :core:design-system      (theme, colors, typography, R.string)  │
│  :core:ui                 (shared Compose components, dialogs,   │
│                            shimmer, banner ads, top bar…)        │
└──────────────────────────────────────────────────────────────────┘
```

### Patterns Used

- **Clean Architecture (3 layers)** — `presentation (feature) → domain → data`. The domain layer depends on nothing platform-specific.
- **MVVM + MVI hybrid** — Each screen exposes a `StateFlow<UiState>`, accepts `UiAction` sealed-class intents, and emits one-shot `UiEvent`s through a `Channel`. See `LoginViewModel` for the canonical implementation.
- **Repository Pattern** — Domain defines interfaces (e.g. `AuthRepository`); `:core:data` provides implementations bound via `@Binds` in `RepositoryModule`.
- **Use Case / Interactor** — Each remote call has a `*ApiUseCase` that performs validation and delegates to a repository, returning `Flow<ApiResult<T>>`.
- **Mapper Pattern** — `Mapper<R,E>` converts API response DTOs into domain entities so the UI never sees raw network types.
- **Sealed result types** — `ApiResult<T>` (`Loading | Success | Error`) and `DataValidationResult` (`Success | Failure`) replace exceptions for control flow.
- **Convention Plugins** — `build-logic/convention/*` provides re-usable Gradle plugin classes (`android.application`, `android.library`, `android.library.compose`, `android.features`, `android.hilt`, `android.firebase`, `android.room`, `jvm.library`) so every module declares only its own dependencies.
- **Dependency Injection** — Hilt, with `SingletonComponent` for app-wide bindings (DB, network, repositories) and `@HiltViewModel` for screen scopes.
- **Authenticator-based token refresh** — OkHttp `Authenticator` (`AuthenticationRefreshToken`) intercepts 401s and replays the request with a refreshed token.

### Navigation Architecture (Navigation 3)

```
MainActivity
└── AppTheme
    └── AppNavConfiguration                    // CompositionLocalProvider for NavResultManager
        └── NavDisplay(backStack)
            └── entryProvider {
                  AuthNavGraph.register(...)
                  CommonNavGraph.register(...)
                  ChatMessageNavGraph.register(...)
                  ProfileNavGraph.register(...)
                }
```

- **NavKeys** are declared in `navigation/AppNavKey.kt` as `@Serializable` sealed-interface members (e.g. `AuthScreens.LoginNavScreen`, `ProfileScreens.OtherProfileNavScreen(username)`).
- **Cross-screen results** flow via `NavResultManager` (`LocalNavResultManager` `CompositionLocal`), avoiding handle-based plumbing.
- Slide + fade transitions are defined globally in `AppNavConfiguration` (700ms left/right slide).

---

## 5. Project Structure

Top-level layout:

```
friendfin/
├── app/                          # Application module (entry point, navigation)
│   ├── src/main/java/com/friendfinapp/dating/
│   │   ├── MainActivity.kt
│   │   ├── BaseApplication.kt
│   │   ├── AppOpenAdManager.kt
│   │   ├── di/
│   │   │   ├── ApplicationModule.kt
│   │   │   └── RepositoryModule.kt
│   │   ├── navigation/
│   │   │   ├── AppNavConfiguration.kt
│   │   │   ├── AppNavKey.kt
│   │   │   ├── NavResultManager.kt
│   │   │   ├── NavigationResultKey.kt
│   │   │   └── graph/
│   │   │       ├── AuthNavGraph.kt
│   │   │       ├── ChatMessageNavGraph.kt
│   │   │       ├── CommonNavGraph.kt
│   │   │       └── ProfileNavGraph.kt
│   │   └── notification/
│   │       └── PushNotificationService.kt
│   ├── src/main/res/             # Compose-friendly resources (drawables, strings, etc.)
│   ├── src/main/AndroidManifest.xml
│   ├── proguard-rules.pro
│   ├── google-services.json      # Required, not in VCS by default — see §6
│   └── build.gradle.kts
│
├── build-logic/                  # Composite build with custom Gradle plugins
│   └── convention/
│       ├── build.gradle.kts
│       └── src/main/kotlin/
│           ├── ApplicationConventionPlugin.kt
│           ├── LibraryConventionPlugin.kt
│           ├── LibraryComposeConventionPlugin.kt
│           ├── ComposeConventionPlugin.kt
│           ├── FeatureConventionPlugin.kt
│           ├── FirebaseConventionPlugin.kt
│           ├── HiltConventionPlugin.kt
│           ├── JvmLibraryConventionPlugin.kt
│           ├── RoomConventionPlugin.kt
│           └── com/friendfinapp/
│               ├── AppConfig.kt         # version, SDK, applicationId
│               ├── AndroidCompose.kt
│               ├── KotlinAndroid.kt
│               ├── AndroidKSP.kt
│               └── ProjectExtension.kt  # `libs` accessor
│
├── core/
│   ├── common/         # AppConstants, extensions, BaseViewModel, utils (Billing, Google sign-in, date)
│   ├── design-system/  # AppTheme, colors, typography, drawables, strings (R = com.friend.designsystem)
│   ├── ui/             # Shared Composables (AppButton, AppScaffold, BannerAds, dialogs, shimmer…)
│   ├── di/             # OkHttp / Retrofit modules + qualifiers + AuthenticationRefreshToken
│   ├── domain/         # Use cases, repository contracts, validators, ApiResult, FormInput
│   ├── data/           # Retrofit services, repository implementations, mappers, NetworkBoundResource
│   ├── sharedpref/     # SharedPrefHelper + SpKey
│   ├── cache/          # Room AppDatabase + DAOs (defined; currently not wired into :app)
│   └── model/
│       ├── apiresponse/   # DTOs deserialized from the backend
│       └── entity/        # Domain entities consumed by UI
│
├── feature/
│   ├── home/
│   ├── auth/
│   │   ├── splash-screen/
│   │   ├── login/
│   │   ├── registraion/        # NB: typo retained in the module path
│   │   └── forgot-password/
│   ├── chat-message/
│   │   ├── chat-list/
│   │   ├── chat-room/
│   │   └── forward-message/
│   ├── profile-manager/
│   │   ├── profile-overview/
│   │   ├── my-profile/
│   │   ├── other-profile/
│   │   ├── profile-completion/
│   │   ├── personal-setting/
│   │   ├── change-password/
│   │   └── vip-membership/
│   └── common/
│       ├── privacy-policy/
│       ├── report-abuse/
│       └── video-player/
│
├── gradle/
│   ├── libs.versions.toml        # Version catalog (single source of truth)
│   └── wrapper/
├── settings.gradle.kts           # Module inclusion + TYPESAFE_PROJECT_ACCESSORS
├── build.gradle.kts              # Root: plugin aliases only
├── gradle.properties             # AndroidX, R8 full mode, kapt K2, Jetifier
├── local.properties              # SDK path (per developer, not committed)
├── README.md
├── TO-DO.txt                     # In-flight backlog (see §15)
└── gradlew / gradlew.bat
```

### Naming Conventions Inside a Feature Module

```
feature/<group>/<feature-name>/src/main/kotlin/com/friend/<feature>/
├── <Feature>ScreenRoute.kt     # @Composable entry point, wires VM and nav
├── <Feature>Screen.kt          # Stateless Composable, accepts state + action
├── <Feature>ViewModel.kt       # @HiltViewModel
├── UiState.kt                  # data class UiState, sealed UiAction, sealed UiEvent
└── components/                 # Sub-Composables for this screen only
```

---

## 6. Setup, Installation & Environment Configuration

### Prerequisites
- **JDK 17** (Temurin / Zulu recommended).
- **Android Studio Ladybug** (`2024.x`) or newer with the AGP 9.1 compatibility.
- **Android SDK** with platform `36` and build-tools matching.
- A **Google Cloud / Firebase project** with the application ID `com.friendfinapp.dating` registered.

### Initial Clone
```bash
git clone <repo-url> friendfin
cd friendfin
```

### Required Local Files

1. **`local.properties`** — generated by Android Studio. Must contain at minimum:
   ```properties
   sdk.dir=/Users/<you>/Library/Android/sdk
   ```

2. **`app/google-services.json`** — download from the Firebase Console (Project Settings → Your Apps → Android `com.friendfinapp.dating`) and drop into `app/`. The Firebase Gradle plugin will fail the build without it.

3. **Release keystore** (only if you build release locally). The project expects `app/app_credential/friendfinjks`. Credentials are hard-coded in `app/build.gradle.kts` (`signingConfigs.release`); for new contributors, ask the project owner for a copy of the keystore.

   > ⚠️ **Security note:** the release keystore password is currently committed in `app/build.gradle.kts`. See [§15 Known Limitations](#15-known-limitations--future-improvements).

### Backend & Third-Party IDs
The following are wired through Hilt qualifiers (`core/di/qualifier/AppIDs.kt`, `AppBaseUrl.kt`, …). Bindings are provided in `:app` (currently hardcoded in `ApplicationModule.kt`-adjacent providers — search `@Provides @AppBaseUrl`, `@AppOpenAdId`, etc.):

| Qualifier             | Purpose                                  |
|-----------------------|------------------------------------------|
| `@AppBaseUrl`         | REST base URL for Retrofit               |
| `@AppFileBaseUrl`     | Base URL for static/uploaded media       |
| `@AppOpenAdId`        | AdMob App-Open ad unit                   |
| `@BannerAdId`         | AdMob banner ad unit                     |
| `@GoogleWebClientId`  | OAuth client ID (`AppConstants.WEB_CLIENT_ID`) |
| `@AppBuildType`       | Build flavor (currently unused)          |
| `@AppVersion`         | Version name                             |

Update these provider methods (and `res/values/strings.xml` for `AdmobAppId` / `CHANNEL_ID`) when pointing at a new environment.

---

## 7. Build, Run & Deployment

### Build Variants
Defined in `app/build.gradle.kts`:

| Variant | Minified | Resource shrink | Signing |
|---------|----------|-----------------|---------|
| `debug` | No | No | Debug keystore (`signingConfigs.debug`) |
| `release` | Yes (R8 full mode) | Yes | `friendfinjks` keystore |

`android.enableR8.fullMode=true` is set globally (`gradle.properties`).

### Common Gradle Commands

```bash
# Compile and assemble debug APK
./gradlew :app:assembleDebug

# Install debug APK on the connected device
./gradlew :app:installDebug

# Assemble release APK (requires keystore)
./gradlew :app:assembleRelease

# Build an Android App Bundle for Play Console upload
./gradlew :app:bundleRelease

# Clean
./gradlew clean

# Run Android Lint on a single module
./gradlew :app:lintDebug

# Run unit tests (JVM)
./gradlew testDebugUnitTest

# Run instrumented tests on a connected device
./gradlew connectedDebugAndroidTest
```

### Running From Android Studio
1. Open the project root in Android Studio.
2. Let Gradle sync complete (the version catalog and composite `build-logic` resolve first).
3. Select the `app` run configuration → choose a device/emulator → Run.

### Deployment / Release Checklist
1. Bump `versionCode` & `versionName` in `build-logic/convention/.../AppConfig.kt`.
2. Update release notes / changelog.
3. Confirm `google-services.json` is the **production** Firebase project.
4. `./gradlew :app:bundleRelease`.
5. Upload `app/build/outputs/bundle/release/app-release.aab` to Play Console.
6. Crashlytics symbol mapping is auto-uploaded — `FirebaseConventionPlugin` sets `mappingFileUploadEnabled = true`.

---

## 8. API & Integrations

### Networking Stack

`core:di/module/RetrofitModule.kt` creates a singleton Retrofit with:
- Base URL from `@AppBaseUrl`.
- `OkHttpClient` from `OkHttpModule` (30 s connect/read/write timeouts, logging interceptor, Authorization header, refresh-token authenticator).
- `GsonConverterFactory` (HTML escaping disabled).
- `RxJava3CallAdapterFactory` (legacy; new code uses `suspend fun` + `Flow`).

Service interfaces live in `core:data/apiservice/`:

| Service | Description |
|---------|-------------|
| `AuthApiServices` | Login, Google login, registration, forgot/change password, logout, FCM token update |
| `ProfileManagerApiServices` | Profile read/update, photo upload, abuse report, block/unblock, account deletion, online-status ping |
| `ChatMessageApiServices` | Chat list, history, search, send (multipart), forward, delete messages |
| `SearchApiServices` | Friend suggestion feed, country/state/city lookup |
| `AuthRefreshApiService` (in `core:di`) | Refresh-token call invoked by `Authenticator` |

### Endpoint Reference

> Paths are relative to `@AppBaseUrl`. Bodies are documented by the `Params` data classes on each `*ApiUseCase`.

**Auth (`api/Auth/v1/...`)**

| Method | Path | Use Case | Notes |
|--------|------|----------|-------|
| POST | `Login` | `PostLoginApiUseCase` | Body: `{username, password}` |
| POST | `LoginWithGoogle?email=` | `PostGoogleLoginApiUseCase` | If user unknown → navigate to registration |
| POST | `Register` | `PostRegistrationApiUseCase` | |
| POST | `ForgotPassword?email=` | `PostForgotPasswordApiUseCase` | |
| POST | `PasswordChange` | `PostPasswordChangeApiUseCase` | |
| POST | `Logout` | `PostLogoutApiUseCase` | |

**Profile (`api/Profile/v1/...` & `v1/...`)**

| Method | Path | Notes |
|--------|------|-------|
| GET | `GetProfileInformation` | Current user |
| GET | `GetOtherProfileInformation?otherUsername=` | Other user |
| POST | `UpdateProfileInformation` | Body: profile update params |
| POST | `UploadPhoto` (multipart) | Username, PhotoAlbumID, image, … |
| POST | `v1/UserOnlineUpdate` | Heartbeat |
| POST | `v1/AbuseReport` | |
| POST | `v1/BlockUnblock?blockedUser=` | |
| POST | `v1/UserDelete?deleteReason=` | Account deletion |

**Chat (`v1/...`)**

| Method | Path | Notes |
|--------|------|-------|
| GET | `Chats?pageNo=` | Paged chat list |
| GET | `ChatHistory?toUsername=` | Conversation messages |
| POST | `ChatHistorySearch` | In-conversation search |
| POST | `MessageForward` | Forward to many recipients |
| POST | `SelectedMessageClean` | Delete selected messages |
| POST | `MessageSend` (multipart) | Text + optional image/audio/video parts |

**Search (`v1/...` & `api/Location/v1/...`)**

| Method | Path |
|--------|------|
| POST | `UserHomeWithSearch` |
| GET | `api/Location/v1/countries` |
| GET | `api/Location/v1/regions?country=` |
| GET | `api/Location/v1/cities?region=&country=` |

### Error & Result Handling

All API calls flow through `NetworkBoundResource.downloadData`:

```kotlin
suspend fun <T> downloadData(api: suspend () -> Response<T>): Flow<ApiResult<T>>
```

It emits `Loading(true)` → `Success(data)` **or** `Error(message, code)` → `Loading(false)`. Error messages are parsed from the server `{"message": "..."}` envelope, with network/SSL/timeout/IO exceptions mapped to user-friendly strings.

### Third-Party Integrations

| Integration | Where wired |
|-------------|-------------|
| Firebase Cloud Messaging | `app/.../notification/PushNotificationService.kt` (manifest service), FCM token written to `SpKey.fcmToken` at app start in `BaseApplication.getFirebaseToken()` |
| Firebase Crashlytics | `FirebaseConventionPlugin` enables mapping-file upload on release |
| Firebase Performance | Plugin alias `firebase.perf.plugin` in `app/build.gradle.kts` |
| AdMob (Banner + App-Open) | `MobileAds.initialize(this)` in release; `AppOpenAdManager` shown on resume for non-premium users; meta-data `com.google.android.gms.ads.APPLICATION_ID` |
| Google Sign-In / Credential Manager | `core/common/.../GoogleSignInManager.kt` |
| Google Play Billing | `BillingManager.start(this)` in `MainActivity.onCreate`; subscription check in `BaseApplication.checkActivePurchases` toggles `AppConstants.isPremiumUser` |
| ExoPlayer (Media3) | `feature/common/video-player` |
| Canhub `image-cropper` | Activity declared in `AndroidManifest.xml`; used by profile photo upload |

---

## 9. State Management

The app uses a **unidirectional data flow** per screen:

```
                ┌────────────┐    action      ┌──────────────┐
   user input → │ Composable │ ─────────────▶ │  ViewModel   │
                │  Screen    │                │ (HiltVM)     │
                │            │ ◀────────────  │              │
                └────────────┘   StateFlow    └──────────────┘
                       ▲                            │
                       │       UiEvent (Channel)    │
                       └────────────────────────────┘
```

Conventions (see `feature:auth:login`):

- `data class UiState(...)` holds form fields, loading flags, validation results.
- `sealed interface UiAction` is the intent vocabulary (`UsernameChanged`, `PerformLogin`, …).
- `sealed interface UiEvent` is for one-shot side effects (`ShowMessage`, `NavigateToHome`).
- The `ViewModel` exposes `uiState: StateFlow<UiState>` and `uiEvent: Flow<UiEvent>`. UI actions are dispatched through a single `val action: (UiAction) -> Unit` lambda passed down to Composables.
- `BaseViewModel.execute { ... }` is a convenience that launches in `viewModelScope`.
- Form-field state uses `FormInput<T>` from `core:domain/base/FormInput.kt`, with validation handled by `FormValidator` and reported via use-case `Channel<...IoResult>`.

Global "ambient" state lives in:
- `AppConstants.isPremiumUser` (toggled by Billing).
- `SharedPrefHelper` for persisted user/session state.
- `LocalNavResultManager` Composition Local for cross-screen results.

---

## 10. Database & Storage

### Room (`:core:cache`)
- `AppDatabase` and `DatabaseModule` are defined and a `DemoDao` is present.
- **Status:** the cache module is *not* currently included as an `implementation` in `:app` (see `app/build.gradle.kts` — `//implementation(cache)` is commented out). Room is configured via the `android.room` convention plugin but no live persistence is happening on `main` at the time of writing.

### SharedPreferences (`:core:sharedpref`)
The primary client-side store. `SharedPrefHelper` (Hilt-provided in `ApplicationModule`) wraps a single `SharedPreferences`. Keys are centralised in `SpKey`:

| Category | Keys |
|----------|------|
| Session | `loginStatus`, `authToken`, `refreshToken`, `tokenExpireAt`, `isLoginByGoogle`, `googleLoginToken`, `fcmToken` |
| Profile | `userName`, `fullName`, `email`, `gender`, `dateOfBirth`, `interestedIn`, `country`, `state`, `city`, `zipCode`, `profilePicture`, `bodyType`, `drinking`, `eyes`, `hair`, `height`, `interests`, `lookingFor`, `smoking`, `aboutYou`, `title`, `weight`, `whatsUp` |

### Files
- `FileProvider` (`com.friendfinapp.dating.fileprovider`) with `xml/file_paths`.
- Image cropper temp output.
- Audio recordings created by `AudioRecorder` in `feature:chat-message:chat-room`.

---

## 11. Authentication & Authorization Flow

### Login Flow

```
SplashNavScreen
    │
    │  reads SpKey.loginStatus + token expiry
    ▼
 ┌─────────────────────────┐        Logged in?
 │     LoginNavScreen      │◀───────────────── NO
 │  (email/password OR     │
 │   Google Sign-In)       │
 └─────────────────────────┘
            │  PostLoginApiUseCase / PostGoogleLoginApiUseCase
            │  → AuthRepoImpl  → /api/Auth/v1/Login
            ▼
   On Success:
     SpKey.loginStatus = true
     SpKey.authToken / refreshToken / tokenExpireAt persisted
            │
            ▼
   FetchProfileApiUseCase   ───────▶  HomeNavScreen
```

### Token Refresh (401 Handling)
1. Every request gets `Authorization: Bearer <SpKey.authToken>` from `OkHttpModule`.
2. On HTTP `401`, OkHttp invokes `AuthenticationRefreshToken.authenticate(...)`:
   1. Calls `AuthRefreshApiService.refreshToken({ refreshToken })` synchronously.
   2. On 200, persists the new `authToken` / `refreshToken` / `expireAt` and replays the request with the new token.
   3. On failure, returns `null` → original 401 propagates → repository emits `ApiResult.Error(401)` → UI routes back to login.

### Authorization
There is **no role-based authorization** client-side; the backend authorises by token. A premium/non-premium distinction is maintained via:
- `AppConstants.isPremiumUser` (set in `BaseApplication.checkActivePurchases` and re-queried on resume).
- Used to gate app-open ads and unlock VIP-only features in the VIP membership screen.

### Google Sign-In
`GoogleSignInManager` (in `core:common`) uses Google Credential Manager + `AppConstants.WEB_CLIENT_ID` to obtain an account, then calls `LoginWithGoogle?email=...`. If the server responds with `isUserExist = false`, the UI navigates to `RegistrationNavScreen(email, displayName)`.

### Logout
`PostLogoutApiUseCase` clears server-side session; UI then clears `SharedPrefHelper` and navigates to `LoginNavScreen`.

---

## 12. Testing Strategy

> The current test suite is the auto-generated AGP scaffold (`ExampleUnitTest`, `ExampleInstrumentedTest`) per module. The infrastructure to run tests is fully configured; meaningful coverage has not yet been written. This section documents the *intended* strategy.

### Test Layers

| Layer | Tools | Targets |
|-------|-------|---------|
| **Unit (JVM)** | JUnit 4, MockK / Mockito, kotlinx-coroutines-test, Turbine | Use cases, ViewModels, mappers, validators |
| **Instrumented** | AndroidX Test Ext, Espresso, Compose UI Test, Hilt testing | Screen-level Compose tests, Room DAO, integration |
| **End-to-end** | Manual via Play Console internal track | Full flows on real devices |

### Commands
```bash
# All JVM tests
./gradlew testDebugUnitTest

# A single module
./gradlew :feature:auth:login:testDebugUnitTest

# All instrumented tests (needs a running device/emulator)
./gradlew connectedDebugAndroidTest
```

### Conventions When Adding Tests
- Place under `src/test/java/...` (JVM) or `src/androidTest/java/...` (instrumented), matching the package of the SUT.
- For ViewModels: collect `uiState` & `uiEvent` with Turbine; replace use cases with fakes that emit deterministic `ApiResult` flows.
- For repositories: use a fake `ApiService` returning canned `Response<T>` to exercise `NetworkBoundResource` happy- and error-paths.

---

## 13. Coding Conventions & Best Practices

- **Kotlin style:** `kotlin.code.style=official` (Kotlin coding conventions).
- **JVM target:** 17 across all modules (`configureKotlinAndroid` in `KotlinAndroid.kt`).
- **Compiler opt-ins** (set globally): `kotlin.RequiresOptIn`, `ExperimentalCoroutinesApi`, `FlowPreview`.
- **Module boundaries:**
   - `:feature:*` → may depend on `:core:domain`, `:core:common`, `:core:design-system`, `:core:ui`, `:core:sharedpref`, `:core:di`, `:core:model:entity`. **No feature module depends on another.**
   - `:core:data` is the only module that knows about Retrofit / DTOs.
   - `:core:domain` is pure Kotlin/JVM where possible.
- **Naming:**
   - Screen entry: `<Feature>ScreenRoute` (stateful), `<Feature>Screen` (stateless).
   - VM: `<Feature>ViewModel` (`@HiltViewModel`).
   - State / intent / event: `UiState`, `UiAction`, `UiEvent` per screen.
   - Use cases: `<Verb><Subject>ApiUseCase` (e.g. `PostLoginApiUseCase`, `FetchOtherProfileApiUseCase`).
- **DI:** prefer constructor injection. `@Singleton` only for app-wide collaborators (DB, OkHttp, Retrofit, repositories, helpers).
- **Threading:** all I/O happens on `Dispatchers.IO` inside `NetworkBoundResource`. UI collects on the Main dispatcher (`collectAsStateWithLifecycle`).
- **Resources:** all string/dimens/color resources live in `:core:design-system`; reference via `com.friend.designsystem.R`. Feature-specific resources go in that feature module.
- **Compose:** stateless composables receive `state` and `onAction`. Avoid passing the ViewModel down the tree.
- **No new XML layouts** for new screens — Compose only.
- **Avoid mutable global state.** `AppConstants.isPremiumUser` and the `BodyTypes` / `Eyes` / `Height` lists are legacy globals; new state should live in a ViewModel or a repository.

---

## 14. CI/CD Pipeline

> No `.github/workflows`, Bitrise, or other CI configuration is present in the repository at this time. The information below describes the *expected* configuration that the project's tooling already supports.

Suggested pipeline (GitHub Actions example):

```yaml
name: Android CI
on:
  pull_request:
    branches: [ main, development ]
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { distribution: 'temurin', java-version: '17' }
      - uses: gradle/actions/setup-gradle@v3
      - run: echo "${{ secrets.GOOGLE_SERVICES_JSON }}" > app/google-services.json
      - run: ./gradlew lintDebug testDebugUnitTest :app:assembleDebug
```

For Play Store delivery use [`r0adkll/upload-google-play`](https://github.com/r0adkll/upload-google-play) with `bundleRelease` artifacts. Crashlytics symbol upload is wired automatically by the `FirebaseConventionPlugin` (`mappingFileUploadEnabled = true`).

---

## 15. Known Limitations & Future Improvements

### Active Backlog (`TO-DO.txt`)
1. **Update SDK & tooling** — keep Android SDK / Gradle / Billing on the latest stable.
2. **Remove interstitial-ads code** — a wrong interstitial ad ID is currently in `settings.xml`; remove all interstitial code for performance.
3. **Fix slow “Upload photo” & duplicate uploads** — debounce submits and stream uploads.
4. **WhatsApp-style message dates & times** — show date headers for received messages and timestamps per bubble (likely needs API support).
5. **Top-right chat menu** — add search, move "Report user" under it, scope search to the displayed window (currently the latest 500 messages, configurable).

### Architecture / Code-Health Limitations

| Area | Limitation |
|------|------------|
| Local cache | `:core:cache` exists but is **not wired into `:app`**. The app re-fetches everything on each launch. |
| Globals | `AppConstants` holds mutable globals (e.g. `isPremiumUser`, lookup lists) that should move into repositories. |
| Hardcoded credentials | Release keystore alias & passwords are checked in to `app/build.gradle.kts` — should move to env vars / `~/.gradle/gradle.properties`. |
| API base URL provider | Currently expected to be added in `:app` Hilt module; some providers in `ApplicationModule.kt` are commented out. |
| RxJava 3 | Pulled in via the Retrofit RxJava3 adapter but no longer used by new code — can be removed once legacy code is gone. |
| Logging interceptor | Always at `Level.BODY`, even in release. Switch to `Level.NONE` for release builds. |
| Cleartext traffic | `usesCleartextTraffic="true"` and a permissive `network_security_config.xml` — should be tightened for production. |
| Module name typo | `:feature:auth:registraion` (missing "t") — fix carefully across `settings.gradle.kts`, project file paths and Gradle accessors. |
| Tests | Only AGP-scaffold `ExampleUnitTest` files exist; meaningful coverage to be added. |
| Realtime chat | All chat is request/response over REST. WebSocket / FCM-driven updates would improve UX. |
| Pagination | `DATA_PER_PAGE = 50`; no Paging 3 integration — manual `pageNo` plumbing. |

### Suggested Next Steps
- Switch to `EncryptedSharedPreferences` for `authToken` / `refreshToken`.
- Move version & SDK config into the version catalog (`libs.versions.toml`) so `AppConfig.kt` is the only Kotlin spot.
- Introduce `Result.runCatching`/`flow.catch` pipelines in repos so individual API errors don't have to be re-emitted manually.
- Add a baseline-profile-generating module (the `app/release/baselineProfiles/` directory already exists).
- Add ktlint / detekt + a `pre-commit` Git hook.

---

## 16. Contribution Guidelines

### Branching
- `main` — production-ready. Tags should match `versionName`.
- `development` — integration branch (merged into `main` for releases — see git log).
- `hotfix` — out-of-band fixes against `main`.
- Feature work: `feature/<short-slug>` cut from `development`.
- Bug fixes: `fix/<short-slug>` cut from `development` (or `hotfix` for production issues).

### Commit Messages
Follow the existing convention visible in `git log`:

```
fix(AppConfig): increment version code and version name
feat(chat-room): add message search bottom sheet
refactor(core:data): extract mappers to dedicated files
```

`type(scope): short imperative summary`. Types in use: `feat`, `fix`, `refactor`, `chore`, `docs`.

### Pull Requests
1. Rebase onto the latest `development`.
2. Ensure `./gradlew lintDebug testDebugUnitTest :app:assembleDebug` passes locally.
3. Include screenshots / screen recordings for any UI change.
4. Reference the relevant `TO-DO.txt` item or issue ticket.
5. Keep PRs single-purpose where possible — split refactors from feature work.

### Adding a New Feature Module
1. Create the directory: `feature/<group>/<name>/`.
2. Add the module to `settings.gradle.kts`:
   ```kotlin
   include(":feature:<group>:<name>")
   ```
3. Create `feature/<group>/<name>/build.gradle.kts`:
   ```kotlin
   plugins { alias(libs.plugins.android.features) }
   android { namespace = "com.friend.<name>" }
   ```
   The `android.features` convention plugin (`FeatureConventionPlugin`) brings in DI, Compose, Material, lifecycle, navigation, common test deps automatically.
4. Add the module to the `:app` `dependencies` block.
5. Add a NavKey under `AppNavKey.kt` and register the entry in the relevant `*NavGraph.kt`.
6. Use the `<Feature>ScreenRoute / <Feature>Screen / <Feature>ViewModel / UiState` template (copy `feature/auth/login` as a starting point).

### Code Review Checklist
- New screens follow the MVI-style state/action/event split.
- Every API call goes through a `*ApiUseCase` and `NetworkBoundResource`.
- No hard-coded base URLs or secrets in code.
- No raw `Activity` / `Fragment` introductions — Compose-only.
- Public Compose APIs are stateless and previewable (`@Preview` in `core:ui:preview` when reusable).
- No new dependencies without an entry in `libs.versions.toml`.

---

## 17. Appendix: Module Reference

### Convention Plugins (`build-logic`)
| Plugin id | Class | Applies |
|-----------|-------|---------|
| `android.application` | `ApplicationConventionPlugin` | `com.android.application` + Kotlin Android config |
| `android.library` | `LibraryConventionPlugin` | `com.android.library` + Kotlin Android + test deps |
| `android.library.compose` | `LibraryComposeConventionPlugin` | Adds Compose to a library |
| `android.compose.application` | `ComposeConventionPlugin` | Adds Compose to the app module |
| `android.features` | `FeatureConventionPlugin` | The "feature module" preset (library + compose + hilt + standard deps) |
| `android.hilt` | `HiltConventionPlugin` | Hilt + KSP |
| `android.firebase` | `FirebaseConventionPlugin` | google-services + crashlytics + Firebase BOM/bundle |
| `android.room` | `RoomConventionPlugin` | Room + KSP |
| `jvm.library` | `JvmLibraryConventionPlugin` | Pure-Kotlin JVM library |

### Module Snapshot
| Module | Type | Purpose |
|--------|------|---------|
| `:app` | application | App shell, MainActivity, navigation, app-level DI |
| `:core:common` | library | AppConstants, BaseViewModel, extensions, Billing/Google helpers, date utils |
| `:core:design-system` | library | Theme, colors, typography, drawables, strings |
| `:core:ui` | library | Shared Composables, dialogs, shimmer, banner ads, top bar |
| `:core:di` | library | OkHttp/Retrofit modules, qualifiers, refresh-token authenticator |
| `:core:domain` | library | Use cases, repository contracts, validators, `ApiResult` |
| `:core:data` | library | Retrofit services, repository implementations, mappers, NetworkBoundResource |
| `:core:sharedpref` | library | `SharedPrefHelper`, `SpKey` |
| `:core:cache` | library | Room AppDatabase + DAOs (currently unwired) |
| `:core:model:apiresponse` | library | API DTOs |
| `:core:model:entity` | library | Domain entities |
| `:feature:auth:splash-screen` | feature | Splash + auto-routing |
| `:feature:auth:login` | feature | Email/Google login |
| `:feature:auth:registraion` | feature | Registration |
| `:feature:auth:forgot-password` | feature | Forgot password |
| `:feature:home` | feature | Suggestion feed, filters |
| `:feature:chat-message:chat-list` | feature | Conversations list |
| `:feature:chat-message:chat-room` | feature | Conversation screen + audio recorder/player |
| `:feature:chat-message:forward-message` | feature | Forward selected messages |
| `:feature:profile-manager:profile-overview` | feature | Logged-in user dashboard |
| `:feature:profile-manager:my-profile` | feature | Edit own profile |
| `:feature:profile-manager:other-profile` | feature | View other users |
| `:feature:profile-manager:profile-completion` | feature | New-user wizard |
| `:feature:profile-manager:personal-setting` | feature | Settings, account actions |
| `:feature:profile-manager:change-password` | feature | Password change |
| `:feature:profile-manager:vip-membership` | feature | Subscription purchase |
| `:feature:common:privacy-policy` | feature | Static policy screen |
| `:feature:common:report-abuse` | feature | Abuse report form |
| `:feature:common:video-player` | feature | ExoPlayer-based video player |

---

*Last updated: 2026-06-29.*

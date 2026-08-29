# Login Screen with Firebase Phone and Google Sign-In

This plan covers the implementation of a login screen using Firebase Authentication. It supports both Phone Number (SMS-based) and Google Sign-In (via Credential Manager).

## User Review Required

> [!IMPORTANT]
> - **SHA-1 Fingerprint**: Google Sign-In and Phone Auth (for app verification) require the SHA-1 fingerprint of your signing certificate to be registered in the Firebase Console.
> - **Phone Auth Quota**: Firebase Phone Auth has a free tier and requires SMS credits/billing for high usage.
> - **Google Cloud Console**: Ensure the "Google People API" is enabled in your Google Cloud Project to fetch user profiles.

## Proposed Changes

### Dependencies & Configuration

#### [MODIFY] [libs.versions.toml](file:///Users/hellotechnologies/StudioProjects/MyFinance/gradle/libs.versions.toml)
Add versions and library definitions for Firebase Auth, Play Services Auth, and Credentials.

#### [MODIFY] [build.gradle.kts](file:///Users/hellotechnologies/StudioProjects/MyFinance/app/build.gradle.kts)
Add the new dependencies to the `app` module.

---

### Authentication Logic

#### [NEW] [AuthManager.kt](file:///Users/hellotechnologies/StudioProjects/MyFinance/app/src/main/java/com/mathi/finance/AuthManager.kt)
A utility class to handle Firebase Authentication logic, including Google Sign-In and Phone Number verification.

#### [NEW] [AuthViewModel.kt](file:///Users/hellotechnologies/StudioProjects/MyFinance/app/src/main/java/com/mathi/finance/AuthViewModel.kt)
A ViewModel to manage the UI state and coordinate between the UI and `AuthManager`.

---

### User Interface

#### [NEW] [LoginScreen.kt](file:///Users/hellotechnologies/StudioProjects/MyFinance/app/src/main/java/com/mathi/finance/LoginScreen.kt)
The Compose-based login screen with fields for phone number, OTP input, and a Google Sign-In button.

#### [MODIFY] [MainActivity.kt](file:///Users/hellotechnologies/StudioProjects/MyFinance/app/src/main/java/com/mathi/finance/MainActivity.kt)
Update the entry point to display the `LoginScreen` or the main app content based on the authentication state.

## Verification Plan

### Automated Tests
- N/A (Focusing on UI and integration logic first).

### Manual Verification
1.  **Google Sign-In**: Click the "Sign in with Google" button and verify a successful login.
2.  **Phone Auth**:
    - Enter a phone number.
    - Click "Send OTP".
    - Enter the received OTP.
    - Click "Verify OTP" and verify successful login.
3.  **Persistence**: Close and reopen the app to ensure the user stays logged in.

# Fix Application Instantiation Error

The application is failing to start with a `java.lang.RuntimeException: Unable to instantiate application com.mathi.finance.MyFinanceApp`. This usually indicates a mismatch between the `AndroidManifest.xml` declaration and the actual class, or a build configuration issue that prevents the class from being correctly instantiated.

## Proposed Changes

### Build Configuration
#### [MODIFY] [app/build.gradle.kts](file:///Users/hellotechnologies/StudioProjects/MyFinance/app/build.gradle.kts)
- Update `compileSdk` and `targetSdk` to stable version `35` (Android 15).
- Correct the `buildTypes` block to use standard AGP properties (`isMinifyEnabled` instead of the non-standard `optimization` block).
- Ensure the Kotlin Android plugin is explicitly applied if missing (though the successful build suggests it might be applied transitively, it's safer to be explicit).

### Manifest
#### [MODIFY] [AndroidManifest.xml](file:///Users/hellotechnologies/StudioProjects/MyFinance/app/src/main/AndroidManifest.xml)
- Add the `package` attribute to the `<manifest>` tag to ensure correct resource and class resolution.
- Use the fully qualified name for the `Application` class in `android:name` to avoid resolution issues during manifest merging.

### Application Class
#### [MODIFY] [MyFinanceApp.kt](file:///Users/hellotechnologies/StudioProjects/MyFinance/app/src/main/java/com/mathi/finance/MyFinanceApp.kt)
- Add explicit `public` visibility to the class.
- Add an explicit public no-arg constructor to ensure the Android system can always instantiate it.

## Verification Plan
### Automated Tests
- Run `gradlew assembleDebug` to ensure the project still builds correctly.
- Deploy the app to the emulator and check the logcat for successful Koin initialization.

### Manual Verification
- Verify that the app starts without the `FATAL EXCEPTION: main` related to `Unable to instantiate application`.

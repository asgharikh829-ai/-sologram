# Sologram Android client

This repository is a branded Android client based on the Telegram for Android source.

## Current branding

- App name: Sologram
- Package ID: `com.sologram.messenger`
- Version: `1.0.0`
- Launcher artwork: `TMessagesProj/src/main/res/drawable/sologram_launcher.xml`

## Cloud build

GitHub Actions builds the release APK with the workflow:

`.github/workflows/build-sologram.yml`

Open the repository's **Actions** tab and select **Build Sologram APK** to run it manually. The resulting `Sologram-Android-APK` artifact contains the installable APK.

The workflow creates a temporary signing key for each run. This is suitable for initial testing only: a later build will have a different signature and cannot update the earlier install. Before distributing Sologram or publishing updates, configure a persistent Sologram-owned signing key using private GitHub Actions secrets.

## Services that need Sologram-owned credentials

Telegram-owned Firebase and Google Maps configuration is intentionally not used by this fork. Push notifications and Google Maps features need credentials from Sologram-owned Firebase and Google Cloud projects.

The Telegram API ID/hash in `TMessagesProj/src/main/java/org/telegram/messenger/BuildVars.java` are still upstream defaults for initial development. Register Sologram at https://my.telegram.org/apps and replace them with the credentials issued for your app before public distribution. Do not publish private signing keys or other secrets in this repository.

## License

This project is derived from Telegram for Android. Preserve the upstream GNU GPL v2-or-later licensing and provide the corresponding source when distributing modified binaries.

## API credentials

Configure `SLOGRAM_API_ID` and `SLOGRAM_API_HASH` as GitHub Actions repository secrets before running the Android APK workflow. The credentials are injected at build time and are not committed to source control.

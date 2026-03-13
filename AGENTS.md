# AGENTS.md

This file provides instructions for automation agents working on this repository.

## Project summary
- MateDroid is a native Android app (Kotlin, Jetpack Compose) for viewing Tesla data from a self-hosted Teslamate instance via TeslamateApi.
- Primary code: app/src/main/java/com/matedroid/
- Key docs: docs/DEVELOPMENT.md, docs/ASSETS.md, README.md, CHANGELOG.md

## Key commands
- Build debug APK: ./gradlew assembleDebug
- Unit tests: ./gradlew testDebugUnitTest (or ./gradlew test)
- Lint: ./gradlew lintDebug
- Install to device: ./gradlew installDebug (or make install)
- Make targets: make build, make install, make run, make test, make clean
- Version code bump: ./scripts/bump-version-code.sh

## Development rules
- Create a new branch and PR for each feature or bugfix.
- Commit meaningful changes with short, clear messages.
- Keep README.md up to date but do not change its writing style.
- Update docs/DEVELOPMENT.md and docs/ASSETS.md if related changes are made.
- Do not create releases unless explicitly instructed. Do not release major versions without confirmation.
- Code comments must be in English.

## Localization
- Never hardcode user-visible strings in Kotlin. Always use string resources and stringResource().
- Add new strings to all 4 locales:
  - app/src/main/res/values/strings.xml (English)
  - app/src/main/res/values-it/strings.xml (Italian)
  - app/src/main/res/values-es/strings.xml (Spanish)
  - app/src/main/res/values-ca/strings.xml (Catalan)
- Use snake_case for string names and add XML comments for translators.
- Do not translate technical terms like AC, DC, kW, kWh.
- Run ./gradlew lintDebug before committing.

## Charts and graphs
- Histogram bars must be tappable and show a tooltip with the value on tap.
- Histogram bars use the palette accent color.
- Line graphs must have Y-axis labels: 1st quarter, half, 3rd quarter, end.
- Line graphs must have X-axis labels: start, 1st quarter, half, 3rd quarter, end.
- Tapping a line graph point shows a tooltip with the Y value.

## Teslamate API gotchas
- Always verify the local Teslamate API response format when needed.
- parseDateParam accepts only:
  - RFC3339: 2024-12-07T00:00:00Z
  - DateTime: 2024-12-07 00:00:00
- TeslamateApi pre-converts values to the user's unit system.
  - Do not convert distance, speed, temperature, efficiency, or pressure.
  - UnitFormatter must only attach unit labels.

## Releases and metadata
- F-Droid recipe reference: fdroid/com.matedroid.yml
- Fastlane metadata: fastlane/metadata/android/{locale}/
  - en-US, it-IT, es-ES, ca-ES
  - title.txt, short_description.txt, full_description.txt, changelogs/{versionCode}.txt
- For a new version, create changelog files in all locales.
- Use the release workflow/skill to handle version bumps and translations.

## Built-in workflows (Claude skills)
If using Claude Code, see .claude/skills:
- feature: full feature workflow with tests, PR, and install on device
- fix: issue fix workflow with tests, PR, and merge
- release: version bump + changelog + fastlane metadata
- new-screen: scaffold a new Compose screen
- translate: add localized strings
- check-api: inspect Teslamate API JSON responses
- mock: run mockserver with car profiles

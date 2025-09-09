# friednFinDatingApp

# New app work

1. **Update SDK & tooling** — Update to the latest Android SDK, Gradle, Billing, etc.
2. **Remove interstitial ads code** — Currently a wrong interstitial ad ID is in `settings.xml`; remove all ad code for performance.
3. **Fix slow “Upload photo” & duplicate uploads** — Prevent multiple submissions and improve upload speed.
4. **Show message date & time (WhatsApp-style)** — Display dates for received messages and time with each message; may need API changes (coordinate with Raihan).
5. **Top-right chat menu**
    * Add **Search** (likely needs API support; check with Raihan).
    * Move **Report User** under this menu.
    * Search **only** within displayed messages (currently the latest 500; configurable).

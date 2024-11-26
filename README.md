# Bear Metal Scouting (but for prototyping 2024)

![Bear Metal Logo](composeApp/src/commonMain/resources/bearmetallogo.jpg)

## Scouting App info

The Bear Metal Scouting App is a multiplatform application for Desktop, Android, ~~and hopefully iOS~~.

## How to Set up Webhooks

1. **Create a Webhook in Discord**

- Open Discord and navigate to the server where you want to create the webhook.
- Click on the server name in the top-left corner to open the server settings.
- In the server settings, find and click on the "Integrations" tab.
- Click on "Webhooks".
- Click on the "Create Webhook" button.
- Customize the webhook by selecting a channel where it will post scorecards and optionally add a picture. The name of
  the webhook is dynamically changed in the code.
- Click the "Copy Webhook URL" button to copy the webhook URL to your clipboard.

2. **Integrate the Webhook URL into `matchmenu.kt`**

   Open `matchmenu.kt` and replace `YOUR_WEBHOOK_URL` with the webhook URL you copied from Discord.

   ```kotlin
   val webhook = DiscordWebhook("YOUR_WEBHOOK_URL")
   ```

3. **Done!**

   Now compile and run the app!
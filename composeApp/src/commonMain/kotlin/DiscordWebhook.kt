import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nodes.scoutName
import java.net.HttpURLConnection
import java.net.URL

/**
 * Class responsible for sending messages to a Discord webhook.
 *
 * @property url The URL of the Discord webhook.
 */
class DiscordWebhook(private val url: String) {
    /**
     * Sends the scorecard information to a specified webhook URL.
     *
     * @param scorecard The scorecard object that contains the scoring details to be sent.
     */
    fun sendToWebhook(scorecard: Scorecard) {
        // Launch a coroutine on the IO dispatcher
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val message =
                    "I just got ${scorecard.totalScore} points. Of those points, ${scorecard.bonusScore} were bonus points and ${scorecard.totalScore - scorecard.bonusScore + scorecard.penaltyScore} were from investing. I lost ${scorecard.penaltyScore} points due to penalties${if (scorecard.penaltyScore > 0) "." else "! Yay!"}"
                val webhookUrl = URL(this@DiscordWebhook.url)
                val connection = webhookUrl.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                val jsonPayload = constructJsonPayload(message, scorecard)
                connection.outputStream.use { os ->
                    val input = jsonPayload.toByteArray()
                    os.write(input, 0, input.size)
                }

                connection.responseCode // To actually send the request
                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Constructs a JSON payload string based on the provided message and scorecard.
     *
     * @param message The content message to be included in the JSON payload.
     * @param scorecard An instance of the Scorecard data class that contains the score details.
     * @return A JSON string representing the payload with the message and scorecard information.
     */
    private fun constructJsonPayload(message: String, scorecard: Scorecard): String {
        val color = (0..16777215).random()
        return """{
        |"content":"$message",
        |"username":"${scorecard.teamName}",
        |"embeds": [
        |   {
        |       "author": { "name": "${scorecard.teamName}", "icon_url": "https://picsum.photos/100/100" },
        |       "color": $color,
        |       "fields": [
        |           { "name": "Total Score", "value": "${scorecard.totalScore}" },
        |           { "name": "Penalty Score", "value": "${scorecard.penaltyScore}" },
        |           { "name": "Bonus Score", "value": "${scorecard.bonusScore}" },
        |           { "name": "Amount Invested", "value": "${scorecard.totalScore - scorecard.bonusScore + scorecard.penaltyScore}" },
        |           { "name": "High Goal", "value": "${scorecard.highGoalAmount} Assets" },
        |           { "name": "Middle Goal", "value": "${scorecard.midGoalAmount} Assets" },
        |           { "name": "Low Goal", "value": "${scorecard.lowGoalAmount} Assets" }
        |       ],
        |       "footer": { "text": "Score submitted by ${scoutName.value}" }
        |   }
        |]}""".trimMargin()
    }
}

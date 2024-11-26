/**
 * Represents the scorecard for a team in a match.
 *
 * @property teamName The name of the team.
 * @property matchNumber The number of the match (currently not used).
 * @property totalScore The total score accumulated by the team.
 * @property penaltyScore The score deducted due to penalties.
 * @property bonusScore The score added as bonuses.
 * @property highGoalAmount The number of assets in the high goal.
 * @property midGoalAmount The number of assets in the mid goal.
 * @property lowGoalAmount The number of assets in the low goal.
 */
data class Scorecard(
    val teamName: String,
    val matchNumber: Int,
    var totalScore: Double,
    var penaltyScore: Int,
    var bonusScore: Int,
    var highGoalAmount: Int,
    var midGoalAmount: Int,
    var lowGoalAmount: Int
)
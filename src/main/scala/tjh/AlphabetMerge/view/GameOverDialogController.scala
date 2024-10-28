package tjh.AlphabetMerge.view

import scalafx.scene.control.TextField
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import tjh.AlphabetMerge.MainApp
import tjh.AlphabetMerge.model.PlayerScore

import scala.util.{Failure, Success}

@sfxml
class GameOverDialogController(
                                private val nameField: TextField
                              ) {

  var dialogStage: Stage = null
  var currentScore: Int = -1

  def handleSubmit(): Unit = {
    var playerName = nameField.text.value.trim
    val playerScoreValue = currentScore

    // If the name is empty, replace it with "Anonymous"
    if (playerName.isEmpty) {
      playerName = "Anonymous"
    }

    val playerScore = PlayerScore(playerName, playerScoreValue)
    playerScore.save() match {
      case Success(_) => println(s"Player score saved: $playerName - $playerScoreValue")
      case Failure(ex) => println(s"Error saving player score: ${ex.getMessage}")
    }

    dialogStage.close() // Close the dialog and continue the game
    MainApp.startGame() // Optionally reset the game after submission
  }
}
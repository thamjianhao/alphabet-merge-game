package tjh.AlphabetMerge.view

import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import tjh.AlphabetMerge.MainApp

@sfxml
class GameWinDialogController {
  var dialogStage : Stage = null

  def handleContinue(): Unit = {
    dialogStage.close() // Close the dialog and continue the game
  }

  def handleNewGame(): Unit = {
    dialogStage.close() // Close the dialog and continue the game
    MainApp.startGame() // Start a new game
  }
}
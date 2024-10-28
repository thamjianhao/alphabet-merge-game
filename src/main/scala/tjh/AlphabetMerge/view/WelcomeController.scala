package tjh.AlphabetMerge.view

import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml
import tjh.AlphabetMerge.MainApp

@sfxml
class WelcomeController {
  // Method to handle starting the game
  def handleStartGame(event: ActionEvent): Unit = {
    MainApp.startGame()
  }

  // Method to handle showing the leaderboard
  def getLeaderboard(event: ActionEvent): Unit = {
    MainApp.showLeaderboard()
  }

  // Method to handle quitting the game
  def handleQuitGame(event: ActionEvent): Unit = {
    MainApp.stage.close()
  }

}
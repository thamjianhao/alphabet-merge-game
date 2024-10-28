package tjh.AlphabetMerge.view

import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml
import tjh.AlphabetMerge.MainApp
import tjh.AlphabetMerge.util.SoundManager

@sfxml
class RootLayoutController {
  def handleStartGame(event: ActionEvent): Unit = {
    MainApp.startGame()
  }

  // Method to handle showing the leaderboard
  def handleLeaderboard(event: ActionEvent): Unit = {
    MainApp.showLeaderboard()
  }

  def handleWelcome(event: ActionEvent): Unit = {
    MainApp.showWelcome()
  }

  // Method to handle quitting the game
  def handleQuitGame(event: ActionEvent): Unit = {
    MainApp.stage.close()
  }

  def turnMusicOn(event: ActionEvent): Unit = {
    if (!SoundManager.isBackgroundMusicPlaying) {
      SoundManager.initialize()
    }
  }

  def turnMusicOff(event: ActionEvent): Unit = {
    SoundManager.stopBackgroundMusic()
  }
}

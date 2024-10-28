package tjh.AlphabetMerge

import javafx.{scene => jfxs}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.stage.Stage
import tjh.AlphabetMerge.util.{Database, SoundManager}
import tjh.AlphabetMerge.view.{GameController, GameOverDialogController, GameWinDialogController}

object MainApp extends JFXApp {
  // Initialize database and sound manager
  Database.setupDB()
  SoundManager.initialize()

  val rootResource = getClass.getResource("view/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "Alphabet Merge"
    scene = new Scene {
      root = roots
    }
  }

  showWelcome()

  def showWelcome(): Unit = {
    val resource = getClass.getResource("view/Welcome.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showLeaderboard(): Unit = {
    val resource = getClass.getResource("view/Leaderboard.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def startGame(): Unit = {
    val resource = getClass.getResource("view/Game.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)

    // Get the controller instance and pass the high score
    val controller = loader.getController[GameController#Controller]()

    roots.requestFocus()

    // Capture key events and delegate to the controller
    stage.scene().onKeyPressed = (event: KeyEvent) => {
      event.code match {
        case KeyCode.Left => controller.moveLeft()
        case KeyCode.Right => controller.moveRight()
        case KeyCode.Up => controller.moveUp()
        case KeyCode.Down => controller.moveDown()
        case _ => // do nothing
      }
    }
  }

  def showGameOver(currentScore: Int): Unit = {
    val resource = getClass.getResource("view/GameOverDialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val dialogPane = loader.getRoot[jfxs.layout.AnchorPane]
    val control = loader.getController[GameOverDialogController#Controller]

    val dialogStage = new Stage {
      title = "Game Over"
      scene = new Scene {
        root = dialogPane
      }
      initOwner(stage)
    }

    control.dialogStage = dialogStage
    control.currentScore = currentScore
    dialogStage.showAndWait()
  }

  def showGameWin(): Unit = {
    val resource = getClass.getResource("view/GameWinDialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val dialogPane = loader.getRoot[jfxs.layout.AnchorPane]
    val control = loader.getController[GameWinDialogController#Controller]

    val dialogStage = new Stage {
      title = "You Win!"
      scene = new Scene {
        root = dialogPane
      }
      initOwner(stage)
    }

    control.dialogStage = dialogStage
    dialogStage.showAndWait()
  }
}
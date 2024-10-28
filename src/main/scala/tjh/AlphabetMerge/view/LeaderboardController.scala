package tjh.AlphabetMerge.view

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}
import scalafxml.core.macros.sfxml
import tjh.AlphabetMerge.model.PlayerScore

@sfxml
class LeaderboardController(
                                   private val tableView: TableView[PlayerScore],
                                   private val nameColumn: TableColumn[PlayerScore, String],
                                   private val scoreColumn: TableColumn[PlayerScore, String]
                                 ) {

  // Initialize the controller
  initialize()

  private def initialize(): Unit = {
    // Configure the table columns to use the PlayerScore properties

    nameColumn.cellValueFactory = { cellData =>
      cellData.value.name
    }

    scoreColumn.cellValueFactory = { cellData =>
      cellData.value.score
    }

    // Fetch all player scores from the database and set them in the table
    val topScores = PlayerScore.getAllPlayerScores
    tableView.items = ObservableBuffer(topScores)
  }
}
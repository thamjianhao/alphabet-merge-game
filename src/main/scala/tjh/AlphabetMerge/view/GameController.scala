package tjh.AlphabetMerge.view

import scala.util.Random
import scalafx.scene.control.Label
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import tjh.AlphabetMerge.MainApp
import tjh.AlphabetMerge.model.PlayerScore
import tjh.AlphabetMerge.util.SoundManager

@sfxml
class GameController(
                      private var cell00: Label,
                      private var cell01: Label,
                      private var cell02: Label,
                      private var cell03: Label,
                      private var cell10: Label,
                      private var cell11: Label,
                      private var cell12: Label,
                      private var cell13: Label,
                      private var cell20: Label,
                      private var cell21: Label,
                      private var cell22: Label,
                      private var cell23: Label,
                      private var cell30: Label,
                      private var cell31: Label,
                      private var cell32: Label,
                      private var cell33: Label,
                      private var scoreLabel: Text,
                      private var bestLabel: Text
                    ) {

  private val alphabet = ('A' to 'K').toList // Letters from A to K
  private var board = Array.ofDim[Char](4, 4)
  private var currentScore: Int = 0
  private var hasWon: Boolean = false

  // Initialize the game
  initialize()

  def initialize(): Unit = {
    // Initialize and reset the game board
    board = Array.fill(4, 4)(' ')
    currentScore = 0
    hasWon = false

    // Get the best score from the database
    val bestScore = PlayerScore.getAllPlayerScores.headOption.map(_.score.value).getOrElse(0)
    bestLabel.text = bestScore.toString

    // Add two random tiles to start the game
    addRandomTile()
    addRandomTile()
    updateUI()
  }

  def moveLeft(): Unit = {
    var moved = false
    for (row <- board) {
      val filteredRow = row.filter(_ != ' ')
      val mergedRow = mergeTiles(filteredRow)
      if (!row.sameElements(mergedRow ++ Array.fill(4 - mergedRow.length)(' '))) {
        moved = true
        Array.copy(mergedRow ++ Array.fill(4 - mergedRow.length)(' '), 0, row, 0, 4)
      }
    }
    if (moved) {
      addRandomTile()
      updateUI()
      if (!hasWon && checkGameWin()) return
      if (checkGameOver()) {
        SoundManager.playGameOverSound() // Play game over sound
        MainApp.showGameOver(currentScore)
      } else {
        SoundManager.playMoveSound()
      }
    }
  }

  def moveRight(): Unit = {
    for (i <- 0 until 4) {
      board(i) = board(i).reverse
    }
    moveLeft()
    for (i <- 0 until 4) {
      board(i) = board(i).reverse
    }
    updateUI()
  }

  def moveUp(): Unit = {
    board = board.transpose
    moveLeft()
    board = board.transpose
    updateUI()
  }

  def moveDown(): Unit = {
    board = board.transpose
    for (i <- 0 until 4) {
      board(i) = board(i).reverse
    }
    moveLeft()
    for (i <- 0 until 4) {
      board(i) = board(i).reverse
    }
    board = board.transpose
    updateUI()
  }

  private def updateUI(): Unit = {
    val cells = Array(
      Array(cell00, cell01, cell02, cell03),
      Array(cell10, cell11, cell12, cell13),
      Array(cell20, cell21, cell22, cell23),
      Array(cell30, cell31, cell32, cell33)
    )

    for (i <- 0 until 4; j <- 0 until 4) {
      val value = board(i)(j)
      cells(i)(j).text = if (value == ' ') "" else value.toString
      cells(i)(j).setStyle(s"""-fx-background-color: ${getStyleForValue(value)};""")
    }

    scoreLabel.text = currentScore.toString
  }

  private def getStyleForValue(value: Char): String = {
    value match {
      case 'A' => "#f28187"
      case 'B' => "#ec9772"
      case 'C' => "#f4b46a"
      case 'D' => "#f6c358"
      case 'E' => "#bace82"
      case 'F' => "#a2c75d"
      case 'G' => "#82c072"
      case 'H' => "#edcc61"
      case 'I' => "#77aecc"
      case 'J' => "#8185e9"
      case 'K' => "#b468e5" // Winning condition
      case _ => "#cccacb"
    }
  }

  private def addRandomTile(): Unit = {
    val emptyCells = for {
      i <- 0 until 4
      j <- 0 until 4 if board(i)(j) == ' '
    } yield (i, j)

    if (emptyCells.nonEmpty) {
      val (row, col) = emptyCells(Random.nextInt(emptyCells.size))
      board(row)(col) = if (Random.nextDouble() < 0.9) 'A' else 'B'
    }
  }

  private def mergeTiles(row: Array[Char]): Array[Char] = {
    if (row.length <= 1) row
    else if (row(0) == row(1)) {
      val mergedValue = nextLetter(row(0))
      currentScore += scoreForLetter(mergedValue)
      Array(mergedValue) ++ mergeTiles(row.drop(2))
    } else {
      Array(row(0)) ++ mergeTiles(row.drop(1))
    }
  }

  private def nextLetter(letter: Char): Char = {
    val index = alphabet.indexOf(letter)
    if (index >= 0 && index < alphabet.length - 1) alphabet(index + 1) else letter
  }

  private def scoreForLetter(letter: Char): Int = math.pow(2, alphabet.indexOf(letter) + 1).toInt

  private def checkGameOver(): Boolean = {
    // Check if there are any empty cells
    for (i <- 0 until 4) {
      for (j <- 0 until 4) {
        if (board(i)(j) == ' ') {
          return false // There is at least one empty cell, so the game is not over
        }
      }
    }

    // Check if any tiles can be merged horizontally
    for (i <- 0 until 4) {
      for (j <- 0 until 3) { // Only need to check up to the second last column
        if (board(i)(j) == board(i)(j + 1)) {
          return false // There is a horizontal merge available, so the game is not over
        }
      }
    }

    // Check if any tiles can be merged vertically
    for (i <- 0 until 3) { // Only need to check up to the second last row
      for (j <- 0 until 4) {
        if (board(i)(j) == board(i + 1)(j)) {
          return false // There is a vertical merge available, so the game is not over
        }
      }
    }

    // If no empty cells and no possible merges, the game is over
    true
  }

  private def checkGameWin(): Boolean = {
    for (i <- 0 until 4) {
      for (j <- 0 until 4) {
        if (board(i)(j) == 'K') {
          hasWon = true // Set the flag when the player wins
          SoundManager.playWinSound() // Play win sound
          MainApp.showGameWin()
          return true
        }
      }
    }
    false
  }
}
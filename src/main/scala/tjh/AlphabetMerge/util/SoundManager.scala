package tjh.AlphabetMerge.util

import javafx.scene.media.{AudioClip, Media, MediaPlayer}
import scala.util.Random

object SoundManager {
  private var mediaPlayer: Option[MediaPlayer] = None
  private var winSound: Option[AudioClip] = None
  private var gameOverSound: Option[AudioClip] = None
  private var moveSound: Option[AudioClip] = None
  private var isMusicPlaying: Boolean = false

  // List of background music tracks
  private val backgroundMusicTracks = List(
    "/sounds/BackgroundMusic1.mp3",
    "/sounds/BackgroundMusic2.mp3",
    "/sounds/BackgroundMusic3.mp3"
  )

  def initialize(): Unit = {
    if (!isMusicPlaying) {
      // Load the win sound
      winSound = Some(new AudioClip(getClass.getResource("/sounds/GameWinSoundEffect.mp3").toString))

      // Load the game over sound
      gameOverSound = Some(new AudioClip(getClass.getResource("/sounds/GameOverSoundEffect.mp3").toString))

      // Load the move sound
      moveSound = Some(new AudioClip(getClass.getResource("/sounds/MoveSoundEffect.mp3").toString))

      // Start playing random background music
      playRandomBackgroundMusic()
      isMusicPlaying = true
    }
  }

  private def playRandomBackgroundMusic(): Unit = {
    // Randomly select a background music track
    val randomTrack = Random.shuffle(backgroundMusicTracks).head
    val backgroundMusic = new Media(getClass.getResource(randomTrack).toString)
    mediaPlayer = Some(new MediaPlayer(backgroundMusic))
    mediaPlayer.foreach { player =>
      player.setOnEndOfMedia(() => playRandomBackgroundMusic()) // Play next random track after this one ends
      player.setVolume(0.25) // Set volume for background music
      player.play()
    }
  }

  def playMoveSound(): Unit = {
    moveSound.foreach { player =>
      player.setVolume(0.3) // Set volume for sound effect
      player.play()
    }
  }

  def playWinSound(): Unit = {
    winSound.foreach { player =>
      player.setVolume(0.3) // Set volume for sound effect
      player.play()
    }
  }

  def playGameOverSound(): Unit = {
    gameOverSound.foreach { player =>
      player.setVolume(0.3) // Set volume for sound effect
      player.play()
    }
  }

  def stopBackgroundMusic(): Unit = {
    mediaPlayer.foreach(_.stop())
    isMusicPlaying = false
  }

  def isBackgroundMusicPlaying: Boolean = isMusicPlaying
}
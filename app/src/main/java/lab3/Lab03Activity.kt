package pl.oleksandra.pam.lab3

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import lab3.MemoryAnimation
import lab3.MemoryBoardView
import lab3.GameStates
import pl.oleksandra.pam.lab06.R

class Lab03Activity : AppCompatActivity() {
    private lateinit var completionPlayer: MediaPlayer
    private lateinit var negativePlayer: MediaPlayer
    private val animation = MemoryAnimation()
    private lateinit var mBoard: GridLayout
    private lateinit var mBoardModel: MemoryBoardView

    // Zmienna sterująca dźwiękiem
    private var isSound: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        mBoard = findViewById(R.id.board)

        if (savedInstanceState != null) {
            val rows = savedInstanceState.getInt("rows")
            val cols = savedInstanceState.getInt("cols")
            val icons = savedInstanceState.getIntArray("icons")
            val revealed = savedInstanceState.getBooleanArray("revealed")
            // Odzyskujemy też stan dźwięku po obrocie
            isSound = savedInstanceState.getBoolean("is_sound", true)

            mBoardModel = MemoryBoardView(mBoard, cols, rows, icons)
            if (revealed != null) {
                mBoardModel.setRevealedStates(revealed)
            }
        } else {
            val rows = intent.getIntExtra("rows", 3)
            val columns = intent.getIntExtra("columns", 3)
            mBoardModel = MemoryBoardView(mBoard, columns, rows)
        }

        setupGameListener()
    }

    // Tworzenie menu w górnym pasku
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.board_activity_menu, menu)

        // Upewniamy się, że ikona w menu pasuje do stanu isSound (ważne po obrocie ekranu)
        val soundItem = menu.findItem(R.id.board_activity_sound)
        if (isSound) {
            soundItem.setIcon(R.drawable.glosnik)
        } else {
            soundItem.setIcon(R.drawable.glosnik_off)
        }
        return true
    }

    // Obsługa kliknięcia w ikonę głośnika
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.board_activity_sound) {
            if (isSound) {
                Toast.makeText(this, "Sound turned off", Toast.LENGTH_SHORT).show()
                item.setIcon(R.drawable.glosnik_off)
                isSound = false
            } else {
                Toast.makeText(this, "Sound turned on", Toast.LENGTH_SHORT).show()
                item.setIcon(R.drawable.glosnik)
                isSound = true
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupGameListener() {
        mBoardModel.setOnGameChangeListener { e ->
            mBoard.isEnabled = false

            when (e.state) {
                GameStates.Matching -> {
                    e.tiles.forEach { it.revealed = true }
                    mBoard.isEnabled = true
                }

                GameStates.Match -> {
                    // Sprawdzamy flagę isSound przed odtworzeniem
                    if (isSound) playSound(completionPlayer)

                    e.tiles.forEach { tile ->
                        tile.revealed = true
                        animation.animateMatch(tile.button) {
                            mBoard.isEnabled = true
                        }
                    }
                }

                GameStates.NoMatch -> {
                    // Sprawdzamy flagę isSound przed odtworzeniem
                    if (isSound) playSound(negativePlayer)

                    e.tiles.forEach { tile ->
                        tile.revealed = true
                        animation.animateNoMatch(tile.button) {
                            tile.revealed = false
                            mBoard.isEnabled = true
                        }
                    }
                }

                GameStates.Finished -> {
                    Toast.makeText(this, "Wszystkie pary znalezione!", Toast.LENGTH_LONG).show()
                    mBoard.isEnabled = true
                }
            }
        }
    }

    private fun playSound(player: MediaPlayer) {
        try {
            if (player.isPlaying) {
                player.pause()
                player.seekTo(0)
            }
            player.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        completionPlayer = MediaPlayer.create(applicationContext, R.raw.completion)
        negativePlayer = MediaPlayer.create(applicationContext, R.raw.negative_guitar)
    }

    override fun onPause() {
        super.onPause()
        completionPlayer.release()
        negativePlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("rows", mBoard.rowCount)
        outState.putInt("cols", mBoard.columnCount)
        outState.putIntArray("icons", mBoardModel.getIcons())
        outState.putBooleanArray("revealed", mBoardModel.getRevealedStates())
        // Zapisujemy stan wyciszenia
        outState.putBoolean("is_sound", isSound)
    }
}
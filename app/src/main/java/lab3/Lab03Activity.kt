package lab3

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R
import java.util.Timer
import kotlin.concurrent.schedule

class Lab03Activity : AppCompatActivity() {
    lateinit var completionPlayer: MediaPlayer
    lateinit var negativePLayer: MediaPlayer
    private val animation = MemoryAnimation()
    private lateinit var mBoard: GridLayout
    private lateinit var mBoardModel: MemoryBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)

        mBoard = findViewById(R.id.board)

        if (savedInstanceState != null) {
            // Odtwarzanie po obrocie
            val rows = savedInstanceState.getInt("rows")
            val cols = savedInstanceState.getInt("cols")
            val icons = savedInstanceState.getIntArray("icons")
            val revealed = savedInstanceState.getBooleanArray("revealed")

            mBoardModel = MemoryBoardView(mBoard, cols, rows, icons)

            if (revealed != null) {
                mBoardModel.setRevealedStates(revealed)
            }
        } else {
            // Nowa gra
            val rows = intent.getIntExtra("rows", 3)
            val columns = intent.getIntExtra("columns", 3)
            mBoardModel = MemoryBoardView(mBoard, columns, rows)
        }

        setupGameListener()
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
                    e.tiles.forEach { tile ->
                        tile.revealed = true
                        animation.animateMatch(tile.button) {
                            mBoard.isEnabled = true
                            completionPlayer.start()
                        }
                    }
                }

                GameStates.NoMatch -> {
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
    override protected fun onResume() {
        super.onResume()
        completionPlayer = MediaPlayer.create(applicationContext, R.raw.completion)
        negativePLayer = MediaPlayer.create(applicationContext, R.raw.negative_guitar)
    }


    override protected fun onPause() {
        super.onPause();
        completionPlayer.release()
        negativePLayer.release()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("rows", mBoard.rowCount)
        outState.putInt("cols", mBoard.columnCount)
        outState.putIntArray("icons", mBoardModel.getIcons())
        outState.putBooleanArray("revealed", mBoardModel.getRevealedStates())
    }
}
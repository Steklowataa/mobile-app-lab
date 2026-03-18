package lab3

import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R
import java.util.Timer
import kotlin.concurrent.schedule

class Lab03Activity : AppCompatActivity() {

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

            // Przekazujemy zapisane ikony, żeby model nie losował ich od nowa!
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
            when (e.state) {
                GameStates.Matching, GameStates.Match -> {
                    e.tiles.forEach { it.revealed = true }
                }
                GameStates.NoMatch -> {
                    e.tiles.forEach { it.revealed = true }
                    Timer().schedule(1000) {
                        runOnUiThread {
                            e.tiles.forEach { it.revealed = false }
                        }
                    }
                }
                GameStates.Finished -> {
                    Toast.makeText(this, "Wygrana!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Zapisujemy wszystko, co potrzebne do odtworzenia identycznej planszy
        outState.putInt("rows", mBoard.rowCount)
        outState.putInt("cols", mBoard.columnCount)
        outState.putIntArray("icons", mBoardModel.getIcons())
        outState.putBooleanArray("revealed", mBoardModel.getRevealedStates())
    }
}
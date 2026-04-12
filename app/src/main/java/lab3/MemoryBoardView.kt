package lab3

import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import pl.oleksandra.pam.lab06.R
import java.util.Stack

class MemoryBoardView(
    private val gridLayout: GridLayout,
    private val cols: Int,
    private val rows: Int,
    predefinedIcons: IntArray? = null
) {
    private val tiles: MutableMap<String, Tile> = mutableMapOf()
    private val deckResource: Int = R.drawable.rounded_10k_24
    private val boardIcons: MutableList<Int> = mutableListOf()

    private val icons: List<Int> = listOf(
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,
        R.drawable.outline_ac_unit_24, R.drawable.outline_accessibility_24,
        R.drawable.outline_adb_24, R.drawable.outline_add_a_photo_24,
        R.drawable.outline_60fps_select_24, R.drawable.outline_10mp_24,


    )

    private val matchedPair: Stack<Tile> = Stack()
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)
    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = {}

    init {
        val totalTiles = cols * rows
        if (predefinedIcons != null && predefinedIcons.size == totalTiles) {
            boardIcons.addAll(predefinedIcons.toList())
        } else {
            val neededPairs = totalTiles / 2
            val selectedIcons = icons.take(neededPairs)
            boardIcons.addAll(selectedIcons)
            boardIcons.addAll(selectedIcons)
            boardIcons.shuffle()
        }

        gridLayout.columnCount = cols
        gridLayout.rowCount = rows

        for (i in 0 until totalTiles) {
            val r = i / cols
            val c = i % cols

            val btn = ImageButton(gridLayout.context).apply {
                tag = "$r,$c"
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = GridLayout.spec(c, 1, 1f)
                    rowSpec = GridLayout.spec(r, 1, 1f)
                    setGravity(Gravity.FILL)
                }
            }

            val tile = Tile(btn, boardIcons[i], deckResource)
            tiles[btn.tag.toString()] = tile
            btn.setOnClickListener { onClickTile(it) }
            gridLayout.addView(btn)
        }
    }

    private fun onClickTile(v: View) {
        val tile = tiles[v.tag.toString()] ?: return
        if (tile.revealed) return

        matchedPair.push(tile)
        val state = logic.process { tile.tileResource }
        onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), state))

        if (state != GameStates.Matching) {
            matchedPair.clear()
        }
    }

    fun setOnGameChangeListener(listener: (MemoryGameEvent) -> Unit) {
        onGameChangeStateListener = listener
    }

    fun getIcons(): IntArray = boardIcons.toIntArray()

    fun getRevealedStates(): BooleanArray {
        return tiles.values.map { it.revealed }.toBooleanArray()
    }

    fun setRevealedStates(states: BooleanArray) {
        tiles.values.forEachIndexed { index, tile ->
            if (index < states.size) {
                tile.revealed = states[index]
            }
        }
    }
}
package lab3

class MemoryGameLogic(private val maxMatches: Int) {
    private val valueFunctions: MutableList<() -> Int> = mutableListOf()
    private var matches: Int = 0

    fun process(value: () -> Int): GameStates {
        if (valueFunctions.isEmpty()) {
            valueFunctions.add(value)
            return GameStates.Matching
        }
        valueFunctions.add(value)
        val result = valueFunctions[0]() == valueFunctions[1]()
        matches += if (result) 1 else 0
        valueFunctions.clear()
        return when {
            result && matches == maxMatches -> GameStates.Finished
            result -> GameStates.Match
            else -> GameStates.NoMatch
        }
    }
}
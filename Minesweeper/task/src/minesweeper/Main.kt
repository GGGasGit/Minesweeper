package minesweeper

import kotlin.random.Random

class Minefield(private val rows: Int, private val columns: Int, private val numberOfMines: Int) {

    private val field = MutableList(this.rows) { MutableList(this.columns) { "/" } }

    private val mines = MutableList(this.rows) { MutableList(this.columns) { false } }
    private var minesGenerated = false

    private val marked = MutableList(this.rows) { MutableList(this.columns) { false } }
    private val explored = MutableList(this.rows) { MutableList(this.columns) { false } }
    private val revealed = MutableList(this.rows) { MutableList(this.columns) { false } }

    private fun increaseNumber(row: Int, column: Int) {
        if (!this.mines[row][column]) {
            if (this.field[row][column] == "/") {
                this.field[row][column] = "1"
            } else {
                this.field[row][column] = (this.field[row][column].toInt() + 1).toString()
            }
        }
    }

    private fun calculateNumberOfMines() {
        if (this.mines[0][0]) {
            this.increaseNumber(0, 1)
            this.increaseNumber(1, 0)
            this.increaseNumber(1, 1)
        }
        if (this.mines[0][this.columns - 1]) {
            this.increaseNumber(0, this.columns - 2)
            this.increaseNumber(1, this.columns - 2)
            this.increaseNumber(1, this.columns - 1)
        }
        if (this.mines[this.rows - 1][0]) {
            this.increaseNumber(this.rows - 2, 0)
            this.increaseNumber(this.rows - 2, 1)
            this.increaseNumber(this.rows - 1, 1)
        }
        if (this.mines[this.rows - 1][this.columns - 1]) {
            this.increaseNumber(this.rows - 1, this.columns - 2)
            this.increaseNumber(this.rows - 2, this.columns - 2)
            this.increaseNumber(this.rows - 2, this.columns - 1)
        }
        for (row in 1 until this.rows - 1) {
            if (this.mines[row][0]) {
                this.increaseNumber(row - 1, 0)
                this.increaseNumber(row - 1, 1)
                this.increaseNumber(row, 1)
                this.increaseNumber(row + 1, 1)
                this.increaseNumber(row + 1, 0)
            }
            if (this.mines[row][this.columns - 1]) {
                this.increaseNumber(row - 1, this.columns - 1)
                this.increaseNumber(row - 1, this.columns - 2)
                this.increaseNumber(row, this.columns - 2)
                this.increaseNumber(row + 1, this.columns - 2)
                this.increaseNumber(row + 1, this.columns - 1)
            }
        }
        for (column in 1 until this.columns - 1) {
            if (this.mines[0][column]) {
                this.increaseNumber(0, column - 1)
                this.increaseNumber(1, column - 1)
                this.increaseNumber(1, column)
                this.increaseNumber(1, column + 1)
                this.increaseNumber(0, column + 1)
            }
            if (this.mines[this.rows - 1][column]) {
                this.increaseNumber(this.rows - 1, column - 1)
                this.increaseNumber(this.rows - 2, column - 1)
                this.increaseNumber(this.rows - 2, column)
                this.increaseNumber(this.rows - 2, column + 1)
                this.increaseNumber(this.rows - 1, column + 1)
            }
        }
        for (row in 1 until this.rows - 1) {
            for (column in 1 until this.columns - 1) {
                if (this.mines[row][column]) {
                    this.increaseNumber(row - 1, column - 1)
                    this.increaseNumber(row - 1, column)
                    this.increaseNumber(row - 1, column + 1)
                    this.increaseNumber(row, column - 1)
                    this.increaseNumber(row, column + 1)
                    this.increaseNumber(row + 1, column - 1)
                    this.increaseNumber(row + 1, column)
                    this.increaseNumber(row + 1, column + 1)
                }
            }
        }
    }

    fun printField() {
        print("\n |")
        for (i in 1..columns) print(i)
        println("|")
        print("—|")
        repeat(columns) {
            print("—")
        }
        println("|")
        for (row in 0 until this.rows) {
            print("${row + 1}|")
            for (column in 0 until this.columns) {
                print(if (this.explored[row][column]) {
                    this.field[row][column]
                } else {
                    if (this.marked[row][column]) {
                        "*"
                    } else {
                        "."
                    }
                })
            }
            println("|")
        }
        print("—|")
        repeat(columns) {
            print("—")
        }
        println("|")
    }

    private fun cellExplorer(row: Int, column: Int) {
        if (row == 0 && column == 0) {
            if (this.mines[0][1] || this.mines[1][0] || this.mines[1][1]) {
                this.explored[0][0] = true
                this.marked[0][0] = false
            } else {
                this.revealed[0][0] = true
                this.explored[0][0] = true
                this.marked[0][0] = false
                this.explored[0][1] = true
                this.marked[0][1] = false
                this.explored[1][0] = true
                this.marked[1][0] = false
                this.explored[1][1] = true
                this.marked[1][1] = false
            }
        }
        if (row == 0 && column == this.columns - 1) {
            if (this.mines[0][this.columns - 2] || this.mines[1][this.columns - 2] || this.mines[1][this.columns - 1]) {
                this.explored[0][this.columns - 1] = true
                this.marked[0][this.columns - 1] = false
            } else {
                this.revealed[0][this.columns - 1] = true
                this.explored[0][this.columns - 1] = true
                this.marked[0][this.columns - 1] = false
                this.explored[0][this.columns - 2] = true
                this.marked[0][this.columns - 2] = false
                this.explored[1][this.columns - 2] = true
                this.marked[1][this.columns - 2] = false
                this.explored[1][this.columns - 1] = true
                this.marked[1][this.columns - 1] = false
            }
        }
        if (row == this.rows - 1 && column == 0) {
            if (this.mines[this.rows - 2][0] || this.mines[this.rows - 2][1] || this.mines[this.rows - 1][1]) {
                this.explored[this.rows - 1][0] = true
                this.marked[this.rows - 1][0] = false
            } else {
                this.revealed[this.rows - 1][0] = true
                this.explored[this.rows - 1][0] = true
                this.marked[this.rows - 1][0] = false
                this.explored[this.rows - 2][0] = true
                this.marked[this.rows - 2][0] = false
                this.explored[this.rows - 2][1] = true
                this.marked[this.rows - 2][1] = false
                this.explored[this.rows - 1][1] = true
                this.marked[this.rows - 1][1] = false
            }
        }
        if (row == this.rows - 1 && column == this.columns - 1) {
            if (this.mines[this.rows - 1][this.columns - 2] || this.mines[this.rows - 2][this.columns - 2] || this.mines[this.rows - 2][this.columns - 1]) {
                this.explored[this.rows - 1][this.columns - 1] = true
                this.marked[this.rows - 1][this.columns - 1] = false
            } else {
                this.revealed[this.rows - 1][this.columns - 1] = true
                this.explored[this.rows - 1][this.columns - 1] = true
                this.marked[this.rows - 1][this.columns - 1] = false
                this.explored[this.rows - 1][this.columns - 2] = true
                this.marked[this.rows - 1][this.columns - 2] = false
                this.explored[this.rows - 2][this.columns - 2] = true
                this.marked[this.rows - 2][this.columns - 2] = false
                this.explored[this.rows - 2][this.columns - 1] = true
                this.marked[this.rows - 2][this.columns - 1] = false
            }
        }

        if (row == 0 && column in 1 until this.columns - 1) {
                if (this.mines[0][column - 1] || this.mines[0][column + 1] ||
                    this.mines[1][column - 1] || this.mines[1][column] || this.mines[1][column + 1])
                {
                    this.explored[0][column] = true
                    this.marked[0][column] = false
                } else {
                    this.revealed[0][column] = true
                    this.explored[0][column] = true
                    this.marked[0][column] = false
                    this.explored[0][column - 1] = true
                    this.marked[0][column - 1] = false
                    this.explored[0][column + 1] = true
                    this.marked[0][column + 1] = false
                    this.explored[1][column] = true
                    this.marked[1][column] = false
                    this.explored[1][column - 1] = true
                    this.marked[1][column - 1] = false
                    this.explored[1][column + 1] = true
                    this.marked[1][column + 1] = false
                }
        }
        if (row == this.rows - 1 && column in 1 until this.columns - 1) {
            if (this.mines[this.rows - 1][column - 1] || this.mines[this.rows - 1][column + 1] ||
                this.mines[this.rows - 2][column - 1] || this.mines[this.rows - 2][column] || this.mines[this.rows - 2][column + 1])
            {
                this.explored[this.rows - 1][column] = true
                this.marked[this.rows - 1][column] = false
            } else {
                this.revealed[this.rows - 1][column] = true
                this.explored[this.rows - 1][column] = true
                this.marked[this.rows - 1][column] = false
                this.explored[this.rows - 1][column - 1] = true
                this.marked[this.rows - 1][column - 1] = false
                this.explored[this.rows - 1][column + 1] = true
                this.marked[this.rows - 1][column + 1] = false
                this.explored[this.rows - 2][column] = true
                this.marked[this.rows - 2][column] = false
                this.explored[this.rows - 2][column - 1] = true
                this.marked[this.rows - 2][column - 1] = false
                this.explored[this.rows - 2][column + 1] = true
                this.marked[this.rows - 2][column + 1] = false
            }
        }
        if (column == 0 && row in 1 until this.rows - 1) {
            if (this.mines[row - 1][0] || this.mines[row + 1][0] ||
                this.mines[row - 1][1] || this.mines[row][1] || this.mines[row + 1][1])
            {
                this.explored[row][0] = true
                this.marked[row][0] = false
            } else {
                this.revealed[row][0] = true
                this.explored[row][0] = true
                this.marked[row][0] = false
                this.explored[row - 1][0] = true
                this.marked[row - 1][0] = false
                this.explored[row + 1][0] = true
                this.marked[row + 1][0] = false
                this.explored[row][1] = true
                this.marked[row][1] = false
                this.explored[row - 1][1] = true
                this.marked[row - 1][1] = false
                this.explored[row + 1][1] = true
                this.marked[row + 1][1] = false
            }
        }
        if (column == this.columns - 1 && row in 1 until this.rows - 1) {
            if (this.mines[row - 1][this.columns - 1] || this.mines[row + 1][this.columns - 1] ||
                this.mines[row - 1][this.columns - 2] || this.mines[row][this.columns - 2] || this.mines[row + 1][this.columns - 2])
            {
                this.explored[row][this.columns - 1] = true
                this.marked[row][this.columns - 1] = false
            } else {
                this.revealed[row][this.columns - 1] = true
                this.explored[row][this.columns - 1] = true
                this.marked[row][this.columns - 1] = false
                this.explored[row - 1][this.columns - 1] = true
                this.marked[row - 1][this.columns - 1] = false
                this.explored[row + 1][this.columns - 1] = true
                this.marked[row + 1][this.columns - 1] = false
                this.explored[row][this.columns - 2] = true
                this.marked[row][this.columns - 2] = false
                this.explored[row - 1][this.columns - 2] = true
                this.marked[row - 1][this.columns - 2] = false
                this.explored[row + 1][this.columns - 2] = true
                this.marked[row + 1][this.columns - 2] = false
            }
        }

        if (row in 1 until this.rows - 1 && column in 1 until this.columns - 1) {
            if (this.mines[row - 1][column - 1] || this.mines[row - 1][column] || this.mines[row - 1][column + 1] ||
                this.mines[row][column - 1] || this.mines[row][column + 1] ||
                this.mines[row + 1][column - 1] || this.mines[row + 1][column] || this.mines[row + 1][column + 1])
            {
                this.explored[row][column] = true
                this.marked[row][column] = false
            } else {
                this.revealed[row][column] = true
                this.explored[row][column] = true
                this.explored[row - 1][column - 1] = true
                this.explored[row - 1][column] = true
                this.explored[row - 1][column + 1] = true
                this.explored[row][column - 1] = true
                this.explored[row][column + 1] = true
                this.explored[row + 1][column - 1] = true
                this.explored[row + 1][column] = true
                this.explored[row + 1][column + 1] = true
                this.marked[row][column] = false
                this.marked[row - 1][column - 1] = false
                this.marked[row - 1][column] = false
                this.marked[row - 1][column + 1] = false
                this.marked[row][column - 1] = false
                this.marked[row][column + 1] = false
                this.marked[row + 1][column - 1] = false
                this.marked[row + 1][column] = false
                this.marked[row + 1][column + 1] = false
            }
        }
    }

    private fun mineGenerator(row: Int, column: Int) {
        val randGen = Random.Default
        val integerNumbers = MutableList(this.rows * this.columns) { 0 }
        for (i in 1 until integerNumbers.size) {
            integerNumbers[i] = i
        }
        integerNumbers.remove(row * 9 + column)
        var i = 0
        do {
            val randomInteger = integerNumbers.removeAt(randGen.nextInt(0, integerNumbers.size))
            val rowRandom = randomInteger / 9
            val columnRandom = randomInteger - (rowRandom * 9)
            this.mines[rowRandom][columnRandom] = true
            i++
        } while (i != this.numberOfMines)
    }

    // return codes:
    // -1 - cell already explored, 0 - cell contains mine, 1 - mark set or cleared, 2 - invalid action
    fun handleUserInput(row: Int, column: Int, action: String): Int {
        if (this.explored[row][column]) {
            return -1
        } else {
            when(action) {
                "mine" -> {
                    this.marked[row][column] = !this.marked[row][column]
                    return 1
                }
                "free" -> {
                    if (!minesGenerated) {
                        this.mineGenerator(row, column)
                        this.calculateNumberOfMines()
                        minesGenerated = true
                    }
                    return if (this.mines[row][column]) {
                        for (i in 0 until this.rows) {
                            for (j in 0 until this.columns) {
                                if (this.mines[i][j]) {
                                    this.explored[i][j] = true
                                    this.field[i][j] = "X"
                                }
                            }
                        }
                        0
                    } else {
                        this.cellExplorer(row, column)
                        while (true) {
                            for (i in 0 until this.rows) {
                                for (j in 0 until this.columns) {
                                    if (this.explored[i][j] && this.field[i][j] == "/" && !this.revealed[i][j]) {
                                        this.cellExplorer(i, j)
                                    }
                                }
                            }
                            var revealedAll = true
                            loop@ for (i in 0 until this.rows) {
                                for (j in 0 until this.columns) {
                                    if (this.explored[i][j] && this.field[i][j] == "/" && !this.revealed[i][j]) {
                                        revealedAll = false
                                        break@loop
                                    }
                                }
                            }
                            if (revealedAll) break
                        }
                        1
                    }
                }
                else -> {
                    return 2
                }
            }
        }
    }

    fun checkEndOfGame(): Boolean {
        for (row in 0 until this.rows) {
            for (column in 0 until this.columns) {
                if (this.marked[row][column] && !this.mines[row][column]) return false
                if (!this.marked[row][column] && this.mines[row][column]) return false
            }
        }
        return true
    }
}

fun main() {
    print("How many mines do you want on the field? ")
    val numberOfMines = readln().toInt()
    val mineField = Minefield(9, 9, numberOfMines)
    mineField.printField()
    while (true) {
        print("Set/unset mine marks or claim a cell as free: ")
        val (column, row, action) = readln().split(" ")
        val inputResult = mineField.handleUserInput(row.toInt() - 1, column.toInt() - 1, action)
        if (inputResult == -1) println("This cell is already explored!")
        if (inputResult == 0) {
            mineField.printField()
            println("You stepped on a mine and failed!")
            break
        }
        if (inputResult == 1) mineField.printField()
        if (inputResult == 2) println("Invalid action!")
        if (mineField.checkEndOfGame()) {
            print("Congratulations! You found all the mines!")
            break
        }
    }
}

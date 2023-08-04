package com.gensoft.seachess

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.gensoft.seachess.adapters.GridViewAdapter
import com.gensoft.seachess.adapters.OnBoxClickListener
import com.gensoft.seachess.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnBoxClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GridViewAdapter
    private var currentPlayer = "X" // Player 1 starts with X

    private val board = Array(3) { arrayOfNulls<String>(3) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = GridViewAdapter(applicationContext,this)
        binding.txtPlayer.text = getString(R.string.current_player,currentPlayer)
        binding.gridView.adapter = adapter

    }

    override fun onBoxClicked(row: Int, col: Int, btnBox: Button) {
        if (board[row][col] == null) {
            board[row][col] = currentPlayer
            btnBox.text = currentPlayer

            if (checkWinner(row, col)) {
                val winner = if (currentPlayer == "X") "Player X" else "Player O"
                val message = "$winner Печели!"

                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Game Over")
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Нова игра") { dialog, _ ->
                        // Handle the "OK" button click (if needed)
                        dialog.dismiss()
                    }
                    .create()

                alertDialog.show()
                clearBoard()
            } else {
                //Switching the player after click
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                binding.txtPlayer.text = getString(R.string.current_player,currentPlayer)
                //Check if all buttons.text have items for a draw
                val isDraw = isBoardFull() && !checkWinner(row, col)
                if (isDraw) {
                    //Alert
                    val alertDialog = AlertDialog.Builder(this)
                        .setTitle("Game Over")
                        .setMessage("Равенство!")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, _ ->
                            // Handle the "OK" button click
                            dialog.dismiss()
                        }
                        .create()

                    alertDialog.show()
                    //Reset the game
                    clearBoard()
                }
            }
        }
    }
    private fun checkWinner(row: Int, col: Int): Boolean {
        // Check rows
        for (i in 0 until BOARD_SIZE) {
            if (board[row][i] != currentPlayer) {
                break
            }
            if (i == BOARD_SIZE - 1) {
                return true
            }
        }

        // Check columns
        for (i in 0 until BOARD_SIZE) {
            if (board[i][col] != currentPlayer) {
                break
            }
            if (i == BOARD_SIZE - 1) {
                return true
            }
        }

        // Check main diagonal (top-left to bottom-right)
        if (row == col) {
            for (i in 0 until BOARD_SIZE) {
                if (board[i][i] != currentPlayer) {
                    break
                }
                if (i == BOARD_SIZE - 1) {
                    return true
                }
            }
        }

        // Check reverse diagonal (top-right to bottom-left)
        if (row + col == BOARD_SIZE - 1) {
            for (i in 0 until BOARD_SIZE) {
                if (board[i][BOARD_SIZE - 1 - i] != currentPlayer) {
                    break
                }
                if (i == BOARD_SIZE - 1) {
                    return true
                }
            }
        }

        return false
    }

    private fun isBoardFull(): Boolean {
        for (row in 0 until BOARD_SIZE) {
            for (col in 0 until BOARD_SIZE) {
                if (board[row][col].isNullOrEmpty()) {
                    return false
                }
            }
        }
        return true
    }

    private fun clearBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = null
            }
        }
        currentPlayer = "X"
        binding.txtPlayer.text = getString(R.string.current_player, currentPlayer)
        adapter.resetGame() // Clear the game board in the adapter
    }

    companion object{
        private const val BOARD_SIZE = 3
    }
}
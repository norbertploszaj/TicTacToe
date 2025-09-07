package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TicTacToeScreen()
                }
            }
        }
    }
}

@Composable
fun TicTacToeScreen() {
    var board by remember { mutableStateOf(List(9) { ' ' }) }
    var xTurn by remember { mutableStateOf(true) }
    var status by remember { mutableStateOf("Tura: X") }
    var gameOver by remember { mutableStateOf(false) }

    fun winner(b: List<Char>): Char? {
        val lines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for (line in lines) {
            val (a, b1, c) = line
            if (b[a] != ' ' && b[a] == b[b1] && b[a] == b[c]) return b[a]
        }
        return null
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Kółko i Krzyżyk", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Text(status, fontSize = 18.sp)

        Spacer(Modifier.height(16.dp))

        for (r in 0..2) {
            Row {
                for (c in 0..2) {
                    val idx = r * 3 + c
                    val cell = board[idx]
                    Surface(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                            .clickable(enabled = !gameOver && cell == ' ') {
                                val newBoard = board.toMutableList()
                                newBoard[idx] = if (xTurn) 'X' else 'O'
                                board = newBoard
                                val w = winner(board)
                                when {
                                    w == 'X' -> { status = "Wygrał X"; gameOver = true }
                                    w == 'O' -> { status = "Wygrało O"; gameOver = true }
                                    board.none { it == ' ' } -> { status = "Remis"; gameOver = true }
                                    else -> {
                                        xTurn = !xTurn
                                        status = "Tura: ${if (xTurn) "X" else "O"}"
                                    }
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (cell == ' ') "" else cell.toString(),
                                fontSize = 36.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            board = List(9) { ' ' }
            xTurn = true
            status = "Tura: X"
            gameOver = false
        }) {
            Text("Resetuj")
        }
    }
}

package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

/** Unit tests for the console game's controller behavior. */
class TicTacToeConsoleTest {
    /** Verifies the controller runs alternating moves until a player wins. */
    @Test
    void playsUntilAPlayerWins() {
        Board board = new Board();
        Player first = new ScriptedPlayer('X', new int[][]{{0, 0}, {0, 1}, {0, 2}});
        Player second = new ScriptedPlayer('O', new int[][]{{1, 0}, {1, 1}});
        TicTacToeConsole game = new TicTacToeConsole(new Scanner(""), board, first, second);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            game.startGame();
        } finally {
            System.setOut(originalOut);
        }

        assertTrue(game.checkGameOver());
        assertEquals('X', board.getCell(0, 2));
        assertTrue(output.toString(StandardCharsets.UTF_8).contains("Player (X) wins!"));
    }
    
    /** A deterministic player used to supply a predefined sequence of moves. */
    private static final class ScriptedPlayer extends Player {
        private final int[][] moves;
        private int nextMove;

        /** Creates a scripted player with the given marker and moves. */
        private ScriptedPlayer(char marker, int[][] moves) {
            super(marker);
            this.moves = moves;
        }

        /** Places the next scripted move on the board. */
        @Override
        public void makeMove(Board board) {
            int[] move = moves[nextMove++];
            if (!board.place(move[0], move[1], marker)) {
                throw new AssertionError("Scripted move was invalid");
            }
        }
    }
}
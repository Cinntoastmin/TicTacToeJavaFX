package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

/** Unit tests for human and CPU player move behavior. */
class PlayerTest {
    /** Verifies a human can place a marker at valid coordinates. */
    @Test
    void humanCanPlaceAMoveFromSelectedCoordinates() {
        Board board = new Board();
        HumanPlayer player = new HumanPlayer('X', new Scanner(""));

        assertTrue(player.makeMove(board, 2, 1));
        assertEquals('X', board.getCell(2, 1));
        assertFalse(player.makeMove(board, 2, 1));
        assertFalse(player.makeMove(board, -1, 0));
    }

    /** Verifies the human player retries malformed and occupied coordinates. */
    @Test
    void humanRetriesMalformedAndOccupiedPositions() {
        Board board = new Board();
        board.place(0, 0, 'O');
        HumanPlayer player = new HumanPlayer('X', new Scanner("bad input\n1 1\n2 3\n"));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            player.makeMove(board);
        } finally {
            System.setOut(originalOut);
        }
        assertEquals('X', board.getCell(1, 2));
        assertTrue(output.toString(StandardCharsets.UTF_8).contains("invalid or occupied"));
    }

    /** Verifies the CPU chooses an empty cell and rejects a full board. */
    @Test
    void cpuSelectsAnEmptyCellAndCannotPlayOnAFullBoard() {
        Board board = new Board();
        board.place(0, 0, 'X');
        CpuPlayer cpu = new CpuPlayer('O', new Random(7));
        cpu.makeMove(board);
        assertEquals(1, countMarker(board, 'O'));
        assertEquals('X', board.getCell(0, 0));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (board.isValid(row, column)) {
                    board.place(row, column, 'X');
                }
            }
        }
        assertThrows(IllegalStateException.class, () -> cpu.makeMove(board));
    }

    /** Verifies default player names are assigned and invalid names are rejected. */
    @Test
    void defaultNamesAreAssignedAndNameValidationRejectsNullOrBlankValues() {
        HumanPlayer human = new HumanPlayer('X', new Scanner(""));
        CpuPlayer cpu = new CpuPlayer('O', new Random(1));

        assertEquals("Player #1", human.getName());
        assertEquals("CPU #1", cpu.getName());

        assertThrows(IllegalArgumentException.class, () -> human.setName(null));
        assertThrows(IllegalArgumentException.class, () -> human.setName("   "));
        assertThrows(IllegalArgumentException.class, () -> human.setName("\t\n "));
        assertThrows(IllegalArgumentException.class, () -> cpu.setName(null));
        assertThrows(IllegalArgumentException.class, () -> cpu.setName("   "));
        assertThrows(IllegalArgumentException.class, () -> cpu.setName("\t\n  "));
    }

    @Test
    void DoTwoHumanPlayersHaveDifferentNames(){
        HumanPlayer h1 = new HumanPlayer('X', new Scanner(""));
        HumanPlayer h2 = new HumanPlayer('O', new Scanner(""));

        assertEquals("Player 1", h1.getName());
        assertEquals("Player 2", h2.getName());
    }

    @Test
    void DoTwoCPUPlayersHaveDifferentNames(){
        CpuPlayer c1 = new CpuPlayer('X');
        CpuPlayer c2 = new CpuPlayer('O');

        assertEquals("Player 1", c1.getName());
        assertEquals("Player 2", c2.getName());
    }

    /** Counts the occurrences of a marker on the supplied board. */
    private int countMarker(Board board, char marker) {
        int count = 0;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (board.getCell(row, column) == marker) {
                    count++;
                }
            }
        }
        return count;
    }
}
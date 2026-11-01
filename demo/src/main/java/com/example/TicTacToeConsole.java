package com.example;

import java.util.Objects;
import java.util.Scanner;

/** 
 * 
 * Runs a console game of Tic-Tac-Toe between a human and the CPU.
 */
public class TicTacToeConsole {
    private final Board board;
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private final Scanner scanner;

    /** Creates a game with standard input, a new board, a human, and a CPU. */
    public TicTacToeConsole() {
        this(new Scanner(System.in), new Board(), null, null);
    }

    /**
     * Creates a game using the supplied components, defaulting missing players
     * to a human using X and a CPU using O.
     *
     * @param scanner input source shared with the default human player
     * @param board board used for the game
     * @param player1 first player, or {@code null} for the default human
     * @param player2 second player, or {@code null} for the default CPU
     * @throws NullPointerException if {@code scanner} or {@code board} is null
     * @throws IllegalArgumentException if both players use the same marker
     */
    public TicTacToeConsole(Scanner scanner, Board board, Player player1, Player player2) {
        this.scanner = Objects.requireNonNull(scanner, "scanner");
        this.board = Objects.requireNonNull(board, "board");
        this.player1 = player1 == null ? new HumanPlayer('X', scanner) : player1;
        this.player2 = player2 == null ? new CpuPlayer('O') : player2;
        if (this.player1.getMarker() == this.player2.getMarker()) {
            throw new IllegalArgumentException("Players must use different markers");
        }
        currentPlayer = this.player1;
    }

    /** Starts a console game using the default game components. */
    public static void main(String[] args) {
        new TicTacToeConsole().startGame();
    }

    /** Initializes and plays turns until a player wins or the board is full. */
    public void startGame() {
        board.initialize();
        System.out.println("Welcome to Tic-Tac-Toe! You are X; the CPU is O.");
        board.display();

        while (!checkGameOver()) {
            currentPlayer.makeMove(board);
            board.display();
            if (board.checkWin()) {
                System.out.printf("%s (%c) wins!%n",
                        currentPlayer instanceof CpuPlayer ? "CPU" : "Player", currentPlayer.getMarker());
                break;
            }
            if (board.isFull()) {
                System.out.println("It's a draw!");
                break;
            }
            switchTurn();
        }
    }

    /** Changes the active player to the other player. */
    public void switchTurn() {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
    }

    /**
     * Reports whether the board has a winning line or no empty cells.
     *
     * @return {@code true} when the game has ended
     */
    public boolean checkGameOver() {
        return board.checkWin() || board.isFull();
    }

    /**
     * Returns the board used in this game.
     *
     * @return this game's board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the player whose turn is currently active.
     *
     * @return the active player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the input scanner used by this game.
     *
     * @return this game's scanner
     */
    public Scanner getScanner() {
        return scanner;
    }
}
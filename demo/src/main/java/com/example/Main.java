package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/** JavaFX entry point for the Tic-Tac-Toe game. */
public class Main extends Application {
    private final Board board = new Board();
    private final Button[] buttons = new Button[9];
    private final Label statusLabel = new Label("Your turn");
    private final CpuPlayer cpuPlayer = new CpuPlayer('O');
    private boolean gameOver = false;

    /** Starts the JavaFX application. */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("board");

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                int index = row * 3 + column;
                Button button = new Button(" ");
                button.setPrefSize(100, 100);
                button.setFont(Font.font(36));
                button.getStyleClass().add("cell");
                final int finalRow = row;
                final int finalColumn = column;
                button.setOnAction(event -> handleHumanMove(button, finalRow, finalColumn));
                buttons[index] = button;
                grid.add(button, column, row);
            }
        }

        Button restartButton = new Button("Restart");
        restartButton.getStyleClass().add("restart-button");
        restartButton.setOnAction(event -> resetGame());

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        root.setCenter(grid);
        root.setTop(statusLabel);
        root.setBottom(restartButton);
        statusLabel.getStyleClass().add("status");
        BorderPane.setAlignment(statusLabel, Pos.CENTER);
        BorderPane.setMargin(statusLabel, new Insets(10));
        BorderPane.setAlignment(restartButton, Pos.CENTER);
        BorderPane.setMargin(restartButton, new Insets(10));

        Scene scene = new Scene(root, 350, 420);
        scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());

        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** Handles a human move and then the CPU turn if the game is still active. */
    private void handleHumanMove(Button button, int row, int column) {
        if (gameOver || !board.isValid(row, column)) {
            return;
        }

        board.place(row, column, 'X');
        updateBoard();

        if (board.checkWin()) {
            finishGame("You win!");
            return;
        }

        if (board.isFull()) {
            finishGame("It's a draw!");
            return;
        }

        statusLabel.setText("CPU is thinking...");
        Platform.runLater(() -> {
            if (gameOver) {
                return;
            }
            cpuPlayer.makeMove(board);
            updateBoard();

            if (board.checkWin()) {
                finishGame("CPU wins!");
                return;
            }

            if (board.isFull()) {
                finishGame("It's a draw!");
                return;
            }

            statusLabel.setText("Your turn");
        });
    }

    /** Draws the current board state onto the buttons. */
    private void updateBoard() {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                Button button = buttons[row * 3 + column];
                char cell = board.getCell(row, column);
                button.setText(cell == ' ' ? " " : String.valueOf(cell));
            }
        }
    }

    /** Resets the board and UI for a new game. */
    private void resetGame() {
        board.initialize();
        gameOver = false;
        statusLabel.setText("Your turn");
        updateBoard();
    }

    /** Marks the game as complete and updates the message label. */
    private void finishGame(String message) {
        gameOver = true;
        statusLabel.setText(message);
    }
}
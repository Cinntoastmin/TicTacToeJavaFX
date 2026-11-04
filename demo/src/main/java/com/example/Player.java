package com.example;

/**
 * 
 * Defines the shared marker and move contract for a Tic-Tac-Toe player.
 */ 
public abstract class Player {
    protected final char marker;
    private String name;

    /**
     * Creates a player with the given marker.
     *
     * @param marker the player's marker, either {@code X} or {@code O}
     * @throws IllegalArgumentException if the marker is not {@code X} or {@code O}
     */
    protected Player(char marker) {
        this(marker, "Player");
    }

    /**
     * Creates a player with the given marker and default name.
     *
     * @param marker the player's marker, either {@code X} or {@code O}
     * @param defaultName the default display name for the player
     * @throws IllegalArgumentException if the marker is not {@code X} or {@code O}
     */
    protected Player(char marker, String defaultName) {
        if (marker != 'X' && marker != 'O') {
            throw new IllegalArgumentException("A player marker must be X or O");
        }
        this.marker = marker;
        this.name = defaultName;
    }

    /**
     * Returns this player's marker.
     *
     * @return the player's marker
     */
    public char getMarker() {
        return marker;
    }

    /**
     * Returns this player's display name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets this player's display name.
     *
     * @param name the player's new display name
     * @throws IllegalArgumentException if the name is null or blank after trimming
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or blank");
        }
        this.name = name.trim();
    }

    /**
     * Makes a move on the supplied board.
     *
     * @param board board on which to make the move
     */
    public abstract void makeMove(Board board);
}
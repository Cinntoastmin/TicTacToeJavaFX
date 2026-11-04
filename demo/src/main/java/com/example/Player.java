package com.example;

/**
 * 
 * Defines the shared marker and move contract for a Tic-Tac-Toe player.
 */ 
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Player {
    private static final Map<Class<? extends Player>, AtomicInteger> DEFAULT_NAME_COUNTERS =
            new ConcurrentHashMap<>();

    protected final char marker;
    private String name;

    /**
     * Creates a player with the given marker.
     *
     * @param marker the player's marker, either {@code X} or {@code O}
     * @throws IllegalArgumentException if the marker is not {@code X} or {@code O}
     */
    protected Player(char marker) {
        this(marker, null);
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
        this.name = defaultName == null ? generateDefaultName() : defaultName;
    }

    /** Creates a unique default name for this concrete player type. */
    private String generateDefaultName() {
        AtomicInteger counter = DEFAULT_NAME_COUNTERS.computeIfAbsent(getClass(), key -> new AtomicInteger(1));
        return "Player " + counter.getAndIncrement();
    }

    /** Resets the default-name counters so tests can start from a predictable baseline. */
    static void resetDefaultNameCounters() {
        DEFAULT_NAME_COUNTERS.clear();
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
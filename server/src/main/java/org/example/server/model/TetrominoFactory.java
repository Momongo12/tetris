package org.example.server.model;

import org.springframework.web.socket.WebSocketSession;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * The TetrominoFactory class is responsible for generating Tetromino objects for players in a game.
 * @version 1.0
 * @author Denis Moskvin
 */
public class TetrominoFactory {
    private final WebSocketSession player1Session;
    private final WebSocketSession player2Session;
    private final LinkedList<TetrominoType> player1Queue;
    private final LinkedList<TetrominoType> player2Queue;
    private final Color[] colors = {new Color(255, 0, 0), new Color(0, 128, 0), new Color(0, 0, 255), new Color(0, 255, 255),
            new Color(255, 0, 255), new Color(255, 255, 0), new Color(128, 128, 128)};
    Random colorRandomIndex;

    /**
     * Constructs a TetrominoFactory object with the specified player WebSocket sessions.
     *
     * @param player1Session The WebSocket session of player 1.
     * @param player2Session The WebSocket session of player 2.
     */
    public TetrominoFactory(WebSocketSession player1Session, WebSocketSession player2Session) {
        this.player1Session = player1Session;
        this.player2Session = player2Session;
        colorRandomIndex = new Random();
        player1Queue = new LinkedList<>();
        player2Queue = new LinkedList<>();

        // Fill queues
        refill(player1Queue);
        player2Queue.addAll(player1Queue);
    }

    /**
     * Refills the specified player's Tetromino queue with all Tetromino types in random order.
     *
     * @param playerQueue The queue of Tetromino types for a player.
     */
    public void refill(LinkedList<TetrominoType> playerQueue) {
        playerQueue.clear();
        playerQueue.addAll(Arrays.asList(TetrominoType.values()));

        Collections.shuffle(playerQueue);
    }

    /**
     * Retrieves the next Tetromino for the specified player.
     *
     * @param playerSession The WebSocket session of the player.
     * @return The next Tetromino for the player.
     */
    public Tetromino getNextTetromino(WebSocketSession playerSession) {
        if (playerSession == player1Session) {
            if (player1Queue.isEmpty()) {
                refill(player1Queue);
                player2Queue.addAll(player1Queue);
            }
            return new Tetromino(player1Queue.removeFirst(), 0, 3, colors[colorRandomIndex.nextInt(colors.length)]);
        }

        if (player2Queue.isEmpty()) {
            refill(player2Queue);
            player2Queue.addAll(player1Queue);
        }
        return new Tetromino(player2Queue.removeFirst(), 0, 3, colors[colorRandomIndex.nextInt(colors.length)]);
    }
}

package org.example.server.model;

import org.springframework.web.socket.WebSocketSession;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class TetrominoFactory {
    private final WebSocketSession player1Session;
    private final WebSocketSession player2Session;
    private final LinkedList<TetrominoType> player1Queue;
    private final LinkedList<TetrominoType> player2Queue;
    private final Color[] colors = {new Color(255, 0, 0),  new Color(0, 128, 0), new Color(0,0, 255), new Color(0, 255, 255),
                                new Color(255, 0, 255), new Color(255, 255, 0), new Color(128, 128, 128)};
    Random colorRandomIndex;

    public TetrominoFactory(WebSocketSession player1Session, WebSocketSession player2Session) {
        this.player1Session = player1Session;
        this.player2Session = player2Session;
        colorRandomIndex = new Random();
        player1Queue = new LinkedList<>();
        player2Queue = new LinkedList<>();

        //fill queues
        refill(player1Queue);
        player2Queue.addAll(player1Queue);
    }

    public void refill(LinkedList<TetrominoType> playerQueue) {
        playerQueue.clear();
        playerQueue.addAll(Arrays.asList(TetrominoType.values()));
        
        Collections.shuffle(playerQueue);
    }


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

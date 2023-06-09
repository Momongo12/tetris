package tetris.model;


import tetris.view.*;

import java.util.ArrayList;

/**
 * @author denMoskvin
 * @version 1.0
 */
public interface GameModel {

    void startGame();
    void pauseGame();
    boolean getGameStatus();

    void moveTetromino(int direction);
    void rotateTetromino();
    void holdTetromino();

    void setGamePanel(GamePanel gamePanel);
    String getOwnSessionId();
    String getOpponentSessionId();

    int getLines(String playerSessionId);
    int getScore(String playerSessionId);
    int getLevel(String playerSessionId);
    ArrayList<Square[]> getBoard(String playerSessionId);
    Tetromino getTetromino(String playerSessionId);
    Tetromino getNextTetromino(String playerSessionId);
    Tetromino getHoldTetromino(String playerSessionId);
}

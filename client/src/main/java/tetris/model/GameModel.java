package tetris.model;

import tetris.view.*;

import java.util.ArrayList;

public interface GameModel {

    void startGame();
    void pauseGame();

    void moveTetromino(int direction);
    void rotateTetromino();
    void holdTetromino();

    boolean getGameStatus();
    void setGamePanel(GamePanel gamePanel);
    void addInfoPanel(InfoPanel infoPanel);
    void addGameMatrix(GameMatrix gameMatrix);
    void addNextShapePanel(NextShapePanel nextShapePanel);
    void addHoldPanel(HoldPanel holdPanel);
    void addLevelPanel(LevelPanel levelPanel);

    int getLines(InfoPanel infoPanel);
    int getScore(InfoPanel infoPanel);
    int getLevel(LevelPanel levelPanel);
    ArrayList<Square[]> getBoard(GameMatrix gameMatrix);
    Tetromino getTetromino(GameMatrix gameMatrix);
    Tetromino getNextTetromino(NextShapePanel nextShapePanel);
    Tetromino getHoldTetromino(HoldPanel holdPanel);
}

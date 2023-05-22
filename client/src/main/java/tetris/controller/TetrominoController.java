package tetris.controller;

import tetris.model.GameModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TetrominoController extends KeyAdapter {
    private GameModel gameModel;

    public TetrominoController(GameModel gameModel) {this.gameModel = gameModel;}
    @Override
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();
        if (!gameModel.getGameStatus()) return;
        switch (keyCode) {
            case (KeyEvent.VK_UP) -> gameModel.rotateTetromino();
            case (KeyEvent.VK_RIGHT) -> gameModel.moveTetromino(1);
            case (KeyEvent.VK_DOWN) -> gameModel.moveTetromino(2);
            case (KeyEvent.VK_LEFT) -> gameModel.moveTetromino(3);
            case (KeyEvent.VK_SPACE) -> gameModel.holdTetromino();
        }
    }
    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }
}
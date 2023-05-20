package tetris.model;

import static tetris.util.TetrisConstants.*;
import org.apache.logging.log4j.Logger;
import tetris.controller.TetrominoController;
import tetris.logger.MyLoggerFactory;
import tetris.resource.ResourceManager;
import tetris.view.GamePanel;
import tetris.view.InfoPanel;
import tetris.view.NextShapePanel;
import tetris.view.Square;

import javax.sound.sampled.*;
import java.awt.*;
import java.util.ArrayList;


public class GameModel {
    private final int stepIncremetialOfGameSpeed = 200;
    private final int initialGameSpeed = 1000;
    private int gameSpeed = 1000;
    private int level = 1;
    private int score = 0;
    private int lines = 0;

    private static final Logger LOGGER = MyLoggerFactory.getLogger(GameModel.class);

    private ArrayList<Square[]> board;
    private TetrominoController tetrominoController;
    private TetrominoFactory tetrominoFactory;

    private Tetromino tetromino;
    private Tetromino nextTetromino;
    private boolean isHoldTetromino = false;
    private Tetromino holdTetromino;

    private GameLauncher gameLauncher;
    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private NextShapePanel nextShapePanel;

    private Color boardColor = new Color(20, 40, 60);
    private boolean gameOver;
    private boolean gameIsActive;

    private Clip moveSound, rotateSound, dropSound;

    public GameModel(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
        gameIsActive = false;
        gameOver = false;
        initSounds();
        initBoard();
        tetrominoFactory = new TetrominoFactory();
        tetromino = tetrominoFactory.getNextTetromino();
        nextTetromino = tetrominoFactory.getNextTetromino();
        tetrominoController = new TetrominoController(this);
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void initSounds(){
        try{
            moveSound = ResourceManager.getSound("moveSound.wav");
            rotateSound = ResourceManager.getSound("rotateSound.wav");
            dropSound = ResourceManager.getSound("dropSound.wav");

            LineListener listenerForProcessingSoundEnd = event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    dropSound.setFramePosition(0);
                }
            };

            //process the end of the clip
            moveSound.addLineListener(listenerForProcessingSoundEnd);
            rotateSound.addLineListener(listenerForProcessingSoundEnd);
            dropSound.addLineListener(listenerForProcessingSoundEnd);

            //volume level setting
            ((FloatControl) moveSound.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-20f);
            ((FloatControl) rotateSound.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-20f);
            ((FloatControl) dropSound.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-20f);

            LOGGER.debug("Game sounds loaded");

        } catch (Exception ex){
            LOGGER.error("Game sound initialization error", ex);
        }

    }

    private void initBoard() {
        board = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            board.add(i, new Square[COLUMNS]);
            for (int j = 0; j < COLUMNS; j++) {
                int x = (j * BLOCK_SIZE);
                int y = (i * BLOCK_SIZE);
                board.get(i)[j] = new Square(x, y, i, j, boardColor);
            }
        }
    }

    public void startGame() {
        gameIsActive = true;
        while (!gameOver) {
            try {
                Thread.sleep(gameSpeed);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!gameIsActive) break;
            if (tetrominoIsTouchedGround(tetromino)) {
                dropSound.start();
                putTetrominoOnGround(tetromino);
                tetromino = nextTetromino;
                nextTetromino = tetrominoFactory.getNextTetromino();
                gameOver = isGameOver();
            } else {
                moveTetromino(2);
            }
            gamePanel.repaint();
        }
        if (gameOver) {
            gameIsActive = false;
            endGame();
        }
    }

    public void pauseGame(){
        gameIsActive = !gameIsActive;
    }

    public void refreshGameData(){
        gameIsActive = false;
        gameOver = false;
        level = 1;
        score = 0;
        lines = 0;
        initBoard();
        tetrominoFactory.refill();
        tetromino = tetrominoFactory.getNextTetromino();
        nextTetromino = tetrominoFactory.getNextTetromino();
        gamePanel.deleteGameOverPanelAndAddGameMatrix();

        LOGGER.debug("game data refreshed");
    }

    private void endGame() {
        gamePanel.displayGameOverPanel();
        gameLauncher.getNavigationPanel().getPlayOrPauseButton().doClick();
        gameLauncher.updateStatisticPlayer(score, lines, level);
    }

    public void moveTetromino(int direction) {
        if (direction != 2) moveSound.start();
        if (tetrominoIsTouchWall(tetromino, direction)) {
            return;
        }
        switch (direction) {
            case (1) -> tetromino.moveRight();
            case (2) -> tetromino.moveDown();
            case (3) -> tetromino.moveLeft();
        }
        if (tetrominoIsTouchedGround(tetromino)) {
            dropSound.start();
            gameOver = isGameOver();
            putTetrominoOnGround(tetromino);
            tetromino = nextTetromino;
            nextTetromino = tetrominoFactory.getNextTetromino();
        }
        removeFilledLines();
        gamePanel.repaint();
        LOGGER.trace("Tetromino moved");
    }

    public void rotateTetromino() {
        rotateSound.start();
        tetromino.rotate(board, boardColor);
        gamePanel.repaint();
        LOGGER.trace("Tetromino rotated");
    }

    public void holdTetromino() {
        if (isHoldTetromino) {
            Tetromino tmpTetromino = holdTetromino;
            holdTetromino = tetromino;
            tetromino = tmpTetromino;
            gamePanel.repaint();
        } else {
            holdTetromino = tetromino;
            tetromino = nextTetromino;
            nextTetromino = tetrominoFactory.getNextTetromino();
            gamePanel.repaint();
            isHoldTetromino = true;
        }
        LOGGER.trace("Tetromino holded");
    }

    private void removeFilledLines(){
        int countRemovedLines = 0;
        for (int i = 0; i < ROWS; i++){
            int countFilledSquare = 0;
            for (int j = 0; j < COLUMNS; j++){
                if (board.get(i)[j].getColor() != boardColor) countFilledSquare++;
            }
            if (countFilledSquare == COLUMNS){
                board.remove(i);
                board.add(0, new Square[COLUMNS]);
                for (int j = 0; j < COLUMNS; j++){
                    board.get(0)[j] = new Square(0, 0, i, j, boardColor);
                }
                countRemovedLines++;
                lines++;
            }
        }
        if (countRemovedLines == 1){
            score += 100;
        }else if (countRemovedLines == 2){
            score += 300;
        }else if (countRemovedLines == 3){
            score += 700;
        }else if (countRemovedLines == 4){
            score += 1500;
        }
        LOGGER.trace("filed line removed");
    }

    private boolean tetrominoIsTouchWall(Tetromino tetromino, int direction) {
        for (Square square : tetromino.getBlocks()) {
            int col = square.getCol();
            int row = square.getRow();
            if (direction == 3 && col > 0 && board.get(row)[col - 1].getColor() != boardColor) {
                return true;
            } else if (direction == 1 && col < (COLUMNS - 1) && board.get(row)[col + 1].getColor() != boardColor) {
                return true;
            }
        }
        return false;
    }

    private boolean tetrominoIsTouchedGround(Tetromino tetromino) {
        for (Square square : tetromino.getBlocks()) {
            int col = square.getCol();
            int row = square.getRow();
            if (row + 1 >= ROWS || board.get(row + 1)[col].getColor() != boardColor) {
                return true;
            }
        }
        LOGGER.trace("Tetromino touched ground");
        return false;
    }

    private boolean isGameOver() {
        for (Square square : tetromino.getBlocks()) {
            if (square.getRow() == 0 && square.getCol() == 4) return true;
        }
        return false;
    }

    private void putTetrominoOnGround(Tetromino tetromino) {
        for (Square square : tetromino.getBlocks()) {
            board.get(square.getRow())[square.getCol()].setColor(square.getColor());
        }
    }

    public void setInfoPanel(InfoPanel infoPanel) {
        this.infoPanel = infoPanel;
    }

    public boolean getGameStatus() {
        return gameIsActive;
    }

    public int getScore() {
        return score;
    }

    public int getLines() {
        return lines;
    }

    public int getLevel(){
        return level;
    }

    public Tetromino getNextTetromino() {
        return nextTetromino;
    }

    public Tetromino getHoldTetromino() {
        return holdTetromino;
    }

    public void setGameLevel(int level){
         this.level = level;
         updateGameSpeed(level);
    }

    private void updateGameSpeed(int level){
        this.gameSpeed = initialGameSpeed - (level - 1) * stepIncremetialOfGameSpeed;
    }

    public ArrayList<Square[]> getBoard() {
        return board;
    }

    public TetrominoController getTetrominoController() {
        return tetrominoController;
    }

    public Tetromino getTetromino() {
        return tetromino;
    }
}

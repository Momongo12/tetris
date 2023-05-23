package com.example.server.model;

//import org.apache.logging.log4j.Logger;
import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
//import tetris.logger.MyLoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.server.util.TetrisConstants.*;

@Data
public class PvPGameModel{
//    private static final Logger LOGGER = MyLoggerFactory.getLogger(SoloGameModel.class);

    public static final int ROWS = 20;
    public static final int COLUMNS = 10;
    private final Color boardColor = new Color(20, 40, 60);
    private final PvPGameSession pvPGameSession;
    private final WebSocketSession sessionPlayer1;
    private final WebSocketSession sessionPlayer2;
    private final TetrominoFactory tetrominoFactory1;
    private final TetrominoFactory tetrominoFactory2;
    private boolean isHoldTetrominoPlayer1;
    private boolean isHoldTetrominoPlayer2;

    public PvPGameModel(WebSocketSession sessionPlayer1, WebSocketSession sessionPlayer2) {
        this.sessionPlayer1 = sessionPlayer1;
        this.sessionPlayer2 = sessionPlayer2;
        this.pvPGameSession = new PvPGameSession();

        pvPGameSession.setGameMatrixPlayer1(initBoard());
        pvPGameSession.setGameMatrixPlayer2(initBoard());

        this.tetrominoFactory1 = new TetrominoFactory();
        this.tetrominoFactory2 = new TetrominoFactory();

        pvPGameSession.setTetrominoPlayer1(tetrominoFactory1.getNextTetromino());
        pvPGameSession.setNextTetrominoPlayer1(tetrominoFactory1.getNextTetromino());

        pvPGameSession.setTetrominoPlayer2(tetrominoFactory2.getNextTetromino());
        pvPGameSession.setNextTetrominoPlayer2(tetrominoFactory2.getNextTetromino());

    }

    private ArrayList<Square[]> initBoard() {
        ArrayList<Square[]> board = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            board.add(i, new Square[COLUMNS]);
            for (int j = 0; j < COLUMNS; j++) {
                int x = (j * BLOCK_SIZE);
                int y = (i * BLOCK_SIZE);
                board.get(i)[j] = new Square(x, y, i, j, boardColor);
            }
        }
        return board;
    }

    public void startGame() {
        while (!pvPGameSession.isGameOver()) {
            try {
                Thread.sleep(pvPGameSession.getGameSpeed());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tetrominoIsTouchedGround(pvPGameSession.getTetrominoPlayer1(), pvPGameSession.getGameMatrixPlayer1())) {
                putTetrominoOnGround(pvPGameSession.getTetrominoPlayer1(), pvPGameSession.getGameMatrixPlayer1());
                pvPGameSession.setTetrominoPlayer1(pvPGameSession.getNextTetrominoPlayer1());
                pvPGameSession.setNextTetrominoPlayer1(tetrominoFactory1.getNextTetromino());
                pvPGameSession.setGameOver(isGameOver(pvPGameSession.getTetrominoPlayer1()));
            } else {
                moveTetromino(pvPGameSession.getTetrominoPlayer1(), pvPGameSession.getGameMatrixPlayer1(), 2, sessionPlayer1);
            }
            if (tetrominoIsTouchedGround(pvPGameSession.getTetrominoPlayer2(), pvPGameSession.getGameMatrixPlayer2())) {
                putTetrominoOnGround(pvPGameSession.getTetrominoPlayer2(), pvPGameSession.getGameMatrixPlayer2());
                pvPGameSession.setTetrominoPlayer2(pvPGameSession.getNextTetrominoPlayer2());
                pvPGameSession.setNextTetrominoPlayer2(tetrominoFactory2.getNextTetromino());
                pvPGameSession.setGameOver(isGameOver(pvPGameSession.getTetrominoPlayer2()));
            } else {
                moveTetromino(pvPGameSession.getTetrominoPlayer2(), pvPGameSession.getGameMatrixPlayer2(), 2, sessionPlayer2);
            }
            sendPlayersGameSessionState();
        }
    }

    public void pauseGame() {

    }

    public void moveTetromino(Tetromino tetromino, ArrayList<Square[]> board, int direction, WebSocketSession sessionPlayer) {
        if (tetrominoIsTouchWall(tetromino, board, direction)) {
            return;
        }
        switch (direction) {
            case (1) -> tetromino.moveRight();
            case (2) -> tetromino.moveDown();
            case (3) -> tetromino.moveLeft();
        }
        if (tetrominoIsTouchedGround(tetromino, board)) {
            pvPGameSession.setGameOver(isGameOver(tetromino));
            putTetrominoOnGround(tetromino, board);
            if (sessionPlayer == sessionPlayer1) {
                pvPGameSession.setTetrominoPlayer1(pvPGameSession.getNextTetrominoPlayer1());
                pvPGameSession.setNextTetrominoPlayer1(tetrominoFactory1.getNextTetromino());
            }else {
                pvPGameSession.setTetrominoPlayer2(pvPGameSession.getNextTetrominoPlayer2());
                pvPGameSession.setNextTetrominoPlayer2(tetrominoFactory2.getNextTetromino());
            }
        }
        removeFilledLines(board, sessionPlayer);
        sendPlayersGameSessionState();
//        LOGGER.trace("Tetromino moved");
    }

    public void rotateTetromino(Tetromino tetromino, ArrayList<Square[]> board) {
        tetromino.rotate(board, boardColor);
        sendPlayersGameSessionState();
//        LOGGER.trace("Tetromino rotated");
    }

    public void holdTetromino(WebSocketSession sessionPlayer) {
        if (sessionPlayer.getId().equals(sessionPlayer1.getId())){
            if (isHoldTetrominoPlayer1) {
                Tetromino tmpTetromino = pvPGameSession.getHoldTetrominoPlayer1();
                pvPGameSession.setHoldTetrominoPlayer1(pvPGameSession.getTetrominoPlayer1());
                pvPGameSession.setTetrominoPlayer1(tmpTetromino);
            } else {
                pvPGameSession.setHoldTetrominoPlayer1(pvPGameSession.getTetrominoPlayer1());
                pvPGameSession.setTetrominoPlayer1(pvPGameSession.getNextTetrominoPlayer1());
                pvPGameSession.setNextTetrominoPlayer1(tetrominoFactory1.getNextTetromino());
                isHoldTetrominoPlayer1 = true;
            }
        }else {
            if (isHoldTetrominoPlayer2) {
                Tetromino tmpTetromino = pvPGameSession.getHoldTetrominoPlayer2();
                pvPGameSession.setHoldTetrominoPlayer2(pvPGameSession.getTetrominoPlayer2());
                pvPGameSession.setTetrominoPlayer2(tmpTetromino);
            } else {
                pvPGameSession.setHoldTetrominoPlayer2(pvPGameSession.getTetrominoPlayer2());
                pvPGameSession.setTetrominoPlayer2(pvPGameSession.getNextTetrominoPlayer2());
                pvPGameSession.setNextTetrominoPlayer2(tetrominoFactory2.getNextTetromino());
                isHoldTetrominoPlayer2 = true;
            }
        }
        sendPlayersGameSessionState();
//        LOGGER.trace("Tetromino holded");
    }

    private void removeFilledLines(ArrayList<Square[]> board, WebSocketSession sessionPlayer) {
        int countRemovedLines = 0;
        for (int i = 0; i < ROWS; i++) {
            int countFilledSquare = 0;
            for (int j = 0; j < COLUMNS; j++) {
                if (board.get(i)[j].getColor() != boardColor) countFilledSquare++;
            }
            if (countFilledSquare == COLUMNS) {
                board.remove(i);
                board.add(0, new Square[COLUMNS]);
                for (int j = 0; j < COLUMNS; j++) {
                    board.get(0)[j] = new Square(0, 0, i, j, boardColor);
                }
                countRemovedLines++;
                if ((sessionPlayer == sessionPlayer1)) {
                    pvPGameSession.increaseLinesClearedPlayer1(1);
                } else {
                    pvPGameSession.increaseLinesClearedPlayer2(1);
                }
            }
        }
        if (countRemovedLines == 1) {
            if ((sessionPlayer == sessionPlayer1)) {
                pvPGameSession.increaseScorePlayer1(100);
            } else {
                pvPGameSession.increaseScorePlayer2(100);
            }
        } else if (countRemovedLines == 2) {
            if ((sessionPlayer == sessionPlayer1)) {
                pvPGameSession.increaseScorePlayer1(300);
            } else {
                pvPGameSession.increaseScorePlayer2(300);
            }
        } else if (countRemovedLines == 3) {
            if ((sessionPlayer == sessionPlayer1)) {
                pvPGameSession.increaseScorePlayer1(700);
            } else {
                pvPGameSession.increaseScorePlayer2(700);
            }
        } else if (countRemovedLines == 4) {
            if ((sessionPlayer == sessionPlayer1)) {
                pvPGameSession.increaseScorePlayer1(1500);
            } else {
                pvPGameSession.increaseScorePlayer2(1500);
            }
        }
//        LOGGER.trace("filed line removed");
    }

    private boolean tetrominoIsTouchWall(Tetromino tetromino, ArrayList<Square[]> board,int direction) {
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

    private boolean tetrominoIsTouchedGround(Tetromino tetromino, ArrayList<Square[]> board) {
        for (Square square : tetromino.getBlocks()) {
            int col = square.getCol();
            int row = square.getRow();
            if (row + 1 >= ROWS || board.get(row + 1)[col].getColor() != boardColor) {
                return true;
            }
        }
//        LOGGER.trace("Tetromino touched ground");
        return false;
    }

    private boolean isGameOver(Tetromino tetromino) {
        for (Square square : tetromino.getBlocks()) {
            if (square.getRow() == 0 && square.getCol() == 4) return true;
        }
        return false;
    }

    private void putTetrominoOnGround(Tetromino tetromino, ArrayList<Square[]> board) {
        for (Square square : tetromino.getBlocks()) {
            board.get(square.getRow())[square.getCol()].setColor(square.getColor());
        }
    }

    private void sendPlayersGameSessionState() {
        synchronized (sessionPlayer1) {
            synchronized (sessionPlayer2) {
                try {
                    sessionPlayer1.sendMessage(new TextMessage(pvPGameSession.serialize()));
                    sessionPlayer2.sendMessage(new TextMessage(pvPGameSession.serialize()));
                } catch (IOException e) {
                    System.err.println("Error sending PvPGameSession");
                }
            }
        }
    }
}

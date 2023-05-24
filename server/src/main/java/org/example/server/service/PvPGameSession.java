package org.example.server.service;


import lombok.Data;
import org.example.server.util.TetrisConstants;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import static org.example.server.util.TetrisConstants.*;
import org.example.server.model.*;

import java.io.IOException;
import java.util.ArrayList;

@Data
public class PvPGameSession {
//    private static final Logger log = LogManager.getLogger(PvPGameSession.class);
    private final PvPGameModel pvPGameModel;
    private final WebSocketSession player1Session;
    private final WebSocketSession player2Session;
    private final TetrominoFactory tetrominoFactory;
    private boolean isHoldTetrominoPlayer1;
    private boolean isHoldTetrominoPlayer2;

    public PvPGameSession(WebSocketSession player1Session, WebSocketSession player2Session) {
        this.player1Session = player1Session;
        this.player2Session = player2Session;
        this.pvPGameModel = new PvPGameModel();

        pvPGameModel.setGameMatrixPlayer1(initBoard());
        pvPGameModel.setGameMatrixPlayer2(initBoard());

        this.tetrominoFactory = new TetrominoFactory(player1Session, player2Session);

        pvPGameModel.setTetrominoPlayer1(tetrominoFactory.getNextTetromino(player1Session));
        pvPGameModel.setTetrominoPlayer2(tetrominoFactory.getNextTetromino(player2Session));

        pvPGameModel.setNextTetrominoPlayer1(tetrominoFactory.getNextTetromino(player1Session));
        pvPGameModel.setNextTetrominoPlayer2(tetrominoFactory.getNextTetromino(player2Session));
    }

    private ArrayList<Square[]> initBoard() {
        ArrayList<Square[]> board = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            board.add(i, new Square[COLUMNS]);
            for (int j = 0; j < COLUMNS; j++) {
                int x = (j * TetrisConstants.BLOCK_SIZE);
                int y = (i * TetrisConstants.BLOCK_SIZE);
                board.get(i)[j] = new Square(x, y, i, j, BOARD_COLOR);
            }
        }
        return board;
    }

    public void startGame() {
        while (!pvPGameModel.isGameOver()) {
            try {
                Thread.sleep(pvPGameModel.getGameSpeed());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tetrominoIsTouchedGround(pvPGameModel.getTetrominoPlayer1(), pvPGameModel.getGameMatrixPlayer1())) {
                putTetrominoOnGround(pvPGameModel.getTetrominoPlayer1(), pvPGameModel.getGameMatrixPlayer1());
                updateTetrominoByPlayerSession(player1Session);
                pvPGameModel.setGameOver(isGameOver(pvPGameModel.getTetrominoPlayer1()));
            } else {
                moveTetromino(pvPGameModel.getTetrominoPlayer1(), pvPGameModel.getGameMatrixPlayer1(), 2, player1Session);
            }

            if (tetrominoIsTouchedGround(pvPGameModel.getTetrominoPlayer2(), pvPGameModel.getGameMatrixPlayer2())) {
                putTetrominoOnGround(pvPGameModel.getTetrominoPlayer2(), pvPGameModel.getGameMatrixPlayer2());
                updateTetrominoByPlayerSession(player2Session);
                pvPGameModel.setGameOver(isGameOver(pvPGameModel.getTetrominoPlayer2()));
            } else {
                moveTetromino(pvPGameModel.getTetrominoPlayer2(), pvPGameModel.getGameMatrixPlayer2(), 2, player2Session);
            }

            sendPlayersGameSessionState();
        }
    }

    public void pauseGame() {

    }

    public void moveTetromino(Tetromino tetromino, ArrayList<Square[]> board, int direction, WebSocketSession playerSession) {
        if (tetrominoIsTouchWall(tetromino, board, direction)) {
            return;
        }
        switch (direction) {
            case (1) -> tetromino.moveRight();
            case (2) -> tetromino.moveDown();
            case (3) -> tetromino.moveLeft();
        }
        if (tetrominoIsTouchedGround(tetromino, board)) {
            pvPGameModel.setGameOver(isGameOver(tetromino));
            putTetrominoOnGround(tetromino, board);
            updateTetrominoByPlayerSession(playerSession);
        }
        removeFilledLines(board, playerSession);
        sendPlayersGameSessionState();
//        log.debug("Tetromino moved");
    }

    public void rotateTetromino(Tetromino tetromino, ArrayList<Square[]> board) {
        tetromino.rotate(board, BOARD_COLOR);
        sendPlayersGameSessionState();
//        log.debug("Tetromino rotated");
    }

    public void holdTetromino(WebSocketSession sessionPlayer) {
        if (sessionPlayer.getId().equals(player1Session.getId())){
            if (isHoldTetrominoPlayer1) {
                Tetromino tmpTetromino = pvPGameModel.getHoldTetrominoPlayer1();
                pvPGameModel.setHoldTetrominoPlayer1(pvPGameModel.getTetrominoPlayer1());
                pvPGameModel.setTetrominoPlayer1(tmpTetromino);
            } else {
                pvPGameModel.setHoldTetrominoPlayer1(pvPGameModel.getTetrominoPlayer1());
                pvPGameModel.setTetrominoPlayer1(pvPGameModel.getNextTetrominoPlayer1());
                pvPGameModel.setNextTetrominoPlayer1(tetrominoFactory.getNextTetromino(player1Session));
                isHoldTetrominoPlayer1 = true;
            }
        }else {
            if (isHoldTetrominoPlayer2) {
                Tetromino tmpTetromino = pvPGameModel.getHoldTetrominoPlayer2();
                pvPGameModel.setHoldTetrominoPlayer2(pvPGameModel.getTetrominoPlayer2());
                pvPGameModel.setTetrominoPlayer2(tmpTetromino);
            } else {
                pvPGameModel.setHoldTetrominoPlayer2(pvPGameModel.getTetrominoPlayer2());
                pvPGameModel.setTetrominoPlayer2(pvPGameModel.getNextTetrominoPlayer1());
                pvPGameModel.setNextTetrominoPlayer1(tetrominoFactory.getNextTetromino(player1Session));
                isHoldTetrominoPlayer2 = true;
            }
        }
        sendPlayersGameSessionState();
//        log.debug("Tetromino holded");
    }

    private void removeFilledLines(ArrayList<Square[]> board, WebSocketSession playerSession) {
        int countRemovedLines = 0;
        for (int i = 0; i < ROWS; i++) {
            int countFilledSquare = 0;
            for (int j = 0; j < COLUMNS; j++) {
                if (board.get(i)[j].getColor() != BOARD_COLOR) countFilledSquare++;
            }
            if (countFilledSquare == COLUMNS) {
                board.remove(i);
                board.add(0, new Square[COLUMNS]);
                for (int j = 0; j < COLUMNS; j++) {
                    board.get(0)[j] = new Square(0, 0, i, j, BOARD_COLOR);
                }
                countRemovedLines++;
                increaseLinesClearedByPlayerSession(playerSession, 1);
            }
        }
        switch (countRemovedLines) {
            case (1) -> increaseScoreByPlayerSession(playerSession, 100);
            case (2) -> increaseScoreByPlayerSession(playerSession, 300);
            case (3) -> increaseScoreByPlayerSession(playerSession, 700);
            case (4) -> increaseScoreByPlayerSession(playerSession, 1500);
        }
//        log.debug("filed line removed");
    }

    private boolean tetrominoIsTouchWall(Tetromino tetromino, ArrayList<Square[]> board,int direction) {
        for (Square square : tetromino.getBlocks()) {
            int col = square.getCol();
            int row = square.getRow();
            if (direction == 3 && col > 0 && board.get(row)[col - 1].getColor() != BOARD_COLOR) {
                return true;
            } else if (direction == 1 && col < (COLUMNS - 1) && board.get(row)[col + 1].getColor() != BOARD_COLOR) {
                return true;
            }
        }
//        log.debug("Tetromino touched wall");
        return false;
    }

    private boolean tetrominoIsTouchedGround(Tetromino tetromino, ArrayList<Square[]> board) {
        for (Square square : tetromino.getBlocks()) {
            int col = square.getCol();
            int row = square.getRow();
            if (row + 1 >= ROWS || board.get(row + 1)[col].getColor() != BOARD_COLOR) {
                return true;
            }
        }
//        log.debug("Tetromino touched ground");
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
        synchronized (player1Session) {
            synchronized (player2Session) {
                try {
                    player1Session.sendMessage(new TextMessage(pvPGameModel.serialize()));
                    player2Session.sendMessage(new TextMessage(pvPGameModel.serialize()));
                } catch (IOException e) {
                    System.err.println("Error sending PvPGameModel");
                }
            }
        }
    }

    private void increaseScoreByPlayerSession(WebSocketSession playerSession, int value) {
        if ((playerSession == player1Session)) {
            pvPGameModel.setScorePlayer1(pvPGameModel.getScorePlayer1() + value);
        } else {
            pvPGameModel.setScorePlayer2(pvPGameModel.getScorePlayer2() + value);
        }
    }

    private void increaseLinesClearedByPlayerSession(WebSocketSession playerSession, int value) {
        if ((playerSession == player1Session)) {
            pvPGameModel.setLinesClearedPlayer1(pvPGameModel.getLinesClearedPlayer1() + value);
        } else {
            pvPGameModel.setLinesClearedPlayer2(pvPGameModel.getLinesClearedPlayer2() + value);
        }
    }

    private void updateTetrominoByPlayerSession(WebSocketSession playerSession) {
        if (playerSession == player1Session) {
            pvPGameModel.setTetrominoPlayer1(pvPGameModel.getNextTetrominoPlayer1());
            pvPGameModel.setNextTetrominoPlayer1(tetrominoFactory.getNextTetromino(player1Session));
        }else {
            pvPGameModel.setTetrominoPlayer2(pvPGameModel.getNextTetrominoPlayer2());
            pvPGameModel.setNextTetrominoPlayer2(tetrominoFactory.getNextTetromino(player2Session));
        }
    }
}
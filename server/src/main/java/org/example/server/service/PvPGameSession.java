package org.example.server.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.server.util.TetrisConstants;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import static org.example.server.util.TetrisConstants.*;
import org.example.server.model.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * PvPGameSession class represents a game session for a player versus player (PvP) Tetris game.
 * It manages the game state, players' sessions, tetrominos, and game logic.
 *
 * @version 1.0
 * @author Denis Moskvin
 */
@Data
public class PvPGameSession {
    private static final Logger log = LogManager.getLogger(PvPGameSession.class);
    private final PvPGameModel pvPGameModel;
    private final WebSocketSession player1Session;
    private final WebSocketSession player2Session;
    private final TetrominoFactory tetrominoFactory;
    private boolean isHoldTetrominoPlayer1;
    private boolean isHoldTetrominoPlayer2;

    /**
     * Constructs a new PvPGameSession with the specified player sessions.
     *
     * @param player1Session The WebSocketSession for player 1.
     * @param player2Session The WebSocketSession for player 2.
     */
    public PvPGameSession(WebSocketSession player1Session, WebSocketSession player2Session) {
        this.player1Session = player1Session;
        this.player2Session = player2Session;
        this.pvPGameModel = new PvPGameModel(player1Session.getId(), player2Session.getId());

        pvPGameModel.setGameMatrixPlayer1(initBoard());
        pvPGameModel.setGameMatrixPlayer2(initBoard());

        this.tetrominoFactory = new TetrominoFactory(player1Session, player2Session);

        pvPGameModel.setTetrominoPlayer1(tetrominoFactory.getNextTetromino(player1Session));
        pvPGameModel.setTetrominoPlayer2(tetrominoFactory.getNextTetromino(player2Session));

        pvPGameModel.setNextTetrominoPlayer1(tetrominoFactory.getNextTetromino(player1Session));
        pvPGameModel.setNextTetrominoPlayer2(tetrominoFactory.getNextTetromino(player2Session));
    }

    /**
     * Initializes the game board by creating an ArrayList of Square arrays.
     * Each Square represents a cell on the game board.
     *
     * @return The initialized game board represented as an ArrayList of Square arrays.
     */
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

    /**
     * Starts the game by executing the game logic in separate threads for player 1 and player 2.
     * The game continues until either player 1 or player 2 is game over.
     */
    public void startGame() {
        log.info("game started");
        new Thread(() -> {
            while (!pvPGameModel.isGameOverPlayer1()) {
                try {
                    Thread.sleep(pvPGameModel.getGameSpeed());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (tetrominoIsTouchedGround(pvPGameModel.getTetrominoPlayer1(), pvPGameModel.getGameMatrixPlayer1())) {
                    putTetrominoOnGround(pvPGameModel.getTetrominoPlayer1(), pvPGameModel.getGameMatrixPlayer1());
                    updateTetrominoByPlayerSession(player1Session);
                    updateGameOverStateByPlayerSession(player1Session);
                } else {
                    moveTetromino(pvPGameModel.getTetrominoPlayer1(), pvPGameModel.getGameMatrixPlayer1(), 2, player1Session);
                }

                sendPlayersGameSessionState();
            }
            destroyPlayerSessionData(player1Session);
            sendPlayersGameSessionState();
        }).start();

        new Thread(() -> {
            while (!pvPGameModel.isGameOverPlayer2()) {
                try {
                    Thread.sleep(pvPGameModel.getGameSpeed());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (tetrominoIsTouchedGround(pvPGameModel.getTetrominoPlayer2(), pvPGameModel.getGameMatrixPlayer2())) {
                    putTetrominoOnGround(pvPGameModel.getTetrominoPlayer2(), pvPGameModel.getGameMatrixPlayer2());
                    updateTetrominoByPlayerSession(player2Session);
                    updateGameOverStateByPlayerSession(player2Session);
                } else {
                    moveTetromino(pvPGameModel.getTetrominoPlayer2(), pvPGameModel.getGameMatrixPlayer2(), 2, player2Session);
                }

                sendPlayersGameSessionState();
            }
            destroyPlayerSessionData(player2Session);
            sendPlayersGameSessionState();
        }).start();
    }

    /**
     * Destroys the player session data by setting the game matrix, hold tetromino, and next tetromino to null.
     *
     * @param playerSession The WebSocketSession of the player.
     */
    private void destroyPlayerSessionData(WebSocketSession playerSession){
        if (playerSession.getId().equals(player1Session.getId())) {
            pvPGameModel.setGameMatrixPlayer1(null);
            pvPGameModel.setHoldTetrominoPlayer1(null);
            pvPGameModel.setNextTetrominoPlayer1(null);
        }else {
            pvPGameModel.setGameMatrixPlayer2(null);
            pvPGameModel.setHoldTetrominoPlayer2(null);
            pvPGameModel.setNextTetrominoPlayer2(null);
        }
    }

    public void pauseGame() {

    }

    /**
     * Moves the tetromino on the game board in the specified direction.
     * If the tetromino touches the wall, it will not move.
     * If the tetromino touches the ground, it will be put on the ground and the game state will be updated.
     * Filled lines will be removed from the game board and the game state will be updated.
     *
     * @param tetromino      The tetromino to move.
     * @param board          The game board.
     * @param direction      The direction to move the tetromino (1 = right, 2 = down, 3 = left).
     * @param playerSession  The WebSocketSession of the player.
     */
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
            updateGameOverStateByPlayerSession(playerSession);
            putTetrominoOnGround(tetromino, board);
            updateTetrominoByPlayerSession(playerSession);
        }
        removeFilledLines(board, playerSession);
        sendPlayersGameSessionState();
        log.debug("Tetromino moved");
    }

    /**
     * Rotates the tetromino on the game board.
     * Sends the updated game session state to both players.
     *
     * @param tetromino The tetromino to rotate.
     * @param board     The game board.
     */
    public void rotateTetromino(Tetromino tetromino, ArrayList<Square[]> board) {
        tetromino.rotate(board, BOARD_COLOR);
        sendPlayersGameSessionState();
        log.debug("Tetromino rotated");
    }

    /**
     * Holds the current tetromino for the player.
     * If the hold tetromino is available, swaps it with the current tetromino.
     * If the hold tetromino is not available, sets the hold tetromino as the current tetromino
     * and gets the next tetromino as the current tetromino.
     * Sends the updated game session state to both players.
     *
     * @param sessionPlayer The WebSocketSession of the player.
     */
    public void holdTetromino(WebSocketSession sessionPlayer) {
        System.out.println(sessionPlayer.getId());
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
        log.debug("Tetromino holded");
    }

    /**
     * Removes filled lines from the game board.
     * Increases the score and lines cleared for the player.
     * Sends the updated game session state to both players.
     *
     * @param board          The game board.
     * @param playerSession  The WebSocketSession of the player.
     */
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
        log.debug("filed line removed");
    }

    /**
     * Checks if the tetromino touches the wall in the specified direction.
     *
     * @param tetromino  The tetromino to check.
     * @param board      The game board.
     * @param direction  The direction to move the tetromino (1 = right, 3 = left).
     * @return True if the tetromino touches the wall, False otherwise.
     */
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
        log.debug("Tetromino touched wall");
        return false;
    }

    /**
     * Checks if the tetromino touches the ground.
     *
     * @param tetromino  The tetromino to check.
     * @param board      The game board.
     * @return True if the tetromino touches the ground, False otherwise.
     */
    private boolean tetrominoIsTouchedGround(Tetromino tetromino, ArrayList<Square[]> board) {
        for (Square square : tetromino.getBlocks()) {
            int col = square.getCol();
            int row = square.getRow();
            if (row + 1 >= ROWS || board.get(row + 1)[col].getColor() != BOARD_COLOR) {
                return true;
            }
        }
        log.debug("Tetromino touched ground");
        return false;
    }

    /**
     * Checks if the tetromino causes the game to be over.
     *
     * @param tetromino The tetromino to check.
     * @return True if the tetromino causes the game to be over, False otherwise.
     */
    private boolean isGameOver(Tetromino tetromino) {
        for (Square square : tetromino.getBlocks()) {
            if (square.getRow() == 0 && square.getCol() == 4) return true;
        }
        return false;
    }

    /**
     * Puts the tetromino on the ground by updating the game board.
     *
     * @param tetromino The tetromino to put on the ground.
     * @param board     The game board.
     */
    private void putTetrominoOnGround(Tetromino tetromino, ArrayList<Square[]> board) {
        for (Square square : tetromino.getBlocks()) {
            board.get(square.getRow())[square.getCol()].setColor(square.getColor());
        }
    }

    /**
     * Sends the game session state to both players.
     */
    private void sendPlayersGameSessionState() {
        if (player1Session.isOpen()) {
            sendGameSessionState(player1Session);
        }else if (!pvPGameModel.isGameOverPlayer1()){
            pvPGameModel.setGameOverPlayer1(true);
            destroyPlayerSessionData(player1Session);
        }
        if (player2Session.isOpen()) {
            sendGameSessionState(player2Session);
        }else if (!pvPGameModel.isGameOverPlayer2()){
            pvPGameModel.setGameOverPlayer2(true);
            destroyPlayerSessionData(player2Session);
        }
    }

    /**
     * Sends the game session state to a specific player.
     *
     * @param playerSession The WebSocket session of the player.
     */
    private void sendGameSessionState(WebSocketSession playerSession) {
        synchronized (playerSession) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("eventType", "updateGameSession");
                objectNode.put("pvPGameModel", pvPGameModel.serialize());

                String jsonMessage = objectMapper.writeValueAsString(objectNode);

                playerSession.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                log.error("Error sending PvPGameModel");
                e.printStackTrace();
            }
        }
    }

    /**
     * Increases the score of the player session by the specified value.
     *
     * @param playerSession The WebSocket session of the player.
     * @param value         The value to increase the score by.
     */
    private void increaseScoreByPlayerSession(WebSocketSession playerSession, int value) {
        if ((playerSession == player1Session)) {
            pvPGameModel.setScorePlayer1(pvPGameModel.getScorePlayer1() + value);
        } else {
            pvPGameModel.setScorePlayer2(pvPGameModel.getScorePlayer2() + value);
        }
    }

    /**
     * Increases the number of lines cleared by the player session by the specified value.
     *
     * @param playerSession The WebSocket session of the player.
     * @param value         The value to increase the lines cleared by.
     */
    private void increaseLinesClearedByPlayerSession(WebSocketSession playerSession, int value) {
        if ((playerSession == player1Session)) {
            pvPGameModel.setLinesClearedPlayer1(pvPGameModel.getLinesClearedPlayer1() + value);
        } else {
            pvPGameModel.setLinesClearedPlayer2(pvPGameModel.getLinesClearedPlayer2() + value);
        }
    }

    /**
     * Updates the current tetromino and next tetromino for the player session.
     *
     * @param playerSession The WebSocket session of the player.
     */
    private void updateTetrominoByPlayerSession(WebSocketSession playerSession) {
        if (playerSession == player1Session) {
            pvPGameModel.setTetrominoPlayer1(pvPGameModel.getNextTetrominoPlayer1());
            pvPGameModel.setNextTetrominoPlayer1(tetrominoFactory.getNextTetromino(player1Session));
        }else {
            pvPGameModel.setTetrominoPlayer2(pvPGameModel.getNextTetrominoPlayer2());
            pvPGameModel.setNextTetrominoPlayer2(tetrominoFactory.getNextTetromino(player2Session));
        }
    }

    /**
     * Updates the game over state for the player session.
     *
     * @param playerSession The WebSocket session of the player.
     */
    private void updateGameOverStateByPlayerSession(WebSocketSession playerSession) {
        if (playerSession == player1Session) {
            pvPGameModel.setGameOverPlayer1(isGameOver(pvPGameModel.getTetrominoPlayer1()));
        }else {
            pvPGameModel.setGameOverPlayer2(isGameOver(pvPGameModel.getTetrominoPlayer2()));
        }
    }
}

package tetris.view;

import tetris.model.GameModel;
import tetris.model.SoloGameModel;
import tetris.model.Tetromino;
import static tetris.resource.ResourceManager.*;

import javax.swing.*;
import java.awt.*;

public class HoldPanel extends JLabel {
    GameModel gameModel;
    private final int BLOCK_SIZE = 25;
    private final int WIGHT = 120;
    private final int HEIGHT = 110;
    private final int offsetYForShape = 30;
    private final int offsetXForShape = 10;
    HoldPanel(GameModel gameModel){
        this.gameModel = gameModel;
        gameModel.addHoldPanel(this);
        setPreferredSize(new Dimension(WIGHT, HEIGHT));
        setSize(getPreferredSize());
        setIcon(getImg("backgroundForHoldShapePanel.png", WIGHT, HEIGHT));

    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Tetromino holdTetromino = gameModel.getHoldTetromino(this);
        if (holdTetromino != null) {
            boolean[][] holdTetrominoShape = holdTetromino.getShape();
            for (int i = 0; i < holdTetrominoShape.length; i++) {
                for (int j = 0; j < holdTetrominoShape[i].length; j++) {
                    if (holdTetrominoShape[i][j]) {
                        int x = j * BLOCK_SIZE + offsetXForShape;
                        int y = i * BLOCK_SIZE + offsetYForShape;
                        g.setColor(holdTetromino.getColor());
                        g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        }
    }
}

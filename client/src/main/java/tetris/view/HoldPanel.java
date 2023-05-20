package tetris.view;

import tetris.model.GameModel;
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
        setPreferredSize(new Dimension(WIGHT, HEIGHT));
        setSize(getPreferredSize());
        setIcon(getImg("backgroundForHoldShapePanel.png", WIGHT, HEIGHT));

    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Tetromino tetromino = gameModel.getHoldTetromino();
        boolean[][] tetrominoShape = new boolean[0][0];
        if (tetromino != null) tetrominoShape = gameModel.getHoldTetromino().getShape();
        for (int i = 0; i < tetrominoShape.length; i++){
            for (int j = 0; j < tetrominoShape[i].length; j++){
                if (tetrominoShape[i][j]){
                    int x = j * BLOCK_SIZE + offsetXForShape;
                    int y = i * BLOCK_SIZE + offsetYForShape;
                    g.setColor(gameModel.getNextTetromino().getColor());
                    g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }
}

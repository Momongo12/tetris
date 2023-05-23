package tetris.view;

import tetris.model.GameModel;
import tetris.model.SoloGameModel;
import tetris.model.Tetromino;

import static tetris.resource.ResourceManager.*;

import javax.swing.*;
import java.awt.*;

public class NextShapePanel extends JLabel {
    GameModel gameModel;
    private final int BLOCK_SIZE = 25;
    private final int WIGHT = 120;
    private final int HEIGHT = 110;
    private final int offsetYForShape = 30;
    private final int offsetXForShape = 10;
    public NextShapePanel(GameModel gameModel){
        this.gameModel = gameModel;
        gameModel.addNextShapePanel(this);
        setPreferredSize(new Dimension(WIGHT, HEIGHT));
        setSize(getPreferredSize());
        setBackground(new Color(5, 110, 225));
        setIcon(getImg("backgroundForNextShapePanel.png", WIGHT, HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Tetromino nextTetromino = gameModel.getNextTetromino(this);
        if (nextTetromino != null) {
            boolean[][] tetrominoShape = nextTetromino.getShape();
            for (int i = 0; i < tetrominoShape.length; i++){
                for (int j = 0; j < tetrominoShape[i].length; j++){
                    if (tetrominoShape[i][j]){
                        int x = j * BLOCK_SIZE + offsetXForShape;
                        int y = i * BLOCK_SIZE + offsetYForShape;
                        g.setColor(gameModel.getNextTetromino(this).getColor());
                        g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        }
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }
}

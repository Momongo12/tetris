package tetris.view;

import tetris.model.GameModel;
import tetris.model.Tetromino;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameMatrix extends JPanel {
    GameModel gameModel;
    private int BLOCK_SIZE = 25;
    GameMatrix(GameModel gameModel){
        this.gameModel = gameModel;
        setPreferredSize(new Dimension(250, 500));
        setSize(getPreferredSize());
        setFocusable(false);
        setBackground(Color.red);
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        ArrayList<Square[]> board = gameModel.getBoard();
        Tetromino tetromino = gameModel.getTetromino();
        for (int i = 0; i < board.size(); i++){
            for (int j = 0; j < board.get(i).length; j++){
                int x = j * BLOCK_SIZE;
                int y = i * BLOCK_SIZE;
                Color squareColor = board.get(i)[j].getColor();
                g2.setColor(squareColor);
                g2.fillRect(x, y, 24, 24);
                g2.setColor(new Color(17, 82, 164));
                g2.drawRect(x, y, 25, 25);
            }
        }
        drawTetromino(g2, tetromino);
    }

    public void drawTetromino(Graphics2D g, Tetromino tetromino){
        if (tetromino != null){
            ArrayList<Square> blocks = tetromino.getBlocks();
            for (int i = 0; i < blocks.size(); i++){
                int x = blocks.get(i).getX();
                int y = blocks.get(i).getY();
                Color squareColor = blocks.get(i).getColor();
                g.setColor(squareColor);
                g.fillRect(x, y, 24, 24);
            }
        }
        g.dispose();
    }
}

package tetris.model;

import tetris.view.Square;

import static tetris.util.TetrisConstants.*;
import java.awt.*;
import java.util.ArrayList;

public class Tetromino {
    private TetrominoType type;
    private boolean[][] shape;
    ArrayList<Square> blocks;
    private final Color color;
    private int row;
    private int col;

    public Tetromino(TetrominoType type, int row, int col, Color color) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.color = color;
        shape = type.getBlocks();
        blocks = new ArrayList<>();
        updateBlocks();
    }

    public void moveLeft() {
        for (Square square: blocks) {
            if (square.getX() < BLOCK_SIZE){
                return;
            }
        }
        col--;
        updateBlocks();
    }

    public void moveRight() {
        for (Square square: blocks) {
            if (square.getCol() >= COLUMNS - 1){
                return;
            }
        }
        col++;
        updateBlocks();
    }

    public void moveDown() {
        row++;
        updateBlocks();
    }

    public void rotate(ArrayList<Square[]> board, Color boardColor) {
        boolean[][] newShape = new boolean[type.getHeight()][type.getWidth()];
        for (int i = 0; i < type.getHeight(); i++) {
            for (int j = 0; j < type.getWidth(); j++) {
                if (type == TetrominoType.I){
                    newShape[j][i] = shape[i][j];
                }else {
                    newShape[j][i] = shape[type.getHeight() - 1 - i][j];
                }
            }
        }
        for (int i = 0; i < newShape.length; i++){
            for (int j = 0; j < newShape[i].length; j++){
                if (newShape[i][j]){
                    int posX = (col + j) * (BLOCK_SIZE);
                    int posY = (row  + i) * (BLOCK_SIZE);
                    if (posX + BLOCK_SIZE > (BLOCK_SIZE * COLUMNS) || posY + BLOCK_SIZE > (BLOCK_SIZE * ROWS)){
                        return;
                    }
                    if (board.get(row + i)[col + j].getColor() != boardColor){
                        return;
                    }
                }
            }
        }
        shape = newShape;
        updateBlocks();
    }

    public boolean[][] getShape() {
        return shape;
    }
    public ArrayList<Square> getBlocks(){
        return blocks;
    }

    public void updateBlocks(){
        blocks.clear();
        for (int i = 0; i < shape.length; i++){
            for (int j = 0; j < shape[i].length; j++){
                if (shape[i][j]){
                    int posX = (col + j) * (BLOCK_SIZE);
                    int posY = (row  + i) * (BLOCK_SIZE);
                    blocks.add(new Square(posX, posY, col + j, row + i, color));
                }
            }
        }
    }

    public Color getColor() { return color; }
}

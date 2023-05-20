package tetris.view;

import java.awt.*;

public class Square {
    private int x;
    private int y;
    private int col;
    private int row;
    private Color color;

    public Square(int x, int y, int col, int row, Color color) {
        this.x = x;
        this.y = y;
        this.col = col;
        this.row = row;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() { return y; }

    public int getCol() { return col; }

    public int getRow() { return row; }

    public void setX(int newPosX) { x = newPosX; }

    public void setY(int newPosY) { y = newPosY; }

    public void setCol(int col) { this.col = col; }

    public void setRow(int row) { this.row = row; }

    public void setColor(Color color) { this.color = color; }
}

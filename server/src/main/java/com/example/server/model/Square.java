package com.example.server.model;

import lombok.Data;

import java.awt.*;

@Data
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
}

package org.example.server.model;

/**
 * @version 1.0
 * @author Denis Moskvin
 */
public enum TetrominoType {
    I(new boolean[][]{
            {true, true, true, true},
            {false, false, false, false},
            {false, false, false, false},
            {false, false, false, false}

    }),
    J(new boolean[][]{
            {true, false, false},
            {true, true, true},
            {false, false, false}
    }),
    L(new boolean[][]{
            {false, false, true},
            {true, true, true},
            {false, false, false}
    }),
    O(new boolean[][]{
            {true, true},
            {true, true}
    }),
    S(new boolean[][]{
            {false, true, true},
            {true, true, false},
            {false, false, false}
    }),
    T(new boolean[][]{
            {false, true, false},
            {true, true, true},
            { false, false, false}
    }),
    Z(new boolean[][]{
            {true, true, false},
            {false, true, true},
            { false, false, false}
    });

    private boolean[][] blocks;

    TetrominoType(boolean[][] blocks) {
        this.blocks = blocks;
    }

    public boolean[][] getBlocks() {
        return blocks;
    }

    public int getWidth() {
        return blocks[0].length;
    }

    public int getHeight() {
        return blocks.length;
    }
}
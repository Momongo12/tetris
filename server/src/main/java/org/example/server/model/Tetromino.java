package org.example.server.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.example.server.util.TetrisConstants.*;

/**
 * This class represent tetromino figure and methods for use it.
 * @version 1.0
 * @author Denis Moskvin
 */
public class Tetromino {
    private TetrominoType type;
    private boolean[][] shape;
    ArrayList<Square> blocks;

    @JsonSerialize(using = ColorSerializer.class)
    @JsonDeserialize(using = ColorDeserializer.class)
    private Color color;
    private int row;
    private int col;

    /**
     * Creates a new Tetromino object with the specified type, position, and color.
     *
     * @param type  The type of the Tetromino.
     * @param row   The row position of the Tetromino.
     * @param col   The column position of the Tetromino.
     * @param color The color of the Tetromino.
     */
    public Tetromino(TetrominoType type, int row, int col, Color color) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.color = color;
        shape = type.getBlocks();
        blocks = new ArrayList<>();
        updateBlocks();
    }

    public Tetromino() {

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

    public static class ColorSerializer extends JsonSerializer<Color> {
        @Override
        public void serialize(Color value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("argb");
            gen.writeString(Integer.toHexString(value.getRGB()));
            gen.writeEndObject();
        }
    }

    public static class ColorDeserializer extends StdDeserializer<Color> {
        public ColorDeserializer() {
            super(Color.class);
        }

        @Override
        public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TreeNode root = p.getCodec().readTree(p);
            TextNode rgba = (TextNode) root.get("argb");
            if (rgba != null) {
                return new Color(Integer.parseUnsignedInt(rgba.textValue(), 16), true);
            } else {
                // Return a default color value if rgba is null
                return Color.BLACK; // Or any other default color of your choice
            }
        }
    }

    public boolean[][] getShape() {
        return shape;
    }

    public Color getColor() { return color; }
}

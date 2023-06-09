package tetris.model;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author denMoskvin
 * @version 1.0
 */
public class TetrominoFactory {
    private final LinkedList<TetrominoType> queue;
    private final Color[] colors = {new Color(255, 0, 0),  new Color(0, 128, 0), new Color(0,0, 255), new Color(0, 255, 255),
                                new Color(255, 0, 255), new Color(255, 255, 0), new Color(128, 128, 128)};
    Random colorRandomIndex;

    public TetrominoFactory() {
        colorRandomIndex = new Random();
        queue = new LinkedList<>();
        refill();
    }

    public void refill() {
        queue.clear();
        queue.addAll(Arrays.asList(TetrominoType.values()));
        Collections.shuffle(queue);
    }


    public Tetromino getNextTetromino() {
        if (queue.isEmpty()) refill();
        return new Tetromino(queue.removeFirst(), 0, 3, colors[colorRandomIndex.nextInt(colors.length)]);
    }

}

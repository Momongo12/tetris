package tetris;

import org.apache.logging.log4j.Logger;
import tetris.logger.MyLoggerFactory;
import tetris.model.GameLauncher;

public class Tetris {

    private static final Logger LOGGER = MyLoggerFactory.getLogger(Tetris.class);

    public static void main(String [] args){
        try {
            new GameLauncher();
        } catch (Exception e){
            LOGGER.fatal("Game Launcher has been crashed");
        }
    }
}

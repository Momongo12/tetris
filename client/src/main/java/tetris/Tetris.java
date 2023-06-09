package tetris;

import lombok.extern.log4j.Log4j2;
import tetris.model.GameLauncher;

@Log4j2
public class Tetris {
    public static void main(String [] args){
        try {
            new GameLauncher();
        } catch (Exception e){
            log.fatal("Game Launcher has been crashed");
        }
    }
}

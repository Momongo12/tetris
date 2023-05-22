package tetris.controller;

import tetris.model.GameLauncher;

public class LauncherController {
    private GameLauncher gameLauncher;
    public LauncherController(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
    }

    public void handlePvpGameButtonClick() {
        gameLauncher.connectToServer();
        gameLauncher.displayPvPGameField();
    }

    public void handleSoloGameButtonClick() {
        gameLauncher.getLauncherView().displayChooseLevelPanel();
    }
}

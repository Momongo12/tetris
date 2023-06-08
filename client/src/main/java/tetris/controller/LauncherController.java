package tetris.controller;

import lombok.extern.log4j.Log4j2;
import tetris.model.HighScoreTableModel;
import tetris.model.*;
import tetris.model.dto.LoginRequestDto;
import tetris.model.dto.RegistrationRequestDto;
import tetris.service.AuthService;
import tetris.service.HighScoresService;
import tetris.service.PlayerStatsService;

import javax.swing.*;

@Log4j2
public class LauncherController {
    private final GameLauncher gameLauncher;
    private final AuthService authService;
    private final PlayerStatsService playerStatsService;
    private final HighScoresService highScoresService;

    public LauncherController(GameLauncher gameLauncher, AuthService authService, PlayerStatsService playerStatsService, HighScoresService highScoresService) {
        this.authService = authService;
        this.gameLauncher = gameLauncher;
        this.playerStatsService = playerStatsService;
        this.highScoresService = highScoresService;
    }

    public void handlePvpGameButtonClick() {
        gameLauncher.connectToServer();
        gameLauncher.findPvPGameSession();
    }

    public void handleSoloGameButtonClick() {
        if (gameLauncher.getGameModel() instanceof PvPGameModel) {
            gameLauncher.setGameModel(new SoloGameModel(gameLauncher));
        }
        gameLauncher.getLauncherView().displayChooseLevelPanel();
    }

    public void handleHighScoresTableUpdateButtonClick(JTable table, HighScoreTableModel highScoreTableModel) {
        HighScore highScore = highScoresService.getHighScores();
        highScoreTableModel.updateHighScoreData(highScore);
        table.updateUI();
    }

    public void handleLogInButtonClick(LoginRequestDto loginRequestDto) {
        log.info("Player login attempt");
        if (authService.authenticate(loginRequestDto)) {
            gameLauncher.setCurrentPlayer(playerStatsService.getPlayerByEmail(loginRequestDto.getEmail()));
            gameLauncher.getLauncherView().displayLauncherHomepage();
        }
    }

    public void handleRegistrationButtonClick(RegistrationRequestDto registrationRequestDto) {
        log.info("Player registration attempt");
        if (authService.registerPlayer(registrationRequestDto)) {
            gameLauncher.setCurrentPlayer(playerStatsService.getPlayerByEmail(registrationRequestDto.getEmail()));
            gameLauncher.getLauncherView().displayLauncherHomepage();
        }
    }
}

package tetris.controller;

import lombok.extern.log4j.Log4j2;
import tetris.model.GameLauncher;
import tetris.model.dto.LoginRequestDto;
import tetris.model.dto.RegistrationRequestDto;
import tetris.service.AuthService;
import tetris.service.PlayerStatsService;

@Log4j2
public class LauncherController {
    private final GameLauncher gameLauncher;
    private final AuthService authService;
    private final PlayerStatsService playerStatsService;
    public LauncherController(GameLauncher gameLauncher, AuthService authService, PlayerStatsService playerStatsService) {
        this.authService = authService;
        this.gameLauncher = gameLauncher;
        this.playerStatsService = playerStatsService;
    }

    public void handlePvpGameButtonClick() {
        gameLauncher.connectToServer();
        gameLauncher.findPvPGameSession();
    }

    public void handleSoloGameButtonClick() {
        gameLauncher.getLauncherView().displayChooseLevelPanel();
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

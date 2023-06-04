package tetris.controller;

import lombok.extern.log4j.Log4j2;
import tetris.model.GameLauncher;
import tetris.model.dto.LoginRequestDto;
import tetris.model.dto.RegistrationRequestDto;
import tetris.service.AuthService;

@Log4j2
public class LauncherController {
    private GameLauncher gameLauncher;
    private AuthService authService;
    public LauncherController(GameLauncher gameLauncher, AuthService authService) {
        this.authService = authService;
        this.gameLauncher = gameLauncher;
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
        authService.authenticate(loginRequestDto);
        gameLauncher.getLauncherView().displayLauncherHomepage();
    }

    public void handleRegistrationButtonClick(RegistrationRequestDto registrationRequestDto) {
        log.info("Player registration attempt");
        authService.registerPlayer(registrationRequestDto);
        gameLauncher.getLauncherView().displayLauncherHomepage();
    }
}

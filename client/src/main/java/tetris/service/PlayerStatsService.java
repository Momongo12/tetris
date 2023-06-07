package tetris.service;

import tetris.model.Player;

public interface PlayerStatsService {

    Player getPlayerByEmail(String email);
}

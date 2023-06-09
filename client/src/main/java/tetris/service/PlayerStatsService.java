package tetris.service;

import tetris.model.Player;

/**
 * @author denMoskvin
 * @version 1.0
 */
public interface PlayerStatsService {

    Player getPlayerByEmail(String email);
    void updateStatisticPlayer(Player player);
}

package tetris.view;

import tetris.model.GameModel;

import static tetris.resource.ResourceManager.*;

import javax.swing.*;
import java.awt.*;

public class LevelPanel extends JLabel {
    GameModel gameModel;
    private final String playerSessionId;
    private JLabel levelLabel;

    private final int WIGHT = 120;
    private final int HEIGHT = 55;

    public LevelPanel(GameModel gameModel, String playerSessionId){
        this.gameModel = gameModel;
        this.playerSessionId = playerSessionId;

        levelLabel = new JLabel("   " + gameModel.getLevel(playerSessionId));
        levelLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
        levelLabel.setForeground(Color.BLACK);
        levelLabel.setPreferredSize(new Dimension(120, 30));
        levelLabel.setSize(getPreferredSize());
        levelLabel.setOpaque(false);
        levelLabel.setFocusable(false);
        levelLabel.setBackground(new Color(0, 0, 0, 0));
        levelLabel.setVerticalTextPosition(SwingConstants.CENTER);

        JLabel levelTextLabel = new JLabel(" level:");
        levelTextLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
        levelTextLabel.setForeground(Color.WHITE);
        levelTextLabel.setPreferredSize(new Dimension(120, 25));
        levelTextLabel.setSize(getPreferredSize());
        levelTextLabel.setOpaque(false);
        levelTextLabel.setFocusable(false);
        levelTextLabel.setBackground(new Color(0, 0, 0, 0));

        setPreferredSize(new Dimension(WIGHT, HEIGHT));
        setSize(getPreferredSize());
        setIcon(getImg("LevelAndScoreBackground.png", WIGHT, HEIGHT));
        setLayout(new FlowLayout());
        add(levelTextLabel);
        add(levelLabel);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        levelLabel.removeAll();
        levelLabel.setText("   " + gameModel.getLevel(playerSessionId));
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }
}

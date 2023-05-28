package tetris.view;

import tetris.model.GameLauncher;
import tetris.model.GameModel;

import static tetris.resource.ResourceManager.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverPanel extends JPanel implements ActionListener {
    private final GameLauncher gameLauncher;
    private final GameModel gameModel;
    private JButton toHomeButton;
    private JButton restartButton;

    GameOverPanel(GameLauncher gameLauncher, String playerSessionId){
        this.gameLauncher = gameLauncher;
        this.gameModel = gameLauncher.getGameModel();
        setPreferredSize(new Dimension(250, 500));
        setBackground(new Color(0, 0, 0, 128));
        setFocusable(false);
        setLayout(new BorderLayout());

        JLabel tetrisLogo = new JLabel(getImg("Tetris-logo1.png", 138, 96));
        tetrisLogo.setOpaque(false);

        JLabel gameOverTextLabel = new JLabel("Game Over");
        gameOverTextLabel.setPreferredSize(new Dimension(250, 50));
        gameOverTextLabel.setSize(getPreferredSize());
        gameOverTextLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 38));
        gameOverTextLabel.setForeground(Color.WHITE);
        gameOverTextLabel.setHorizontalAlignment(JLabel.CENTER);
        gameOverTextLabel.setVerticalAlignment(JLabel.CENTER);
        gameOverTextLabel.setOpaque(false);

        JPanel gameOverTextAndTetrisLogoBox = createPanelOffset(250, 160);
        gameOverTextAndTetrisLogoBox.setLayout(new FlowLayout());
        gameOverTextAndTetrisLogoBox.add(tetrisLogo);
        gameOverTextAndTetrisLogoBox.add(createPanelOffset(250, 10));
        gameOverTextAndTetrisLogoBox.add(gameOverTextLabel);


        add(gameOverTextAndTetrisLogoBox, BorderLayout.PAGE_START);

        if (playerSessionId.equals(gameModel.getOwnSessionId())) {
            addButtonsNavigation();
        }
    }

    private void addButtonsNavigation() {
        toHomeButton = createButtonForGameOverPanel("Home");
        toHomeButton.addActionListener(this);

        restartButton = createButtonForGameOverPanel("Restart");
        restartButton.addActionListener(this);

        JPanel toHomeAndRestartButtonsBox = new JPanel();
        toHomeAndRestartButtonsBox.setOpaque(false);
        toHomeAndRestartButtonsBox.setPreferredSize(new Dimension(240, 40));
        toHomeAndRestartButtonsBox.add(toHomeButton, BorderLayout.LINE_START);
        toHomeAndRestartButtonsBox.add(restartButton, BorderLayout.LINE_END);
        toHomeAndRestartButtonsBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        add(toHomeAndRestartButtonsBox, BorderLayout.PAGE_END);
    }

    private JButton createButtonForGameOverPanel(String text){
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(204, 154, 66));
        button.setPreferredSize(new Dimension(110, 30));

        return button;
    }

    private JPanel createPanelOffset(int wight, int height){
        JPanel panelOffset = new JPanel();
        panelOffset.setPreferredSize(new Dimension(wight, height));
        panelOffset.setSize(getPreferredSize());
        panelOffset.setOpaque(false);

        return panelOffset;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == toHomeButton){
            gameLauncher.getLauncherView().displayLauncherHomepage();
        }
        gameLauncher.restartGame();
    }
}
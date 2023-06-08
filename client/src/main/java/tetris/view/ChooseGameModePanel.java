package tetris.view;

import tetris.controller.LauncherController;
import tetris.model.GameLauncher;

import javax.swing.*;

import java.awt.*;

import static tetris.resource.ResourceManager.getImg;
import static tetris.util.ViewUtil.*;

public class ChooseGameModePanel extends JPanel {
    private GameLauncher gameLauncher;
    private LauncherController launcherController;

    private Image backImage;

    ChooseGameModePanel(GameLauncher gameLauncher, LauncherController launcherController) {
        this.gameLauncher = gameLauncher;
        this.launcherController = launcherController;

        backImage = getImg("background0.jpg");

        setSize(1000 ,700);
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createBevelBorder(0 , Color.BLACK, Color.BLACK));
        setOpaque(true);

        JLabel label = new JLabel("Choose Game Mode :");
        label.setFont(new Font("Helvetica", Font.BOLD, 40));
        label.setForeground(Color.black);

        JButton soloGameButton = new JButton(getImg("soloGameButton.png", 350, 75));
        soloGameButton.setBackground(new Color(0, 0, 0, 0));
        soloGameButton.setFocusable(false);
        soloGameButton.setOpaque(false);
        soloGameButton.setBorder(null);
        soloGameButton.addActionListener(event -> launcherController.handleSoloGameButtonClick());

        JButton pvpGameButton = new JButton(getImg("pvpGameButton.png", 350, 75));
        pvpGameButton.setBackground(new Color(0, 0, 0, 0));
        pvpGameButton.setFocusable(false);
        pvpGameButton.setOpaque(false);
        pvpGameButton.setBorder(null);
        pvpGameButton.addActionListener(e -> launcherController.handlePvpGameButtonClick());

        add(createPanelOffset(1000, 100));
        add(label);
        add(createPanelOffset(1000, 80));
        add(soloGameButton);
        add(createPanelOffset(1000, 100));
        add(pvpGameButton);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backImage, 0, 0, getWidth(), getHeight(), this);
    }
}

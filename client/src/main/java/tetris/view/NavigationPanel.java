package tetris.view;

import tetris.model.GameLauncher;
import static tetris.resource.ResourceManager.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavigationPanel extends JPanel implements ActionListener {
    private JButton playOrPauseButton;
    private boolean playEnabled = false;

    private JButton enableORDisableBackgroundMusicButton;
    private JButton menuButton;

    private GameLauncher gameLauncher;

    public NavigationPanel(GameLauncher gameLauncher){
        this.gameLauncher = gameLauncher;
        gameLauncher.setNavigationPanel(this);

        playOrPauseButton = new JButton();
        playOrPauseButton.setIcon(getImg("play.png", 30, 30));
        playOrPauseButton.setBackground(new Color(0, 0, 0, 0));
        playOrPauseButton.setOpaque(false);
        playOrPauseButton.setFocusable(false);
        playOrPauseButton.addActionListener(this);

        enableORDisableBackgroundMusicButton = new JButton();
        enableORDisableBackgroundMusicButton.setIcon(getImg("sound-off.png", 30, 20));
        enableORDisableBackgroundMusicButton.setBackground(new Color(0, 0, 0, 0));
        enableORDisableBackgroundMusicButton.setOpaque(false);
        enableORDisableBackgroundMusicButton.setFocusable(false);
        enableORDisableBackgroundMusicButton.addActionListener(this);

        menuButton = new JButton();
        menuButton.setBackground(new Color(0, 0, 0, 0));
        menuButton.setOpaque(false);
        menuButton.setFocusable(false);
        menuButton.setIcon(getImg("menu.png", 30, 30));
        menuButton.addActionListener(this);


        JPanel playOrPauseAndMenuBoxPanel = new JPanel();
        playOrPauseAndMenuBoxPanel.setLayout(new FlowLayout());
        playOrPauseAndMenuBoxPanel.setVisible(true);
        playOrPauseAndMenuBoxPanel.setOpaque(false);
        playOrPauseAndMenuBoxPanel.setBackground(null);
        playOrPauseAndMenuBoxPanel.setFocusable(false);
        playOrPauseAndMenuBoxPanel.add(menuButton);
        playOrPauseAndMenuBoxPanel.add(playOrPauseButton);

        setPreferredSize(new Dimension(1120, 70));
        setSize(getPreferredSize());
        setBackground(null);
        setVisible(true);
        setFocusable(false);
        setOpaque(false);
        setLayout(new BorderLayout());
        add(playOrPauseAndMenuBoxPanel, BorderLayout.LINE_START);
        add(enableORDisableBackgroundMusicButton, BorderLayout.LINE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == playOrPauseButton){
            if (!playEnabled){
                playOrPauseButton.setIcon(getImg("pause.png", 30, 30));
                gameLauncher.startGame();
            }else {
                playOrPauseButton.setIcon(getImg("play.png", 30, 30));
                gameLauncher.stopGame();
            }
            playEnabled = !playEnabled;
        }else if (e.getSource() == menuButton){
            if (!gameLauncher.isBackgroundMusicDisabled()){
                enableORDisableBackgroundMusicButton.doClick();
            }
            if (playEnabled){
                playOrPauseButton.setIcon(getImg("play.png", 30, 30));
                playEnabled = false;
            }
            gameLauncher.stopGame();
            gameLauncher.getLauncherView().displayMenuPause();
        }else if (e.getSource() == enableORDisableBackgroundMusicButton){
            if (gameLauncher.isBackgroundMusicDisabled()){
                enableORDisableBackgroundMusicButton.setIcon(getImg("sound-off.png", 30, 20));
                gameLauncher.changeStateOfBackgroundMusic();
            }else {
                enableORDisableBackgroundMusicButton.setIcon(getImg("sound-on.png", 30, 20));
                gameLauncher.changeStateOfBackgroundMusic();
            }
        }
    }

    public boolean getPlayStatus() { return playEnabled; }

    public JButton getPlayOrPauseButton() {
        return playOrPauseButton;
    }

}

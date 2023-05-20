package tetris.view;

import tetris.model.GameLauncher;
import static tetris.resource.ResourceManager.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPause extends JPanel implements ActionListener {
    private GameLauncher gameLauncher;

    private Image backImage = getImg("background0.jpg");

    private JButton resumeButton;
    private JButton restartButton;

    public MenuPause(GameLauncher gameLauncher){
        this.gameLauncher = gameLauncher;

        setPreferredSize(new Dimension(1000, 700));
        setSize(getPreferredSize());
        setFocusable(false);
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createBevelBorder(0 , Color.BLACK, Color.BLACK));


        resumeButton = new JButton();
        resumeButton.setIcon(getImg("resume.png", 500, 100));
        resumeButton.setBackground(new Color(0, 0, 0, 0));
        resumeButton.setOpaque(false);
        resumeButton.addActionListener(this);

        restartButton = new JButton();
        restartButton.setIcon(getImg("restart.png", 500, 100));
        restartButton.setBackground(new Color(0, 0, 0, 0));
        restartButton.setOpaque(false);
        restartButton.addActionListener(this);

        addOffset(210);
        add(resumeButton);
        addOffset(50);
        add(restartButton);


    }

    private void addOffset(int height){
        JPanel offset = new JPanel();
        offset.setPreferredSize(new Dimension(1000, height));
        offset.setSize(getPreferredSize());
        offset.setOpaque(false);
        add(offset);
    }

    @Override
    public void paintComponent(Graphics g){
        g.drawImage(backImage, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == resumeButton){
            gameLauncher.getLauncherView().resumeGame();
        }else if (e.getSource() == restartButton){
            gameLauncher.getGameModel().refreshGameData();
            gameLauncher.getLauncherView().displayLauncherHomepage();
        }
    }
}

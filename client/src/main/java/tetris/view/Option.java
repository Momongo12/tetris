package tetris.view;

import tetris.model.GameLauncher;
import static tetris.resource.ResourceManager.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class Option extends JPanel implements ActionListener {
    GameLauncher gameLauncher;
    Image img, updatedBackImage;
    private JButton soundEnableOrDisableButton, soundUpdateButton;
    private JLabel soundEnableOrDisableLabel;
    private JButton backButton, backButtonRefresh;
    private JPanel panel;
    
    private int imageIndex;


    Option(GameLauncher gameLauncher){
        this.gameLauncher = gameLauncher;

        setSize(1000, 700);
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));
        setBackground(new Color(6, 6, 9));

        backButton = new JButton("CHANGE BACKGROUND");
        backButton.setOpaque(false);
        backButton.setIcon(getImg("background.png", getWidth() / 13, getHeight() / 13));
        backButton.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusable(false);
        backButton.setBackground(new Color(0, 0, 0, 0));
        backButton.addActionListener(this);

        backButtonRefresh = new JButton(getImg("refresh.png", getWidth() / 13, getHeight() / 13));
        backButtonRefresh.setOpaque(false);
        backButtonRefresh.setFocusable(false);
        backButtonRefresh.setBackground(new Color(0, 0, 0, 0));
        backButtonRefresh.addActionListener(this);

        soundEnableOrDisableLabel = new JLabel("Music");
        soundEnableOrDisableLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
        soundEnableOrDisableLabel.setForeground(Color.WHITE);
        soundEnableOrDisableLabel.setIcon(getImg("sound-off.png", getWidth() / 13, getHeight() / 13));

        soundEnableOrDisableButton = new JButton();
        soundEnableOrDisableButton.setBackground(new Color(0, 0, 0, 0));
        soundEnableOrDisableButton.setOpaque(false);
        soundEnableOrDisableButton.setFocusable(false);
        soundEnableOrDisableButton.add(soundEnableOrDisableLabel);
        soundEnableOrDisableButton.addActionListener(this);

        soundUpdateButton = new JButton(getImg("music.png", getWidth() / 13, getHeight() / 13));
        soundUpdateButton.setOpaque(false);
        soundUpdateButton.setFocusable(false);
        soundUpdateButton.setBackground(new Color(0, 0, 0, 0));
        soundUpdateButton.addActionListener(this);


        panel = new JPanel();
        panel.setBackground(new Color(26, 26, 32));
        panel.setPreferredSize(new Dimension(500, 250));
        panel.setSize(getPreferredSize());
        panel.setLayout(new FlowLayout());
        panel.add(createPanelOffset(500, 60));
        panel.add(backButton);
        panel.add(backButtonRefresh);
        panel.add(soundEnableOrDisableButton);
        panel.add(soundUpdateButton);

        add(createPanelOffset(1000, 200));
        add(panel);
        validate();
    }

    private JPanel createPanelOffset(int width, int height){
        JPanel panelOffset = new JPanel();
        panelOffset.setPreferredSize(new Dimension(width, height));
        panelOffset.setOpaque(false);

        return panelOffset;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButtonRefresh){
            imageIndex = (imageIndex + 1) % 7 ;
            updatedBackImage = getImg("background" + imageIndex + ".jpg");
            img = updatedBackImage;
            LauncherView.gamePanel.setBackImage(updatedBackImage);
            repaint();
        }else if (e.getSource() == backButton){
            FileDialog fileDialog = new FileDialog(new JFrame(), "sounds");
            fileDialog.show();
            try{
                File file = new File(fileDialog.getDirectory() + "/" + fileDialog.getFile());
                updatedBackImage = new ImageIcon(file.getAbsolutePath()).getImage();
                img = updatedBackImage;
                LauncherView.gamePanel.setBackImage(updatedBackImage);
                repaint();
            }catch (Exception err){
                System.err.println(err.getLocalizedMessage());
            }
        }else if (e.getSource() == soundUpdateButton){
            gameLauncher.updateBackgroundSound();
        }else if (e.getSource() == soundEnableOrDisableButton){
            gameLauncher.changeStateOfBackgroundMusic();
        }
    }
}

package tetris.view;

import tetris.model.GameLauncher;
import static tetris.resource.ResourceManager.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseLevelPanel extends JPanel implements ActionListener{

    private Image backImage;

    private JLabel label;

    private JButton easyLevelButton, mediumLevelButton, hardLevelButton;

    private GameLauncher gameLauncher;

    ChooseLevelPanel(GameLauncher gameLauncher){
        this.gameLauncher = gameLauncher;

        backImage = getImg("background0.jpg");

        setSize(1000 ,700);
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createBevelBorder(0 , Color.BLACK, Color.BLACK));
        setOpaque(true);

        label = new JLabel("Choose Level :");
        label.setFont(new Font("Helvetica", Font.BOLD, 40));
        label.setForeground(Color.black);

        easyLevelButton = new JButton(getImg("easy.png", getWidth() * 9 / 20, getHeight() / 12));
        easyLevelButton.setBackground(new Color(0, 0, 0, 0));
        easyLevelButton.setFocusable(false);
        easyLevelButton.setOpaque(false);
        easyLevelButton.addActionListener(this);

        mediumLevelButton = new JButton(getImg("medium.png", getWidth() * 9 / 20, getHeight() / 12));
        mediumLevelButton.setBackground(new Color(0, 0, 0, 0));
        mediumLevelButton.setFocusable(false);
        mediumLevelButton.setOpaque(false);
        mediumLevelButton.addActionListener(this);

        hardLevelButton = new JButton(getImg("hard.png", getWidth() * 9 / 20, getHeight() / 12));
        hardLevelButton.setBackground(new Color(0, 0, 0, 0));
        hardLevelButton.setFocusable(false);
        hardLevelButton.setOpaque(false);
        hardLevelButton.addActionListener(this);

        addChooseLevelButtons();
    }

    private void addChooseLevelButtons(){
        addOffset(100);
        add(label);
        addOffset(10);
        add(easyLevelButton);
        addOffset(80);
        add(mediumLevelButton);
        addOffset(80);
        add(hardLevelButton);
    }

    private void addOffset(int height){
        JPanel offset = new JPanel();
        offset.setPreferredSize(new Dimension(1000, height));
        offset.setSize(getPreferredSize());
        offset.setOpaque(false);
        add(offset);
    }

    public void setBackImage(Image backImage) {
        this.backImage = backImage;
    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backImage, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == easyLevelButton){
            gameLauncher.getSoloGameModel().setGameLevel(1);
        }else if (e.getSource() == mediumLevelButton){
            gameLauncher.getSoloGameModel().setGameLevel(2);
        }else if (e.getSource() == hardLevelButton){
            gameLauncher.getSoloGameModel().setGameLevel(3);
        }
        gameLauncher.getLauncherView().displayGamePanel();
    }
}

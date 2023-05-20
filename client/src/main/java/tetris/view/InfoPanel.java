package tetris.view;

import tetris.model.GameModel;
import static tetris.resource.ResourceManager.*;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private GameModel gameModel;

    private final int WIGHT = 120;
    private final int HEIGHT = 55;

    private JLabel linesLabel;

    private JLabel scoreLabel;

    public InfoPanel(GameModel gameModel){
        this.gameModel = gameModel;
        gameModel.setInfoPanel(this);

        JLabel linesBoxLabel = new JLabel();
        linesBoxLabel.setIcon(getImg("linesBackground.png", WIGHT, HEIGHT));
        linesBoxLabel.setForeground(Color.WHITE);
        linesBoxLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 19));
        linesBoxLabel.setBackground(null);
        linesBoxLabel.setOpaque(false);
        linesBoxLabel.setFocusable(false);

        JLabel linesTextLabel = new JLabel(" lines:");
        linesTextLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
        linesTextLabel.setForeground(Color.WHITE);
        linesTextLabel.setPreferredSize(new Dimension(120, 25));
        linesTextLabel.setSize(getPreferredSize());
        linesTextLabel.setOpaque(false);
        linesTextLabel.setFocusable(false);
        linesTextLabel.setBackground(new Color(0, 0, 0, 0));
        
        linesLabel = new JLabel("   " + gameModel.getLines());
        linesLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
        linesLabel.setForeground(Color.BLACK);
        linesLabel.setPreferredSize(new Dimension(120, 30));
        linesLabel.setSize(getPreferredSize());
        linesLabel.setOpaque(false);
        linesLabel.setFocusable(false);
        linesLabel.setBackground(new Color(0, 0, 0, 0));
        linesLabel.setVerticalTextPosition(SwingConstants.CENTER);
        
        linesBoxLabel.setLayout(new FlowLayout());
        linesBoxLabel.add(linesTextLabel);
        linesBoxLabel.add(linesLabel);
        
        
        JLabel scoreBoxLabel = new JLabel();
        scoreBoxLabel.setIcon(getImg("LevelAndScoreBackground.png", WIGHT, HEIGHT));
        scoreBoxLabel.setForeground(Color.WHITE);
        scoreBoxLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 19));
        scoreBoxLabel.setBackground(null);
        scoreBoxLabel.setOpaque(false);
        scoreBoxLabel.setFocusable(false);

        JLabel scoreTextLabel = new JLabel(" score:");
        scoreTextLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
        scoreTextLabel.setForeground(Color.WHITE);
        scoreTextLabel.setPreferredSize(new Dimension(120, 25));
        scoreTextLabel.setSize(getPreferredSize());
        scoreTextLabel.setOpaque(false);
        scoreTextLabel.setFocusable(false);
        scoreTextLabel.setBackground(new Color(0, 0, 0, 0));

        scoreLabel = new JLabel("   " + gameModel.getScore());
        scoreLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setPreferredSize(new Dimension(120, 30));
        scoreLabel.setSize(getPreferredSize());
        scoreLabel.setOpaque(false);
        scoreLabel.setFocusable(false);
        scoreLabel.setBackground(new Color(0, 0, 0, 0));
        scoreLabel.setVerticalTextPosition(SwingConstants.CENTER);

        scoreBoxLabel.setLayout(new FlowLayout());
        scoreBoxLabel.add(scoreTextLabel);
        scoreBoxLabel.add(scoreLabel);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(120, 120));
        setSize(getPreferredSize());
        setOpaque(false);
        setFocusable(false);
        setVisible(true);
        setBackground(new Color(5, 110, 225));
        add(linesBoxLabel, BorderLayout.PAGE_START);
        add(scoreBoxLabel, BorderLayout.PAGE_END);
    }

    @Override
    public void paintComponent(Graphics g){

        linesLabel.removeAll();
        linesLabel.setText("   " + gameModel.getLines());

        scoreLabel.removeAll();
        scoreLabel.setText("   " + gameModel.getScore());
        super.paintComponent(g);
    }

}

package tetris.view;

import tetris.model.GameLauncher;
import tetris.model.GameModel;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final GameModel gameModel;
    private final GameLauncher gameLauncher;

    private JPanel gameField;

    private final GameMatrix gameMatrix;
    private final InfoPanel infoPanel;
    public final NavigationPanel navigationPanel;
    public final NextShapePanel nextShapePanel;
    private final HoldPanel holdPanel;
    private final LevelPanel levelPanel;

    private Image img;

    public GamePanel(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
        this.gameModel = gameLauncher.getGameModel();
        infoPanel = new InfoPanel(gameModel);
        gameMatrix = new GameMatrix(gameModel);
        navigationPanel = new NavigationPanel(gameLauncher);
        nextShapePanel = new NextShapePanel(gameModel);
        holdPanel = new HoldPanel(gameModel);
        levelPanel = new LevelPanel(gameModel);

        setPreferredSize(new Dimension(1120, 700));
        setSize(getPreferredSize());
        setLayout(new FlowLayout());
        setFocusable(true);
        setOpaque(false);
        setVisible(true);
        gameModel.setGamePanel(this);

        JPanel gameAndQueue = new JPanel();
        gameAndQueue.setLayout(new BorderLayout());
        gameAndQueue.setOpaque(false);

        gameField = new JPanel();
        gameField.setLayout(new BorderLayout(4, 4));
        gameField.setBackground(null);
        gameField.setOpaque(false);
        gameField.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(3, 70, 152)));
        gameField.add(gameMatrix, BorderLayout.CENTER);

        JPanel nextPanel = new JPanel();
        nextPanel.setLayout(new BorderLayout(4, 4));
        nextPanel.setBackground(null);
        nextPanel.setOpaque(false);
        nextPanel.add(nextShapePanel, BorderLayout.PAGE_START);
        nextPanel.add(infoPanel, BorderLayout.PAGE_END);
        nextPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JPanel holdAndLevelPanel = new JPanel();
        holdAndLevelPanel.setLayout(new BorderLayout(4, 4));
        holdAndLevelPanel.setBackground(null);
        holdAndLevelPanel.setOpaque(false);
        holdAndLevelPanel.add(holdPanel, BorderLayout.PAGE_START);
        holdAndLevelPanel.add(levelPanel, BorderLayout.PAGE_END);
        holdAndLevelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        gameAndQueue.add(gameField, BorderLayout.CENTER);
        gameAndQueue.add(nextPanel, BorderLayout.LINE_END);
        gameAndQueue.add(holdAndLevelPanel, BorderLayout.LINE_START);

        JPanel panelOffset = new JPanel();
        panelOffset.setBackground(null);
        panelOffset.setPreferredSize(new Dimension(1120, 90));
        panelOffset.setSize(getPreferredSize());
        panelOffset.setVisible(true);
        panelOffset.setOpaque(false);

        add(navigationPanel);
        add(gameAndQueue);
    }

    public void setBackImage(Image img){
        this.img = img;
    }

    public void displayGameOverPanel(){
        gameField.removeAll();
        gameField.add(new GameOverPanel(gameLauncher));
        repaint();
        revalidate();
    }

    public void deleteGameOverPanelAndAddGameMatrix(){
        gameField.removeAll();
        gameField.add(gameMatrix);
        repaint();
        revalidate();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0,0, getWidth(), getHeight(), this);
        gameMatrix.repaint();
        infoPanel.repaint();
        nextShapePanel.repaint();
        holdPanel.repaint();
    }
}

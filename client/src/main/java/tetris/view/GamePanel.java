package tetris.view;

import tetris.model.GameLauncher;
import tetris.model.GameModel;
import static tetris.util.ViewUtil.*;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final GameModel gameModel;
    private final GameLauncher gameLauncher;

    private JPanel gameField;

    private GameMatrix gameMatrix1, gameMatrix2;
    private InfoPanel infoPanel1, infoPanel2;
    private NavigationPanel navigationPanel;
    private NextShapePanel nextShapePanel1, nextShapePanel2;
    private HoldPanel holdPanel1, holdPanel2;
    private LevelPanel levelPanel1, levelPanel2;

    private Image img;

    public GamePanel(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
        gameLauncher.setGamePanel(this);
        this.gameModel = gameLauncher.getGameModel();
        gameModel.setGamePanel(this);
        infoPanel1 = new InfoPanel(gameModel);
        gameMatrix1 = new GameMatrix(gameModel);
        navigationPanel = new NavigationPanel(gameLauncher);
        nextShapePanel1 = new NextShapePanel(gameModel);
        holdPanel1 = new HoldPanel(gameModel);
        levelPanel1 = new LevelPanel(gameModel);

        setPreferredSize(new Dimension(1120, 700));
        setSize(getPreferredSize());
        setLayout(new FlowLayout());
        setFocusable(true);
        setOpaque(false);
        setVisible(true);

        displaySoloGameMode();
    }

    public void setBackImage(Image img){
        this.img = img;
    }

    public void displaySoloGameMode() {
        JPanel gameAndQueue = new JPanel();
        gameAndQueue.setLayout(new BorderLayout());
        gameAndQueue.setOpaque(false);

        gameField = createGameField(gameMatrix1);

        JPanel nextPanel = new JPanel();
        nextPanel.setLayout(new BorderLayout(4, 4));
        nextPanel.setBackground(null);
        nextPanel.setOpaque(false);
        nextPanel.add(nextShapePanel1, BorderLayout.PAGE_START);
        nextPanel.add(infoPanel1, BorderLayout.PAGE_END);
        nextPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JPanel holdAndLevelPanel = new JPanel();
        holdAndLevelPanel.setLayout(new BorderLayout(4, 4));
        holdAndLevelPanel.setBackground(null);
        holdAndLevelPanel.setOpaque(false);
        holdAndLevelPanel.add(holdPanel1, BorderLayout.PAGE_START);
        holdAndLevelPanel.add(levelPanel1, BorderLayout.PAGE_END);
        holdAndLevelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        gameAndQueue.add(gameField, BorderLayout.CENTER);
        gameAndQueue.add(nextPanel, BorderLayout.LINE_END);
        gameAndQueue.add(holdAndLevelPanel, BorderLayout.LINE_START);

        removeAll();
        add(navigationPanel);
        add(gameAndQueue);
    }

    public void displayPvPGameMode() {
        infoPanel2 = new InfoPanel(gameModel);
        gameMatrix2 = new GameMatrix(gameModel);
        nextShapePanel2 = new NextShapePanel(gameModel);
        holdPanel2 = new HoldPanel(gameModel);
        levelPanel2 = new LevelPanel(gameModel);

        JPanel gameAndQueue1 = new JPanel();
        gameAndQueue1.setLayout(new BorderLayout());
        gameAndQueue1.setOpaque(false);

        JPanel gameAndQueue2 = new JPanel();
        gameAndQueue2.setLayout(new BorderLayout());
        gameAndQueue2.setOpaque(false);

        JPanel gameField1 = createGameField(gameMatrix1);
        JPanel gameField2 = createGameField(gameMatrix2);

        JPanel gameDetails1 = new JPanel();
        gameDetails1.setLayout(new BorderLayout(4, 4));
        gameDetails1.setBackground(null);
        gameDetails1.setOpaque(false);
        gameDetails1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        JPanel gameDetails2 = new JPanel();
        gameDetails2.setLayout(new BorderLayout(4, 4));
        gameDetails2.setBackground(null);
        gameDetails2.setOpaque(false);
        gameDetails2.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JPanel nextAndHoldShapePanel1 = createNextAndHoldShapePanel(nextShapePanel1, holdPanel1);
        JPanel nextAndHoldShapePanel2 = createNextAndHoldShapePanel(nextShapePanel2, holdPanel2);

        JPanel detailsPanel1 = createDetailsPanel(infoPanel1, levelPanel1);

        JPanel detailsPanel2 = createDetailsPanel(infoPanel2, levelPanel2);

        gameDetails1.add(nextAndHoldShapePanel1, BorderLayout.PAGE_START);
        gameDetails1.add(detailsPanel1, BorderLayout.PAGE_END);

        gameDetails2.add(nextAndHoldShapePanel2, BorderLayout.PAGE_START);
        gameDetails2.add(detailsPanel2, BorderLayout.PAGE_END);

        gameAndQueue1.add(gameField1, BorderLayout.LINE_END);
        gameAndQueue1.add(gameDetails1, BorderLayout.LINE_START);

        gameAndQueue2.add(gameDetails2, BorderLayout.LINE_END);
        gameAndQueue2.add(gameField2, BorderLayout.LINE_START);

        JPanel gameAndQueueBox = new JPanel();
        gameAndQueueBox.setOpaque(false);
        gameAndQueueBox.setBackground(null);
        gameAndQueueBox.setLayout(new BorderLayout());
        gameAndQueueBox.setPreferredSize(new Dimension(800, 500));

        gameAndQueueBox.add(gameAndQueue1, BorderLayout.LINE_START);
        gameAndQueueBox.add(gameAndQueue2, BorderLayout.LINE_END);

        removeAll();
        add(navigationPanel);
        add(gameAndQueueBox);
    }

    private JPanel createDetailsPanel(InfoPanel infoPanel, LevelPanel levelPanel) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout(4, 4));
        detailsPanel.setBackground(null);
        detailsPanel.setOpaque(false);
        detailsPanel.add(levelPanel, BorderLayout.PAGE_START);
        detailsPanel.add(infoPanel, BorderLayout.PAGE_END);

        return detailsPanel;
    }

    private JPanel createNextAndHoldShapePanel(NextShapePanel nextShapePanel, HoldPanel holdPanel) {
        JPanel nextAndHoldShapePanel = new JPanel();
        nextAndHoldShapePanel.setLayout(new BorderLayout(4, 4));
        nextAndHoldShapePanel.setBackground(null);
        nextAndHoldShapePanel.setOpaque(false);
        nextAndHoldShapePanel.add(nextShapePanel, BorderLayout.PAGE_START);
        nextAndHoldShapePanel.add(holdPanel, BorderLayout.PAGE_END);
        nextAndHoldShapePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        return nextAndHoldShapePanel;
    }

    private JPanel createGameField(GameMatrix gameMatrix) {
        gameField = new JPanel();
        gameField.setLayout(new BorderLayout(4, 4));
        gameField.setBackground(null);
        gameField.setOpaque(false);
        gameField.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(3, 70, 152)));
        gameField.add(gameMatrix, BorderLayout.CENTER);

        return gameField;
    }

    public void displayGameOverPanel(){
        gameField.removeAll();
        gameField.add(new GameOverPanel(gameLauncher));
        repaint();
        revalidate();
    }

    public void deleteGameOverPanelAndAddGameMatrix(){
        gameField.removeAll();
        gameField.add(gameMatrix1);
        repaint();
        revalidate();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0,0, getWidth(), getHeight(), this);
        gameMatrix1.repaint();
        infoPanel1.repaint();
        nextShapePanel1.repaint();
        holdPanel1.repaint();
        if (gameMatrix2 != null && infoPanel2 != null && nextShapePanel2 != null && holdPanel2 != null) {
            gameMatrix2.repaint();
            infoPanel2.repaint();
            nextShapePanel2.repaint();
            holdPanel2.repaint();
        }
    }
}

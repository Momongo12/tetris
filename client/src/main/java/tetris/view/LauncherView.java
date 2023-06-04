package tetris.view;

import lombok.extern.log4j.Log4j2;
import tetris.controller.LauncherController;
import tetris.model.GameLauncher;
import tetris.model.GameModel;
import tetris.view.registration.EventLogin;
import tetris.view.registration.LoginAndRegister;

import static tetris.resource.ResourceManager.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@Log4j2
public class LauncherView extends JFrame {
    private JPanel barPanel;
    private JPanel barPanelButton;
    private JPanel inter;
    private JTabbedPane tabbedPane;

    private boolean maximized;

    public static Option option;
    public static ChooseLevelPanel chooseLevelPanel;
    public MenuPause menuPause;

    public JButton maximizerButton;
    public JButton exitButton;
    public JButton undecoratedButton;

    public static GamePanel gamePanel;
    public GameLauncher gameLauncher;
    private LauncherController launcherController;
    private RegistrationPanel registrationPanel;
    private LoginAndRegister loginAndRegisterPanel;
    private UserProfilePanel userProfilePanel;

    private ChooseGameModePanel chooseGameModePanel;

    public LauncherView(GameLauncher gameLauncher, LauncherController launcherController){
        this.gameLauncher = gameLauncher;
        this.launcherController = launcherController;

        gamePanel = new GamePanel(gameLauncher);
        gamePanel.setBackImage(getImg("background6.jpg"));

        menuPause = new MenuPause(gameLauncher);

        chooseGameModePanel = new ChooseGameModePanel(gameLauncher, launcherController);

        chooseLevelPanel = new ChooseLevelPanel(gameLauncher);

        registrationPanel = new RegistrationPanel(gameLauncher);

        barPanel = new JPanel();

        exitButton = new JButton(" ");
        exitButton.setIcon(new ImageIcon(getImg("cancel.png")));
        exitButton.setBackground(null);
        exitButton.setBorder(null);
        exitButton.setFocusable(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LauncherPreview.getExit().setVisible(true);
            }
        });

        maximizerButton = new JButton(" ");
        maximizerButton.setIcon(new ImageIcon(getImg("maximize.png")));
        maximizerButton.setBackground(null);
        maximizerButton.setBorder(null);
        maximizerButton.setFocusable(false);
        maximizerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                maximizerButtonMouseClicked(e);
            }
        });

        undecoratedButton = new JButton(" ");
        undecoratedButton.setIcon(new ImageIcon(getImg("upload.png")));
        undecoratedButton.setBackground(null);
        undecoratedButton.setBorder(null);
        undecoratedButton.setFocusable(false);
        undecoratedButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                undecorated();
            }
        });

        barPanelButton = new JPanel();
        barPanelButton.add(undecoratedButton);
        barPanelButton.add(maximizerButton);
        barPanelButton.add(exitButton);
        barPanelButton.setBackground(null);

        JLabel iconLabel = new JLabel("     Tetris", getImg("Tetris-logo1.png", 30, 25), SwingConstants.LEFT);
        iconLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setBackground(null);
        iconLabel.setOpaque(false);
        iconLabel.setVerticalAlignment(JLabel.CENTER);
        iconLabel.setVerticalTextPosition(JLabel.CENTER);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));


        barPanel.setLayout(new BorderLayout());
        barPanel.add(barPanelButton, BorderLayout.LINE_END);
        barPanel.add(iconLabel, BorderLayout.LINE_START);
        barPanel.setBackground(new Color(18, 30, 49));

        barPanel.validate();

        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        inter = new JPanel();

        AboutMeInfoPanel aboutMeInfoPanel = new AboutMeInfoPanel();
        HighScorePanel highScorePanel = new HighScorePanel();
        userProfilePanel = new UserProfilePanel(gameLauncher);

        option = new Option(gameLauncher);


        inter.setLayout(new BorderLayout());

        tabbedPane.addTab("   Home", getImg("home1.png", 40, 40), chooseGameModePanel);
        tabbedPane.addTab("   Option", getImg("option.png", 40, 40), option);
        tabbedPane.addTab("   About-me", getImg("about-me.png", 40, 40), aboutMeInfoPanel);
        tabbedPane.addTab("   High Scores", getImg("high-scores.png", 40, 40), highScorePanel);
        tabbedPane.addTab("   Profile", getImg("profile-icon.png", 40, 40), userProfilePanel);
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 16));
        tabbedPane.setBackground(new Color(24, 43, 53));
        tabbedPane.setForeground(Color.black);
        tabbedPane.setFocusable(true);
        tabbedPane.validate();

        inter.setBackground(new Color(18, 33, 43));// 54, 63, 73
        inter.setFocusable(true);
        inter.validate();

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                 UnsupportedLookAndFeelException ex) {
            log.error(ex);
        }

        initRegistrationPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1120, 700));
        setSize(getPreferredSize());
        setLocationRelativeTo(null);
        setResizable(true);
        setIconImage(getImg("Tetris-logo1.png", 60, 50).getImage());
        setTitle("Tetris");
        setUndecorated(true);
        add(barPanel, BorderLayout.NORTH);
        add(inter, BorderLayout.CENTER);
        setFocusable(true);
        validate();
        setVisible(true);

    }

    private void maximizerButtonMouseClicked(MouseEvent evt) {
        if (!maximized) {
            // handle fullscreen - taskbar
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            setMaximizedBounds(env.getMaximumWindowBounds());
            ImageIcon ii = new ImageIcon(getImg("minimize.png"));
            maximizerButton.setIcon(ii);
        } else {
            setExtendedState(JFrame.NORMAL);
            ImageIcon ii = new ImageIcon(getImg("maximize.png"));
            maximizerButton.setIcon(ii);
        }
        maximized = !maximized;
    }
    public void undecorated(){
        dispose();
        this.setUndecorated(!this.isUndecorated());
        pack();
        repaint();
        revalidate();
        setVisible(true);
    }

    public void displayMenuPause(){
        tabbedPane.setComponentAt(0, menuPause);
        inter.removeAll();
        inter.add(tabbedPane);
        repaint();
        revalidate();
    }

    public void resumeGame(){
        inter.removeAll();
        inter.add(gamePanel);
        repaint();
        revalidate();
    }

    public void displayGamePanel(){
        inter.removeAll();
        inter.add(gamePanel);
        repaint();
        revalidate();
    }

    public void displayPvPGameMode(GameModel gameModel) {
        gamePanel.displayPvPGameMode(gameModel);
        displayGamePanel();
    }

    public void displayLauncherHomepage(){
//        userProfilePanel.updateProfilePanel(); //обновляем UI здесь, т.к при вызове этой фунции user в лаунчере всегда инициализироване или обновлен
        displayChooseGameModePanel();
    }

    public void displayChooseLevelPanel() {
        tabbedPane.setComponentAt(0, chooseLevelPanel);
        inter.removeAll();
        inter.add(tabbedPane);
        repaint();
        revalidate();
    }

    public void displayChooseGameModePanel() {
        tabbedPane.setComponentAt(0, chooseGameModePanel);
        inter.removeAll();
        inter.add(tabbedPane);
        repaint();
        revalidate();
    }

    private void initRegistrationPanel(){
        loginAndRegisterPanel = new LoginAndRegister(gameLauncher);

        inter.add(loginAndRegisterPanel, BorderLayout.CENTER);
        EventLogin event = new EventLogin() {
            @Override
            public void loginDone() {
                displayLauncherHomepage();
            }

            @Override
            public void logOut() {
                inter.removeAll();
                inter.add(loginAndRegisterPanel);
                inter.revalidate();
                inter.repaint();
            }
        };
        loginAndRegisterPanel.setEventLogin(event);
    }

}

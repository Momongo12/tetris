package tetris.view;

import tetris.dataAccessLayer.PlayerStatisticsTableDataAccessObject;
import tetris.dataAccessLayer.PlayerTableDataAccessObject;
import tetris.model.GameLauncher;
import static tetris.resource.ResourceManager.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationPanel extends JPanel implements ActionListener {
    private GameLauncher gameLauncher;

    private JButton loginButton;
    private JTextField loginTextField;

    private Image backgroundImage;
    private Dimension registationPanelSize = new Dimension(1120, 700);

    private Color loginPanelBackgroundColor = new Color(0, 0, 0, 180);
    private Dimension sizeLoginFields = new Dimension(200, 30);

    RegistrationPanel(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
        backgroundImage = getImg("backgroundRegistration.jpg");

        JLabel label = new JLabel("Input name:");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 24));

        loginTextField = new JTextField();
        loginTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        loginTextField.setPreferredSize(sizeLoginFields);
        loginTextField.setBackground(Color.LIGHT_GRAY);
        loginTextField.setForeground(Color.BLACK);
        loginTextField.setHorizontalAlignment(JTextField.CENTER);
        loginTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));


        loginButton = new JButton("Log in");
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.setPreferredSize(sizeLoginFields);
        loginButton.addActionListener(this);

        JPanel boxLoginPanel = new JPanel();
        boxLoginPanel.setLayout(new FlowLayout());
        boxLoginPanel.setPreferredSize(new Dimension(230, 115));
        boxLoginPanel.setSize(getPreferredSize());
        boxLoginPanel.setBackground(loginPanelBackgroundColor);
        boxLoginPanel.add(label);
        boxLoginPanel.add(loginTextField);
        boxLoginPanel.add(loginButton);

        JPanel panelOffset = new JPanel();
        panelOffset.setOpaque(false);
        panelOffset.setPreferredSize(new Dimension(1120, 250));
        panelOffset.setSize(getPreferredSize());

        setPreferredSize(registationPanelSize);
        setSize(getPreferredSize());
        setLayout(new FlowLayout());
        add(panelOffset);
        add(boxLoginPanel);
    }

    @Override
    public void paintComponent(Graphics g){
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton){
            String playerName = loginTextField.getText().strip();
            if (!PlayerTableDataAccessObject.doesUserExist(playerName)){
                PlayerTableDataAccessObject.addUserToTableUsers(playerName);
                PlayerStatisticsTableDataAccessObject.initStatisticsForUser(playerName);
                gameLauncher.setCurrentPlayer(playerName);
                gameLauncher.getLauncherView().displayLauncherHomepage();
            }else {
                gameLauncher.setCurrentPlayer(playerName);
                gameLauncher.getLauncherView().displayLauncherHomepage();
                System.out.println("This user exist");
            }
        }
    }

}

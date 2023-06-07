package tetris.view;

import tetris.model.GameLauncher;
import tetris.model.Player;
import static tetris.resource.ResourceManager.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserProfilePanel extends JPanel{
    GameLauncher gameLauncher;
    Player player;
    JPanel statisticPanel;
    public UserProfilePanel(GameLauncher gameLauncher){
        this.gameLauncher = gameLauncher;
        
        setPreferredSize(new Dimension(1000, 700));
        setSize(getPreferredSize());
        setLayout(new BorderLayout());
        setBackground(new Color(6, 6, 9));
        setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));
    }

    public void updateProfilePanel(){
        if (player == null) {
            player = gameLauncher.getCurrentPlayer();
            createUI();
        }else {
            statisticPanel.removeAll();
            LinkedHashMap<String, Integer> statisticMap = player.getPlayerStatisticMap();
            int indexStatisticLabel = 0;
            for (Map.Entry<String, Integer> pair: statisticMap.entrySet()) {
                JLabel statisticLabel = createLabelForUserStatisticPanel(pair.getKey(), pair.getValue(), indexStatisticLabel++);
                statisticPanel.add(statisticLabel);
            }
            statisticPanel.repaint();
        }
    }

    public void createUI(){
        player = gameLauncher.getCurrentPlayer();
        JLabel usernameLabel = createUserInfoLabel("   " + player.getName(), 30);
        JLabel userDataOfRegistation = createUserInfoLabel("   Date of registration:  " + player.getDateOfRegistation().toString(), 18);
        AvatarPanel avatarPanel = new AvatarPanel();

        //Create upper panel with user Avatar and user Info
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        upperPanel.setBackground(new Color(26, 26, 32));
        upperPanel.setPreferredSize(new Dimension(getWidth(), 150));

        JPanel avatarAndUserInfoBox = new JPanel();
        avatarAndUserInfoBox.setFocusable(false);
        avatarAndUserInfoBox.setLayout(new FlowLayout());
        avatarAndUserInfoBox.setOpaque(false);
        avatarAndUserInfoBox.setPreferredSize(new Dimension(450, 150));
        
        JPanel usernameAndUserDataOfRegistrationBox = new JPanel();
        usernameAndUserDataOfRegistrationBox.setFocusable(false);
        usernameAndUserDataOfRegistrationBox.setOpaque(false);
        usernameAndUserDataOfRegistrationBox.setPreferredSize(new Dimension(300, 150));

        usernameAndUserDataOfRegistrationBox.add(usernameLabel, BorderLayout.PAGE_START);
        usernameAndUserDataOfRegistrationBox.add(userDataOfRegistation, BorderLayout.PAGE_END);

        avatarAndUserInfoBox.add(avatarPanel);
        avatarAndUserInfoBox.add(usernameAndUserDataOfRegistrationBox);

        upperPanel.add(avatarAndUserInfoBox, BorderLayout.LINE_START);
        //create lower panel with user Statistics
        JPanel lowerPanel = new JPanel();
        lowerPanel.setOpaque(false);
        lowerPanel.setPreferredSize(new Dimension(1000, 650));
        lowerPanel.setSize(getPreferredSize());
        lowerPanel.setLayout(new FlowLayout());

        //create title of statistic panel

        JLabel statisticTitleLabel = new JLabel("Statistics");
        statisticTitleLabel.setOpaque(false);
        statisticTitleLabel.setBackground(new Color(5, 5, 9));
        statisticTitleLabel.setPreferredSize(new Dimension(1000, 60));
        statisticTitleLabel.setForeground(Color.WHITE);
        statisticTitleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        statisticTitleLabel.setHorizontalAlignment(JLabel.CENTER);
        statisticTitleLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel statisticPanel = createStatisticPanel();

        lowerPanel.add(statisticTitleLabel);
        lowerPanel.add(statisticPanel);

        add(upperPanel, BorderLayout.PAGE_START);
        add(lowerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatisticPanel(){
        statisticPanel = new JPanel();
        statisticPanel.setLayout(new GridLayout(5, 0));
        statisticPanel.setPreferredSize(new Dimension(750, 300));
        statisticPanel.setOpaque(false);

        int indexStatisticLabel = 0;
        for (Map.Entry<String, Integer> pair: player.getPlayerStatisticMap().entrySet()) {
            JLabel statisticLabel = createLabelForUserStatisticPanel(pair.getKey(), pair.getValue(), indexStatisticLabel++);
            statisticPanel.add(statisticLabel);
        }

        return statisticPanel;
    }

    private JLabel createLabelForUserStatisticPanel(String nameOfStatisticField, int valueOfStatisticField, int indexStatisticLabel){
        JLabel statisticLabel = new JLabel("  " + nameOfStatisticField + ":   " + valueOfStatisticField);
        statisticLabel.setPreferredSize(new Dimension(750, 60));
        statisticLabel.setSize(getPreferredSize());
        statisticLabel.setOpaque(true);
        statisticLabel.setHorizontalAlignment(JLabel.LEFT);
        statisticLabel.setVerticalAlignment(JLabel.CENTER);
        statisticLabel.setFont(new Font("Arial", Font.PLAIN,20));
        statisticLabel.setForeground(Color.WHITE);
        statisticLabel.setBackground(indexStatisticLabel % 2 == 0? new Color(22, 21, 27): new Color(27, 26, 32));
        return statisticLabel;
    }

    private JLabel createUserInfoLabel(String text, int fontSize){
        JLabel label = new JLabel(text);
        label.setOpaque(false);
        label.setPreferredSize(new Dimension(300, 60));
        label.setSize(getPreferredSize());
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, fontSize));
        label.setForeground(Color.WHITE);

        return label;
    }
    private class AvatarPanel extends JPanel {

        private Image avatarImage;
        private Image defaultAvatarImage;
        private Ellipse2D.Double avatarBorder;
        private Ellipse2D.Double imageBorder;
        private boolean isHovering;
        private boolean isDefaultImage;

        public AvatarPanel() {
            setPreferredSize(new Dimension(130,  130));
            setBackground(Color.WHITE);
            setOpaque(false);
            avatarBorder = new Ellipse2D.Double(0, 0, getWidth(), getHeight());
            imageBorder = new Ellipse2D.Double(10, 10, getWidth() - 20, getHeight() - 20);
            isHovering = false;
            isDefaultImage = true;

            defaultAvatarImage = getImg("default_avatar.jpg", 130, 130).getImage();

            // Add mouse listener to handle avatar image click and hover events
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
//                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    fileChooser.setDialogTitle("Choose Avatar Image");
                    int result = fileChooser.showOpenDialog(AvatarPanel.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        try {
                            avatarImage = ImageIO.read(selectedFile).getScaledInstance(130, 130, Image.SCALE_SMOOTH);
                            isDefaultImage = false;
                            repaint();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovering = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovering = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            // Draw avatar border
            avatarBorder.setFrame(0, 0, getWidth(), getHeight());
            g2d.setPaint(Color.BLACK);
            g2d.draw(avatarBorder);

            // Draw image border
            imageBorder.setFrame(10, 10, getWidth() - 20, getHeight() - 20);
            g2d.setClip(imageBorder);
            g2d.setPaint(Color.WHITE);
            g2d.fill(imageBorder);

            // Draw avatar image
            Image image = isDefaultImage ? defaultAvatarImage : avatarImage;
            if (image != null) {
                int x = 0;
                int y = 0;
                g2d.drawImage(image, x, y, this);
            }

            // Draw hover effect
            if (isHovering) {
                g2d.setPaint(new Color(255, 255, 255, 100));
                g2d.fill(avatarBorder);
                g2d.setPaint(Color.BLACK);
                g2d.draw(avatarBorder);
            }

            g2d.dispose();
        }

        public void setDefaultAvatarImage(BufferedImage defaultAvatarImage) {
            this.defaultAvatarImage = defaultAvatarImage;
        }
    }
}

package tetris.view;

import javax.swing.*;
import java.awt.*;

public class AboutMeInfoPanel extends JPanel {

    AboutMeInfoPanel(){
        setPreferredSize(new Dimension(1000, 700));
        setSize(getPreferredSize());
        setLayout(new BorderLayout());
        setBackground(new Color(6, 6, 9));
        setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));

        JLabel infoAboutDevelopmentThisWindow = new JLabel("This window is under development");
        infoAboutDevelopmentThisWindow.setPreferredSize(new Dimension(1000, 100));
        infoAboutDevelopmentThisWindow.setVerticalAlignment(JLabel.CENTER);
        infoAboutDevelopmentThisWindow.setHorizontalAlignment(JLabel.CENTER);
        infoAboutDevelopmentThisWindow.setFont(new Font("Arial", Font.BOLD, 32));
        infoAboutDevelopmentThisWindow.setForeground(Color.WHITE);

        add(infoAboutDevelopmentThisWindow);
    }
}

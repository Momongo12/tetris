package tetris.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static tetris.resource.ResourceManager.getImg;

public class ExitPanel extends JDialog {
    JButton noButton, yesButton, exitBarButton;

    public ExitPanel() {
        setTitle("Exit");
        setUndecorated(true);
        setPreferredSize(new Dimension(700, 250));
        setSize(getPreferredSize());
        setBackground(new Color(0, 0, 0, 0));
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JLabel txt = new JLabel("  Do you really want to exit ?         ");
        txt.setFont(new Font("World of Water", Font.HANGING_BASELINE, 45));
        txt.setForeground(Color.black);
        txt.setLocation(0, 30);

        noButton = new JButton(getImg("no.png", 100, 100));
        noButton.setBackground(null);
        noButton.setFocusable(true);
        noButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        yesButton = new JButton(getImg("yes.png", 100, 100));
        yesButton.setBackground(null);
        yesButton.setFocusable(true);
        yesButton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        noButton.addActionListener(ev -> setVisible(false));
        yesButton.addActionListener(ev -> System.exit(0));

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(650, 200));
        panel.setSize(panel.getPreferredSize());
        panel.setBackground(Color.darkGray);
        panel.setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));;
        panel.add(txt);
        panel.add(noButton);
        panel.add(yesButton);

        exitBarButton = new JButton(" ");
        exitBarButton.setIcon(new ImageIcon(getImg("cancel.png")));
        exitBarButton.setBackground(null);
        exitBarButton.setBorder(null);
        exitBarButton.setFocusable(false);
        exitBarButton.addActionListener(arg0 -> {
            setVisible(false);
            revalidate();
        });

        JLabel label = new JLabel("   Exit");
        label.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
        label.setForeground(Color.WHITE);
        label.setBackground(null);
        label.setOpaque(false);

        JPanel barPanel = new JPanel();
        barPanel.setPreferredSize(new Dimension(700, 40));
        barPanel.setSize(barPanel.getPreferredSize());
        barPanel.setLayout(new BorderLayout());
        barPanel.add(exitBarButton, BorderLayout.EAST);
        barPanel.add(label, BorderLayout.WEST);
        barPanel.setBackground(new Color(18, 30, 49));
        barPanel.setBorder(BorderFactory.createBevelBorder(0, new Color(18, 30, 49), Color.black));

        add(barPanel);
        add(panel);

        validate();
        setVisible(false);
    }
}
package tetris.view;

import tetris.model.GameLauncher;
import static tetris.resource.ResourceManager.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class LauncherPreview extends JPanel {
    private JFrame window;
    private JFrame frame;
    private JButton exitButton, exitBarButton;
    private JButton nextButton;
    private int width, height, sleep = 0;
    private AnimationPreview animationPreview;
    private Image backImage, iconImage;

    private GameLauncher gameLauncher;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backImage, 0, 0, width, height, this);
    }

    public LauncherPreview(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
        width = 1000 - 150;
        height = 700 - 150;
//        backImage = getImg("previewBackground.jpg");
        iconImage = getImg("Tetris-logo1.png", 60, 50).getImage();
//
//        window = new JFrame();
//        window.setPreferredSize(new Dimension(width + 100, height + 100));
//        window.setSize(window.getPreferredSize());
//        window.setLocationRelativeTo(null);
//        window.setUndecorated(true);
//        window.setBackground(new Color(0, 0, 0, 0));
//        window.setIconImage(iconImage);
//        window.addKeyListener(new KeyListener() {
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
//                    nextButton.doClick();
//                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
//                    exitButton.doClick();
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//            }
//
//            @Override
//            public void keyTyped(KeyEvent e) {
//            }
//
//        });
//
//        nextButton = new JButton(getImg("next.png", width / 10, height / 10));
//        exitButton = new JButton(getImg("back.png", width / 10, height / 10));//"img/back.png"
//
//        nextButton.setBackground(new Color(0, 0, 0, 0));
//        nextButton.setFocusable(false);
//        nextButton.setOpaque(false);
//        nextButton.setBounds(width * 8 / 10 - 4, height * 19 / 23 - 6, width / 9, height / 9);
//
//        exitButton.setBackground(new Color(0, 0, 0, 0));
//        exitButton.setFocusable(false);
//        exitButton.setOpaque(false);
//        exitButton.setBounds(width * 1 / 10 - 6, height * 19 / 23 + 6, width / 9, height / 9);
//
//        setLayout(null);
//        setPreferredSize(new Dimension(width, height));
//        setSize(getPreferredSize());
//        setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));
//        setOpaque(true);
//        add(nextButton);
//        add(exitButton);
//        nextButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent ev) {
//
//                frame.remove(animationPreview);
//                animationPreview = new AnimationPreview(1, frame);
//                frame.setVisible(true);
//                frame.add(animationPreview);
//                window.setVisible(false);
//                window.removeAll();
//            }
//        });
//
//        exitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent ev) {
//                exit.setVisible(true);
//                revalidate();
//            }
//        });
//
//        exitBarButton = new JButton(" ");
//        exitBarButton.setIcon(new ImageIcon(getImg("cancel.png")));
//        exitBarButton.setBackground(null);
//        exitBarButton.setBorder(null);
//        exitBarButton.setFocusable(false);
//        exitBarButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent arg0) {
//                exit.setVisible(true);
//                revalidate();
//            }
//        });
//
//        JLabel label = new JLabel("   Tetris");
//        label.setFont(new Font("Showcard Gothic", Font.PLAIN, 23));
//        label.setForeground(Color.WHITE);
//        label.setBackground(null);
//        label.setOpaque(false);
//
//        JPanel barPanel = new JPanel();
//        barPanel.setPreferredSize(new Dimension(width + 100, 40));
//        barPanel.setSize(barPanel.getPreferredSize());
//        barPanel.setLayout(new BorderLayout());
//        barPanel.add(exitBarButton, BorderLayout.EAST);// WEST
//        barPanel.add(label, BorderLayout.WEST);
//        barPanel.setBackground(new Color(18, 30, 49));// 18,30,49//54, 64, 69
//        barPanel.setBorder(BorderFactory.createBevelBorder(0, Color.DARK_GRAY, Color.black));
//
//        JPanel panel = new JPanel();
//        panel.setPreferredSize(new Dimension(width + 100, 40));
//        panel.setSize(barPanel.getPreferredSize());
//        panel.setBackground(new Color(18, 30, 49));// 18,30,49//54, 64, 69
//        panel.setBorder(BorderFactory.createBevelBorder(1, Color.DARK_GRAY, Color.black));
//
//        window.setLayout(new FlowLayout());
//        window.add(barPanel);
//        window.add(this);
//        window.add(panel);
//        window.validate();
//
        frame = new JFrame();
        frame.setSize(1000, 700); // new
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0));
        animationPreview = new AnimationPreview(1, frame);
        frame.setVisible(true);
        frame.setIconImage(iconImage);
        frame.add(animationPreview);
    }

    public class AnimationPreview extends JPanel {
        protected int w, h, w1, h1, i, y;
        public Timer timer;
        Image img;

        public AnimationPreview(int i, JFrame frame) {
            this.i = i;
            img = getImg("Tetris-logo1.png");
            setSize(1000, 700);
            setBackground(new Color(0, 0, 0, 0));
            setOpaque(false);
            w = width * i - 200;
            h = height * i;
            y = height - 40;
            timer = new Timer(0, null);
            timer.addActionListener(arg0 -> {
                action(i);
                repaint();
            });
            timer.start();
        }

        public void action(int i) {
            if (i == 1) {
                action2();
            } else if (i == 0) {
                if (h < height) {
                    w = w + (width - 100) / 50;
                    h = h + height / 50;
                } else {
                    timer.stop();
                    frame.setVisible(false);
                }
            }

        }

        public void action2() {
            timer.setDelay(0);
            if (y > 250) {
                w = h = 0;
                w1 += 500 / 140;
                h1 += 350 / 140;
                y -= 2;
            } else {
                if (sleep < 200)
                    sleep += 6;
                else {
                    timer.stop();
                    frame.setVisible(false);
                    gameLauncher.getLauncherView().setVisible(true);
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);// clear and repaint
            g.drawImage(backImage, 50 + (width - w) / 2, (height - h) / 2 + 40, w, h, this);
            g.drawImage(img, 50 + (width - w1) / 2, y, w1, h1, this);
        }
    }
}
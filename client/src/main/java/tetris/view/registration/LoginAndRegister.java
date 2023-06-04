package tetris.view.registration;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import tetris.model.GameLauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginAndRegister extends JPanel {

    private final GameLauncher gameLauncher;
    private MigLayout layout;
    private Register register;
    private Login login;
    private Animator animator;
    private boolean isLogin;
    public static Color mainColor = new Color(246, 207, 104);

    public void setAnimate(int animate) {
        layout.setComponentConstraints(register, "pos (50%)-290px-" + animate + " 0.5al n n");
        layout.setComponentConstraints(login, "pos (50%)-10px+" + animate + " 0.5al n n");
        if (animate == 30) {
            if (isLogin) {
                setComponentZOrder(login, 0);
            } else {
                setComponentZOrder(register, 0);
            }
        }
        revalidate();
    }

    public LoginAndRegister(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
        initComponents();
        init();
        initAnimator();
    }

    private void initAnimator() {
        animator = new Animator(1000, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (isLogin) {
                    register.setAlpha(fraction);
                    login.setAlpha(1f - fraction);
                } else {
                    register.setAlpha(1f - fraction);
                    login.setAlpha(fraction);
                }
            }
        });
        animator.addTarget(new PropertySetter(this, "animate", 0, 30, 0));
        animator.setResolution(0);
    }

    private void init() {
        setBackground(mainColor);
        layout = new MigLayout("fill", "fill", "fill");
        setLayout(layout);
        register = new Register(gameLauncher);
        login = new Login(gameLauncher);
        applyEvent(register, false);
        applyEvent(login, true);
        add(register, "pos (50%)-290px 0.5al n n");
        add(login, "pos (50%)-10px 0.5al n n");
        register.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    showLogin(false);
                }
            }
        });
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    showLogin(true);
                }
            }
        });
    }

    public void showLogin(boolean show) {
        if (show != isLogin) {
            if (!animator.isRunning()) {
                isLogin = show;
                animator.start();
            }
        }
    }

    private void applyEvent(JComponent panel, boolean login) {
        for (Component com : panel.getComponents()) {
            com.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent me) {
                    showLogin(login);
                }
            });
        }
    }

    public void setEventLogin(EventLogin event) {
        login.setEventLogin(event);
    }

    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 698, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 452, Short.MAX_VALUE)
        );
    }
}
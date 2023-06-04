package tetris.view.registration;

import tetris.model.GameLauncher;
import tetris.model.dto.RegistrationRequestDto;
import tetris.view.component.Button;
import tetris.view.component.Password;
import tetris.view.component.TextField;

import java.awt.*;

public class Register extends PanelCustom {

    private final GameLauncher gameLauncher;

    public Register(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
        initComponents();
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        textField1 = new TextField();
        textField2 = new TextField();
        textField3 = new TextField();
        password1 = new Password();
        button1 = new Button();

        setBackground(new java.awt.Color(58, 58, 58));

        jLabel1.setFont(new java.awt.Font("sansserif", Font.BOLD, 20));
        jLabel1.setForeground(new java.awt.Color(242, 242, 242));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SIGN UP");

        textField1.setForeground(new java.awt.Color(242, 242, 242));
        textField1.setFont(new java.awt.Font("sansserif", Font.PLAIN, 14));
        textField1.setHint("NAME");

        textField2.setForeground(new java.awt.Color(242, 242, 242));
        textField2.setFont(new java.awt.Font("sansserif", Font.PLAIN, 14));
        textField2.setHint("EMAIL");

        textField3.setForeground(new java.awt.Color(242, 242, 242));
        textField3.setFont(new java.awt.Font("sansserif", Font.PLAIN, 14));
        textField3.setHint("USER NAME");

        password1.setForeground(new java.awt.Color(242, 242, 242));
        password1.setFont(new java.awt.Font("sansserif", Font.PLAIN, 14));
        password1.setHint("PASSWORD");

        button1.setBackground(new java.awt.Color(86, 142, 255));
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setText("Sign Up");
        button1.setFont(new java.awt.Font("sansserif", Font.BOLD, 14));
        button1.addActionListener((event) -> {
            RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(textField1.getText(), textField3.getText(),
                    textField2.getText(), password1.getText());
            gameLauncher.getLauncherController().handleRegistrationButtonClick(registrationRequestDto);
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(password1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(textField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                        .addComponent(textField2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(textField3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel1)
                                .addGap(40, 40, 40)
                                .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(textField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(textField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(password1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40))
        );
    }

    private Button button1;
    private javax.swing.JLabel jLabel1;
    private Password password1;
    private TextField textField1;
    private TextField textField2;
    private TextField textField3;
}
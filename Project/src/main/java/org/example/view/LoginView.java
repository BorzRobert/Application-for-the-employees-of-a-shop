package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static javax.swing.BoxLayout.Y_AXIS;

public class LoginView extends JFrame {
    private JLabel title;
    private JLabel username;
    private JLabel password;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JButton btnLogin;
    private JButton btnGuestMode;

    public LoginView() throws HeadlessException {
        setSize(300, 300);
        setLocationRelativeTo(null);
        initializeFields();
        setLayout(new BoxLayout(getContentPane(), Y_AXIS));
        add(title);
        add(username);
        add(tfUsername);
        add(password);
        add(tfPassword);
        add(btnLogin);
        add(btnGuestMode);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initializeFields() {
        tfUsername = new JTextField();
        tfPassword = new JPasswordField();
        title = new JLabel("Musical Instrument Shop(LogIn Page)");
        username = new JLabel("Username");
        password = new JLabel("Password");
        btnLogin = new JButton("Login");
        btnGuestMode = new JButton("Continue as a guest");
    }

    public String getUsername() {
        return tfUsername.getText();
    }

    public String getPassword() {
        return tfPassword.getText();
    }

    public void setLoginButtonListener(ActionListener loginButtonListener) {
        btnLogin.addActionListener(loginButtonListener);
    }

    public void setGuestModeButtonListener(ActionListener guestModeButtonListener) {
        btnGuestMode.addActionListener(guestModeButtonListener);
    }

    public void setVisible() {
        this.setVisible(true);
    }

}

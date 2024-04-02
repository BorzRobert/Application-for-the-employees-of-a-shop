package org.example.view;

import org.example.model.order.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static javax.swing.BoxLayout.Y_AXIS;

public class CashierView extends JFrame {
    private JLabel title;
    private JLabel id;
    private JLabel username;
    private JLabel password;
    private JLabel address;
    private JLabel activeOrders;
    private JLabel activeUsers;

    private JTextField tfAddress;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JTextField tfId;

    private JButton btnRegister;
    private JButton btnProcessOrder;
    private JButton btnShowActiveOrders;
    private JButton btnUpdateUser;
    private JButton btnDeleteUser;


    private JTextArea ordersList;
    private JTextArea usersList;

    private JComboBox<Order> orderSelector;

    public CashierView() throws HeadlessException {
        setSize(700, 700);
        setLocationRelativeTo(null);
        initializeFields();
        setLayout(new BoxLayout(getContentPane(), Y_AXIS));
        add(title);
        add(id);
        add(tfId);
        add(username);
        add(tfUsername);
        add(password);
        add(tfPassword);
        add(address);
        add(tfAddress);
        add(btnRegister);
        add(btnUpdateUser);
        add(btnDeleteUser);
        add(activeUsers);
        add(usersList);
        add(activeOrders);
        add(ordersList);
        add(orderSelector);
        add(btnProcessOrder);
        add(btnShowActiveOrders);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initializeFields() {
        tfUsername = new JTextField();
        tfAddress = new JTextField();
        tfId = new JTextField();
        tfPassword = new JPasswordField();
        title = new JLabel("You are logged in as a cashier");
        username = new JLabel("Username");
        password = new JLabel("Password");
        address = new JLabel("Address");
        id = new JLabel("Id(used just for deletion and update)");
        activeOrders = new JLabel("Active orders");
        activeUsers = new JLabel("Active users");
        btnRegister = new JButton("Register(fidelity customer)");
        btnUpdateUser = new JButton("Update user");
        btnDeleteUser = new JButton("Delete user");
        btnProcessOrder = new JButton("Process");
        btnShowActiveOrders = new JButton("Show active orders and users");
        ordersList = new JTextArea();
        usersList = new JTextArea();
        orderSelector = new JComboBox<>();
        ordersList.setEditable(false);
        usersList.setEditable(false);
        btnProcessOrder.setEnabled(false);
    }

    public Long getIdLong() {
        return Long.parseLong(tfId.getText());
    }

    public String getIdString() {
        return tfId.getText();
    }

    public String getAddress() {
        return tfAddress.getText();
    }

    public String getUsername() {
        return tfUsername.getText();
    }

    public String getPassword() {
        return tfPassword.getText();
    }

    public JTextField getTfAddress() {
        return tfAddress;
    }

    public JTextField getTfUsername() {
        return tfUsername;
    }

    public JTextField getTfPassword() {
        return tfPassword;
    }

    public JTextField getTfId() {
        return tfId;
    }

    public JTextArea getOrdersList() {
        return ordersList;
    }

    public JTextArea getUsersList() {
        return usersList;
    }

    public JButton getBtnProcessOrder() {
        return btnProcessOrder;
    }

    public JButton getBtnShowActiveOrders() {
        return btnShowActiveOrders;
    }

    public JComboBox<Order> getOrderSelector() {
        return orderSelector;
    }

    public void registerButtonListener(ActionListener registerButtonListener) {
        btnRegister.addActionListener(registerButtonListener);
    }

    public void processButtonListener(ActionListener processButtonListener) {
        btnProcessOrder.addActionListener(processButtonListener);
    }

    public void showActiveOrdersButtonListener(ActionListener showActiveOrdersButtonListener) {
        btnShowActiveOrders.addActionListener(showActiveOrdersButtonListener);
    }

    public void updateUserButtonListener(ActionListener updateUserButtonListener) {
        btnUpdateUser.addActionListener(updateUserButtonListener);
    }

    public void deleteUserButtonListener(ActionListener deleteUserButtonListener) {
        btnDeleteUser.addActionListener(deleteUserButtonListener);
    }

    public void setVisible() {
        this.setVisible(true);
    }
}

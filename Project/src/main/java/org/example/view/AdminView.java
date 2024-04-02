package org.example.view;

import org.example.model.order.Order;
import org.example.model.security.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static javax.swing.BoxLayout.Y_AXIS;

public class AdminView extends JFrame {
    private JLabel title;
    private JLabel id;
    private JLabel username;
    private JLabel password;
    private JLabel address;
    private JLabel activeCashiers;
    private JLabel report;
    private JLabel percentage;

    private JLabel month;

    private JTextField tfAddress;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JTextField tfId;
    private JTextField tfPercentage;

    private JButton btnRegister;
    private JButton btnShowActiveCashiers;
    private JButton btnUpdateCashier;
    private JButton btnDeleteCashier;
    private JButton btnGenerateReport;
    private JButton btnUpdatePercentage;

    private JTextArea cashiersList;
    private JTextArea reportList;

    private JComboBox<User> cashierSelector;
    private JComboBox<Integer> monthSelector;

    public AdminView() throws HeadlessException {
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
        add(btnUpdateCashier);
        add(btnDeleteCashier);
        add(activeCashiers);
        add(cashiersList);
        add(report);
        add(reportList);
        add(cashierSelector);
        add(month);
        add(monthSelector);
        add(btnGenerateReport);
        add(btnShowActiveCashiers);
        add(percentage);
        add(tfPercentage);
        add(btnUpdatePercentage);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initializeFields() {
        tfUsername = new JTextField();
        tfAddress = new JTextField();
        tfId = new JTextField();
        tfPassword = new JPasswordField();
        tfPercentage = new JTextField();
        title = new JLabel("You are logged in as an admin");
        username = new JLabel("Username");
        password = new JLabel("Password");
        address = new JLabel("Address");
        percentage = new JLabel("Percentage");
        id = new JLabel("Id(used just for deletion and update)");
        month = new JLabel("Select month for the generated report");
        activeCashiers = new JLabel("Active cashiers");
        report = new JLabel("Report");
        btnRegister = new JButton("Register(cashier)");
        btnUpdateCashier = new JButton("Update cashier");
        btnDeleteCashier = new JButton("Delete cashier");
        btnGenerateReport = new JButton("Generate report");
        btnShowActiveCashiers = new JButton("Show active cashiers");
        btnUpdatePercentage = new JButton("Update bonus percentage");
        cashiersList = new JTextArea();
        reportList = new JTextArea();
        cashierSelector = new JComboBox<>();
        monthSelector = new JComboBox<>();
        reportList.setEditable(false);
        cashiersList.setEditable(false);
        btnGenerateReport.setEnabled(false);
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

    public int getTfPercentageInt() {
        return Integer.parseInt(tfPercentage.getText());
    }

    public JTextField getTfPercentage() {
        return tfPercentage;
    }

    public JTextArea getCashiersList() {
        return cashiersList;
    }

    public JTextArea getReportList() {
        return reportList;
    }

    public JButton getBtnGenerateReport() {
        return btnGenerateReport;
    }

    public JButton getBtnShowActiveCashiers() {
        return btnShowActiveCashiers;
    }

    public JComboBox<User> getCashierSelector() {
        return cashierSelector;
    }

    public JComboBox<Integer> getMonthSelector() {
        return monthSelector;
    }

    public void registerButtonListener(ActionListener registerButtonListener) {
        btnRegister.addActionListener(registerButtonListener);
    }

    public void generateReportButtonListener(ActionListener processButtonListener) {
        btnGenerateReport.addActionListener(processButtonListener);
    }

    public void showActiveCashiersButtonListener(ActionListener showActiveOrdersButtonListener) {
        btnShowActiveCashiers.addActionListener(showActiveOrdersButtonListener);
    }

    public void updateCashierButtonListener(ActionListener updateUserButtonListener) {
        btnUpdateCashier.addActionListener(updateUserButtonListener);
    }

    public void deleteCashierButtonListener(ActionListener deleteUserButtonListener) {
        btnDeleteCashier.addActionListener(deleteUserButtonListener);
    }

    public void updatePercentageButtonListener(ActionListener updatePercentageButtonListener) {
        btnUpdatePercentage.addActionListener(updatePercentageButtonListener);
    }

    public void setVisible() {
        this.setVisible(true);
    }
}

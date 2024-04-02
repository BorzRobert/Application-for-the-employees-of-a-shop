package org.example.controller;

import org.example.model.Statistics.Statistics;
import org.example.model.security.ERole;
import org.example.model.security.User;
import org.example.model.security.UserBuilder;
import org.example.model.validation.Notification;
import org.example.service.security.PaymentService;
import org.example.service.security.SecurityService;
import org.example.service.user.UserService;
import org.example.view.AdminView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminController {

    private final AdminView view;

    private final SecurityService securityService;

    private final PaymentService paymentService;

    private final UserService userService;

    public AdminController(AdminView view, SecurityService securityService, PaymentService paymentService, UserService userService) {
        this.view = view;
        this.securityService = securityService;
        this.paymentService = paymentService;
        this.userService = userService;

        this.view.deleteCashierButtonListener(new AdminController.DeleteUserButtonListener());
        this.view.registerButtonListener(new AdminController.RegisterButtonListener());
        this.view.generateReportButtonListener(new AdminController.GenerateReportButtonListener());
        this.view.showActiveCashiersButtonListener(new AdminController.ShowActiveCashiersButtonListener());
        this.view.updateCashierButtonListener(new AdminController.UpdateUserButtonListener());
        this.view.updatePercentageButtonListener(new AdminController.UpdatePercentageButtonListener());
    }

    private class UpdatePercentageButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!view.getTfPercentage().getText().equals("") && isNumeric(view.getTfPercentage().getText())) {
                Notification<Integer> updateResultNotification = paymentService.updateBonusPercentage(view.getTfPercentageInt());
                if (!updateResultNotification.hasErrors()) {
                    JOptionPane.showMessageDialog(view.getContentPane(), "The bonus percentage has been updated successfully!");
                    view.getTfPercentage().setText("");
                } else
                    JOptionPane.showMessageDialog(view.getContentPane(), updateResultNotification.getFormattedErrors());
            } else
                JOptionPane.showMessageDialog(view.getContentPane(), "You must provide a valid number in order to update bonus percentage!");
        }
    }

    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();
            String address = view.getAddress();

            Notification<User> registerNotification = securityService.register(username, password, address, true);

            if (registerNotification.hasErrors()) {
                JOptionPane.showMessageDialog(view.getContentPane(), registerNotification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(view.getContentPane(), "Registration successful!");
                refreshCashiers();
                clearTextFields();
            }
        }
    }

    private class GenerateReportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.getReportList().setText("");
            User selectedCashier = (User) view.getCashierSelector().getSelectedItem();
            int period = (int) view.getMonthSelector().getSelectedItem();
            Notification<Statistics> cashierStatisticsNotification = paymentService.findCashierStatistics(selectedCashier.getId(), period);
            if (!cashierStatisticsNotification.hasErrors()) {
                view.getReportList().append(cashierStatisticsNotification.getResult().toString());
            } else
                JOptionPane.showMessageDialog(view.getContentPane(), cashierStatisticsNotification.getFormattedErrors());
        }
    }

    private class ShowActiveCashiersButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshCashiers();
            initializeMonths();
            view.getBtnGenerateReport().setEnabled(true);
            view.getBtnShowActiveCashiers().setEnabled(false);
        }
    }

    private class UpdateUserButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!view.getIdString().equals("")) {
                Long id = view.getIdLong();
                Notification<User> findUserNotification = userService.findById(id);
                if (!findUserNotification.hasErrors()) {
                    if (userService.findById(id).getResult().getRoles().get(0).getRole().equals(ERole.CASHIER.toString())) {
                        String username = view.getUsername();
                        String password = view.getPassword();
                        String address = view.getAddress();
                        User updatedUser = new UserBuilder()
                                .setId(id)
                                .setUsername(username)
                                .setAddress(address)
                                .setPassword(password)
                                .build();
                        Notification<User> addUserNotification = userService.updateUser(updatedUser);
                        if (addUserNotification.hasErrors()) {
                            JOptionPane.showMessageDialog(view.getContentPane(), addUserNotification.getFormattedErrors());
                        } else {
                            JOptionPane.showMessageDialog(view.getContentPane(), "The cashier with id=" + id + " was successfully updated");
                            refreshCashiers();
                            clearTextFields();
                        }
                    } else
                        JOptionPane.showMessageDialog(view.getContentPane(), "You don't have the access to update the user with the provided id!");
                } else
                    JOptionPane.showMessageDialog(view.getContentPane(), findUserNotification.getFormattedErrors());

            } else
                JOptionPane.showMessageDialog(view.getContentPane(), "You must provide an id in order to update the user!");

        }
    }

    private class DeleteUserButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!view.getIdString().equals("")) {
                Notification<User> userNotification = new Notification<>();
                Long id = view.getIdLong();
                userNotification = userService.findById(id);
                if (!userNotification.hasErrors()) {
                    if (userNotification.getResult().getRoles().get(0).getRole().equals(ERole.CASHIER.toString())) {
                        userService.deleteUser(userNotification.getResult().getId());
                        JOptionPane.showMessageDialog(view.getContentPane(), "The cashier with id=" + id + " was successfully deleted!");
                        refreshCashiers();
                        clearTextFields();
                    } else
                        JOptionPane.showMessageDialog(view.getContentPane(), "You don't have the access to delete the user with the provided id!");

                } else
                    JOptionPane.showMessageDialog(view.getContentPane(), userNotification.getFormattedErrors());

            } else
                JOptionPane.showMessageDialog(view.getContentPane(), "You must provide an id in order to delete the user!");
        }
    }

    public void initializeMonths() {
        for (int i = 1; i <= 12; i++)
            view.getMonthSelector().addItem(i);
    }

    public void refreshCashiers() {
        view.getCashiersList().setText("");
        List<User> allCashiers = userService.findAll();
        view.getCashierSelector().removeAllItems();
        for (User user : allCashiers) {
            if (user.getRoles().get(0).getRole().equals(ERole.CASHIER.toString())) {
                view.getCashierSelector().addItem(user);
                view.getCashiersList().append(user.toString() + "\n");
            }
        }
    }

    public void clearTextFields() {
        view.getTfId().setText("");
        view.getTfAddress().setText("");
        view.getTfUsername().setText("");
        view.getTfPassword().setText("");
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

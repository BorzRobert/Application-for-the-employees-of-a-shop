package org.example.controller;

import org.example.model.cart.Cart;
import org.example.model.cart.CartBuilder;
import org.example.model.item.Item;
import org.example.model.order.Order;
import org.example.model.security.ERole;
import org.example.model.security.User;
import org.example.model.security.UserBuilder;
import org.example.model.validation.Notification;
import org.example.service.security.PaymentService;
import org.example.service.security.SecurityService;
import org.example.service.user.UserService;
import org.example.view.CashierView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

public class CashierController {

    private final CashierView view;

    private final SecurityService securityService;

    private final PaymentService paymentService;

    private final UserService userService;

    private final User cashier;

    public CashierController(CashierView view, SecurityService securityService, PaymentService paymentService, UserService userService, User cashier) {
        this.view = view;
        this.securityService = securityService;
        this.paymentService = paymentService;
        this.userService = userService;
        this.cashier = cashier;
        this.view.registerButtonListener(new CashierController.RegisterButtonListener());
        this.view.processButtonListener(new CashierController.ProcessButtonListener());
        this.view.showActiveOrdersButtonListener(new CashierController.ShowActiveOrdersButtonListener());
        this.view.updateUserButtonListener(new CashierController.UpdateUserButtonListener());
        this.view.deleteUserButtonListener(new CashierController.DeleteUserButtonListener());
    }

    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();
            String address = view.getAddress();
            List<User> allUsers;

            Notification<User> registerNotification = securityService.register(username, password, address, false);

            if (registerNotification.hasErrors()) {
                JOptionPane.showMessageDialog(view.getContentPane(), registerNotification.getFormattedErrors());
            } else {
                Notification<Cart> createCartNotification = paymentService.createCart(new CartBuilder().setOwnerId(registerNotification.getResult().getId()).build());
                if (createCartNotification.hasErrors())
                    JOptionPane.showMessageDialog(view.getContentPane(), createCartNotification.getFormattedErrors());
                else {
                    JOptionPane.showMessageDialog(view.getContentPane(), "Registration successful!");
                    refreshUsers();
                    clearTextFields();
                }
            }
        }
    }

    private class ProcessButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Random random = new Random();
            int randomNumber = random.nextInt(10);
            if (randomNumber > 1) {
                view.getOrdersList().setText("");
                List<Order> allOrders = paymentService.retrieveOrders().getResult();
                if (allOrders.size() > 0) {
                    Order selectedOrder = (Order) view.getOrderSelector().getSelectedItem();
                    paymentService.process(selectedOrder, cashier);
                    JOptionPane.showMessageDialog(view.getContentPane(), "Order #" + selectedOrder.getOrderId() + " was successfully processed!");
                    refreshUsers();
                    refresOrders();
                } else
                    JOptionPane.showMessageDialog(view.getContentPane(), "There are no orders to process!");
            } else
                JOptionPane.showMessageDialog(view.getContentPane(), "Something went wrong, please try again later!");
        }
    }

    private class ShowActiveOrdersButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshUsers();
            refresOrders();
            view.getBtnProcessOrder().setEnabled(true);
            view.getBtnShowActiveOrders().setEnabled(false);
        }
    }

    private class UpdateUserButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!view.getIdString().equals("")) {
                Long id = view.getIdLong();
                Notification<User> findUserNotification = userService.findById(id);
                if (!findUserNotification.hasErrors()) {
                    if (userService.findById(id).getResult().getRoles().get(0).getRole().equals(ERole.FIDELITY_USER.toString())) {
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
                            JOptionPane.showMessageDialog(view.getContentPane(), "The fidelity-user with id=" + id + " was successfully updated");
                            refreshUsers();
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
                    if (userNotification.getResult().getRoles().get(0).getRole().equals(ERole.FIDELITY_USER.toString())) {
                        userService.deleteUser(userNotification.getResult().getId());
                        JOptionPane.showMessageDialog(view.getContentPane(), "The fidelity-user with id=" + id + " was successfully deleted!");
                        refreshUsers();
                        refresOrders();
                        clearTextFields();
                    } else
                        JOptionPane.showMessageDialog(view.getContentPane(), "You don't have the access to delete the user with the provided id!");

                } else
                    JOptionPane.showMessageDialog(view.getContentPane(), userNotification.getFormattedErrors());

            } else
                JOptionPane.showMessageDialog(view.getContentPane(), "You must provide an id in order to delete the user!");
        }
    }

    public void refreshUsers() {
        view.getUsersList().setText("");
        List<User> allUsers = userService.findAll();
        for (User user : allUsers) {
            if (user.getRoles().get(0).getRole().equals(ERole.FIDELITY_USER.toString()))
                view.getUsersList().append(user.toString() + "\n");
        }
    }

    public void refresOrders() {
        view.getOrdersList().setText("");
        List<Order> allOrders = paymentService.retrieveOrders().getResult();
        view.getOrderSelector().removeAllItems();
        for (Order order : allOrders) {
            view.getOrderSelector().addItem(order);
            view.getOrdersList().append("Order with number #" + order.getOrderId() + " having the items:\n");
            for (Item item : order.getItems()) {
                view.getOrdersList().append(item.toString() + "\n");
            }
        }
    }

    public void clearTextFields() {
        view.getTfId().setText("");
        view.getTfAddress().setText("");
        view.getTfUsername().setText("");
        view.getTfPassword().setText("");
    }
}

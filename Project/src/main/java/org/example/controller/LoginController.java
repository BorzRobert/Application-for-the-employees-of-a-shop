package org.example.controller;

import org.example.model.security.User;
import org.example.model.validation.Notification;
import org.example.service.item.ItemService;
import org.example.service.security.PaymentService;
import org.example.service.security.SecurityService;
import org.example.service.user.UserService;
import org.example.view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {

    private final LoginView view;
    private final GuestView guestView;
    private final SecurityService securityService;
    private final ItemService itemService;
    private final PaymentService paymentService;
    private final UserService userService;


    public LoginController(LoginView view, GuestView guestView, SecurityService securityService, ItemService itemService, PaymentService paymentService, UserService userService) {
        this.view = view;
        this.guestView = guestView;
        this.securityService = securityService;
        this.itemService = itemService;
        this.paymentService = paymentService;
        this.userService = userService;
        this.view.setLoginButtonListener(new LoginButtonListener());
        this.view.setGuestModeButtonListener(new GuestModeButtonListener());
    }

    private class LoginButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();

            Notification<User> res = securityService.login(username, password);
            if (!res.hasErrors()) {
                JOptionPane.showMessageDialog(view.getContentPane(), "Login successful!");
                switch (res.getResult().getRoles().get(0).getRole()) {
                    case "CASHIER":
                        CashierView cashierView = new CashierView();
                        CashierController cashierController = new CashierController(cashierView, securityService, paymentService, userService, res.getResult());
                        view.setVisible(false);
                        cashierView.setVisible(true);
                        break;
                    case "FIDELITY_USER":
                        FidelityUserView fidelityUserView = new FidelityUserView();
                        FidelityUserController fidelityUserController = new FidelityUserController(fidelityUserView, itemService, paymentService, res.getResult());
                        view.setVisible(false);
                        fidelityUserView.setVisible(true);
                        break;
                    case "ADMINISTRATOR":
                        AdminView adminView = new AdminView();
                        AdminController adminController = new AdminController(adminView, securityService, paymentService, userService);
                        view.setVisible(false);
                        adminView.setVisible(true);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(view.getContentPane(), res.getFormattedErrors());
            }
        }
    }

    private class GuestModeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(view.getContentPane(), "You will be redirected to our guest page!");
            view.setVisible(false);
            guestView.setVisible();
        }
    }
}

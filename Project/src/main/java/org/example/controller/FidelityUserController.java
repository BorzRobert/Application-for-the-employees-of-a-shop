package org.example.controller;

import org.example.model.cart.Cart;
import org.example.model.item.Item;
import org.example.model.order.Order;
import org.example.model.security.User;
import org.example.model.validation.Notification;
import org.example.service.item.ItemService;
import org.example.service.security.PaymentService;
import org.example.view.FidelityUserView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class FidelityUserController {

    private final FidelityUserView view;

    private final ItemService itemService;

    private final PaymentService paymentService;

    private final User fidelityUser;

    public FidelityUserController(FidelityUserView view, ItemService itemService, PaymentService paymentService, User fidelityUser) {
        this.view = view;
        this.itemService = itemService;
        this.paymentService = paymentService;
        this.fidelityUser = fidelityUser;
        this.view.addToCartButtonListener(new FidelityUserController.AddToCartButtonListener());
        this.view.checkoutButtonListener(new FidelityUserController.CheckoutButtonListener());
        this.view.showItemsButtonListener(new FidelityUserController.ShowItemsButtonListener());
    }

    private class AddToCartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isInCart = false;
            view.getCartList().setText("");
            Notification<Cart> currentShoppingCart = paymentService.retrieveCart(fidelityUser.getId());
            Item selectedItem = (Item) view.getItemSelector().getSelectedItem();
            if (!currentShoppingCart.hasErrors()) {
                for (Item item : currentShoppingCart.getResult().getItems()) {
                    if (selectedItem.toString().equals(item.toString()))
                        isInCart = true;
                }
                if (!isInCart) {
                    try {
                        paymentService.addToCart(currentShoppingCart.getResult().getCartId(), selectedItem.getId());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    currentShoppingCart.getResult().getItems().add(selectedItem);
                } else
                    JOptionPane.showMessageDialog(view.getContentPane(), "You can buy just one item of a kind per order!");

                for (Item item : currentShoppingCart.getResult().getItems()) {
                    view.getCartList().append(item.toString() + "\n");
                }
            } else {
                JOptionPane.showMessageDialog(view.getContentPane(), currentShoppingCart.getFormattedErrors());
            }
        }
    }

    private class CheckoutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Order order = new Order();
            order.setOrderId(1L);
            order.setOwnerId(fidelityUser.getId());
            Notification<Cart> currentShoppingCart = paymentService.retrieveCart(fidelityUser.getId());
            if (currentShoppingCart.getResult().getItems().size() > 0) {
                order.setItems(currentShoppingCart.getResult().getItems());
                Notification<Order> registerNotification = paymentService.checkout(order);
                if (!registerNotification.hasErrors()) {
                    JOptionPane.showMessageDialog(view.getContentPane(), "The checkout was done successfully(items will remain in cart until a cashier will confirm the transaction)!\n Don't modify the cart until your order is confirmed!");
                } else {
                    JOptionPane.showMessageDialog(view.getContentPane(), registerNotification.getFormattedErrors());
                }
            } else
                JOptionPane.showMessageDialog(view.getContentPane(), "The shopping cart is empty, add something to checkout!");
        }
    }

    private class ShowItemsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Item> items = itemService.findAll();
            Notification<Cart> shoppingCartItems = paymentService.retrieveCart(fidelityUser.getId());
            view.getItemSelector().removeAllItems();
            for (Item item : items) {
                view.getItemsList().append(item.toString() + "\n");
                view.getItemSelector().addItem(item);
            }
            for (Item item : shoppingCartItems.getResult().getItems()) {
                view.getCartList().append(item.toString() + "\n");
            }
            view.getBtnShowItems().setEnabled(false);
            view.getBtnAddToCart().setEnabled(true);
            view.getBtnCheckout().setEnabled(true);

            view.getItemsList().append("\n\n\n\nFidelity points: " + fidelityUser.getPoints() + " points");
        }
    }
}

package org.example.controller;

import org.example.model.item.Item;
import org.example.model.order.Order;
import org.example.model.validation.Notification;
import org.example.service.item.ItemService;
import org.example.service.security.PaymentService;
import org.example.view.GuestView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GuestController {

    private final GuestView view;

    private final ItemService itemService;

    private final PaymentService paymentService;

    private final List<Item> floatingShoppingCart = new ArrayList<>();

    public GuestController(GuestView view, ItemService itemService, PaymentService paymentService) {
        this.view = view;
        this.itemService = itemService;
        this.paymentService = paymentService;
        this.view.addToCartButtonListener(new GuestController.AddToCartButtonListener());
        this.view.checkoutButtonListener(new GuestController.CheckoutButtonListener());
        this.view.showItemsButtonListener(new GuestController.ShowItemsButtonListener());
    }

    private class AddToCartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Item selectedItem = (Item) view.getItemSelector().getSelectedItem();
            view.getCartList().setText("");
            if (!floatingShoppingCart.contains(selectedItem))
                floatingShoppingCart.add(selectedItem);
            else
                JOptionPane.showMessageDialog(view.getContentPane(), "You can buy just one item of a kind per order!");

            for (Item item : floatingShoppingCart) {
                view.getCartList().append(item.toString() + "\n");
            }
        }
    }

    private class CheckoutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Order order = new Order();
            order.setOrderId(1L);
            order.setOwnerId(1L);
            if (floatingShoppingCart.size() > 0) {
                order.setItems(floatingShoppingCart);
                Notification<Order> registerNotification = paymentService.checkout(order);
                if (!registerNotification.hasErrors()) {
                    JOptionPane.showMessageDialog(view.getContentPane(), "The checkout was done successfully!");
                    view.getCartList().setText("");
                    floatingShoppingCart.clear();
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
            view.getItemsList().setText("");
            view.getItemSelector().removeAllItems();
            for (Item item : items) {
                view.getItemsList().append(item.toString() + "\n");
                view.getItemSelector().addItem(item);
            }
            view.getBtnShowItems().setEnabled(false);
            view.getBtnAddToCart().setEnabled(true);
            view.getBtnCheckout().setEnabled(true);
        }
    }
}

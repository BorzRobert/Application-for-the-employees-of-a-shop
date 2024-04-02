package org.example.view;

import org.example.model.item.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static javax.swing.BoxLayout.Y_AXIS;

public class FidelityUserView extends JFrame {
    private JLabel title;
    private JLabel availableItems;
    private JLabel currentCart;
    private JTextArea itemsList;
    private JTextArea cartList;
    private JButton btnAddToCart;
    private JButton btnCheckout;
    private JButton btnShowItems;
    private JComboBox<Item> itemSelector;

    public FidelityUserView() throws HeadlessException {
        setSize(700, 700);
        setLocationRelativeTo(null);
        initializeFields();
        setLayout(new BoxLayout(getContentPane(), Y_AXIS));
        add(title);
        add(availableItems);
        add(itemsList);
        add(currentCart);
        add(cartList);
        add(itemSelector);
        add(btnAddToCart);
        add(btnCheckout);
        add(btnShowItems);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public JTextArea getItemsList() {
        return itemsList;
    }

    public JTextArea getCartList() {
        return cartList;
    }

    public JComboBox<Item> getItemSelector() {
        return itemSelector;
    }

    public JButton getBtnShowItems() {
        return btnShowItems;
    }

    public JButton getBtnAddToCart() {
        return btnAddToCart;
    }

    public JButton getBtnCheckout() {
        return btnCheckout;
    }


    private void initializeFields() {
        title = new JLabel("You are logged in as a fidelity user");
        availableItems = new JLabel("Available Items");
        currentCart = new JLabel("Shopping Cart");
        itemsList = new JTextArea("");
        cartList = new JTextArea("");
        itemsList.setEditable(false);
        cartList.setEditable(false);
        btnAddToCart = new JButton("Add to cart");
        btnAddToCart.setEnabled(false);
        btnCheckout = new JButton("Checkout");
        btnCheckout.setEnabled(false);
        btnShowItems = new JButton("Show Items");
        itemSelector = new JComboBox<>();
    }

    public void addToCartButtonListener(ActionListener addToCartButtonListener) {
        btnAddToCart.addActionListener(addToCartButtonListener);
    }

    public void checkoutButtonListener(ActionListener checkoutButtonListener) {
        btnCheckout.addActionListener(checkoutButtonListener);
    }

    public void showItemsButtonListener(ActionListener showItemsButtonListener) {
        btnShowItems.addActionListener(showItemsButtonListener);
    }

    public void setVisible() {
        this.setVisible(true);
    }
}

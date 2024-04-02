package org.example.service.security;

import org.example.model.Statistics.Statistics;
import org.example.model.cart.Cart;
import org.example.model.item.Item;
import org.example.model.order.Order;
import org.example.model.security.User;
import org.example.model.validation.Notification;
import org.example.repository.cart.CartRepository;
import org.example.repository.item.ItemRepository;
import org.example.repository.order.OrderRepository;
import org.example.repository.security.RoleRepository;
import org.example.repository.security.StatisticsRepository;
import org.example.repository.security.UserRepository;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PaymentService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final StatisticsRepository statisticsRepository;

    public PaymentService(OrderRepository orderRepository, ItemRepository itemRepository, CartRepository cartRepository, StatisticsRepository statisticsRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.statisticsRepository = statisticsRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Notification<Order> checkout(Order order) {
        Notification<Order> checkoutNotification = new Notification<>();
        Order processedOrder;
        try {
            processedOrder = orderRepository.create(order);
        } catch (SQLException e) {
            checkoutNotification.addError("There was something wrong with the database!");
            return checkoutNotification;
        }
        checkoutNotification.setResult(processedOrder);
        return checkoutNotification;
    }

    public void process(Order order, User cashier) {
        try {
            Notification<Integer> bonusNotification = new Notification<>();
            orderRepository.deleteOrder(order.getOrderId());
            if (order.getOwnerId() != 1) {
                Cart userCart = cartRepository.findUserCart(order.getOwnerId());
                cartRepository.deleteItemsFromCart(userCart.getCartId());
            }
            Long totalCost = order.orderCost();
            Long numberOfItems = order.orderNumberOfItems();
            bonusNotification = statisticsRepository.findBonusPercentage();
            int bonusPercentage = bonusNotification.getResult();
            int userBonus = (int) (totalCost * bonusPercentage) / 100;
            Long userUpdatedBonus;
            if (userRepository.getUserPoints(order.getOwnerId()) >= totalCost) {
                userUpdatedBonus = userRepository.getUserPoints(order.getOwnerId()) - totalCost + userBonus;
            } else
                userUpdatedBonus = userRepository.getUserPoints(order.getOwnerId()) + userBonus;
            userRepository.updateUserPoints(order.getOwnerId(), userUpdatedBonus);
            Date now = new Date(System.currentTimeMillis());
            orderRepository.addProcessedOrder(order.getOrderId(), order.getOwnerId(), cashier.getId(), numberOfItems, totalCost, now);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Notification<Cart> createCart(Cart cart) {
        Notification<Cart> createCartNotification = new Notification<>();
        Cart createdCart;
        try {
            createdCart = cartRepository.create(cart);
        } catch (SQLException e) {
            createCartNotification.addError("There was something wrong with the database!");
            return createCartNotification;
        }
        createCartNotification.setResult(createdCart);
        return createCartNotification;
    }

    public Notification<Cart> retrieveCart(Long id) {
        Notification<Cart> retrieveCartNotification = new Notification<>();
        Cart userCart;
        List<Item> cartItems;
        try {
            userCart = cartRepository.findUserCart(id);
        } catch (SQLException e) {
            retrieveCartNotification.addError("There was something wrong with the database!");
            return retrieveCartNotification;
        }
        retrieveCartNotification.setResult(userCart);
        return retrieveCartNotification;
    }

    public Notification<Cart> addToCart(Long cartId, Long itemId) throws SQLException {
        Notification<Cart> addCartNotification = new Notification<>();
        try {
            cartRepository.addItemToCart(cartId, itemId);
        } catch (SQLException e) {
            addCartNotification.addError("There was something wrong with the database!");
            return addCartNotification;
        }
        return addCartNotification;
    }

    public Notification<List<Order>> retrieveOrders() {
        Notification<List<Order>> retrieveOrdersNotification = new Notification<>();
        List<Order> allOrders;
        try {
            allOrders = orderRepository.findAllOrders();
        } catch (SQLException e) {
            retrieveOrdersNotification.addError("There was something wrong with the database!");
            return retrieveOrdersNotification;
        }
        retrieveOrdersNotification.setResult(allOrders);
        return retrieveOrdersNotification;
    }

    public Notification<Integer> updateBonusPercentage(int newPercentage) {
        Notification<Integer> updateBonusPercentageNotification = new Notification<>();
        try {
            updateBonusPercentageNotification = statisticsRepository.updateBonusPercentage(newPercentage);
        } catch (SQLException e) {
            updateBonusPercentageNotification.addError("There was something wrong with the database!");
            return updateBonusPercentageNotification;
        }
        updateBonusPercentageNotification.setResult(updateBonusPercentageNotification.getResult());
        return updateBonusPercentageNotification;
    }

    public Notification<Statistics> findCashierStatistics(Long id, int period) {
        Notification<Statistics> resultNotification = new Notification<>();
        try {
            resultNotification = statisticsRepository.findCashierStatistics(id, period);
        } catch (SQLException e) {
            resultNotification.addError("There was something wrong with the database!");
            return resultNotification;
        }
        return resultNotification;
    }
}

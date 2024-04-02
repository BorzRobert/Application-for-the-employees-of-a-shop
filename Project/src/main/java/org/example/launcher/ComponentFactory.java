package org.example.launcher;

import org.example.controller.GuestController;
import org.example.controller.LoginController;
import org.example.database.DatabaseConnectionFactory;
import org.example.database.SupportedDatabase;
import org.example.model.item.Item;
import org.example.repository.cart.CartRepository;
import org.example.repository.cart.CartRepositorySQL;
import org.example.repository.item.ItemRepository;
import org.example.repository.item.ItemRepositorySQL;
import org.example.repository.order.OrderRepository;
import org.example.repository.order.OrderRepositorySQL;
import org.example.repository.security.*;
import org.example.service.item.ItemService;
import org.example.service.security.PaymentService;
import org.example.service.security.SecurityService;
import org.example.service.user.UserService;
import org.example.view.GuestView;
import org.example.view.LoginView;

import java.sql.Connection;

public class ComponentFactory {

    private final LoginView loginView;
    private final GuestView guestView;

    private final LoginController loginController;
    private final GuestController guestController;

    private final SecurityService securityService;
    private final PaymentService paymentService;
    private final ItemService itemService;

    private final UserService userService;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    private final StatisticsRepository statisticsRepository;


    public ComponentFactory(SupportedDatabase supportedDatabase, Boolean componentsForTest) {
        final Connection connection = DatabaseConnectionFactory.getConnectionWrapper(supportedDatabase, componentsForTest).getConnection();
        this.roleRepository = new RoleRepositorySQL(connection);
        this.userRepository = new UserRepositorySQL(connection, roleRepository);
        this.itemRepository = new ItemRepositorySQL(connection);
        this.orderRepository = new OrderRepositorySQL(connection, itemRepository);
        this.cartRepository = new CartRepositorySQL(connection, itemRepository);
        this.statisticsRepository = new StatisticsRepositorySQL(connection);
        this.securityService = new SecurityService(userRepository, roleRepository);
        this.paymentService = new PaymentService(orderRepository, itemRepository, cartRepository, statisticsRepository, userRepository, roleRepository);
        this.itemService = new ItemService(itemRepository);
        this.userService = new UserService(userRepository);
        this.loginView = new LoginView();
        this.guestView = new GuestView();
        this.loginController = new LoginController(loginView, guestView, securityService, itemService, paymentService, userService);
        this.guestController = new GuestController(guestView, itemService, paymentService);
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public GuestView getGuestView() {
        return guestView;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public GuestController getGuestController() {
        return guestController;
    }

    public SecurityService getSecurityService() {
        return securityService;
    }

    public ItemService getItemService() {
        return itemService;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ItemRepository getBookRepository() {
        return itemRepository;
    }

}

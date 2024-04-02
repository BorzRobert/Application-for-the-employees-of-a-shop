package org.example.service.security;

import org.example.database.DatabaseConnectionFactory;
import org.example.database.DbConnection;
import org.example.model.cart.Cart;
import org.example.model.cart.CartBuilder;
import org.example.model.item.Item;
import org.example.model.item.ItemBuilder;
import org.example.model.order.Order;
import org.example.model.order.OrderBuilder;
import org.example.model.security.ERole;
import org.example.model.security.Role;
import org.example.model.security.User;
import org.example.model.security.UserBuilder;
import org.example.model.validation.Notification;
import org.example.repository.cart.CartRepository;
import org.example.repository.cart.CartRepositorySQL;
import org.example.repository.item.ItemRepository;
import org.example.repository.item.ItemRepositorySQL;
import org.example.repository.order.OrderRepository;
import org.example.repository.order.OrderRepositorySQL;
import org.example.repository.security.*;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.SupportedDatabase.MYSQL;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentServiceTest {

    private PaymentService paymentService;

    private OrderRepository orderRepository;
    private ItemRepository itemRepository;

    private CartRepository cartRepository;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private StatisticsRepository statisticsRepository;

    @BeforeEach
    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
        itemRepository.removeAll();
    }

    @BeforeAll
    public void setup() {
        DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(MYSQL, true);
        this.statisticsRepository = new StatisticsRepositorySQL(connectionWrapper.getConnection());
        this.roleRepository = new RoleRepositorySQL(connectionWrapper.getConnection());
        this.userRepository = new UserRepositorySQL(connectionWrapper.getConnection(), roleRepository);
        this.itemRepository = new ItemRepositorySQL(connectionWrapper.getConnection());
        this.cartRepository = new CartRepositorySQL(connectionWrapper.getConnection(), itemRepository);
        this.orderRepository = new OrderRepositorySQL(connectionWrapper.getConnection(), itemRepository);
        this.paymentService = new PaymentService(orderRepository, itemRepository, cartRepository, statisticsRepository, userRepository, roleRepository);
    }

    @Test
    void checkout() throws SQLException {
        Order invalidOrder = new OrderBuilder().setOrderId(1L).setOwnerId(-1L).setItems(new ArrayList<>()).build();
        Notification<Order> notCreatedOrder = paymentService.checkout(invalidOrder);

        assertTrue(notCreatedOrder.hasErrors());

        Order validOrder = new OrderBuilder().setOrderId(1L).setOwnerId(createValidUser().getId()).setItems(createValidItemList()).build();
        Notification<Order> createdOrder = paymentService.checkout(validOrder);

        assertNotNull(createdOrder.getResult());
    }

    @Test
    void createCart() throws SQLException {
        Cart cart = new CartBuilder().setCartId(1L).setOwnerId(-1L).build();
        Notification<Cart> cartNotification = paymentService.createCart(cart);

        assertTrue(cartNotification.hasErrors());


        User user = createValidUser();
        cart = new CartBuilder().setCartId(1L).setOwnerId(user.getId()).build();
        cartNotification = paymentService.createCart(cart);

        assertNotNull(cartNotification.getResult());
    }

    @Test
    void retrieveCart() throws SQLException {
        Cart cart = new CartBuilder().setCartId(1L).setOwnerId(-1L).build();
        Notification<Cart> cartNotification = paymentService.retrieveCart(cart.getOwnerId());

        assertTrue(cartNotification.hasErrors());

        User user = createValidUser();
        cart = new CartBuilder().setCartId(1L).setOwnerId(user.getId()).build();
        paymentService.createCart(cart);
        cartNotification = paymentService.retrieveCart(cart.getOwnerId());

        assertNotNull(cartNotification.getResult());
    }

    @Test
    void addToCart() throws SQLException {
        List<Item> validItems = createValidItemList();
        User user = createValidUser();
        Cart cart = new CartBuilder().setCartId(1L).setOwnerId(user.getId()).build();
        cart = paymentService.createCart(cart).getResult();
        Notification<Cart> cartNotification = paymentService.addToCart(cart.getCartId(), validItems.get(0).getId());

        assertFalse(cartNotification.hasErrors());
    }

    @Test
    void retrieveOrders() throws SQLException {
        Order validOrder = new OrderBuilder().setOrderId(1L).setOwnerId(createValidUser().getId()).setItems(createValidItemList()).build();
        Order createdOrder = orderRepository.create(validOrder);

        Notification<List<Order>> ordersNotification = paymentService.retrieveOrders();

        assertNotNull(ordersNotification.getResult());
    }

    @Test
    void updateBonusPercentage() {
        Notification<Integer> updateNotification = paymentService.updateBonusPercentage(10);

        assertNull(updateNotification.getResult());
    }

    private User createValidUser() throws SQLException {
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(new Role(1L, ERole.FIDELITY_USER.toString()));
        User user = new UserBuilder()
                .setId(2L)
                .setPassword("parolamea@1")
                .setAddress("Strada Morii")
                .setPoints(200)
                .setUsername("user@yahoo.com")
                .setRoles(userRoles)
                .build();
        return userRepository.create(user);
    }

    private List<Item> createValidItemList() throws SQLException {
        Item item1 = new ItemBuilder().setName("Saxophone").setPrice(175L).setManufacturer("SaxMax").build();
        Item item2 = new ItemBuilder().setName("Keyboard").setPrice(350L).setManufacturer("Yamaha").build();
        List<Item> items = new ArrayList<>();
        items.add(itemRepository.create(item1));
        items.add(itemRepository.create(item2));

        return items;
    }
}
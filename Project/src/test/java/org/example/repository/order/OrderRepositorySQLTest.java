package org.example.repository.order;

import org.example.database.DatabaseConnectionFactory;
import org.example.database.DbConnection;
import org.example.model.item.Item;
import org.example.model.item.ItemBuilder;
import org.example.model.order.Order;
import org.example.model.order.OrderBuilder;
import org.example.model.security.ERole;
import org.example.model.security.Role;
import org.example.model.security.User;
import org.example.model.security.UserBuilder;
import org.example.repository.item.ItemRepository;
import org.example.repository.item.ItemRepositorySQL;
import org.example.repository.security.RoleRepository;
import org.example.repository.security.RoleRepositorySQL;
import org.example.repository.security.UserRepository;
import org.example.repository.security.UserRepositorySQL;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.SupportedDatabase.MYSQL;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderRepositorySQLTest {

    private OrderRepository repository;

    private ItemRepository itemRepository;

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    @BeforeAll
    public void setupClass() {
        DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(MYSQL, true);
        this.itemRepository = new ItemRepositorySQL(connectionWrapper.getConnection());
        this.repository = new OrderRepositorySQL(connectionWrapper.getConnection(), itemRepository);
        this.roleRepository = new RoleRepositorySQL(connectionWrapper.getConnection());
        this.userRepository = new UserRepositorySQL(connectionWrapper.getConnection(), roleRepository);
    }

    @BeforeEach
    @AfterEach
    void setup() {
        userRepository.deleteAll();
        repository.deleteAll();
        itemRepository.removeAll();
        initializeRoles();
    }

    @Test
    void create() throws SQLException {
        Order invalidOrder = new OrderBuilder().setOrderId(1L).setOwnerId(-1L).setItems(new ArrayList<>()).build();

        assertThrows(SQLException.class, () -> repository.create(invalidOrder));

        Order validOrder = new OrderBuilder().setOrderId(1L).setOwnerId(createValidUser().getId()).setItems(createValidItemList()).build();
        Order createdOrder = repository.create(validOrder);

        assertNotNull(createdOrder);
    }

    @Test
    void findAllOrders() throws SQLException {
        assertEquals(0, repository.findAllOrders().size());
    }

    @Test
    void getItemsFromOrder() throws SQLException {

        Order invalidOrder = new OrderBuilder().setOrderId(1L).setOwnerId(-1L).setItems(new ArrayList<>()).build();

        assertEquals(0, repository.getItemsFromOrder(invalidOrder.getOrderId()).size());

        Order validOrder = repository.create(new OrderBuilder().setOrderId(1L).setOwnerId(createValidUser().getId()).setItems(createValidItemList()).build());

        assertNotNull(repository.getItemsFromOrder(validOrder.getOrderId()));

    }

    @Test
    void deleteOrder() throws SQLException {

        Order validOrder = new OrderBuilder().setOrderId(1L).setOwnerId(createValidUser().getId()).setItems(createValidItemList()).build();
        Order createdOrder = repository.create(validOrder);
        repository.deleteOrder(createdOrder.getOrderId());

        assertEquals(0, repository.findAllOrders().size());
    }

    @Test
    void deleteAll() throws SQLException {
        Order validOrder = repository.create(new OrderBuilder().setOrderId(1L).setOwnerId(createValidUser().getId()).setItems(createValidItemList()).build());
        repository.deleteAll();

        assertEquals(0, repository.findAllOrders().size());
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

    private  void initializeRoles(){
        for (ERole role : ERole.values()) {
            roleRepository.create(role);
        }
    }
}
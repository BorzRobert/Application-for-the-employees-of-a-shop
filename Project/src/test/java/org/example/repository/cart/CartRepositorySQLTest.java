package org.example.repository.cart;

import org.example.database.DatabaseConnectionFactory;
import org.example.database.DbConnection;
import org.example.model.cart.Cart;
import org.example.model.cart.CartBuilder;
import org.example.model.item.Item;
import org.example.model.item.ItemBuilder;
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
class CartRepositorySQLTest {

    private CartRepository repository;

    private UserRepository userRepository;

    private RoleRepository roleRepository;
    private ItemRepository itemRepository;

    @BeforeAll
    public void setupClass() {
        DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(MYSQL, true);
        this.itemRepository = new ItemRepositorySQL(connectionWrapper.getConnection());
        repository = new CartRepositorySQL(connectionWrapper.getConnection(), itemRepository);
        this.roleRepository = new RoleRepositorySQL(connectionWrapper.getConnection());
        this.userRepository = new UserRepositorySQL(connectionWrapper.getConnection(), roleRepository);
    }

    @BeforeEach
    @AfterEach
    void setup() {
        itemRepository.removeAll();
        userRepository.deleteAll();
        repository.deleteAll();
        initializeRoles();
    }

    @Test
    void findAll() throws SQLException {
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void create() throws SQLException {
        //Invalid cart
        Cart cartNoIdAndNoItems = new CartBuilder()
                .setCartId(1L)
                .build();

        assertThrows(NullPointerException.class, () -> repository.create(cartNoIdAndNoItems));

        //Valid cart
        User createdUser = createValidUser();
        Cart cart = new CartBuilder().setOwnerId(createdUser.getId()).build();

        assertNotNull(repository.create(cart));
    }

    @Test
    void findUserCart() throws SQLException {

        assertThrows(SQLException.class, () -> repository.findUserCart(1L));

        assertNotNull(repository.findUserCart(createValidCartWithOneItem().getOwnerId()));

    }

    @Test
    void getItemsFromCart() throws SQLException {
        List<Long> indexList = new ArrayList<>();
        indexList.add(1L);

        assertThrows(SQLException.class, () -> repository.getItemsFromCart(indexList));

        Cart createdCart = createValidCartWithOneItem();
        List<Item> itemsFromCart = repository.getItemsFromCart(repository.getItemsIdsFromCart(createdCart.getCartId()));

        assertNotNull(itemsFromCart);

    }

    @Test
    void getItemsIdsFromCart() throws SQLException {
        Cart createdCart = createValidCartWithOneItem();
        assertNotNull(repository.getItemsIdsFromCart(createdCart.getCartId()));
    }

    @Test
    void deleteAll() throws SQLException {
        repository.create(createValidCartEmpty());
        repository.deleteAll();

        assertEquals(0, repository.findAll().size());
    }

    private Cart createValidCartWithOneItem() throws SQLException {
        Item item = new ItemBuilder().setName("Saxophone").setPrice(175L).setManufacturer("SaxMax").build();
        List<Item> items = new ArrayList<>();
        items.add(item);
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
        Item createdItem = itemRepository.create(item);
        User createdUser = userRepository.create(user);
        Cart cart = new CartBuilder().setOwnerId(createdUser.getId()).setCartId(1L).build();
        Cart createdCart = repository.create(cart);
        repository.addItemToCart(createdCart.getCartId(), createdItem.getId());
        createdCart.setItems(items);

        return createdCart;
    }

    private Cart createValidCartEmpty() throws SQLException {
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
       User createdUser = userRepository.create(user);
       Cart createdCart = repository.create(new CartBuilder().setOwnerId(createdUser.getId()).build());
       return createdCart;
    }

    private User createValidUser() throws SQLException {
        Item item = new ItemBuilder().setName("Saxophone").setPrice(175L).setManufacturer("SaxMax").build();
        List<Item> items = new ArrayList<>();
        items.add(item);
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
        Item createdItem = itemRepository.create(item);
        User createdUser = userRepository.create(user);

        return createdUser;
    }

    private  void initializeRoles(){
        for (ERole role : ERole.values()) {
            roleRepository.create(role);
        }
    }
}
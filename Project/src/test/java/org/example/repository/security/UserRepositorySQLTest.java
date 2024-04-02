package org.example.repository.security;

import org.example.database.DatabaseConnectionFactory;
import org.example.database.DbConnection;
import org.example.model.item.Item;
import org.example.model.item.ItemBuilder;
import org.example.model.security.ERole;
import org.example.model.security.Role;
import org.example.model.security.User;
import org.example.model.security.UserBuilder;
import org.example.model.validation.Notification;
import org.example.repository.item.ItemRepository;
import org.example.repository.item.ItemRepositorySQL;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.SupportedDatabase.MYSQL;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositorySQLTest {

    private UserRepository repository;

    private RoleRepository roleRepository;

    private ItemRepository itemRepository;

    private UserRepository userRepository;

    @BeforeAll
    public void setupClass() {
        DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(MYSQL, true);
        this.roleRepository = new RoleRepositorySQL(connectionWrapper.getConnection());
        this.repository = new UserRepositorySQL(connectionWrapper.getConnection(), roleRepository);
        this.itemRepository = new ItemRepositorySQL(connectionWrapper.getConnection());
        this.userRepository = new UserRepositorySQL(connectionWrapper.getConnection(), roleRepository);
    }

    @BeforeEach
    @AfterEach
    void setup() {
        repository.deleteAll();
        initializeRoles();
    }

    @Test
    void findAll() {
        repository.deleteAll();
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void findById() throws SQLException {

        Notification<User> userNotification = repository.findById(-1L);

        assertEquals(userNotification.getFormattedErrors(), "The user does not exist!");

        User user = createValidUser();
        userNotification = repository.findById(user.getId());

        assertNotNull(userNotification.getResult());
    }

    @Test
    void findByUsernameAndPassword() throws SQLException {
        Notification<User> userNotification = repository.findByUsernameAndPassword("nuexista", "sigurnuexista");

        assertEquals(userNotification.getFormattedErrors(), "Invalid username or password.");

        User user = createValidUser();
        userNotification = repository.findByUsernameAndPassword(user.getUsername(), user.getPassword());

        assertNotNull(userNotification.getResult());
    }

    @Test
    void create() throws SQLException {

        User invalidUser = new UserBuilder().setId(1L).build();

        assertThrows(SQLException.class, () -> repository.create(invalidUser));

        User user = createValidUser();

        assertNotNull(user);
    }

    @Test
    void updateUser() throws SQLException {
        User validUser = createValidUser();

        assertNull(repository.updateUser(validUser));
    }

    @Test
    void getUserPoints() throws SQLException {

        assertEquals(-1, repository.getUserPoints(-1L));

        User validUser = createValidUser();

        assertNotNull(repository.getUserPoints(validUser.getId()));
    }

    @Test
    void deleteAll() {
        repository.deleteAll();
        assertEquals(0, repository.findAll().size());
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
        user.setPassword(encodePassword(user.getPassword()));
        User createdUser = userRepository.create(user);

        return createdUser;
    }

    private void initializeRoles() {
        for (ERole role : ERole.values()) {
            roleRepository.create(role);
        }
    }

    private String encodePassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
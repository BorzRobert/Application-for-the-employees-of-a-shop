package org.example.repository.security;

import org.example.database.DatabaseConnectionFactory;
import org.example.database.DbConnection;
import org.example.model.security.ERole;
import org.example.model.security.Role;
import org.example.model.security.User;
import org.example.model.security.UserBuilder;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.SupportedDatabase.MYSQL;
import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleRepositorySQLTest {

    private RoleRepository repository;

    private UserRepository userRepository;

    @BeforeAll
    public void setupClass() {
        DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(MYSQL, true);
        this.repository = new RoleRepositorySQL(connectionWrapper.getConnection());
        this.userRepository = new UserRepositorySQL(connectionWrapper.getConnection(), repository);
    }

    @BeforeEach
    @AfterEach
    void setup() {
        repository.removeAll();
        userRepository.deleteAll();
    }

    @Test
    void create() {
        ERole role = ERole.ADMINISTRATOR;
        repository.create(role);

        assertNotNull(repository.findRoleByTitle(role));
    }


    @Test
    void findAll() {
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void findRoleByTitle() {
        ERole role = ERole.ADMINISTRATOR;
        repository.create(role);

        assertNotNull(repository.findRoleByTitle(role));
    }

    @Test
    void findRoleById() {
        ERole role = ERole.ADMINISTRATOR;
        repository.create(role);

        assertNotNull(repository.findRoleById(repository.findAll().get(0).getId()));
    }

    @Test
    void findRolesForUser() throws SQLException {
        repository.create(ERole.FIDELITY_USER);
        User user = createValidUser();

        assertNotNull(repository.findRolesForUser(user.getId()));
    }

    @Test
    void addRolesToUser() throws SQLException {
        repository.create(ERole.FIDELITY_USER);
        User user = createValidUser();

        assertNotNull(user.getRoles());
    }

    @Test
    void removeAll() {
        ERole role = ERole.ADMINISTRATOR;
        repository.create(role);
        repository.removeAll();

        assertEquals(0, repository.findAll().size());
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
}
package org.example.service.security;

import org.example.database.DatabaseConnectionFactory;
import org.example.database.DbConnection;
import org.example.model.security.User;
import org.example.model.validation.Notification;
import org.example.repository.security.RoleRepository;
import org.example.repository.security.RoleRepositorySQL;
import org.example.repository.security.UserRepository;
import org.example.repository.security.UserRepositorySQL;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.example.database.SupportedDatabase.MYSQL;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecurityServiceTest {

  private SecurityService securityService;
  private UserRepository userRepository;

  @BeforeEach
  @AfterEach
  public void cleanup() {
    userRepository.deleteAll();
  }

  @BeforeAll
  public void setup() {
    DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(MYSQL, true);
    Connection connection = connectionWrapper.getConnection();

    RoleRepository roleRepositorySQL = new RoleRepositorySQL(connection);
    userRepository = new UserRepositorySQL(connection, roleRepositorySQL);
    securityService = new SecurityService(userRepository, roleRepositorySQL);
  }

  @Test
  void register() {
    Notification<User> user1 = securityService.register("johndoe@yahoo.com", "parolamea@1", "Strada Strazii", false);
    User result = user1.getResult();

    assertNotNull(result);

    user1 = securityService.register("johndoe@###yahoo.com", "p", "", false);

    assertTrue(user1.hasErrors());
  }

  @Test
  void login() {
    Notification<User> user2 = securityService.login("johndoe@yahoo.com", "parolamea@1");

    assertTrue(user2.hasErrors());

    Notification<User> user1 = securityService.register("johndoe@yahoo.com", "parolamea@1", "Strada Strazii", false);
    user2 = securityService.login("johndoe@yahoo.com", "parolamea@1");

    assertNotNull(user2.getResult());
  }
}
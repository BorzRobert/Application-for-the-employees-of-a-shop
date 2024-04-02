package org.example.repository.item;

import org.example.database.DatabaseConnectionFactory;
import org.example.database.DbConnection;
import org.example.model.item.Item;
import org.example.model.item.ItemBuilder;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.example.database.SupportedDatabase.MYSQL;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemRepositorySQLTest {

  private ItemRepository repository;

  @BeforeAll
  public void setupClass() {
    DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(MYSQL, true);
    repository = new ItemRepositorySQL(connectionWrapper.getConnection());
  }

  @BeforeEach
  @AfterEach
  void setup() {
    repository.removeAll();
  }

  @Test
  void findAll() {
    assertEquals(0, repository.findAll().size());
  }

  @Test
  void create() throws SQLException {
    Item itemNoManufacturer = new ItemBuilder()
        .setName("Saxophone")
        .setPrice(175L)
        .build();

    assertThrows(SQLException.class, () -> repository.create(itemNoManufacturer));

    Item itemNoName = new ItemBuilder().setManufacturer("SaxMax")
        .setPrice(175L)
        .build();

    assertThrows(SQLException.class, () -> repository.create(itemNoName));


    Item validItem = new ItemBuilder()
        .setManufacturer("SaxMax")
        .setName("Saxophone")
        .setPrice(175L)
        .build();

    assertNotNull(repository.create(validItem));
  }

  @Test
  void removeAll() throws SQLException {
    repository.create(new ItemBuilder()
        .setManufacturer("MaxSax")
        .setName("Saxophone")
        .setPrice(175L)
        .build());

    repository.removeAll();

    assertEquals(0, repository.findAll().size());
  }
}
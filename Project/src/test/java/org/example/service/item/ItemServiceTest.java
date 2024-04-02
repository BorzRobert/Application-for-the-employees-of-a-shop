package org.example.service.item;

import org.example.database.DatabaseConnectionFactory;
import org.example.database.SupportedDatabase;
import org.example.repository.item.ItemRepository;
import org.example.repository.item.ItemRepositorySQL;
import org.example.service.item.ItemService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServiceTest {

  private ItemService itemService;
  private ItemRepository itemRepository;

  @BeforeEach
  @AfterEach
  public void cleanup() {
    itemRepository.removeAll();
  }

  @BeforeAll
  public void setup() {
    itemRepository = new ItemRepositorySQL(DatabaseConnectionFactory.getConnectionWrapper(SupportedDatabase.MYSQL, true).getConnection());
    itemService = new ItemService(itemRepository);
  }

  @Test
  void findAll() {
    assertEquals(0, itemService.findAll().size());
  }
}
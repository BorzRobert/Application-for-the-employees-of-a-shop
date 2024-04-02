package org.example;

import org.example.database.Bootstrap;
import org.example.database.DatabaseConnectionFactory;
import org.example.database.DbConnection;
import org.example.model.item.Item;
import org.example.model.item.ItemBuilder;
import org.example.repository.item.ItemRepository;
import org.example.repository.item.ItemRepositorySQL;

import java.sql.SQLException;

import static org.example.database.SupportedDatabase.MYSQL;

public class Main {
    public static void main(String[] args) throws SQLException {
        new Bootstrap().bootstrap();

        DbConnection connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(MYSQL, true);

        if (connectionWrapper.testConnection()) {
            System.out.println("Connection successful");
        } else {
            System.out.println("Connection failed");
        }
        Item item = new ItemBuilder().setManufacturer("SaxMax").setName("Saxophone").setPrice(175L).build();

        ItemRepositorySQL mysqlRepository = new ItemRepositorySQL(connectionWrapper.getConnection());
    }

}
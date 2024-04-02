package org.example.database;

import static org.example.database.Constants.TABLES.*;

public class SQLTableCreationFactory {

    public String getCreateSQLForTable(String table) {
        return switch (table) {
            case ITEM -> "CREATE TABLE IF NOT EXISTS item (" +
                    "  id int(11) NOT NULL AUTO_INCREMENT," +
                    "  manufacturer varchar(500) NOT NULL," +
                    "  name varchar(500) NOT NULL," +
                    "  price int(11) DEFAULT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE KEY id_UNIQUE (id)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
            case USER -> "CREATE TABLE IF NOT EXISTS user (" +
                    "  id INT NOT NULL AUTO_INCREMENT," +
                    "  username VARCHAR(200) NOT NULL," +
                    "  password_ VARCHAR(64) NOT NULL," +
                    "  address VARCHAR(100) NOT NULL," +
                    "  points INT NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  UNIQUE INDEX username_UNIQUE (username ASC));";
            case ROLE -> "  CREATE TABLE IF NOT EXISTS role (" +
                    "  id INT NOT NULL AUTO_INCREMENT," +
                    "  role VARCHAR(100) NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  UNIQUE INDEX role_UNIQUE (role ASC));";
            case USER_ROLE -> "\tCREATE TABLE IF NOT EXISTS user_role (" +
                    "  id INT NOT NULL AUTO_INCREMENT," +
                    "  user_id INT NOT NULL," +
                    "  role_id INT NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  INDEX user_id_idx (user_id ASC)," +
                    "  INDEX role_id_idx (role_id ASC)," +
                    "  CONSTRAINT user_fkid" +
                    "    FOREIGN KEY (user_id)" +
                    "    REFERENCES user (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE," +
                    "  CONSTRAINT role_fkid" +
                    "    FOREIGN KEY (role_id)" +
                    "    REFERENCES role (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE);";
            case ORDER -> "\tCREATE TABLE IF NOT EXISTS order_ (" +
                    "  id INT NOT NULL AUTO_INCREMENT," +
                    "  user_id INT NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  INDEX user_id_idx (user_id ASC)," +
                    "  CONSTRAINT user_fkid1" +
                    "    FOREIGN KEY (user_id)" +
                    "    REFERENCES user (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE)";
            case ORDER_ITEM -> "\tCREATE TABLE IF NOT EXISTS order_item (" +
                    "  id INT NOT NULL AUTO_INCREMENT," +
                    "  order_id INT NOT NULL," +
                    "  item_id INT NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  INDEX user_id_idx (order_id ASC)," +
                    "  INDEX role_id_idx (item_id ASC)," +
                    "  CONSTRAINT user_fkid3" +
                    "    FOREIGN KEY (order_id)" +
                    "    REFERENCES order_ (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE," +
                    "  CONSTRAINT role_fkid4" +
                    "    FOREIGN KEY (item_id)" +
                    "    REFERENCES item (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE);";
            case CART -> "\tCREATE TABLE IF NOT EXISTS cart (" +
                    "  id INT NOT NULL AUTO_INCREMENT," +
                    "  user_id INT NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  INDEX user_id_idx (user_id ASC)," +
                    "  CONSTRAINT user_fkid5" +
                    "    FOREIGN KEY (user_id)" +
                    "    REFERENCES user (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE)";
            case CART_ITEM -> "\tCREATE TABLE IF NOT EXISTS cart_item (" +
                    "  id INT NOT NULL AUTO_INCREMENT," +
                    "  cart_id INT NOT NULL," +
                    "  item_id INT NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  INDEX user_id_idx (cart_id ASC)," +
                    "  INDEX role_id_idx (item_id ASC)," +
                    "  CONSTRAINT user_fkid6" +
                    "    FOREIGN KEY (cart_id)" +
                    "    REFERENCES cart (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE," +
                    "  CONSTRAINT role_fkid7" +
                    "    FOREIGN KEY (item_id)" +
                    "    REFERENCES item (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE);";
            case PROCESSED_ORDERS -> "\tCREATE TABLE IF NOT EXISTS processed_orders (" +
                    "  id INT NOT NULL AUTO_INCREMENT," +
                    "  order_id INT NOT NULL," +
                    "  user_id INT NOT NULL," +
                    "  cashier_id INT NOT NULL," +
                    "  nr_of_items INT NOT NULL," +
                    "  total_value INT NOT NULL," +
                    "  processing_date date NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  INDEX user_id_idx (order_id ASC)," +
                    "  INDEX role_id_idx (user_id ASC)," +
                    "  CONSTRAINT role_fkid10" +
                    "    FOREIGN KEY (cashier_id)" +
                    "    REFERENCES user (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE);";
            case BONUS -> "CREATE TABLE IF NOT EXISTS bonus (" +
                    "  id int(11) NOT NULL AUTO_INCREMENT," +
                    "  percentage int(11) NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE KEY id_UNIQUE (id)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";

            default -> "";
        };
    }

}

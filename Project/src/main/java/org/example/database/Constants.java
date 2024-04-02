package org.example.database;

public class Constants {

    public static class SCHEMAS {
        public static final String TEST = "sd-basics-test";
        public static final String PRODUCTION = "sd-basics";

        public static final String[] SCHEMAS = new String[]{TEST, PRODUCTION};
    }

    public static class TABLES {
        public static final String ITEM = "item";
        public static final String USER = "user";
        public static final String ROLE = "role";
        public static final String USER_ROLE = "user_role";
        public static final String ORDER = "order_";
        public static final String ORDER_ITEM = "order_item";
        public static final String CART = "cart";
        public static final String CART_ITEM = "cart_item";
        public static final String PROCESSED_ORDERS = "processed_orders";
        public static final String BONUS = "bonus";

        public static final String[] ORDERED_TABLES_FOR_CREATION = {ITEM, USER, ROLE, USER_ROLE, ORDER, ORDER_ITEM, CART, CART_ITEM, PROCESSED_ORDERS, BONUS};
    }

}

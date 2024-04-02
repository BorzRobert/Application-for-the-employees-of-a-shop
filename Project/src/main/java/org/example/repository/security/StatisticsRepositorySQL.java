package org.example.repository.security;

import org.example.model.Statistics.Statistics;
import org.example.model.security.User;
import org.example.model.validation.Notification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.database.Constants.TABLES.BONUS;
import static org.example.database.Constants.TABLES.USER;

public class StatisticsRepositorySQL implements StatisticsRepository {

    private final Connection connection;

    public StatisticsRepositorySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Notification<Integer> findBonusPercentage() throws SQLException {
        Notification<Integer> resultNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * from " + BONUS;
            ResultSet bonusResultSet = statement.executeQuery(sql);
            bonusResultSet.next();
            resultNotification.setResult(bonusResultSet.getInt("percentage"));
            return resultNotification;

        } catch (SQLException e) {
            e.printStackTrace();
            resultNotification.addError("Something is wrong with the database.");
        }
        return resultNotification;
    }

    @Override
    public Notification<Integer> updateBonusPercentage(int newPercentage) throws SQLException {
        Notification<Integer> resultNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "UPDATE " + BONUS + " SET percentage = " + newPercentage;
            statement.executeUpdate(sql);
            return resultNotification;
        } catch (SQLException e) {
            e.printStackTrace();
            resultNotification.addError("Something is wrong with the database.");
        }
        return resultNotification;
    }

    public Notification<Statistics> findCashierStatistics(Long id, int period) throws SQLException {
        Notification<Statistics> resultNotification = new Notification<>();
        Statistics statistics = new Statistics(0, 0, 0, period);
        try {
            Statement statement = connection.createStatement();
            String itemsSoldQuery = "SELECT SUM(nr_of_items) FROM processed_orders WHERE cashier_id =" + id + " AND MONTH(processing_date)=" + period + ";";
            ResultSet statisticsResultSet = statement.executeQuery(itemsSoldQuery);
            statisticsResultSet.next();
            statistics.setNumberOfItems(statisticsResultSet.getInt(1));

            String revenueQuery = "SELECT SUM(total_value) FROM processed_orders WHERE cashier_id =" + id + " AND MONTH(processing_date) =" + period + ";";
            statisticsResultSet = statement.executeQuery(revenueQuery);
            statisticsResultSet.next();
            statistics.setTotalValue(statisticsResultSet.getInt(1));

            String uniqueCustomersQuery = "SELECT COUNT(DISTINCT user_id) FROM processed_orders WHERE cashier_id =" + id + " AND MONTH(processing_date) =" + period + ";";
            statisticsResultSet = statement.executeQuery(uniqueCustomersQuery);
            statisticsResultSet.next();
            statistics.setUniqueCustomers(statisticsResultSet.getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();
            resultNotification.addError("Something is wrong with the database.");
        }
        resultNotification.setResult(statistics);
        return resultNotification;
    }
}

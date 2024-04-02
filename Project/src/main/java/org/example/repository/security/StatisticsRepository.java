package org.example.repository.security;

import org.example.model.Statistics.Statistics;
import org.example.model.security.User;
import org.example.model.validation.Notification;

import java.sql.SQLException;

public interface StatisticsRepository {

    Notification<Integer> findBonusPercentage() throws SQLException;

    Notification<Integer> updateBonusPercentage(int newPercentage) throws SQLException;

    Notification<Statistics> findCashierStatistics(Long id, int period) throws SQLException;


}

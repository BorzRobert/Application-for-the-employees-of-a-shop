package org.example.repository.security;

import org.example.model.item.Item;
import org.example.model.security.User;
import org.example.model.validation.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserRepository {

    public List<User> findAll();

    public Notification<User> findById(Long id);

    Notification<User> findByUsernameAndPassword(String username, String password);

    User create(User user) throws SQLException;

    public void updateUserPoints(Long userId, Long points) throws SQLException;

    public User updateUser(User user) throws SQLException;

    public Long getUserPoints(Long userId) throws SQLException;

    public void deleteById(Long id);

    void deleteAll();
}

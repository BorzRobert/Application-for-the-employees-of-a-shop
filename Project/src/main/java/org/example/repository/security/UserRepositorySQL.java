package org.example.repository.security;

import org.example.model.item.Item;
import org.example.model.item.ItemBuilder;
import org.example.model.security.User;
import org.example.model.security.UserBuilder;
import org.example.model.validation.Notification;
import org.example.model.validation.UserValidator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.Constants.TABLES.USER;

public class UserRepositorySQL implements UserRepository {

    private final Connection connection;
    private final RoleRepository roleRepository;

    public UserRepositorySQL(Connection connection, RoleRepository roleRepository) {
        this.connection = connection;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> findAll() {
        final String sql = "Select * from " + USER;

        List<User> users = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    private User getUserFromResultSet(ResultSet rs) throws SQLException {
        return new UserBuilder()
                .setId(rs.getLong("id"))
                .setAddress(rs.getString("address"))
                .setPassword(rs.getString("password_"))
                .setPoints(rs.getInt("points"))
                .setUsername(rs.getString("username"))
                .setRoles(roleRepository.findRolesForUser(rs.getLong("id")))
                .build();
    }

    public Notification<User> findById(Long id){
        Notification<User> resultNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql =
                    "Select * from " + USER + " where id=" + id;
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            if (userResultSet.next()) {
                User user = new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password_"))
                        .setAddress(userResultSet.getString("address"))
                        .setPoints(userResultSet.getInt("points"))
                        .setRoles(roleRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
                resultNotification.setResult(user);
                return resultNotification;
            } else {
                resultNotification.addError("The user does not exist!");
            }
        } catch (SQLException e) {
            System.out.println(e);
            resultNotification.addError("Something is wrong with the database.");
        }
        return resultNotification;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        Notification<User> resultNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`=\'" + username + "\' and `password_`=\'" + password + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            if (userResultSet.next()) {
                User user = new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password_"))
                        .setAddress(userResultSet.getString("address"))
                        .setPoints(userResultSet.getInt("points"))
                        .setRoles(roleRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
                resultNotification.setResult(user);
                return resultNotification;
            } else {
                resultNotification.addError("Invalid username or password.");
            }
        } catch (SQLException e) {
            System.out.println(e);
            resultNotification.addError("Something is wrong with the database.");
        }
        return resultNotification;
    }

    @Override
    public User create(User user) throws SQLException {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.setString(3, user.getAddress());
            insertUserStatement.setInt(4, user.getPoints());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            roleRepository.addRolesToUser(user, user.getRoles());

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public void updateUserPoints(Long userId, Long points) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            String sql = "UPDATE " + USER + " SET points=" + points + " WHERE id=" + userId;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User updateUser(User user) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            String sql = "UPDATE " + USER + " SET username='" + user.getUsername() + "'," +
                    " points=" + user.getPoints() + "," +
                    " password_='" + user.getPassword() + "'," +
                    " address='" + user.getAddress() + "'" +
                    " WHERE id=" + user.getId();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getUserPoints(Long userId) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + USER + " WHERE id=" + userId;
            ResultSet userResultSet = statement.executeQuery(sql);
            userResultSet.next();
            return userResultSet.getLong("points");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    public void deleteById(Long id) {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id="+id;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

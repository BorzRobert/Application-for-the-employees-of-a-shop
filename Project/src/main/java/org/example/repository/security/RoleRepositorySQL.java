package org.example.repository.security;

import org.example.model.item.Item;
import org.example.model.security.ERole;
import org.example.model.security.Role;
import org.example.model.security.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.Constants.TABLES.ROLE;
import static org.example.database.Constants.TABLES.USER_ROLE;

public class RoleRepositorySQL implements RoleRepository {
    private final Connection connection;

    public RoleRepositorySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(ERole role) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + ROLE + " values (null, ?)");
            insertStatement.setString(1, role.toString());
            insertStatement.executeUpdate();
        } catch (SQLException e) {

        }
    }

    @Override
    public List<Role> findAll() {
        final String sql = "Select * from role";

        List<Role> roles = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                roles.add(new Role(resultSet.getLong("id"), resultSet.getString("role")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return roles;
    }

    @Override
    public Role findRoleByTitle(ERole role) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + ROLE + " where `role`=\'" + role.toString() + "\'";
            ResultSet roleResultSet = statement.executeQuery(fetchRoleSql);
            roleResultSet.next();
            Long roleId = roleResultSet.getLong("id");
            String roleTitle = roleResultSet.getString("role");
            return new Role(roleId, roleTitle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Role findRoleById(Long roleId) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + ROLE + " where `id`=\'" + roleId + "\'";
            ResultSet roleResultSet = statement.executeQuery(fetchRoleSql);
            roleResultSet.next();
            String roleTitle = roleResultSet.getString("role");
            return new Role(roleId, roleTitle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Role> findRolesForUser(long userId) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + USER_ROLE + " where `user_id`=\'" + userId + "\'";
            ResultSet roleResultSet = statement.executeQuery(fetchRoleSql);
            roleResultSet.next();
            Long roleId = roleResultSet.getLong("role_id");
            Role userRole = findRoleById(roleId);
            List<Role> returnedList = new ArrayList<Role>();
            returnedList.add(userRole);
            return returnedList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        Role role = findRoleByTitle(ERole.valueOf(roles.get(0).getRole().toString()));
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + USER_ROLE + " values (null, ?, ?)");
            insertStatement.setLong(1, user.getId());
            insertStatement.setLong(2, role.getId());
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM " + ROLE + " WHERE id >= 0";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

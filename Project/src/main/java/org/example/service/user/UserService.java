package org.example.service.user;


import org.example.model.security.User;
import org.example.model.validation.Notification;
import org.example.model.validation.UserValidator;
import org.example.repository.security.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Notification<User> findById(Long id) {
        return repository.findById(id);
    }

    public List<User> findAll() {return repository.findAll();}

    public void deleteUser(Long id){
        repository.deleteById(id);
    }

    public Notification<User> updateUser(User user){
        UserValidator validator = new UserValidator(user);
        boolean validUser = validator.validate();
        Notification<User> userUpdateNotification = new Notification<>();

        if (!validUser) {
            validator.getErrors().forEach(userUpdateNotification::addError);
        } else {
            user.setPassword(encodePassword(user.getPassword()));
            User updatedUser;
            try {
                updatedUser = repository.updateUser(user);
            } catch (SQLException e) {
                userUpdateNotification.addError("There was something wrong with the database!");
                return userUpdateNotification;
            }
            userUpdateNotification.setResult(updatedUser);
        }
        return userUpdateNotification;
    }

    private String encodePassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

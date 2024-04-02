package org.example.model.security;

import java.util.List;
import java.util.Optional;

public class UserBuilder {
  private User user;

  public UserBuilder() {
    user = new User();
  }

  public UserBuilder setId(Long id) {
    user.setId(id);
    return this;
  }
  public UserBuilder setUsername(String username) {
    user.setUsername(username);
    return this;
  }

  public UserBuilder setPassword(String password) {
    user.setPassword(password);
    return this;
  }

  public UserBuilder setAddress(String address) {
    user.setAddress(address);
    return this;
  }

  public UserBuilder setPoints(int points) {
    user.setPoints(points);
    return this;
  }

  public UserBuilder setRoles(List<Role> roles) {
    user.setRoles(roles);
    return this;
  }

  public User build() {
    return user;
  }
}

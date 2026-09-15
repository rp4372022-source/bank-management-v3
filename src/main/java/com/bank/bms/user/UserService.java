package com.bank.bms.user;

public interface UserService {
    public User createUser(User user);
    public User updateUser(Long id,User user);
    public User userLogin(String email, String password);
}

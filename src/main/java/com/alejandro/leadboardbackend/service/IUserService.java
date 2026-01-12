package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.model.User;

public interface IUserService {
    User registerUser(String username, String password);
    boolean authenticate(String username, String password);
}

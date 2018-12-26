package com.sports.cricket.service;

import com.sports.cricket.model.UserLogin;

public interface LoginService {

    UserLogin loginUser(String userName, String password);

}

package com.example.demo.service;

import java.util.List;

import com.example.demo.dtos.UserRequest;
import com.example.demo.dtos.UserResponse;

public interface UserService {

    UserResponse saveUser(UserRequest userRequest);

    UserResponse getUser();

    List<UserResponse> getAllUser();


}
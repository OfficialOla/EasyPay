package com.example.EasyPay.services;


import com.example.EasyPay.data.models.User;
import com.example.EasyPay.dtos.requests.UserRegistrationRequest;
import com.example.EasyPay.dtos.response.ApiResponse;
import com.example.EasyPay.dtos.response.UserRegistrationResponse;
import com.example.EasyPay.dtos.response.UserResponse;
import com.example.EasyPay.exceptions.EmailExistsException;
import com.example.EasyPay.exceptions.PhoneNumberExistsException;
import com.example.EasyPay.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    UserRegistrationResponse register(UserRegistrationRequest registerRequest) throws EmailExistsException, PhoneNumberExistsException;

    String closeAccount(String accountNumber);

    UserResponse findById(String id) throws UserNotFoundException;
    List<UserResponse> getAllUsers(int page, int items);
    long count();
    ApiResponse<?> deleteById(String id);
    void delete(User user);


}

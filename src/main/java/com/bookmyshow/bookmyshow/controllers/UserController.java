package com.bookmyshow.bookmyshow.controllers;

import com.bookmyshow.bookmyshow.dto.*;
import com.bookmyshow.bookmyshow.models.User;
import com.bookmyshow.bookmyshow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public SignUpResponseDto signUp(SignUpRequestDto request) {
        SignUpResponseDto response = new SignUpResponseDto();
        try {
            User user = userService.signUp(request.getEmail(), request.getPassword());
            response.setStatus(ResponseStatus.SUCCESS);
            response.setUserId(user.getId());
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
        }
        return response;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        LoginResponseDto response = new LoginResponseDto();
        try {
            User loggedInUser = userService.login(request.getEmail(), request.getPassword());
            response.setStatus(ResponseStatus.SUCCESS);
            response.setUserId(loggedInUser.getId());
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
        }
        return response;
    }
}

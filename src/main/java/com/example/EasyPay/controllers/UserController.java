package com.example.EasyPay.controllers;

import com.example.EasyPay.dtos.requests.UserRegistrationRequest;
import com.example.EasyPay.dtos.response.UserRegistrationResponse;
import com.example.EasyPay.exceptions.EasyPayException;
import com.example.EasyPay.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody UserRegistrationRequest userRegistrationRequest ){
        try{
            var response = userService.register(userRegistrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch(EasyPayException exception){
            var response = new UserRegistrationResponse();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/all/{page}/{size}")
    public ResponseEntity<?> getAllUsers(@PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));

    }


}

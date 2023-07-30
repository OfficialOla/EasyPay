package com.example.EasyPay.services;

import com.example.EasyPay.data.models.User;
import com.example.EasyPay.data.repositories.UserRepository;

import com.example.EasyPay.dtos.requests.UserRegistrationRequest;
import com.example.EasyPay.dtos.response.ApiResponse;
import com.example.EasyPay.dtos.response.UserRegistrationResponse;
import com.example.EasyPay.dtos.response.UserResponse;
import com.example.EasyPay.exceptions.EmailExistsException;
import com.example.EasyPay.exceptions.PhoneNumberExistsException;
import com.example.EasyPay.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.EasyPay.utils.AppUtils.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

     private final UserRepository userRepository;
     private final ModelMapper modelMapper;

    @Override
    public UserRegistrationResponse register(UserRegistrationRequest registerRequest) throws EmailExistsException, PhoneNumberExistsException {
        if (userEmailExist(registerRequest.getEmailAddress()))
            throw new EmailExistsException(registerRequest.getEmailAddress() + "already exist");
        if (!validatePhoneNumber(registerRequest.getPhoneNumber()))
            throw new PhoneNumberExistsException(String.format(USER_WITH_PHONE_NUMBER_ALREADY_EXIST, registerRequest.getPhoneNumber()));
        User user = new User();
        modelMapper.map(registerRequest, user);
        User savedUser = userRepository.save(user);

        return buildUserRegistrationResponse(savedUser.getId());
    }
    private UserRegistrationResponse buildUserRegistrationResponse(String id) {
        UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse();
        userRegistrationResponse.setMessage(USER_REGISTRATION_SUCCESSFUL);
        userRegistrationResponse.setId(id);
        return userRegistrationResponse;
    }

    private boolean userEmailExist(String emailAddress) {
    User foundUser =userRepository.findByEmailAddress(emailAddress);
    return foundUser != null;
    }
    public boolean validatePhoneNumber(String phoneNumber){
        for (int i = 0; i <phoneNumber.length() ; i++) {
            if (!Character.isDigit(phoneNumber.charAt(i))){
                return false;
            }
        }
        return true;
    }

//    @Override
//    public String login(LoginRequest loginRequest) throws InvalidLoginException {
//        User user = userRepository.findByEmailAddress(loginRequest.getEmailAddress());
//        if(user == null || !user.getPassword().equals(loginRequest.getPassword()) ||
//                !user.getEmailAddress().equals(loginRequest.getEmailAddress())){
//            throw new InvalidLoginException("invalid email or password");
//        }
//        else return "login successful";
//    }
    @Override
    public String closeAccount(String accountNumber) {
        User user = userRepository.findByAccountNumber(accountNumber);
        userRepository.delete(user);
        return "Account successfully closed";
    }

    @Override
    public UserResponse findById(String id) throws UserNotFoundException {
        Optional<User> foundUser =userRepository.findById(id);
        User easyPayUser =foundUser.orElseThrow(()-> new UserNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, id)));
        return buildUserResponse(easyPayUser);
       
    }

    private static UserResponse buildUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmailAddress())
                .name(user.getFirstName() + " " + user.getLastName())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers(int page, int items) {
        Pageable pageable = buildPageRequest(page, items);
        System.out.println(userRepository.findAll());
        Page<User> userPage =userRepository.findAll(pageable);
        List<User>users =userPage.getContent();
        return users.stream()
                .map(UserServiceImpl::buildUserResponse)
                .toList();
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public ApiResponse<?> deleteById(String id) {
        userRepository.deleteById(id);
        return ApiResponse.builder()
                .message(USER_DELETED_SUCCESSFULLY)
                .build();
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
}
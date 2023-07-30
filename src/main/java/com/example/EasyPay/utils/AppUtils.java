package com.example.EasyPay.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class AppUtils {
    public  static final String USER_REGISTRATION_SUCCESSFUL = "User registration successful";
    public  static final String USER_ACCOUNT_CREATED_SUCCESSFUL = "User account created successfully";
    public  static final String USER_WITH_PHONE_NUMBER_ALREADY_EXIST = "User with id %s already exists";

    public  static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    public static final String USER_WITH_ID_NOT_FOUND = "User with id %s not found";
    private static final int DEFAULT_PAGE_NUMBER = 1 ;
    private static final int DEFAULT_PAGE_LIMIT = 10;
    public static final int ZERO = 0;
    public static final int ONE = 1;

    public static Pageable buildPageRequest(int page, int items){
        if (page<=ZERO) page=DEFAULT_PAGE_NUMBER;
        if (items<=ZERO) items = DEFAULT_PAGE_LIMIT;
        page-=ONE;
        return PageRequest.of(page, items);
    }
}

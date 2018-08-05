package com.example.answer.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class User {
    private int userID;
    private String userAccount;
    private String userPassword;
    private String userTelephone;
    private String userEmail;
    private String userName;
    private String userPosition;
    private String collectRouteID;
    public User(){

    }
}

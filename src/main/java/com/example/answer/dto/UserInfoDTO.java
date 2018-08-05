package com.example.answer.dto;

import lombok.ToString;

@ToString
public class UserInfoDTO {
    private String telephone;
    private String name;
    private int sex;
    private String birthday;


    public UserInfoDTO(String telephone, String name, int sex, String birthday) {
        this.telephone = telephone;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
    }

    public UserInfoDTO() {
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}

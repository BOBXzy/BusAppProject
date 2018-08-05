package com.example.answer.mapper;

import com.example.answer.bean.Driver;
import com.example.answer.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoginMapper {
    @Select("SELECT * FROM driver where driverTelephone = #{telephone}")
    public List<Driver> isValidDriver(@Param("telephone") String tel);

    @Select("SELECT * FROM user where userTelephone = #{telephone}")
    public List<User> isVaildUser(@Param("telephone") String tel);

    @Insert("INSERT INTO user (userTelephone,userAccount) VALUES(#{telephone},#{telephone})")
    public int createUser(@Param("telephone") String tel);

    // 根据 手机号 获取 userID
    @Select("SELECT userID FROM user where userTelephone = #{telephone}")
    public int getUserIDByTel(@Param("telephone") String tel);

}

package com.example.answer.controller;

import com.example.answer.bean.JsonResult;
import com.example.answer.bean.JsonStatus;
import com.example.answer.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/api")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // 获取用户信息
    @RequestMapping(value = "getUserInfo",method =  RequestMethod.GET)
    public ResponseEntity<JsonStatus>getUserInfo(@RequestParam(value = "role",required = false) String role,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
            throws ServletException {
        JsonResult r = new JsonResult();
        try{
            Object claims = request.getAttribute("claims");
            if(claims == null){
                r.setStatus("-1002");
                r.setMsg("账号未登陆");
            } else {
                JSONObject json  = JSONObject.fromObject(claims);
                String tel = json.getString("tel");
                r.setResult(userService.getUserInfo(role,tel));
                r.setMsg("获取用户信息成功！");
            }
            System.out.println("进入getUserInfo");
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
    // 修改用户名
    @RequestMapping(value = "setUserName",method = RequestMethod.POST)
    public ResponseEntity<JsonStatus>setUserName(@RequestParam(value = "role",required = false) String role,
                                                   @RequestParam(value = "userName",required = false) String userName,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response)
            throws ServletException {
        JsonResult r = new JsonResult();
        try{
            Object claims = request.getAttribute("claims");
            if(claims == null){
                r.setStatus("-1002");
                r.setMsg("账号未登陆");
            } else {
                JSONObject json  = JSONObject.fromObject(claims);
                String tel = json.getString("tel");
                if(userService.setUserName(role,tel,userName)>0) {
                    r.setStatus("0");
                    r.setMsg("用户名修改成功");
                } else {
                    r.setStatus("-1001");
                    r.setMsg("用户名修改失败");
                }
            }
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
    // 修改用户性别
    @RequestMapping(value = "setUserSex",method = RequestMethod.POST)
    public ResponseEntity<JsonStatus>setUserSex(@RequestParam(value = "role",required = false) String role,
                                                 @RequestParam(value = "sex",required = false) int sex,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
            throws ServletException {
        JsonResult r = new JsonResult();
        try{
            Object claims = request.getAttribute("claims");
            if(claims == null){
                r.setStatus("-1002");
                r.setMsg("账号未登陆");
            } else {
                JSONObject json  = JSONObject.fromObject(claims);
                String tel = json.getString("tel");
                if(userService.setUserSex(role,tel,sex)>0) {
                    r.setStatus("0");
                    r.setMsg("用户性别修改成功");
                } else {
                    r.setStatus("-1001");
                    r.setMsg("用户性别修改失败");
                }
            }
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    // 修改用户生日
    @RequestMapping(value = "setUserBirthday",method = RequestMethod.POST)
    public ResponseEntity<JsonStatus>setUserSex(@RequestParam(value = "role",required = false) String role,
                                                @RequestParam(value = "birthday",required = false) String birthday,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
            throws ServletException {
        JsonResult r = new JsonResult();
        try{
            Object claims = request.getAttribute("claims");
            if(claims == null){
                r.setStatus("-1002");
                r.setMsg("账号未登陆");
            } else {
                JSONObject json  = JSONObject.fromObject(claims);
                String tel = json.getString("tel");
                if(userService.setUserBirthday(role,tel,birthday)>0) {
                    r.setStatus("0");
                    r.setMsg("用户生日修改成功");
                } else {
                    r.setStatus("-1001");
                    r.setMsg("用户生日修改失败");
                }
            }
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

}

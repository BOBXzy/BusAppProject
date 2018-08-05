package com.example.answer.controller;

import com.example.answer.bean.JsonResult;
import com.example.answer.bean.JsonStatus;
import com.example.answer.dto.RouteInfoDTO;
import com.example.answer.service.LoginService;
import com.example.answer.util.CommonUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/api")
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @SuppressWarnings("unused")
    private static class LoginCodeResponse {
        public String msgNum;  // 验证码
        public String tamp;  // 过期时间
        public LoginCodeResponse(final String msgNum,final String tamp) {
            this.msgNum = msgNum;
            this.tamp = tamp;
        }
    }

    @SuppressWarnings("unused")
    private static class LoginTokenResponse {
        public String token;  // 用户token
        public String role;
        public LoginTokenResponse(final String token) {
            this.token = token;
        }
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    @RequestMapping(value = "getVerificaCode",method = RequestMethod.POST)
    public ResponseEntity<JsonStatus>getVerificaCode(@RequestParam(value = "telephone",required = false) String tel,HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        JsonResult r = new JsonResult();
        try{
            System.out.println("this is test:"+tel);
            Map<String, Object> codeMap = loginService.getVerificaCode(tel);
            if(codeMap.get("status").equals("0")){
                r.setStatus("0");
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MINUTE, 5);
                String currentTime = sf.format(c.getTime());// 生成5分钟后时间，用户校验是否过期
                LoginCodeResponse lr = new LoginCodeResponse(Jwts.builder().setSubject("msgNum")
                        .claim("msgNum", codeMap.get("msgNum")).setIssuedAt(new Date())
                        .signWith(SignatureAlgorithm.HS256, "secretkey").compact(),currentTime);
                r.setResult(lr);
//                response.setHeader("Access-Token",lr.toString());
                r.setMsg("获取短信验证码成功");
            }

        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
//        return ResponseEntity.ok(r);
    }
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public ResponseEntity<JsonStatus>login(@RequestParam(value = "telephone",required = false) String telephone,
                                           @RequestParam(value = "code",required = false) String code,
                                           @RequestParam(value = "tamp",required = false) String tamp,
                                           @RequestParam(value = "msgNum",required = false) String msgNum
                                           ) throws ServletException{
        JsonResult r = new JsonResult();
        try{
            System.out.println("telephone:"+telephone);
            System.out.println("code:"+code);
            System.out.println("tamp:"+tamp);
            System.out.println("msgNum:"+msgNum);
            final Claims claims = Jwts.parser().setSigningKey("secretkey")
                    .parseClaimsJws(msgNum).getBody();
            JSONObject json  = JSONObject.fromObject(claims);
            String realCode = json.getString("msgNum");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = sf.format(c.getTime());// 生成5分钟后时间，用户校验是否过期
            System.out.println("currentTime:"+currentTime);
            System.out.println("tamp:"+tamp);
            System.out.println(tamp.compareTo(currentTime));
            if(tamp.compareTo(currentTime) > 0) {
                if(code.equals(realCode)) {
                    r.setStatus("0");
                    LoginTokenResponse lr = new LoginTokenResponse(Jwts.builder().setSubject("token")
                            .claim("tel", telephone).setIssuedAt(new Date())
                            .signWith(SignatureAlgorithm.HS256, "secretkey").compact());
                    if(loginService.isValidDriver(telephone)){
                        lr.setRole("driver");
                    }else {
                        lr.setRole("user");
                        if(loginService.isVaildUser(telephone)){
                            System.out.println("该账号已经注册过了");
                        } else {
                            loginService.createUser(telephone);
                        }
                    }
                    r.setResult(lr);
                    r.setMsg("登陆成功");
                }else {
                    System.out.println("code:"+code);
                    System.out.println("realCode:"+realCode);
                    r.setStatus("-1001");
                    r.setMsg("验证失败");
                }
            }else {
                System.out.println("验证码超时了");
                r.setStatus("-1003");
                r.setMsg("验证码超时");
            }
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


}

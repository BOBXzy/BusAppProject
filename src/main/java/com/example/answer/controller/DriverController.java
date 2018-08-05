package com.example.answer.controller;

import com.example.answer.bean.JsonResult;
import com.example.answer.bean.JsonStatus;
import com.example.answer.dto.DriverDTO;
import com.example.answer.dto.RouteInfoDTO;
import com.example.answer.service.DriverService;
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
import java.util.List;

@RequestMapping("/api")
@RestController
public class DriverController {
    @Autowired
    private DriverService driverService;

    @RequestMapping(value = "getDriverInfo",method = RequestMethod.GET)
    public ResponseEntity<JsonStatus>getDriverInfo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        JsonResult r = new JsonResult();
        try{
//            DriverDTO driver = driverService.getDriverInfo("17816873920");
//            r.setStatus("0");
//            r.setMsg("获取司机信息成功");
//            r.setResult(driver);
            Object claims = request.getAttribute("claims");
            if(claims == null) {
                r.setStatus("-1002");
                r.setMsg("账户未登录");
            } else {
                JSONObject json  = JSONObject.fromObject(claims);
                System.out.println("json:"+json);
                String tel = json.getString("tel");
                System.out.println("tel:"+tel);
                System.out.println("getDriverInfo（）+"+tel);
                DriverDTO driver = driverService.getDriverInfo(tel);
                r.setStatus("0");
                r.setMsg("获取司机信息成功");
                r.setResult(driver);
            }
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @RequestMapping(value = "setStopIndex",method = RequestMethod.POST)
    public ResponseEntity<JsonStatus>setStopIndex(
            @RequestParam(value = "busCode",required = false) int busID,
            @RequestParam(value = "routeCode",required = false) int routeID,
            @RequestParam(value = "currentIndex",required = false) int currentIndex,
            @RequestParam(value = "direction",required = false) int direction,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException {
        JsonResult r = new JsonResult();
        try{
            if(driverService.setStopIndex(busID, routeID,currentIndex,direction) == 0) {
                r.setStatus("-1100");
                r.setMsg("设置错误");
            } else {
                r.setStatus("0");
                r.setMsg("设置站点成功");
            }
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @RequestMapping(value = "setTerminalIndex",method = RequestMethod.POST)
    public ResponseEntity<JsonStatus>setTerminalIndex(
            @RequestParam(value = "busCode",required = false) int busID,
            @RequestParam(value = "routeCode",required = false) int routeID,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException {
        JsonResult r = new JsonResult();
        try{
            if(driverService.setTerminalIndex(busID,routeID,0) == 0) {
                r.setStatus("-1100");
                r.setMsg("设置错误");
            } else {
                r.setStatus("0");
                r.setMsg("设置站点成功");
            }
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
    @RequestMapping(value = "setBusPosition", method = RequestMethod.POST)
    public ResponseEntity<JsonStatus>setBusPosition(
            @RequestParam(value = "busCode",required = false) int busID,
            @RequestParam(value = "position",required = false) String position)
        throws ServletException {
        JsonResult r = new JsonResult();
        try{
            if(driverService.setBusPosition(busID,position) == 0) {
                r.setStatus("-1100");
                r.setMsg("设置错误");
            } else {
                r.setStatus("0");
                r.setMsg("设置站点成功");
            }
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
}

package com.example.answer.controller;

import com.example.answer.bean.JsonResult;
import com.example.answer.bean.JsonStatus;
import com.example.answer.service.PassengerService;
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
public class PassengerController {
    @Autowired
    private PassengerService passengerService;

    // 获取Route站点列表
    @RequestMapping(value = "getWorkingDriver",method = RequestMethod.GET)
    public ResponseEntity<JsonStatus>getWorkingDriver(@RequestParam(value = "lat",required = false) String lat,
                                                 @RequestParam(value = "lng",required = false) String lng,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
            throws ServletException {

        JsonResult r = new JsonResult();
        try{
            r.setResult(passengerService.getWorkingList(lat,lng));
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    // 获取校车线路信息



}

package com.example.answer.controller;

import com.example.answer.bean.JsonResult;
import com.example.answer.bean.JsonStatus;
import com.example.answer.dto.StopDTO;
import com.example.answer.dto.StopInfoDTO;
import com.example.answer.dto.StopNameDTO;
import com.example.answer.service.StopService;
import com.example.answer.util.CommonUtils;
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
public class StopController {
    @Autowired
    private StopService stopService;

    @SuppressWarnings("unused")
    private static class StopListResponse {
        public int nearStopCode;  // 最近点stopID
        public List<StopDTO> stopList;  // 所有站点列表
        public StopListResponse() {
        }
        public int getNearStopCode() {
            return nearStopCode;
        }

        public void setNearStopCode(int nearStopCode) {
            this.nearStopCode = nearStopCode;
        }

        public List<StopDTO> getStopList() {
            return stopList;
        }

        public void setStopList(List<StopDTO> stopList) {
            this.stopList = stopList;
        }
    }

    @RequestMapping(value = "getStopPosition",method = RequestMethod.GET)
    public ResponseEntity<JsonStatus>getStopPosition(
            @RequestParam(value = "lat",required = false) String lat,
            @RequestParam(value = "lng",required = false) String lng,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException {

        JsonResult r = new JsonResult();
        try{
            System.out.println("lat:"+lat);
            System.out.println("lng:"+lng);
            StopListResponse slr = new StopListResponse();
            List<StopDTO> stopList = stopService.getStopPosition();
            StopDTO stopDTO = CommonUtils.getNearPosition(Double.parseDouble(lat),Double.parseDouble(lng),stopList);
            System.out.println("stopDTO:"+stopDTO);
            slr.setNearStopCode(stopDTO.getStopID());
            slr.setStopList(stopList);
            r.setResult(slr);
            r.setStatus("0");
            r.setMsg("获取站点信息成功");
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @RequestMapping(value = "getStopInfo",method = RequestMethod.GET)
    public ResponseEntity<JsonStatus>getStopInfo(
            @RequestParam(value = "lat",required = false) String lat,
            @RequestParam(value = "lng",required = false) String lng,
            @RequestParam(value = "stopCode",required = false) int stopCode,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException {

        JsonResult r = new JsonResult();
        try{
            System.out.println("lat1111:"+lat);
            System.out.println("lng1111:"+lng);
            StopInfoDTO sid = stopService.getStopInfo(stopCode);
            if(sid == null) {
                r.setStatus("-1001");
                r.setMsg("错误的stopCode");
            } else {
                double distance = CommonUtils.getDistance(Double.parseDouble(lat),Double.parseDouble(lng),Double.parseDouble(sid.getLat()),Double.parseDouble(sid.getLng()));
                sid.setDistance(Integer.toString((int) distance));
                r.setResult(sid);
                r.setStatus("0");
                r.setMsg("获取信息成功");
            }
        }catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @RequestMapping(value = "getNearStopList",method = RequestMethod.GET)
    public ResponseEntity<JsonStatus>getNearStopList(
            @RequestParam(value = "lat",required = false) String lat,
            @RequestParam(value = "lng",required = false) String lng,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException {
        JsonResult r = new JsonResult();
        try{
            List<StopNameDTO> stopNameList = stopService.getStopName();
            List<StopNameDTO> snl = CommonUtils.getRangeList(Double.parseDouble(lat),Double.parseDouble(lng),stopNameList,2000);
            r.setResult(snl);
            r.setMsg("获取成功");
            r.setStatus("0");
//            StopDTO stopDTO = CommonUtils.getNearPosition(Double.parseDouble(lat),Double.parseDouble(lng),stopList);
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @RequestMapping(value = "getStopDetail",method = RequestMethod.GET)
    public ResponseEntity<JsonStatus>getStopDetail(
            @RequestParam(value = "stopName",required = false) String stopName,
            @RequestParam(value = "busCode",required = false) int busCode,
            @RequestParam(value = "stopIndex",required = false) int stopIndex,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException {
        JsonResult r = new JsonResult();
        try{
           r.setResult(stopService.getStopDetail(stopName,busCode,stopIndex));
           r.setStatus("0");
           r.setMsg("获取站点详情成功");
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }



}

package com.example.answer.controller;

import com.example.answer.bean.*;
import com.example.answer.dto.RouteInfoDTO;
import com.example.answer.dto.RouteStopDTO;
import com.example.answer.service.LoginService;
import com.example.answer.service.RouteService;
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
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController
public class RouteController {
    @Autowired
    private RouteService routeService;
    @Autowired
    private LoginService loginService;

    // 获取Route信息
    @RequestMapping(value = "getRouteList",method = RequestMethod.GET)
    public ResponseEntity<JsonStatus>getRouteList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        JsonResult r = new JsonResult();

//        JsonResult r = new JsonResult();
        try{
            List<Route> routes = routeService.getRouteList();
            r.setStatus("0");
//            System.out.println(routes);
            for (int i=0;i<routes.size();i++){
                if(routes.get(i).getStopList() == null){
                    System.out.println("the null of index is "+ i);
                }else{
                    JSONObject jsonObjectStopList=JSONObject.fromObject(routes.get(i).getStopList());
                    routes.get(i).setStopList(jsonObjectStopList.getString("forward").replaceAll("\"","\'"));
                }
                JSONObject jsonObjectStartStop=JSONObject.fromObject(routes.get(i).getStartStop());
                routes.get(i).setStartStop(jsonObjectStartStop.getString("forward").replaceAll("\"","\'"));
                JSONObject jsonObjectEndStop=JSONObject.fromObject(routes.get(i).getEndStop());
                routes.get(i).setEndStop(jsonObjectEndStop.getString("forward").replaceAll("\"","\'"));
//                System.out.println(routes);
            }
            r.setResult(routes);
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
//        return ResponseEntity.ok(r);
    }

    @RequestMapping(value = "getRouteInfoList",method = RequestMethod.GET)
    public ResponseEntity<JsonStatus>getRouteInfoList(@RequestParam(value = "stopCode",required = false) String stopID,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response)
            throws ServletException {

        JsonResult r = new JsonResult();
        try{

            System.out.println("stopID:"+stopID);
            int stopCode;
            List<RouteInfoDTO> routes = new ArrayList<>();
            Object claims = request.getAttribute("claims");
            if(claims == null){
                r.setStatus("-1002");
                r.setMsg("账号未登陆");
            } else {
                JSONObject json  = JSONObject.fromObject(claims);
                String tel = json.getString("tel");
                if(stopID == null) {
                    stopCode = -1;
                    System.out.println("传的参数为空");
                    routes = routeService.getRouteInfoList(stopCode);
                } else {
                    stopCode = Integer.parseInt(stopID);
                    routes = routeService.getRouteInfoList(stopCode);
                }

                System.out.println(tel);
                List<Collect> collects = routeService.getCollectList(tel);

                for (int i=0;i<routes.size();i++){
                    for(int j = 0; j<collects.size();j++) {
                        if(collects.get(j).getRouteID() == routes.get(i).getRouteID()){
                            routes.get(i).setCollect(true);
                            continue;
                        }
                        if(j == collects.size()) {
                            routes.get(i).setCollect(false);
                        }
                    }
                    JSONObject jsonObjectStartStop=JSONObject.fromObject(routes.get(i).getStartStop());
                    routes.get(i).setStartStop(jsonObjectStartStop.getString("forward").replaceAll("\"","\'"));
                    JSONObject jsonObjectEndStop=JSONObject.fromObject(routes.get(i).getEndStop());
                    routes.get(i).setEndStop(jsonObjectEndStop.getString("forward").replaceAll("\"","\'"));
                }
                r.setStatus("0");
                r.setResult(routes);
            }

        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
//        return ResponseEntity.ok(r);
    }

    // 获取Route站点列表
    @RequestMapping(value = "getStopList",method = RequestMethod.GET)
    public ResponseEntity<JsonStatus>getStopList(@RequestParam(value = "routeCode",required = false) int routeID,
                                                 @RequestParam(value = "routeDirection",required = false) int routeDirection,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
            throws ServletException {

        JsonResult r = new JsonResult();
        try{
            RouteStopDTO stopList = routeService.getRouteStopList(routeID,routeDirection);
            r.setStatus("0");
            r.setResult(stopList);
            r.setMsg("获取列表成功");
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    // 获取收藏线路列表
    @RequestMapping(value = "getCollectList",method = RequestMethod.GET)
    public ResponseEntity<JsonStatus>getCollectList(HttpServletRequest request,
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
                List<RouteInfoDTO> routes = routeService.getCollectRouteList(tel);
                for (int i=0;i<routes.size();i++) {
                    JSONObject jsonObjectStartStop=JSONObject.fromObject(routes.get(i).getStartStop());
                    routes.get(i).setStartStop(jsonObjectStartStop.getString("forward").replaceAll("\"","\'"));
                    JSONObject jsonObjectEndStop=JSONObject.fromObject(routes.get(i).getEndStop());
                    routes.get(i).setEndStop(jsonObjectEndStop.getString("forward").replaceAll("\"","\'"));
                }
                r.setStatus("0");
                r.setResult(routes);
            }
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    // 新增线路收藏
    @RequestMapping(value = "createCollect",method = RequestMethod.POST)
    public ResponseEntity<JsonStatus>createCollect(@RequestParam(value = "routeID",required = false) int routeID,
                                                   @RequestParam(value = "direction",required = false) int direction,
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
                int userID = loginService.getUserIDByTel(tel);
                if(routeService.createCollect(userID,routeID,direction) > 0) {
                    r.setStatus("0");
                    r.setMsg("新增收藏成功");
                }else {
                    r.setStatus("-1001");
                    r.setMsg("新增收藏失败");
                }
            }
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
    // 取消线路收藏
    @RequestMapping(value = "deleteCollect",method = RequestMethod.POST)
    public ResponseEntity<JsonStatus>deleteCollect(@RequestParam(value = "routeID",required = false) int routeID,
                                                   @RequestParam(value = "direction",required = false) int direction,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response)
            throws ServletException {

        JsonResult r = new JsonResult();
        try{
            System.out.println("进入了deleteCollect");
            Object claims = request.getAttribute("claims");
            if(claims == null){
                r.setStatus("-1002");
                r.setMsg("账号未登陆");
            } else {
                JSONObject json  = JSONObject.fromObject(claims);
                String tel = json.getString("tel");
                int userID = loginService.getUserIDByTel(tel);
                if(routeService.deleteCollect(userID,routeID,direction) > 0) {
                    r.setStatus("0");
                    r.setMsg("取消收藏成功");
                }else {
                    r.setStatus("-1001");
                    r.setMsg("取消收藏失败");
                }
            }
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    // 获取校车详情
    @RequestMapping(value = "getBusDetail", method = RequestMethod.GET)
    public ResponseEntity<JsonStatus>getBusDetail(@RequestParam(value = "routeCode",required = false) int routeCode,
                                                  @RequestParam(value = "busCode",required = false) int busCode,
                                                  @RequestParam(value = "direction",required = false) int direction,
                                                  @RequestParam(value = "nearStopName",required = false) String nearStopName,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response)
            throws ServletException {

        JsonResult r = new JsonResult();
        try{
            r.setResult(routeService.getBusDetail(routeCode,busCode,nearStopName,direction));
            r.setMsg("获取成功");
            r.setStatus("0");
        }catch (Exception e){
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("-1000");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
}

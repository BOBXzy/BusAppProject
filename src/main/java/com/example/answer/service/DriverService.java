package com.example.answer.service;

import com.example.answer.dto.DriverDTO;

import java.util.List;

public interface DriverService {
    // 获取司机信息
    public DriverDTO getDriverInfo(String tel);

    // 更新司机站点情况
    public int setStopIndex(int busID, int routeID, int currentIndex,int direction);

    // 司机抵达终点
    public int setTerminalIndex(int busID, int routeID, int currentIndex);

    // 更新司机坐标
    public int setBusPosition(int busID, String position);
}

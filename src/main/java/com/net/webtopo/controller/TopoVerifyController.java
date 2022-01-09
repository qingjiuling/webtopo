package com.net.webtopo.controller;

import com.net.webtopo.pojo.TestResult;
import com.net.webtopo.util.JsonUtils;
import com.net.webtopo.util.ResultWrapper;
import com.net.webtopo.util.RouterConnect;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/check")
public class TopoVerifyController {

    @Resource
    ConnectionController connectionController;
    /**
     * ping
     * @return
     */
    @GetMapping("/ping/{id}")
    public ResultWrapper topoPing(@PathVariable("id") String routeId, @RequestParam("ip") String pingRouteIp) {
        System.out.println(pingRouteIp);
        String rs = RouterConnect.instance.sendCommand("ping " + pingRouteIp);
        System.out.println(rs);
        try {
            rs = new String(rs.getBytes("ISO-8859-1"),"GBK");
            return new ResultWrapper(rs);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResultWrapper(500,"路由连通失败");
    }

    /**
     * test
     * @return
     */
    @GetMapping("/test")
    @ResponseBody
    public ResultWrapper topoTest() {
        try{
            List<TestResult> resultList = new LinkedList<>();

            String content = JsonUtils.readJsonFile("src/main/resources/file/test.json");
            JSONObject jsonObject = JSONObject.fromObject(content);

//            ConnectionController connectionController = new ConnectionController();
            JSONArray cases = jsonObject.getJSONArray("data");
            String temp = null;
            for (Object value : cases) {
//                JSONObject myCase = (JSONObject) jsonObject.get(key);
                JSONObject myCase = (JSONObject) value;
                String routerId = myCase.getString("router_id");
                String cmd = myCase.getString("input");
                if(temp == null || !temp.equals(routerId)){
                    try {
                        //添加一个关闭连接
                        System.out.println("Reconnect router.");
                        temp = routerId;
                        connectionController.distinctConnection();
                        connectionController.establishConnection(routerId);
                    } catch (Exception e) {
                        System.out.println(routerId);
                    }
                }
                RouterConnect.instance.sendCommand("terminal length 0"); // 发送给路由器
                String rs = RouterConnect.instance.sendCommand(cmd); // 发送给路由器
                System.out.println(rs);
                TestResult ts = new TestResult(Integer.parseInt(myCase.getString("case")), cmd,rs); // 把每一行指令的结果存入队列
                resultList.add(ts);
                //添加一个关闭连接
                connectionController.distinctConnection();

//                try {
//                    rs = new String(rs.getBytes("ISO-8859-1"),"GBK");
//
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                    return new ResultWrapper(500,cmd + " 指令发送失败");
//                }
            }

            return new ResultWrapper(resultList.toString()); // 返回输入输出列表的字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultWrapper(500,"测试文件获取失败");
    }

    public static void main(String[] args) {
        TopoVerifyController topoVerifyController = new TopoVerifyController();
        topoVerifyController.topoTest();
    }
}

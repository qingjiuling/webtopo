package com.net.webtopo.controller;

import com.net.webtopo.pojo.TestResult;
import com.net.webtopo.util.JsonUtils;
import com.net.webtopo.util.ResultWrapper;
import com.net.webtopo.util.RouterConnect;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/check")
public class TopoVerifyController {

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

            ConnectionController connectionController = new ConnectionController();
            for (Object key:jsonObject.keySet()
            ) {
                JSONObject myCase = (JSONObject) jsonObject.get(key);
                String routerId = myCase.getString("router_id");
                String cmd = myCase.getString("input");
                connectionController.establishConnection(routerId);
                String rs = RouterConnect.instance.sendCommand(cmd); // 发送给路由器
                try {
                    rs = new String(rs.getBytes("ISO-8859-1"),"GBK");
                    System.out.println(rs);
                    TestResult ts = new TestResult(cmd,rs); // 把每一行指令的结果存入队列
                    resultList.add(ts);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return new ResultWrapper(500,cmd + " 指令发送失败");
                }
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

package com.net.webtopo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.net.webtopo.util.JsonUtils;
import com.net.webtopo.util.ResultWrapper;

/*import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;*/

import com.net.webtopo.util.RouterConnect;
import net.sf.json.JSONObject;

import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/config")
public class ConfigController {

    public static String portConvert(String s){
        if(s.equals("lo0"))return "loopback0";
        else if(s.equals("l01")) return "loopback1";
        else if (s.equals("l02")) return "loopback2";
        else return s;
    }


    @PutMapping("/interaction")
    public ResultWrapper interactConfig(@RequestBody Map<String,String> interact){

        // 更改configInfo文件
        String content = JsonUtils.readJsonFile("src/main/resources/File/configInfo.json");
        JSONObject jsonObject = JSONObject.fromObject(content);
        Object router = jsonObject.get(interact.get("id"));
        jsonObject.remove(interact.get("id"));
        JSONObject routerJson = JSONObject.fromObject(router);
        Object myPort = routerJson.get(interact.get("interface"));
        routerJson.remove(interact.get("interface"));
        JSONObject portJson = JSONObject.fromObject(myPort);
        portJson.put("ip", interact.get("ip"));
        portJson.put("mask", interact.get("mask"));
        portJson.put("status", "activated");

        routerJson.put(interact.get("interface"),portJson);
        jsonObject.put(interact.get("id"),routerJson);
        String editJson = JSON.toJSONString(jsonObject,
                SerializerFeature.PrettyFormat);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/File/configInfo.json"));
            bw.write(editJson);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //向路由器发送信息
        String rs = "";
        rs = rs + RouterConnect.instance.sendCommand("configure terminal");
        rs = rs + RouterConnect.instance.sendCommand(String.format("interface %s", interact.get("interface")));
        rs = rs + RouterConnect.instance.sendCommand(String.format("ip address %s %s", interact.get("ip"), interact.get("mask")));
        rs = rs + RouterConnect.instance.sendCommand("no shutdown");
        return new ResultWrapper(rs);
    }

    @PutMapping("/file")
    public ResultWrapper file(@RequestParam("id") String id){
        // 读取配置文件
        String content = JsonUtils.readJsonFile("src/main/resources/File/config_file.json");
        JSONObject jsonObject = JSONObject.fromObject(content);
        Object router = jsonObject.get("router_01");
        JSONObject jsonRouter = JSONObject.fromObject(router);
        String[] portList = jsonRouter.getString("port").split(",");
        String rs = "";

        // 配置端口，掩码成谜
        for(int i=0;i< portList.length;i++){
            String[] tempList = portList[i].split(":");
            tempList[0] = portConvert(tempList[0]);
            rs = rs + RouterConnect.instance.sendCommand("configure terminal");
            rs = rs + RouterConnect.instance.sendCommand(String.format("interface %s", tempList[0]));
            rs = rs + RouterConnect.instance.sendCommand(String.format("ip address %s %s", tempList[1], "255.255.255.0"));
            rs = rs + RouterConnect.instance.sendCommand("no shutdown");
            rs = rs + RouterConnect.instance.sendCommand("exit");
            rs = rs + RouterConnect.instance.sendCommand("exit");
        }

        // 执行command
        String[] commandList = jsonRouter.getString("command").split(",");
        rs = rs + RouterConnect.instance.sendCommand("configure terminal");
        for (int i=0;i<commandList.length;i++){
            rs = rs + RouterConnect.instance.sendCommand(commandList[i]);
        }
        rs = rs + RouterConnect.instance.sendCommand("exit");

        // 执行ping
        String[] pingList = jsonRouter.getString("ping").split(",");
        for (int i=0;i< pingList.length;i++){
            rs = rs + RouterConnect.instance.sendCommand(String.format("ping %s", pingList[i]));
        }

        // 执行show
        rs = rs + RouterConnect.instance.sendCommand("show");

        // 执行showtest，不知道什么意思，明天问

        return new ResultWrapper(rs);
    }

    @PutMapping("command")
    public ResultWrapper interactCommand(@RequestBody Map<String,String> interact){
        String rs = RouterConnect.instance.sendCommand(interact.get("command"));
        return new ResultWrapper(interact.get(rs));
    }
}

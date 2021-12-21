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

import static org.apache.tomcat.jni.Time.sleep;


@RestController
@RequestMapping("/config")
public class ConfigController {

    public static void main(String[] args) {
        System.out.println(portConvert("s0/0/0"));
    }
    public static String portConvert(String s){
        if(s.equals("lo0"))return "loopback0";
        else if(s.equals("l01")) return "loopback1";
        else if(s.equals("l02")) return "loopback2";
        else if(s.equals("s0")) return "s0/0/0";
        else if(s.equals("s1")) return "s0/0/1";
        else if(s.equals("s0/0/0")) return "s0";
        else if(s.equals("s0/0/1")) return "s1";
        else return s;
    }

    public static void statusChange(String id,String port,String ip,String mask){
        // 更改configInfo文件
        String content = JsonUtils.readJsonFile("src/main/resources/File/configInfo.json");
        JSONObject jsonObject = JSONObject.fromObject(content);
        Object router = jsonObject.get(id);
        jsonObject.remove(id);
        JSONObject routerJson = JSONObject.fromObject(router);
        Object myPort = routerJson.get(port);
        routerJson.remove(port);
        JSONObject portJson = JSONObject.fromObject(myPort);
        portJson.put("ip", ip);
        portJson.put("mask", mask);
        portJson.put("status", "activated");

        routerJson.put(port,portJson);
        jsonObject.put(id,routerJson);
        System.out.println(jsonObject);
        //String editJson = JSON.toJSONString(jsonObject,SerializerFeature.PrettyFormat);
//        String editJson = JSON.toJSONString(jsonObject,
//                SerializerFeature.PrettyFormat);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/File/configInfo.json"));
            bw.write(String.valueOf(jsonObject));
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PutMapping("/interaction")
    public ResultWrapper interactConfig(@RequestBody Map<String,String> interact){
        //更改ConfigInfo文件
        statusChange(interact.get("id"),interact.get("interface"),interact.get("ip"),interact.get("mask"));

        String rs = "";
        rs = rs + RouterConnect.instance.sendCommand("configure terminal");
        sleep(500);
        rs = rs + RouterConnect.instance.sendCommand(String.format("interface %s", portConvert(interact.get("interface"))));
        sleep(500);
        rs = rs + RouterConnect.instance.sendCommand(String.format("ip address %s %s", interact.get("ip"), interact.get("mask")));
        sleep(500);
        rs = rs + RouterConnect.instance.sendCommand("no shutdown");
        sleep(500);
        rs = rs + RouterConnect.instance.sendCommand("exit");
        rs = rs + RouterConnect.instance.sendCommand("exit");
        System.out.println(rs);
        return new ResultWrapper(rs);
    }

    @PutMapping("/file")
    public ResultWrapper file(@RequestParam("id") String id){
        // 读取配置文件
        String content = JsonUtils.readJsonFile("src/main/resources/File/config_file.json");
        JSONObject jsonObject = JSONObject.fromObject(content);
        Object router = jsonObject.get(id);
        JSONObject jsonRouter = JSONObject.fromObject(router);
        String[] portList = jsonRouter.getString("port").split(",");
        String rs = "";
        // 配置端口，掩码成谜
        for(int i=0;i< portList.length;i++){
            String[] tempList = portList[i].split(":");
            System.out.println(tempList[0] + "233");
            if((tempList[0].equals("lo0"))||(tempList[0].equals("lo1"))||(tempList[0].equals("lo2"))) System.out.println(444);
            else tempList[0] = portConvert(tempList[0]);
            System.out.println(id);
            System.out.println(tempList[0]);
            System.out.println(tempList[1]);
            statusChange(id, tempList[0],tempList[1],"255.255.255.0");
            rs = rs + RouterConnect.instance.sendCommand("configure terminal");
            rs = rs + RouterConnect.instance.sendCommand(String.format("interface %s", portConvert(tempList[0])));
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
        rs = rs + RouterConnect.instance.sendCommand("exit");

        // 执行ping
//        String[] pingList = jsonRouter.getString("ping").split(",");
//        for (int i=0;i< pingList.length;i++){
//            rs = rs + RouterConnect.instance.sendCommand(String.format("ping %s", pingList[i]));
//        }

        // 执行show
        rs = rs + RouterConnect.instance.sendCommand("show");

        // 执行showtest，不知道什么意思，明天问
        System.out.println(rs);
        return new ResultWrapper(rs);
    }

    @PutMapping("command")
    public ResultWrapper interactCommand(@RequestBody Map<String,String> interact){
        String rs = RouterConnect.instance.sendCommand(interact.get("command"));
        return new ResultWrapper(interact.get(rs));
    }
}

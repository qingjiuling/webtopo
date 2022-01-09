package com.net.webtopo.controller;

import com.net.webtopo.util.JsonUtils;
import com.net.webtopo.util.ResultWrapper;
import com.net.webtopo.util.RouterConnect;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class ClearController {

    @Resource
    ConnectionController connectionController;

    @GetMapping("/clear")
    public ResultWrapper clear(){
        String content = JsonUtils.readJsonFile("configInfo.json");
        try {
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(String.format("src/main/resources/File/configInfo.json"))
            );
            out.write(content.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            String rs = "";
            connectionController.distinctConnection();
            connectionController.establishConnection("router_01");
            rs = rs + RouterConnect.instance.sendCommand("configure terminal");
            rs = rs + RouterConnect.instance.sendCommand("default interface s0/0/0");
            rs = rs + RouterConnect.instance.sendCommand("default interface s0/0/1");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo0");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo1");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo2");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo3");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo4");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo5");
            rs = rs + RouterConnect.instance.sendCommand("no router ospf 1");
            rs = rs + RouterConnect.instance.sendCommand("clear ip route *");
            rs = rs + RouterConnect.instance.sendCommand("exit");
            rs = rs + RouterConnect.instance.sendCommand("exit");
            connectionController.distinctConnection();
            connectionController.establishConnection("router_02");
            rs = rs + RouterConnect.instance.sendCommand("configure terminal");
            rs = rs + RouterConnect.instance.sendCommand("default interface s0/0/0");
            rs = rs + RouterConnect.instance.sendCommand("default interface s0/0/1");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo0");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo1");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo2");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo3");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo4");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo5");
            rs = rs + RouterConnect.instance.sendCommand("no router ospf 1");
            rs = rs + RouterConnect.instance.sendCommand("no router rip");
            rs = rs + RouterConnect.instance.sendCommand("no ip route 32.30.30.0 255.255.255.0 s0/0/1");
            rs = rs + RouterConnect.instance.sendCommand("clear ip route *");
            rs = rs + RouterConnect.instance.sendCommand("exit");
            rs = rs + RouterConnect.instance.sendCommand("exit");
            connectionController.distinctConnection();
            connectionController.establishConnection("router_03");
            rs = rs + RouterConnect.instance.sendCommand("configure terminal");
            rs = rs + RouterConnect.instance.sendCommand("default interface s0/0/0");
            rs = rs + RouterConnect.instance.sendCommand("default interface s0/0/1");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo0");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo1");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo2");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo3");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo4");
            rs = rs + RouterConnect.instance.sendCommand("no interface lo5");
            rs = rs + RouterConnect.instance.sendCommand("no router rip");
            rs = rs + RouterConnect.instance.sendCommand("no ip route 22.20.20.0 255.255.255.0 s0/0/1");
            rs = rs + RouterConnect.instance.sendCommand("clear ip route *");
            rs = rs + RouterConnect.instance.sendCommand("exit");
            rs = rs + RouterConnect.instance.sendCommand("exit");
            connectionController.distinctConnection();
            System.out.println(rs);
            return new ResultWrapper("执行成功");
        } catch (FileNotFoundException e) {
            return new ResultWrapper(500,"没有文件");
        } catch (IOException e) {
            return new ResultWrapper(500,"IO失败");
        }
    }
}

package com.net.webtopo.controller;

import com.net.webtopo.util.ResultWrapper;
import com.net.webtopo.util.RouterConnect;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connection")
public class ConnectionController {

    @GetMapping("/establish")
    public ResultWrapper establishConnection(@RequestParam("ip") String ip){
        RouterConnect.instance.login(ip, 23, "CISCO");
        return new ResultWrapper("test");
    }

    @GetMapping("distinct")
    public ResultWrapper distinctConnection(){
        RouterConnect.instance.distinct();
        return new ResultWrapper("test2");
    }
}

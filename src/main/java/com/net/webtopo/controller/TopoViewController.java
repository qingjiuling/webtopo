package com.net.webtopo.controller;

import com.net.webtopo.pojo.AllInfo;
import com.net.webtopo.util.ReadInfo;
import com.net.webtopo.util.ResultWrapper;
import com.net.webtopo.util.RouterConnect;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.UnsupportedEncodingException;

@RestController
public class TopoViewController {

    ReadInfo rd = new ReadInfo();

    /**
     * view all information
     * @return
     */
    @GetMapping("/view")
    public ResultWrapper getAllInfo() {
        AllInfo ai = new AllInfo(rd.getRouter("router_01"), rd.getRouter("router_02"), rd.getRouter("router_03"), rd.getCable("cable_01"), rd.getCable("cable_02"));
        return new ResultWrapper(ai);
    }

    /**
     * view router information
     * @return
     */
    @GetMapping("/view/router/{id}")
    public ResultWrapper getRouter(@PathVariable("id") String routeId) {
        return new ResultWrapper(rd.getRouter(routeId));
    }

    /**
     * view cable information
     * @return
     */
    @GetMapping("/view/cable/{id}")
    public ResultWrapper getCable(@PathVariable String id) {
        return new ResultWrapper(rd.getCable(id));
    }
}

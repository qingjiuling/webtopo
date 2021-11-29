package com.net.webtopo.controller;

import com.net.webtopo.pojo.TestResult;
import com.net.webtopo.util.ReadInfo;
import com.net.webtopo.util.ResultWrapper;
import com.net.webtopo.util.RouterConnect;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/check")
public class TopoVerifyController {

    /**
     * ping
     * @return
     */
    @GetMapping("/ping/{id}}")
    public ResultWrapper topoPing(@PathVariable("id") String routeId, @RequestParam("ip") String pingRouteIp) {
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
    @GetMapping("/test/{id}}")
    @ResponseBody
    public ResultWrapper topoTest(@PathVariable("id") String routeId) {
        // Path path = Paths.get(String.format("src/main/resources/file/%s.json", routeId)); // 测试文件的名字可能要改一下
        // File file = new File(String.valueOf(path));
        File file = new File("src/main/resources/file/test.json");

        try (BufferedReader br = new BufferedReader(new FileReader(file));) {
            List<TestResult> list = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) { // 按行读取文件
                System.out.println(line); // 打印每一行指令
                String rs = RouterConnect.instance.sendCommand(line); // 发送给路由器
                try {
                    rs = new String(rs.getBytes("ISO-8859-1"),"GBK");
                    System.out.println(rs);
                    TestResult ts = new TestResult(line,rs); // 把每一行指令的结果存入队列
                    list.add(ts);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return new ResultWrapper(500,line + " 指令发送失败");
                }
            }
            return new ResultWrapper(list); // 返回输入输出列表
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResultWrapper(500,"测试文件获取失败");
    }
}

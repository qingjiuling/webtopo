package com.net.webtopo.controller;

import com.alibaba.fastjson.JSON;
import com.net.webtopo.util.JsonUtils;
import com.net.webtopo.util.ResultWrapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
public class ConfigFileController {

    /**
     * add config file
     *
     * @param
     * @return
     */
    @PostMapping("/config")
    public ResultWrapper addConfigFile(@RequestBody MultipartFile file) {
        try  {
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream("src/main/resources/File/config_file.json")
            );
            out.write(file.getBytes());
            out.flush();
            out.close();
            return new ResultWrapper("执行成功");
        } catch (FileNotFoundException e) {
            return new ResultWrapper(500, "失败");
        } catch (IOException e) {
            return new ResultWrapper(500, "失败");
        }
    }

    /**
     * view config file
     * @return
     */
    @GetMapping("/config")
    public ResultWrapper viewConfigFile() {
//        Path path = Paths.get("src/main/resources/File/config_file.json");
//        byte[] data = new byte[0];
//        try {
//            data = Files.readAllBytes(path);
//        } catch (IOException e) {
//            return new ResultWrapper(500,"文件读取失败");
//        }
//        try {
//            String result = new String(data, "utf-8");
//            System.out.println(result);
//            return new ResultWrapper(result);
//        } catch (UnsupportedEncodingException e) {
//            return new ResultWrapper(500,"文件读取失败");
//        }
        // 把json转成String
        String content = JsonUtils.readJsonFile("src/main/resources/File/config_file.json");
        JSONObject jsonObject = JSONObject.fromObject(content);
        JSONArray array = new JSONArray();
        array.add(jsonObject);
        String res = array.toString();
        res = res.substring(1,res.length()-1);
        return new ResultWrapper(res);
    }

    /**
     * update config file
     * @param
     * @return
     */
    @PutMapping("/config")
    public ResultWrapper updateConfigFile(@RequestParam("context") String context) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(String.format("src/main/resources/File/config_file.json"))
            );
            out.write(context.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            return new ResultWrapper("执行成功");
        } catch (FileNotFoundException e) {
            return new ResultWrapper(500,"失败");
        } catch (IOException e) {
            return new ResultWrapper(500,"失败");
        }
    }
}

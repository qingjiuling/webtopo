package com.net.webtopo.controller;

import com.net.webtopo.util.ResultWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    public ResultWrapper addConfigFile(@RequestParam("id") String id, @RequestBody MultipartFile file) {
        try  {
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream("src/main/resources/File/config_file.json")
            );
            out.write(file.getBytes());
            out.flush();
            out.close();
            return new ResultWrapper();
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
    @GetMapping("/config/{id}")
    public ResultWrapper viewConfigFile(@PathVariable String id) {
        Path path = Paths.get("src/main/resources/File/config_file.json");
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            return new ResultWrapper(500,"文件读取失败");
        }
        try {
            String result = new String(data, "utf-8");
            System.out.println(result);
            return new ResultWrapper(result);
        } catch (UnsupportedEncodingException e) {
            return new ResultWrapper(500,"文件读取失败");
        }

    }

    /**
     * update config file
     * @param
     * @return
     */
    @PutMapping("/config/{id}")
    public ResultWrapper updateConfigFile(@RequestParam("context") String context, @PathVariable("id") String id) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(String.format("src/main/resources/File/config_file.json"))
            );
            out.write(context.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            return new ResultWrapper();
        } catch (FileNotFoundException e) {
            return new ResultWrapper(500,"失败");
        } catch (IOException e) {
            return new ResultWrapper(500,"失败");
        }
    }
}

package com.net.webtopo.controller;

import com.net.webtopo.util.JsonUtils;
import com.net.webtopo.util.ResultWrapper;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
public class TestFileController {

    @PostMapping("/test")
    public ResultWrapper addTestFile(@RequestBody MultipartFile file) {
        try  {
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream("src/main/resources/File/test.json")
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

    @GetMapping("/test")
    public ResultWrapper viewTestFile() {
        Path path = Paths.get("src/main/resources/File/test.json");
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
//        String content = JsonUtils.readJsonFile("src/main/resources/File/test.json");
//        JSONObject jsonObject = JSONObject.fromObject(content);
//        return new ResultWrapper(jsonObject.get(id));
    }

    @PutMapping("/test")
    public ResultWrapper updateTestFile(@RequestParam("context") String context) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream("src/main/resources/File/test.json")
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

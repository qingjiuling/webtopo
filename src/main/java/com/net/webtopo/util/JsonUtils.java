package com.net.webtopo.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtils {
    /**
     * 读取json文件
     */
    /*public static void main(String[] args) {
        String path = "src/main/webapp/json/bottomFgs.json";
        String jsonStr = JsonUtils.readJsonFile(path);

        JSONArray jsonArray = JSONArray.fromObject(jsonStr);

        JSONObject nbnewjson = new JSONObject();
        nbnewjson.accumulate("have", "0");

        JSONObject newjson = new JSONObject();
        newjson.accumulate("id", newModelid);
        newjson.accumulate("state", "closed");
        newjson.accumulate("pId", "md001");
        newjson.accumulate("text", addname);
        newjson.accumulate("attributes", nbnewjson);

        jsonArray.add(newjson);
        //将json转换为json字符串
        String newJsonString = jsonArray.toString();

        JsonUtils.writeJsonFile(newJsonString, path);
    }*/
    public static String readJsonFile(String path){
        String laststrJson = "";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File(path)));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                laststrJson = laststrJson + tempString;
                line++;
            }
            reader.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return laststrJson;
    }

    /**
     * 写出json文件
     */
    public static void writeJsonFile(String newJsonString, String path){
        try {
            FileWriter fw = new FileWriter(path);
            PrintWriter out = new PrintWriter(fw);
            out.write(newJsonString);
            out.println();
            fw.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

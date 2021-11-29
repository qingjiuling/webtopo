package com.net.webtopo.util;

import com.net.webtopo.pojo.Cable;
import com.net.webtopo.pojo.RouterIntf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import lombok.Data;

@Data
public class ReadInfo {

    public List<RouterIntf> getRouter(String routerId) {

        Path path = Paths.get("src/main/resources/file/configInfo.json");
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String object = null;
        try {
            object = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //System.out.println(result);
        JSONObject jsonObj;
        jsonObj = JSONObject.fromObject(object);
        String[] inf ={"s0","s1","g0","g1"};
        List<RouterIntf> list = new ArrayList<>();
        for(int i=0;i<4;i++) {
            String ip;
            String itf;
            String mask;
            String status;

            ip= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(routerId)).get(inf[i])).get("ip"));
            itf= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(routerId)).get(inf[i])).get("interface"));
            mask= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(routerId)).get(inf[i])).get("mask"));
            status= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(routerId)).get(inf[i])).get("status"));

            RouterIntf rf =new RouterIntf(ip,itf,mask,status);
            list.add(rf);
            //System.out.println(list);
        }
        return list;
    }

    public Cable getCable(String cableId) {

        Path path = Paths.get("src/main/resources/file/configInfo.json");
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String object = null;
        try {
            object = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //System.out.println(result);
        JSONObject jsonObj;
        jsonObj = JSONObject.fromObject(object);

        //左右两边路由信息
        String leftRouter = String.valueOf(((JSONObject)((JSONObject)jsonObj.get(cableId)).get("left")).get("router"));
        String leftInf = String.valueOf(((JSONObject)((JSONObject)jsonObj.get(cableId)).get("left")).get("interface"));
        String rightRouter = String.valueOf(((JSONObject)((JSONObject)jsonObj.get(cableId)).get("right")).get("router"));
        String rightInf = String.valueOf(((JSONObject)((JSONObject)jsonObj.get(cableId)).get("right")).get("interface"));
        String leftSta = null;
        String rightSta = null;
        String cableSta = "0";

        //左边路由

        String ip;
        String itf;
        String mask;
        String status;

        ip= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(leftRouter)).get(leftInf)).get("ip"));
        itf= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(leftRouter)).get(leftInf)).get("interface"));
        mask= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(leftRouter)).get(leftInf)).get("mask"));
        status= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(leftRouter)).get(leftInf)).get("status"));
        leftSta =status;

        RouterIntf rfl =new RouterIntf(ip,itf,mask,status);

        //右边路由

        ip= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("ip"));
        itf= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("interface"));
        mask= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("mask"));
        status= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("status"));
        rightSta =status;

        RouterIntf rfr =new RouterIntf(ip,itf,mask,status);

        if((leftRouter != null)&&(leftInf != null)&&(rightRouter != null)&&(rightInf != null)&&(leftSta == "1")&&(rightSta =="1")) {
            cableSta = "1";
        }

        Cable cb =new Cable(Integer.valueOf(cableSta),rfl,rfr);
        return cb;
    }
}

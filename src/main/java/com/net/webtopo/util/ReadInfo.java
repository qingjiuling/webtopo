package com.net.webtopo.util;

import com.net.webtopo.pojo.Cable;
import com.net.webtopo.pojo.Router;
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

    public Router getRouter(String routerId) {

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
        String[] inf ={"s0","s1","f0","f1","lo0","lo1","lo2","lo3","lo4","lo5"};
        List<RouterIntf> list = new ArrayList<>();
        for(int i=0;i<10;i++) {
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
        }
        Router rt = new Router(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7), list.get(8), list.get(9));
        return rt;
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
        String rightRouter = String.valueOf(((JSONObject)((JSONObject)jsonObj.get(cableId)).get("right")).get("router"));
        String leftInf = String.valueOf(((JSONObject)((JSONObject)jsonObj.get(cableId)).get("left")).get("interface"));
        String rightInf = String.valueOf(((JSONObject)((JSONObject)jsonObj.get(cableId)).get("right")).get("interface"));
        String leftSta = null;
        String rightSta = null;
        String cableSta = "0";
        String err = "未配置";

        Cable cb;

        RouterIntf rfl, rfr;

        if((leftRouter != "null") && (rightRouter != "null")) {
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

            rfl =new RouterIntf(ip,itf,mask,status);

            //右边路由
            ip= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("ip"));
            itf= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("interface"));
            mask= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("mask"));
            status= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("status"));
            rightSta =status;

            rfr =new RouterIntf(ip,itf,mask,status);

            if((leftInf != "null")&&(rightInf != "null")&&(leftSta.equals("1"))&&(rightSta.equals("1"))) {
                cableSta = "1";
            }
            cb =new Cable(cableSta,rfl,rfr);
        }else if(leftRouter == "null" && rightRouter != "null") {
            rfl =new RouterIntf(err,err,err,err);

            String ip;
            String itf;
            String mask;
            String status;

            //右边路由
            ip= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("ip"));
            itf= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("interface"));
            mask= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("mask"));
            status= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(rightRouter)).get(rightInf)).get("status"));

            rfr =new RouterIntf(ip,itf,mask,status);

            cb =new Cable(cableSta,rfl,rfr);
        }else if(rightRouter == "null" && leftRouter != "null") {
            rfr =new RouterIntf(err,err,err,err);

            String ip;
            String itf;
            String mask;
            String status;

            //左边路由
            ip= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(leftRouter)).get(leftInf)).get("ip"));
            itf= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(leftRouter)).get(leftInf)).get("interface"));
            mask= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(leftRouter)).get(leftInf)).get("mask"));
            status= String.valueOf(((JSONObject)((JSONObject)jsonObj.get(leftRouter)).get(leftInf)).get("status"));

            rfl =new RouterIntf(ip,itf,mask,status);

            cb =new Cable(cableSta,rfl,rfr);
        }else {
            rfl =new RouterIntf(err,err,err,err);
            rfr =new RouterIntf(err,err,err,err);
            cb =new Cable(cableSta,rfl,rfr);
        }
        return cb;
    }
}

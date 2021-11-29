package com.net.webtopo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

//路由的接口

public class RouterIntf {

    private String ip;
    private String itf;
    private String mask;
    private String status;

    public RouterIntf(String ip, String itf, String mask, String status) {
        this.ip = ip;
        this.itf = itf;
        this.mask = mask;
        this.status = status;
    }

}

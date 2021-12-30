package com.net.webtopo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class Router {

    private RouterIntf s0;
    private RouterIntf s1;
    private RouterIntf f0;
    private RouterIntf f1;
    private RouterIntf lo0;
    private RouterIntf lo1;
    private RouterIntf lo2;
    private RouterIntf lo3;
    private RouterIntf lo4;
    private RouterIntf lo5;

    public Router(RouterIntf s0, RouterIntf s1, RouterIntf f0, RouterIntf f1, RouterIntf lo0,
                  RouterIntf lo1, RouterIntf lo2, RouterIntf lo3, RouterIntf lo4, RouterIntf lo5) {
        this.s0 = s0;
        this.s1 = s1;
        this.f0 = f0;
        this.f1 = f1;
        this.lo0 = lo0;
        this.lo1 = lo1;
        this.lo2 = lo2;
        this.lo3 = lo3;
        this.lo4 = lo4;
        this.lo5 = lo5;
    }

}

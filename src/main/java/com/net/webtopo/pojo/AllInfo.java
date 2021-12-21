package com.net.webtopo.pojo;


import java.util.List;

public class AllInfo {

    private List<RouterIntf> router1;
    private List<RouterIntf> router2;
    private List<RouterIntf> router3;
    private Cable cable1;
    private Cable cable2;

    public AllInfo (List<RouterIntf> router1, List<RouterIntf> router2, List<RouterIntf> router3, Cable cable1, Cable cable2) {
        this.router1 = router1;
        this.router2 = router2;
        this.router3 = router3;
        this.cable1 = cable1;
        this.cable2 = cable2;
    }
}

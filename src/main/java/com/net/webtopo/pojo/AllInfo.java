package com.net.webtopo.pojo;


import lombok.Data;

import java.util.List;

@Data
public class AllInfo {

    private Router router1;
    private Router router2;
    private Router router3;
    private Cable cable1;
    private Cable cable2;

    public AllInfo (Router router1, Router router2, Router router3, Cable cable1, Cable cable2) {
        this.router1 = router1;
        this.router2 = router2;
        this.router3 = router3;
        this.cable1 = cable1;
        this.cable2 = cable2;
    }
}

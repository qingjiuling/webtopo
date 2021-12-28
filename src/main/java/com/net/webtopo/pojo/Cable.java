package com.net.webtopo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Cable {

    private String status;
    private RouterIntf left;
    private RouterIntf right;

    public Cable(String status, RouterIntf left, RouterIntf right) {
        this.status = status;
        this.left = left;
        this.right = right;
    }

}

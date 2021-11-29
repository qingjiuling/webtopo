package com.net.webtopo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Cable {

    private int status;
    private RouterIntf left;
    private RouterIntf right;

    public Cable(int status, RouterIntf left, RouterIntf right) {
        this.status = status;
        this.left = left;
        this.right = right;
    }

}

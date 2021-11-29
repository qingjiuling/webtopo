package com.net.webtopo.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultWrapper implements Serializable {
    /**
     * 正常情况下 code 为 200
     */
    private Integer code;
    private Object data;
    private String msg;

    public ResultWrapper(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    /**
     * 正常情况下带有数据的返回结果
     * @param data 传入的数据
     */
    public ResultWrapper(Object data) {
        this(200, data, "");
    }

    /**
     * 正常情况下仅返回成功状态
     */
    public ResultWrapper() {
        this(200, null, "");
    }

    /**
     * 异常情况下，返回错误信息
     * @param code 错误代码
     * @param msg 错误信息
     */
    public ResultWrapper(Integer code, String msg) {
        this(500, null, msg);
    }
}

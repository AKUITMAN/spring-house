package com.qf.pojo.resp;

import lombok.Data;

@Data
public class BaseResp {

    private Integer code;

    private String message;

    private Object data;

    private long total;
}

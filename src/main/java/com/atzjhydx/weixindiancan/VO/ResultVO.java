package com.atzjhydx.weixindiancan.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -7857031810165288368L;
    //错误码
    private Integer code;
    //提示信息
    private String msg;
    //返回的具体内容
    private T data;


}

package com.matrix.proxy.module;


/**
 * @ClassName Message
 * @Author QIANGLU
 * @Date 2020/3/23 6:24 下午
 * @Version 1.0
 */
public class Message {

    private Integer code;

    private String body;


    public Message(Integer code, String body) {
        this.code = code;
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}

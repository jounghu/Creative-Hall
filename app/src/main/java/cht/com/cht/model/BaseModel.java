package cht.com.cht.model;

import java.io.Serializable;

public class BaseModel<T> implements Serializable {
    public String code;
    public String msg;

    public T data;


    public boolean success() {
        return code.equals("1");
    }


    @Override
    public String toString() {
        return "BaseModel{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
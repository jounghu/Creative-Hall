package cht.com.cht.model;

import java.io.Serializable;

/**
 * msg 1代表用户存在
 * 0 代表用户不存在
 */
public class Message implements Serializable {


    private static final long serialVersionUID = -2465942508591131780L;
    private int msg;

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public Message(int msg) {
        super();
        this.msg = msg;
    }

    public Message() {
        super();
    }

    @Override
    public String toString() {
        return "Message [msg=" + msg + "]";
    }

}

package cht.com.cht.model;

import java.io.Serializable;

/**
 *
 * 校外bean
 *
 * Created by Administrator on 2017/2/20.
 */
public class OutDoor implements Serializable{
    private String title;
    private String content;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "OutDoor{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public OutDoor() {
    }

    public OutDoor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {

        if(this == o) return  true;


        if (o == null || getClass() != o.getClass()) return false;
        OutDoor outDoor = (OutDoor) o;
        return outDoor.getTitle().equals(title) && outDoor.getContent().equals(content);
    }
}

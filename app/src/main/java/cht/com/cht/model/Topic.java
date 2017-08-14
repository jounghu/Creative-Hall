package cht.com.cht.model;

import java.sql.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/27.
 */
public class Topic {

    private int id;
    private String headImag;
    private String nickName;
    private String titie;
    private String content;
    private Date date;
    private int zan;
    private int comment;

    public Topic(int id, String headImag, String nickName, String titie, String content, Date date, int zan, int comment, List<String> imagPath) {
        this.id = id;
        this.headImag = headImag;
        this.nickName = nickName;
        this.titie = titie;
        this.content = content;
        this.date = date;
        this.zan = zan;
        this.comment = comment;
        this.imagPath = imagPath;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", headImag='" + headImag + '\'' +
                ", nickName='" + nickName + '\'' +
                ", titie='" + titie + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", zan=" + zan +
                ", comment=" + comment +
                ", imagPath=" + imagPath +
                '}';
    }

    public int getComment() {
        return comment;
    }

    private List<String> imagPath;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadImag() {
        return headImag;
    }

    public void setHeadImag(String headImag) {
        this.headImag = headImag;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTitie() {
        return titie;
    }

    public void setTitie(String titie) {
        this.titie = titie;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public List<String> getImagPath() {
        return imagPath;
    }

    public void setImagPath(List<String> imagPath) {
        this.imagPath = imagPath;
    }

    public Topic() {
    }


    public void setComment(int comment) {
        this.comment = comment;
    }
}

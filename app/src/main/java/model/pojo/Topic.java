package model.pojo;


import com.tt.findyou.utils.TimeUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TT on 2016/12/8.
 */
public class Topic implements Serializable{
    private Integer id;
    private String title;
    private String content;
    private UserInfo userInfo;
    private String pubTime;
    private List<TopicImage> imageList;
    private Integer type;
    private String targetTruename;
    private Integer targetSex;
    private List<Experience> expList;

    public String getFormatPubTime(){
        return TimeUtils.convertTimeToFormat(this.pubTime);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public List<TopicImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<TopicImage> imageList) {
        this.imageList = imageList;
    }

    public List<Experience> getExpList() {
        return expList;
    }

    public void setExpList(List<Experience> expList) {
        this.expList = expList;
    }

    public Integer getTargetSex() {
        return targetSex;
    }

    public void setTargetSex(Integer targetSex) {
        this.targetSex = targetSex;
    }

    public String getTargetTruename() {
        return targetTruename;
    }

    public void setTargetTruename(String targetTruename) {
        this.targetTruename = targetTruename;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

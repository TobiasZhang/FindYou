package model.pojo;

import com.tt.findyou.utils.TimeUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/19 0019.
 */
public class Tip implements Serializable {
    private Integer id;
    private UserInfo userInfo;
    private String content;
    private String pubTime;
    public String getFormatPubTime(){
        return TimeUtils.convertTimeToFormat(this.pubTime);
    }

    public Tip(Integer id, UserInfo userInfo, String content, String pubTime) {
        this.id = id;
        this.userInfo = userInfo;
        this.content = content;
        this.pubTime = pubTime;
    }

    public Tip() {

    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }
}

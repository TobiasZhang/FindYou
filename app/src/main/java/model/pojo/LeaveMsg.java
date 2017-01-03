package model.pojo;


import com.tt.findyou.utils.TimeUtils;

import java.io.Serializable;

/**
 * Created by TT on 2016/12/8.
 */
public class LeaveMsg implements Serializable {
    private Integer id;
    private String content;
    private UserInfo toUser;
    private UserInfo fromUser;
    private String pubTime;

    public String getFormatPubTime(){
        return TimeUtils.convertTimeToFormat(this.pubTime);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserInfo getToUser() {
        return toUser;
    }

    public void setToUser(UserInfo toUser) {
        this.toUser = toUser;
    }

    public UserInfo getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserInfo fromUser) {
        this.fromUser = fromUser;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }
}

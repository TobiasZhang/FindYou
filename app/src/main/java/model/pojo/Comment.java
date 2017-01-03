package model.pojo;


import com.tt.findyou.utils.TimeUtils;

/**
 * Created by TT on 2016/12/8.
 */
public class Comment {
    private Integer id;
    private String content;
    private UserInfo userInfo;
    private Topic topic;
    private Comment comment;
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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }
}

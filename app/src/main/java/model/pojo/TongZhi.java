package model.pojo;

import com.tt.findyou.utils.TimeUtils;

import java.io.Serializable;

/**
 * Created by TT on 2016/12/10.
 */
public class TongZhi implements Serializable {
    private Integer id;
    private String content;
    private String pubTime;
    private String userTruename;
    private String userHeadImage;
    private int type;//1回复主题帖 2回复回复帖 3留言
    //回复
    private String topicTitle;
    private Integer topicId;

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

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getUserTruename() {
        return userTruename;
    }

    public void setUserTruename(String userTruename) {
        this.userTruename = userTruename;
    }

    public String getUserHeadImage() {
        return userHeadImage;
    }

    public void setUserHeadImage(String userHeadImage) {
        this.userHeadImage = userHeadImage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }
}

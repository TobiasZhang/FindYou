package model.pojo;

import com.tt.findyou.utils.TimeUtils;

import java.io.Serializable;
import java.util.List;

import utils.HttpUtils;

/**
 * Created by TT on 2016/12/10.
 */
public class TuiJian implements Serializable {
    private Integer id;
    private Integer type;
    private String nickname;
    private String headImage;
    private String birth;
    private Integer sex;
    private String targetTruename;
    private Integer targetSex;
    private List<TopicImage> topicImageList;
    private District district;
    private String truename;
    //主题帖
    private String title;
    private String content;
    private String pubTime;
    //用户 同一地点
    private Experience experience;
    //用户 共同爱好
    private List<Hobby> hobbyList;

    public String getFormatPubTime(){
        return TimeUtils.convertTimeToFormat(this.pubTime);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImage() {
        return HttpUtils.BASE_URL_IMAGE_USER_HEAD+headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
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

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public List<Hobby> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<Hobby> hobbyList) {
        this.hobbyList = hobbyList;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getTargetTruename() {
        return targetTruename;
    }

    public void setTargetTruename(String targetTruename) {
        this.targetTruename = targetTruename;
    }

    public Integer getTargetSex() {
        return targetSex;
    }

    public void setTargetSex(Integer targetSex) {
        this.targetSex = targetSex;
    }

    public List<TopicImage> getTopicImageList() {
        return topicImageList;
    }

    public void setTopicImageList(List<TopicImage> topicImageList) {
        this.topicImageList = topicImageList;
    }
}

package model.pojo;



import java.io.Serializable;

import utils.HttpUtils;

/**
 * Created by TT on 2016/12/7.
 */
public class UserImage implements Serializable {
    private Integer id;
    private String url;
    private UserInfo userInfo;

    public UserImage(Integer id, String url, UserInfo userInfo) {
        this.id = id;
        this.url = url;
        this.userInfo = userInfo;
    }

    public UserImage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return HttpUtils.BASE_URL_IMAGE_USER+url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}

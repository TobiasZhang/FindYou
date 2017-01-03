package model.pojo;



import java.io.Serializable;
import java.util.Date;
import java.util.List;

import utils.EmptyUtil;
import utils.HttpUtils;

public class UserInfo implements Serializable {
	private Integer id;
	private String tel;
	private String password;
	private String nickname;
	private String birth;
	private String headImage;
	private List<UserImage> imageList;
	private District district;
	private Integer sex;
	private List<Experience> expList;
	private List<Hobby> hobbyList;
	private List<UserInfo> friendList;
	private Integer isFriend;
	private String truename;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getHeadImage() {
		return EmptyUtil.isEmpty(headImage)?null:HttpUtils.BASE_URL_IMAGE_USER_HEAD+headImage
				;
	}
	public String getHeadName(){
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public List<UserImage> getImageList() {
		return imageList;
	}

	public void setImageList(List<UserImage> imageList) {
		this.imageList = imageList;
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

	public List<Experience> getExpList() {
		return expList;
	}

	public void setExpList(List<Experience> expList) {
		this.expList = expList;
	}

	public List<Hobby> getHobbyList() {
		return hobbyList;
	}

	public void setHobbyList(List<Hobby> hobbyList) {
		this.hobbyList = hobbyList;
	}

	public List<UserInfo> getFriendList() {
		return friendList;
	}

	public void setFriendList(List<UserInfo> friendList) {
		this.friendList = friendList;
	}

	public Integer getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(Integer isFriend) {
		this.isFriend = isFriend;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}
}

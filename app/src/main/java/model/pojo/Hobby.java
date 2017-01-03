package model.pojo;


import java.io.Serializable;
import java.util.List;

/**
 * Created by TT on 2016/12/9.
 */
public class Hobby implements Serializable{
    private Integer id;
    private String name;
    private Hobby hobby;
    private Integer isDefault;
    private List<Hobby> subList;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hobby getHobby() {
        return hobby;
    }

    public void setHobby(Hobby hobby) {
        this.hobby = hobby;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public List<Hobby> getSubList() {
        return subList;
    }

    public void setSubList(List<Hobby> subList) {
        this.subList = subList;
    }
}

package model.pojo;

import java.io.Serializable;

/**
 * Created by TT on 2016/12/8.
 */
public class District implements Serializable {
    private Integer id;
    private String name;
    private Integer level;
    private District district;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}

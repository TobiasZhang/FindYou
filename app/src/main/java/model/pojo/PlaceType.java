package model.pojo;


import java.io.Serializable;

/**
 * Created by TT on 2016/12/9.
 */
public class PlaceType implements Serializable{
    private Integer id;
    private String name;
    private PlaceType placeType;

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
}

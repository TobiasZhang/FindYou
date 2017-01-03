package model.pojo;



import java.io.Serializable;

import utils.HttpUtils;

/**
 * Created by TT on 2016/12/7.
 */
public class TopicImage implements Serializable {
    private Integer id;
    private String url;
    private Topic topic;
    private String description;

    public TopicImage() {
    }

    public TopicImage(Integer id, String url, Topic topic, String description) {

        this.id = id;
        this.url = url;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return HttpUtils.BASE_URL_IMAGE_TOPIC+url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

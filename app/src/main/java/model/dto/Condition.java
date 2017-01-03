package model.dto;


/**
 * Created by TT on 2016/12/7.
 */

public class Condition {
    private int pid = 1;
    private int pageSize = 10;
    private int startWith;
    private int maxPid;
    private int max;
    private Integer uid;
    private Integer tid;



    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getMaxPid() {
        return maxPid;
    }

    public void setMaxPid(int maxPid) {
        this.maxPid = maxPid;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getStartWith() {
        return (pid-1)*pageSize;
    }

    public void setStartWith(int startWith) {
        this.startWith = startWith;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }
}

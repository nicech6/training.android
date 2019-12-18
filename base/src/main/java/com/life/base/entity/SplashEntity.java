package com.life.base.entity;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/18
 * @email: nicech6@163.com
 */
public class SplashEntity {

    /**
     * id : 206
     * type : 4
     * animate : 1
     * duration : 3
     * start_time : 1480058707
     * end_time : 1480490708
     * thumb : http://i0.hdslb.com/bfs/archive/29204aa43c1ed90ab5c33ecf8003753617c52823.png
     * hash : 8ff78e25ea4427baba4ce82c298e22aa
     * times : 1
     * skip : 0
     * uri :
     */

    private int id;
    private int type;
    private int animate;
    private int duration;
    private int start_time;
    private int end_time;
    private String thumb;
    private String hash;
    private int times;
    private int skip;
    private String uri;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAnimate() {
        return animate;
    }

    public void setAnimate(int animate) {
        this.animate = animate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

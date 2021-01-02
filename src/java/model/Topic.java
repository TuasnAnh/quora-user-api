/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author ADMIN
 */
public class Topic {

    private int tid;
    private String topicName;
    private String topicImageUrl;
    private int follower;
    private boolean isFollowed;

    public Topic(int tid, String topicName, String topicImageUrl, int follower) {
        this.tid = tid;
        this.topicName = topicName;
        this.topicImageUrl = topicImageUrl;
        this.follower = follower;
    }

    public Topic(int tid, String topicName, String topicImageUrl, int follower, boolean isFollowed) {
        this.tid = tid;
        this.topicName = topicName;
        this.topicImageUrl = topicImageUrl;
        this.follower = follower;
        this.isFollowed = isFollowed;
    }

    public Topic(String topicName, String topicImageUrl) {
        this.topicName = topicName;
        this.topicImageUrl = topicImageUrl;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicImageUrl() {
        return topicImageUrl;
    }

    public void setTopicImageUrl(String topicImageUrl) {
        this.topicImageUrl = topicImageUrl;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public boolean isIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author ADMIN
 */
public class Question {

    private int questionId;
    private int userId;
    private int topicId;
    private String content;
    private Timestamp time;
    private int totalAnswer;
    private String topicName;

    public Question(int questionId, int userId, int topicId, String content, Timestamp time, int totalAnswer) {
        this.questionId = questionId;
        this.userId = userId;
        this.topicId = topicId;
        this.content = content;
        this.time = time;
        this.totalAnswer = totalAnswer;
    }

    public Question(int questionId, int userId, int topicId, String content, Timestamp time, int totalAnswer, String topicName) {
        this.questionId = questionId;
        this.userId = userId;
        this.topicId = topicId;
        this.content = content;
        this.time = time;
        this.totalAnswer = totalAnswer;
        this.topicName = topicName;
    }

    public Question(int questionId, int topicId, String content, Timestamp time, int totalAnswer, String topicName) {
        this.questionId = questionId;
        this.topicId = topicId;
        this.content = content;
        this.time = time;
        this.totalAnswer = totalAnswer;
        this.topicName = topicName;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getTotalAnswer() {
        return totalAnswer;
    }

    public void setTotalAnswer(int totalAnswer) {
        this.totalAnswer = totalAnswer;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

}

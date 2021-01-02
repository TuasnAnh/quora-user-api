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
public class Answer {
    private int answerId;
    private int authorId;
    private String time;
    private String authorCredential;
    private String question;
    private int questionId;
    private String content;
    private int upvote;
    private int downvote;
    private boolean isUpvote;
    private boolean isDownvote;
    private boolean isBookmarked;
    private int topicId;
    private String topicName;
    private String url;
    private String authorName;

    public Answer(int answerId, int authorId, String time, String authorCredential, String question, int questionId, String content, int upvote, int downvote, boolean isUpvote, boolean isDownvote) {
        this.answerId = answerId;
        this.authorId = authorId;
        this.time = time;
        this.authorCredential = authorCredential;
        this.question = question;
        this.questionId = questionId;
        this.content = content;
        this.upvote = upvote;
        this.downvote = downvote;
        this.isUpvote = isUpvote;
        this.isDownvote = isDownvote;
    }

    public Answer(int answerId, int authorId, String time, String authorCredential, int questionId, String content, int upvote, int downvote, int topicId, String topicName, String url, String authorName) {
        this.answerId = answerId;
        this.authorId = authorId;
        this.time = time;
        this.authorCredential = authorCredential;
        this.questionId = questionId;
        this.content = content;
        this.upvote = upvote;
        this.downvote = downvote;
        this.topicId = topicId;
        this.topicName = topicName;
        this.url = url;
        this.authorName = authorName;
    }
    
    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthorCredential() {
        return authorCredential;
    }

    public void setAuthorCredential(String authorCredential) {
        this.authorCredential = authorCredential;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }

    public boolean isIsUpvote() {
        return isUpvote;
    }

    public void setIsUpvote(boolean isUpvote) {
        this.isUpvote = isUpvote;
    }

    public boolean isIsDownvote() {
        return isDownvote;
    }

    public void setIsDownvote(boolean isDownvote) {
        this.isDownvote = isDownvote;
    }

    public boolean isIsBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
}

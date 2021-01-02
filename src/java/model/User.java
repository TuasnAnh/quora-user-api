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
public class User {

    private int uid;
    private String firstName;
    private String lastName;
    private String description;
    private String credential;
    private String school;
    private String degreeType;
    private String graduationYear;
    private String location;
    private String roll;
    private String url;
    private int totalAnswer;
    private int totalQuestion;
    private int totalBookmark;

    private String email;
    private String password;
    private String loginStatus;

    public User(int uid, String email, String firstName, String lastName, String description, String credential, String school, String degreeType, String graduationYear, String location, String url) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.credential = credential;
        this.school = school;
        this.degreeType = degreeType;
        this.graduationYear = graduationYear;
        this.location = location;
        this.url = url;
        this.uid = uid;
    }

    public User(int uid, String firstName, String lastName, String description, String credential, String school, String degreeType, String graduationYear, String location, String roll) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.credential = credential;
        this.school = school;
        this.degreeType = degreeType;
        this.graduationYear = graduationYear;
        this.location = location;
        this.roll = roll;
    }
    
    public User(String loginStatus) {
        this.loginStatus = loginStatus;
    }
    
    public User(int uid, String email, String roll, String loginStatus) {
        this.uid = uid;
        this.email = email;
        this.roll = roll;
        this.loginStatus = loginStatus;
    }
    
    public User(int uid, String roll) {
        this.uid = uid;
        this.roll = roll;
    }

    public int getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDescription() {
        return description;
    }

    public String getCredential() {
        return credential;
    }

    public String getSchool() {
        return school;
    }

    public String getDegreeType() {
        return degreeType;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public String getLocation() {
        return location;
    }

    public String getRoll() {
        return roll;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalAnswer() {
        return totalAnswer;
    }

    public void setTotalAnswer(int totalAnswer) {
        this.totalAnswer = totalAnswer;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public int getTotalBookmark() {
        return totalBookmark;
    }

    public void setTotalBookmark(int totalBookmark) {
        this.totalBookmark = totalBookmark;
    }

    
    
}

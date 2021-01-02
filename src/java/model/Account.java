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
public class Account {
    private int aid;
    private int uid;
    private String email;
    private String password;
    private String loginStatus;

    public Account(int uid, String email, String password, String loginStatus) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.loginStatus = loginStatus;
    }
    
    public Account(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public int getAid() {
        return aid;
    }

    public int getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLoginStatus() {
        return loginStatus;
    }
    
}

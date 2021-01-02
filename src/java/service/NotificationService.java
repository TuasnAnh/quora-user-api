/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import model.Notification;

/**
 *
 * @author ADMIN
 */
public interface NotificationService {

    public boolean addVoteNotification(int userId, String type, int answerId);

    public boolean addNewAnswerNotice(int userId, String type, int questionId);

    public boolean removeNotification(int notiId);

    public boolean markAsSeen(int userId, String type);

    public int getTotalUnseenNotification(int userId);

    public List<Notification> getNotification(int userId, String type);

}

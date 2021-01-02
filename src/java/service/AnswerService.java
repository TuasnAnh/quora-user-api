/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import java.util.Map;
import model.Answer;

/**
 *
 * @author ADMIN
 */
public interface AnswerService {
    public int getAnswerAuthor(int answerId);

    public boolean addAnswer(int userId, int questionId, String content);

    public Map<String, String> setVote(int userId, int answerId, String relationship);

    public List<Answer> getAnswerInQuestion(int questionId, int userId, int lastId);

    public List<Answer> getUserAnswer(int userId, int lastId, int currentUser);

    public List<Answer> getTopicAnswer(int topicId, int userId, int lastId);

    public List<Answer> getSuggestAnswer(int userId, int lastId);

}

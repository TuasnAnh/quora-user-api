/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import model.Question;

/**
 *
 * @author ADMIN
 */
public interface QuestionService {
    public int getQuestionAuthor(int questionId);
    public boolean addQuestion(int userId, int topicId, String content);
    public Question getQuestion(int questionId);
    public List<Question> getUserQuestion(int questionid, int lastId);
    public List<Question> getTopicQuestion(int topicId, int lastId);
    public List<Question> getSuggestQuestion(int userId, int lastId);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import java.util.Map;
import model.Topic;

/**
 *
 * @author ADMIN
 */
public interface TopicService {

    public List<Topic> getFollowedTopic(int userId);

    public Map<String, String> followTopic(int userId, int topicId);

    public List<Topic> getSuggestTopic(int userId);

    public Topic getTopicInfor(int userId, int topicId);

}

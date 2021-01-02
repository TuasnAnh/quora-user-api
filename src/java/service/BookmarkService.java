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
public interface BookmarkService {
    public Map<String, String> insertBookmark(int useId, int answerId);
    public List<Answer> getUserBookmark(int userId, int lastId);
}

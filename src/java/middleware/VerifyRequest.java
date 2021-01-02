/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import serviceImplement.AccountServiceImplement;

/**
 *
 * @author ADMIN
 */
public class VerifyRequest {

    public static boolean verifyUserRequest(HttpServletRequest request, HttpServletResponse response, AccountServiceImplement accountService) throws IOException {
        String roll = Authenticate.authenticate(request, response, accountService);
        if (roll != null && Authorization.userAuthor(response, roll)) {
            return true;
        }
        return false;
    }
}

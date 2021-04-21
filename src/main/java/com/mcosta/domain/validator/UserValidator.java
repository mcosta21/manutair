package com.mcosta.domain.validator;

import com.mcosta.domain.dao.UserDao;
import com.mcosta.domain.model.User;

public class UserValidator {

    private String message;

    public boolean isValid(User user) {

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            message = "Login não informado.";
            return false;
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            message = "Senha não informada.";
            return false;
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            message = "Nome não informado.";
            return false;
        }

        if (user.getUserType() == null) {
            message = "Tipo de Usuário não informado.";
            return false;
        }

        /*
        User alreadyExists = userDao.getUserByUsername(user.getUsername());
        if(alreadyExists != null) {
            message = "Login" + user.getUsername() + " já utilizado.";
            return false;
        }
        */
        return true;
    }

    public boolean isValidToLogin(String username, String password) {

        if (username == null || username.isEmpty()) {
            message = "Usuário não informado.";
            return false;
        }

        if (password == null || password.isEmpty()) {
            message = "Senha não informada.";
            return false;
        }

        return true;
    }

    public String getMessage() {
        return message;
    }
}

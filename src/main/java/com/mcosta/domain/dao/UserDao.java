package com.mcosta.domain.dao;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mcosta.domain.enumeration.UserTypeEnum;
import com.mcosta.domain.model.User;
import com.mcosta.domain.validator.UserValidator;
import com.thoughtworks.xstream.XStream;

public class UserDao implements Dao {
    
    private static Set<User> users = new HashSet<User>();
    private static String FILE_NAME = "database/users.xml";
    private UserValidator userValidator = new UserValidator();

    public UserDao() {
        index();
    }

    @Override
    public void create(Object object) throws Exception {
        User user = (User) object;
        if(!userValidator.isValid(user)) {
            throw new Exception(userValidator.getMessage());
        }

        if(user.getUserType() == UserTypeEnum.TECHNICIAN) {
            user.setTechnician(true);
        }
        user.setIdUser(Long.valueOf(index().size()+1));
        users.add(user);
        persist();

    }
    
    @Override
    public void update(Object object) throws Exception {
        User user = (User) object;

        if(user.getUserType() == UserTypeEnum.TECHNICIAN) {
            user.setTechnician(true);
        }
        else {
            user.setTechnician(false);
        }
        persist();
    }

    @Override
    public void delete(Object object) throws Exception{
        User user = (User) object;
        users.remove(user);
        persist();
    }

    @Override
    public List<User> index() {
        File file = new File(FILE_NAME);

        if(!file.exists()) return new ArrayList<User>();

        XStream xs = new XStream();
        users = (Set<User>) xs.fromXML(file);

        List<User> items = new ArrayList<>();
        for(User u : users){
            items.add(u);
        }

        return items;
    }

    public User getUserById(Long id){
        File file = new File(FILE_NAME);
        if(!file.exists()) return null;

        XStream xs = new XStream();
        for(User u : (Set<User>) xs.fromXML(file)){
            if(u.getIdUser().equals(id)){
                return u;
            }
        }
        return null;
    }

    public List<User> getUsersTechnician() {
        File file = new File(FILE_NAME);
        if(!file.exists()) return new ArrayList<User>();

        XStream xs = new XStream();
        List<User> items = new ArrayList<>();
        for(User u : (Set<User>) xs.fromXML(file)){
            if(u.isTechnician() && u.getUserType() == UserTypeEnum.TECHNICIAN) items.add(u);
        }

        return items;
    }

    private void persist() throws Exception {
        XStream xs = new XStream();
        String xml = xs.toXML(users);
        FileWriter fw = new FileWriter(FILE_NAME);
        fw.write(xml);
        fw.close();        
    }

    public User getUserByUsername(String username) {
        File file = new File(FILE_NAME);
        if(!file.exists()) return null;

        XStream xs = new XStream();
        for(User u : (Set<User>) xs.fromXML(file)){
            if(u.getUsername().toUpperCase().equals(username.toUpperCase())) {
                return u;
            }
        }
        return null;
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        File file = new File(FILE_NAME);
        if(!file.exists()) return null;

        XStream xs = new XStream();
        for(User u : (Set<User>) xs.fromXML(file)){
            if(u.getUsername().toUpperCase().equals(username.toUpperCase()) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}

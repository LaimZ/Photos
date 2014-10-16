package ru.ncedu.laimz.photos;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class UserHelper {
    private UserHelper() {};
    
    public static List<User> getUsers() throws DBException {
        //List<User> users = new LinkedList<User>();
        try {
            return PhotosDAO.getAllUsers();
        } catch (SQLException e) {
            throw new DBException("Exception while getting users", e);
        }
    }
    
    public static User getUser(String name) throws DBException {
        List<User> users = new LinkedList<User>();
        User user = null;
        try {
            users = PhotosDAO.getAllUsers();
            for (User a:users) {
                if (a.getName().equals(name)) {
                    user = a;
                    break;
                }
            }
            if (user != null) {
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DBException("Exception while getting user", e);
        }
    }
    
    
    
    
    
}

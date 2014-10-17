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
    
    public static List<User> getUserByName(String name) throws DBException {
        try {
            return PhotosDAO.getUserByName(name);
        } catch (SQLException e) {
            throw new DBException("Exception while getting user", e);
        }
    }

    public static User getUserById(long id) throws DBException {
        try {
            return PhotosDAO.getUserById(id);
        } catch (DBException e) {
            throw new DBException("Exception while getting user", e);
        }
    }
    
    
    
    
    
}

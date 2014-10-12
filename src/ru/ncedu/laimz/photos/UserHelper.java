package ru.ncedu.laimz.photos;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

class DBException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5237765559966597280L;
    public DBException() {super();}
    public DBException(String string) {super(string);}
    public DBException(String string, Exception e) {super(string, e);}
    
}

public class UserHelper {
    public User getUser(String name) throws DBException {
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
            // TODO Auto-generated catch block
            throw new DBException("while getting user", e);
        }
    }
}

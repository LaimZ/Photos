package ru.ncedu.laimz.photos;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import ru.ncedu.laimz.photos.model.Album;


public class AlbumHelper {
    private AlbumHelper() {};
    
    public static List<Album> getAlbums() throws DBException {
        //List<Album> albums = new LinkedList<Album>();
        try {
            return PhotosDAO.getAllAlbums();
        } catch (SQLException e) {
            throw new DBException("Exception while getting albums", e);
        }
    }
    
    public static List<Album> getUserAlbums(long user_id) throws DBException {
        try {
            return PhotosDAO.getUserAlbums(user_id);
        } catch (DBException e) {
            throw new DBException("Exception while getting user albums", e);
        }
    }
    
    public static List<Album> getUserAlbumByName(long user_id, String name) throws DBException {
        try {
            return PhotosDAO.getUserAlbumByName(user_id, name);
        } catch (DBException e) {
            throw new DBException("Exception while getting user album by name", e);
        }
    }

    public static Album getUserAlbumById(long user_id, long id) throws DBException {
        try {
            return PhotosDAO.getUserAlbumById(user_id, id);
        } catch (DBException e) {
            throw new DBException("Exception while getting user album by id", e);
        }
    }
    
    
    
    
    
}

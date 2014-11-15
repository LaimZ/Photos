package ru.ncedu.laimz.photos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import ru.ncedu.laimz.photos.model.Album;
import ru.ncedu.laimz.photos.model.Photo;
import ru.ncedu.laimz.photos.model.User;


public class PhotoHelper {
    private PhotoHelper() {};
    private static String photoNameRegexp = "\\w{1,}";

    public static List<Photo> getAllPhotosInAlbum(long album_id) throws DBException {
        //List<Photo> photos = new LinkedList<Photo>();
        try {
            return PhotosDAO.getAllPhotosInAlbum(album_id);
        } catch (DBException e) {
            throw new DBException("Exception while getting photos", e);
        }
    }

    public static void UploadPhoto(User currentUser, Album currentAlbum,
            String fileName, String name, String description, String tags) throws FileNotFoundException, DBException {
        try{
            File file = new File(fileName);
            if (file.exists() && file.isFile() && file.length() < 200*1024*1024) {
                if (name.matches(photoNameRegexp)) {
                    //FileInputStream is = new FileInputStream(file);
                    //byte[] data = is.
                    Photo photo = new Photo(0,
                            name,
                            currentAlbum.getId(),
                            currentUser.getId(),
                            description,
                            tags,
                            new byte[1]
                            );
                    PhotosDAO.addPhoto(photo, file);
                }
            } else {
                throw new FileNotFoundException("File not found!");
            }
        } catch (NullPointerException e) {
            throw new FileNotFoundException("Empty name!");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found!");
        } catch (DBException e) {
            throw new DBException("Exception in Helper!", e);
        }

    }

    /*public static List<Photo> getUserPhotos(long user_id) throws DBException {
        try {
            return PhotosDAO.getUserPhotos(user_id);
        } catch (DBException e) {
            throw new DBException("Exception while getting user photos", e);
        }
    }*/

    /*public static List<Photo> getUserPhotoByName(long user_id, String name) throws DBException {
        try {
            return PhotosDAO.getUserPhotoByName(user_id, name);
        } catch (DBException e) {
            throw new DBException("Exception while getting user photo by name", e);
        }
    }*/

    /*public static Photo getPhoto(long album_id, long id) throws DBException {
        try {
            return PhotosDAO.getPhotoById(album_id, id);
        } catch (DBException e) {
            throw new DBException("Exception while getting user photo by id", e);
        }
    }*/





}

package ru.ncedu.laimz.photos;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Album {
    private long id;
    private long author_id;
    private String name;
    
    public Album () {}

    /*public Album(ResultSet rs) throws SQLException {
        this.id = rs.getLong(1);
        this.author_id = rs.getLong(2);
        this.name = rs.getString(3);
    }*/
    
    public Album (long id, String name, long author_id) {
        this.id = id;
        this.author_id = author_id;
        this.name = name;
    }
    
    public long getId() {
        return this.id;
    }

    public long getAuthorId() {
        return this.author_id;
    }

    public String getName() {
        return this.name;
    }
}

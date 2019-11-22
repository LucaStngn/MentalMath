package com.example.database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.mainClasses.History;
import static androidx.room.OnConflictStrategy.IGNORE;

/*
 *  HistoryDAO.java
 *  Room (DAO - Data Access Object)
 *  Here you can define all the Database queries in form of Java methods.
 */

@Dao
public interface HistoryDAO {

    // Query to insert a new row to the 'History_table':
    @Insert(onConflict = IGNORE)
    void insertHistory(History h);

    // Query to get all past user answers from the history database:
    @Query("SELECT * FROM History_Table")
    History[] getHistory();
}
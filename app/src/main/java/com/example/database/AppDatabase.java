package com.example.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.mainClasses.History;
import com.example.mainClasses.Quiz;
import com.example.mainClasses.XP;

/*
 *  AppDatabase.java
 *  This class handles the Room database used to store data of the API responses.
 */

@Database(entities = {XP.class, History.class, Quiz.class}, version = 1)                            // Here you define all the entities (tables) the database will have.
public abstract class AppDatabase extends RoomDatabase {
    public abstract XPDAO xpDao();                                                                  // Here you get access to all the pre-written queries in your custom DAO.
    public abstract HistoryDAO historyDAO();                                                        // Here you get access to all the pre-written queries in your custom DAO.
    public abstract QuizDAO quizDAO();                                                              // Here you get access to all the pre-written queries in your custom DAO.

    private static AppDatabase instance;
    public static AppDatabase getInstance(Context context) {                                        // Method, so we don't have to create the database every time we run the app. This way we create it once and access it every time afterwards.

        if(instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "MentalMathDB")      // Here you can define the name of your database file.
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
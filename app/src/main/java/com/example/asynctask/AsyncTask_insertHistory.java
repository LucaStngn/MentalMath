package com.example.asynctask;
import android.os.AsyncTask;
import com.example.database.AppDatabase;
import com.example.mainClasses.History;

/*
 * AsyncTask_insertHistory.java
 * handles what to do in the extra thread.
 * We need one of these classes per 'different task' (here per type of database query).
 */

public class AsyncTask_insertHistory extends AsyncTask<History, Integer, String> {                  // <Input, (Unit of progress), Output>
    private AsyncTaskDelegate_insertHistory delegate;                                               // We store a variable for an object that implements our interface, so we know that whatever is in here, knows how to handle the result of our task.
    private AppDatabase db;                                                                         // This AsyncTask will also need to be given a database instance, so it can perform database queries.

    public void setDelegate(AsyncTaskDelegate_insertHistory delegate) {
        this.delegate = delegate;
    }

    public void setDatabase(AppDatabase database) {
        this.db = database;
    }

    /**
     * doInBackground()
     * In this method you do the task that could take a long time.
     * --> e.g. database queries / API requests
     * @param history: The '...' means it accepts either a 'single history' or an 'array of histories' or 'nothing'!
     */
    @Override
    protected String doInBackground(History... history) {                                           // The parameter always needs the '...' (because the doInBackground() expects a List)
        db.historyDAO().insertHistory(history[0]);                                                  // Code to perform the task.
        return "Data inserted successfully!";                                                       // This value will be passed to onPostExecute as the result parameter.
    }

    /**
     * onPostExecute()
     * Once doInBackground() is completed, this method will automatically run.
     * In this method you usually give the result back to the delegate (the activity / fragment the thread was executed from) and let them handle it.
     * @param result: is passed automatically from doInBackground().
     */
    @Override
    protected void onPostExecute(String result) {
        delegate.handleTaskResult(result);                                                          // Call the delegate's method with the results
    }
}
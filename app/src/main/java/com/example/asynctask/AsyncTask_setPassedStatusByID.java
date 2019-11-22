package com.example.asynctask;
import android.os.AsyncTask;
import com.example.database.AppDatabase;

/*
 * AsyncTask_setPassedStatusByID.java
 * handles what to do in the extra thread.
 * We need one of these classes per 'different task' (here per type of database query).
 */

public class AsyncTask_setPassedStatusByID extends AsyncTask<Integer, Integer, String> {            // <Input, (Unit of progress), Output>
    private AsyncTaskDelegate_setPassedStatusByID delegate;                                         // We store a variable for an object that implements our interface, so we know that whatever is in here, knows how to handle the result of our task.
    private AppDatabase db;                                                                         // This AsyncTask will also need to be given a database instance, so it can perform database queries.

    public void setDelegate(AsyncTaskDelegate_setPassedStatusByID delegate) {
        this.delegate = delegate;
    }

    public void setDatabase(AppDatabase database) {
        this.db = database;
    }

    /**
     * doInBackground()
     * In this method you do the task that could take a long time.
     * --> e.g. database queries / API requests
     * @param ID: The '...' means it accepts either a 'single ID' or an 'array of IDs' or 'nothing'!
     */
    @Override
    protected String doInBackground(Integer... ID) {                                                // The parameter always needs the '...' (because the doInBackground() expects a List)
        db.quizDAO().setPassedStatusByID(ID[0]);                                                    // Do the task
        return "Done";
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
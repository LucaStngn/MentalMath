package com.example.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.asynctask.AsyncTaskDelegate_getHistory;
import com.example.asynctask.AsyncTask_getHistory;
import com.example.database.AppDatabase;
import com.example.mainClasses.History;
import com.example.mentalmath.HistoryAdapter;
import com.example.mentalmath.R;

/*
 *  HistoryFragment.java
 *  This class defines the functionality of the 'fragment_history' layout.
 */

public class HistoryFragment extends Fragment implements AsyncTaskDelegate_getHistory {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryFragment thisFragment = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);        // Connect to the layout file 'fragment_history.xml'.

        // Database:
        final AppDatabase db = AppDatabase.getInstance(getContext());                               // Database Object

        // 'getHistory' via AsyncTask (in another thread):
        AsyncTask_getHistory task = new AsyncTask_getHistory();                                     // Create extra thread.
        task.setDatabase(db);                                                                       // Set database.
        task.setDelegate(thisFragment);                                                             // Set delegate.
        task.execute();                                                                             // Get the list of History objects from the database using the DAO.

        // Recycler View:
        recyclerView = v.findViewById(R.id.rv_history);                                             // 1.0) Link UI-Element (via it's ID 'rv_history') to the Java variable 'recyclerView'.
        layoutManager = new LinearLayoutManager(getContext());                                      // 2.1) Create new Layout Manager
        recyclerView.setLayoutManager(layoutManager);                                               // 2.2) Assign Layout Manager to the RecyclerView it should manage.

        return v;
    }

    /**
     * Required Method to handle the result of the Async Task 'getHistory()'
     */
    public void handleTaskResult(History[] result) {
        // Recycler View:
        HistoryAdapter historyAdapter = new HistoryAdapter();                                       // 3.1) Create new Object of the class 'HistoryAdapter' called 'historyAdapter'
        historyAdapter.setData(result);                                                             // 3.2) Call the method 'setData()' in the class 'HistoryAdapter' and pass this method the whole 'history' from the database.
        recyclerView.setAdapter(historyAdapter);                                                    // 3.3) Assign the Adapter object 'historyAdapter' to the RecyclerView it should manage.
    }
}
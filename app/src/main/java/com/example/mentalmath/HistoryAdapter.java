package com.example.mentalmath;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mainClasses.History;
import java.util.Arrays;
import java.util.Collections;
import io.github.sidvenu.mathjaxview.MathJaxView;
import static com.example.fragments.TrainingFragment.html_1;
import static com.example.fragments.TrainingFragment.html_2;

/*
 *  HistoryAdapter.java
 *  This class handles the data of each 'history_chunk' loaded.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private History[] list;                                                                         // DATA: Create Array of 'History Objects', called 'list'.

    // Give data to the adapter:
    public void setData(History[] allAnswers) {
        Collections.reverse(Arrays.asList(allAnswers));                                             // Reverse array order, so that the newest history entry is always the first one.
        this.list = allAnswers;                                                                     // Assign all the 'history' objects from the database to the Array of 'history' objects.
    }

    // Function 1/3: Create new views (The 'chunks'):
    @NonNull @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_chunk, parent, false);    // Create a new view from the layout file 'history_chunk'. (One of this view represents one 'history_chunk')
        return new HistoryViewHolder(view);                                                                                 // Create an instance of my custom ViewHolder for this 'history_chunk' view.
    }

    // Function 2/3: Replace the contents of all RecyclerView elements (Gets executed for every RecyclerView element):
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, final int position) {                     // CustomViewHolder
        final History historyAtPosition = list[position];                                                                    // Get the ID of the current view.
        holder.bind(historyAtPosition);
    }

    // Function 3/3: Return the amount of items in the dataset:
    @Override
    public int getItemCount() {
        return list.length;
    }

    // Creating the HistoryViewHolder class:
    public static class HistoryViewHolder extends RecyclerView.ViewHolder {                         // Custom ViewHolder (inherited from the default ViewHolder) that represents one item in the RecyclerView. It doesn't have data when it's first constructed. We assign the data in onBindViewHolder.
        private MathJaxView MJV1, MJV2, MJV3;
        private TextView txt1;
        private View viw1;
        public HistoryViewHolder(View v) {                                                          // This constructor is used in onCreateViewHolder.
            super(v);                                                                               // Runs the constructor for the ViewHolder superclass
            viw1 = v;                                                                               // View needed later for the explicit intent.
            MJV1 = v.findViewById(R.id.mathViewQuestion);
            MJV2 = v.findViewById(R.id.mathViewCorrectAnswer);
            MJV3 = v.findViewById(R.id.mathViewUserAnswer);
            txt1 = v.findViewById(R.id.txtStaticUser);
        }

        // Assign data to the view elements inside the RecyclerView:
        private void bind(final History history) {
            // The custom html input is necessary to set the 'left-alignment' of the MathJaxViews:
            MJV1.loadDataWithBaseURL("about:blank", html_1 + "<math>" + history.getQuestion() + "</math>" + html_2, "text/html", "UTF-8", "");
            MJV2.loadDataWithBaseURL("about:blank", html_1 + history.getCorrect_choice() + html_2, "text/html", "UTF-8", "");
            MJV3.loadDataWithBaseURL("about:blank", html_1 + history.getUser_choice() + html_2, "text/html", "UTF-8", "");

            // Set TextView color according to user answer correctness:
            if (history.getCorrect_choice().equals(history.getUser_choice())) {
                txt1.setTextColor(Color.rgb(50,205,50));
            } else {
                txt1.setTextColor(Color.RED);
            }
        }
    }
}
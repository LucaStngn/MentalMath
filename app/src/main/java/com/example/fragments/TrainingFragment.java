package com.example.fragments;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asynctask.AsyncTaskDelegate_insertHistory;
import com.example.asynctask.AsyncTask_insertHistory;
import com.example.database.AppDatabase;
import com.example.mainClasses.History;
import com.example.mainClasses.XP;
import com.example.mentalmath.R;
import com.example.mainClasses.Task;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import io.github.sidvenu.mathjaxview.MathJaxView;                                                   // Library used to display MathML via MathJax in WebViews (https://github.com/sidvenu/MathJaxView)

/*
 *  TrainingFragment.java
 *  This class defines the functionality of the 'trainingFragment' layout.
 */

public class TrainingFragment extends Fragment implements AsyncTaskDelegate_insertHistory {
    private String API_URL = "https://studycounts.com/api/v1/algebra/linear-equations.json?difficulty=beginner";    // Start difficulty is beginner level.
    private MathJaxView MJV1, MJV2, MJV3, MJV4, MJV5, MJV6;
    private TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8;
    private Task taskFromJSON;
    private RadioGroup rdG1;
    private int amount, XP, right = 0, wrong = 0;
    private Timer timer = new Timer();
    private XP defaultXPObject = new XP();
    private TrainingFragment thisFragment = this;
    public static String config = "" +                                                              // This way, we can configure the MathJaxView display style in Java itself. (You can't just change a MathJaxView's properties via Java. You need to change it's actual html configuration)
            "MathJax.Hub.Config({" +                                                                // Received from the official MathJax documentation: http://docs.mathjax.org/en/latest/options/index.html
            "    extensions: ['fast-preview.js']," +                                                // Via this configuration, we can enable "Left alignment" of MathJaxViews.
            "    messageStyle: 'none'," +
            "    \"fast-preview\": {" +
            "      disabled: false" +
            "    }," +
            "    CommonHTML: {" +
            "      linebreaks: { automatic: true, width: \"container\" }" +
            "    }," +
            "    tex2jax: {" +
            "      inlineMath: [ ['$','$'] ]," +
            "      displayMath: [ ['$$','$$'] ]," +
            "      processEscapes: true" +
            "    }," +
            "    TeX: {" +
            "      extensions: [\"file:///android_asset/MathJax/extensions/TeX/mhchem.js\"]," +
            "      mhchem: {legacy: false}"+
            "    }" +
            "});";
    public static String html_1 = "<html><head><style>body {text-align: left;}</style><script type=\"text/x-mathjax-config\">" + config + "</script><script type=\"text/javascript\" async src=\"file:///android_asset/MathJax/MathJax.js?config=TeX-MML-AM_CHTML\"></script></head><body>";
    public static String html_2 = "</body></html>";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_training, container, false);       // Connect to the layout file 'fragment_training.xml'.
        MJV1 = v.findViewById(R.id.mathView);
        MJV2 = v.findViewById(R.id.mathViewR1);
        MJV3 = v.findViewById(R.id.mathViewR2);
        MJV4 = v.findViewById(R.id.mathViewR3);
        MJV5 = v.findViewById(R.id.mathViewR4);
        MJV6 = v.findViewById(R.id.mathViewR5);
        rdG1 = v.findViewById(R.id.rdgAnswer);
        txt1 = v.findViewById(R.id.txtOutcome);
        txt2 = v.findViewById(R.id.txtInstructions);
        txt3 = v.findViewById(R.id.txtDifficulty);
        txt4 = v.findViewById(R.id.txtCategory);
        txt5 = v.findViewById(R.id.txtTopic);
        txt6 = v.findViewById(R.id.txtXP);
        txt7 = v.findViewById(R.id.txtRight);
        txt8 = v.findViewById(R.id.txtWrong);
        final AppDatabase db = AppDatabase.getInstance(getContext());                               // Database Object

        // Set default value in database (BUT only when the database is created):
        db.xpDao().insertXP(defaultXPObject);                                                       // Not done via AsyncTask by intention, because thereby we can't rely on a 100% correct program schedule.
        // Get current XP value:
        XP = db.xpDao().getXP();                                                                    // Not done via AsyncTask by intention, because thereby we can't rely on a 100% correct program schedule.

        // Set question difficulty, based on which quizzes are passed so far:
        if (db.quizDAO().getPassedStatusByID(3)) {
            API_URL = "https://studycounts.com/api/v1/algebra/linear-equations.json";               // Random difficulty
        } else if (db.quizDAO().getPassedStatusByID(2)) {
            API_URL = "https://studycounts.com/api/v1/algebra/linear-equations.json?difficulty=advanced";
        } else if (db.quizDAO().getPassedStatusByID(1)) {
            API_URL = "https://studycounts.com/api/v1/algebra/linear-equations.json?difficulty=intermediate";
        }

        if (!isOnline()) {
            Toast t = Toast.makeText(getContext(), "No internet connection...", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        } else {
            // Volley API Request (Start):
            final RequestQueue queue = Volley.newRequestQueue(getContext());                        // Instantiate the RequestQueue.
            final StringRequest SR1 = new StringRequest(Request.Method.GET, API_URL,                // Request a string response from the API URL.
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            // Display Task:
                            taskFromJSON = new Gson().fromJson(response, Task.class);               // Create a 'task' object from the API JSON response.
                            MJV1.setText("<math>" + taskFromJSON.getQuestion() + "</math>");        // Display the Task via MathJax.
                            txt2.setText(taskFromJSON.getInstruction());
                            txt3.setText(taskFromJSON.getDifficulty());
                            txt4.setText(taskFromJSON.getCategory());
                            txt5.setText(taskFromJSON.getTopic());
                            txt6.setText(Integer.toString(XP));
                            txt7.setText(Integer.toString(right));
                            txt8.setText(Integer.toString(wrong));

                            // Display the 5 possible answers (with custom formatting)
                            // The html input is necessary to get the 'left-alignment'
                            MJV2.loadDataWithBaseURL("about:blank", html_1 + taskFromJSON.getChoices()[0] + html_2, "text/html", "UTF-8", "");
                            MJV3.loadDataWithBaseURL("about:blank", html_1 + taskFromJSON.getChoices()[1] + html_2, "text/html", "UTF-8", "");
                            MJV4.loadDataWithBaseURL("about:blank", html_1 + taskFromJSON.getChoices()[2] + html_2, "text/html", "UTF-8", "");
                            MJV5.loadDataWithBaseURL("about:blank", html_1 + taskFromJSON.getChoices()[3] + html_2, "text/html", "UTF-8", "");
                            MJV6.loadDataWithBaseURL("about:blank", html_1 + taskFromJSON.getChoices()[4] + html_2, "text/html", "UTF-8", "");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse.statusCode == 429) {
                        Toast t = Toast.makeText(getContext(), "Max 10 requests per min!", Toast.LENGTH_SHORT); // Current limitation by the API (https://studycounts.com/api. Therefore we catch this error here to guarantee user-friendliness)
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    } else {
                        System.out.println("API Error!");
                    }
                }
            });
            queue.add(SR1);                                                                         // Add the request to the RequestQueue and execute the request.
            // Volley API Request (End)

        rdG1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {                  // If the user selects one of the 5 radio buttons:
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {                         // checkedId is the id of the selected RadioButton.
                RadioButton selectedRB = group.findViewById(checkedId);                             // Currently selected Radiobutton.

                // Gets executed 3 seconds after any user answer:
                final Handler handler = new Handler();                                              // https://developer.android.com/reference/android/os/Handler
                TimerTask clearOutput = new TimerTask() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                txt1.setText("");
                            }
                        });
                    }
                };

                // Set XP change (according to difficulty)
                switch (taskFromJSON.getDifficulty()) {
                    case "Beginner":
                        amount = 10;
                        break;
                    case "Intermediate":
                        amount = 20;
                        break;
                    case "Advanced":
                        amount = 30;
                        break;
                }

                // Handle correct / wrong answers:
                if (group.indexOfChild(selectedRB) == taskFromJSON.getCorrect_choice()) {
                    txt1.setText("Correct!\n+" + amount + " XP");
                    txt1.setTextColor(getResources().getColor(R.color.colorCorrect));
                    XP = XP + amount;
                    right++;
                } else {
                    txt1.setText("Wrong...\n-10 " + " XP");
                    txt1.setTextColor(getResources().getColor(R.color.colorWrong));
                    XP = XP - 10;
                    wrong++;
                }

                // Insert the task into history database:
                History histObj = new History();
                histObj.setQuestion(taskFromJSON.getQuestion());
                histObj.setCorrect_choice(taskFromJSON.getChoices()[taskFromJSON.getCorrect_choice()]);
                histObj.setUser_choice(taskFromJSON.getChoices()[group.indexOfChild(selectedRB)]);
                // Insert via AsyncTask (in another thread):
                AsyncTask_insertHistory task = new AsyncTask_insertHistory();                       // Create extra thread.
                task.setDatabase(db);                                                               // Set database.
                task.setDelegate(thisFragment);                                                     // Set delegate.
                task.execute(histObj);

                // Update the statistics:
                txt6.setText(Integer.toString(XP));
                txt7.setText(Integer.toString(right));
                txt8.setText(Integer.toString(wrong));

                // Reset to next task:
                db.xpDao().setXP(XP);                                                               // Update the database
                timer.schedule(clearOutput, 3000);                                            // After 3 seconds, clear the text output
                selectedRB.setChecked(false);                                                       // Uncheck the user selected radioButton. (Does not trigger the 'setOnCheckedChangeListener')
                loadNewTask(queue, SR1);                                                            // Request new Task from the API
            }
        });
        }
        return v;
    }

    /**
     * Method to check if the phone has working internet connection.
     * @return true, if it has a stable internet connection.
     * Credits go to: https://medium.com/it-works-locally/what-if-your-android-user-doesnt-have-access-to-the-internet-ad3588cdbda4
     */
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    /**
     * Method to request a new task from the API via Volley.
     */
    public void loadNewTask(RequestQueue queue, StringRequest SR) {
        queue.add(SR);
    }

    /**
     * Required Method to handle the result of the Async Task 'insertHistory()'
     */
    public void handleTaskResult(String result) {
        // Do nothing (History / XP have just been inserted to the database)
    }
}
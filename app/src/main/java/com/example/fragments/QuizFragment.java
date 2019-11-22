package com.example.fragments;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asynctask.AsyncTaskDelegate_setPassedStatusByID;
import com.example.asynctask.AsyncTask_setPassedStatusByID;
import com.example.database.AppDatabase;
import com.example.mainClasses.Task;
import com.example.mentalmath.R;
import com.google.gson.Gson;
import io.github.sidvenu.mathjaxview.MathJaxView;                                                   // Library used to display MathML via MathJax in WebViews (https://github.com/sidvenu/MathJaxView)
import java.util.Timer;
import java.util.TimerTask;
import static com.example.fragments.TrainingFragment.html_1;                                        // Reference value from TrainingFragment, so it doesn't need to be created twice.
import static com.example.fragments.TrainingFragment.html_2;                                        // Reference value from TrainingFragment, so it doesn't need to be created twice.

/*
 *  QuizFragment.java
 *  This class defines the functionality of the 'quizFragment' layout.
 */

public class QuizFragment extends Fragment implements AsyncTaskDelegate_setPassedStatusByID {
    private String API_URL;
    public static String passedMessage = "";
    public static boolean passedStatus = false;
    private MathJaxView MJV1, MJV2, MJV3, MJV4, MJV5, MJV6;
    private TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9;
    private Task taskFromJSON;
    private RadioGroup rdG1;
    private int right = 0, wrong = 0, questionCounter = 1, interval = 1000, MS;
    private Timer timer = new Timer();
    private ProgressBar PGB1;
    private QuizFragment thisFragment = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);           // Connect to the layout file 'fragment_quiz.xml'.
        MJV1 = v.findViewById(R.id.mathView);
        MJV2 = v.findViewById(R.id.mathViewCorrectAnswer);
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
        txt6 = v.findViewById(R.id.txtRight);
        txt7 = v.findViewById(R.id.txtWrong);
        txt8 = v.findViewById(R.id.txtCounter);
        txt9 = v.findViewById(R.id.txtTimer);
        PGB1 = v.findViewById(R.id.progressBar);

        // Set Quiz difficulty according to the quiz that was clicked on:
        switch (QuizMenuFragment.QuizID) {
            case 1:
                API_URL = "https://studycounts.com/api/v1/algebra/linear-equations.json?difficulty=beginner";
                MS = 120000;                                                                        // 2 minutes to complete the easy quiz.
                break;
            case 2:
                API_URL = "https://studycounts.com/api/v1/algebra/linear-equations.json?difficulty=intermediate";
                MS = 240000;                                                                        // 4 minutes to complete the medium quiz.
                break;
            case 3:
                API_URL = "https://studycounts.com/api/v1/algebra/linear-equations.json?difficulty=advanced";
                MS = 360000;                                                                        // 6 minutes to complete the hard quiz.
                break;
        }

        // Volley API Request (Start):
        final RequestQueue queue = Volley.newRequestQueue(getContext());                            // Instantiate the RequestQueue.
        final StringRequest SR1 = new StringRequest(Request.Method.GET, API_URL,                    // Request a string response from the API URL.
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        // Display Task:
                        taskFromJSON = new Gson().fromJson(response, Task.class);                   // Create a 'task' object from the API JSON response.
                        MJV1.setText("<math>" + taskFromJSON.getQuestion() + "</math>");            // Display the Task via MathJax.
                        txt2.setText(taskFromJSON.getInstruction());
                        txt3.setText(taskFromJSON.getDifficulty());
                        txt4.setText(taskFromJSON.getCategory());
                        txt5.setText(taskFromJSON.getTopic());
                        txt6.setText(Integer.toString(right));
                        txt7.setText(Integer.toString(wrong));
                        txt8.setText(questionCounter + "/10");

                        // Display the 5 possible answers (with custom formatting)
                        // The custom html input is necessary to set the 'left-alignment' of the MathJaxViews:
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
        queue.add(SR1);                                                                             // Add the request to the RequestQueue and execute the request.
        // Volley API Request (End)

        // Display the time left (countdown & progressbar):
        new CountDownTimer(MS, interval) {                                                          // https://developer.android.com/reference/android/os/CountDownTimer
            public void onTick(long msUntilFinished) {
                txt9.setText(Long.toString(msUntilFinished / interval));                         // Countdown
                PGB1.setProgress((int)(((double)msUntilFinished/(double)MS)*100));                  // Progressbar
            }
            public void onFinish() {
                checkIfPassed();                                                                    // If the time is up.
            }
        }.start();

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

                // Handle correct / wrong answers:
                if (group.indexOfChild(selectedRB) == taskFromJSON.getCorrect_choice()) {           // If the selected radioButton ID matches the correctAnswer ID
                    right++;
                    txt1.setText("Correct!");
                    txt1.setTextColor(getResources().getColor(R.color.colorCorrect));
                } else {
                    wrong++;
                    txt1.setText("Wrong...");
                    txt1.setTextColor(getResources().getColor(R.color.colorWrong));
                }

                // Set the displayed statistics:
                questionCounter++;
                txt6.setText(Integer.toString(right));
                txt7.setText(Integer.toString(wrong));
                txt8.setText(questionCounter + "/10");

                // Reset to next task
                if (questionCounter < 10 && wrong <= 2) {
                    timer.schedule(clearOutput, 3000);                                        // After 3 seconds, clear the text output
                    selectedRB.setChecked(false);                                                   // Uncheck the user selected radioButton. (This way it does not trigger the 'setOnCheckedChangeListener')
                    loadNewTask(queue, SR1);                                                        // Request new Task from the API
                } else {
                    checkIfPassed();                                                                // If the user gave too many wrong answers.
                }
            }
        });
        return v;
    }

    /**
     * Helper method to change the fragment displayed in the activity.
     * @param fmt: instance of the fragment that is supposed to go into the slot.
     */
    public void swapFragment(Fragment fmt) {
        if (getActivity() != null) {
            FragmentManager FM = getActivity().getSupportFragmentManager();                         // Instantiate a new FragmentManager object 'FM'. (Needed to to the add / replace / remove commands)
            FragmentTransaction fmtTransaction = FM.beginTransaction();
            fmtTransaction.replace(R.id.fragmentContainer, fmt);
            fmtTransaction.commit();                                                                // Execute the action above.
        }
    }

    /**
     * Helper method to load a new task from the API:
     */
    public void loadNewTask(RequestQueue queue, StringRequest SR) {
        queue.add(SR);
    }

    /**
     * Helper method check if the user passed the quiz:
     */
    public void checkIfPassed() {
        final AppDatabase db = AppDatabase.getInstance(getContext());                               // Database Object
        if (right >= 8 && wrong <= 2) {
            // Insert via AsyncTask (in another thread):
            AsyncTask_setPassedStatusByID task = new AsyncTask_setPassedStatusByID();               // Create extra thread.
            task.setDatabase(db);                                                                   // Set database.
            task.setDelegate(thisFragment);                                                         // Set delegate.
            task.execute(QuizMenuFragment.QuizID);                                                  // Change the database value to true!
            swapFragment(new QuizMenuFragment());                                                   // Go back to quizMenu.
            passedMessage = "Congratulations!\nYou passed this quiz!\nYou can now train the next difficulty.";
            passedStatus = true;
        } else {
            swapFragment(new QuizMenuFragment());                                                   // Go back to quizMenu.
            passedMessage = "Failed!\nYou need at least 8/10 correct answers to pass a quiz!";
            passedStatus = false;
        }
    }

    /**
     * Required Method to handle the result of the Async Task 'setPassedStatusByID()'
     */
    @Override
    public void handleTaskResult(String result) {
        // Do nothing (Quiz Status has just been set to true)
    }
}
package com.example.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.asynctask.AsyncTaskDelegate_insertQuiz;
import com.example.asynctask.AsyncTask_insertQuiz;
import com.example.database.AppDatabase;
import com.example.mainClasses.Quiz;
import com.example.mentalmath.R;

/*
 *  QuizMenuFragment.java
 *  This class defines the functionality of the 'quizzesFragment' layout.
 */

public class QuizMenuFragment extends Fragment implements AsyncTaskDelegate_insertQuiz {
    private TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8;
    private Button btn1, btn2, btn3;
    private int XP, EasyUnlock = 10, MediumUnlock = 20, HardUnlock = 30;
    public static int QuizID = 0;                                                                   // 'Global variable' always keeps the information of the quiz that has been clicked on.
    private Quiz defaultQuizObject = new Quiz();
    private QuizMenuFragment thisFragment = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quizmenu, container, false);        // Connect to the layout file 'fragment_quizmenu.xml'.
        btn1 = v.findViewById(R.id.btnEasy);
        btn2 = v.findViewById(R.id.btnMedium);
        btn3 = v.findViewById(R.id.btnHard);
        txt1 = v.findViewById(R.id.txtXP);
        txt2 = v.findViewById(R.id.txtEasyUnlockStatus);
        txt3 = v.findViewById(R.id.txtEasyPassStatus);
        txt4 = v.findViewById(R.id.txtMediumUnlockStatus);
        txt5 = v.findViewById(R.id.txtMediumPassStatus);
        txt6 = v.findViewById(R.id.txtHardUnlockStatus);
        txt7 = v.findViewById(R.id.txtHardPassStatus);
        txt8 = v.findViewById(R.id.txtOutput);
        final AppDatabase db = AppDatabase.getInstance(getContext());                               // Database Object

        // Get current XP value from database:
        XP = db.xpDao().getXP();
        txt1.setText(Integer.toString(XP));                                                         // Load current database XP value into TextView on Fragment load.

        for (int i=1; i<=3; i++) {                                                                  // For all three quiz difficulties:
            defaultQuizObject.setQuizID(i);
            defaultQuizObject.setPassed(false);
            // 'Insert' via AsyncTask (in another thread):
            AsyncTask_insertQuiz task1 = new AsyncTask_insertQuiz();                                // Create extra thread.
            task1.setDatabase(db);                                                                  // Set database.
            task1.setDelegate(thisFragment);                                                        // Set delegate.
            task1.execute(defaultQuizObject);                                                       // Set default values (3 times false) in database (BUT only when the database is created)
        }

        // Set 'quiz passed' values, depending on the database values.
        if (db.quizDAO().getPassedStatusByID(1)) {
            txt3.setText("Yes");
            txt3.setTextColor(getResources().getColor(R.color.colorCorrect));
        } else {
            txt3.setText("No");
            txt3.setTextColor(getResources().getColor(R.color.colorWrong));
        }
        if (db.quizDAO().getPassedStatusByID(2)) {
            txt5.setText("Yes");
            txt5.setTextColor(getResources().getColor(R.color.colorCorrect));
        } else {
            txt5.setText("No");
            txt5.setTextColor(getResources().getColor(R.color.colorWrong));
        }
        if (db.quizDAO().getPassedStatusByID(3)) {
            txt7.setText("Yes");
            txt7.setTextColor(getResources().getColor(R.color.colorCorrect));
        } else {
            txt7.setText("No");
            txt7.setTextColor(getResources().getColor(R.color.colorWrong));
        }

        // Set 'quiz unlocked' values, based on the current XP value:
        if (XP >= EasyUnlock) {
            txt2.setText("Yes");
            txt2.setTextColor(getResources().getColor(R.color.colorCorrect));
        } else {
            txt2.setText("No");
            txt2.setTextColor(getResources().getColor(R.color.colorWrong));
        }
        if (XP >= MediumUnlock) {
            txt4.setText("Yes");
            txt4.setTextColor(getResources().getColor(R.color.colorCorrect));
        } else {
            txt4.setText("No");
            txt4.setTextColor(getResources().getColor(R.color.colorWrong));
        }
        if (XP >= HardUnlock) {
            txt6.setText("Yes");
            txt6.setTextColor(getResources().getColor(R.color.colorCorrect));;
        } else {
            txt6.setText("No");
            txt6.setTextColor(getResources().getColor(R.color.colorWrong));
        }

        // Set Quiz output, (ONLY) if the user is coming back from a quiz:
        String passedMessage = QuizFragment.passedMessage;
        boolean passedStatus = QuizFragment.passedStatus;
        if (passedStatus) {
            txt8.setText(passedMessage);
            txt8.setTextColor(getResources().getColor(R.color.colorCorrect));
        } else {
            txt8.setText(passedMessage);
            txt8.setTextColor(getResources().getColor(R.color.colorWrong));
        }

        // Easy Button Click Handling:
        btn1.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
            if (XP >= EasyUnlock)
               {
                   QuizID = 1;
                   swapFragment(new QuizFragment());
               } else {
                    txt8.setText("You need " + (EasyUnlock - XP) + " XP more to unlock this quiz.");
                    txt8.setTextColor(getResources().getColor(R.color.colorWrong));
               }
            }
        });

        // Medium Button Click Handling:
        btn2.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
            if (XP >= MediumUnlock)
                {
                    QuizID = 2;
                    swapFragment(new QuizFragment());
                } else {
                    txt8.setText("You need " + (MediumUnlock - XP) + " XP more to unlock this quiz.");
                    txt8.setTextColor(getResources().getColor(R.color.colorWrong));
                }
            }

        });

        // Hard Button Click Handling:
        btn3.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
            if (XP >= HardUnlock)
                {
                    QuizID = 3;
                    swapFragment(new QuizFragment());
                } else {
                    txt8.setText("You need " + (HardUnlock - XP) + " XP more to unlock this quiz.");
                    txt8.setTextColor(getResources().getColor(R.color.colorWrong));
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
     * Required Method to handle the result of the Async Task 'insertQuiz()'
     */
    public void handleTaskResult(String result) {
        // Do nothing (Quiz has just been inserted to the database)
    }
}
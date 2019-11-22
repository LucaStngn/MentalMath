package com.example.mainClasses;

/*
 *  Task.java
 *  This class defines how one 'Task Object' looks like.
 *  It needs to be defined exactly the way the data is expected to be passed from the API.
 */

import androidx.room.Ignore;

public class Task {
    private String id;
    private String question;
    @Ignore
    private String[] choices;
    private int correct_choice;
    private String instruction;
    private String category;
    private String topic;
    private String difficulty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public int getCorrect_choice() {
        return correct_choice;
    }

    public void setCorrect_choice(int correct_choice) {
        this.correct_choice = correct_choice;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
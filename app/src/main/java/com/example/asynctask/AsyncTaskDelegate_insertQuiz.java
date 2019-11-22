package com.example.asynctask;

/*
 * AsyncTaskDelegate_insertQuiz.java
 * (Interface)
 * It contains a method to handle the result of the 'insertQuiz' task.
 * Whichever class implements this interface will then HAVE to implement handleTaskResult.
 * This means we can guarantee that any class with this interface knows how to handle a task result.
 * You need one of those interfaces for each different type of result that your tasks return)
 */

public interface AsyncTaskDelegate_insertQuiz {
    void handleTaskResult(String result);
}
package com.quizapp;

class QuizResult {
    private int resultID;
    private int studentID;
    private int score;

    public QuizResult(int studentID, int score) {
       
        this.studentID = studentID;
        this.score = score;
    }

// Getters and setters for QuizResult properties

// Getter methods
    public int getResultID() {
        return resultID;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getScore() {
        return score;
    }

    // Setter methods
    public void setResultID(int resultID) {
        this.resultID = resultID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
}

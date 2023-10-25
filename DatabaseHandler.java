package com.quizapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class DatabaseHandler {
    private Connection connection;

    public void connect(String DB_URL, String DB_USER, String DB_PASSWORD) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

public void addStudent(Student student) {
        try {
            // Add the student to the Students table in the database.
            String insertQuery = "INSERT INTO Students (firstName, lastName, username, password, city, email, mobileNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getUsername());
            preparedStatement.setString(4, student.getPassword());
            preparedStatement.setString(5, student.getCity());
            preparedStatement.setString(6, student.getEmail());
            preparedStatement.setString(7, student.getMobileNumber());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student added to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


public Student getStudent(String username) {
        // Retrieve a student's information by username from the Students table in the database.
        String query = "SELECT * FROM Students WHERE username = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int studentID = resultSet.getInt("studentID");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String password = resultSet.getString("password");
                String city = resultSet.getString("city");
                String email = resultSet.getString("email");
                String mobileNumber = resultSet.getString("mobileNumber");

                return new Student(studentID, firstName, lastName, username, password, city, email, mobileNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

public Student getStudentById(int studentID) {
    // Retrieve a student's information by studentID from the Students table in the database.
    String query = "SELECT * FROM Students WHERE studentID = ?";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, studentID);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int dbStudentID = resultSet.getInt("studentID");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String city = resultSet.getString("city");
            String email = resultSet.getString("email");
            String mobileNumber = resultSet.getString("mobileNumber");

            return new Student(dbStudentID, firstName, lastName, username, password, city, email, mobileNumber);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

public void addQuestion(Question question) {
        // Add a new question to the Questions table in the database.
        try {
            String insertQuery = "INSERT INTO Questions (question, optionA, optionB, optionC, optionD, correctAnswer) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, question.getQuestion());
            preparedStatement.setString(2, question.getOptionA());
            preparedStatement.setString(3, question.getOptionB());
            preparedStatement.setString(4, question.getOptionC());
            preparedStatement.setString(5, question.getOptionD());
            preparedStatement.setString(6, question.getCorrectAnswer());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Question added to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

public List<Question> getQuestions() {
        // Retrieve a list of quiz questions from the Questions table in the database.
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM Questions";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int questionID = resultSet.getInt("questionID");
                String question = resultSet.getString("question");
                String optionA = resultSet.getString("optionA");
                String optionB = resultSet.getString("optionB");
                String optionC = resultSet.getString("optionC");
                String optionD = resultSet.getString("optionD");
                String correctAnswer = resultSet.getString("correctAnswer");

                questionList.add(new Question(questionID, question, optionA, optionB, optionC, optionD, correctAnswer));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questionList;
    }

public void storeResult(QuizResult result) {
        // Store the quiz result in the Results table in the database.
        try {
            String insertQuery = "INSERT INTO Results (studentID, score) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, result.getStudentID());
            preparedStatement.setInt(2, result.getScore());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Result stored in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


public List<QuizResult> getResults() {
        // Retrieve and return quiz results in ascending order from the Results table in the database.
        List<QuizResult> resultList = new ArrayList<>();
        String query = "SELECT * FROM Results ORDER BY score ASC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int resultID = resultSet.getInt("resultID");
                int studentID = resultSet.getInt("studentID");
                int score = resultSet.getInt("score");

                resultList.add(new QuizResult(studentID, score));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

public QuizResult getResultsById(int studentID) {
        // Retrieve a student's score by their ID from the Results table in the database.
        String query = "SELECT * FROM Results WHERE studentID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, studentID);
 ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int resultID = resultSet.getInt("resultID");
                int score = resultSet.getInt("score");

                return new QuizResult(studentID, score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

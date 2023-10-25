package com.quizapp;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class QuizApplication {
    public static void main(String[] args) {
        DatabaseHandler databaseHandler = new DatabaseHandler();

        try {
            String DB_URL = "jdbc:mysql://localhost:3306/quizapp ";
            String DB_USER = "root";
            String DB_PASSWORD = "Badalisgreat06";

            databaseHandler.connect("jdbc:mysql://localhost:3306/quizapp","root", "Badalisgreat06");

            System.out.println("Connected to the database.");

            Scanner scanner = new Scanner(System.in);

  while (true) {
    System.out.println("Menu:");
    System.out.println("1. Student Registration");
    System.out.println("2. Student Login");
    System.out.println("3. Display Questions");
    System.out.println("4. Store Quiz Result");
    System.out.println("5. Display All Students' Scores (Ascending   Order)");
    System.out.println("6. Fetch Student Score by ID");
    System.out.println("7. Add a Question");
    System.out.println("8. Quit");
    System.out.print("Enter your choice: ");
    int choice = scanner.nextInt();

                if (choice == 1) {
                    studentRegistration(databaseHandler, scanner);
                } else if (choice == 2) {
                    studentLogin(databaseHandler, scanner);
                } else if (choice == 3) {
                    displayQuestions(databaseHandler, scanner);
                } else if (choice == 4) {
                    storeQuizResult(databaseHandler, scanner);
                } else if (choice == 5) {
                    displayScoresAscendingOrder(databaseHandler);
                } else if (choice == 6) {
                    fetchStudentScoreByID(databaseHandler, scanner);
                } else if (choice == 7) {
                    addQuestion(databaseHandler, scanner);
                } else if (choice == 8) {
                    // Quit the program
                    break;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }

// Close the database connection when done
            databaseHandler.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }
    }

 public static void studentRegistration(DatabaseHandler databaseHandler, Scanner scanner) {
    // Input student details
    System.out.print("Enter the first name: ");
    String firstName = scanner.next();
    System.out.print("Enter the last name: ");
    String lastName = scanner.next();
    System.out.print("Enter the username: ");
    String username = scanner.next();
    System.out.print("Enter the password: ");
    String password = scanner.next();
    System.out.print("Enter the city: ");
    String city = scanner.next();
    System.out.print("Enter the email: ");
    String email = scanner.next();
    System.out.print("Enter the mobile number: ");
    String mobileNumber = scanner.next();

    // Create a new Student object with the provided details
    Student student = new Student(0, firstName, lastName, username, password, city, email, mobileNumber);

    // Call the method to add the student to the database
    databaseHandler.addStudent(student);
    System.out.println("Student registration successful.");
}


public static void studentLogin(DatabaseHandler databaseHandler, Scanner scanner) {
    // Input student login credentials
    System.out.print("Enter the username: ");
    String username = scanner.next();
    System.out.print("Enter the password: ");
    String password = scanner.next();

    // Call the method to retrieve the student from the database based on the username
    Student student = databaseHandler.getStudent(username);

    if (student != null && student.getPassword().equals(password)) {
        System.out.println("Login successful.");
        // You can continue with any further logic for the logged-in student.
    } else {
        System.out.println("Login failed. Please check your username and password.");
    }
}


public static void displayQuestions(DatabaseHandler databaseHandler, Scanner scanner) {
    // Call the method to retrieve the list of questions from the database
    List<Question> questions = databaseHandler.getQuestions();

    if (questions.isEmpty()) {

        System.out.println("No questions available.");

    } else {

        int questionNumber = 1;

        int score = 0; //initialize the score

        for (Question question : questions) {

            // Display the question and options
            System.out.println("Question " + questionNumber + ": " + question.getQuestion());
            System.out.println("a. " + question.getOptionA());
            System.out.println("b. " + question.getOptionB());
            System.out.println("c. " + question.getOptionC());
            System.out.println("d. " + question.getOptionD());
            System.out.println("your answer (a, b, c, d): ");
            String userAnswer = scanner.next().toLowerCase();

            if (userAnswer.equals(question.getCorrectAnswer().toLowerCase())) {
                System.out.println("Correct!");
                score++; // Increase the score for each correct answer
            } else {
                System.out.println("Incorrect!");
            }

            questionNumber++;
           
          }
     
          System.out.println("Quiz completed. Your score is: " + score);
       }
   }



public static void storeQuizResult(DatabaseHandler databaseHandler, Scanner scanner) {
    // Prompt the user to enter their username and password for authentication
    System.out.print("Enter your username: ");
    String username = scanner.next();
    System.out.print("Enter your password: ");
    String password = scanner.next();

    // Check if the provided username and password are valid (You can implement this logic)
    boolean isAuthenticated = validateUser(databaseHandler, username, password);

    if (isAuthenticated) {
        // If the user is authenticated, proceed to store the quiz result
        System.out.print("Enter your score: ");
        int score = scanner.nextInt();

        // Get the student ID based on the authenticated username
        int studentID = databaseHandler.getStudent(username).getStudentID();

        int resultID;
		// Create a QuizResult object and store the result in the database
        QuizResult result = new QuizResult(studentID, score);
        databaseHandler.storeResult(result);

        System.out.println("Result stored in the database.");
    } else {
        System.out.println("Authentication failed. Unable to store the result.");
    }
}

// Implement a method to validate the user's credentials
public static boolean validateUser(DatabaseHandler databaseHandler, String username, String password) {
    // You can implement the logic to validate the user's credentials here
    // For example, check if the username and password match a record in the database
    Student student = databaseHandler.getStudent(username);

    return student != null && student.getPassword().equals(password);
}


public static void displayScoresAscendingOrder(DatabaseHandler databaseHandler) {
    List<QuizResult> resultList = databaseHandler.getResults();

    if (resultList.isEmpty()) {
        System.out.println("No quiz results found in the database.");
        return;
    }

    // Sort the resultList in ascending order based on scores
    resultList.sort(Comparator.comparingInt(QuizResult::getScore));

    // Display the sorted results
    System.out.println("List of student scores in ascending order:");
    for (QuizResult result : resultList) {
        Student student = databaseHandler.getStudentById(result.getStudentID());
        System.out.println("Student ID: " + student.getStudentID());
        System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Score: " + result.getScore());
        System.out.println();
    }
}



public static void fetchStudentScoreByID(DatabaseHandler databaseHandler, Scanner scanner) {
    System.out.print("Enter student ID: ");
    int studentID = scanner.nextInt();

    // Retrieve the quiz result for the specified student ID
    QuizResult result = databaseHandler.getResultsById(studentID);

    if (result != null) {
        // If a result is found, display the student's score
        Student student = databaseHandler.getStudentById(studentID);
        System.out.println("Student ID: " + student.getStudentID());
        System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Score: " + result.getScore());
    } else {
        // If no result is found, display a message
        System.out.println("No quiz result found for the specified student ID.");
    }
}



public static void addQuestion(DatabaseHandler databaseHandler, Scanner scanner) {
    // Prompt the user to enter the question and options
    System.out.print("Enter the question: ");
    scanner.nextLine(); 
    String question = scanner.nextLine();
    
    System.out.println();
    
    System.out.print("Enter option A: ");
    String optionA = scanner.nextLine();
    
    System.out.print("Enter option B: ");
    String optionB = scanner.nextLine();
    
    System.out.print("Enter option C: ");
    String optionC = scanner.nextLine();
    
    System.out.print("Enter option D: ");
    String optionD = scanner.nextLine();
    
    System.out.print("Enter the correct answer (A, B, C, or D): ");
    String correctAnswer = scanner.nextLine().toUpperCase(); // Convert to uppercase for consistency

    // Create a Question object
    Question newQuestion = new Question(0, question, optionA, optionB, optionC, optionD, correctAnswer);

    // Add the question to the database
    databaseHandler.addQuestion(newQuestion);

    System.out.println("Question added successfully.");
}
}





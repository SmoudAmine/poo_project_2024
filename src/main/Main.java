package main;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dao.QuestionDAO;
import dao.QuizDAO;
import dao.ResultDAO;
import dao.UserDAO;
import entities.Question;
import entities.Quiz;
import entities.Result;
import entities.User;

public class Main {

    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String GREEN = "\033[32m";
    private static final String YELLOW = "\033[33m";
    private static final String BLUE = "\033[34m";
    private static final String CYAN = "\033[36m";

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        QuizDAO quizDAO = new QuizDAO();
        QuestionDAO questionDAO = new QuestionDAO();
        ResultDAO resultDAO = new ResultDAO();

        Scanner sc = new Scanner(System.in);
        try {
            int continuerprog;
            do {
                System.out.println("Continuer le programme? 1-Oui 2-Non");
                continuerprog = getValidInteger(sc, "Veuillez entrer 1 pour Oui ou 2 pour Non.");

                if (continuerprog == 2) {
                    break;
                }

                System.out.println("1-Enseignant 2-Etudiant");
                int profil = getValidInteger(sc, "Veuillez entrer 1 pour Enseignant ou 2 pour Etudiant.");

                switch (profil) {
                    case 1:
                        handleEnseignant(sc, quizDAO, questionDAO);
                        break;
                    case 2:
                        handleEtudiant(userDAO, quizDAO, questionDAO, resultDAO);
                        break;
                    default:
                        System.out.println("Choix invalide.");
                        break;
                }
            } while (continuerprog == 1);
        } finally {
            sc.close(); // Ensure the scanner is closed in the end
        }
    }

    private static void handleEnseignant(Scanner sc, QuizDAO quizDAO, QuestionDAO questionDAO) {
        System.out.println("Que voulez-vous faire? 1-Ajouter 2-Supprimer 3-Afficher les résultats");
        int choixenseignant = getValidInteger(sc, "Veuillez entrer 1 pour Ajouter, 2 pour Supprimer ou 3 pour Afficher les résultats.");

        switch (choixenseignant) {
            case 1:
                addQuiz(quizDAO, questionDAO);
                break;
            case 2:
                removeQuiz(quizDAO);
                break;
            case 3:
                displayResults();
                break;
            default:
                System.out.println("Choix invalide.");
                break;
        }
    }

    private static void addQuiz(QuizDAO quizDAO, QuestionDAO questionDAO) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Donner le nom du quiz:");
        String quizName = sc.nextLine();

        // Create and add quiz
        Quiz quiz = new Quiz(quizName);
        int quizId = quizDAO.addQuiz(quiz.getName()); // Retrieve the generated quiz ID
        quiz.setId(quizId); // Set the ID to the quiz object

        System.out.println("Donner le nombre de questions (nb: il ne faut pas dépasser 10 questions):");
        int nQ = getValidInteger(sc, "Veuillez entrer un nombre entier valide pour le nombre de questions.");
        nQ = Math.min(nQ, 10); // Limit the number of questions to 10

        // Loop to add questions
        for (int i = 0; i < nQ; i++) {
            System.out.println("Donner la question:");
            String questionText = sc.nextLine();

            System.out.println("Donner la réponse:");
            String answer = sc.nextLine();

            // Create and add question
            Question question = new Question(quiz.getId(), questionText, answer);
            questionDAO.addQuestion(question);
        }
    }

    private static void removeQuiz(QuizDAO quizDAO) {
        Scanner sc = new Scanner(System.in);

        // Display existing modules
        List<Quiz> quizzes = quizDAO.getAllQuizzes(); // Fetch existing quizzes from the DAO
        if (quizzes.isEmpty()) {
            System.out.println("Aucun module disponible.");
            return;
        }

        System.out.println("Les modules existants:");
        for (int i = 0; i < quizzes.size(); i++) {
            System.out.println("numéro: " + quizzes.get(i).getId() + " name: " + quizzes.get(i).getName());
        }

        // Handle user input for module deletion
        int moduleToDelete = -1;
        while (moduleToDelete < 1 || moduleToDelete > quizzes.size()) {
            System.out.println("Donner le module à effacer (choisir par le numéro): ");
            moduleToDelete = getValidInteger(sc, "Veuillez entrer un numéro valide pour le module à effacer.");
        }

        // Delete the selected module
        quizDAO.deleteQuiz(moduleToDelete);

        System.out.println("Module effacé avec succès.");
    }

    private static void handleEtudiant(UserDAO userDAO, QuizDAO quizDAO, QuestionDAO questionDAO, ResultDAO resultDAO) {
        Scanner sc = new Scanner(System.in);
        System.out.println("1-Tester vos connaissances 2-Visualiser vos résultats");
        int choixEtud = getValidInteger(sc, "Veuillez entrer 1 pour Tester vos connaissances ou 2 pour Visualiser vos résultats.");

        switch (choixEtud) {
            case 1:
                testKnowledge(userDAO, quizDAO, questionDAO, resultDAO);
                break;
            case 2:
                viewResults(resultDAO);
                break;
            default:
                System.out.println("Choix invalide.");
                break;
        }
    }

    private static void testKnowledge(UserDAO userDAO, QuizDAO quizDAO, QuestionDAO questionDAO, ResultDAO resultDAO) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Première fois? 1-Oui 2-Non");
        int premier = getValidInteger(sc, "Veuillez entrer 1 pour Oui ou 2 pour Non.");
        User user = null;
        int userId = 0;

        if (premier == 1) {
            System.out.println("Donner votre nom:");
            String name = sc.nextLine();
            System.out.println("Donner votre prénom:");
            String surname = sc.nextLine();
            System.out.println("Donner votre groupe:");
            int groupId = getValidInteger(sc, "Veuillez entrer un nombre entier valide pour le groupe.");
            System.out.println("Donner votre numéro CIN:");
            int cin = getValidInteger(sc, "Veuillez entrer un nombre entier valide pour le numéro CIN.");

            user = new User(name, surname, groupId, cin);
            userId = userDAO.addUser(user);
        } else {
            System.out.println("Donner votre numéro CIN:");
            int cin = getValidInteger(sc, "Veuillez entrer un nombre entier valide pour le numéro CIN.");
            user = userDAO.getUserByCin(cin);
            if (user == null) {
                System.out.println("Utilisateur non trouvé.");
                return;
            }
            userId = user.getId();
        }

        System.out.println("Les modules existants:");
        ArrayList<Quiz> quizzes = quizDAO.getAllQuizzes();
        for (int j = 0; j < quizzes.size(); j++) {
            System.out.println((j + 1) + "-" + quizzes.get(j).getName());
        }

        System.out.println("Donner le module:");
        int numQ = getValidInteger(sc, "Veuillez entrer un nombre entier valide pour le module.");

        Quiz selectedQuiz = quizzes.get(numQ - 1);
        ArrayList<Question> questions = questionDAO.getQuestionsByQuizId(selectedQuiz.getId());
        int score = calculateScore(questions);

        System.out.println("Votre score: " + score);
        System.out.println("Les réponses correctes sont:");
        for (Question q : questions) {
            System.out.println(q.getQuestionText() + " - Réponse: " + q.getAnswer());
        }

        Result result = new Result(userId, selectedQuiz.getId(), score);
        resultDAO.addResult(result);
    }

    private static int calculateScore(ArrayList<Question> questions) {
        Scanner sc = new Scanner(System.in);
        int score = 0;
        for (Question question : questions) {
            System.out.println(question.getQuestionText());
            System.out.println("Donner la réponse:");
            String answer = sc.nextLine();
            if (answer.equals(question.getAnswer())) {
                score++;
            }
        }
        return score;
    }

    private static void viewResults(ResultDAO resultDAO) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Donner votre numéro CIN:");
        int cin = getValidInteger(sc, "Veuillez entrer un nombre entier valide pour le numéro CIN.");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByCin(cin);
        if (user == null) {
            System.out.println("Utilisateur non trouvé.");
            return;
        }

        ArrayList<Result> results = resultDAO.getResultsByUserId(user.getId());
        if (results.isEmpty()) {
            System.out.println("Aucun résultat trouvé pour cet utilisateur.");
        } else {
            System.out.println("Vos résultats:");
            for (Result result : results) {
                System.out.println("Quiz ID: " + result.getQuizId() + ", Score: " + result.getScore() + ", Date: "
                        + result.getAttemptDate());
            }
        }
    }

    private static void displayResults() {
        Scanner sc = new Scanner(System.in);
        ResultDAO resultDAO = new ResultDAO();
        UserDAO userDAO = new UserDAO();
        QuizDAO quizDAO = new QuizDAO();

        System.out.println("1-Afficher les résultats de tous les étudiants 2-Afficher les résultats d'un étudiant spécifique");
        int choice = getValidInteger(sc, "Veuillez entrer 1 pour afficher les résultats de tous les étudiants ou 2 pour un étudiant spécifique.");

        if (choice == 1) {
            // Display results for all students
            ArrayList<Result> results = resultDAO.getAllResults();
            printResultsTable(results, userDAO, quizDAO);
        } else if (choice == 2) {
            // Display results for a specific student
            System.out.println("Donner le numéro CIN de l'étudiant:");
            int cin = getValidInteger(sc, "Veuillez entrer un nombre entier valide pour le numéro CIN.");
            User user = userDAO.getUserByCin(cin);
            if (user == null) {
                System.out.println("Utilisateur non trouvé.");
                return;
            }
            ArrayList<Result> results = resultDAO.getResultsByUserId(user.getId());
            printResultsTable(results, userDAO, quizDAO);
        } else {
            System.out.println("Choix invalide.");
        }
    }

    private static void printResultsTable(ArrayList<Result> results, UserDAO userDAO, QuizDAO quizDAO) {
        // Print table header
        System.out.println(CYAN + BOLD
                + String.format("%-10s %-30s %-10s %-15s", "User ID", "Quiz Name", "Score", "Date") + RESET);

        for (Result result : results) {
            User user = userDAO.getUserById(result.getUserId());
            Quiz quiz = quizDAO.getQuizById(result.getQuizId());

            // Print each result row
            System.out.println(BLUE + String.format("%-10d %-30s %-10d %-15s", user.getId(), quiz.getName(),
                    result.getScore(), result.getAttemptDate()) + RESET);
        }
    }

    private static int getValidInteger(Scanner sc, String errorMessage) {
        int result = -1;
        boolean validInput = false;
        while (!validInput) {
            try {
                result = sc.nextInt();
                validInput = true; // Valid input received, exit loop
            } catch (InputMismatchException e) {
                System.out.println(errorMessage);
                sc.next(); // Clear the invalid input
            }
        }
        sc.nextLine(); // Clear the newline character
        return result;
    }
}

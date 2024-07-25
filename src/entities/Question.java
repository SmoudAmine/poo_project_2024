package entities;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private int quizId;
    private String questionText;
    private String answer;
    private String correctAnswer; // Added to store the correct answer

    public Question() {
    	
    }
    
    public Question(int quizId, String questionText, String answer, String correctAnswer) {
        this.quizId = quizId;
        this.questionText = questionText;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    public List<String> getAnswerList() {
        List<String> answerList = new ArrayList<>();
        if (answer != null && !answer.isEmpty()) {
            String[] answerArray = answer.split("\\|");
            for (String answer : answerArray) {
                answerList.add(answer);
            }
        }
        return answerList;
    }
}

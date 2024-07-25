package entities;

public class Question {
    private int id;
    private int quizId;
    private String questionText;
    private String answer;

    // Constructors
    public Question() {}

    public Question(int quizId, String questionText, String answer) {
        this.quizId = quizId;
        this.questionText = questionText;
        this.answer = answer;
    }

    // Getters and Setters
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
}

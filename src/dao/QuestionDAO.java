package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entities.Question;

public class QuestionDAO {
    
	public void addQuestion(Question question) {
	    try (Connection connection = DBConnection.getConnection()) {
	        String sql = "INSERT INTO Question (quiz_id, question_text, answer, correct_answer) VALUES (?, ?, ?, ?)";
	        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        statement.setInt(1, question.getQuizId());
	        statement.setString(2, question.getQuestionText());
	        statement.setString(3, question.getAnswer()); // Store answers as a single string
	        statement.setString(4, question.getCorrectAnswer()); // Store the correct answer
	        statement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public ArrayList<Question> getQuestionsByQuizId(int quizId) {
	    ArrayList<Question> questions = new ArrayList<>();
	    try (Connection connection = DBConnection.getConnection()) {
	        String sql = "SELECT * FROM Question WHERE quiz_id = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setInt(1, quizId);
	        ResultSet resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            Question question = new Question();
	            question.setId(resultSet.getInt("id"));
	            question.setQuizId(resultSet.getInt("quiz_id"));
	            question.setQuestionText(resultSet.getString("question_text"));

	            // Retrieve answers as a single string
	            String answersString = resultSet.getString("answer");
	            question.setAnswer(answersString);

	            // Retrieve correct answer
	            question.setCorrectAnswer(resultSet.getString("correct_answer"));
	            questions.add(question);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return questions;
	}


    public void deleteQuestion(int questionId) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "DELETE FROM Question WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, questionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteQuestionsByQuizId(int quizId) throws SQLException {
        String sql = "DELETE FROM question WHERE quiz_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quizId);
            statement.executeUpdate();
        }
    }
}

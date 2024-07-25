package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.Question;

public class QuestionDAO {
    
    public void addQuestion(Question question) {
        try (Connection connection = DBConnection.getConnection()){
            String sql = "INSERT INTO question (quiz_id, question_text, answer) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, question.getQuizId()); // Set the quiz_id from the Question object
            statement.setString(2, question.getQuestionText());
            statement.setString(3, question.getAnswer());
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
                question.setAnswer(resultSet.getString("answer"));
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
}

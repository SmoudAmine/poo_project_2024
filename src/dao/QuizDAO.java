package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entities.Quiz;
import entities.User;

public class QuizDAO {

    public int addQuiz(String name) {
        int quizId = 0;
        try(Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO quiz (name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                quizId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizId;
    }

    public void addQuestion(int quizId, String questionText, String answer) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO Question (quiz_id, question_text, answer) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quizId);
            statement.setString(2, questionText);
            statement.setString(3, answer);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Quiz> getAllQuizzes() {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Quiz";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Quiz quiz = new Quiz(resultSet.getString("name"));
                quiz.setId(resultSet.getInt("id"));
				quizzes.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

	public Quiz getQuizById(int quizId) {
		 try (Connection connection = DBConnection.getConnection()) {
	            String sql = "SELECT * FROM Quiz WHERE id = ?";
	            PreparedStatement statement = connection.prepareStatement(sql);
	            statement.setInt(1, quizId);
	            ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					return new Quiz(resultSet.getString("name"));
				}
		 }catch (SQLException e) {
	            e.printStackTrace();
		}
		return null;
	}

	 public void deleteQuiz(int quizId) {
	        String sql = "DELETE FROM quiz WHERE id = ?";
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, quizId);
	            int rowsAffected = pstmt.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Le module a été effacé avec succès.");
	            } else {
	                System.out.println("Aucun module trouvé avec cet identifiant.");
	            }

	        } catch (SQLException e) {
	            System.out.println("Erreur lors de la suppression du module : " + e.getMessage());
	        }
	    }
}

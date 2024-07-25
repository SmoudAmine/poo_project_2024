package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import entities.Result;

public class ResultDAO {
	public void addResult(Result result) {
		if (!checkUserExists(result.getUserId()) || !checkQuizExists(result.getQuizId())) {
			System.out.println("Error: User ID or Quiz ID does not exist.");
			return;
		}

		try (Connection connection = DBConnection.getConnection()) {
			String sql = "INSERT INTO Result (user_id, quiz_id, score, attempt_date) VALUES (?, ?, ?, ?)";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, result.getUserId());
				statement.setInt(2, result.getQuizId());
				statement.setInt(3, result.getScore());
				statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				statement.executeUpdate();
				System.out.println("Result added successfully.");
			}
		} catch (SQLException e) {
			System.out.println("Error adding result: " + e.getMessage());
		}
	}

	private boolean checkUserExists(int userId) {
		String sql = "SELECT COUNT(*) FROM user WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, userId);

			// Execute the query and retrieve the result
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// Get the count from the result set
					int count = resultSet.getInt(1);
					return count > 0;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error checking user existence: " + e.getMessage());
		}
		return false;
	}

	private boolean checkQuizExists(int quizId) {
		String sql = "SELECT COUNT(*) FROM quiz WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, quizId);

			// Execute the query and retrieve the result
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// Get the count from the result set
					int count = resultSet.getInt(1);
					return count > 0;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error checking quiz existence: " + e.getMessage());
		}
		return false;
	}

	public ArrayList<Result> getResultsByUserId(int userId) {
		ArrayList<Result> results = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			String sql = "SELECT * FROM Result WHERE user_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, userId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Result result = new Result();
				result.setId(resultSet.getInt("id"));
				result.setUserId(resultSet.getInt("user_id"));
				result.setQuizId(resultSet.getInt("quiz_id"));
				result.setScore(resultSet.getInt("score"));
				result.setAttemptDate(resultSet.getTimestamp("attempt_date"));
				results.add(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	public void deleteResult(int resultId) {
		try (Connection connection = DBConnection.getConnection()) {
			String sql = "DELETE FROM Result WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, resultId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Result> getAllResults() {
		ArrayList<Result> results = new ArrayList<>();
		String sql = "SELECT * FROM Result";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Result result = new Result();
				result.setId(rs.getInt("id"));
				result.setUserId(rs.getInt("user_id"));
				result.setQuizId(rs.getInt("quiz_id"));
				result.setScore(rs.getInt("score"));
				result.setAttemptDate(rs.getTimestamp("attempt_date"));
				results.add(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
    public void deleteResultsByQuizId(int quizId) throws SQLException {
        String sql = "DELETE FROM result WHERE quiz_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quizId);
            statement.executeUpdate();
        }
    }

}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entities.User;

public class UserDAO {
	
	
	 public int addUser(User user) {
	        int userId = 0;
			String sql = "INSERT INTO User (name, surname, group_id, cin) VALUES (?, ?, ?, ?)";

	        try (Connection connection = DBConnection.getConnection();
	             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	            statement.setString(1, user.getName());
	            statement.setString(2, user.getSurname());
	            statement.setInt(3, user.getGroupId());
	            statement.setInt(4, user.getCin());

	            int affectedRows = statement.executeUpdate();

	            if (affectedRows > 0) {
	                ResultSet generatedKeys = statement.getGeneratedKeys();
	                if (generatedKeys.next()) {
	                    userId = generatedKeys.getInt(1);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return userId;
	    }
	
	public User getUserById(int id) {
		try (Connection connection = DBConnection.getConnection()) {
			String sql = "SELECT * FROM User WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				User user = new User(resultSet.getString("name"), resultSet.getString("surname"),
						resultSet.getInt("group_id"), resultSet.getInt("cin"));
				user.setId(resultSet.getInt("id"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User getUserByCin(int cin) {
		try (Connection connection = DBConnection.getConnection()) {
			String sql = "SELECT * FROM User WHERE cin = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, cin);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				User user = new User(resultSet.getString("name"), resultSet.getString("surname"),
						resultSet.getInt("group_id"), resultSet.getInt("cin"));
				user.setId(resultSet.getInt("id"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

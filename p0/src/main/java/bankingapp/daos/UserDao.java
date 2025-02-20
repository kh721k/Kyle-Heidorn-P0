package bankingapp.daos;

import bankingapp.models.User;
import bankingapp.utils.ConnectionUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    Connection connection;

    //Initializes the connection using the ConnectionUtils class
    public UserDao(Connection connection) throws SQLException, IOException {
        this.connection = ConnectionUtils.getConnection();
    }

    //create and update user
    public User saveUser(User user) throws SQLException {
        if (user.getUserId() == null) {
            // If the user ID is null, it means the user is new and you need to insert a new record
            String sql = "INSERT INTO users (username, password, email, first_name, last_name) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getFirstName());
                pstmt.setString(5, user.getLastName());
                pstmt.executeUpdate();

                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    user.setUserId(keys.getInt(1));
                }
            }
        } else {
            // If the user ID is not null, it means you need to update an existing record
            String sql = "UPDATE users SET username = ?, password = ?, email = ?, first_name = ?, last_name = ? WHERE user_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getFirstName());
                pstmt.setString(5, user.getLastName());
                pstmt.setInt(6, user.getUserId());
                pstmt.executeUpdate();
            }
        }
    return user;

    }

    //read user by their id
    public User readUserId(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet results = pstmt.executeQuery();

        User user = new User();
        if (results.next()) {
            user.setUserId(results.getInt("user_id"));
            user.setUsername(results.getString("username"));
            user.setPassword(results.getString("password"));
            user.setEmail(results.getString("email"));
            user.setFirstName(results.getString("first_name"));
            user.setLastName(results.getString("last_name"));
        } else {
            return null;
        }

        return user;
    }

    //read user by username
    public User readUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet results = pstmt.executeQuery();

        User user = new User();
        if(results.next()) {
            user.setUserId(results.getInt("user_id"));
            user.setUsername(results.getString("username"));
            user.setPassword(results.getString("password"));
            user.setEmail(results.getString("email"));
            user.setFirstName(results.getString("first_name"));
            user.setLastName(results.getString("last_name"));
        }

        return user;
    }

    //delete user by their id
    public User deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.execute();
        }
        return null;
    }
}

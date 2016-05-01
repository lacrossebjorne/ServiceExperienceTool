package com.set.dao;

import static com.set.dao.DAOUtil.prepareStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.set.entities.ResetPassword;
import com.set.entities.User;

/**
 * 
 * @author Björn Dalberg
 * @created 19 apr. 2016
 */

public class ResetPasswordDAOJDBC implements ResetPasswordDAO {

	private static final String SQL_FIND_RESET_PASSWORD_BY_ID = "SELECT * FROM setdb.reset_password WHERE reset_password_id = ?";
	private static final String SQL_FIND_RESET_PASSWORD_BY_USERNAME = "SELECT * FROM setdb.reset_password as rp join setdb.user as u on rp.user_id = u.user_id Where u.user_name = ? order by rp.user_id;";
	private static final String SQL_INSERT_RESET_PASSWORD = "INSERT INTO setdb.reset_password (securitycode, user_id) VALUES (?, ?)";
	private DAOFactory daoFactory;

	public ResetPasswordDAOJDBC(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public ResetPassword find(Long resetID) {
		return find(SQL_FIND_RESET_PASSWORD_BY_ID, resetID);
	}

	@Override
	public ResetPassword find(String username) {
		return find(SQL_FIND_RESET_PASSWORD_BY_USERNAME, username);
	}

	private ResetPassword find(String sql, Object value) {
		ResetPassword reset = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = daoFactory.getConnection();
			statement = prepareStatement(connection, sql, false, value);
			resultSet = statement.executeQuery();
			if (resultSet.next())
				reset = processQuery(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return reset;
	}

	@Override
	public void insert(ResetPassword resetPassword) {
		if (resetPassword.getResetPasswordId() == null)
			throw new IllegalArgumentException("The resetpassword-object does not exists");

		Object[] resetPassObj = { resetPassword.getSecuritycode(), resetPassword.getUser().getUserId() };
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet keys = null;
		try {
			connection = daoFactory.getConnection();
			statement = prepareStatement(connection, SQL_INSERT_RESET_PASSWORD, true, resetPassObj);
			int insertedRows = statement.executeUpdate();
			if (insertedRows == 1) {
				keys = statement.getGeneratedKeys();
				resetPassword.setResetPasswordId(keys.getLong(1));
				}
			else 
				throw new SQLException("New reset password could not be inserted");
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		}finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private ResetPassword processQuery(ResultSet resultSet) throws SQLException {
		// Create the user who will recieve new password
		User absentMindedUser = new User();
		absentMindedUser.setUserId(resultSet.getLong("user_id"));
		absentMindedUser.setUserName(resultSet.getString("user_name"));
		absentMindedUser.setFirstName(resultSet.getString("first_name"));
		absentMindedUser.setLastName(resultSet.getString("last_name"));
		absentMindedUser.setEmail(resultSet.getString("email"));
		absentMindedUser.setEnabled(resultSet.getBoolean("enabled"));
		absentMindedUser.setPhoneNumber(resultSet.getString("phone_number"));
		absentMindedUser.setCreatedAt(resultSet.getDate("created_at"));
		absentMindedUser.setUpdatedAt(resultSet.getDate("updated_at"));

		// Create the ForgottenPassword object and store the user within it
		ResetPassword reset = new ResetPassword();
		reset.setResetPasswordId(resultSet.getLong("forgot_password_id"));
		reset.setSecuritycode(resultSet.getString("securitycode"));
		reset.setExperationTime(resultSet.getDate("created_at"));
		reset.setUser(absentMindedUser);
		return reset;
	}

}

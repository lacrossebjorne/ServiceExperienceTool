package com.set.dao;

import static com.set.dao.DAOUtil.prepareStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.set.dao.helpers.IsoDateConverter;
import com.set.entities.ResetPassword;

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
	private IsoDateConverter isoDateConverter;

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

		Object[] resetPassObj = { resetPassword.getSecuritycode(), resetPassword.getUserId() };
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

	// Create the ForgottenPassword object and store the userId within it
	private ResetPassword processQuery(ResultSet resultSet) throws SQLException {
		ResetPassword reset = new ResetPassword();
		isoDateConverter = new IsoDateConverter();
		reset.setResetPasswordId(resultSet.getLong("forgot_password_id"));
		reset.setSecuritycode(resultSet.getString("securitycode"));
		reset.setExpirationTime(isoDateConverter.parseToUTCString(resultSet.getTimestamp("created_at")));
		reset.setUserId(resultSet.getLong("user_id"));
		return reset;
	}

}

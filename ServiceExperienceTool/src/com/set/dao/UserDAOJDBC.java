package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.set.entities.ResetPassword;
import com.set.entities.Role;
import com.set.entities.User;

import static com.set.dao.DAOUtil.*;

/**
 * 
 * @author Björn Dalberg
 * @created 19 apr. 2016
 */

public class UserDAOJDBC implements UserDAO {

	private static final String SQL_FIND_BY_ID = "SELECT user_id, user_name, first_name, last_name, email, phone_number, created_at, updated_at, enabled FROM user WHERE user_id = ? ORDER BY user_id";
	private static final String SQL_FIND_BY_USERNAME_AND_PASSWORD = "SELECT user_id, user_name, first_name, last_name, email, phone_number, created_at, updated_at, enabled FROM user WHERE user_name = ? AND password = MD5(?)";
	private static final String SQL_LIST_USERS = "CALL setdb.listUsers()"; //"SELECT * FROM user ORDER BY user_id"
	private static final String SQL_INSERT_USER = "INSERT INTO user (first_name, last_name, email, user_name, password, phone_number, enabled) VALUES (?, ?, ?, ?, MD5(?), ?, ?)";
	private static final String SQL_UPDATE_USER = "UPDATE user SET first_name = ?, last_name = ?, email = ?, user_name = ?, phone_number = ?,  enabled = ? WHERE user_id = ?";
	private static final String SQL_CHANGE_PASSWORD = "UPDATE user SET password = MD5(?) WHERE user_id = ?";
	private static final String SQL_ENABLE_USER = "UPDATE user SET enable = 1 WHERE user_id = ?";
	private static final String SQL_DISABLE_USER = "UPDATE user SET enable = 0 WHERE user_id = ?";
	private static final String SQL_EXIST_USERNAME = "SELECT user_id FROM user WHERE user_name = ?";
	private static final String SQL_DELETE_USER = "DELETE FROM user WHERE user_id = ?";
	private static final String SQL_INSERT_USER_ROLE = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
	private DAOFactory daoFactory;

	public UserDAOJDBC(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public User find(Long id) {
		return find(SQL_FIND_BY_ID, id);
	}

	@Override
	public User find(String username) {
		return find(SQL_EXIST_USERNAME, username);
	}

	@Override
	public User find(String username, String password) {
		return find(SQL_FIND_BY_USERNAME_AND_PASSWORD, username, password);
	}

	private User find(String sql, Object... values) {
		User user = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = daoFactory.getConnection();
			statement = prepareStatement(connection, sql, false, values);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				user = processResult(resultSet);
			}
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
		return user;
	}

	@Override
	public List<User> listUsers() {
		List<User> users = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(SQL_LIST_USERS);
			resultSet = statement.executeQuery();
			User user = null;
			Long userID = null;
			Role role = null;
			Long roleID = null;
			List<Role> roles = null;
			Long resetpassID = null;
			Set<ResetPassword> resetPasswordsSet = null;
			while (resultSet.next()) {
				if(userID == null || userID != resultSet.getLong("user_id")) {
					if (user != null) {
						user.setRoles(roles);
						user.setResetPasswords(resetPasswordsSet);
						users.add(user);
						roleID = null;
						resetpassID = null;
					}
					user = new User();
					user.setUserId(resultSet.getLong("user_id"));
					user.setUserName(resultSet.getString("user_name"));
					user.setFirstName(resultSet.getString("first_name"));
					user.setLastName(resultSet.getString("last_name"));
					user.setEmail(resultSet.getString("email"));
					user.setEnabled(resultSet.getBoolean("enabled"));
					user.setPhoneNumber(resultSet.getString("phone_number"));
					user.setCreatedAt(resultSet.getDate("created_at"));
					user.setUpdatedAt(resultSet.getDate("updated_at"));
					roles = new ArrayList<>();
					if ((roleID = resultSet.getLong("role_id")) != 0) {
						role = new Role();
						role.setRoleId(roleID);
						role.setName(resultSet.getString("rolename"));
						role.setDescription(resultSet.getString("role_description"));
						role.setEnabled(resultSet.getBoolean("role_enabled"));
						roles.add(role);
					}
					resetPasswordsSet = new HashSet<>();
					if ((resetpassID = resultSet.getLong("reset_password_id")) != 0) {
						ResetPassword reset = new ResetPassword();
						reset.setResetPasswordId(resetpassID);
						reset.setSecuritycode(resultSet.getString("securitycode"));
						reset.setExpirationTime(resultSet.getDate("expiration_time"));
						reset.setUserId(userID);
						resetPasswordsSet.add(reset);
					}
				} else {
					if ((roleID = resultSet.getLong("role_id")) != 0) {
						role = new Role();
						role.setRoleId(roleID);
						role.setName(resultSet.getString("rolename"));
						role.setDescription("role_description");
						role.setEnabled(resultSet.getBoolean("role_enabled"));
						roles.add(role);
					}
					if ((resetpassID = resultSet.getLong("reset_password_id")) != 0) {
						ResetPassword reset = new ResetPassword();
						reset.setResetPasswordId(resetpassID);
						reset.setSecuritycode(resultSet.getString("securitycode"));
						reset.setExpirationTime(resultSet.getDate("expiration_time"));
						reset.setUserId(userID);
						resetPasswordsSet.add(reset);
					}
				}
			}
			users.add(user);
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
		return users;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean createUser(User user) throws IllegalArgumentException {
		boolean userCreated = false;
		if (user.getUserId() != null)
			throw new IllegalArgumentException("User already exists");
		Object[] userObject = { user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserName(),
				user.getPassword(), user.getPhoneNumber(), user.isEnabled() };
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet userKeys = null;
		try {
			connection = daoFactory.getConnection();
			connection.setAutoCommit(false);
			statement = prepareStatement(connection, SQL_INSERT_USER, true, userObject);
			if (statement.executeUpdate() != 1)
				throw new SQLException("New user failed to be inserted.");
			else {
				userKeys = statement.getGeneratedKeys();
				if (userKeys.next()) {
					Long userID = userKeys.getLong(1);
					user.setUserId(userID);
					statement = connection.prepareStatement(SQL_INSERT_USER_ROLE);
					for (Role userRole : user.getRoles()) {
						statement.setLong(1, userID);
						statement.setLong(2, userRole.getRoleId());
						statement.addBatch();
					}
					if (statement.executeUpdate() == 0)
						throw new SQLException("Could not insert roles into user_roles table.");
				}
				connection.commit();
				userCreated = true;
			}
		} catch (SQLException | IllegalArgumentException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				userKeys.close();
				statement.close();
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return userCreated;
	}

	@Override
	public void updateUser(User user) {
		if (user.getUserId() == null)
			throw new IllegalArgumentException("User does not exists");

		Object[] userObject = { user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserName(),
				user.getPhoneNumber(), user.isEnabled(), user.getUserId() };
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = daoFactory.getConnection();
			connection.setAutoCommit(false);
			statement = prepareStatement(connection, SQL_UPDATE_USER, false, userObject);
			int updatedRows = statement.executeUpdate();
			if (updatedRows == 0)
				throw new SQLException("New user failed to be updated.");
			connection.commit();
		} catch (SQLException | IllegalArgumentException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				statement.close();
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteUser(User user) {
		Object[] userObject = { user.getUserId() };
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = daoFactory.getConnection();
			statement = prepareStatement(connection, SQL_DELETE_USER, false, userObject);
			int deletedRows = statement.executeUpdate();
			if (deletedRows == 1)
				user.setUserId(null);
			else
				throw new SQLException("User could not be deleted");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean existUserName(String username) {
		Object[] values = { username };
		boolean userExists = false;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = daoFactory.getConnection();
			statement = prepareStatement(connection, SQL_EXIST_USERNAME, false, values);
			resultSet = statement.executeQuery();
			userExists = resultSet.next();
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
		return userExists;
	}

	@Override
	public void changePassword(User user) {
		if (user.getUserId() == null)
			throw new IllegalArgumentException("User does not exist");

		Object[] userObject = { user.getPassword(), user.getUserId() };
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = daoFactory.getConnection();
			statement = prepareStatement(connection, SQL_CHANGE_PASSWORD, false, userObject);
			int updatedRows = statement.executeUpdate();
			if (updatedRows == 0)
				throw new SQLException("Password could not be updated");
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void enableUser(User user) {
		if (user.getUserId() == null)
			throw new IllegalArgumentException("User does not exist");

		Object[] userObject = { user.isEnabled(), user.getUserId() };
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = daoFactory.getConnection();
			statement = prepareStatement(connection, SQL_ENABLE_USER, false, userObject);
			int updatedRows = statement.executeUpdate();
			if (updatedRows == 0)
				throw new SQLException("Unable to enable user");
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void disableUser(User user) {
		if (user.getUserId() == null)
			throw new IllegalArgumentException("User does not exist");

		Object[] userObject = { user.isEnabled(), user.getUserId() };
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = daoFactory.getConnection();
			statement = prepareStatement(connection, SQL_DISABLE_USER, false, userObject);
			int updatedRows = statement.executeUpdate();
			if (updatedRows == 0)
				throw new SQLException("Unable to disable user");
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private User processResult(ResultSet resultSet) throws SQLException {
		User user = new User();
		user.setUserId(resultSet.getLong("user_id"));
		user.setUserName(resultSet.getString("user_name"));
		user.setFirstName(resultSet.getString("first_name"));
		user.setLastName(resultSet.getString("last_name"));
		user.setEmail(resultSet.getString("email"));
		user.setEnabled(resultSet.getBoolean("enabled"));
		user.setPhoneNumber(resultSet.getString("phone_number"));
		user.setCreatedAt(resultSet.getDate("created_at"));
		user.setUpdatedAt(resultSet.getDate("updated_at"));
		return user;
	}
}

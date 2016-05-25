package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.set.dao.helpers.IsoDateConverter;
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

	private static final String SQL_FIND_BY_ID = "CALL setdb.listUserByID(?)";
	private static final String SQL_FIND_BY_USERNAME_AND_PASSWORD = "CALL setdb.listUserByUsernameAndPassword(?, MD5(?))";
	private static final String SQL_LIST_USERS = "CALL setdb.listUsers()";
	private static final String SQL_EXIST_USERNAME = "CALL setdb.listUserByUsername(?)";
	private static final String SQL_INSERT_USER = "INSERT INTO user (first_name, last_name, email, user_name, password, phone_number, enabled) VALUES (?, ?, ?, ?, MD5(?), ?, ?)";
	private static final String SQL_UPDATE_USER = "UPDATE user SET first_name = ?, last_name = ?, email = ?, user_name = ?, phone_number = ?,  enabled = ? WHERE user_id = ?";
	private static final String SQL_CHANGE_PASSWORD = "UPDATE user SET password = MD5(?) WHERE user_id = ?";
	private static final String SQL_ENABLE_USER = "UPDATE user SET enable = 1 WHERE user_id = ?";
	private static final String SQL_DISABLE_USER = "UPDATE user SET enable = 0 WHERE user_id = ?";
	private static final String SQL_DELETE_USER = "DELETE FROM user WHERE user_id = ?";
	private static final String SQL_INSERT_USER_ROLE = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
	private DAOFactory daoFactory;
	private IsoDateConverter isoDateConverter;
	
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
		List<User> userAsList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = daoFactory.getConnection();
			statement = prepareStatement(connection, sql, false, values);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				userAsList.add(processUser(resultSet));
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
		return mergeAll(userAsList).get(0);
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
			while (resultSet.next()) {
				users.add(processUser(resultSet));
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
		return mergeAll(users);
	}

	@SuppressWarnings("resource")
	@Override
	public User createUser(User user) throws IllegalArgumentException {
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
			Long userID = null;
			if (statement.executeUpdate() != 1)
				throw new SQLException("New user failed to be inserted.");
			else {
				userKeys = statement.getGeneratedKeys();
				if (userKeys.next()) {
					userID = userKeys.getLong(1);
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
				user.setUserId(userID);
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
		return user;
	}

	@SuppressWarnings("resource")
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
			if(!user.getRoles().isEmpty()) {
				statement = connection.prepareStatement(SQL_INSERT_USER_ROLE);
				for (Role userRole : user.getRoles()) {
					statement.setLong(1, user.getUserId());
					statement.setLong(2, userRole.getRoleId());
					statement.addBatch();
				}
				if (statement.executeUpdate() == 0)
					throw new SQLException("Could not insert roles into user_roles table.");
			}
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

	//Processes the resultSet from the database and creates an User entity for each row returned
	private User processUser(ResultSet resultSet) throws SQLException {
		User user = new User();
		isoDateConverter = new IsoDateConverter();
		user.setUserId(resultSet.getLong("user_id"));
		user.setUserName(resultSet.getString("user_name"));
		user.setFirstName(resultSet.getString("first_name"));
		user.setLastName(resultSet.getString("last_name"));
		user.setEmail(resultSet.getString("email"));
		user.setEnabled(resultSet.getBoolean("enabled"));
		user.setPhoneNumber(resultSet.getString("phone_number"));
		user.setCreatedAt(isoDateConverter.parseToUTCString(resultSet.getDate("created_at")));
		if (resultSet.getDate("updated_at") != null)
			user.setUpdatedAt(isoDateConverter.parseToUTCString(resultSet.getDate("updated_at")));
		List<Role> roles = new ArrayList<>();
		Long roleID = null;
		if ((roleID = resultSet.getLong("role_id")) != 0) {
			Role role = new Role();
			role.setRoleId(roleID);
			role.setName(resultSet.getString("rolename"));
			role.setDescription(resultSet.getString("role_description"));
			role.setEnabled(resultSet.getBoolean("role_enabled"));
			roles.add(role);
			user.setRoles(roles);
		}
		Set<ResetPassword> resetPasswordsSet = new HashSet<>();
		Long resetpassID = null;
		if ((resetpassID = resultSet.getLong("reset_password_id")) != 0) {
			ResetPassword reset = new ResetPassword();
			reset.setResetPasswordId(resetpassID);
			reset.setSecuritycode(resultSet.getString("securitycode"));
			if (resultSet.getTimestamp("expiration_time") != null)
				reset.setExpirationTime(isoDateConverter.parseToUTCString(resultSet.getTimestamp("expiration_time")));
			reset.setUserId(user.getUserId());
			resetPasswordsSet.add(reset);
			user.setResetPasswords(resetPasswordsSet);
		}
		return user;
	}

	/*
	 * Merges collected user entities from database 
	 * (multiple rows with same userId can be returned from the stored procedure) 
	 * who have the same userId - on roles and resetpasswords
	 */
	private List<User> mergeAll(Collection<User> input) {
		return new ArrayList<>(input.stream().collect(Collectors.toMap(User::getUserId, e -> e, User::merge)).values());
	}
}

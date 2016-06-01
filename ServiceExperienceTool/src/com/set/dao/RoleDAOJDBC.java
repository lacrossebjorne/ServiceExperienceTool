package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.set.entities.Role;
import com.set.entities.User;

import static com.set.dao.DAOUtil.*;

/**
 * @author Björn Dalberg
 * @created 4 maj 2016
 */
public class RoleDAOJDBC implements RoleDAO {

	private static final String SQL_CREATE_ROLE = "INSERT INTO role (name, description, enabled) VALUES (?, ?, ?)";
	private static final String SQL_FIND_BY_ID = "CALL setdb.listRolesByID(?)";
	private static final String SQL_FIND_BY_ROLE_NAME = "SELECT * FROM role WHERE name = ? ORDER BY role_id";
	private static final String SQL_LIST_ALL_ROLES = "call setdb.listRoles();";
	private static final String SQL_UPDATE_ROLE = "UPDATE role SET name = ?, description = ?, enabled = ? WHERE role_id = ?";
	private static final String SQL_DELETE_ROLE = "DELETE FROM role WHERE role_id = ?";
	private DAOFactory daoFactory;

	public RoleDAOJDBC(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Role find(Long id) {
		return find(SQL_FIND_BY_ID, id);
	}

	@Override
	public Role find(String name) {
		return find(SQL_FIND_BY_ROLE_NAME, name);
	}

	private Role find(String sql, Object... values) {
		List<Role> roleAsList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = daoFactory.getConnection();
			statement = prepareStatement(connection, sql, false, values);
			resultSet = statement.executeQuery();
			if(resultSet.next()) {
				roleAsList.add(processResult(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException | NullPointerException e1) {
				e1.printStackTrace();
			}
		}
		roleAsList = mergeAll(roleAsList);
		return roleAsList.get(0);
	}

	@Override
	public List<Role> listRoles() {
		List<Role> roles = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(SQL_LIST_ALL_ROLES);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				roles.add(processResult(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException | NullPointerException e1) {
				e1.printStackTrace();
			}
		}
		return mergeAll(roles);
	}

	@Override
	public Role createRole(Role role) {
		if (role.getRoleId() != null)
			throw new IllegalArgumentException("Role already exist");
		Object[] roleObj = { role.getName(), role.getDescription(), role.isEnabled() };
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet keys = null;
		try {
			connection = daoFactory.getConnection();
			connection.setAutoCommit(false);
			statement = prepareStatement(connection, SQL_CREATE_ROLE, true, roleObj);
			if (statement.executeUpdate() != 1)
				throw new SQLException("New role failed to be inserted");
			else {
				keys = statement.getGeneratedKeys();
				if (keys.next())
					role.setRoleId(keys.getLong(1));
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
				connection.setAutoCommit(true);
				if (keys != null)
					keys.close();
				statement.close();
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return role;
	}

	@Override
	public boolean updateRole(Role role) {
		boolean isUpdated = false;
		if (role.getRoleId() == null)
			throw new IllegalArgumentException("Role does not exist");
		Object[] roleObj = { role.getName(), role.getDescription(), role.isEnabled(), role.getRoleId() };
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet keys = null;
		try {
			connection = daoFactory.getConnection();
			connection.setAutoCommit(false);
			statement = prepareStatement(connection, SQL_UPDATE_ROLE, false, roleObj);
			if (statement.executeUpdate() == 0)
				throw new SQLException("New role failed to be inserted");
			connection.commit();
			isUpdated = true;
		} catch (SQLException | IllegalArgumentException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
				if (keys != null)
					keys.close();
				statement.close();
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return isUpdated;
	}

	@Override
	public boolean deleteRole(Role role) {
		boolean isDeleted = false;
		if (role.getRoleId() == null)
			throw new IllegalArgumentException("Role does not exist");
		Object[] roleObj = { role.getRoleId() };
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet keys = null;
		try {
			connection = daoFactory.getConnection();
			connection.setAutoCommit(false);
			statement = prepareStatement(connection, SQL_DELETE_ROLE, false, roleObj);
			if (statement.executeUpdate() == 0)
				throw new SQLException("Role failed to be deleted");
			else
				role.setRoleId(null);
			connection.commit();
			isDeleted = true;
		} catch (SQLException | IllegalArgumentException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
				if (keys != null)
					keys.close();
				statement.close();
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return isDeleted;
	}
	
	@Override
	public void enableRole(Role role) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableRole(Role role) {
		// TODO Auto-generated method stub

	}
	
	/*
	 * Merges collected user entities from database (multiple rows with same
	 * userId can be returned from the stored procedure) who have the same
	 * userId - on roles and resetpasswords
	 */
	private List<Role> mergeAll(Collection<Role> input) {
		return new ArrayList<>(input.stream().collect(Collectors.toMap(Role::getRoleId, e -> e, Role::merge)).values());
	}
	
	/**
	 * @param resultSet
	 * @return Role
	 */
	private Role processResult(ResultSet resultSet) throws SQLException {
		Role role = new Role();
		role.setRoleId(resultSet.getLong("role_id"));
		role.setName(resultSet.getString("name"));
		role.setDescription(resultSet.getString("description"));
		role.setEnabled(resultSet.getBoolean("enabled"));
		List<User> userList = new ArrayList<>();
		Long userID = null;
		if((userID = resultSet.getLong("user_id")) != 0) {
			User user = new User();
			user.setUserId(userID);
			user.setUserName(resultSet.getString("user_name"));
			userList.add(user);
			role.setUserList(userList);
		}	
		return role;
	}
}

package com.set.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.set.entities.Role;
import static com.set.dao.DAOUtil.*;

/**
 * @author Björn Dalberg
 * @created 4 maj 2016
 */
public class RoleDAOJDBC implements RoleDAO {

	private static final String SQL_CREATE_ROLE = "INSERT INTO role (name, description, enabled) VALUES (?, ?, ?)";
	private static final String SQL_FIND_BY_ID = "SELECT * FROM role WHERE role_id = ? ORDER BY role_id";
	private static final String SQL_FIND_BY_ROLE_NAME = "SELECT * FROM role WHERE name = ? ORDER BY role_id";
	private static final String SQL_LIST_ALL_ROLES = "SELECT * FROM role ORDER BY role_id";
	private static final String SQL_UPDATE_ROLE = "UPDATE SET name = ?, description = ?, enabled = ? WHERE role_id = ?";
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
		Role role = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = daoFactory.getConnection();
			statement = prepareStatement(connection, sql, false, values);
			resultSet = statement.executeQuery();
			if(resultSet.next()) {
				role = processResult(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
			} catch (SQLException | NullPointerException e1) {
				e1.printStackTrace();
			}
		}
		return role;
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
			} catch (SQLException | NullPointerException e1) {
				e1.printStackTrace();
			}
		}
		return roles;
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
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return role;
	}

	@Override
	public void updateRole(Role role) {
		if (role.getRoleId() == null)
			throw new IllegalArgumentException("Role does not exist");
		Object[] roleObj = { role.getName(), role.getDescription(), role.isEnabled() };
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
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void deleteRole(Role role) {
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
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * @param resultSet
	 * @return
	 */
	private Role processResult(ResultSet resultSet) throws SQLException {
		Role role = new Role();
		role.setRoleId(resultSet.getLong(1));
		role.setName(resultSet.getString(2));
		role.setDescription(resultSet.getString(3));
		role.setEnabled(resultSet.getBoolean(4));
		return role;
	}

	@Override
	public void enableRole(Role role) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableRole(Role role) {
		// TODO Auto-generated method stub

	}

}

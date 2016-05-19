package com.set.testers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.*;
import com.set.dao.DAOFactory;
import com.set.entities.User;

public class UserListTest {

	public static void main(String[] args) {
	
		
		/*List<User> users = DAOFactory.getInstance("setdb.jdbc").getUserDAO().listUsers();	
		
		for (User user : users) {
			System.out.println(user);
		}*/
	}
	
	public static List<User> mergeAll(List<User> input) {
	    return new ArrayList<>(input.stream().collect(Collectors.toMap(User::getUserId, e->e, User::merge)).values());
	}
}

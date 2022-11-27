package hu.webuni.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Users {

	public static List<User> users = new ArrayList<User>();
	
	static {
		users.add(new User("user", "password", Arrays.asList("user")));
		users.add(new User("admin", "password", Arrays.asList("user","admin")));
	}
	
}

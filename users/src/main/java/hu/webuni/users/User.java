package hu.webuni.users;

import java.util.ArrayList;
import java.util.List;

public class User {

	private String username;
	private String password;
	private List<String> authority = new ArrayList<String>();
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getAuthority() {
		return authority;
	}
	public void setAuthority(ArrayList<String> authority) {
		this.authority = authority;
	}
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", authority=" + authority + "]";
	}
	public User(String username, String password, List<String> authority) {
		super();
		this.username = username;
		this.password = password;
		this.authority = authority;
	}
	
	
	
}

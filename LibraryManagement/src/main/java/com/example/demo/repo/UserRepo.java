package com.example.demo.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.User;

@Repository
public class UserRepo {
  private List<User> users=new ArrayList<User>();
  
  public void createUsers() {
	  users=List.of(
			  new User(1, "Amlan", "a@gmail.com", null),
			  new User(2, "Amu", "amu@gmail.com", null)
			  );
  }
  
  public List<User> getAllUsers(){
	  return users;
  }
  
  public User findById(int id) {
	  for(int i=0;i<users.size();i++) {
		  if(users.get(i).getUserId()==(id)) {
			  return users.get(i);
		  }
	  }
	  return null;
  }
  

  public List<User> search(String name) {
      return users.stream().filter(x -> x.getUserName().startsWith(name)).collect(Collectors.toList());
  }

  public User save(User u) {
      User user = new User();
      user.setUserId(u.getUserId());
      user.setUserName(u.getUserName());
      user.setUserEmail(u.getUserEmail());
      user.setBooks(u.getBooks());
      users.add(user);
      return user;
  }

  public String delete(Integer id) {
      users.removeIf(x -> x.getUserId() == (id));
      return null;
  }

  public User update(User user) {
      int idx = 0;
      int id = 0;
      for (int i = 0; i < users.size(); i++) {
          if (users.get(i).getUserId() == (user.getUserId())) {
              id = user.getUserId();
              idx = i;
              break;
          }
      }

      User user1 = new User();
      user1.setUserId(id);
      user1.setUserName(user.getUserName());
      user1.setUserEmail(user.getUserEmail());
      user1.setBooks(user.getBooks());
      users.set(idx, user);
      return user1;
  }
  
}

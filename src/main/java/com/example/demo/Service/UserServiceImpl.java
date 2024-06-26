package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class UserServiceImpl implements UserService
{

	@Autowired
	private UserRepository userRepo;
	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

	@Override
	public User addUser(User user) {
		log.debug("user deratils {}",user);	
		if(user!=null)
		{
			return userRepo.saveAndFlush(user);
			
		}
		return null;
	}

	@Override
	public boolean loginUser(String username, String password,String userRole) {
		
		User user1 = userRepo.validateUser(username, password,userRole);
		log.debug("user {}",user1);
		if(user1!=null)
		{
			return true;
		}
		return false;
	}

	@Override
	public List<User> getAllUsers() {
	
		List<User> userList = userRepo.findAll();
		
		if(userList!=null & userList.size() >0)
		{
			return userList;
		}
		else
			return null;
	}
	
	@Override
	public User getUserById(int uid) {
		Optional<User> user = userRepo.findById(uid);
		if(user.isPresent())	
		{
			return user.get();
		}
		
		return null;
	}
	
	@Override
	public int forgotPassword(String username,String petname) {

		try {
		  User user2 = userRepo.RequestValue(username,petname);
		  return user2.getId();
		}
		catch(Exception e) {
			System.out.println(-1);
			return -1;
		}
}
	@Override
    public User updateUser(int uid, User user) {
        Optional<User> existingUser = userRepo.findById(uid);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setUserRole(user.getUserRole());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPetname(user.getPetname());
            return userRepo.save(updatedUser);
        }
        return null;
    }
	@Override
	public boolean deleteUser(int uid) {
	    Optional<User> existingUser = userRepo.findById(uid);
	    if (existingUser.isPresent()) {
	        userRepo.delete(existingUser.get());
	        return true;
	    }
	    return false;
	}
	
	
	
}
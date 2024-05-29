package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.User;
import com.example.demo.Service.UserService;


@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "*")
public class UserController
{
	@Autowired
	private UserService uService;
	
	@GetMapping("/getAllUsers")//done
	public ResponseEntity<?> getAllUsers()
	{
		List<User> userlist = uService.getAllUsers();
		
		if(userlist!=null)
		{
			return new ResponseEntity<List<User>>(userlist, HttpStatus.OK);
		}
		return new ResponseEntity<String>("userlist is empty", HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/findUser/{uid}")
	public ResponseEntity<?> findMovie(@PathVariable("uid") int uid) {
		return new ResponseEntity<User>(uService.getUserById(uid), HttpStatus.OK);
	}
	
	@PutMapping("/updateUser/{uid}")
    public ResponseEntity<?> updateUser(@PathVariable("uid") int uid, @RequestBody User user) {
        User updatedUser = uService.updateUser(uid, user);
        if (updatedUser != null) {
            return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
        }
    }
	@DeleteMapping("/deleteUser/{uid}")
	public ResponseEntity<?> deleteUser(@PathVariable("uid") int uid) {
	    boolean isRemoved = uService.deleteUser(uid);
	    if (isRemoved) {
	        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
	    }
	}
	
	
	

}

package com.example.demo.Controller;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("auth/v1")
@CrossOrigin(origins="*")
public class AuthenticationController 
{
	private Map<String, String> mapObj = new HashMap<String, String>();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userrepo;
//	 @Autowired
//	    private JwtClient jwtClient;
	 
	
	
	@PostMapping("/addUser")
	public ResponseEntity<?> registerUser(@RequestBody User user)
	{
		if(userService.addUser(user)!=null)
	{
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
		return new ResponseEntity<String>("user registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	//method to generate token inside login API
	public String generateToken(String username, String password,String userRole) throws ServletException, Exception
	{
		String jwtToken;
		if(username ==null || password ==null)
		{
			throw new ServletException("Please enter valid credentials");
		}
		
		boolean flag = userService.loginUser(username, password,userRole);
		
		if(!flag)
		{
			throw new ServletException("Invalid credentials");
		}
		
		else
		{
			jwtToken = Jwts.builder().setSubject(username).setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis()+3000000))
						.signWith(SignatureAlgorithm.HS256, "secret key").compact();
						
		}
		
		return jwtToken;
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> logiUser(@RequestBody User user)
	{
	
		try
		{
			log.debug("user object is {}",user);
			String jwtToken =generateToken(user.getUsername(), user.getPassword(),user.getUserRole());
			mapObj.put("Message", "User successfully logged in");
			mapObj.put("Token", jwtToken);
			mapObj.put("userRole", user.getUserRole());
		}
		catch(Exception e)
		{
			mapObj.put("Message", "User not logged in");
			mapObj.put("Token", null);
			return new ResponseEntity<>(mapObj, HttpStatus.UNAUTHORIZED);
		}
		
		return new ResponseEntity<>(mapObj, HttpStatus.OK);
	}
	@PutMapping("/forgotpassword")
	public ResponseEntity<?> forgotPassword(@RequestBody User user) {
		String username=user.getUsername();
		String petname=user.getPetname();
		String password=user.getPassword();
		int response = userService.forgotPassword(username,petname);
		log.debug("response object is,{}",response);
		try {
		User user2=userService.getUserById(response);
		user2.setPassword(password);
		userrepo.save(user2);
		return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


		
}
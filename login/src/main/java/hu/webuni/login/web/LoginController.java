package hu.webuni.login.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.security.config.JwtService;
import hu.webuni.security.config.LoginDto;

@RestController
@RequestMapping("/api")
public class LoginController {

	@Autowired
	JwtService jwtService;

	@Autowired
	AuthenticationManager authenticationManager;

	@PostMapping
	public String login(@RequestBody LoginDto login) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
		return jwtService.createToken((UserDetails) authentication.getPrincipal());
	}

	@GetMapping("/validateToken")
	//csak teszthez, de lehetne ellenőrizni a token lejárati idejét, tartalmát, stb.
	public String validateToken() {
		return "hello";
	}
	
}

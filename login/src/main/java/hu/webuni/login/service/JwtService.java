package hu.webuni.login.service;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtService {
	
	private static String ISSUER = "MicroService";
	private static Algorithm ALG = Algorithm.HMAC256("microservice secret");
	private static final String AUTH = "auth";
	private static final int EXPIRE_MINUTES = 10;
	
	public String createToken(UserDetails userDetails) {
		String token = JWT.create()
			.withSubject(userDetails.getUsername())
			.withArrayClaim(AUTH, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
			.withExpiresAt(new Date(System.currentTimeMillis()+EXPIRE_MINUTES*60000))
			.withIssuer(ISSUER)
			.sign(ALG);			
		return token;
	}

	public UserDetails parseJwt(String token) {
		DecodedJWT decoded = JWT.require(ALG)
				.withIssuer(ISSUER)
				.build()
				.verify(token);
		User user = new User(decoded.getSubject(), "...", 
				decoded.getClaim(AUTH).asList(String.class).stream().
					map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		return user;
	}
	
}

package hu.webuni.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter {

	private final String AUTH = "Authorization";
	private final String BEARER = "Bearer ";
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		System.out.println("filter");
		ServerHttpRequest request = exchange.getRequest();
		String authHeader = null;
		try {
			authHeader=request.getHeaders().getOrEmpty(AUTH).get(0);
		}catch (Exception e) {
		}
		if (authHeader == null || !authHeader.startsWith(BEARER)) {
			return this.onError(exchange, "Authorization exception", HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Auth:"+authHeader);
		
		
		
		/*
		if (authHeader != null && authHeader.startsWith(BEARER)) {
			String token = authHeader.substring(BEARER.length());
			
			UserDetails principal = jwtService.parseJwt(token);
			
			Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		*/
		return chain.filter(exchange);
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
	
}

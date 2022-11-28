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
		ServerHttpRequest request = exchange.getRequest();
		if (!this.needAuthentication(request)) {
			return chain.filter(exchange);
		}
		String authHeader = null;
		try {
			authHeader=request.getHeaders().getOrEmpty(AUTH).get(0);
			if (authHeader == null || !authHeader.startsWith(BEARER)) {
				return this.onError(exchange, "Authorization exception", HttpStatus.UNAUTHORIZED);
			}
			return chain.filter(exchange);
//			String token = authHeader.substring(BEARER.length());
//			Set<URI> uris = exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet());
//	        String originalUri = (uris.isEmpty()) ? "Unknown" : uris.iterator().next().toString();
//			
//			WebClient webClient = WebClient.create();
//			return webClient.get()
//					.uri("http://localhost:8080/login/validateToken?token="+token).retrieve()
//					.bodyToMono(String.class).flatMap( response -> {
//						System.out.println("response: "+response);
//						return chain.filter(exchange);
//					});
		}catch (Exception e) {}
		return this.onError(exchange, "Authorization exception", HttpStatus.UNAUTHORIZED);
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean needAuthentication(ServerHttpRequest request) {
    	//System.out.println(request.getPath().toString());
        return !(request.getPath().toString().equals("/api/validateToken") || 
        		request.getPath().toString().equals("/api"));
    }
	
}

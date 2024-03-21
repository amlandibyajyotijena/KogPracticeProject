package com.example.demo.util;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;


//2
@Slf4j
public class JwtUtils {
	private static String jwtSecret = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
	private final static long jwtExpiration = 77777L;

	private JwtUtils() {
	}

	public static Optional<String> getUsernameFromToken(String token) {
		// Get claims
		Optional<Claims> claims = parseToken(token);

		// Extract subject
		if (claims.isPresent()) {
			return Optional.of(claims.get().getSubject());
		}

		return Optional.empty();
	}

	public static String generateToken(String username) {
		// Create signing key
		Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

		// Generate token
		var currentDate = new Date();

		var expiration = new Date(currentDate.getTime() + jwtExpiration);

		return Jwts.builder().setId(UUID.randomUUID().toString()).setIssuer("Amlan").setSubject(username)
				.setIssuedAt(currentDate).setExpiration(expiration).signWith(key).compact();
	}

	public static boolean validateToken(String jwtToken) {
		return parseToken(jwtToken).isPresent();
	}

	private static Optional<Claims> parseToken(String token) {
		// Create JwtParser
		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes()).build();

		try {
			return Optional.of(jwtParser.parseClaimsJws(token).getBody());
		} catch (ExpiredJwtException e) {
			log.error("token expired");
		} catch (UnsupportedJwtException e) {
			System.out.println(e.getMessage());
		} catch (MalformedJwtException e) {
			System.out.println(e.getMessage());
		} catch (SignatureException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

		return Optional.empty();
	}

}

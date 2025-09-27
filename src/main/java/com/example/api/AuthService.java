package com.example.api;

import com.example.config.ConfigManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

/**
 * Authentication Service for OAuth token management
 * Handles token retrieval and validation using RestAssured
 */
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private final String authUrl;
    private final String clientSecret;
    
    public AuthService() {
        ConfigManager.TestConfig config = ConfigManager.getConfig();
        this.authUrl = config.authUrl();
        this.clientSecret = config.clientSecret();
        
        // Set base URI for RestAssured
        RestAssured.baseURI = authUrl;
    }
    
    /**
     * Get OAuth token using password grant
     */
    public TokenResponse getToken(String username, String password) {
        try {
            LOGGER.info("Requesting OAuth token for user: {}", username);
            
            Response response = given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("scope", "openid")
                .formParam("grant_type", "password")
                .formParam("username", username)
                .formParam("password", password)
                .formParam("client_id", clientSecret)
                .when()
                .post("/oauth/token")
                .then()
                .extract()
                .response();
            
            if (response.getStatusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody().asString());
                
                String idToken = jsonResponse.get("id_token").asText();
                String accessToken = jsonResponse.has("access_token") ? 
                    jsonResponse.get("access_token").asText() : null;
                String refreshToken = jsonResponse.has("refresh_token") ? 
                    jsonResponse.get("refresh_token").asText() : null;
                int expiresIn = jsonResponse.has("expires_in") ? 
                    jsonResponse.get("expires_in").asInt() : 3600;
                
                LOGGER.info("Successfully obtained OAuth token for user: {}", username);
                return new TokenResponse(idToken, accessToken, refreshToken, expiresIn, response.getStatusCode());
                
            } else {
                LOGGER.error("Failed to obtain token. Status: {}, Response: {}", 
                    response.getStatusCode(), response.getBody().asString());
                return new TokenResponse(null, null, null, 0, response.getStatusCode());
            }
            
        } catch (Exception e) {
            LOGGER.error("Error during token request for user: {}", username, e);
            throw new RuntimeException("Token request failed", e);
        }
    }
    
    /**
     * Get token using configuration values
     */
    public TokenResponse getStaffToken() {
        ConfigManager.TestConfig config = ConfigManager.getConfig();
        return getToken(config.staffUsername(), config.staffPassword());
    }
    
    /**
     * Get token for test user
     */
    public TokenResponse getTestUserToken() {
        ConfigManager.TestConfig config = ConfigManager.getConfig();
        return getToken(config.testUsername(), config.testPassword());
    }
    
    /**
     * Validate token by making a test API call
     */
    public boolean validateToken(String token) {
        try {
            Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/oauth/userinfo")
                .then()
                .extract()
                .response();
            
            boolean isValid = response.getStatusCode() == 200;
            LOGGER.info("Token validation result: {}", isValid);
            return isValid;
            
        } catch (Exception e) {
            LOGGER.error("Error during token validation", e);
            return false;
        }
    }
    
    /**
     * Refresh token if refresh token is available
     */
    public TokenResponse refreshToken(String refreshToken) {
        try {
            LOGGER.info("Refreshing OAuth token");
            
            Response response = given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("grant_type", "refresh_token")
                .formParam("refresh_token", refreshToken)
                .formParam("client_id", clientSecret)
                .when()
                .post("/oauth/token")
                .then()
                .extract()
                .response();
            
            if (response.getStatusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody().asString());
                
                String newIdToken = jsonResponse.get("id_token").asText();
                String newAccessToken = jsonResponse.has("access_token") ? 
                    jsonResponse.get("access_token").asText() : null;
                String newRefreshToken = jsonResponse.has("refresh_token") ? 
                    jsonResponse.get("refresh_token").asText() : refreshToken;
                int expiresIn = jsonResponse.has("expires_in") ? 
                    jsonResponse.get("expires_in").asInt() : 3600;
                
                LOGGER.info("Successfully refreshed OAuth token");
                return new TokenResponse(newIdToken, newAccessToken, newRefreshToken, expiresIn, response.getStatusCode());
                
            } else {
                LOGGER.error("Failed to refresh token. Status: {}, Response: {}", 
                    response.getStatusCode(), response.getBody().asString());
                return new TokenResponse(null, null, null, 0, response.getStatusCode());
            }
            
        } catch (Exception e) {
            LOGGER.error("Error during token refresh", e);
            throw new RuntimeException("Token refresh failed", e);
        }
    }
    
    /**
     * Token Response data class
     */
    public static class TokenResponse {
        private final String idToken;
        private final String accessToken;
        private final String refreshToken;
        private final int expiresIn;
        private final int statusCode;
        
        public TokenResponse(String idToken, String accessToken, String refreshToken, int expiresIn, int statusCode) {
            this.idToken = idToken;
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.expiresIn = expiresIn;
            this.statusCode = statusCode;
        }
        
        public String getIdToken() { return idToken; }
        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public int getExpiresIn() { return expiresIn; }
        public int getStatusCode() { return statusCode; }
        
        public boolean isSuccess() { return statusCode == 200 && idToken != null; }
        
        public String getBearerToken() {
            return accessToken != null ? "Bearer " + accessToken : "Bearer " + idToken;
        }
        
        @Override
        public String toString() {
            return String.format("TokenResponse{statusCode=%d, hasIdToken=%s, hasAccessToken=%s, expiresIn=%d}", 
                statusCode, idToken != null, accessToken != null, expiresIn);
        }
    }
}
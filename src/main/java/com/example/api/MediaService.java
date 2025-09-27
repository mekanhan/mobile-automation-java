package com.example.api;

import com.example.config.ConfigManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * Media Service for content and organization API operations
 * Handles live feeds, events, news, athletics, staff directory, dining
 * Equivalent to mediaService.js from the original framework
 */
public class MediaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private final String mediaApiUrl;
    private final AuthService authService;
    
    public MediaService() {
        ConfigManager.TestConfig config = ConfigManager.getConfig();
        this.mediaApiUrl = config.mediaApiUrl();
        this.authService = new AuthService();
        
        RestAssured.baseURI = mediaApiUrl;
    }
    
    /**
     * Get all organizations for a school
     */
    public List<Organization> getAllOrganizations(String schoolId) {
        try {
            LOGGER.info("Fetching all organizations for school: {}", schoolId);
            
            Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/v1/p/" + schoolId + "/secondary_organizations/")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            List<Organization> organizations = new ArrayList<>();
            JsonNode responseJson = objectMapper.readTree(response.getBody().asString());
            
            if (responseJson.isArray()) {
                for (JsonNode orgNode : responseJson) {
                    organizations.add(new Organization(
                        orgNode.get("id").asText(),
                        orgNode.get("name").asText(),
                        orgNode.has("description") ? orgNode.get("description").asText() : ""
                    ));
                }
            }
            
            LOGGER.info("Retrieved {} organizations", organizations.size());
            return organizations;
            
        } catch (Exception e) {
            LOGGER.error("Failed to fetch organizations for school: {}", schoolId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Get specific organization by name
     */
    public Organization getOrganization(String schoolId, String organizationName) {
        List<Organization> organizations = getAllOrganizations(schoolId);
        return organizations.stream()
            .filter(org -> org.getName().equalsIgnoreCase(organizationName))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Get live feed content for an organization
     */
    public LiveFeedResponse getLiveFeed(String organizationId) {
        try {
            LOGGER.info("Fetching live feed for organization: {}", organizationId);
            
            Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/v6/secondary_organizations/" + organizationId + "/live_feeds/")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            JsonNode responseJson = objectMapper.readTree(response.getBody().asString());
            
            List<FeedItem> feedItems = new ArrayList<>();
            if (responseJson.has("results") && responseJson.get("results").isArray()) {
                for (JsonNode itemNode : responseJson.get("results")) {
                    feedItems.add(new FeedItem(
                        itemNode.get("id").asText(),
                        itemNode.has("title") ? itemNode.get("title").asText() : "",
                        itemNode.has("content") ? itemNode.get("content").asText() : "",
                        itemNode.has("created_at") ? itemNode.get("created_at").asText() : ""
                    ));
                }
            }
            
            LOGGER.info("Retrieved {} live feed items", feedItems.size());
            return new LiveFeedResponse(feedItems, true);
            
        } catch (Exception e) {
            LOGGER.error("Failed to fetch live feed for organization: {}", organizationId, e);
            return new LiveFeedResponse(new ArrayList<>(), false);
        }
    }
    
    /**
     * Get events for an organization
     */
    public EventsResponse getEvents(String eventsId) {
        try {
            LOGGER.info("Fetching events for events ID: {}", eventsId);
            
            Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/v2/s/" + eventsId + "/events")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            JsonNode responseJson = objectMapper.readTree(response.getBody().asString());
            
            List<Event> events = new ArrayList<>();
            if (responseJson.has("results") && responseJson.get("results").isArray()) {
                for (JsonNode eventNode : responseJson.get("results")) {
                    events.add(new Event(
                        eventNode.get("id").asText(),
                        eventNode.has("title") ? eventNode.get("title").asText() : "",
                        eventNode.has("description") ? eventNode.get("description").asText() : "",
                        eventNode.has("start_date") ? eventNode.get("start_date").asText() : "",
                        eventNode.has("end_date") ? eventNode.get("end_date").asText() : ""
                    ));
                }
            }
            
            LOGGER.info("Retrieved {} events", events.size());
            return new EventsResponse(events, true);
            
        } catch (Exception e) {
            LOGGER.error("Failed to fetch events for ID: {}", eventsId, e);
            return new EventsResponse(new ArrayList<>(), false);
        }
    }
    
    /**
     * Get news articles
     */
    public NewsResponse getNews(String newsId) {
        try {
            LOGGER.info("Fetching news for news ID: {}", newsId);
            
            Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/v5/custom_sections/" + newsId + "/articles")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            JsonNode responseJson = objectMapper.readTree(response.getBody().asString());
            
            List<NewsArticle> articles = new ArrayList<>();
            if (responseJson.has("results") && responseJson.get("results").isArray()) {
                for (JsonNode articleNode : responseJson.get("results")) {
                    articles.add(new NewsArticle(
                        articleNode.get("id").asText(),
                        articleNode.has("title") ? articleNode.get("title").asText() : "",
                        articleNode.has("content") ? articleNode.get("content").asText() : "",
                        articleNode.has("published_date") ? articleNode.get("published_date").asText() : ""
                    ));
                }
            }
            
            LOGGER.info("Retrieved {} news articles", articles.size());
            return new NewsResponse(articles, true);
            
        } catch (Exception e) {
            LOGGER.error("Failed to fetch news for ID: {}", newsId, e);
            return new NewsResponse(new ArrayList<>(), false);
        }
    }
    
    /**
     * Get athletics/scores data
     */
    public AthleticsResponse getAthletics(String organizationId) {
        try {
            LOGGER.info("Fetching athletics data for organization: {}", organizationId);
            
            Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/v6/secondary_organizations/" + organizationId + "/scores_schedules")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            JsonNode responseJson = objectMapper.readTree(response.getBody().asString());
            
            List<AthleticEvent> events = new ArrayList<>();
            if (responseJson.has("results") && responseJson.get("results").isArray()) {
                for (JsonNode eventNode : responseJson.get("results")) {
                    events.add(new AthleticEvent(
                        eventNode.get("id").asText(),
                        eventNode.has("sport") ? eventNode.get("sport").asText() : "",
                        eventNode.has("opponent") ? eventNode.get("opponent").asText() : "",
                        eventNode.has("date") ? eventNode.get("date").asText() : "",
                        eventNode.has("score") ? eventNode.get("score").asText() : ""
                    ));
                }
            }
            
            LOGGER.info("Retrieved {} athletic events", events.size());
            return new AthleticsResponse(events, true);
            
        } catch (Exception e) {
            LOGGER.error("Failed to fetch athletics for organization: {}", organizationId, e);
            return new AthleticsResponse(new ArrayList<>(), false);
        }
    }
    
    /**
     * Get staff directory
     */
    public StaffResponse getStaff(String organizationId) {
        try {
            LOGGER.info("Fetching staff directory for organization: {}", organizationId);
            
            Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/v6/secondary_organizations/" + organizationId + "/directories")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            JsonNode responseJson = objectMapper.readTree(response.getBody().asString());
            
            List<StaffMember> staff = new ArrayList<>();
            if (responseJson.has("results") && responseJson.get("results").isArray()) {
                for (JsonNode staffNode : responseJson.get("results")) {
                    staff.add(new StaffMember(
                        staffNode.get("id").asText(),
                        staffNode.has("name") ? staffNode.get("name").asText() : "",
                        staffNode.has("title") ? staffNode.get("title").asText() : "",
                        staffNode.has("email") ? staffNode.get("email").asText() : "",
                        staffNode.has("phone") ? staffNode.get("phone").asText() : ""
                    ));
                }
            }
            
            LOGGER.info("Retrieved {} staff members", staff.size());
            return new StaffResponse(staff, true);
            
        } catch (Exception e) {
            LOGGER.error("Failed to fetch staff for organization: {}", organizationId, e);
            return new StaffResponse(new ArrayList<>(), false);
        }
    }
    
    /**
     * Get dining information
     */
    public DiningResponse getDining(String organizationId) {
        try {
            LOGGER.info("Fetching dining info for organization: {}", organizationId);
            
            Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/v6/secondary_organizations/" + organizationId + "/dinings")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            JsonNode responseJson = objectMapper.readTree(response.getBody().asString());
            
            List<DiningOption> diningOptions = new ArrayList<>();
            if (responseJson.has("results") && responseJson.get("results").isArray()) {
                for (JsonNode diningNode : responseJson.get("results")) {
                    diningOptions.add(new DiningOption(
                        diningNode.get("id").asText(),
                        diningNode.has("name") ? diningNode.get("name").asText() : "",
                        diningNode.has("description") ? diningNode.get("description").asText() : "",
                        diningNode.has("hours") ? diningNode.get("hours").asText() : ""
                    ));
                }
            }
            
            LOGGER.info("Retrieved {} dining options", diningOptions.size());
            return new DiningResponse(diningOptions, true);
            
        } catch (Exception e) {
            LOGGER.error("Failed to fetch dining for organization: {}", organizationId, e);
            return new DiningResponse(new ArrayList<>(), false);
        }
    }
    
    /**
     * Get current user information
     */
    public UserResponse getCurrentUser() {
        try {
            AuthService.TokenResponse tokenResponse = authService.getTestUserToken();
            String authToken = tokenResponse.getBearerToken();
            
            Response response = given()
                .header("Authorization", authToken)
                .when()
                .get("/users/me")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            JsonNode responseJson = objectMapper.readTree(response.getBody().asString());
            
            User user = new User(
                responseJson.get("id").asText(),
                responseJson.has("username") ? responseJson.get("username").asText() : "",
                responseJson.has("email") ? responseJson.get("email").asText() : "",
                responseJson.has("first_name") ? responseJson.get("first_name").asText() : "",
                responseJson.has("last_name") ? responseJson.get("last_name").asText() : ""
            );
            
            LOGGER.info("Retrieved current user: {}", user.getUsername());
            return new UserResponse(user, true);
            
        } catch (Exception e) {
            LOGGER.error("Failed to fetch current user", e);
            return new UserResponse(null, false);
        }
    }
    
    // Data classes for responses
    
    public static class Organization {
        private final String id;
        private final String name;
        private final String description;
        
        public Organization(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    public static class FeedItem {
        private final String id;
        private final String title;
        private final String content;
        private final String createdAt;
        
        public FeedItem(String id, String title, String content, String createdAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.createdAt = createdAt;
        }
        
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public String getCreatedAt() { return createdAt; }
    }
    
    public static class Event {
        private final String id;
        private final String title;
        private final String description;
        private final String startDate;
        private final String endDate;
        
        public Event(String id, String title, String description, String startDate, String endDate) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
        }
        
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
    }
    
    public static class NewsArticle {
        private final String id;
        private final String title;
        private final String content;
        private final String publishedDate;
        
        public NewsArticle(String id, String title, String content, String publishedDate) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.publishedDate = publishedDate;
        }
        
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public String getPublishedDate() { return publishedDate; }
    }
    
    public static class AthleticEvent {
        private final String id;
        private final String sport;
        private final String opponent;
        private final String date;
        private final String score;
        
        public AthleticEvent(String id, String sport, String opponent, String date, String score) {
            this.id = id;
            this.sport = sport;
            this.opponent = opponent;
            this.date = date;
            this.score = score;
        }
        
        public String getId() { return id; }
        public String getSport() { return sport; }
        public String getOpponent() { return opponent; }
        public String getDate() { return date; }
        public String getScore() { return score; }
    }
    
    public static class StaffMember {
        private final String id;
        private final String name;
        private final String title;
        private final String email;
        private final String phone;
        
        public StaffMember(String id, String name, String title, String email, String phone) {
            this.id = id;
            this.name = name;
            this.title = title;
            this.email = email;
            this.phone = phone;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getTitle() { return title; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
    }
    
    public static class DiningOption {
        private final String id;
        private final String name;
        private final String description;
        private final String hours;
        
        public DiningOption(String id, String name, String description, String hours) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.hours = hours;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getHours() { return hours; }
    }
    
    public static class User {
        private final String id;
        private final String username;
        private final String email;
        private final String firstName;
        private final String lastName;
        
        public User(String id, String username, String email, String firstName, String lastName) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }
        
        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
    }
    
    // Response wrapper classes
    public static class LiveFeedResponse {
        private final List<FeedItem> feedItems;
        private final boolean success;
        
        public LiveFeedResponse(List<FeedItem> feedItems, boolean success) {
            this.feedItems = feedItems;
            this.success = success;
        }
        
        public List<FeedItem> getFeedItems() { return feedItems; }
        public boolean isSuccess() { return success; }
    }
    
    public static class EventsResponse {
        private final List<Event> events;
        private final boolean success;
        
        public EventsResponse(List<Event> events, boolean success) {
            this.events = events;
            this.success = success;
        }
        
        public List<Event> getEvents() { return events; }
        public boolean isSuccess() { return success; }
    }
    
    public static class NewsResponse {
        private final List<NewsArticle> articles;
        private final boolean success;
        
        public NewsResponse(List<NewsArticle> articles, boolean success) {
            this.articles = articles;
            this.success = success;
        }
        
        public List<NewsArticle> getArticles() { return articles; }
        public boolean isSuccess() { return success; }
    }
    
    public static class AthleticsResponse {
        private final List<AthleticEvent> events;
        private final boolean success;
        
        public AthleticsResponse(List<AthleticEvent> events, boolean success) {
            this.events = events;
            this.success = success;
        }
        
        public List<AthleticEvent> getEvents() { return events; }
        public boolean isSuccess() { return success; }
    }
    
    public static class StaffResponse {
        private final List<StaffMember> staff;
        private final boolean success;
        
        public StaffResponse(List<StaffMember> staff, boolean success) {
            this.staff = staff;
            this.success = success;
        }
        
        public List<StaffMember> getStaff() { return staff; }
        public boolean isSuccess() { return success; }
    }
    
    public static class DiningResponse {
        private final List<DiningOption> diningOptions;
        private final boolean success;
        
        public DiningResponse(List<DiningOption> diningOptions, boolean success) {
            this.diningOptions = diningOptions;
            this.success = success;
        }
        
        public List<DiningOption> getDiningOptions() { return diningOptions; }
        public boolean isSuccess() { return success; }
    }
    
    public static class UserResponse {
        private final User user;
        private final boolean success;
        
        public UserResponse(User user, boolean success) {
            this.user = user;
            this.success = success;
        }
        
        public User getUser() { return user; }
        public boolean isSuccess() { return success; }
    }
}
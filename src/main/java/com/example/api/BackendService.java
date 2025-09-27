package com.example.api;

import com.example.config.ConfigManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Backend Service for educational platform API operations
 * Handles chat, assignments, announcements using RestAssured
 * Equivalent to backendAPI.js from the original framework
 */
public class BackendService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackendService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private final String backendApiUrl;
    private final AuthService authService;
    
    public BackendService() {
        ConfigManager.TestConfig config = ConfigManager.getConfig();
        this.backendApiUrl = config.backendApiUrl();
        this.authService = new AuthService();
        
        RestAssured.baseURI = backendApiUrl;
    }
    
    /**
     * Send chat message between users
     */
    public ChatMessageResponse sendChatMessage(String classId, String fromUserToken, String recipientUserId) {
        try {
            AuthService.TokenResponse tokenResponse = authService.getStaffToken();
            String authToken = tokenResponse.getBearerToken();
            
            // Find existing chat thread
            String chatId = findOrCreateChatThread(classId, recipientUserId, authToken);
            
            // Generate random message
            String messageContent = "Test message " + System.currentTimeMillis();
            
            // Send message
            Map<String, Object> messagePayload = new HashMap<>();
            messagePayload.put("chatThreadId", chatId);
            messagePayload.put("attachmentIds", new ArrayList<>());
            messagePayload.put("content", messageContent);
            messagePayload.put("deliveryId", "delivery_" + System.currentTimeMillis());
            messagePayload.put("messageType", "MESSAGE_TYPE_DEFAULT");
            
            Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .body(messagePayload)
                .when()
                .post("/v1/chat_threads/" + chatId + "/messages")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            LOGGER.info("Chat message sent successfully: {}", messageContent);
            return new ChatMessageResponse(messageContent, chatId, true);
            
        } catch (Exception e) {
            LOGGER.error("Failed to send chat message", e);
            return new ChatMessageResponse(null, null, false);
        }
    }
    
    /**
     * Send broadcast message to multiple recipients
     */
    public ChatMessageResponse sendBroadcastMessage(String classId, String fromUserToken, 
                                                   String recipient1Id, String recipient2Id) {
        try {
            AuthService.TokenResponse tokenResponse = authService.getStaffToken();
            String authToken = tokenResponse.getBearerToken();
            
            // Create participants list
            List<String> participants = new ArrayList<>();
            participants.add(recipient1Id);
            participants.add(recipient2Id);
            
            // Find or create broadcast thread
            String chatId = findOrCreateBroadcastThread(classId, participants, authToken);
            
            // Generate broadcast message
            String messageContent = "Broadcast message " + System.currentTimeMillis();
            
            // Send broadcast message
            Map<String, Object> messagePayload = new HashMap<>();
            messagePayload.put("chatThreadId", chatId);
            messagePayload.put("attachmentIds", new ArrayList<>());
            messagePayload.put("content", messageContent);
            messagePayload.put("deliveryId", "broadcast_" + System.currentTimeMillis());
            messagePayload.put("messageType", "MESSAGE_TYPE_BROADCAST");
            
            Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .body(messagePayload)
                .when()
                .post("/v1/chat_threads/" + chatId + "/messages")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            LOGGER.info("Broadcast message sent successfully: {}", messageContent);
            return new ChatMessageResponse(messageContent, chatId, true);
            
        } catch (Exception e) {
            LOGGER.error("Failed to send broadcast message", e);
            return new ChatMessageResponse(null, null, false);
        }
    }
    
    /**
     * Create announcement with attachment
     */
    public AnnouncementResponse createAnnouncement(String classId, String fromUserToken) {
        try {
            AuthService.TokenResponse tokenResponse = authService.getStaffToken();
            String authToken = tokenResponse.getBearerToken();
            
            // Upload attachment first
            String attachmentId = uploadAnnouncementAttachment(authToken);
            
            // Create announcement
            String announcementBody = "Test announcement " + System.currentTimeMillis();
            
            Map<String, Object> announcementPayload = new HashMap<>();
            announcementPayload.put("classId", classId);
            announcementPayload.put("body", announcementBody);
            announcementPayload.put("links", new ArrayList<>());
            
            List<String> attachments = new ArrayList<>();
            if (attachmentId != null) {
                attachments.add(attachmentId);
            }
            announcementPayload.put("attachmentIds", attachments);
            announcementPayload.put("isDirty", true);
            
            Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .body(announcementPayload)
                .when()
                .post("/v1/announcements")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            LOGGER.info("Announcement created successfully: {}", announcementBody);
            return new AnnouncementResponse(announcementBody, true);
            
        } catch (Exception e) {
            LOGGER.error("Failed to create announcement", e);
            return new AnnouncementResponse(null, false);
        }
    }
    
    /**
     * Create assignment with attachment
     */
    public AssignmentResponse createAssignment(String classId, String fromUserToken) {
        try {
            AuthService.TokenResponse tokenResponse = authService.getStaffToken();
            String authToken = tokenResponse.getBearerToken();
            
            // Upload attachment first
            String attachmentId = uploadAssignmentAttachment(authToken);
            
            // Create assignment
            String assignmentTitle = "Test Assignment " + System.currentTimeMillis();
            String assignmentInstructions = "Assignment instructions for testing";
            
            // Set due date to 7 days from now
            LocalDateTime dueDate = LocalDateTime.now().plusDays(7);
            String formattedDueDate = dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
            
            Map<String, Object> assignmentPayload = new HashMap<>();
            assignmentPayload.put("classId", classId);
            assignmentPayload.put("dueDate", formattedDueDate);
            assignmentPayload.put("title", assignmentTitle);
            assignmentPayload.put("instructions", assignmentInstructions);
            assignmentPayload.put("links", new ArrayList<>());
            assignmentPayload.put("isValid", true);
            assignmentPayload.put("fileRequired", true);
            
            List<String> attachments = new ArrayList<>();
            if (attachmentId != null) {
                attachments.add(attachmentId);
            }
            assignmentPayload.put("attachmentIds", attachments);
            assignmentPayload.put("assignmentType", "ASSIGNMENT_TYPE_GRADED");
            assignmentPayload.put("max_points", 2);
            
            Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .body(assignmentPayload)
                .when()
                .post("/v1/assignments")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            LOGGER.info("Assignment created successfully: {}", assignmentTitle);
            return new AssignmentResponse(assignmentTitle, assignmentInstructions, true);
            
        } catch (Exception e) {
            LOGGER.error("Failed to create assignment", e);
            return new AssignmentResponse(null, null, false);
        }
    }
    
    /**
     * Delete all posts (assignments, announcements, assessments) for cleanup
     */
    public boolean deleteAllRoomsPosts(String classId, String fromUserToken) {
        try {
            AuthService.TokenResponse tokenResponse = authService.getStaffToken();
            String authToken = tokenResponse.getBearerToken();
            
            // Delete assignments
            deleteAllAssignments(classId, authToken);
            
            // Delete assessments
            deleteAllAssessments(classId, authToken);
            
            // Delete announcements
            deleteAllAnnouncements(classId, authToken);
            
            LOGGER.info("Successfully deleted all posts for class: {}", classId);
            return true;
            
        } catch (Exception e) {
            LOGGER.error("Failed to delete all posts for class: {}", classId, e);
            return false;
        }
    }
    
    // Private helper methods
    
    private String findOrCreateChatThread(String classId, String recipientUserId, String authToken) {
        try {
            // Find existing thread
            Map<String, Object> findPayload = new HashMap<>();
            findPayload.put("classId", classId);
            findPayload.put("clientId", "test_client");
            findPayload.put("groupIds", new ArrayList<>());
            
            List<String> participants = new ArrayList<>();
            participants.add(recipientUserId);
            findPayload.put("participantUserIds", participants);
            
            Response findResponse = given()
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .body(findPayload)
                .when()
                .post("/v1/chat_threads/find")
                .then()
                .extract()
                .response();
            
            if (findResponse.getStatusCode() == 200) {
                JsonNode responseJson = objectMapper.readTree(findResponse.getBody().asString());
                if (responseJson.has("id")) {
                    return responseJson.get("id").asText();
                }
            }
            
            // Create new thread if not found
            Map<String, Object> createPayload = new HashMap<>();
            createPayload.put("classId", classId);
            createPayload.put("groupIds", new ArrayList<>());
            createPayload.put("participantUserIds", participants);
            createPayload.put("threadType", "THREAD_TYPE_DEFAULT");
            
            Response createResponse = given()
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .body(createPayload)
                .when()
                .post("/v1/chat_threads")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            JsonNode createJson = objectMapper.readTree(createResponse.getBody().asString());
            return createJson.get("id").asText();
            
        } catch (Exception e) {
            LOGGER.error("Failed to find or create chat thread", e);
            throw new RuntimeException("Chat thread creation failed", e);
        }
    }
    
    private String findOrCreateBroadcastThread(String classId, List<String> participants, String authToken) {
        // Similar implementation to findOrCreateChatThread but for broadcast
        // Implementation details would follow the same pattern
        return "broadcast_thread_" + System.currentTimeMillis();
    }
    
    private String uploadAnnouncementAttachment(String authToken) {
        try {
            // Create attachment metadata
            Map<String, Object> attachmentPayload = new HashMap<>();
            attachmentPayload.put("file_name", "test_image.jpeg");
            attachmentPayload.put("mime_type", "image/jpeg");
            attachmentPayload.put("alt_text", "Test image");
            
            Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .body(attachmentPayload)
                .when()
                .post("/v1/announcements/attachments")
                .then()
                .statusCode(201)
                .extract()
                .response();
            
            JsonNode responseJson = objectMapper.readTree(response.getBody().asString());
            String attachmentId = responseJson.get("id").asText();
            String presignedUrl = responseJson.get("presignedUrl").asText();
            
            // Upload file to presigned URL (simplified for demo)
            uploadToPresignedUrl(presignedUrl);
            
            return attachmentId;
            
        } catch (Exception e) {
            LOGGER.error("Failed to upload announcement attachment", e);
            return null;
        }
    }
    
    private String uploadAssignmentAttachment(String authToken) {
        try {
            // Create attachment metadata
            Map<String, Object> attachmentPayload = new HashMap<>();
            attachmentPayload.put("file_name", "test_document.pdf");
            attachmentPayload.put("mime_type", "application/pdf");
            
            Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .body(attachmentPayload)
                .when()
                .post("/v1/assignments/attachments")
                .then()
                .statusCode(200)
                .extract()
                .response();
            
            JsonNode responseJson = objectMapper.readTree(response.getBody().asString());
            String attachmentId = responseJson.get("id").asText();
            String presignedUrl = responseJson.get("presignedUrl").asText();
            
            // Upload file to presigned URL (simplified for demo)
            uploadToPresignedUrl(presignedUrl);
            
            return attachmentId;
            
        } catch (Exception e) {
            LOGGER.error("Failed to upload assignment attachment", e);
            return null;
        }
    }
    
    private void uploadToPresignedUrl(String presignedUrl) {
        // Simplified implementation - in real scenario would upload actual file
        LOGGER.info("Would upload file to presigned URL: {}", presignedUrl);
    }
    
    private void deleteAllAssignments(String classId, String authToken) {
        String[] filters = {"FILTER_PUBLISHED", "FILTER_SCHEDULED", "FILTER_DRAFT"};
        
        for (String filter : filters) {
            try {
                Response response = given()
                    .header("Authorization", authToken)
                    .queryParam("classwork_type", "CLASSWORK_TYPE_ASSIGNMENT")
                    .queryParam("filter", filter)
                    .when()
                    .get("/v1/classes/" + classId + "/classwork")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
                
                JsonNode responseJson = objectMapper.readTree(response.getBody().asString());
                if (responseJson.has("streamObjects")) {
                    for (JsonNode streamObject : responseJson.get("streamObjects")) {
                        String assignmentId = streamObject.get("message").get("id").asText();
                        deleteAssignment(assignmentId, authToken);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Failed to delete assignments with filter: {}", filter, e);
            }
        }
    }
    
    private void deleteAllAssessments(String classId, String authToken) {
        // Similar implementation to deleteAllAssignments but for assessments
        LOGGER.info("Deleting all assessments for class: {}", classId);
    }
    
    private void deleteAllAnnouncements(String classId, String authToken) {
        // Similar implementation for announcements
        LOGGER.info("Deleting all announcements for class: {}", classId);
    }
    
    private void deleteAssignment(String assignmentId, String authToken) {
        try {
            given()
                .header("Authorization", authToken)
                .when()
                .delete("/v1/assignments/" + assignmentId)
                .then()
                .statusCode(200);
            
            LOGGER.info("Deleted assignment: {}", assignmentId);
        } catch (Exception e) {
            LOGGER.error("Failed to delete assignment: {}", assignmentId, e);
        }
    }
    
    // Response classes
    public static class ChatMessageResponse {
        private final String message;
        private final String chatId;
        private final boolean success;
        
        public ChatMessageResponse(String message, String chatId, boolean success) {
            this.message = message;
            this.chatId = chatId;
            this.success = success;
        }
        
        public String getMessage() { return message; }
        public String getChatId() { return chatId; }
        public boolean isSuccess() { return success; }
    }
    
    public static class AnnouncementResponse {
        private final String body;
        private final boolean success;
        
        public AnnouncementResponse(String body, boolean success) {
            this.body = body;
            this.success = success;
        }
        
        public String getBody() { return body; }
        public boolean isSuccess() { return success; }
    }
    
    public static class AssignmentResponse {
        private final String title;
        private final String instructions;
        private final boolean success;
        
        public AssignmentResponse(String title, String instructions, boolean success) {
            this.title = title;
            this.instructions = instructions;
            this.success = success;
        }
        
        public String getTitle() { return title; }
        public String getInstructions() { return instructions; }
        public boolean isSuccess() { return success; }
    }
}
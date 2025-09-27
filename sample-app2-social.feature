Feature: Mobile App 2 - Social Features
  As a user of Mobile App 2
  I want to interact socially within the app
  So that I can connect and share with other users

  Background:
    Given I have launched "Mobile App 2"
    And I am logged into Mobile App 2
    And I am on the social feed screen

  @smoke @social @iosMobileApp2 @androidMobileApp2
  Scenario: View social feed in Mobile App 2
    Then I should see the Mobile App 2 social feed
    And I should see recent posts from other users
    And I should see the post creation button
    And the feed should be scrollable

  @regression @iosMobileApp2 @androidMobileApp2 @posting
  Scenario: Create a new post in Mobile App 2
    When I tap the "Create Post" button
    And I enter "This is a test post from Mobile App 2!" in the post content
    And I tap "Publish"
    Then my post should appear at the top of the feed
    And I should see "Post published successfully" confirmation
    And the post should show my username and timestamp

  @regression @mobileApp2 @interaction
  Scenario: Like and comment on posts in Mobile App 2
    Given there are posts visible in the Mobile App 2 feed
    When I tap the "Like" button on the first post
    Then the like count should increase by 1
    And the like button should show as activated
    When I tap the "Comment" button
    And I enter "Great post!" in the comment field
    And I tap "Submit Comment"
    Then my comment should appear under the post
    And the comment count should increase by 1

  @regression @mobileApp2 @profile
  Scenario: View user profile in Mobile App 2
    When I tap on a username in the feed
    Then I should navigate to that user's Mobile App 2 profile
    And I should see their profile picture
    And I should see their bio and follower count
    And I should see their recent posts
    And I should see a "Follow" or "Following" button

  @smoke @mobileApp2 @search
  Scenario: Search for users in Mobile App 2
    When I tap the search icon in Mobile App 2
    And I enter "testuser" in the search field
    Then I should see matching user profiles
    And I should be able to tap on a user to view their profile
    And I should see relevant hashtags and posts

  @regression @mobileApp2 @messaging
  Scenario: Send direct message in Mobile App 2
    Given I am viewing another user's profile in Mobile App 2
    When I tap the "Message" button
    Then I should open the Mobile App 2 messaging interface
    When I type "Hello from Mobile App 2!" 
    And I tap "Send"
    Then the message should appear in the conversation
    And I should see "Delivered" status

  @smoke @mobileApp2 @notifications @ios @android
  Scenario: Receive social notifications in Mobile App 2
    Given I have notifications enabled for Mobile App 2
    When another user likes my post
    Then I should receive a push notification
    And the notification should say "Someone liked your post in Mobile App 2"
    When I tap the notification
    Then Mobile App 2 should open to the relevant post

  @regression @mobileApp2 @privacy
  Scenario: Manage privacy settings in Mobile App 2
    When I navigate to Mobile App 2 settings
    And I tap on "Privacy Settings"
    Then I should see options for:
      | Setting | Options |
      | Profile Visibility | Public, Friends Only, Private |
      | Message Permissions | Everyone, Friends Only, No One |
      | Activity Status | Show Online Status, Hide Status |
    When I change my profile visibility to "Friends Only"
    And I save the settings
    Then I should see "Privacy settings updated" confirmation

  @regression @mobileApp2 @reporting
  Scenario: Report inappropriate content in Mobile App 2
    Given I see a post with inappropriate content
    When I long-press on the post
    Then I should see a context menu with "Report Post" option
    When I tap "Report Post"
    Then I should see reporting categories:
      | Category |
      | Spam |
      | Harassment |
      | Inappropriate Content |
      | Copyright Violation |
    When I select "Inappropriate Content"
    And I tap "Submit Report"
    Then I should see "Thank you for reporting. We'll review this content."
    And the post should be hidden from my feed

  @smoke @mobileApp2 @sharing
  Scenario: Share content from Mobile App 2
    Given I am viewing a post in Mobile App 2
    When I tap the "Share" button
    Then I should see sharing options:
      | Option |
      | Copy Link |
      | Share to Messages |
      | Share to Other Apps |
    When I tap "Copy Link"
    Then the post URL should be copied to clipboard
    And I should see "Link copied" confirmation

  @regression @mobileApp2 @offline
  Scenario: Handle offline mode in Mobile App 2 social features
    Given I have previously loaded the Mobile App 2 social feed
    When I disconnect from the internet
    Then I should still be able to view previously loaded posts
    And I should see "You're offline" indicator
    When I try to create a new post
    Then I should see "Posts will be sent when you're back online"
    When I reconnect to the internet
    Then any pending posts should be automatically published

  @regression @mobileApp2 @performance
  Scenario: Smooth scrolling performance in Mobile App 2 feed
    Given the Mobile App 2 social feed has loaded
    When I scroll through the feed rapidly
    Then the scrolling should be smooth without lag
    And images should load progressively
    And the feed should automatically load more content when I reach the bottom
    And loading should not interrupt the user experience
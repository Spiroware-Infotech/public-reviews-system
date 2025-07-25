# Review Management Sytem

Creating a Review Management System for a college or coaching center and many more categories application involve enabling users and other stakeholders (like parents or alumni) to provide feedback on courses, infrastructure, etc. A moderate but powerful feature set will balance usability, analytics, and moderation. Public users can search and give review comments for the different organizations


## Core Modules:

### 1. User Service
  Handles authentication, profiles, roles (admin, organization, public user).
•	Responsibilities:
o	Register/Login
o	JWT authentication
o	User profile (name, location, etc.)
o	Manage Forgot password send OTP to email
o	Social media login like Gmail and store user information in user table with the default password.
o	Once public users log in, they can update their profile and delete the comments.

________________________________________
### 2. Organization Service
  Manage businesses like coaching centers, doctors, restaurants, etc.
•	Responsibilities:
o	Create/Update organization info
o	Assign categories (e.g., Restaurant, Doctor)
o	Store geo-location, subscription periods
o	Organization can login with limited features. can view all reviews but they can’t update or delete those comments. They can modify only their profile details.
o	Social Media links for each organization
o	Manage the subscription flag if their subscription is over.
o	UI changes- Show sponsored badges, de-priority expired
•	Data Entities: Organization, Category, Subscription
•	Search: Expose via REST or integrate with Elasticsearch
________________________________________
### 3. Review & Rating Service
 Manage reviews, comments, subjects, ratings.
•	Responsibilities:
o	Submit/update/delete review
o	Calculate average rating per org
o	Manage subject + comment + star rating
o	Review moderation (for Admin)
o	Public users can give replay to main comments with the rating filed is optional.
o	We must maintain like and dislike for each comment or review.
o	If anyone commented with abuse or bad words still, we could show for public users.
•	Data Entities: Review
________________________________________
### 4. Search for Gateway Service (Optional but powerful)
  Dedicated service for searching organizations by location, category, and rating.
•	Responsibilities:
o	Serve frontend search queries
o	Integrate with Elasticsearch/PostGIS
o	Filter & rank based on subscription, rating, distance
o	Global search screen if anyone search based on name, we will list done number of categories organization list with top rated.
o	Filter based on local and distance 


________________________________________
### 5. Notification Service (Optional)
 Handles reminders for subscription expiry, new review notifications, etc.
•	Responsibilities:
o	Email/SMS/Web Push for key events
o	Scheduled checks (e.g., subscription expiry)
o	Whenever the user or public tries to give comments, we can rent comment mail to organizations.
o	Add a scheduled job to notify organizations when their promo ends?
o	Include a paid plan upgrade option after free promo?
•	Data Entities: notifications, sms_details


________________________________________

### 6. Admin Service
Manage organizations, reviews, subjects, all users
•	Responsibilities:
o	Submit/update/delete review
o	Create/Update/Delete Users
o	Create/Update/Delete Organizations 
o	Manage Categories – Add/Edit/Delete
o	Manage Notifications
o	Manage subjects
o	Manage the review Statistics
o	Manage the abuse or bad comments list in separate screen for future purpose, each bad comment comes with red flag.



### Tech Stack: Spring Boot + Spring Security + JWT + Swagger 3 Open API + React JS + Redux



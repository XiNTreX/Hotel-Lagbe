<div align="center">
<h1> HOTEL-LAGBE</h1>
</div>

Hotel-Lagbe is a Java-based Client-Server hotel booking platform. It features a graphical user interface built with JavaFX and utilizes socket programming for seamless communication between the client application and the central server. It also integrates with external APIs to fetch real-world locations and hotel data.

-----------------------------------------------------
 FEATURES
-----------------------------------------------------
* User Authentication: Secure sign-up and login system with brute-force protection (30-second lockout after 3 failed attempts).
* Live Location & Hotel Search: Fetches real-world cities in Bangladesh and nearby accommodations using the CountriesNow and Geoapify Places APIs.
* Room Booking System: Users can search for hotels, view different room types (Standard, Deluxe, Executive), and book them for specific dates.
* Booking Management: Users have a personalized dashboard to view active bookings and cancel them if needed.
* In-Memory Database: Manages users, sessions, and active bookings dynamically on the server side.

-----------------------------------------------------
 PREREQUISITES
-----------------------------------------------------
Before running the application, ensure you have the following installed on your local machine:
* Java Development Kit (JDK) 21 (or a compatible modern JDK like 17+)
* Gradle (Optional: The project includes a Gradle wrapper 'gradlew' which will automatically download the necessary Gradle version).
* An active internet connection (Required to fetch dynamic locations and hotel data via APIs).

-----------------------------------------------------
 INSTALLATION GUIDELINES
-----------------------------------------------------
1. Clone or Download the Repository:
```bash
   git clone https://github.com/XiNTreX/Hotel-Lagbe.git
   cd "Hotel-Lagbe/Hotel Lagbe"
```
2. Build the Project:
   Use the provided Gradle wrapper to build the project and automatically download all necessary dependencies (JavaFX, Jackson, etc.).
   
   On Windows:
   ```bash
    gradlew.bat build
   ```
   On macOS/Linux:
   ```bash
    ./gradlew build
   ```
-----------------------------------------------------
 RUNNING THE APPLICATION
-----------------------------------------------------
Because this is a Client-Server application, you MUST start the server before launching the client. Otherwise, the client will fail to connect.

STEP 1: Start the Server
You can run the server directly from your IDE (like IntelliJ IDEA, Eclipse, or VS Code).
* Navigate to: src/main/java/com/hotel_lagbe/server/ServerMain.java
* Run the 'main' method.
* You should see the console output: "Server is online and waiting for clients!" listening on port 8080.

STEP 2: Start the Client
Once the server is running, you can launch the Client Application.
* Using Gradle (Recommended):
  The build.gradle file is pre-configured to run the Client application by default.
  
  On Windows:
  ```bash
   gradlew.bat run
  ```
  On macOS/Linux:
  ```bash
   ./gradlew run
  ```
* Using an IDE: 
  Navigate to src/main/java/com/hotel_lagbe/client/ClientMain.java and run the 'main' method.

-----------------------------------------------------
 USAGE NOTES & TESTING
-----------------------------------------------------
* Admin Test Account: You can sign up for a new account from the launch screen, or use the default pre-configured admin account to test the system immediately:
  - Username: admin
  - Password: admin123

-----------------------------------------------------
 PROJECT STRUCTURE
-----------------------------------------------------
* com.hotel_lagbe.client: Contains the JavaFX views, controllers, and API connection services.
* com.hotel_lagbe.server: Contains the socket server, Request/Response handlers, and the in-memory DataStore.
* com.hotel_lagbe.shared: Contains shared data models (User, Hotel, Room, Booking) and Networking protocols (MessageType, Request, Response) used by both the Client and Server.

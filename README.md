# urlShortener
URL shortener service in Scala

URL Shortener API
A classic microservice project that's great for practicing web frameworks and basic state management.

Core Features:

An API endpoint (e.g., /shorten) that accepts a POST request with a long URL and returns a short URL (e.g., http://your-service/aBc1D).

An API endpoint that, when receiving a GET request for a short URL (e.g., /aBc1D), redirects the user to the original long URL.

Scala Concepts: Akka HTTP, Play Framework, or http4s for routing; circe or play-json for JSON handling; scala.util.Random for key generation; scala.collection.mutable.Map for simple in-memory storage (or Slick/Doobie if you want to add a simple H2/PostgreSQL database).

Difficulty: Medium-Low

Estimated Time: 1-2 days


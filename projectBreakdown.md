Here’s a more detailed breakdown for the **URL Shortener API** project.

### 🎯 Project 1: URL Shortener API

This plan focuses on using **Akka HTTP** and **Circe** for a modern, idiomatic Scala feel. It's perfect for a weekend because you can start with an in-memory "database" and have a fully working app quickly.

-----

### 1\. 🛠️ Recommended Tech Stack

  * **Web Framework:** **Akka HTTP**. It's a powerful library (not a full framework like Play) that gives you fine-grained control over routing and requests using its elegant DSL.
  * **JSON Library:** **Circe**. A very popular, powerful, and functional JSON library that integrates well with Akka HTTP via the `akka-http-circe` helper library.
  * **Storage (for the weekend):** `scala.collection.concurrent.Map`. This is a thread-safe map perfect for a simple, in-memory key-value store. It requires no database setup.

-----

### 2\. 🗺️ Weekend Battle Plan

Here is a step-by-step guide to structure your weekend.

#### Day 1: Setup and the "Shorten" Endpoint

1.  **Project Setup (1-2 hours):**

      * Create a new Scala project using `sbt`.
      * Add your `build.sbt` dependencies: `akka-http`, `akka-stream`, `circe-core`, `circe-generic`, `circe-parser`, and `akka-http-circe`.

2.  **Define Your Models (30 mins):**

      * Create `case class`es for your API. You'll need `akka-http-circe`'s `de.heikoseeberger.akkahttpcirce.FailFastCirceSupport` and `io.circe.generic.auto._`.

    <!-- end list -->

    ```scala
    // In a new file, e.g., models/UrlModels.scala
    case class ShortenRequest(url: String)
    case class ShortenResponse(shortUrl: String)
    ```

3.  **Create the Storage Service (1 hour):**

      * This service will handle the logic of storing and retrieving URLs.

    <!-- end list -->

    ```scala
    // In a new file, e.g., services/UrlRepository.scala
    import scala.collection.concurrent.Map
    import scala.util.Random

    class UrlRepository {
      // Our in-memory "database"
      private val urlStore: Map[String, String] = 
        scala.collection.concurrent.TrieMap[String, String]()

      private def generateShortKey(): String = {
        // Simple 6-char random alphanumeric string
        Random.alphanumeric.take(6).mkString
      }

      def save(longUrl: String): String = {
        val key = generateShortKey()
        // We'll assume no collisions for a weekend project!
        urlStore.put(key, longUrl)
        key
      }

      def find(key: String): Option[String] = {
        urlStore.get(key)
      }
    }
    ```

4.  **Build the POST Route (2-3 hours):**

      * Create your Akka HTTP routes. This is the core of the app.

    <!-- end list -->

    ```scala
    // In a new file, e.g., routing/UrlRoutes.scala
    import akka.http.scaladsl.server.{Directives, Route}
    import akka.http.scaladsl.model.{StatusCodes}
    import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

    class UrlRoutes(repository: UrlRepository) 
      extends Directives with FailFastCirceSupport {
      
      // We'll need circe's auto-derivation
      import io.circe.generic.auto._
      
      val routes: Route =
        path("shorten") {
          post {
            // Automatically unmarshals the JSON body to our case class
            entity(as[ShortenRequest]) { request =>
              val key = repository.save(request.url)
              
              // !! Remember to replace localhost:8080 with your actual domain/config
              val response = ShortenResponse(s"http://localhost:8080/$key")
              
              complete(StatusCodes.Created, response)
            }
          }
        }
    }
    ```

#### Day 2: The "Redirect" Endpoint and Server

1.  **Build the GET Route (1-2 hours):**

      * Add the redirect logic to your `UrlRoutes` class.

    <!-- end list -->

    ```scala
    // Inside UrlRoutes class, combine with the existing route

    val routes: Route =
      concat(
        path("shorten") {
          // ... (your POST route from Day 1)
        },
        path(Segment) { key =>
          get {
            repository.find(key) match {
              case Some(longUrl) =>
                // Send a 302 Found redirect
                redirect(longUrl, StatusCodes.Found)
              case None =>
                complete(StatusCodes.NotFound, s"Short URL '$key' not found.")
            }
          }
        }
      )
    ```

2.  **Create the Main App (1 hour):**

      * Create an `object` with a `main` method to tie everything together and start the server.

    <!-- end list -->

    ```scala
    // In a new file, e.g., Main.scala
    import akka.actor.ActorSystem
    import akka.http.scaladsl.Http
    import scala.concurrent.ExecutionContext
    import scala.io.StdIn

    object Main {
      def main(args: Array[String]): Unit = {
        implicit val system: ActorSystem = ActorSystem("url-shortener")
        implicit val ec: ExecutionContext = system.dispatcher

        val repository = new UrlRepository()
        val allRoutes = new UrlRoutes(repository).routes

        val binding = Http().newServerAt("localhost", 8080).bind(allRoutes)

        println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
        StdIn.readLine() // let it run until user presses return

        binding
          .flatMap(_.unbind()) // trigger unbinding from the port
          .onComplete(_ => system.terminate()) // and shutdown when done
      }
    }
    ```

3.  **Test and Refine (2-3 hours):**

      * Run your `Main` app.
      * Use a tool like `curl` or Postman to test:
          * **POST:** `curl -X POST -H "Content-Type: application/json" -d '{"url":"https://www.google.com"}' http://localhost:8080/shorten`
          * **GET:** Open the returned short URL (e.g., `http://localhost:8080/aBc1D`) in your browser. It should redirect you to Google.
          * **Test 404:** Try a key that doesn't exist.

-----

### 3\. 🧑‍💻 Key Scala Concepts You'll Practice

  * **Case Classes & ADTs:** Perfect for modeling your JSON request/response.
  * **Traits/Classes:** Structuring your app by separating concerns (e.g., `UrlRepository`, `UrlRoutes`).
  * **Futures:** Akka HTTP uses `Future`s under the hood for non-blocking I/O (though the high-level DSL hides much of it).
  * **Functional Composition:** The Akka HTTP routing DSL (e.g., `path`, `get`, `post`, `concat`) is a prime example of composing small functions.
  * **Implicits:** Used heavily by both Akka HTTP and Circe to find the correct JSON converters (`FailFastCirceSupport`) and a "materializer" for running streams.
  * **Collections:** Using a `concurrent.Map` for thread-safe state.

Does this detailed plan look like a good fit for your long weekend?

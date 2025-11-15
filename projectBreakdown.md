Absolutely. Cask is a fantastic choice for this\! It's much lighter and simpler than Akka HTTP, making it perfect for a long-weekend project where you want to focus on logic rather than boilerplate.

(A quick note: **Cask** is a *server* framework, which is what you need to build the API. **sttp** is primarily a *client* library, which you would use to *call* other APIs. So for this project, Cask is the tool for the job.)

Here is the amended plan using **Cask** and **sbt**.

-----

### 1\. 🛠️ Revised Tech Stack

  * **Web Framework:** **Cask**. A minimal, annotation-based web framework from Li Haoyi.
  * **JSON Library:** **ujson**. Cask's default JSON library, also by Li Haoyi. It's simple and requires no setup.
  * **Storage:** `scala.collection.concurrent.Map`. The in-memory `TrieMap` is still the perfect thread-safe choice.
  * **Build Tool:** **sbt**.

-----

### 2\. 🗺️ Weekend Battle Plan (Cask Edition)

This plan is simpler because Cask bundles the server and JSON handling into one small package. You can likely fit this all in a single `.scala` file.

#### Day 1, Step 1: Project Setup (1 hour)

Create a new sbt project. Your `build.sbt` will be very simple.

```scala
// build.sbt
scalaVersion := "2.13.12" // Or 3.x.x

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "cask" % "0.9.1",
  // For testing, highly recommended
  "com.lihaoyi" %% "utest" % "0.8.2" % "test"
)

// Add this line if you want to use the utest framework
testFrameworks += new TestFramework("utest.runner.Framework")
```

After setting this up, run `sbt` in your terminal to download the dependencies.

#### Day 1, Step 2: Create the Main App (3-4 hours)

Create a file like `src/main/scala/UrlShortener.scala`. We can put both the storage and the web routes in here.

```scala
import scala.collection.concurrent.TrieMap
import scala.util.Random

// This object will hold our app's logic
object UrlShortener extends cask.MainRoutes {

  // 1. --- The Storage Service ---
  // Our in-memory "database"
  private val urlStore: TrieMap[String, String] = TrieMap[String, String]()

  private def generateShortKey(): String = {
    // Simple 6-char random alphanumeric string
    // We'll still assume no collisions for a weekend project!
    Random.alphanumeric.take(6).mkString
  }

  // 2. --- The "Shorten" Endpoint (POST /shorten) ---
  @cask.post("/shorten")
  def shorten(request: cask.Request): cask.Response = {
    try {
      // Parse the JSON body using ujson
      val json = ujson.read(request.text())
      val longUrl = json("url").str

      // Save to our "database"
      val key = generateShortKey()
      urlStore.put(key, longUrl)

      // !! Remember to update localhost:8080 with your actual host/port
      val shortUrl = s"http://localhost:8080/$key"

      // Create a JSON response
      cask.Response(ujson.Obj("shortUrl" -> shortUrl), statusCode = 201) // 201 Created

    } catch {
      case e: Exception =>
        // Basic error handling
        cask.Response(ujson.Obj("error" -> "Bad request"), statusCode = 400)
    }
  }

  // 3. --- The "Redirect" Endpoint (GET /:key) ---
  @cask.get("/:key")
  def redirect(key: String): cask.Response = {
    urlStore.get(key) match {
      case Some(longUrl) =>
        // Cask has a built-in Redirect helper
        cask.Redirect(longUrl, statusCode = 302) // 302 Found
      case None =>
        cask.Response(ujson.Obj("error" -> "Not found"), statusCode = 404)
    }
  }

  // 4. --- Start the Server ---
  println(s"Server starting at http://localhost:8080 ...")
  initialize()
}
```

#### Day 2, Step 3: Run and Test (1-2 hours)

1.  **Run the App:**

      * Go to your sbt shell (by typing `sbt`).
      * Type `run`.
      * Your server is now running\!

2.  **Test with `curl` (or Postman):**

      * **POST (Create Short URL):**

        ```bash
        curl -X POST -H "Content-Type: application/json" \
             -d '{"url":"https://www.google.com"}' \
             http://localhost:8080/shorten
        ```

        *You should get back:* `{"shortUrl":"http://localhost:8080/aBc1D"}` (with a random key)

      * **GET (Test Redirect):**

          * Copy the short URL from the response (e.g., `http://localhost:8080/aBc1D`) and paste it into your browser. It should redirect you to Google.
          * Or, use `curl -v` to see the redirect header:
            ```bash
            curl -v http://localhost:8080/aBc1D
            ```

        *You'll see a `302 Found` and a `Location: https://www.google.com` header.*

-----

### 3\. 🧑‍💻 Key Scala Concepts You'll Practice

  * **Annotations:** Cask uses `@cask.post` and `@cask.get` to define routes, which is a different (and simpler) style than Akka HTTP's DSL.
  * **Simple JSON:** Using `ujson` directly (`ujson.read`, `ujson.Obj`) is a great, lightweight alternative to complex case class derivation with Circe.
  * **Error Handling:** Using a simple `try/catch` or `Option.get` is very common in Cask apps for quick error responses.
  * **Main Object:** Extending `cask.MainRoutes` is a simple way to boot a full web server.

This Cask-based approach significantly reduces the setup time and complexity, letting you build the "fully functional software" part even faster.

How does this revised plan with Cask look for your weekend?



### 1. 🎯 URL Shortener API

A classic microservice project that's great for practicing web frameworks and basic state management.

* **Core Features:**
    * An API endpoint (e.g., `/shorten`) that accepts a POST request with a long URL and returns a short URL (e.g., `http://your-service/aBc1D`).
    * An API endpoint that, when receiving a GET request for a short URL (e.g., `/aBc1D`), redirects the user to the original long URL.
### 1\. 🛠️ Revised Tech Stack

  * **Web Framework:** **Cask**. A minimal, annotation-based web framework from Li Haoyi.
  * **JSON Library:** **ujson**. Cask's default JSON library, also by Li Haoyi. It's simple and requires no setup.
  * **Storage:** `scala.collection.concurrent.Map`. The in-memory `TrieMap` is still the perfect thread-safe choice.
  * **Build Tool:** **sbt**.
* **Difficulty:** **Medium-Low**
* **Estimated Time:** **1-2 days**


object UrlShortener extends cask.MainRoutes {

        val store = scala.collection.mutable.Map[String, String]()

        def shortenAndStoreUrl(url: String): String = {
                val key= scala.util.Random.alphanumeric.take(10).mkString
                store(key) = url

                "http://localhost:8080/"+key
        }

        @cask.staticResources("/static")
        def staticEndpoint(): String="."

        @cask.post("/shorten")
        def shorten(request: cask.Request) = {
                shortenAndStoreUrl(request.text())
        }

        initialize()
}


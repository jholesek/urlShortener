object UrlShortener extends cask.MainRoutes {
        def shortenUrl(url: String): String = {
                url.reverse
        }

        @cask.staticResources("/static")
        def staticEndpoint(): String="."

        @cask.post("/shorten")
        def shorten(request: cask.Request) = {
                shortenUrl(request.text())
        }

        initialize()
}


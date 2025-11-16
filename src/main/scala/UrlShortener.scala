object UrlShortener extends cask.MainRoutes {
        @cask.staticResources("/static")
        def staticEndpoint(): String="."

        @cask.post("/shorten")
        def shorten(request: cask.Request) = {
                request.text().reverse
        }

        initialize()
}


object Example extends cask.MainRoutes {
        @cask.staticFiles("/static")
        def staticEndpoint(): String = "src/main/resources"

        initialize()

}

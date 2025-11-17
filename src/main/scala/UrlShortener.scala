import scalatags.Text.all._

object UrlShortener extends cask.MainRoutes {

        val store = scala.collection.mutable.Map[String, String]()

        def shortenAndStoreUrl(url: String): String = {
                val key= scala.util.Random.alphanumeric.take(10).mkString
                store(key) = url

                "http://localhost:8080/link/"+key
        }

        @cask.staticResources("/static")
        def staticEndpoint(): String="."

        @cask.get("/link")
        def forwardReq(segments: cask.RemainingPathSegments): doctype = {
                //s"""<a href=""""+store(segments.value.mkString)+s"""">link</a>"""
                //cask.Redirect(store(segments.value.mkString))
                //segments.value.mkString
                doctype("html") (
                        html(
                                head(
                                        (s"""
                                                <meta charset="UTF-8">
        <meta http-equiv="refresh" content="0; url=http://example.com">
                                        """),
                                        meta(s"""http-equiv:="refresh""
                                                content="0; url=http://example.com" """),
                                        script(s"""
            window.location.href = "http://example.com" """)
                                ),
                                body( p(
                                        ("If you are not redirected automatically, follow this" ), 
                                        a(href:="http://example.com")(p("link to example"))
                                        )
                                )
                        )
                )
        }

        @cask.post("/shorten")
        def shorten(request: cask.Request) = {
                shortenAndStoreUrl(request.text())
        }

        initialize()
}


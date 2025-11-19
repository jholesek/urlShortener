import scalatags.Text.all._

object UrlShortener extends cask.MainRoutes {

        val store = scala.collection.mutable.Map[String, String]()

        def shortenAndStoreUrl(url: String): String = {
                val key= scala.util.Random.alphanumeric.take(10).mkString
                store(key) = url

                "http://localhost:8080/link/"+key
        }

        @cask.staticFiles("/static")
        def staticEndpoint(): String="src/main/resources"

        @cask.get("/check")
        def returnCheck(): cask.Response[String] = {
                cask.Response("""<!doctype html>
                        |<html><body>krneki</body></html>
                        """.stripMargin,
                        headers = Seq("Content-Type" -> "text/html")
                        )
        }

  @cask.get("/login")
  def getLogin(): cask.Response[String] = {
    val html =
      """<!doctype html>
        |<html>
        |<body>
        |<form action="/login" method="post">
        |  <label for="name">Username:</label><br>
        |  <input type="text" name="name" value=""><br>
        |  <label for="password">Password:</label><br>
        |  <input type="text" name="password" value=""><br><br>
        |  <input type="submit" value="Submit">
        |</form>
        |</body>
        |</html>""".stripMargin

    cask.Response(data = html, headers = Seq("Content-Type" -> "text/html"))
  }

        @cask.get("/link")
        def forwardReq(segments: cask.RemainingPathSegments): cask.Response[String] = {
                //s"""<a href=""""+store(segments.value.mkString)+s"""">link</a>"""
                //cask.Redirect(store(segments.value.mkString))
                val redir = store(segments.value.mkString)
                println(redir)
                val html =
                        """<!DOCTYPE HTML>
                                <html lang="en-US">
                                <head>
                                <meta charset="UTF-8">
                                <meta http-equiv="refresh" content="0; url="""+redir+s""" ">
                                <title>Page Redirection</title>
                                </head>
                                <body>
                                        If you are not redirected automatically, follow this <a href='"""+redir+"""'>link """+redir+"""</a>.
                                </body>
                                </html>""".stripMargin
                cask.Response(data = html, headers = Seq("Content-Type" -> "text/html"))
                
        }

        @cask.post("/shorten")
        def shorten(request: cask.Request) = {
                shortenAndStoreUrl(request.text())
        }

        initialize()
}


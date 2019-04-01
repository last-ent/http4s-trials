package com.example

import org.log4s.getLogger
import cats.effect._
import cats.implicits._
import com.example.auth.AuthClient
import com.example.hello.HelloRoute
import com.example.user.UserRoute
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Server extends IOApp {
  val logger = getLogger("Simple Log")

  val services = HelloRoute.service <+> AuthClient.middleware(UserRoute.service)

  val httpApp = Router("/" -> services).orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}

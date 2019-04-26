package com.example

import cats.data.Kleisli
import org.log4s.{Logger, getLogger}
import cats.effect._
import cats.implicits._
import com.example.auth.{AuthClient, Injector}
import com.example.hello.HelloRoute
import com.example.user.UserRoute
import org.http4s.{Request, Response}
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Server extends IOApp {
  val logger: Logger = getLogger("Simple Log")

  val services = Injector.protect(Injector.wrap(HelloRoute.service)) <+> AuthClient.middleware(UserRoute.service)

  val httpApp: Kleisli[IO, Request[IO], Response[IO]] = Router("/" -> services).orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}

package com.example.auth

import cats.data.{Kleisli, OptionT}
import cats.effect._
import org.http4s.{Header, HttpRoutes, Request, Response}
import org.log4s.{Logger, getLogger}
import org.http4s.dsl.io._
object Injector {
  val logger: Logger = getLogger("Simple Log")

  def wrap(service: HttpRoutes[IO]): HttpRoutes[IO] = Kleisli{ req: Request[IO] =>
    service(req.withHeaders(Header("Name", "Bob"))).map{
      case resp => resp.withHeaders(Header("Bye", "bye!"))
    }
  }

  def protect(service: HttpRoutes[IO]): HttpRoutes[IO] = Kleisli { req: Request[IO] =>
    OptionT {
     service.run(req).value.handleErrorWith(ErrorHandlers.handlers)
    }
  }
}


object ErrorHandlers {
  val handlers: Throwable => IO[Option[Response[IO]]] = {
    case err: RuntimeException =>
      BadRequest(s"""{"error": "${err.getMessage}"}""").map(Option(_))
  }
}
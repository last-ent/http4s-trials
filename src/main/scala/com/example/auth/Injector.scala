package com.example.auth

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import org.http4s.dsl.io.Forbidden
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedService, Header, HttpRoutes, Request}
import cats.implicits._

object Injector {
  def wrap(service: HttpRoutes[IO]): HttpRoutes[IO] = Kleisli{ req: Request[IO] =>
    service(req.withHeaders(Header("Name", "Bob"))).map{
      case resp => resp.withHeaders(Header("Bye", "bye!"))
    }
  }
}

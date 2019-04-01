package com.example.hello

import cats.effect.IO
import org.http4s._
import org.http4s.dsl.io._

object HelloRoute {
  val service = HttpRoutes.of[IO] {
    case GET -> Root =>
      Ok("Hello.")
  }
}

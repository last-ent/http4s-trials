package com.example.hello

import cats.effect.IO
import com.example.auth.AuthClient
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.util.CaseInsensitiveString

object HelloRoute {
  val service = HttpRoutes.of[IO] {
    case req @ GET -> Root =>
      Ok(req.headers.get(CaseInsensitiveString("Name")).getOrElse("Not found").toString)

    case req @ GET -> Root / "userF" =>
      for {
        user <- AuthClient.authUserF(req)
        response <- Ok(user.toString)
      } yield response
  }
}

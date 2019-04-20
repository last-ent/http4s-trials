package com.example.user

import cats.effect._
import com.example.auth.User
import org.http4s._
import org.http4s.dsl.io._

object UserRoute {
  def service[F[_]: Effect]: AuthedService[User, F] =
    AuthedService {
      case GET -> Root / "user" as user => Ok(user.toString + "\n").asInstanceOf[F[Response[F]]]
      case GET -> Root / "asdf" as user => false
    }
}

package com.example.user

import cats.effect._
import com.example.auth.User
import org.http4s._
import org.http4s.dsl.io._

object UserRoute {
  val service: AuthedService[User, IO] =
    AuthedService {
      case GET -> Root / "user" as user => Ok(user.toString + "\n")
    }
}

package com.example.auth

import cats.data._
import cats.effect._
import cats.instances.string._
import cats.syntax.eq._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.headers.Authorization
import org.http4s.server._

case class User(id: Long, name: String) {
  override def toString: String = s"Id: $id, name: $name"
}

object AuthClient {

  def isAuthorized(s: String): IO[Either[String, User]] =
    s === "Bearer Token" match {
      case true => IO.pure(Right(User(10, s)))
      case false => IO.pure(Left("Invalid Token."))
    }

  val authUser: Kleisli[IO, Request[IO], Either[String, User]] = Kleisli({ request =>
    request
      .headers
      .get(Authorization) match {
      case None => IO.pure(Left("Couldn't find an Authorization Header"))
      case Some(header) => isAuthorized(header.value)
    }
  })

  val onFailure: AuthedService[String, IO] = Kleisli(req => OptionT.liftF(Forbidden(req.authInfo)))

  val middleware: AuthMiddleware[IO, User] = AuthMiddleware(
    authUser = authUser,
    onFailure = onFailure
  )
}

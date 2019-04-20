package com.example.auth

import cats.data._
import cats.effect._
import cats.instances.string._
import cats.syntax.eq._
import org.http4s
import org.http4s._
import org.http4s.headers.Authorization
import org.http4s.server._

case class User(id: Long, name: String) {
  override def toString: String = s"Id: $id, name: $name"
}

object AuthClient {

  def isAuthorized[F[_]: Effect](s: String): F[Either[String, User]] =
    s === "Bearer Token" match {
      case true => Effect[F].pure(Right(User(10, s)))
      case false => Effect[F].pure(Left("Invalid Token."))
    }

  def authUser[F[_]: Effect]: Kleisli[F, Request[F], Either[String, User]] = Kleisli({ request =>
    request
      .headers
      .get(Authorization) match {
      case None => Effect[F].pure(Left("Couldn't find an Authorization Header"))
      case Some(header) => isAuthorized(header.value)
    }
  })
//  final case class Response[F[_]](
//                                   status: Status = Status.Ok,
//                                   httpVersion: HttpVersion = HttpVersion.`HTTP/1.1`,
//                                   headers: Headers = Headers.empty,
//                                   body: EntityBody[F] = EmptyBody,
//                                   attributes: Vault = Vault.empty)
  def onFailure[F[_]: Effect]: AuthedService[String, F] =
  Kleisli(req => OptionT.liftF(
    Effect[F].pure(Response[F](status = Status.Forbidden, body = http4s.Entity(req.authInfo)))
  ))

  def middleware[F[_]: Effect]: AuthMiddleware[F, User] = AuthMiddleware(
    authUser = authUser,
    onFailure = onFailure
  )
}

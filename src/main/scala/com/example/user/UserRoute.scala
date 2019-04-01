package com.example.user

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.headers.Authorization

object Logic {
  def get(token: Either[String, Authorization.HeaderT]): IO[Option[String]] =
    token match {
      case Right(tok) if tok.value == "Bearer Token" => IO.pure(Some("Hello, Bob."))
      case _ => IO.pure(None)
    }
}

case class AuthTok(value: String)

object UserRoute {
  def authenticate(pf: PartialFunction[Either[String, Authorization.HeaderT], IO[Response[IO]]])
                  (req: Request[IO]) =
    req
      .headers
      .get(Authorization)
      .toRight("AuthToken not found.") match {
      case Left(err: String) => Forbidden(err)
      case token => Logic.get(token).flatMap{
        case None => Forbidden("Invalid Token.")
        case Some(msg) => Ok(msg.toString)
      }
    }

val service = HttpRoutes.of[IO] {
  case req@GET -> Root / "user" =>
  authenticate ( {
  case Right (user) =>
  println (user)
  Ok (user.toString () )
}) (req)


}
}

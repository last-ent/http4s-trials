//package com.example.auth
//
//import cats.effect._
//import cats.implicits._
//import cats.data._
//import org.http4s._
//import org.http4s.dsl.io._
//import org.http4s.headers.Authorization
////import org.http4s.implicits._
//import org.http4s.server._
//
//case class User(id: Long, name: String)
//
//object AuthClient {
//
////  val authUser: Kleisli[OptionT[IO, ?], Request[IO], User] =
////    Kleisli(_ => OptionT.liftF(IO(???)))
//
//  def isAuthorized(s: String): Option[User] =
//    if(s == "Token") Some(User(10, s)) else None
//
//  val authUser: Kleisli[IO, Request[IO], Either[String, User]] = Kleisli({request =>
//    val message = for {
//      header <- request.headers.get(Authorization).toRight("Couldn't find an Authorization Header")
//      token <- isAuthorized(header.value).toRight("Unauthorized to access this url.")
//    } yield token
//    IO.pure(message)
//  })
//
//  val onFailure = AuthedService[String, IO] =
//    Kleisli(req => OptionT.liftF(Forbidden(req)))
//
//  val middleware: AuthMiddleware[IO, User] =
//}

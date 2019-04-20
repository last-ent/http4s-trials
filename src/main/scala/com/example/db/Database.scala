package com.example.db

import cats.effect._
import doobie._
import doobie._
import doobie.implicits._
import cats._
import cats.effect._
import cats.implicits._


import scala.concurrent.ExecutionContext


case class DBConfig(host: String, username: String, password: String, databaseName: String)

class Database(config: DBConfig) {
  implicit val cs = IO.contextShift(ExecutionContext.global)

  private val xa = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver", // driver classname
    url = s"jdbc:postgresql:${config.host}/${config.databaseName}", // connect URL (driver-specific)
    user = config.username, // user
    pass = config.password // password
  )

  def executeQuery[A](query: doobie.ConnectionIO[A]): IO[A] = query.transact(xa)
}

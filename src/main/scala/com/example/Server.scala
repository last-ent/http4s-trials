package com.example

import cats.data.Kleisli
import com.example.middleware.CallTracer
import io.jaegertracing.Configuration
import org.log4s.{Logger, getLogger}
import cats.effect._
import cats.implicits._
import com.example.middleware.{AuthClient, Injector}
import com.example.hello.HelloRoute
import com.example.user.UserRoute
import org.http4s.{Request, Response}
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import com.example.opentracing.MyOpenTracer
import io.opentracing.Tracer
import com.example.tracedcall.TracedRoute
import io.opentracing.mock.MockTracer
import io.jaegertracing.Configuration.{SamplerConfiguration, ReporterConfiguration}
import io.jaegertracing.Configuration

object Server extends IOApp {
  val logger: Logger = getLogger("Simple Log")

  def getTracer(): Tracer = {
    val config = Configuration.fromEnv("trail-server")
    println(config.getSampler.getParam)
    val samplerConfig = SamplerConfiguration.fromEnv().withType("const").withParam(1)
    val reporterConfig = ReporterConfiguration.fromEnv().withLogSpans(true)
    config.withSampler(samplerConfig).withReporter(reporterConfig).getTracer
  }

  val tracer: Tracer = getTracer

  val services = Injector.protect(Injector.wrap(HelloRoute.service)) <+> CallTracer.trace(new MyOpenTracer(tracer))(TracedRoute.service) <+> AuthClient.middleware(UserRoute.service)
  val httpApp: Kleisli[IO, Request[IO], Response[IO]] = Router("/" -> services).orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}

package com.example.middleware

import com.example.opentracing.MyOpenTracer
import io.opentracing.mock.MockTracer
import io.opentracing.{Tracer, Span}
import com.example.opentracing.OpenTracer
import org.http4s.{Header, HttpRoutes, Request, Response}
import cats.effect.IO
import cats.data.{OptionT, Kleisli}
import org.http4s.dsl.io._

object CallTracer {
  def trace(openTracer: OpenTracer)
           (service: (Span, OpenTracer) => HttpRoutes[IO]): HttpRoutes[IO] = Kleisli { req: Request[IO] =>
    OptionT {
      val rootSpan: Span = openTracer.createRootSpan("TracedRoute")
      val resp = service(rootSpan, openTracer)
        .run(req)
        .value
        .handleErrorWith{err =>
          openTracer.logError(rootSpan, err)
          BadRequest(s"""{"error": "${err.getMessage}"}""").map(Option(_))
        }
      openTracer.closeSpan(rootSpan)
      resp
    }
  }
}

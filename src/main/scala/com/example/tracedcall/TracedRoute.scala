package com.example.tracedcall

import cats.effect.IO
import com.example.opentracing.OpenTracer
import io.opentracing.{ Span, Tracer }
import org.http4s._
import org.http4s.dsl.io._

object TracedRoute {
  def service(rootSpan: Span, tracer: Tracer, openTracer: OpenTracer) = HttpRoutes.of[IO] {
    case req @ GET -> Root / "name" / name => Ok(s"""{"name": $name}""")
  }
}

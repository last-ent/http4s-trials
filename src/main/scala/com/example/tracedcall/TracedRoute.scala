package com.example.tracedcall

import cats.effect.IO
import com.example.opentracing.OpenTracer
import io.opentracing.{ Span, Tracer }
import org.http4s._
import org.http4s.dsl.io._

object TracedRoute {
  def service(rootSpan: Span, openTracer: OpenTracer) = HttpRoutes.of[IO] {
    case req @ GET -> Root / "name" / name =>
      openTracer.createChildSpan("call_name", rootSpan).finish()
      Ok(s"""{"name": $name}""")
  }
}

package com.example.opentracing

import io.jaegertracing.Configuration
import io.opentracing.{Tracer, Span}


trait OpenTracer {
  def getTracer(serviceName: String): Tracer
  def getRootSpan(rootSpanName: String): Span
  def getChildSpan(rootSpan: Span): Span
  def closeSpan(span: Span): Span
  def logError(span: Span, exception: Throwable): Span
}

class OpenTracing extends OpenTracer {
  def getTracer(serviceName: String): Tracer =
    Configuration.fromEnv(serviceName).getTracer
}

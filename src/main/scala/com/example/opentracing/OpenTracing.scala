package com.example.opentracing

import io.jaegertracing.Configuration
import io.opentracing.{Tracer, Span}


trait OpenTracer {
  def getTracer(serviceName: String): Tracer

  def createRootSpan(rootSpanName: String, tracer: Tracer): Span
  def createChildSpan(spanName: String, rootSpan: Span, tracer: Tracer): Span

  def logError(span: Span, exception: Throwable): Span
  def closeSpan(span: Span)
}

object MyOpenTracer extends OpenTracer {
  def getTracer(serviceName: String): Tracer =
    Configuration.fromEnv(serviceName).getTracer

  def createRootSpan(rootSpanName: String, tracer: Tracer): Span =
    tracer.buildSpan(rootSpanName).start()

  def createChildSpan(spanName: String, rootSpan: Span, tracer: Tracer): Span =
    tracer.buildSpan(spanName).asChildOf(rootSpan).start()


  def closeSpan(span: Span) = span.finish()

  def logError(span: Span, exception: Throwable): Span = span.log(exception.getMessage)
}

package com.example.opentracing

import io.jaegertracing.Configuration
import io.opentracing.{Tracer, Span}
import org.log4s.{Logger, getLogger}

trait OpenTracer {
  def createRootSpan(rootSpanName: String): Span
  def createChildSpan(spanName: String, rootSpan: Span): Span

  def logError(span: Span, exception: Throwable): Span
  def closeSpan(span: Span)
}

class MyOpenTracer(val tracer: Tracer) extends OpenTracer {

  def createRootSpan(rootSpanName: String): Span = {
    val span = tracer.buildSpan(rootSpanName).start()
    span.log("hello")
    span
  }

  def createChildSpan(spanName: String, rootSpan: Span): Span = {
    val span = tracer.buildSpan(spanName).asChildOf(rootSpan).start()
    span.log(s"Child span created")
    span
  }
  def closeSpan(span: Span) = span.finish()

  def log(span: Span, msg: String): Span = span.log(msg)
 
  def logError(span: Span, exception: Throwable): Span = log(span, exception.getMessage)
}

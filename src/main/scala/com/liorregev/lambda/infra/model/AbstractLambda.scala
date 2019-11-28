package com.liorregev.lambda.infra.model

import java.io.{InputStream, OutputStream}

import cats.implicits._
import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import com.liorregev.lambda.infra.invoker.LambdaInvoker
import play.api.libs.json.{Format, Json}

abstract class AbstractLambda[Req: Format, Resp: Format, Err: Format]
  extends RequestStreamHandler {

  def handle(req: Req): Either[Err, Resp]

  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    handleRequest(input, output)
  }

  def handleRequest(input: InputStream, output: OutputStream): Unit = {
    val result = Json.parse(input)
      .validate[Req]
      .asEither
      .leftMap(errors => RequestParseError[Resp, Err](errors.toString()))
      .flatMap(req => handle(req).leftMap(InvocationFailure[Resp, Err]))
      .fold(
        identity,
        InvocationSuccess[Resp, Err]
      )
    output.write(Json.toJson(result).toString.getBytes())
  }

  def apply(req: Req)(implicit invoker: LambdaInvoker): LambdaInvocationResult[Resp, Err] = invoker.invoke(this)(req)
}

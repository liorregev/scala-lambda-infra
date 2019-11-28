package com.liorregev.lambda.infra.invoker

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.liorregev.lambda.infra.model.{AbstractLambda, LambdaInvocationResult, ResponseParseError}
import play.api.libs.json.{Format, Json}

class SerializedLocalLambdaInvoker
  extends LambdaInvoker{

  override def invoke[Req: Format, Resp: Format, Err: Format](lambda: AbstractLambda[Req, Resp, Err])(req: Req): LambdaInvocationResult[Resp, Err] = {
    val inputStream = new ByteArrayInputStream(Json.toJson(req).toString().getBytes())
    val outputStream = new ByteArrayOutputStream()
    lambda.handleRequest(inputStream, outputStream)
    Json.parse(outputStream.toString)
      .validate[LambdaInvocationResult[Resp, Err]]
      .asEither
      .fold(
        errors => ResponseParseError[Resp, Err](errors.toString),
        identity
      )
  }
}

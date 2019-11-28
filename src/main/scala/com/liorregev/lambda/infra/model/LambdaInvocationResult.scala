package com.liorregev.lambda.infra.model

import com.liorregev.lambda.infra.serialization._
import play.api.libs.json.{Format, Json, OFormat}

sealed trait LambdaInvocationResult[Success, Fail] extends Product with Serializable
final case class InvocationSuccess[Success: Format, Fail: Format](result: Success)
  extends LambdaInvocationResult[Success, Fail]
final case class InvocationFailure[Success: Format, Fail: Format](result: Fail)
  extends LambdaInvocationResult[Success, Fail]
final case class RequestParseError[Success, Fail](errorString: String)
  extends LambdaInvocationResult[Success, Fail]
final case class ResponseParseError[Success, Fail](errorString: String)
  extends LambdaInvocationResult[Success, Fail]


object LambdaInvocationResult {
  implicit def format[Success: Format, Fail: Format]: OFormat[LambdaInvocationResult[Success, Fail]] = formatFor(
    "parseError" -> Json.format[RequestParseError[Success, Fail]],
    "success" -> Json.format[InvocationSuccess[Success, Fail]],
    "failure" -> Json.format[InvocationFailure[Success, Fail]]
  )
}

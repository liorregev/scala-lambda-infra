package com.liorregev.lambda.echo

import play.api.libs.json.{Json, OFormat}

sealed trait EchoResponse
final case class EchoSuccess(data: String) extends EchoResponse
final case class EchoFailure(error: String) extends EchoResponse

object EchoResponse {
  implicit val successFormat: OFormat[EchoSuccess] = Json.format
  implicit val failureFormat: OFormat[EchoFailure] = Json.format
}
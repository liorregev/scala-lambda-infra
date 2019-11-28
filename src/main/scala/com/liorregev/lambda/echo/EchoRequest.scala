package com.liorregev.lambda.echo

import play.api.libs.json.{Json, OFormat}

final case class EchoRequest(data: String)

object EchoRequest {
  implicit val format: OFormat[EchoRequest] = Json.format
}
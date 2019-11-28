package com.liorregev.lambda.infra.serialization

import play.api.libs.json._

trait SerializableADT[T] {
  def reads(fieldName: String): PartialFunction[JsValue, JsResult[T]]
  def writes(fieldName: String): PartialFunction[T, JsObject]
}
package com.liorregev.lambda.echo

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import org.scalatest.{EitherValues, FunSuite, Matchers}
import cats.implicits._
import com.liorregev.lambda.infra.invoker.SerializedLocalLambdaInvoker
import com.liorregev.lambda.infra.model.{InvocationFailure, InvocationSuccess, LambdaInvocationResult}
import play.api.libs.json.Json

class EchoLambdaTest extends FunSuite with Matchers with EitherValues {
  private val lambda = new EchoLambda()

  test("Success using handle") {
    val req = EchoRequest("data")
    val resp = lambda.handle(req)
    resp should be (EchoSuccess(req.data).asRight[EchoFailure])
  }

  test("Failure using handle") {
    val req = EchoRequest(EchoLambda.failureTrigger)
    val resp = lambda.handle(req)
    resp should be (EchoFailure(EchoLambda.failureResponse).asLeft[EchoSuccess])
  }

  test("Success using handleRequest") {
    val req = EchoRequest("data")
    val inputStream = new ByteArrayInputStream(Json.toJson(req).toString().getBytes())
    val outputStream = new ByteArrayOutputStream()
    lambda.handleRequest(inputStream, outputStream)
    val result = Json.parse(outputStream.toString).as[LambdaInvocationResult[EchoSuccess, EchoFailure]]
    result should be (InvocationSuccess[EchoSuccess, EchoFailure](EchoSuccess(req.data)))
  }

  test("Failure using handleRequest") {
    val req = EchoRequest(EchoLambda.failureTrigger)
    val inputStream = new ByteArrayInputStream(Json.toJson(req).toString().getBytes())
    val outputStream = new ByteArrayOutputStream(100)
    lambda.handleRequest(inputStream, outputStream)
    val result = Json.parse(outputStream.toString).as[LambdaInvocationResult[EchoSuccess, EchoFailure]]
    result should be (InvocationFailure[EchoSuccess, EchoFailure](EchoFailure(EchoLambda.failureResponse)))
  }

  test("Success using SerializedLocalLambdaInvoker") {
    implicit val invoker: SerializedLocalLambdaInvoker = new SerializedLocalLambdaInvoker()
    val req = EchoRequest("data")
    val result = lambda(req)
    result should be (InvocationSuccess[EchoSuccess, EchoFailure](EchoSuccess(req.data)))
  }

  test("Failure using SerializedLocalLambdaInvoker") {
    implicit val invoker: SerializedLocalLambdaInvoker = new SerializedLocalLambdaInvoker()
    val req = EchoRequest(EchoLambda.failureTrigger)
    val result = lambda(req)
    result should be (InvocationFailure[EchoSuccess, EchoFailure](EchoFailure(EchoLambda.failureResponse)))
  }
}

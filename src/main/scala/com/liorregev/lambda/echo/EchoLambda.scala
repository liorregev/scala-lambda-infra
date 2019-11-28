package com.liorregev.lambda.echo

import cats.implicits._
import com.liorregev.lambda.infra.model.AbstractLambda

class EchoLambda() extends AbstractLambda[EchoRequest, EchoSuccess, EchoFailure]{

  override def handle(req: EchoRequest): Either[EchoFailure, EchoSuccess] = {
    if(req.data == EchoLambda.failureTrigger) {
      EchoFailure(EchoLambda.failureResponse).asLeft[EchoSuccess]
    } else {
      EchoSuccess(req.data).asRight[EchoFailure]
    }
  }
}

object EchoLambda {
  val failureTrigger: String = "this should fail"
  val failureResponse: String = "Echo failed"
}
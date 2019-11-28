package com.liorregev.lambda.infra.invoker

import com.liorregev.lambda.infra.model.{AbstractLambda, LambdaInvocationResult}
import play.api.libs.json.Format

abstract class LambdaInvoker {
  def invoke[Req: Format, Resp: Format, Err: Format](lambda: AbstractLambda[Req, Resp, Err])(req: Req): LambdaInvocationResult[Resp, Err]
}

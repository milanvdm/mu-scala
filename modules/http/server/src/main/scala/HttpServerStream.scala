/*
 * Copyright 2017-2018 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package freestyle.rpc.http
package server

import cats.effect.Effect
import fs2.{Stream, StreamApp}
import monix.execution.Scheduler
import org.http4s.HttpService

object HttpServerStream {

  def apply[F[_]: Effect](service: HttpService[F], prefix: String = "/")(
      implicit C: HttpConfig,
      S: Scheduler): Stream[F, StreamApp.ExitCode] = {
    val httpServerBuilder: HttpServerBuilder[F] = new HttpServerBuilder[F]

    httpServerBuilder.build(service, prefix).serve
  }

}
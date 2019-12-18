/*
 * Copyright 2017-2019 47 Degrees, LLC. <http://www.47deg.com>
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

package higherkindness.mu.rpc.healthcheck.unary.handler

import pbdirect._

final case class HealthCheck(@pbIndex(1) nameService: String)
final case class ServerStatus(@pbIndex(1) status: String)
final case class HealthStatus(@pbIndex(1) hc: HealthCheck, @pbIndex(2) status: ServerStatus)
final case class AllStatus(@pbIndex(2) all: List[HealthStatus])

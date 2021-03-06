/*
 * Copyright 2017-2020 47 Degrees, LLC. <http://www.47deg.com>
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

package higherkindness.mu.rpc.protocol

import cats.Applicative
import cats.syntax.applicative._
import higherkindness.mu.rpc.common._
import higherkindness.mu.rpc.protocol.Utils._
import org.scalacheck.Prop._
import org.scalatest._
import org.scalatestplus.scalacheck.Checkers

class RPCMethodNameTests extends RpcBaseTestSuite with BeforeAndAfterAll with Checkers {

  object RPCService {

    case class Request(s: String)

    case class Response(length: Int)

    @service(Protobuf, methodNameStyle = Capitalize) trait ProtoRPCServiceDef[F[_]] {
      def proto(req: Request): F[Response]
    }
    @service(Avro, methodNameStyle = Capitalize) trait AvroRPCServiceDef[F[_]] {
      def avro(req: Request): F[Response]
    }
    @service(AvroWithSchema, methodNameStyle = Capitalize) trait AvroWithSchemaRPCServiceDef[F[_]] {
      def avroWithSchema(req: Request): F[Response]
    }

    class RPCServiceDefImpl[F[_]: Applicative]
        extends ProtoRPCServiceDef[F]
        with AvroRPCServiceDef[F]
        with AvroWithSchemaRPCServiceDef[F] {

      def proto(bd: Request): F[Response]          = Response(bd.s.length).pure
      def avro(bd: Request): F[Response]           = Response(bd.s.length).pure
      def avroWithSchema(bd: Request): F[Response] = Response(bd.s.length).pure
    }

  }

  "A RPC server" should {

    import RPCService._
    import higherkindness.mu.rpc.TestsImplicits._

    implicit val H: RPCServiceDefImpl[ConcurrentMonad] = new RPCServiceDefImpl[ConcurrentMonad]

    "be able to call a service with a capitalized method using proto" in {

      withClient(
        ProtoRPCServiceDef.bindService[ConcurrentMonad],
        ProtoRPCServiceDef.clientFromChannel[ConcurrentMonad](_)) { client =>
        check {
          forAll { s: String =>
            client.proto(Request(s)).map(_.length).unsafeRunSync() == s.length
          }
        }
      }

    }

    "be able to call a service with a capitalized method using avro" in {

      withClient(
        AvroRPCServiceDef.bindService[ConcurrentMonad],
        AvroRPCServiceDef.clientFromChannel[ConcurrentMonad](_)) { client =>
        check {
          forAll { s: String =>
            client.avro(Request(s)).map(_.length).unsafeRunSync() == s.length
          }
        }
      }

    }

    "be able to call a service with a capitalized method using avro with schema" in {

      withClient(
        AvroWithSchemaRPCServiceDef.bindService[ConcurrentMonad],
        AvroWithSchemaRPCServiceDef.clientFromChannel[ConcurrentMonad](_)) { client =>
        check {
          forAll { s: String =>
            client.avroWithSchema(Request(s)).map(_.length).unsafeRunSync() == s.length
          }
        }
      }

    }
  }
}

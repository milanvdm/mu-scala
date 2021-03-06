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

package higherkindness.mu.rpc
package internal
package util

import java.time.{Duration, ZoneOffset, ZonedDateTime}

import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
import com.fortysevendeg.scalacheck.datetime.GenDateTime._
import org.scalatest._
import org.scalacheck.Prop._
import org.scalatestplus.scalacheck.Checkers

class JavaTimeUtilTests extends WordSpec with Matchers with Checkers {

  val from: ZonedDateTime = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
  val range: Duration     = Duration.ofDays(365 * 200)

  "JavaTimeUtil" should {

    "allow to convert LocalDate to and from int" in {
      import com.fortysevendeg.scalacheck.datetime.jdk8.granularity.days
      check {
        forAll(genDateTimeWithinRange(from, range)) { zdt: ZonedDateTime =>
          val date  = zdt.toLocalDate
          val value = JavaTimeUtil.localDateToInt(date)
          JavaTimeUtil.intToLocalDate(value) == date
        }
      }
    }

    "allow to convert LocalDateTime to and from long" in {
      import com.fortysevendeg.scalacheck.datetime.jdk8.granularity.seconds
      check {
        forAll(genDateTimeWithinRange(from, range)) { zdt: ZonedDateTime =>
          val date  = zdt.toLocalDateTime
          val value = JavaTimeUtil.localDateTimeToLong(date)
          JavaTimeUtil.longToLocalDateTime(value) == date
        }
      }
    }

    "allow to convert Instant to and from long" in {
      import com.fortysevendeg.scalacheck.datetime.jdk8.granularity.seconds
      check {
        forAll(genDateTimeWithinRange(from, range)) { zdt: ZonedDateTime =>
          val date  = zdt.toInstant
          val value = JavaTimeUtil.instantToLong(date)
          JavaTimeUtil.longToInstant(value) == date
        }
      }
    }
  }

}

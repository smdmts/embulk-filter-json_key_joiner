package org.embulk.filter.json_key_joiner.json

import org.scalatest.Matchers

class JsonParserSpec extends org.scalatest.FlatSpec with Matchers {

  it should "be parse" in {
    val appender = Map("abc" -> "efg")
    val before = """
                   |{
                   |  "name" : "John",
                   |  "age" : 50
                   |}
                 """.stripMargin
    val after = JsonParser(before, appender).noSpaces

    after shouldBe """{"name":"John","age":50,"abc":"efg"}"""

  }

}

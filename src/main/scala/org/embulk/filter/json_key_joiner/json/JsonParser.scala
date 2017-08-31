package org.embulk.filter.json_key_joiner.json

import io.circe._
import io.circe.parser._

object JsonParser {
  def apply(json: String, joiner: Map[String, String]): Json = {
    decode[Map[String, Json]](json) match {
      case Right(v: Map[String, Json]) =>
        Json.fromFields(v ++ joiner.mapValues(Json.fromString))
      case _ =>
        sys.error(s"could not parse json. $json")
    }
  }
}

package org.embulk.filter.json_key_joiner.row

import org.embulk.spi.{
  Column,
  PageBuilder,
  PageReader,
  ColumnVisitor => EmbulkColumnVisitor
}
import org.embulk.filter.json_key_joiner.json.JsonParser

case class SetValueColumnVisitor(reader: PageReader,
                                 columnName: String,
                                 joiner: Map[String, String])
    extends EmbulkColumnVisitor {
  import scala.collection.mutable
  private val valueHolderSet = mutable.Set[ValueHolder[_]]()

  val spiParser = new org.embulk.spi.json.JsonParser()

  override def timestampColumn(column: Column): Unit =
    value(column, reader.getTimestamp)

  override def stringColumn(column: Column): Unit =
    value(column, reader.getString)

  override def longColumn(column: Column): Unit =
    value(column, reader.getLong)

  override def doubleColumn(column: Column): Unit =
    value(column, reader.getDouble)

  override def booleanColumn(column: Column): Unit =
    value(column, reader.getBoolean)

  val jsonParser = new org.embulk.spi.json.JsonParser()
  override def jsonColumn(column: Column): Unit = {
    if (column.getName == columnName) {
      val json = JsonParser(reader.getJson(column).toJson, joiner)
      valueHolderSet.add(
        ValueHolder(column, Some(jsonParser.parse(json.noSpaces))))
    } else {
      value(column, reader.getJson)
    }
  }

  def value[A](column: Column, method: => (Column => A)): Option[A] = {
    val result = if (reader.isNull(column)) {
      None
    } else {
      Some(method(column))
    }
    valueHolderSet.add(ValueHolder(column, result))
    result
  }

  def getRow(pageBuilder: PageBuilder): Row =
    Row(valueHolderSet, pageBuilder)

}

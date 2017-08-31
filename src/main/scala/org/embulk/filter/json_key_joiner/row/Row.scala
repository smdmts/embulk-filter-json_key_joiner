package org.embulk.filter.json_key_joiner.row

import org.embulk.spi.PageBuilder
import org.embulk.spi.`type`._
import org.embulk.spi.time.Timestamp
import org.msgpack.value.Value

import scala.collection.mutable

case class Row(seq: mutable.Set[ValueHolder[_]], pageBuilder: PageBuilder) {
  def addRecord(): Unit = {
    seq.foreach { vh =>
      vh.value match {
        case Some(v: Boolean) if vh.column.getType.isInstanceOf[BooleanType] =>
          pageBuilder.setBoolean(vh.column, v)
        case Some(v: Long) if vh.column.getType.isInstanceOf[LongType] =>
          pageBuilder.setLong(vh.column, v)
        case Some(v: Double) if vh.column.getType.isInstanceOf[DoubleType] =>
          pageBuilder.setDouble(vh.column, v)
        case Some(v: String) if vh.column.getType.isInstanceOf[StringType] =>
          pageBuilder.setString(vh.column, v)
        case Some(v: Timestamp)
            if vh.column.getType.isInstanceOf[TimestampType] =>
          pageBuilder.setTimestamp(vh.column, v)
        case Some(v: Value) if vh.column.getType.isInstanceOf[JsonType] =>
          pageBuilder.setJson(vh.column, v)
        case None =>
          pageBuilder.setNull(vh.column)
        case _ =>
          sys.error("unmatched types.")
      }
    }
    pageBuilder.addRecord()
  }

}

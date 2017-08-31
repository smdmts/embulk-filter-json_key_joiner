package org.embulk.filter.json_key_joiner

import org.embulk.filter.json_key_joiner.row.SetValueColumnVisitor

import scala.collection.JavaConverters._
import org.embulk.spi.{
  Exec,
  Page,
  PageBuilder,
  PageReader,
  Schema,
  PageOutput => EmbulkPageOutput
}

case class PageOutput(task: PluginTask,
                      schema: Schema,
                      output: EmbulkPageOutput)
    extends EmbulkPageOutput {
  val pageBuilder = new PageBuilder(Exec.getBufferAllocator, schema, output)
  override def add(page: Page): Unit = {
    val baseReader: PageReader = new PageReader(schema)
    baseReader.setPage(page)
    while (baseReader.nextRecord()) {
      val visitor = SetValueColumnVisitor(
        baseReader,
        task.getJsonColumnName,
        task.getKeyWithIndex.asScala.toMap
      )
      schema.visitColumns(visitor)
      visitor.getRow(pageBuilder).addRecord()
    }
    baseReader.close()
  }

  override def finish(): Unit = pageBuilder.finish()

  override def close(): Unit = pageBuilder.close()
}

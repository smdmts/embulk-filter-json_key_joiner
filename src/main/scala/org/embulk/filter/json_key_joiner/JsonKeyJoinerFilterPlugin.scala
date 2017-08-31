package org.embulk.filter.json_key_joiner

import org.embulk.config.{ConfigSource, TaskSource}
import org.embulk.spi
import org.embulk.spi.{FilterPlugin, Schema}

class JsonKeyJoinerFilterPlugin extends FilterPlugin {

  override def transaction(config: ConfigSource,
                           inputSchema: Schema,
                           control: FilterPlugin.Control): Unit = {
    val task = config.loadConfig(classOf[PluginTask])
    val taskSource = task.dump()
    control.run(taskSource, inputSchema)
  }

  override def open(taskSource: TaskSource,
                    inputSchema: Schema,
                    outputSchema: Schema,
                    output: spi.PageOutput): PageOutput = {
    val task = taskSource.loadTask(classOf[PluginTask])
    PageOutput(task, outputSchema, output)
  }

}

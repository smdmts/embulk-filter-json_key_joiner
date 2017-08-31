package org.embulk.filter.json_key_joiner

import org.embulk.config.{Config, ConfigDefault, Task}

trait PluginTask extends Task {

  @Config("key_value")
  @ConfigDefault("{}")
  def getKeyWithIndex: java.util.Map[String, String]

}

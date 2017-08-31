package org.embulk.filter.json_key_joiner.row

import org.embulk.spi.Column

case class ValueHolder[A](column: Column, value: Option[A])

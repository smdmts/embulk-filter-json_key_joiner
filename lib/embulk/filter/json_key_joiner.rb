Embulk::JavaPlugin.register_filter(
  "json_key_joiner", "org.embulk.filter.json_key_joiner.JsonKeyJoinerFilterPlugin",
  File.expand_path('../../../../classpath', __FILE__))

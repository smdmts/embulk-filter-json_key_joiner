# Json Key Joiner filter plugin for Embulk

Join to json default key-value to defined JSON Type column.

* **Plugin type**: filter

## Configuration

- **key_value**: joining key value (hash Map[String,String], required)
- **json_column_name**: processing json column (string, required)

## Example

```yaml

filters:
  - type: json_key_joiner
    json_column_name: record # JSON Type Column
    key_value:
      append_key: append_value
```

- before json
```
{ "key1":"value1" }
```

- after json
```
{ "key1":"value1" , "append_key":"append_value" }
```


## Build

```
$ ./gradlew gem 
```

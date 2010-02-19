package fm.last.mucene.schema;

import java.util.Map;

public class Schema {
  Map<String, FieldDescriptor> fields;

  public FieldDescriptor get(String name) {
    return fields.get(name);
  }
}

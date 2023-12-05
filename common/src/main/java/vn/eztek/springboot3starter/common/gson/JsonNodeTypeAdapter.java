package vn.eztek.springboot3starter.common.gson;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class JsonNodeTypeAdapter extends TypeAdapter<JsonNode> {

  private final Gson gson;

  public JsonNodeTypeAdapter() {
    gson = new Gson();
  }

  @Override
  public void write(JsonWriter out, JsonNode value) throws IOException {
    String jsonString = gson.toJson(value);
    out.jsonValue(jsonString);
  }

  @Override
  public JsonNode read(JsonReader in) throws IOException {
    JsonElement jsonElement = gson.fromJson(in, JsonElement.class);
    return new Gson().fromJson(jsonElement, JsonNode.class);
  }
}

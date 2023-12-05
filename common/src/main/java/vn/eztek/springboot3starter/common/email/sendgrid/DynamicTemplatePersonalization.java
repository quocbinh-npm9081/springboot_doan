package vn.eztek.springboot3starter.common.email.sendgrid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DynamicTemplatePersonalization extends Personalization {

  @JsonProperty(value = "dynamic_template_data")
  private Map<String, Object> dynamic_template_data;

  @JsonProperty("dynamic_template_data")
  @Override
  public Map<String, Object> getDynamicTemplateData() {
    if (dynamic_template_data == null) {
      return Collections.emptyMap();
    }
    return dynamic_template_data;
  }

  public void setDynamicTemplateData(Map<String, String> dynamicTemplateData) {
    if (dynamicTemplateData == null) {
      dynamic_template_data = Collections.emptyMap();
    } else {
      dynamic_template_data = new HashMap<>();
      dynamic_template_data.putAll(dynamicTemplateData);
    }
  }

}


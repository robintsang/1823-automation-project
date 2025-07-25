package hk1823.automation.Utility.robin.playwright;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplaintData {
    @JsonProperty("????")
    public String timestamp;
    public String name;
    public String email;
    public String phone;
    public String category;
    public String sub_category_1;
    @com.fasterxml.jackson.annotation.JsonProperty("sub_category_1.2")
    public String sub_category_1_2;
    public String sub_category_1_other__detail;
    public String sub_category_1_2_other__detail;
    public String description;
    public String location;
    public String attachment;
} 
package academy.teenfuture.crse.api;

import lombok.Data;

@Data
public class Demo {
  long id = 5;
  boolean active = true;
  String name = "my name";
  String abc = "this is abc";

  public Demo() {
  }

  public Demo(long id) {
    this.id = id;
  }

}
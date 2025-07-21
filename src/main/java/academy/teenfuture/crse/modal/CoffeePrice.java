package academy.teenfuture.crse.modal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "coffeePrice")
@Data
public class CoffeePrice {
  @Id
  public String coffeeName;

  @Column(nullable = false)
  public String coffeePrice;

}

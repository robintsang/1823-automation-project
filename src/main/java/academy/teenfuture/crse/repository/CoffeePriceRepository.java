package academy.teenfuture.crse.repository;

import academy.teenfuture.crse.modal.CoffeePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CoffeePriceRepository extends JpaRepository<CoffeePrice, String> {
  CoffeePrice findByCoffeeName(String coffeeName);
}
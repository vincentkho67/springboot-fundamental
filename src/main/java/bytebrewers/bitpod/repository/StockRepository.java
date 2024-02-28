package bytebrewers.bitpod.repository;

import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockRepository extends JpaRepository<Stock, String>, JpaSpecificationExecutor<Stock> {
}

package bytebrewers.bitpod.repository;

import bytebrewers.bitpod.entity.Bank;
import bytebrewers.bitpod.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PortfolioRepository extends JpaRepository<Portfolio, String>, JpaSpecificationExecutor<Portfolio> {
}

package bytebrewers.bitpod.repository;

import bytebrewers.bitpod.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BankRepository extends JpaRepository<Bank, Integer>, JpaSpecificationExecutor<Bank> {
}

package bytebrewers.bitpod.repository;

import bytebrewers.bitpod.entity.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BankRepositoryTest {


    @Autowired
    private BankRepository bankRepository;

    private Bank bank;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        bank.setName("BNI");
        bank.setAddress("Jl. Jendral Sudirman No. 1");
    }

    @Test
    public void givenBankObject_whenCreateBank_thenReturnSavedBank() {
        bankRepository.save(bank);
        assertNotNull(bankRepository);
    }

    @Test
    public void givenInvalidBankObject_whenCreateBank_thenReturnNull() {
        bank.setName(null);
        assertThrows(DataIntegrityViolationException.class, () -> bankRepository.save(bank));
    }


    @Test
    public void givenBankObject_whenGetBank_thenReturnBank() {
        bankRepository.save(bank);
        assertNotNull(bankRepository.findById(bank.getId()));
    }


    @Test
    public void givenBankObject_whenUpdateBank_thenReturnUpdatedBank() {
        bankRepository.save(bank);
        bank.setName("BCA");
        bankRepository.save(bank);
        assertNotNull(bankRepository.findById(bank.getId()));
    }


    @Test
    public void givenBankObject_whenDeleteBank_thenReturnDeletedBank() {
        bankRepository.save(bank);
        bankRepository.delete(bank);
        assertNotNull(bankRepository.findById(bank.getId()));
    }


}

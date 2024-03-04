package bytebrewers.bitpod.repository;

import bytebrewers.bitpod.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setLot(1000);
        transaction.setPrice(100.0);
    }


    @Test
    public void givenTransactionObject_whenCreateTransaction_thenReturnSavedBank() {
        transactionRepository.save(transaction);
        assertNotNull(transaction);
    }

    @Test
    public void givenTransactionObject_whenGetTransaction_thenReturnTransaction() {
        transactionRepository.save(transaction);
        assertNotNull(transactionRepository.findById(transaction.getId()));
    }


    @Test
    public void givenTransactionObject_whenUpdateTransaction_thenReturnUpdatedTransaction() {
        transactionRepository.save(transaction);
        transaction.setPrice(200.0);
        transactionRepository.save(transaction);
        assertNotNull(transactionRepository.findById(transaction.getId()));
    }


    @Test
    public void givenTransactionObject_whenDeleteTransaction_thenReturnDeletedTransaction() {
        transactionRepository.save(transaction);
        transactionRepository.delete(transaction);
        assertNotNull(transactionRepository.findById(transaction.getId()));
    }


}
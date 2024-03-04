package bytebrewers.bitpod.repository;

import bytebrewers.bitpod.entity.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class StockRepositoryTest {
    @Autowired
    private StockRepository stockRepository;
    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        stock.setName("SBIN");
        stock.setCompany("SBI");
        stock.setPrice(100.0);
        stock.setLot(1000);
    }

    @Test
    void givenStockObject_whenSaveStock_thenReturnSavedStock() {
        stockRepository.save(stock);
        assertNotNull(stock);
    }

    @Test
    void givenStockObject_whenGetStock_thenReturnStock() {
        stockRepository.save(stock);
        assertNotNull(stockRepository.findById(stock.getId()));
    }

    @Test
    void givenStockObject_whenUpdateStock_thenReturnUpdatedStock() {
        stockRepository.save(stock);
        stock.setPrice(200.0);
        stockRepository.save(stock);
        assertNotNull(stockRepository.findById(stock.getId()));
    }
    @Test
    void givenStockObject_whenDeleteStock_thenReturnDeletedStock() {
        stockRepository.save(stock);
        stockRepository.delete(stock);
        assertNotNull(stockRepository.findById(stock.getId()));
    }


}
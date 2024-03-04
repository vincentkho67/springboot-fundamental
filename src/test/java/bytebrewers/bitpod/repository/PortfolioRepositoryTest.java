package bytebrewers.bitpod.repository;

import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PortfolioRepositoryTest {

    @Autowired
    private PortfolioRepository portfolioRepository;
    private Portfolio portfolio;


    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();
        portfolio.setAvgBuy(BigDecimal.valueOf(1000.0));
        portfolio.setReturns("10%");
        User user = new User();
        user.setUsername("abdul");
        portfolio.setUser(user);
    }

    @Test
    public void givenPortfolioObject_whenCreatePortfolio_thenReturnSavedPortfolio() {
        portfolioRepository.save(portfolio);
        assertNotNull(portfolioRepository.findById(portfolio.getId()));
    }

    @Test
    public void givenPortfolioObject_whenGetPortfolio_thenReturnPortfolio() {
        portfolioRepository.save(portfolio);
        assertNotNull(portfolioRepository.findById(portfolio.getId()));
    }


    @Test
    public void givenPortfolioObject_whenUpdatePortfolio_thenReturnUpdatedPortfolio() {
        portfolioRepository.save(portfolio);
        portfolio.setAvgBuy(BigDecimal.valueOf(100000));
        portfolioRepository.save(portfolio);
        assertNotNull(portfolioRepository.findById(portfolio.getId()));
    }


    @Test
    public void givenPortfolioObject_whenDeletePortfolio_thenReturnDeletedPortfolio() {
        portfolioRepository.save(portfolio);
        portfolioRepository.delete(portfolio);
        assertNotNull(portfolioRepository.findById(portfolio.getId()));
    }




}
package bytebrewers.bitpod;
import bytebrewers.bitpod.controller.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AuthControllerTest.class,
        BankControllerTest.class,
        PortfolioControllerTest.class,
        TransactionControllerTest.class,
        StockControllerTest.class
})
public class TestSuite {

}

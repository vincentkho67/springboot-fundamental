package bytebrewers.bitpod;
import bytebrewers.bitpod.controller.*;
import bytebrewers.bitpod.service.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AuthControllerTest.class,
        BankControllerTest.class,
        PortfolioControllerTest.class,
        TransactionControllerTest.class,
        StockControllerTest.class,
        UserControllerTest.class,
        BankServiceTest.class,
        AuthServiceTest.class,
        PortfolioServiceTest.class,
        RoleServiceTest.class,
        StockServiceTest.class,
        TransactionServiceTest.class,
        UserServiceTest.class,

})
public class TestSuite {

}

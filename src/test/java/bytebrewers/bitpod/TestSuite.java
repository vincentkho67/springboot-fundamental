package bytebrewers.bitpod;
import bytebrewers.bitpod.controller.*;
import bytebrewers.bitpod.service.impl.AuthServiceImplTest;
import bytebrewers.bitpod.service.impl.RoleServiceImplTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AuthControllerTest.class,
        BankControllerTest.class,
        PortfolioControllerTest.class,
        TransactionControllerTest.class,
        StockControllerTest.class,
        RoleServiceImplTest.class,
        AuthServiceImplTest.class
})
public class TestSuite {

}

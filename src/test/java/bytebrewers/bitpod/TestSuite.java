package bytebrewers.bitpod;
import bytebrewers.bitpod.controller.AuthControllerTest;
import bytebrewers.bitpod.controller.BankControllerTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({AuthControllerTest.class, BankControllerTest.class})
public class TestSuite {

}

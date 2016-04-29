package at.tuwien.ase.test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Log the currently running test.
 * 
 * <p>Typical usage:
 * 
 * <p>{@code @Rule public LogTestName logTestName = new LogTestName();}
 *
 * <p>See also:
 * <br>{@link org.junit.Rule}
 * <br>{@link org.junit.rules.TestWatcher}
 */
public class LogTestName extends TestWatcher {


    @Override
    protected void starting( Description description ) {
       System.out.println(  description.getMethodName() );
    }

}
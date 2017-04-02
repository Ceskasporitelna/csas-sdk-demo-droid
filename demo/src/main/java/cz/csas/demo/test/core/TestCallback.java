package cz.csas.demo.test.core;

/**
 * The interface Test callback.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09 /06/16.
 */
public interface TestCallback {
    /**
     * Result.
     *
     * @param testResult the test result
     */
    void result(TestResult testResult);
}

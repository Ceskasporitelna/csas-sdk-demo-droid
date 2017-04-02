package cz.csas.demo.test.core;

/**
 * The type Test result.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09 /06/16.
 */
public class TestResult {

    private TestStatus status;
    private String result;

    /**
     * Instantiates a new Test result.
     */
    public TestResult() {
        status = TestStatus.UNKNOWN;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public TestStatus getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(TestStatus status) {
        this.status = status;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * Sets result.
     *
     * @param result the result
     */
    public void setResult(String result) {
        this.result = result;
    }
}

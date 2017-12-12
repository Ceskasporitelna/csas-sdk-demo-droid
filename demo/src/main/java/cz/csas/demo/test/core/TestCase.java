package cz.csas.demo.test.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.Environment;
import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.WebApiConfigurationImpl;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.JudgeClient;
import cz.csas.netbanking.Netbanking;
import cz.csas.netbanking.NetbankingClient;

/**
 * The type Test case.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09 /06/16.
 */
public abstract class TestCase {

    private final String TEST_BASE_URL_OAUTH = "http://csas-judge.herokuapp.com/widp/oauth2";
    private final String TEST_BASE_URL = "http://csas-judge.herokuapp.com/webapi";
    private final String JUDGE_BASE_URL = "http://csas-judge.herokuapp.com";
    private final String WEB_API_KEY_TEST = "TEST_API_KEY";

    /**
     * The M x judge session header.
     */
    protected String mXJudgeSessionHeader;
    /**
     * The M web api configuration.
     */
    protected WebApiConfiguration mWebApiConfiguration;
    /**
     * The M judge client.
     */
    protected JudgeClient mJudgeClient;
    /**
     * The M netbanking client.
     */
    protected NetbankingClient mNetbankingJudgeClient;
    /**
     * The M netbanking client.
     */
    protected NetbankingClient mNetbankingClient;
    /**
     * The M test result.
     */
    protected TestResult mTestResult;
    /**
     * The M test callback.
     */
    protected TestCallback mTestCallback;

    /**
     * Instantiates a new Test case.
     */
    public TestCase() {
        setUp();
    }

    /**
     * Sets up.
     */
    public void setUp() {
        mWebApiConfiguration = new WebApiConfigurationImpl(WEB_API_KEY_TEST, new Environment(TEST_BASE_URL, TEST_BASE_URL_OAUTH, false), "cs-CZ", null);
        mXJudgeSessionHeader = UUID.randomUUID().toString();
        mJudgeClient = new JudgeClient(JUDGE_BASE_URL, CoreSDK.getInstance().getLogger());
        mNetbankingJudgeClient = new NetbankingClient(mWebApiConfiguration);
        mNetbankingClient = Netbanking.getInstance().getNetbankingClient();
        mTestResult = new TestResult();
    }

    /**
     * Run.
     *
     * @param callback the callback
     */
    public abstract void run(TestCallback callback);

    /**
     * Gets name.
     *
     * @return the name
     */
    public abstract String getName();

    /**
     * Gets test result.
     *
     * @return the test result
     */
    public TestResult getTestResult() {
        return mTestResult;
    }

    /**
     * Assert equals boolean.
     *
     * @param o1 the o 1
     * @param o2 the o 2
     * @return the boolean
     */
    public boolean assertEquals(Object o1, Object o2) {
        if (o1.getClass() != o2.getClass()) {
            mTestResult.setResult("Not comparable");
            mTestResult.setStatus(TestStatus.FAILURE);
        } else {
            if (o1 instanceof ArrayList && o2 instanceof ArrayList) {
                for (int i = 0; i < ((ArrayList) o1).size(); i++) {
                    if (!((ArrayList) o2).contains(((ArrayList) o2).get(i))) {
                        mTestResult.setResult("Expected:" + o1.toString() + ", Actual:" + o2.toString());
                        mTestResult.setStatus(TestStatus.FAILURE);
                    }
                }
            } else if (!o1.equals(o2)) {
                mTestResult.setResult("Expected:" + o1.toString() + ", Actual:" + o2.toString());
                mTestResult.setStatus(TestStatus.FAILURE);
            }
        }
        if (mTestResult.getStatus().equals(TestStatus.FAILURE)) {
            mTestCallback.result(mTestResult);
            return false;
        } else {
            mTestResult.setStatus(TestStatus.OK);
            return true;
        }
    }

    /**
     * Assert pdf boolean.
     *
     * @param stream the stream
     * @return the boolean
     * @throws IOException the io exception
     */
    public boolean assertPdf(InputStream stream) throws IOException {
        byte[] expected = "%PDF".getBytes();
        byte[] topOfStream = new byte[4];
        stream.read(topOfStream, 0, 4);
        if (!Arrays.equals(expected, topOfStream)) {
            mTestResult.setResult("Expected:" + expected.length + ", Actual:" + topOfStream.length);
            mTestResult.setStatus(TestStatus.FAILURE);
            mTestCallback.result(mTestResult);
            return false;
        }
        mTestResult.setStatus(TestStatus.OK);
        return true;
    }

    /**
     * Assert png boolean.
     *
     * @param stream the stream
     * @return the boolean
     * @throws IOException the io exception
     */
    public boolean assertPng(InputStream stream) throws IOException {
        byte[] expected = {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};
        byte[] topOfStream = new byte[expected.length];
        stream.read(topOfStream, 0, expected.length);
        if (!Arrays.equals(expected, topOfStream)) {
            mTestResult.setResult("Expected:" + expected.length + ", Actual:" + topOfStream.length);
            mTestResult.setStatus(TestStatus.FAILURE);
            mTestCallback.result(mTestResult);
            return false;
        }
        mTestResult.setStatus(TestStatus.OK);
        return true;
    }

    /**
     * Assert not null boolean.
     *
     * @param o         the o
     * @param fieldName the field name
     * @return the boolean
     */
    public boolean assertNotNull(Object o, String fieldName) {
        if (o == null) {
            mTestResult.setResult(fieldName + " cannot be null.");
            mTestResult.setStatus(TestStatus.FAILURE);
            mTestCallback.result(mTestResult);
            return false;
        }
        mTestResult.setStatus(TestStatus.OK);
        return true;
    }

    /**
     * Assert null boolean.
     *
     * @param o         the o
     * @param fieldName the field name
     * @return the boolean
     */
    public boolean assertNull(Object o, String fieldName) {
        if (o != null) {
            mTestResult.setResult(fieldName + " should be null.");
            mTestResult.setStatus(TestStatus.FAILURE);
            mTestCallback.result(mTestResult);
            return false;
        }
        mTestResult.setStatus(TestStatus.OK);
        return true;
    }

    /**
     * Handle error.
     *
     * @param error the error
     */
    public void handleError(CsSDKError error) {
        mTestResult.setStatus(TestStatus.FAILURE);
        mTestResult.setResult(error.getClass().toString() + ", " + error.getLocalizedMessage());
        mTestCallback.result(mTestResult);
    }
}

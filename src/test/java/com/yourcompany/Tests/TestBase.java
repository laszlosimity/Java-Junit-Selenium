package com.yourcompany.Tests;

import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.junit.ConcurrentParameterized;
import java.net.URL;
import java.util.LinkedList;


@Ignore
@RunWith(ConcurrentParameterized.class)
public class TestBase {


    public static String username = System.getenv("BS_USERNAME");
    public static String accesskey = System.getenv("BS_PASSWORD");
    public static String seleniumURI;
    public static String buildTag;

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    protected String browser;
    protected String os;
    protected String version;
    protected String deviceName;
    protected String deviceOrientation;
    protected String sessionId;
    protected WebDriver driver;

    /**
     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
     * system, version and browser to be used when launching a Sauce VM.  The order of the parameters should be the same
     * as that of the elements within the {@link #browsersStrings()} method.
     * @param os
     * @param version
     * @param browser
     * @param deviceName
     * @param deviceOrientation
     */

    public TestBase(String os, String version, String browser, String deviceName, String deviceOrientation) {
        super();
        this.os = os;
        this.version = version;
        this.browser = browser;
        this.deviceName = deviceName;
        this.deviceOrientation = deviceOrientation;
    }

    /**
     * @return a LinkedList containing String arrays representing the browser combinations the test should be run against. The values
     * in the String array are used as part of the invocation of the test constructor
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() {
        LinkedList browsers = new LinkedList();

        browsers.add(new String[]{"Windows", "67.0", "Chrome", null, null});
        /*browsers.add(new String[]{"Windows 10", "latest", "firefox", null, null});
        browsers.add(new String[]{"Windows 7", "11.0", "internet explorer", null, null});
        browsers.add(new String[]{"OS X 10.11", "10.0", "safari", null, null});
        browsers.add(new String[]{"OS X 10.10", "latest", "chrome", null, null});*/
        return browsers;
    }

    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the {@link #browser},
     * {@link #version} and {@link #os} instance variables, and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link #authentication} instance.
     *
     * @throws Exception if an error occurs during the creation of the {@link RemoteWebDriver} instance.
     */
    @Before
    public void setUp() throws Exception {
        int FIREFOX_MAX_USERPASS_LENGTH = 255;
        final FirefoxProfile profile = new FirefoxProfile();

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("browser", browser);
        capabilities.setCapability("browser_version", version);
        capabilities.setCapability("os", os);
        //capabilities.setCapability(FirefoxDriver.PROFILE, profile);
        //capabilities.setCapability("deviceName", deviceName);
        //capabilities.setCapability("device-orientation", deviceOrientation);

       /* String methodName = name.getMethodName();
        capabilities.setCapability("name", methodName);

        //Getting the build name.
        //Using the Jenkins ENV var. You can use your own. If it is not set test will run without a build id.
        if (buildTag != null) {
            capabilities.setCapability("build", buildTag);
        }*/

       System.out.println("Username is: " + username);

       this.driver = new RemoteWebDriver(
                new URL("https://" + username+ ":" + accesskey + seleniumURI +"/wd/hub"),
                capabilities);

        this.sessionId = (((RemoteWebDriver)driver).getSessionId()).toString();
        System.out.println("It is: " + this.sessionId);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    /**
     * @return the value of the Sauce Job id.
     */
    //@Override
    public String getSessionId() {
        return sessionId;
    }

    @BeforeClass
    public static void setupClass() {
        //get the uri to send the commands to.
        seleniumURI = "@hub-cloud.browserstack.com";
        //If available add build tag. When running under Jenkins BUILD_TAG is automatically set.
        //You can set this manually on manual runs.
        /*buildTag = System.getenv("BUILD_TAG");
        if (buildTag == null) {
            buildTag = System.getenv("SAUCE_BUILD_NAME");
        }*/
    }
}
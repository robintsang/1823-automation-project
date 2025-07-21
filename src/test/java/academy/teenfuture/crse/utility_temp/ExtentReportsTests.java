package academy.teenfuture.crse.utility;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportsTests {

    @Test
    public void extentTest() throws IOException {
        ExtentReports extent = new ExtentReports();
        LocalDateTime currentDateTime = LocalDateTime.now();
        String dateTimeSString = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String path = System.getProperty("user.dir") + "/testresult/index-" + dateTimeSString + ".html";

        // Create a new Extent Spark Reporter and configure it
        ExtentSparkReporter spark = new ExtentSparkReporter(path);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("Automation Testing Report");
        spark.config().setReportName("Extent Report Sample");
        extent.attachReporter(spark);

        // Create a new test case
        ExtentTest test = extent.createTest("Vailied Account Login Test");
        test.pass("Vailid Account Login Test started successfully"); // Log information about the test case
        test.info("Correct PW entered");
        test.pass("Login Test completed successfully");

        // Create another test case
        ExtentTest test1 = extent.createTest("Invalid Account User Login Test"); // Log information about the second
                                                                                 // test
                                                                                 // case
        test1.pass("Invalid Account User Login Test started successfully");
        test1.info("Wrong PW entered");
        test1.fail(MarkupHelper.createLabel("Login Test completed successfully", ExtentColor.RED)); // used to create a
                                                                                                    // custom label

        extent.flush();
        Desktop.getDesktop().browse(new File(path).toURI());
    }
}
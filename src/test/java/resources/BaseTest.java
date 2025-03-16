package resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    protected static ExtentReports extent;

    @BeforeSuite
    public void setupExtent() {
        // Inicializa el reporte Extent
        String path = "./Reporte/Reporte_Kata.html";
        ExtentSparkReporter spark = new ExtentSparkReporter(path);
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @AfterSuite
    public void tearDownExtent() {
        if (extent != null) {
            // Finaliza y guarda el reporte
            extent.flush();
        }
    }
}

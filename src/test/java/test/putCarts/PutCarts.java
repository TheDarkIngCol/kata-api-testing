package test.putCarts;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.GeneralTask;

import java.util.HashMap;
import java.util.Map;

public class PutCarts extends BaseTest {

    private static final String BASE_URL = "https://fakestoreapi.com";
    private Map<String, String> headers = new HashMap<>();
    private ExtentTest test;

    @Test
    public void putByCarts() {
        test = extent.createTest("Prueba: Actualizar un carrito exitosamente");
        String endpoint = "/carts/6";
        headers.put("Content-Type", "application/json");
        String jsonFilePath = "src/main/resources/putCarts.json";

        GeneralTask generalTask = new GeneralTask(BASE_URL, endpoint, "PUT", "", headers, test);
        int statusCode = generalTask.performPostWithJsonFile(
                "putCarts",
                "Actualizar un carrito",
                jsonFilePath
        );

        if (statusCode == 200) {
            System.out.println("La prueba PUT fue exitosa");
        } else {
            System.out.println("La prueba PUT falló. Status code: " + statusCode);
        }
    }
}

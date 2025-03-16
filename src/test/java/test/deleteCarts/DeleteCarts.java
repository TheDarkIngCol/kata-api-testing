package test.deleteCarts;

import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.GeneralTask;

import java.util.HashMap;
import java.util.Map;

public class DeleteCarts extends BaseTest {

    private static final String BASE_URL = "https://fakestoreapi.com";
    private Map<String, String> headers = new HashMap<>();
    private ExtentTest test;

    @Test
    public void deleteCarts() {
        test = extent.createTest("Prueba: Eliminar un usuario");
        String endpoint = "/carts/4";
        headers.put("Content-Type", "application/json");

        GeneralTask generalTask = new GeneralTask(BASE_URL, endpoint, "DELETE", "", headers, test);
        int statusCode = generalTask.executeTest(
                "deleteCarts",
                endpoint,
                200,
                "deleteUser",
                "Eliminar un carrito",
                "La prueba DELETE fue exitosa",
                "La prueba DELETE falló"
        );
    }
}

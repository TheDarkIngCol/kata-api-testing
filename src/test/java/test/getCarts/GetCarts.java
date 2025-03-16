package test.getCarts;

import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.Test;
import resources.BaseTest;
import resources.GeneralTask;
import java.util.HashMap;
import java.util.Map;

public class GetCarts extends BaseTest {

    private static final String BASE_URL = "https://fakestoreapi.com";
    private Map<String, String> headers = new HashMap<>();
    private ExtentTest test;

    @Test
    public void getCartsSuccess() {
        test = extent.createTest("Prueba: Obtener el carrito exitosamente");
        String endpoint = "/carts";
        headers.put("Content-Type", "application/json");

        GeneralTask generalTask = new GeneralTask(BASE_URL, endpoint, "GET", "", headers, test);
        generalTask.executeTest(
                "getCarts",
                endpoint,
                200,
                "getCarts",
                "Obtener los carritos exitosamente",
                "La prueba GET Carts fue exitosa",
                "La prueba Get Carts falló"
        );
    }

    @Test
    public void getCarsFailed() {
        test = extent.createTest("Prueba: Enviar un request con caracteres especiales");
        String endpoint = "/carts/*";
        headers.put("Content-Type", "application/json");

        GeneralTask generalTask = new GeneralTask(BASE_URL, endpoint, "GET", "", headers, test);
        generalTask.executeTest(
                "Enviar un request con caracteres especiales",
                endpoint,
                400,
                "getCartsFailed",
                "Validar el envío de caracteres especiales en los parametros",
                "La prueba GET CartsFailed fue exitosa",
                "La prueba GET Carts falló"
        );
    }
}
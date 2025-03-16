package test.postCarts;

import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.Test;
import resources.BaseTest;
import resources.GeneralTask;

import java.util.HashMap;
import java.util.Map;

public class PostCarts extends BaseTest {

    private static final String BASE_URL = "https://fakestoreapi.com";
    private Map<String, String> headers = new HashMap<>();
    private ExtentTest test;

    @Test
    public void postCarts() {
        test = extent.createTest("Prueba: Crear un carrito exitosamente");
        String endpoint = "/carts";
        headers.put("Content-Type", "application/json");
        String jsonFilePath = "src/main/resources/postCarts.json";

        GeneralTask generalTask = new GeneralTask(BASE_URL, endpoint, "POST", "", headers, test);
        int statusCode = generalTask.performPostWithJsonFile(
                "postCarts",
                "Crear un carrito",
                jsonFilePath
        );

        if (statusCode == 200) {
            System.out.println("La prueba POST fue exitosa");
        } else {
            System.out.println("La prueba POST falló. Status code: " + statusCode);
        }
    }

    @Test
    public void postCartsFailed() {
        test = extent.createTest("Prueba: Enviar un endpoint distinto");
        String endpoint = "/cartss";
        headers.put("Content-Type", "application/json");
        String jsonFilePath = "src/main/resources/postCartsFailed.json";

        GeneralTask generalTask = new GeneralTask(BASE_URL, endpoint, "POST", "", headers, test);
        int statusCode = generalTask.performPostWithJsonFile(
                "postCartsFailed",
                "Enviar un endpoint distinto",
                jsonFilePath
        );

        if (statusCode == 404) {
            System.out.println("La prueba POST CartsFailed fue exitosa");
        } else {
            System.out.println("La prueba POST CartsFailed falló. Status code: " + statusCode);
        }
    }
}

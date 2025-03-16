package resources;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static io.restassured.RestAssured.given;

public class GeneralTask {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private final String baseUrl;
    private final String endPoint;
    private final String method;
    private final String parameters;
    private final Map<String, String> headers;
    private final Map<String, Object> body = new HashMap<>();

    private String responseService;
    private Response response;
    private final ExtentTest test; // Objeto ExtentTest

    public GeneralTask(
            String baseUrl,
            String endPoint,
            String method,
            String parameters,
            Map<String, String> headers,
            ExtentTest test) { // Recibe el objeto ExtentTest
        this.baseUrl = baseUrl;
        this.endPoint = endPoint;
        this.method = method;
        this.parameters = parameters;
        this.headers = headers;
        this.test = test; // Inicializa el objeto ExtentTest
    }

    public String getResponseService() {
        return responseService;
    }

    // Método genérico para realizar cualquier tipo de solicitud HTTP
    private int performRequest(String nameTest, String description, String jsonBody) {
        // Registrar el nombre y la descripción del test en el reporte
        test.log(Status.INFO, "Ejecutando: " + nameTest + " - " + description);

        // Construir la URL completa
        String url = baseUrl + endPoint + parameters;

        try {
            // Realizar la solicitud HTTP según el método
            if (method.equalsIgnoreCase("GET")) {
                response = given()
                        .headers(headers)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .when()
                        .get(url);
            } else if (method.equalsIgnoreCase("POST")) {
                response = given()
                        .headers(headers)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(jsonBody)
                        .when()
                        .post(url);
            } else if (method.equalsIgnoreCase("PUT")) {
                response = given()
                        .headers(headers)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(jsonBody)
                        .when()
                        .put(url);
            } else if (method.equalsIgnoreCase("DELETE")) {
                response = given()
                        .headers(headers)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .when()
                        .delete(url);
            } else {
                throw new IllegalArgumentException("Método HTTP no soportado: " + method);
            }

            // Almacenar la respuesta
            responseService = response.getBody().asString();

            // Imprimir la respuesta en consola
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Respuesta del servicio: " + responseService);

            // Registrar el resultado en el reporte
            if (response.getStatusCode() == 200 || response.getStatusCode() == 400 || response.getStatusCode() == 404) {
                test.log(Status.PASS, nameTest + " pasó correctamente");
            } else {
                test.log(Status.FAIL, nameTest + " falló");
            }

            return response.getStatusCode();

        } catch (Exception e) {
            // Manejar excepciones durante la solicitud HTTP
            System.err.println("Error al realizar la solicitud HTTP: " + e.getMessage());
            test.log(Status.FAIL, "Error al realizar la solicitud HTTP: " + e.getMessage());
            return -1; // Código de error personalizado
        }
    }

    // Método para guardar la respuesta y el código de estado en un archivo JSON
    public void saveResponseToJson(String filePath) {
        try {
            // Verificar si la respuesta es nula
            if (response == null) {
                throw new IllegalStateException("La respuesta no está disponible.");
            }

            // Crear un mapa para almacenar la respuesta y el código de estado
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("statusCode", response.getStatusCode());
            responseData.put("responseBody", responseService);

            // Convertir el mapa a JSON
            String jsonResponse = gson.toJson(responseData);

            // Crear el archivo y los directorios necesarios
            File file = new File(filePath);
            File parentDir = file.getParentFile();

            // Crear directorios padres si no existen
            if (parentDir != null && !parentDir.exists()) {
                boolean dirsCreated = parentDir.mkdirs();
                if (!dirsCreated) {
                    throw new IOException("No se pudo crear el directorio: " + parentDir.getAbsolutePath());
                }
            }

            // Escribir el JSON en el archivo
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonResponse);
                System.out.println("Respuesta guardada en: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la respuesta en el archivo JSON: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }

    // Método para leer un archivo JSON
    private String readJsonFile(String filePath) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            StringBuilder jsonBody = new StringBuilder();
            while (scanner.hasNextLine()) {
                jsonBody.append(scanner.nextLine());
            }
            scanner.close();
            return jsonBody.toString();
        } catch (FileNotFoundException e) {
            System.err.println("Archivo JSON no encontrado: " + filePath);
            return null;
        }
    }

    // Método para POST con archivo JSON
    public int performPostWithJsonFile(String nameTest, String description, String jsonFilePath) {
        String jsonBody = readJsonFile(jsonFilePath);
        if (jsonBody == null) {
            test.log(Status.FAIL, "No se pudo leer el archivo JSON: " + jsonFilePath);
            return -1; // Código de error personalizado
        }
        return performRequest(nameTest, description, jsonBody);
    }

    // Método para PUT con archivo JSON
    public int performPutWithJsonFile(String nameTest, String description, String jsonFilePath) {
        String jsonBody = readJsonFile(jsonFilePath);
        if (jsonBody == null) {
            test.log(Status.FAIL, "No se pudo leer el archivo JSON: " + jsonFilePath);
            return -1; // Código de error personalizado
        }
        return performRequest(nameTest, description, jsonBody);
    }

    // Método para DELETE
    public int performDelete(String nameTest, String description) {
        return performRequest(nameTest, description, null);
    }

    // Método para ejecutar pruebas y validar el código de estado
    public int executeTest(
            String testName,
            String endpoint,
            int expectedStatusCode,
            String taskName,
            String taskDescription,
            String successMessage,
            String failureMessage
    ) {
        int statusCode = 0;
        try {
            GeneralTask generalTask = new GeneralTask(baseUrl, endpoint, method, "", headers, test);
            statusCode = generalTask.performRequest(taskName, taskDescription, null);

            Assert.assertEquals(statusCode, expectedStatusCode);
            System.out.println(successMessage);
            return statusCode;

        } catch (AssertionError except) {
            System.out.println(failureMessage + ". Status code: " + statusCode);
            throw except;
        }
    }
}
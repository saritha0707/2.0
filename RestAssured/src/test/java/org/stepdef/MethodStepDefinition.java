package org.stepdef;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MethodStepDefinition {

    RequestSpecification hRequest;
    Response hResponse;
    String Username;
    String Password;
    API_Response respDetails;
    private static final long statusCode = 200;
    private static final String contentType = "application/json; charset=utf-8";
    private static final String server = "cloudflare" ;
    private static final String contentEnding = "gzip";
    private static final Logger logger = LogManager.getLogger(MethodStepDefinition.class);
    private static final String GET_METHOD = "Method.GET";
    private static final String POST_METHOD = "Method.POST";
    private static final String PUT_METHOD = "Method.PUT";
    private static final String DELETE_METHOD = "Method.DELETE";

    Header h = new Header("Content-Type","application/json");

    @Given("^Create a request \"([^\"]*)\"$")
    public void createARequest(String URL) throws Throwable {
        PropertyConfigurator.configure("log4j.properties");
        RestAssured.baseURI = URL;
        hRequest = RestAssured.given();
    }

    @When("^send request with body to server \"([^\"]*)\"$")
    public void sendRequestWithBodyToServer(String request) throws Throwable {
        logger.info("Header:"+ hRequest.toString());
        hResponse = hRequest.header(h).request(Method.GET,request);
        JSONParser Parse = new JSONParser();
        JSONObject resBody = (JSONObject)Parse.parse(hResponse.getBody().asString());
        respDetails = API_Response.builder()
                .statusCode(hResponse.getStatusCode())
                .responseStatusLine(hResponse.getStatusLine())
                .responseTime(hResponse.getTime())
                .header(hResponse.getHeaders().toString())
                .contentType(hResponse.getContentType())
                .server(hResponse.getHeader("Server"))
                .contentEncoding(hResponse.getHeader("Content-Encoding"))
                .jsonBody(resBody)
                .jsonStr(hResponse.getBody().asString())
                .build();
        logger.info(respDetails.toString());
    }

    @Then("^Validate response Status Code (\\d+)$")
    public void validateResponseStatusCode(int statuscode) {

        String responseBody = hResponse.getBody().toString();
        int Status = hResponse.getStatusCode();
        logger.info("Status Code : " + Status);
        //logger.info.println("Status Code : " + Status);
        logger.info("Response:"+ hResponse.toString());
        Assert.assertEquals("Success Status code returned",statuscode,Status);
    }

    @Then("^Validate response header details$")
    public void validateHeaderResponseDetails() {
        logger.info(respDetails);
        Assert.assertEquals("Status Code : ",statusCode, respDetails.getStatusCode());
        Assert.assertEquals("Content-Type : " + respDetails.getContentType(),contentType,respDetails.getContentType());
        Assert.assertEquals("Content-Type : " + respDetails.getServer(),server,respDetails.getServer());
        Assert.assertEquals("Content-Ending : " + respDetails.getContentEncoding(),contentEnding,respDetails.getContentEncoding());
    }

    //using filter to retrieve data from JSONObject
    @Then("^Validate response body details$")
    public void validateResponseBodyDetails() {
       JSONObject respBody = respDetails.getJsonBody();
        //update JSONObject field
       logger.info("Page : " + respBody.get("page"));
        // update page field from JSONObject
        respBody.put("page",10);
        logger.info("After field update value of Page : " + respBody.get("page"));
        //add value to key field
        respBody.put("key",20);
        //remove key field from JSON
        logger.info("Key : " + respBody.get("key"));
        respBody.remove("key");
        logger.info("Contains 'key' field: "  + respBody.containsKey("key"));
        logger.info("Per Page : " + respBody.get("per_page"));
        logger.info("Total :" + respBody.get("total"));
        logger.info("Total Pages : " + respBody.get("total_pages"));

        JSONArray respArray = new JSONArray();
        respArray.add(respBody);

        Stream<String> ss = respArray.stream()
                .map(json -> json.toString());
        List<String> l = ss.collect(Collectors.toList());
        logger.info(l.get(0));
        logger.info("JSON Response : " + respDetails.getJsonStr());

        //all fields from JSONArray
        JSONArray data = (JSONArray) respBody.get("data");
        Iterator i = data.stream().iterator();
        while(i.hasNext())
        {
        JSONObject id = (JSONObject) i.next();
        logger.info(id.toJSONString());
        }
//all fields from JSON Array
        ss = data.stream()
                    .map(json -> json.toString());
        l = ss.collect(Collectors.toList());
        logger.info(l.get(0));
        logger.info("Page : " + respDetails.getJsonStr());


    }

    @Then("^send response to file$")
    public void sendResponseToFile() throws IOException {

        try (FileWriter file = new FileWriter("json_response.json")) {
            file.write(respDetails.getJsonStr());
        }
        catch (Exception e)
        {
            logger.info(e.getMessage());
        }
        logger.info("file is created");
    }

    @Then("^serialize and deserialize JSONObject$")
    public void serializeAndDeserializeJSONObject() throws ParseException {
        //Serialization means to convert JSONObject into string
        JSONObject respBody = respDetails.getJsonBody();
        String respBody_str = respBody.toJSONString();
        logger.info("JSONObject converted to string:" + respBody_str);

        // Deserialization convert String into JSONObject
        String var = "{\n" +
                "  \"website\": \"hildegard.org\",\n" +
                "  \"address\": [\n" +
                "    {\n" +
                "      \"zipcode\": \"502032\",\n" +
                "      \"geo\": [\n" +
                "        {\n" +
                "          \"lng\": \"81.1496\",\n" +
                "          \"lat\": \"-37.3159\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"suite\": \"North-52\",\n" +
                "      \"city\": \"Hyderabad\",\n" +
                "      \"street\": \"KBC\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"phone\": \"1-770-736-8031 x56442\",\n" +
                "  \"name\": \"testing\",\n" +
                "  \"company\": {\n" +
                "    \"bs\": \"harness real-time e-markets\",\n" +
                "    \"catchPhrase\": \"North-52\",\n" +
                "    \"name\": \"Nisum\"\n" +
                "  },\n" +
                "  \"id\": \"11\",\n" +
                "  \"username\": \"tester\"\n" +
                "}\n";
        logger.info("String Object : " + var);
        JSONParser obj = new JSONParser();
        JSONObject var_json;
        var_json = (JSONObject) obj.parse(var);
            logger.info("Deserialize : " + var_json);
    }


    @When("^send \"([^\"]*)\" request with body to server \"([^\"]*)\" with \"([^\"]*)\"$")
    public void sendRequestWithBodyToServerWith(String method, String API, String TestDataFile) throws Throwable {
        JSONParser parser = new JSONParser();
        JSONObject request_params = new JSONObject();
       if(!TestDataFile.isEmpty()) {
request_params = (JSONObject) parser.parse(new FileReader(TestDataFile));

        }
        logger.info(hRequest.toString());
        switch(method)
        {
            case "GET":
                hResponse = hRequest.header(h).request(Method.GET,API);
                update_resp_details();
                break;
            case "POST":
                hResponse = hRequest
                        .header(h)
                        .body(request_params.toJSONString())
                        .request(Method.POST,API);
                update_resp_details();
                break;
            case "UPDATE":
                    hResponse = hRequest
                        .header(h)
                        .body(request_params.toJSONString())
                        .request(Method.PUT,API);
                update_resp_details();
                break;
            case "DELETE":
                hResponse = hRequest
                        .header(h)
                        .request(Method.DELETE,API);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + method);
        }
    }

    public void update_resp_details() throws ParseException {
        JSONParser Parse = new JSONParser();
        if (hResponse.getBody().asString() != null) {
              JSONObject resBody = (JSONObject)Parse.parse(hResponse.getBody().asString());
            respDetails = API_Response.builder()
                    .statusCode(hResponse.getStatusCode())
                    .responseStatusLine(hResponse.getStatusLine())
                    .responseTime(hResponse.getTime())
                    .header(hResponse.getHeaders().toString())
                    .contentType(hResponse.getContentType())
                    .server(hResponse.getHeader("Server"))
                    .contentEncoding(hResponse.getHeader("Content-Encoding"))
                    .jsonBody(resBody)
                    .jsonStr(hResponse.getBody().asString())
                    .build();
            logger.info(respDetails.toString());
        }
    }
    @Then("^Validate response Status Code \"([^\"]*)\"$")
    public void validateResponseStatusCode(String status) throws Throwable {
        logger.info("Response:"+ hResponse.toString());
        Assert.assertEquals(status, String.valueOf(hResponse.getStatusCode()));
    }
}

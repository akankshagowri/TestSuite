package com.qa;
import org.testng.annotations.Test;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class PostApiTest {
 private RequestSpecification api() {
  return given().baseUri(System.getProperty("api.baseUrl", "https://jsonplaceholder.typicode.com"))
    .config(io.restassured.config.RestAssuredConfig.config().httpClient(
       io.restassured.config.HttpClientConfig.httpClientConfig()
        .setParam("http.connection.timeout",10000).setParam("http.socket.timeout",10000)))
    .accept("application/json").log().ifValidationFails();
 }
 @Test public void getPostChecksSchemaAndContent() {
  api().get("/posts/1").then().log().ifValidationFails().statusCode(200)
   .contentType("application/json").body(matchesJsonSchemaInClasspath("post.schema.json"))
   .body("id",equalTo(1)).body("userId",equalTo(1)).body("title",not(emptyString()));
 }
 @Test public void filtersReturnOnlyRequestedOwner() {
  api().queryParam("userId",2).get("/posts").then().statusCode(200)
   .body("size()",greaterThan(0)).body("userId",everyItem(equalTo(2)))
   .body("id",everyItem(greaterThan(0)));
 }
 @Test public void missingResourceReturns404() {
  api().get("/posts/999999").then().statusCode(404).body("size()",equalTo(0));
 }
 @Test public void createChecksEchoNotPersistence() {
  api().contentType("application/json").body("{\"userId\":1,\"title\":\"QA contract\",\"body\":\"independent data\"}")
   .post("/posts").then().statusCode(201).body(matchesJsonSchemaInClasspath("post.schema.json"))
   .body("title",equalTo("QA contract")).body("body",equalTo("independent data")).body("userId",equalTo(1));
 }
 @Test public void patchChecksUpdatedResponse() {
  api().contentType("application/json").body("{\"title\":\"Updated QA title\"}")
   .patch("/posts/1").then().statusCode(200).body(matchesJsonSchemaInClasspath("post.schema.json"))
   .body("id",equalTo(1)).body("title",equalTo("Updated QA title"));
 }
 @Test public void deleteChecksContract() {
  api().delete("/posts/1").then().statusCode(200).body("size()",equalTo(0));
 }
}

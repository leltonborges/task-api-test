package br.ce.tasks.apitest;

import java.net.MalformedURLException;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8085/tasks-backend";
	}
	
	@Test
	public void requireReturTasks() {
		RestAssured
		.given()
		.when()
			.get("/todo")
		.then()
			.statusCode(200);
	}
	
	@Test
	public void requireAddTasksSuccess() {
		RestAssured
		.given()
			.body("{\"task\":\"Teste API\",\"dueDate\": \"2021-12-31\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201);
	}
	
	@Test
	public void notRequireAddTasksSuccess() {
		RestAssured
		.given()
			.body("{\"task\":\"Teste API\",\"dueDate\": \"2010-12-31\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"));
			
	}
	
	@Test
	public void removeSuccessTasks() throws MalformedURLException {
		Integer id = RestAssured
		.given()
			.body("{\"task\":\"Teste tasks API\",\"dueDate\": \"2021-12-31\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
		.extract().path("id");
		
		RestAssured.given()
			.when()
				.delete("/todo/"+id)
				.then()
					.statusCode(204);
	}
}


package omdbApiTest;

import static org.hamcrest.Matchers.notNullValue;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class AppTest {
	
	String baseURI = "http://www.omdbapi.com/";
	
	//Siteden alinan ApiKey in buraya girilmesi gerekmektedir!!!
	String apiKey = "d9d3e4a5";
	
	@Test
	public void testSorcererStoneMovie() {
		
		//Search Parametresi
		String parameters = "&s=Harry+Potter";
		
		String imdbID = "";
		String URI = baseURI + "?apiKey=" + apiKey + parameters;

		//Search request for Harry Potter
		Response res =  (Response) RestAssured.get(URI)
						.then()
						.assertThat()
						.statusCode(HttpStatus.SC_OK)
						.contentType("application/json")
						.extract()
						.response();

		JSONObject jsonObjectFull = new JSONObject(res.body().asString());
		
		//Donen sonuclarin ekranda gosterilmesi
		System.out.println(res.asString());

		JSONArray jsonArray = (JSONArray)jsonObjectFull.get("Search");
		
		for(int i = 0; i< jsonArray.length();i++) {
			
			JSONObject jsonObjOfItem = (JSONObject)jsonArray.get(i);
			
			String  title = (String) jsonObjOfItem.get("Title");
			
			if(title.contains("Harry Potter and the Sorcerer's Stone")){
				//Sonuc Bulundu!
				imdbID = (String) jsonObjOfItem.get("imdbID");
				System.out.println("Sonuc Bulundu. imdbID ==> "+ imdbID);
			}
		}
		
		if(!imdbID.isEmpty()) {
			
			parameters = "&i=" + imdbID;
			
			URI = baseURI + "?apiKey=" + apiKey + parameters;
			
			//Title,Year,Released ve statusCode kontrolu ile istek mesaji!!
			res =  (Response) RestAssured.get(URI)
							.then()
							.assertThat()
							.statusCode(HttpStatus.SC_OK)
							.contentType("application/json")
							.body("Title",notNullValue())
							.body("Year",notNullValue())
							.body("Released",notNullValue())
							.extract()
							.response();
			
			System.out.println(res.asString());
			
		}else {
			System.out.println("Sonuc Bulunamadi!");
		}
	}


}

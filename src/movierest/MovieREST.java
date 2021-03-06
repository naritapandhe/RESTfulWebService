package movierest;


//Required Imports
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.view.Viewable;

import java.sql.*;

@Path("/")
public class MovieREST {

	//Declaring required API Keys
	private static final String THEMOVIEDBAPIKEY="bbfb233a717e24474c6f807493956161";
	private static final String TMDBAPIKEY="2ace9nn4kqudu3r3nsfzfudg";
	
	
	
	public String executeApi(String apiURI, String apiKey){
		//Declaring required variables
		String output = null;
		String outputResult = null;
		String concatenatedUrl=null;
		try{
			
			concatenatedUrl=apiURI+apiKey;
			//Connecting to the TheMovieDB API
			URL url = new URL(concatenatedUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			} else {

				//Initiating GET request and retrieving the list of movies
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
				while ((output = br.readLine()) != null) {
					outputResult = output;

				}
			}

			conn.disconnect();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return outputResult;
				
	}
	
	
	@GET
    @Path("/index")
    @Produces(MediaType.TEXT_HTML)
    public Viewable indexPage() {
        return new Viewable("/MovieForm.html", null);
    }
	
	/**
	 * Function to populate the movies
	 * into local database.
	 */
	@GET()
	@Path("movies/populate")
	@Produces(MediaType.APPLICATION_JSON)
	public String populateMoviesNowPlaying() {
		String output=null;
		String outputResult=null;

		try{
			
			String url = "jdbc:mysql://127.0.0.1:3306/";
			String dbName = "movies";
			String driver = "com.mysql.jdbc.Driver";
			String userName = "root";
			String password = "root";
			
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url + dbName,
					userName, password);
			Statement st = conn.createStatement();
			
			
			URL url1 = new URL("http://api.themoviedb.org/3/movie/now_playing?api_key=bbfb233a717e24474c6f807493956161");
			HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
			
			conn1.setRequestMethod("GET");

			if (conn1.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn1.getResponseCode());
			}else{

				BufferedReader br = new BufferedReader(new InputStreamReader((conn1.getInputStream())));
				while ((output = br.readLine()) != null) {
					
					//Read and split the input to retrive the movie title, description and rating
					StringTokenizer tokenizer = new StringTokenizer(output,",");
					int numberofTokens = tokenizer.countTokens();
					String[] splitArr = new String[numberofTokens];
					String[] id  = new String[numberofTokens];
					String[] Title = new String[numberofTokens];
					String[] Popularity  = new String[numberofTokens];		
					for(int i =0;i<numberofTokens;i++){
						while(tokenizer.hasMoreTokens()){
							splitArr[i] = tokenizer.nextToken(); 
							if(splitArr[i].contains("\"id\":")){

								id = splitArr[i].split(":");
							}
							else if(splitArr[i].contains("\"title\":"))
							{
								Title = splitArr[i].split(":");

							}
							else if(splitArr[i].contains("\"popularity\":"))

							{
								Popularity = splitArr[i].split(":");

							}
							if(id[1]!=null && Title[1]!=null && Popularity[1]!=null){
								double r = Double.parseDouble(Popularity[1]);
								int num = (int)r;
								int Rating = num/8;
								if(Rating<1)
									Rating =1;
								else if(Rating > 5)
									Rating = 5;
								String query = "insert into moviesInfo(movieId,movieTitle,movieDescription,movieRating) values("+id[1]+",'"+Title[1]+"','"+Title[1]+"',"+Rating+");";                           
								PreparedStatement ps = conn.prepareStatement(query);
								ps.executeUpdate();
								for(i=0;i<2;i++){
									id[i]=null;
									Title[i]=null;
									Popularity[i] = null;
								}

							}
						}
					}

					outputResult=output; 

				}
			}

			conn1.disconnect();

		}catch(Exception e){

			e.printStackTrace();
		}


		return outputResult;
	}


	
	/**
	 * Function to return the list of movies
	 * currently playing.
	 * 
	 * @return stringified JSON
	 */
	@GET()
	@Path("/movies")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMoviesNowPlaying() {
		
		//Declaring required variables
		String outputResult = null;
		try {

			//Build the required API URI
			String apiURI="http://api.themoviedb.org/3/movie/now_playing?";
			String apiKey="api_key="+THEMOVIEDBAPIKEY;
			
			//Execute the required API
			outputResult=this.executeApi(apiURI, apiKey);
			
		} catch (Exception e) {

			e.printStackTrace();
		}

		return outputResult;
	}

	/**
	 * Function to retrieve a specific movie
	 *
	 * @param String MovieID
	 * @return stringified JSON
	 */
	@GET
	@Path("/movies/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMovie(@PathParam("id") String id) {
		
		//Declaring required variables
		String outputResult = null;
		JSONObject jsonObject=new JSONObject();
		try {
			
			//Build the required API URI
			String apiURI="http://api.themoviedb.org/3/movie/" + id + "?";
			String apiKey="api_key="+THEMOVIEDBAPIKEY;
			
			//Execute the required API
			outputResult=this.executeApi(apiURI, apiKey);
			JSONObject jObject  = new JSONObject(outputResult); 
			jsonObject.put("results",jObject);
			
		} catch (Exception e) {

			e.printStackTrace();
		}

		return jsonObject.toString();

	}
	
	/**
	 * Function to retrieve the list of movies currently
	 * being showcased in Athen's theatres.
	 * 
	 * @return stringified JSON
	 */
	@GET()
	@Path("/movies/theatres")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMoviesInTheatres() {
		//Declaring required variables
		String outputResult = null;
		JSONObject jsonObject=new JSONObject();
		try {

			//Retrieve the current date
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String dt = dateFormat.format(date).toString();
			
			//Build the required API URI
			String apiURI="http://data.tmsapi.com/v1/movies/showings?startDate=" + dt + "&zip=30606";
			String apiKey="&api_key="+TMDBAPIKEY;
			
			//Execute the required API
			outputResult=this.executeApi(apiURI, apiKey);
			
		} catch (Exception e) {

			e.printStackTrace();
		}

		return outputResult;
	}
	
	/**
	 * Function to search movies by title
	 * 
	 * @param String title
	 * @return stringified JSON
	 */
	@GET()
	@Path("/movies/search/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMoviesByTitle(@PathParam("title") String title) {
		//Declaring required variables
		String outputResult = null;
		try {

			//Connecting to the TheMovieDB API
			String encodedString = URLEncoder.encode(title, "UTF-8").toString();
			
			//Build the required API URI
			String apiURI="http://api.themoviedb.org/3/search/movie?query="+encodedString;
			String apiKey="&api_key="+THEMOVIEDBAPIKEY;
			
			//Execute the required API
			outputResult=this.executeApi(apiURI, apiKey);
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}

		return outputResult;
	}
	
	/**
	 * Function to retrieve list of genre 
	 * 
	 * @return stringified JSON
	 */
	@GET()
	@Path("/movies/genre")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllGenres() {
		//Declaring required variables
		String outputResult = null;
		try {

			//Build the required API URI
			String apiURI="http://api.themoviedb.org/3/genre/movie/list?";
			String apiKey="api_key="+THEMOVIEDBAPIKEY;
			
			//Execute the required API
			outputResult=this.executeApi(apiURI, apiKey);
			
		} catch (Exception e) {

			e.printStackTrace();
		}

		return outputResult;
	}

	/**
	 * Function to retrieve all movies of a particular genre
	 * 
	 * @param genreId
	 * @return stringified JSON
	 */
	@GET()
	@Path("/movies/genre/{genreId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMoviesByGenre(@PathParam("genreId") int genreId) {
		//Declaring required variables
		String outputResult = null;
		try {
			
			//Build the required API URI
			String apiURI="http://api.themoviedb.org/3/genre/" + genreId + "/movies?";
			String apiKey="api_key="+THEMOVIEDBAPIKEY;
			
			//Execute the required API
			outputResult=this.executeApi(apiURI, apiKey);

			
		} catch (Exception e) {

			e.printStackTrace();
		}

		return outputResult;
	}

	/**
	 * Function to create a movie and store it in the database
	 * 
	 * @param stringified movieData
	 * @return Response
	 */
	@POST
	@Path("movies")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMovie(Movie movieData) {
		int outputMovieId = 0;
		String jsonObj = null;
		try {
			String url = "jdbc:mysql://127.0.0.1:3306/";
			String dbName = "movies";
			String driver = "com.mysql.jdbc.Driver";
			String userName = "root";
			String password = "root";
			

			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url + dbName,
					userName, password);
			Statement st = conn.createStatement();
			int insertStatus = st
					.executeUpdate("insert into moviesInfo(movieTitle,movieDescription,movieRating)"
							+ "values"
							+ "('"
							+ movieData.movieTitle
							+ "','"
							+ movieData.movieDescription
							+ "','"
							+ movieData.movieRating + "')");
			if (insertStatus != 0) {
				ResultSet res = st
						.executeQuery("select * from moviesInfo where movieTitle like '%"
								+ movieData.movieTitle + "%'");
				while (res.next()) {
					outputMovieId = res.getInt("movieId");

				}
			}

			JSONObject obj = new JSONObject();
			obj.put("movieId", new Integer(outputMovieId));
			jsonObj = obj.toString();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return Response.ok(jsonObj, MediaType.APPLICATION_JSON).build();

	}

	/**
	 * Function to rate a movie
	 * 
	 * @param Movie movieData
	 * @return Response
	 */
	@PUT
	@Path("movies/rate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMovie(Movie movieData) {
		String jsonObj = null;
		JSONObject obj = new JSONObject();
		try {
			String url = "jdbc:mysql://127.0.0.1:3306/";
			String dbName = "movies";
			String driver = "com.mysql.jdbc.Driver";
			String userName = "root";
			String password = "root";
			
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url + dbName,
					userName, password);
			Statement st = conn.createStatement();
			String query = "update moviesInfo"
					+ " set movieRating = " + movieData.movieRating 
					+ " where movieId= " + movieData.movieId ;

			
			int insertStatus = st.executeUpdate(query);
			if (insertStatus != 0) {
				ResultSet res = st.executeQuery("select * from moviesInfo where movieId=" + movieData.movieId);
				while (res.next()) {
				  obj.put("movieId", new Integer(res.getInt("movieId")));
				  obj.put("movieTitle", new String(res.getString("movieTitle")));
				  obj.put("movieDescription", new String(res.getString("movieDescription")));
				  obj.put("movieRating", new Integer(res.getInt("movieRating")));

				}

			}

			jsonObj = obj.toString();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return Response.ok(jsonObj, MediaType.APPLICATION_JSON).build();

	}

}

package movierest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;

@Path("/")
public class MovieREST {
	
	  @GET()
		@Path("movies")
		@Produces(MediaType.APPLICATION_JSON)
		public String getMoviesNowPlaying() {
			String output=null;
			String outputResult=null;
			try{
			
			URL url = new URL("http://api.themoviedb.org/3/movie/now_playing?api_key=bbfb233a717e24474c6f807493956161");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}else{

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		  		while ((output = br.readLine()) != null) {
		  				outputResult=output; 
		  				 
		  			}
		  		}
		  		
			conn.disconnect();
				
			}catch(Exception e){
				
				e.printStackTrace();
			}
	 
			
			return outputResult;
		}
	  
	  @GET
	     @Path("/movies/{id}")
	      @Produces(MediaType.APPLICATION_JSON)
	      public String getMovie(@PathParam("id") String id) {
	 
	        String output=null;
			String outputResult=null;
			try{
			
			URL url = new URL("http://api.themoviedb.org/3/movie/"+id+"?api_key=bbfb233a717e24474c6f807493956161");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}else{

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		  		while ((output = br.readLine()) != null) {
		  				outputResult=output; 
		  				 
		  			}
		  		}
		  		
			conn.disconnect();
				
			}catch(Exception e){
				
				e.printStackTrace();
			}
	 
			
			return outputResult;

	      }

	  @GET()
	  	@Path("movies/theatres")
	  	@Produces(MediaType.APPLICATION_JSON)
	  	public String getMoviesInTheatres(String id) {
	  		String output=null;
	  		String outputResult=null;
	  		//JSONArray jsonArray = null;
	  		try{
	  		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				String dt=dateFormat.format(date).toString();
			   URL url = new URL("http://data.tmsapi.com/v1/movies/showings?startDate="+dt+"&zip=30606&api_key=2ace9nn4kqudu3r3nsfzfudg");

	  		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	  		conn.setRequestMethod("GET");
	  		
	  		if (conn.getResponseCode() != 200) {
	  			throw new RuntimeException("Failed : HTTP error code : "
	  					+ conn.getResponseCode());
	  		}else{
	   
	  		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	  		while ((output = br.readLine()) != null) {
	  				outputResult=output; 
	  				 
	  			}
	  		}
	  			
	  		conn.disconnect();
	  			
	  		}catch(Exception e){
	  			
	  			e.printStackTrace();
	  		}
	   
	  		return outputResult;
	  	}


	  @GET()
	  	@Path("movies/search/{title}")
	  	@Produces(MediaType.APPLICATION_JSON)
	  	public String getMoviesByTitle(@PathParam("title") String title) {
	  		String output=null;
	  		String outputResult=null;
	  		try{
	  		
	  		  String encodedString=URLEncoder.encode(title,"UTF-8").toString();
			   URL url = new URL("http://api.themoviedb.org/3/search/movie?api_key=bbfb233a717e24474c6f807493956161&query="+encodedString);

	  		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	  		conn.setRequestMethod("GET");
	  		
	  		if (conn.getResponseCode() != 200) {
	  			throw new RuntimeException("Failed : HTTP error code : "
	  					+ conn.getResponseCode());
	  		}else{
	   
	  		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	  		while ((output = br.readLine()) != null) {
	  				outputResult=output; 
	  				 
	  			}
	  		}
	  			
	  		conn.disconnect();
	  			
	  		}catch(Exception e){
	  			
	  			e.printStackTrace();
	  		}
	   
	  		return outputResult;
	  	}


}

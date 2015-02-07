package movierest;

public class Movie {
	public int movieId;
	public String movieTitle;
	public String movieDescription;
	public int movieRating;

	public Movie() {

	}

	public Movie(int movieId, String movieTitle, String movieDescription, int movieRating) {
		this.movieId = movieId;
		this.movieTitle = movieTitle;
		this.movieDescription = movieDescription;
		this.movieRating = movieRating;
	}

	public void setMovieRating(int rating) {
		this.movieRating = rating;
	}

	public int getMovieRating() {
		return this.movieRating;
	}

	public void setMovieDescription(String description) {
		this.movieDescription = description;
	}

	public String getMovieDescription() {
		return this.movieDescription;
	}

	public void setMovieTitle(String title) {
		this.movieTitle = title;
	}

	public String getMovieTitle() {
		return this.movieTitle;
	}

	public void setMovieId(int id) {
		this.movieId = id;
	}

	public int getMovieId() {
		return this.movieId;
	}

	@Override
	public String toString() {
		return new StringBuffer(" Movie ID : ").append(this.movieId)
				.append(" Movie Title : ").append(this.movieTitle)
				.append(" Description : ").append(this.movieDescription)
				.append(" Rating : ").append(this.movieRating).toString();
	}


}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * A class for downloading movie data from the internet.
 * Code adapted from Google.
 *
 * YOUR TASK: Add comments explaining how this code works!
 * 
 * @author Joel Ross & Kyungmin Lee
 */
public class MovieDownloader {

	public static String[] downloadMovieData(String movie) {

		//construct the url for the omdbapi API
		String urlString = "";
		try {
			urlString = "http://www.omdbapi.com/?s=" + URLEncoder.encode(movie, "UTF-8") + "&type=movie";
		}catch(UnsupportedEncodingException uee){
			return null;
		}

		// Defines an object that supports HTTP-specific features, such as making requests
		// for data.
		HttpURLConnection urlConnection = null;
		// Defines a buffered reader object that reads in a stream of input, and preprocesses it 
		// so that it can be efficiently read.
		BufferedReader reader = null;

		String movies[] = null;

		try {
			// Creates a new URL to access
			URL url = new URL(urlString);

			// Makes a GET request to the specified url
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			// Gets the stream of data from the url request response
			InputStream inputStream = urlConnection.getInputStream();
			// Create a new buffer for Strings
			StringBuffer buffer = new StringBuffer();
			// if no data returned, end program/
			if (inputStream == null) {
				return null;
			}
			// Set reader to a buffered (preproccessed) set of the stream data
			reader = new BufferedReader(new InputStreamReader(inputStream));

			// Read the next line
			String line = reader.readLine();

			// Add new line to buffer, if not null. Read the line.
			while (line != null) {
				buffer.append(line + "\n");
				line = reader.readLine();
			}

			// If buffer is empty, end program
			if (buffer.length() == 0) {
				return null;
			}

			// Set results to be the buffered response from the GET request. Format the data.
			String results = buffer.toString();
			results = results.replace("{\"Search\":[","");
			results = results.replace("]}","");
			results = results.replace("},", "},\n");

			movies = results.split("\n");
		} 
		catch (IOException e) {
			return null;
		} 
		// end the connection.
		finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} 
				catch (IOException e) {
				}
			}
		}

		return movies;
	}


	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);

		boolean searching = true;

		while(searching) {					
			System.out.print("Enter a movie name to search for or type 'q' to quit: ");
			String searchTerm = sc.nextLine().trim();
			if(searchTerm.toLowerCase().startsWith("q")){
				searching = false;
			}
			else {
				String[] movies = downloadMovieData(searchTerm);
				for(String movie : movies) {
					System.out.println(movie);
				}
			}
		}
		sc.close();
	}
}

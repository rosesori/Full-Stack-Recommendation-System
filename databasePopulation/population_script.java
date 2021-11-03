package module1.src;
import java.io.*;
import java.util.Scanner;
import java.sql.*;
// import java.util.regex.*;

// Commands to run this script
// This will compile all java files in this directory
// javac *.java
// This command tells the file where to find the postgres jar which it needs to
// execute postgres commands, then executes the code
//* Windows: java -cp ".;postgresql-42.2.8.jar" population_script
//* Mac/Linux: java -cp ".:postgresql-42.2.8.jar" population_script

// MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE

public class population_script {


    /*----------------------------------------ONE FOR EACH CLEANED CSV---------------------------------------*/

    // * DONT need to iterate a primary key
    public static void scanCrewMembers(Connection conn) {
        // Populate Database
        try{
            System.out.println("Populating CrewMembers Table...");

            String fileName = "../../cleanedCSVFiles/crew_member.csv";
            Scanner sc = new Scanner(new File(fileName));
            sc.useDelimiter("\n"); // Sets the delimiter pattern

            sc.next();             // Skips first line

            // Matches the columns of our database table
            String crewId;
            String primaryName;
            String birthYear;
            String[] splitLine;

            // Iterate through each line of file
            while (sc.hasNext()) {
                splitLine = (sc.next()).split(",");    // Split line at commas, splitLine is size 3

                if (splitLine.length < 3) {             // If the array is less than 3 elements
                    continue;
                }
                if (splitLine[2].length() < 4) {       // If the line does not have a birthYear, skip that person
                    continue;
                }

                crewId = splitLine[0];
                primaryName = splitLine[1];
                birthYear = splitLine[2];

                // If the name is super weird, then we're going to skip it
                if ( !(primaryName.matches("[a-zA-Z\\s+_.-]+")) ){
                    continue;
                }else{
                    // Populate database
                    String sqlCommand = "INSERT INTO crewmembers VALUES('" + crewId + "', '" + primaryName + "', '" + birthYear + "');";
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(sqlCommand);
                }
            }

            // Close the scanner
            sc.close();
        } catch (Exception e) {
            System.out.println("Failed to populate CrewMembers table");
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
    }

    // *Need to iterate a primary key
    public static void scanCustomersRatings(Connection conn) throws FileNotFoundException {
        // Populate Database
        try{
            System.out.println("Populating CustomersRatings Table...");

            String fileName = "../../cleanedCSVFiles/customers_ratings.csv";
            Scanner sc = new Scanner(new File(fileName));
            sc.useDelimiter("\n"); // Sets the delimiter pattern

            sc.next();             // Skips first line

            // Matches the columns of our database table
            int customer_ratings_pk = 1;
            String media_ID;
            String customer_ID;
            String customer_rating;
            String date_rated;
            String[] splitLine;

            // Iterate through each line of file
            while (sc.hasNext()) {
                splitLine = (sc.next()).split(",");    // Split line at commas, splitLine is size 3

                media_ID = splitLine[0];
                customer_ID= splitLine[1];
                customer_rating = splitLine[2];
                date_rated = splitLine[3];

                // Populate database
                String sqlCommand = "INSERT INTO customersratings VALUES('" + customer_ratings_pk + "', '" + media_ID + "', '" + customer_ID + "', '" + customer_rating + "', '" + date_rated + "');";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sqlCommand);

                customer_ratings_pk++;
            }

            // Close the scanner
            sc.close();
        } catch (Exception e) {
            System.out.println("Failed to populate CustoemrsRatings table");
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
    }

    // *Need to iterate a primary key
    public static void scanCustomersWatchedLists(Connection conn) {
        // Populate Database
        try{
            System.out.println("Populating CustomersWatchedList Table...");

            String fileName = "../../cleanedCSVFiles/customers_watched_lists.csv";
            Scanner sc = new Scanner(new File(fileName));
            sc.useDelimiter("\n"); // Sets the delimiter pattern

            sc.next();             // Skips first line

            // Matches the columns of our database table
            int customer_watched_lists_pk = 1;
            String customer_ID;
            String media_ID;
            String[] splitLine;

            // Iterate through each line of file
            while (sc.hasNext()) {
                splitLine = (sc.next()).split(",");    // Split line at commas, splitLine is size 3

                customer_ID= splitLine[0];
                media_ID = splitLine[1];

                // Populate database
                String sqlCommand = "INSERT INTO customerswatchedlist VALUES('" + customer_watched_lists_pk + "', '" + customer_ID + "', '" + media_ID + "');";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sqlCommand);

                customer_watched_lists_pk++;
            }

            // Close the scanner
            sc.close();
        } catch (Exception e) {
            System.out.println("Failed to populate customers watched list table");
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
    }

    // * DONT need to iterate a primary key
    public static void scanMediaCollection(Connection conn) {
        // Populate Database
        try{
            System.out.println("Populating MediaCollection Table...");

            // Matches the columns of our database table
            String fileName = "../../cleanedCSVFiles/media_collection.csv";
            Scanner sc = new Scanner(new File(fileName));
            sc.useDelimiter("\n"); // Sets the delimiter pattern

            sc.next();             // Skips first line

            String media_ID;
            String media_type;
            String media_title;
            String runtime;
            String average_rating;
            String[] splitLine;

            // Iterate through each line of file
            while (sc.hasNext()) {
                splitLine = (sc.next()).split(",");    // Split line at commas, splitLine is size 3

                media_ID = splitLine[0];
                media_type = splitLine[1];
                media_title = splitLine[2];
                runtime = splitLine[3];
                average_rating = splitLine[4];

                // If the name is super weird, then we're going to skip it
                
                if ( !(media_title.matches("[0-9a-zA-Z\\s+_.:!?&()-]+")) ){
                    continue;
                }else{
                    // Populate database
                    String sqlCommand = "INSERT INTO mediacollection VALUES('" + media_ID + "', '" + media_type + "', '" + media_title + "', '" + runtime + "', '" + average_rating + "');";
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(sqlCommand);
                }
            }

            // Close the scanner
            sc.close();
        } catch (Exception e) {
            System.out.println("Failed to populate MediaCollection table");
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
    }

    // *Need to iterate a primary key
    public static void scanMediaCrewMembers(Connection conn) {
        // Populate Database
        try{
            System.out.println("Populating MediaCrewMembers Table...");

            String fileName = "../../cleanedCSVFiles/media_crew_members.csv";
            Scanner sc = new Scanner(new File(fileName));
            sc.useDelimiter("\n"); // Sets the delimiter pattern

            sc.next();             // Skips first line

            // Matches the columns of our database table
            int media_crew_members_pk = 1;
            String media_ID;
            String crew_ID;
            String job;
            String[] splitLine;

            // Iterate through each line of file
            while (sc.hasNext()) {
                splitLine = (sc.next()).split(",");    // Split line at commas, splitLine is size 3

                media_ID = splitLine[0];
                crew_ID = splitLine[1];
                job = splitLine[2];

                // Populate database
                String sqlCommand = "INSERT INTO mediacrewmembers VALUES('" + media_crew_members_pk + "', '" + media_ID + "', '" + crew_ID + "', '" + job + "');";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sqlCommand);

                media_crew_members_pk++;
            }

            // Close the scanner
            sc.close();
        } catch (Exception e) {
            System.out.println("Failed to populate MediaCrewMembers table");
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
    }

    // *Need to iterate a primary key
    public static void scanMediaGenres(Connection conn) {
        // Populate Database
        try{
            System.out.println("Populating MediaGenres Table...");

            String fileName = "../../cleanedCSVFiles/media_genres.csv";
            Scanner sc = new Scanner(new File(fileName));
            sc.useDelimiter("\n"); // Sets the delimiter pattern

            sc.next();             // Skips first line in file
            
            // Matches the columns of our database table
            int media_genres_pk = 1;
            String media_ID;
            String genreList;
            String[] splitLine;

            // Iterate through each line of file
            while (sc.hasNext()) {
                splitLine = (sc.next()).split(",", 2);    // Split line at commas, splitLine is size 2

                // Example: starting with line-> tt402,"Comedy,Romance"
                media_ID = splitLine[0];                  // Example: tt402
                genreList = splitLine[1];                     // Example: "Comedy,Romance"

                // If the genre list is surrounded by parentheis, remove the parenthesis around it
                if ( genreList.charAt(0) ==  34) {
                    genreList = genreList.substring(1, genreList.length()-2);   // "Comedy,Romance" should now be Comedy,Romance
                } else {
                    genreList = genreList.substring(0, genreList.length()-1);   // Remove weird "\r" that shows at end of line
                }

                // Populate database with a new entry for every genre for a movie
                // To do this we will need to iterate through genres
                String[] splitGenres = genreList.split(",");

                for ( String genre : splitGenres ) {
                    // Populate database with new entry for every genre for a movie
                    String sqlCommand = "INSERT INTO mediagenres VALUES('" + media_genres_pk + "', '" +  media_ID + "', '" + genre + "');";
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(sqlCommand);
                    media_genres_pk++;
                }

            }

            // Close the scanner
            sc.close();
        } catch (Exception e) {
            System.out.println("Failed to populate MediaGenres table");
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
    }

    /*-----------------------------------------------------------------------------------------------------*/


    public static void main(String[] args) throws FileNotFoundException {

        // Building the connection with your credentials
        Connection conn = null;
        String teamNumber = "11";
        String sectionNumber = "903";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
        String userPassword = "new_password";

        // Connecting to the database
        try {
            conn = DriverManager.getConnection(dbConnectionString, userName, userPassword);
        } catch (Exception e) {
            System.out.println("Failed Driver Manager");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");

        // Call functions to populate date
            scanCrewMembers(conn);
            //scanCustomersRatings(conn);
            //scanCustomersWatchedLists(conn);
            //scanMediaCollection(conn);
            //scanMediaCrewMembers(conn);
            //scanMediaGenres(conn);

        // Closing the connection
        System.out.println("Closing the connection");
        try {
            conn.close();
            System.out.println("Connection Closed.");
        } catch (Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }

}

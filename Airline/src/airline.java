
import java.sql.*;
import java.util.Scanner;

public class airline {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/airline_db"; // Database name
        String user = "root";  
        String pass = "admin";  

        Scanner sc = new Scanner(System.in);

        try {
            // Load and register driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection tempCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/", user, pass);

            // Create Database if not exists
            Statement stmt1 = tempCon.createStatement();
            stmt1.executeUpdate("CREATE DATABASE IF NOT EXISTS airline_db");
            System.out.println("Database checked/created!");

            tempCon.close();

            // Establish connection
            Connection con = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to Airline Management Database!");

            String createTableQuery = "CREATE TABLE IF NOT EXISTS booking (" +
                "booking_id INT PRIMARY KEY," +
                "passenger_name VARCHAR(100)," +
                "flight_no VARCHAR(50)," +
                "destination VARCHAR(50)," +
                "ticket_price DOUBLE)";
            Statement stmtTable = con.createStatement();
            stmtTable.executeUpdate(createTableQuery);
            stmtTable.close();
            System.out.println("Booking table checked/created!");

            while (true) {
                System.out.println("\n=== Airline Management Menu ===");
                System.out.println("1. Add Flight Booking");
                System.out.println("2. Display All Bookings");
                System.out.println("3. Update Booking Details");
                System.out.println("4. Cancel Booking");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        // INSERT Operation
                        System.out.print("Enter Booking ID: ");
                        int bid = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Passenger Name: ");
                        String pname = sc.nextLine();
                        System.out.print("Enter Flight Number: ");
                        String flightNo = sc.nextLine();
                        System.out.print("Enter Destination: ");
                        String dest = sc.nextLine();
                        System.out.print("Enter Ticket Price: ");
                        double price = sc.nextDouble();

                        String insertQuery = "INSERT INTO booking (booking_id, passenger_name, flight_no, destination, ticket_price) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement psInsert = con.prepareStatement(insertQuery);
                        psInsert.setInt(1, bid);
                        psInsert.setString(2, pname);
                        psInsert.setString(3, flightNo);
                        psInsert.setString(4, dest);
                        psInsert.setDouble(5, price);

                        int inserted = psInsert.executeUpdate();
                        System.out.println(inserted + " booking added successfully!");
                        psInsert.close();
                        break;

                    case 2:
                        // DISPLAY Operation
                        String selectQuery = "SELECT * FROM booking";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(selectQuery);

                        System.out.println("\nBookingID\tPassenger Name\tFlight No\tDestination\tTicket Price");
                        System.out.println("-----------------------------------------------------------------------");
                        while (rs.next()) {
                            System.out.printf("%d\t\t%s\t\t%s\t\t%s\t\t%.2f\n",
                                    rs.getInt("booking_id"),
                                    rs.getString("passenger_name"),
                                    rs.getString("flight_no"),
                                    rs.getString("destination"),
                                    rs.getDouble("ticket_price"));
                        }
                        rs.close();
                        stmt.close();
                        break;

                    case 3:
                        // UPDATE Operation
                        System.out.print("Enter Booking ID to update: ");
                        int ubid = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter new Passenger Name: ");
                        String newPname = sc.nextLine();
                        System.out.print("Enter new Destination: ");
                        String newDest = sc.nextLine();
                        System.out.print("Enter new Ticket Price: ");
                        double newPrice = sc.nextDouble();

                        String updateQuery = "UPDATE booking SET passenger_name=?, destination=?, ticket_price=? WHERE booking_id=?";
                        PreparedStatement psUpdate = con.prepareStatement(updateQuery);
                        psUpdate.setString(1, newPname);
                        psUpdate.setString(2, newDest);
                        psUpdate.setDouble(3, newPrice);
                        psUpdate.setInt(4, ubid);

                        int updated = psUpdate.executeUpdate();
                        System.out.println(updated + " booking updated successfully!");
                        psUpdate.close();
                        break;

                    case 4:
                        // DELETE Operation
                        System.out.print("Enter Booking ID to cancel: ");
                        int dbid = sc.nextInt();

                        String deleteQuery = "DELETE FROM booking WHERE booking_id=?";
                        PreparedStatement psDelete = con.prepareStatement(deleteQuery);
                        psDelete.setInt(1, dbid);

                        int deleted = psDelete.executeUpdate();
                        System.out.println(deleted + " booking cancelled successfully!");
                        psDelete.close();
                        break;

                    case 5:
                        // EXIT
                        System.out.println("Exiting Airline Management System... Goodbye!");
                        con.close();
                        sc.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
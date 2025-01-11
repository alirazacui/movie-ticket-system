import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.util.HashMap;
import java.util.Map;

public class VIEWBOOKINGS extends JFrame {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/movie_managment";
    private static final String USER = "root";
    private static final String PASSWORD = "4488";

    public VIEWBOOKINGS() {
        setTitle("View bookings");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Booking list:");
        titleLabel.setBounds(10, 20, 150, 25);
        panel.add(titleLabel);

        JTextArea moviesList = new JTextArea();
        moviesList.setBounds(10, 50, 360, 300);
        moviesList.setEditable(false);

        // Fetch bookings from the database
        String bookingsData = fetchBookingsFromDatabase();
        moviesList.setText(bookingsData);

        JScrollPane scrollPane = new JScrollPane(moviesList);
        scrollPane.setBounds(10, 50, 360, 300);
        panel.add(scrollPane);
    }

    private String fetchBookingsFromDatabase() {
        StringBuilder bookingsData = new StringBuilder();
        Map<Integer, String> movieMap = new HashMap<>();  // Map to store movie IDs and their titles

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Fetch movie details and store them in the map
            String movieQuery = "SELECT movie_id, title FROM movies";
            ResultSet resultSet2 = statement.executeQuery(movieQuery);
            while (resultSet2.next()) {
                int movieId = resultSet2.getInt("movie_id");
                String movieTitle = resultSet2.getString("title");
                movieMap.put(movieId, movieTitle);  // Mapping movie ID to its title
            }

            // Fetch booking details
            String bookingQuery = "SELECT customer_name, movie_id, quantity, total_price FROM tickets";
            ResultSet resultSet = statement.executeQuery(bookingQuery);

            // Process each booking and append to the bookingsData
            while (resultSet.next()) {
                String customerName = resultSet.getString("customer_name");
                int movieIdInBooking = resultSet.getInt("movie_id");
                int quantity = resultSet.getInt("quantity");
                int totalPrice = resultSet.getInt("total_price");

                // Get the movie title from the map
                String movieTitle = movieMap.get(movieIdInBooking);

                // Append the booking details
                if (movieTitle != null) {
                    bookingsData.append("Customer: ").append(customerName)
                            .append(" | Movie: ").append(movieTitle)
                            .append(" | Quantity: ").append(quantity)
                            .append(" | Total Price: $").append(totalPrice)
                            .append("\n");
                }
            }

        } catch (SQLException e) {
            bookingsData.append("Error fetching bookings from database!\n");
            e.printStackTrace();
        }

        if (bookingsData.length() == 0) {
            bookingsData.append("No bookings found in the database.");
        }

        return bookingsData.toString();
    }

    public static void main(String[] args) {
        new VIEWBOOKINGS();
    }
}

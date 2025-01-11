import javax.swing.*;
import java.sql.*;

public class ViewMoviesGUI extends JFrame {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/movie_managment";
    private static final String USER = "root";
    private static final String PASSWORD = "4488";

    public ViewMoviesGUI() {
        setTitle("View Movies");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Movies List:");
        titleLabel.setBounds(10, 20, 150, 25);
        panel.add(titleLabel);

        JTextArea moviesList = new JTextArea();
        moviesList.setBounds(10, 50, 360, 300);
        moviesList.setEditable(false);

        // Fetch movies from the database
        String moviesData = fetchMoviesFromDatabase();
        moviesList.setText(moviesData);

        JScrollPane scrollPane = new JScrollPane(moviesList);
        scrollPane.setBounds(10, 50, 360, 300);
        panel.add(scrollPane);
    }

    private String fetchMoviesFromDatabase() {
        StringBuilder moviesData = new StringBuilder();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT title, duration, ticket_price, available_seats FROM movies")) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                int duration = resultSet.getInt("duration");
                double ticketPrice = resultSet.getDouble("ticket_price");
                int availableSeats = resultSet.getInt("available_seats");

                moviesData.append("Title: ").append(title)
                          .append(" | Duration: ").append(duration).append(" mins")
                          .append(" | Price: $").append(ticketPrice)
                          .append(" | Seats: ").append(availableSeats).append(" available\n");
            }
        } catch (SQLException e) {
            moviesData.append("Error fetching movies from database!\n");
            e.printStackTrace();
        }

        if (moviesData.length() == 0) {
            moviesData.append("No movies found in the database.");
        }

        return moviesData.toString();
    }

    public static void main(String[] args) {
        new ViewMoviesGUI();
    }
}

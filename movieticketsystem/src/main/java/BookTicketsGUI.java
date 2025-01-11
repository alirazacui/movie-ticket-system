import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BookTicketsGUI extends JFrame {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/movie_managment";
    private static final String USER = "root";
    private static final String PASSWORD = "4488";

    public BookTicketsGUI() {
        setTitle("Book Tickets");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Customer Name
        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setBounds(10, 20, 120, 25);
        panel.add(nameLabel);

        JTextField nameText = new JTextField(20);
        nameText.setBounds(150, 20, 200, 25);
        panel.add(nameText);

        // Movie Selection
        JLabel movieLabel = new JLabel("Select Movie:");
        movieLabel.setBounds(10, 60, 120, 25);
        panel.add(movieLabel);

        JComboBox<String> movieDropdown = new JComboBox<>();
        movieDropdown.setBounds(150, 60, 250, 25);
        panel.add(movieDropdown);

        // No. of Tickets
        JLabel ticketsLabel = new JLabel("No. of Tickets:");
        ticketsLabel.setBounds(10, 100, 120, 25);
        panel.add(ticketsLabel);

        JTextField ticketsText = new JTextField(20);
        ticketsText.setBounds(150, 100, 200, 25);
        panel.add(ticketsText);

        // Total Bill
        JLabel totalBillLabel = new JLabel("Total Bill: $0");
        totalBillLabel.setBounds(10, 140, 200, 25);
        panel.add(totalBillLabel);

        // Buttons
        JButton bookButton = new JButton("Book Tickets");
        bookButton.setBounds(10, 180, 150, 25);
        panel.add(bookButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(180, 180, 150, 25);
        panel.add(backButton);

        // Fetch movies and populate dropdown
        populateMovies(movieDropdown);

        // Event Listeners
        ticketsText.addActionListener(e -> calculateTotal(movieDropdown, ticketsText, totalBillLabel));

        bookButton.addActionListener(e -> {
            String customerName = nameText.getText();
            String selectedMovie = (String) movieDropdown.getSelectedItem();
            String tickets = ticketsText.getText();

            if (customerName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Customer name cannot be empty!");
            } else if (selectedMovie == null) {
                JOptionPane.showMessageDialog(null, "Please select a movie!");
            } else {
                try {
                    int numTickets = Integer.parseInt(tickets);
                    if (numTickets <= 0) {
                        JOptionPane.showMessageDialog(null, "Enter a valid number of tickets!");
                        return;
                    }

                    String[] movieDetails = selectedMovie.split(" - ");
                    int movieId = Integer.parseInt(movieDetails[0]);
                    String movieName = movieDetails[1];
                    double ticketPrice = Double.parseDouble(movieDetails[2].substring(1));
                    int availableSeats = Integer.parseInt(movieDetails[3].split(" ")[0]);

                    if (numTickets > availableSeats) {
                        JOptionPane.showMessageDialog(null, "Not enough seats available!");
                    } else {
                        double totalPrice = ticketPrice * numTickets;
                        bookTicketsInDatabase(customerName, movieId, numTickets, totalPrice, availableSeats - numTickets);

                        JOptionPane.showMessageDialog(null, "Tickets Booked Successfully!\n" +
                                "Customer: " + customerName + "\n" +
                                "Movie: " + movieName + "\n" +
                                "No. of Tickets: " + numTickets + "\n" +
                                "Total Bill: $" + totalPrice);

                        dispose();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Enter a valid number of tickets!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error processing the booking!");
                    ex.printStackTrace();
                }
            }
        });

        backButton.addActionListener(e -> dispose());
    }

    private void calculateTotal(JComboBox<String> movieDropdown, JTextField ticketsText, JLabel totalBillLabel) {
        String selectedMovie = (String) movieDropdown.getSelectedItem();
        if (selectedMovie != null && !ticketsText.getText().isEmpty()) {
            try {
                String[] movieDetails = selectedMovie.split(" - ");
                double ticketPrice = Double.parseDouble(movieDetails[2].substring(1));
                int numTickets = Integer.parseInt(ticketsText.getText());

                double totalBill = ticketPrice * numTickets;
                totalBillLabel.setText("Total Bill: $" + totalBill);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number of tickets!");
            }
        }
    }

    private void populateMovies(JComboBox<String> movieDropdown) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT movie_id, title, ticket_price, available_seats FROM movies")) {

            while (resultSet.next()) {
                int movieId = resultSet.getInt("movie_id");
                String title = resultSet.getString("title");
                double price = resultSet.getDouble("ticket_price");
                int seats = resultSet.getInt("available_seats");

                movieDropdown.addItem(movieId + " - " + title + " - $" + price + " - " + seats + " seats");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching movie data from the database!");
            e.printStackTrace();
        }
    }

    private void bookTicketsInDatabase(String customerName, int movieId, int quantity, double totalPrice, int remainingSeats) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Update available seats in movies table
            try (PreparedStatement updateMovieStmt = connection.prepareStatement(
                    "UPDATE movies SET available_seats = ? WHERE movie_id = ?")) {
                updateMovieStmt.setInt(1, remainingSeats);
                updateMovieStmt.setInt(2, movieId);
                updateMovieStmt.executeUpdate();
            }

            // Insert booking details into tickets table
            try (PreparedStatement insertTicketStmt = connection.prepareStatement(
                    "INSERT INTO tickets (customer_name, movie_id, quantity, total_price) VALUES (?, ?, ?, ?)")) {
                insertTicketStmt.setString(1, customerName);
                insertTicketStmt.setInt(2, movieId);
                insertTicketStmt.setInt(3, quantity);
                insertTicketStmt.setDouble(4, totalPrice);
                insertTicketStmt.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating database!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookTicketsGUI::new);
    }
}

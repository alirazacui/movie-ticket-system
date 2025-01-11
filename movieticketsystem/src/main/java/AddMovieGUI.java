import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddMovieGUI extends JFrame {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/movie_managment";
    private static final String USER = "root";
    private static final String PASSWORD = "4488";

    public AddMovieGUI() {
        setTitle("Add Movie");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Movie Title
        JLabel titleLabel = new JLabel("Movie Title:");
        titleLabel.setBounds(10, 20, 100, 25);
        panel.add(titleLabel);

        JTextField titleText = new JTextField(20);
        titleText.setBounds(150, 20, 200, 25);
        panel.add(titleText);

        // Movie Duration
        JLabel durationLabel = new JLabel("Duration (mins):");
        durationLabel.setBounds(10, 60, 120, 25);
        panel.add(durationLabel);

        JTextField durationText = new JTextField(20);
        durationText.setBounds(150, 60, 200, 25);
        panel.add(durationText);

        // Number of Seats
        JLabel seatsLabel = new JLabel("Number of Seats:");
        seatsLabel.setBounds(10, 100, 120, 25);
        panel.add(seatsLabel);

        JTextField seatsText = new JTextField(20);
        seatsText.setBounds(150, 100, 200, 25);
        panel.add(seatsText);

        // Ticket Price
        JLabel priceLabel = new JLabel("Ticket Price:");
        priceLabel.setBounds(10, 140, 120, 25);
        panel.add(priceLabel);

        JTextField priceText = new JTextField(20);
        priceText.setBounds(150, 140, 200, 25);
        panel.add(priceText);

        // Save Button
        JButton saveButton = new JButton("Save Movie");
        saveButton.setBounds(150, 200, 120, 25);
        panel.add(saveButton);

        // Action Listener for Save Button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleText.getText();
                String duration = durationText.getText();
                String seats = seatsText.getText();
                String price = priceText.getText();

                // Validate inputs
                if (title.isEmpty() || duration.isEmpty() || seats.isEmpty() || price.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!");
                } else {
                    try {
                        int durationValue = Integer.parseInt(duration);
                        int seatsNum = Integer.parseInt(seats);
                        double ticketPrice = Double.parseDouble(price);

                        // Save to database
                        saveMovieToDatabase(title, durationValue, seatsNum, ticketPrice);

                        // Display a confirmation message
                        JOptionPane.showMessageDialog(null,
                                "Movie added successfully:\nTitle: " + title +
                                        "\nDuration: " + durationValue + " mins" +
                                        "\nSeats: " + seatsNum +
                                        "\nPrice: $" + ticketPrice);

                        // Close the Add Movie GUI
                        dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Duration, Seats, and Price must be numeric values!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error saving movie to the database!");
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    // Method to save movie data into the database
    private void saveMovieToDatabase(String title, int duration, int seats, double price) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // SQL insert query
            String sql = "INSERT INTO movies (title, duration, available_seats, ticket_price) VALUES (?, ?, ?, ?)";

            // Prepare the statement
            statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setInt(2, duration);
            statement.setInt(3, seats);
            statement.setDouble(4, price);

            // Execute the statement
            statement.executeUpdate();
        } finally {
            // Close resources
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void main(String[] args) {
        // Launch the GUI
        new AddMovieGUI();
    }
}

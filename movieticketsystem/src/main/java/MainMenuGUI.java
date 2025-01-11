import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuGUI extends JFrame {
    public MainMenuGUI() {
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Movie Ticket System");
        titleLabel.setBounds(120, 20, 200, 25);
        panel.add(titleLabel);

        JButton addMovieButton = new JButton("Add Movie");
        addMovieButton.setBounds(120, 60, 150, 25);
        panel.add(addMovieButton);

        JButton viewMoviesButton = new JButton("View Movies");
        viewMoviesButton.setBounds(120, 100, 150, 25);
        panel.add(viewMoviesButton);

        JButton bookTicketsButton = new JButton("Book Tickets");
        bookTicketsButton.setBounds(120, 140, 150, 25);
        panel.add(bookTicketsButton);

        JButton viewBookingsButton = new JButton("View Bookings");
        viewBookingsButton.setBounds(120, 180, 150, 25);
        panel.add(viewBookingsButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(120, 220, 150, 25);
        panel.add(exitButton);

        // Add Action Listeners for each button
        addMovieButton.addActionListener(e -> new AddMovieGUI());
        viewMoviesButton.addActionListener(e -> new ViewMoviesGUI());
        bookTicketsButton.addActionListener(e -> new BookTicketsGUI());
        viewBookingsButton.addActionListener(e -> new VIEWBOOKINGS()); // Launch VIEWBOOKINGS screen
        exitButton.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        // Launch the MainMenuGUI
        new MainMenuGUI();
    }
}

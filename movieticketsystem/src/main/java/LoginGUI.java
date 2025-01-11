/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author premier
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    public LoginGUI() {
        // Set JFrame properties
        setTitle("Admin Login");
        setSize(500, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create a panel to hold components
        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true); // Show the frame
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                if (username.equals("admin") && password.equals("1234")) { // Match with predefined credentials
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    new MainMenuGUI(); // Navigate to Main Menu
                    dispose(); // Close the login frame
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password!");
                }
            }
        });
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Exit extends JPanel {

    public Exit() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(640, 400));

        // Top panel (yellow)
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(640, 83));
        topPanel.setBackground(new Color(196, 200, 16)); // #c4c810
        add(topPanel, BorderLayout.NORTH);

        // Bottom panel (yellow)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(640, 83));
        bottomPanel.setBackground(new Color(196, 200, 16)); // #c4c810
        add(bottomPanel, BorderLayout.SOUTH);

        // Center panel (light gray)
        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(640, 233));
        centerPanel.setBackground(new Color(208, 215, 217)); // #D0D7D9
        centerPanel.setLayout(null);
        add(centerPanel, BorderLayout.CENTER);

        // Add components to center panel
        JLabel authLabel = new JLabel("Автентифікація");
        authLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        authLabel.setBounds(266, 28, 202, 20);
        centerPanel.add(authLabel);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        emailLabel.setBounds(236, 70, 100, 20);
        centerPanel.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(284, 66, 150, 30);
        centerPanel.add(emailField);

        JLabel passwordLabel = new JLabel("Пароль");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passwordLabel.setBounds(236, 108, 100, 20);
        centerPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(284, 104, 150, 30);
        centerPanel.add(passwordField);

        JButton loginButton = new JButton("Увійти");
        loginButton.setBounds(284, 143, 150, 30);
        centerPanel.add(loginButton);

        // Add action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                // Check if credentials match
                if (email.equals("admin") && password.equals("admin")) {
                    // Open the admin panel
                    JFrame adminFrame = new JFrame("Admin Panel");
                    adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    adminFrame.getContentPane().add(new Admin()); // Assuming you have an AdminPanel class
                    adminFrame.setSize(820, 600);
                    adminFrame.setVisible(true);
                    // Close the login window
                    SwingUtilities.getWindowAncestor(Exit.this).dispose();
                } else {
                    // Show error message
                    JOptionPane.showMessageDialog(Exit.this, "Неправильний логін або пароль.", "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Exit());
        frame.pack();
        frame.setVisible(true);
    }
}

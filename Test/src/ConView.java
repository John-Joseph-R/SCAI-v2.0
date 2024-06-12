import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ConView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Connection conn;
    private JTable contactTable;

    public ConView() {
        super("Contact Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                if (validateUser(username, password)) {
                    showContacts(username);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        add(loginPanel);
    }

    private boolean validateUser(String username, String password) {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/scai", "root", "chrisjohn98");
            String query = "SELECT * FROM user WHERE name=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void showContacts(String username) {
        try {
            String query = "SELECT * FROM contact WHERE UserID=(SELECT UserID FROM user WHERE name=?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            // Build JTable with ResultSet
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }
            Object[][] data = new Object[100][columnCount]; // Assuming a maximum of 100 contacts
            int rowCount = 0;
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    data[rowCount][i - 1] = rs.getObject(i);
                }
                rowCount++;
            }

            contactTable = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(contactTable);

            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.add(scrollPane, BorderLayout.CENTER);

            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Dispose of the FAQ frame
                    dispose();
                    
                    // Execute SeniorCitizenAssistanceInterface.java
                    SeniorCitizenAssistanceInterface.main(new String[]{});
                }
            });
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(okButton);
            tablePanel.add(buttonPanel, BorderLayout.SOUTH);

            getContentPane().removeAll();
            getContentPane().add(tablePanel);
            revalidate();
            repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                if (validateUser(username, password)) {
                    showContacts(username);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        getContentPane().removeAll();
        getContentPane().add(loginPanel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ConView view = new ConView();
                view.setVisible(true);
            }
        });
    }
}

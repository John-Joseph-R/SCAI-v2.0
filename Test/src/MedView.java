import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MedView extends JFrame {
    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public MedView() {
        setTitle("MedView Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Name:");
        JLabel passwordLabel = new JLabel("Password:");

        nameField = new JTextField();
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        add(nameLabel);
        add(nameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        setVisible(true);
    }

    private void loginUser() {
        String name = nameField.getText();
        String password = new String(passwordField.getPassword());
    
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/scai", "root", "chrisjohn98");
            String query = "SELECT UserID FROM user WHERE name=? AND password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int userID = resultSet.getInt("UserID");
                displayMedicineTable(userID);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
    
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Close the current window and execute SeniorCitizenAssistanceInterface.java
        SwingUtilities.invokeLater(() -> {
            dispose(); // Close the current window
            SeniorCitizenAssistanceInterface.main(new String[0]); // Execute SeniorCitizenAssistanceInterface.java
        });
    }
    

    private void displayMedicineTable(int userID) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/scai", "root", "chrisjohn98");
            String query = "SELECT * FROM medicine WHERE UserID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            JTable table = new JTable(buildTableModel(resultSet));
            JOptionPane.showMessageDialog(null, new JScrollPane(table), "Medicine Table", JOptionPane.PLAIN_MESSAGE);

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error displaying medicine table. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        DefaultTableModel tableModel = new DefaultTableModel();

        // Add column names to the model
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            tableModel.addColumn(metaData.getColumnName(columnIndex));
        }

        // Add rows to the model
        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                rowData[columnIndex - 1] = resultSet.getObject(columnIndex);
            }
            tableModel.addRow(rowData);
        }

        return tableModel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MedView());
    }
}

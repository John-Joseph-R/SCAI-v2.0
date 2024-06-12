import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class MedEdit extends JFrame implements ActionListener {
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/scai";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "chrisjohn98";

    private JTextField userIdField, medNameField, dosageField, typeField, timeField;
    private JButton insertButton, viewButton;

    public MedEdit() {
        setTitle("Insert Medicine");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        JLabel userIdLabel = new JLabel("User ID:");
        add(userIdLabel);
        userIdField = new JTextField();
        add(userIdField);

        JLabel medNameLabel = new JLabel("Medicine Name:");
        add(medNameLabel);
        medNameField = new JTextField();
        add(medNameField);

        JLabel dosageLabel = new JLabel("Dosage (mg):");
        add(dosageLabel);
        dosageField = new JTextField();
        add(dosageField);

        JLabel typeLabel = new JLabel("Type:");
        add(typeLabel);
        typeField = new JTextField();
        add(typeField);

        JLabel timeLabel = new JLabel("Time:");
        add(timeLabel);
        timeField = new JTextField();
        add(timeField);

        insertButton = new JButton("Insert");
        insertButton.addActionListener(this);
        add(insertButton);

        viewButton = new JButton("View");
        viewButton.addActionListener(this);
        add(viewButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insertButton) {
            // Insert button clicked
            String userId = userIdField.getText();
            String medName = medNameField.getText();
            String dosage = dosageField.getText();
            String type = typeField.getText();
            String time = timeField.getText();

            // Perform the insertion into the database
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                String query = "INSERT INTO medicine (UserID, med_name, dosage_in_mg, type, time) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, userId);
                    statement.setString(2, medName);
                    statement.setString(3, dosage);
                    statement.setString(4, type);
                    statement.setString(5, time);
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this, "Medicine inserted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to insert medicine.");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error inserting medicine: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == viewButton) {
            // View button clicked
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                String query = "SELECT * FROM medicine";
                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery(query);
                    // Create a new JFrame to display the table
                    JFrame frame = new JFrame("Medicine Table");
                    JTable table = new JTable(buildTableModel(resultSet));
                    JScrollPane scrollPane = new JScrollPane(table);
                    frame.add(scrollPane, BorderLayout.CENTER);
                    frame.setSize(600, 400);
                    frame.setVisible(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error viewing medicine: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Helper method to build a table model from ResultSet (same as before)
    public DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        // Column names
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        // Data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(resultSet.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedEdit::new);
    }
}

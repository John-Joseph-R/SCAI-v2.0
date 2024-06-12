import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class ConEdit extends JFrame implements ActionListener {
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/scai";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "chrisjohn98";

    private JTextField userIDField, conNameField, phNoField;
    private JButton insertButton, viewButton;

    public ConEdit() {
        setTitle("Insert Contact");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        JLabel userIDLabel = new JLabel("UserID:");
        add(userIDLabel);
        userIDField = new JTextField();
        add(userIDField);

        JLabel conNameLabel = new JLabel("Contact Name:");
        add(conNameLabel);
        conNameField = new JTextField();
        add(conNameField);

        JLabel phNoLabel = new JLabel("Phone Number:");
        add(phNoLabel);
        phNoField = new JTextField();
        add(phNoField);

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
            int userID = Integer.parseInt(userIDField.getText());
            String conName = conNameField.getText();
            String phNo = phNoField.getText();

            // Insertion into the database
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                String query = "INSERT INTO contact (UserID, con_name, ph_no) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, userID);
                    preparedStatement.setString(2, conName);
                    preparedStatement.setString(3, phNo);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Contact inserted successfully");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error inserting contact: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == viewButton) {
            // View button clicked
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                String query = "SELECT * FROM contact";
                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery(query);
                    // Create a new JFrame to display the table
                    JFrame frame = new JFrame("Contact Table");
                    JTable table = new JTable(buildTableModel(resultSet));
                    JScrollPane scrollPane = new JScrollPane(table);
                    frame.add(scrollPane, BorderLayout.CENTER);
                    frame.setSize(400, 300);
                    frame.setVisible(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error viewing contact: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Helper method to build a table model from ResultSet
    public static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
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
        SwingUtilities.invokeLater(ConEdit::new);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminPanel extends JFrame {
    private JLabel timeLabel;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Changed to DISPOSE_ON_CLOSE
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Time label
        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        updateTime();
        add(timeLabel, BorderLayout.NORTH);

        // Title label
        JLabel titleLabel = new JLabel("Admin Panel");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        JButton medicineButton = new JButton("Medicine");
        JButton registrationButton = new JButton("Registration");
        JButton contactButton = new JButton("Contact");

        // Add ActionListeners to buttons
        medicineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open MedView.java here
                new MedEdit();
            }
        });

        registrationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open MedView.java here
                new Reg();
            }
        });

        contactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open MedView.java here
                new ConEdit();
            }
        });

        buttonPanel.add(medicineButton);
        buttonPanel.add(registrationButton);
        buttonPanel.add(contactButton);

        // Set all buttons to have the same width
        Dimension buttonSize = new Dimension(130, 40);
        medicineButton.setPreferredSize(buttonSize);
        contactButton.setPreferredSize(buttonSize);
        registrationButton.setPreferredSize(buttonSize);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        Timer timer = new Timer(1000, e -> {
            Calendar cal = Calendar.getInstance();
            String time = sdf.format(cal.getTime());
            timeLabel.setText(time);
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminPanel::new);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SeniorCitizenAssistanceInterface extends JFrame {
    private JLabel timeLabel;

    public SeniorCitizenAssistanceInterface() {
        setTitle("Senior Citizen Assistance Interface");
        setSize(400, 300);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Time label
        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        updateTime();
        add(timeLabel, BorderLayout.NORTH);

        // Title label
        JLabel titleLabel = new JLabel("SCAI");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        JButton medicineButton = new JButton("Medicine");
        JButton contactButton = new JButton("Contact");
        buttonPanel.add(medicineButton);
        buttonPanel.add(contactButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // ActionListener for the "Medicine" button
        medicineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open MedView.java here
                new MedView();
                dispose(); // Close the window
            }
        });

        // ActionListener for the "Contact" button
        contactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open ConView.java here
                ConView conView = new ConView();
                conView.setVisible(true); // Make the ConView visible
                dispose(); // Close the window
            }
        });


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
        SwingUtilities.invokeLater(SeniorCitizenAssistanceInterface::new);
    }
}

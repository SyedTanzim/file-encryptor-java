package src.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import src.logic.EncryptorEngine;

/**
 * The MainWindow class creates the graphical user interface (GUI) for the application.
 * It allows the user to select a file, enter a password, and click buttons to encrypt or decrypt.
 */
public class MainWindow extends JFrame {
    
    // UI components that need to be accessed by multiple methods in this class
    private JTextField filePathField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    /**
     * Constructor for MainWindow. It sets up the window size, layout, and all internal UI components.
     */
    public MainWindow() {
        // Set the title of the window
        setTitle("File Encryptor/Decryptor");
        
        // Set the size of the window (width: 500 pixels, height: 300 pixels)
        setSize(500, 300);
        
        // Ensure the application stops running when the window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Center the window on the screen
        setLocationRelativeTo(null);
        
        // Use a GridLayout with 4 rows and 1 column to stack panels vertically
        setLayout(new GridLayout(4, 1));

        // --- Row 1: File selection area ---
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fileLabel = new JLabel("Selected File:");
        
        // Create a text field to show the selected file path (read-only)
        filePathField = new JTextField(20);
        filePathField.setEditable(false);
        
        // Create a button to open the file browser
        JButton browseButton = new JButton("Browse File");
        
        // Add an action listener to tell the button what to do when clicked
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a standard file chooser dialog
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(MainWindow.this);
                
                // If the user selects a file and clicks "Open"
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Display the selected file's absolute path in the text field
                    filePathField.setText(selectedFile.getAbsolutePath());
                    // Update the status label
                    statusLabel.setText("Status: File selected.");
                    statusLabel.setForeground(Color.BLACK);
                }
            }
        });
        
        // Add the components to the first row's panel
        filePanel.add(fileLabel);
        filePanel.add(filePathField);
        filePanel.add(browseButton);

        // --- Row 2: Password entry area ---
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Password:");
        
        // Create a password field which masks the typed characters
        passwordField = new JPasswordField(20);
        
        // Add components to the second row's panel
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        // --- Row 3: Action Buttons area ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Create the Encrypt and Decrypt buttons
        JButton encryptButton = new JButton("Encrypt");
        JButton decryptButton = new JButton("Decrypt");
        
        // Set what happens when "Encrypt" is clicked
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call our helper method in encryption mode (true)
                processFile(true);
            }
        });
        
        // Set what happens when "Decrypt" is clicked
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call our helper method in decryption mode (false)
                processFile(false);
            }
        });
        
        // Add buttons to the third row's panel
        actionPanel.add(encryptButton);
        actionPanel.add(decryptButton);

        // --- Row 4: Status display area ---
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Create a label to show the user what is happening
        statusLabel = new JLabel("Status: Ready");
        statusPanel.add(statusLabel);

        // Add all 4 rows (panels) to the main window frame
        add(filePanel);
        add(passwordPanel);
        add(actionPanel);
        add(statusPanel);
    }

    /**
     * A helper method that reads the UI fields and triggers the encryption or decryption process.
     * 
     * @param isEncrypt true if the user wants to encrypt the file, false for decrypt.
     */
    private void processFile(boolean isEncrypt) {
        // Read the currently selected file path
        String filePath = filePathField.getText();
        
        // Read the password typed by the user
        String password = new String(passwordField.getPassword());

        // Validate that a file has been selected
        if (filePath == null || filePath.trim().isEmpty()) {
            statusLabel.setText("Status: Error - Please select a file.");
            statusLabel.setForeground(Color.RED);
            return; // Stop processing
        }

        // Validate that a password has been entered
        if (password == null || password.trim().isEmpty()) {
            statusLabel.setText("Status: Error - Please enter a password.");
            statusLabel.setForeground(Color.RED);
            return; // Stop processing
        }

        try {
            // Check if we need to encrypt or decrypt based on the button clicked
            if (isEncrypt) {
                // Call the logic engine to perform encryption
                EncryptorEngine.encrypt(filePath, password);
                statusLabel.setText("Status: Encryption successful.");
            } else {
                // Call the logic engine to perform decryption
                EncryptorEngine.decrypt(filePath, password);
                statusLabel.setText("Status: Decryption successful.");
            }
            
            // Set the success message color to dark green
            statusLabel.setForeground(new Color(0, 153, 0)); 
        } catch (Exception ex) {
            // If anything goes wrong, catch the error and display its message in red
            statusLabel.setText("Status: Error - " + ex.getMessage());
            statusLabel.setForeground(Color.RED);
        } finally {
            // Finally block ensures the password field is always cleared, 
            // whether the operation succeeds or fails.
            passwordField.setText("");
        }
    }

    /**
     * An alternative main method to test the UI independently.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Run the UI creation on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }
}

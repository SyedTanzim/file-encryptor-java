package src;

import src.ui.MainWindow;
import javax.swing.SwingUtilities;

/**
 * The Main class serves as the entry point for the File Encryptor application.
 * It is responsible for starting the program and launching the graphical user interface.
 */
public class Main {
    
    /**
     * The main method is the first method that gets called when the application runs.
     * 
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // SwingUtilities.invokeLater is used to ensure that the GUI is created and updated
        // on the special "Event Dispatch Thread" which is required for Swing applications.
        SwingUtilities.invokeLater(() -> {
            // Create a new instance of our main window
            MainWindow window = new MainWindow();
            // Make the window visible on the screen
            window.setVisible(true);
        });
    }
}
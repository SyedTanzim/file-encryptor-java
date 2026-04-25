package src.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The FileHandler class provides helper methods for reading, writing, and deleting files.
 * It acts as a utility class containing only static methods for basic file operations.
 */
public class FileHandler {

    /**
     * Reads all bytes from a given file path into a byte array.
     * 
     * @param filePath The path of the file to read.
     * @return A byte array containing the entire file's data.
     * @throws IOException If the file is not found, is too large, or cannot be completely read.
     */
    public static byte[] readBytes(String filePath) throws IOException {
        // Create a File object representing the file on the disk
        File file = new File(filePath);
        
        // Check if the file actually exists
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        // Get the total size of the file in bytes
        long length = file.length();
        
        // Check if the file is too large to fit in a standard Java byte array
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File is too large to read into a byte array.");
        }

        // Create a byte array to hold the file's data
        byte[] data = new byte[(int) length];

        // Open a stream to read the file; try-with-resources automatically closes it when done
        try (FileInputStream fis = new FileInputStream(file)) {
            int offset = 0;
            int numRead = 0;
            
            // Keep reading chunks of data until the entire array is filled
            while (offset < data.length
                   && (numRead = fis.read(data, offset, data.length - offset)) >= 0) {
                offset += numRead; // Update our position in the array
            }

            // If we couldn't read all the expected bytes, throw an error
            if (offset < data.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
        }

        // Return the file data
        return data;
    }

    /**
     * Writes a given byte array to a file at the specified path.
     * 
     * @param filePath The path where the file should be saved.
     * @param data The byte array to write to the file.
     * @throws IOException If an error occurs during writing.
     */
    public static void writeBytes(String filePath, byte[] data) throws IOException {
        // Create a File object representing where we want to save the data
        File file = new File(filePath);
        
        // Open a stream to write to the file; try-with-resources automatically closes it
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Write all the data into the file
            fos.write(data);
        }
    }

    /**
     * Deletes a file at the specified path.
     * 
     * @param filePath The path of the file to delete.
     * @return true if the file was successfully deleted; false otherwise.
     */
    public static boolean deleteFile(String filePath) {
        // Create a File object for the target file
        File file = new File(filePath);
        
        // If the file exists, attempt to delete it and return the result
        if (file.exists()) {
            return file.delete();
        }
        
        // If the file doesn't exist, we can't delete it
        return false;
    }
}

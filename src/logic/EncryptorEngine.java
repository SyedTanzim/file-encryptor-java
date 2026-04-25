package src.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The EncryptorEngine class contains the core business logic for the application.
 * It handles the encryption and decryption of files using the XOR algorithm
 * and verifies passwords to prevent accidental corruption.
 */
public class EncryptorEngine {

    /**
     * Encrypts a file using the given password and deletes the original file upon success.
     * The new file will have a ".enc" extension.
     * 
     * @param filePath The absolute path of the file to encrypt.
     * @param password The password used to secure the file.
     * @throws IOException If file reading/writing fails or if the original file cannot be deleted.
     */
    public static void encrypt(String filePath, String password) throws IOException {
        // Create a File object for the input file
        File inputFile = new File(filePath);
        
        // Check if the file exists before attempting to encrypt
        if (!inputFile.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        // Define the output file path by appending ".enc" to the original name
        String outputFilePath = filePath + ".enc";
        File outputFile = new File(outputFilePath);

        // Process the file in "encryption mode" (true)
        processFile(inputFile, outputFile, password, true);

        // Delete the original unprotected file after a successful encryption
        if (!inputFile.delete()) {
            throw new IOException("Failed to delete original file after encryption.");
        }
    }

    /**
     * Decrypts a ".enc" file using the given password and deletes the encrypted file upon success.
     * The new file will have the ".enc" extension removed.
     * 
     * @param filePath The absolute path of the file to decrypt.
     * @param password The password required to unlock the file.
     * @throws IOException If file reading/writing fails, the password is wrong, or deletion fails.
     */
    public static void decrypt(String filePath, String password) throws IOException {
        // Create a File object for the input file
        File inputFile = new File(filePath);
        
        // Check if the file exists before attempting to decrypt
        if (!inputFile.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        // Ensure the user is trying to decrypt a file that actually has the .enc extension
        if (!filePath.endsWith(".enc")) {
            throw new IllegalArgumentException("File must have a .enc extension to decrypt.");
        }

        // Determine the output file path by removing the last 4 characters (".enc")
        String outputFilePath = filePath.substring(0, filePath.length() - 4);
        File outputFile = new File(outputFilePath);

        // Process the file in "decryption mode" (false)
        processFile(inputFile, outputFile, password, false);

        // Delete the encrypted file after a successful decryption
        if (!inputFile.delete()) {
            throw new IOException("Failed to delete encrypted file after decryption.");
        }
    }

    /**
     * A private helper method that performs the actual reading, XOR transformation, and writing.
     * It handles both encryption and decryption based on the isEncrypt flag.
     * 
     * @param inputFile The file to read data from.
     * @param outputFile The file to write transformed data to.
     * @param password The password used for the XOR cipher.
     * @param isEncrypt A boolean flag: true for encryption, false for decryption.
     * @throws IOException If an error occurs during file I/O or if the password verification fails.
     */
    private static void processFile(File inputFile, File outputFile, String password, boolean isEncrypt) throws IOException {
        // Ensure the password is not null or empty
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        // Convert the password string into an array of bytes
        byte[] passwordBytes = password.getBytes();
        int passwordLength = passwordBytes.length;

        // Step 1: Open ONLY the input file first to read the data
        try (FileInputStream fis = new FileInputStream(inputFile)) {

            // If we are decrypting, we must first verify the password before creating an output file
            if (!isEncrypt) {
                // Prepare an array to read the 9-byte magic header
                byte[] magic = new byte[9];
                
                // Read 9 bytes and check if they exactly match "ENCVERIFY"
                if (fis.read(magic) != 9 || !"ENCVERIFY".equals(new String(magic))) {
                    throw new IOException("Wrong password! Cannot decrypt file.");
                }
                
                // Prepare an array to read the 4-byte password length
                byte[] lenBytes = new byte[4];
                
                // Read 4 bytes containing the length
                if (fis.read(lenBytes) != 4) {
                    throw new IOException("Wrong password! Cannot decrypt file.");
                }
                
                // Reconstruct the integer length from the 4 bytes using bitwise operations
                int storedLen = ((lenBytes[0] & 0xFF) << 24) |
                                ((lenBytes[1] & 0xFF) << 16) |
                                ((lenBytes[2] & 0xFF) << 8) |
                                (lenBytes[3] & 0xFF);
                                
                // Check if the stored password length matches the provided password length
                if (storedLen != passwordLength) {
                    throw new IOException("Wrong password! Cannot decrypt file.");
                }
            }

            // Step 2: Now that verification is complete, open the output file for writing
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                
                // If we are encrypting, we need to write the verification header to the new file
                if (isEncrypt) {
                    // Write the magic word "ENCVERIFY"
                    fos.write("ENCVERIFY".getBytes());
                    
                    // Write the password length as 4 separate bytes using bitwise shifts
                    fos.write(new byte[] {
                        (byte)(passwordLength >>> 24),
                        (byte)(passwordLength >>> 16),
                        (byte)(passwordLength >>> 8),
                        (byte)passwordLength
                    });
                }

                // Prepare a buffer (a temporary holding area) to process the file in 8KB chunks
                byte[] buffer = new byte[8192];
                int bytesRead;
                long totalBytesProcessed = 0;

                // Read chunks of data from the input file until we reach the end (-1)
                while ((bytesRead = fis.read(buffer)) != -1) {
                    // Loop through every byte in the current chunk
                    for (int i = 0; i < bytesRead; i++) {
                        // Find which byte of the password we should use for this specific byte
                        int passwordIndex = (int) ((totalBytesProcessed + i) % passwordLength);
                        
                        // Transform the byte by performing an XOR operation (^) with the password byte.
                        // XOR is reversible: (Data ^ Password) ^ Password = Data
                        buffer[i] = (byte) (buffer[i] ^ passwordBytes[passwordIndex]);
                    }
                    
                    // Write the transformed chunk to the output file
                    fos.write(buffer, 0, bytesRead);
                    
                    // Keep track of how many bytes we've processed so the password index stays correct
                    totalBytesProcessed += bytesRead;
                }
            }
        }
    }
}

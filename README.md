# File Encryptor

File Encryptor is a simple, beginner-friendly desktop application built in Java that allows users to securely encrypt and decrypt their personal files. The application uses a password-based XOR encryption algorithm and features an easy-to-use graphical interface. 

## Features
- **Secure File Encryption:** Protect any file by providing a secret password.
- **Easy File Decryption:** Restore your original files seamlessly with the correct password.
- **Password Verification:** Includes a secure header verification system to prevent files from being accidentally corrupted by a wrong password.
- **Auto-Cleanup:** Original files are safely deleted after successful encryption/decryption, leaving behind only the desired file.
- **User-Friendly GUI:** Simple graphical window to browse files, enter passwords, and see real-time status updates.

## How to Compile and Run

1. Open your terminal or command prompt.
2. Navigate to the root folder of the project (`FileEncryptor`).
3. Compile the code using the `javac` compiler and place the outputs in the `out` directory:
   ```bash
   javac -d out src/Main.java src/ui/MainWindow.java src/logic/EncryptorEngine.java src/utils/FileHandler.java
   ```
4. Run the application:
   ```bash
   java -cp out src.Main
   ```

## How to Use the App (Step-by-Step)

1. Launch the application using the commands above.
2. Click the **Browse File** button to open the file picker and select the file you want to encrypt or decrypt.
3. Type a secret password in the **Password** field.
4. Click **Encrypt** to protect your file, or click **Decrypt** to unlock a `.enc` file.
5. The application will process the file, update the status at the bottom of the window, and automatically clear your password for safety.

## Project Folder Structure

```
FileEncryptor/
│
├── README.md                 # Project documentation
├── out/                      # Compiled .class files (generated after running javac)
└── src/                      # Java source code files
    ├── Main.java             # Entry point of the program
    ├── ui/
    │   └── MainWindow.java   # Contains the Swing GUI code
    ├── logic/
    │   └── EncryptorEngine.java # Handles XOR encryption/decryption and password checks
    └── utils/
        └── FileHandler.java  # Helper class for basic file reading/writing
```

## Technologies Used
- **Java:** Core programming language.
- **Java Swing:** Used to build the graphical user interface.
- **XOR Encryption:** A symmetric encryption cipher that transforms file data byte-by-byte using the user's password.

## What Each File Does
- **`src/Main.java`**: The starting point of the application. It ensures the graphical interface starts safely on the correct thread.
- **`src/ui/MainWindow.java`**: Defines the buttons, text fields, and layout of the app. It also listens for user clicks and tells the engine what to do.
- **`src/logic/EncryptorEngine.java`**: The "brain" of the app. It reads files chunk-by-chunk, applies the XOR cipher, manages the `.enc` file extensions, and verifies the password header.
- **`src/utils/FileHandler.java`**: A utility tool for low-level file operations (like converting a whole file into bytes or deleting files).

package in.nextstep.txn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveFileToDesktop {
    
    // Function to append data to the file
    public static void appendDataToFile(String filename, String data) {
        // Get the user's home directory
        String userHome = System.getProperty("user.home");
        String desktopPath = userHome + File.separator + "Desktop";

        // Create a new folder "ReceiptData" on the Desktop
        File folder = new File(desktopPath + File.separator + "ReceiptData");

        // If the folder does not exist, create it
        if (!folder.exists()) {
            boolean folderCreated = folder.mkdir();  // mkdir() creates a directory
            if (folderCreated) {
                System.out.println("Folder 'ReceiptData' created successfully.");
            } else {
                System.out.println("Failed to create the folder.");
            }
        }

        // Define the file path using the provided filename
        File file = new File(folder, filename+".txt");

        // Append the data to the file with proper formatting
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Add a timestamp or any other format you want
         //   String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // Write formatted data with new line
            writer.newLine();  // Start a new line
            writer.write("Testcase Name : " +filename+"\n"+ data);  // Write data with timestamp

            System.out.println("Data appended successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
}

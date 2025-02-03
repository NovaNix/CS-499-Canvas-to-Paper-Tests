/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.csgroup.quizmaker.qti;

import java.io.File;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;


/**
 * This class manages the unzipping of the QTI files and stores the extracted files in a temporary directory. 
 * Returns the path to the temporary directory where the extracted files are stored. 
 * 
 * @author Sarah Singhirunnusorn
 */


public class QTI_ImportManager {
    
    private static final Logger logger = LoggerFactory.getLogger(QTI_ImportManager.class);
    private static Path qtiTempDirectory;      

    /**
     * Extract the QTI files into a temporary directory.
     *
     * @param zipFilePath   The path to the QTI ZIP file.
     * @return              The path to the temporary directory that contains the extracted files.
     */
    public static Path extractQTIFile(String zipFilePath) {
        try {
            File zipFile = new File(zipFilePath);
            if (!zipFile.exists()) {
                logger.error("ERROR!! ZIP file does not exist: {}", zipFilePath);
                return null;
            }

            // Creates a temporary directory
            qtiTempDirectory = Files.createTempDirectory("qti_temp_");
            String extractedFilePath = qtiTempDirectory.toAbsolutePath().toString();
            
            logger.info("Extracting QTI ZIP file to: {}", extractedFilePath);

            
            // Extracts the contents of the QTI ZIP file
            unzipFile(zipFilePath, extractedFilePath);

            logger.info("QTI ZIP file extracted successfully to: {}", extractedFilePath);
            return qtiTempDirectory;    // returns path to the temporary directory
            
        } catch (Exception e) {
            logger.error("ERROR!! QTI ZIP file extraction failed: {}", e.getMessage(), e);
            return null;
        }
    }
    
    
    // Unzip the QTI Zip File
    private static void unzipFile(String zipFilePath, String extractedFilePath) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            zipFile.extractAll(extractedFilePath);
            logger.info("Unzipping success, extracted to: {}", extractedFilePath);
            
        } catch (ZipException e) {
            logger.error("ERROR!! Unzipping failed: {}", e.getMessage(), e);
        }
    }

    
    // Return the path to the temporary directory
    public static Path getTempDirectory() {
        return qtiTempDirectory;
    }
}



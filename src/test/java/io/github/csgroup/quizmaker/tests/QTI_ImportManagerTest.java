/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.csgroup.quizmaker.tests;

import io.github.csgroup.quizmaker.qti.QTI_ImportManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test to verify that the QTI files are unzipped and extracted properly.
 * - Ensures that the ZIP file exists 
 * - Checks that the temporary directory is created and contains the extracted files.
 * - The temporary directories are properly deleted after the tests.
 * 
 * @author Sarah Singhirunnusorn
 */


class QTI_ImportManagerTest {
    
    // path to the test QTI ZIP file
    private static final String TestZIP_PATH = "src/test/resources/cs-214-03-fa21-intro-discrete-structure-quiz-export.zip"; 
    
    // stores the path to the temporary directory
    private Path extractedTestFilePath;
    
    
   /**
    * Setup Method: 
    * - Checks the existence of the ZIP file
    * - Extracts the ZIP file 
    * - Stores the path to the temporary directory for testing
    */
    @BeforeEach
    void setUp() {
        File testZip = new File(TestZIP_PATH);
        System.out.println("Checking the TEST ZIP File at: " + testZip.getAbsolutePath());

        assertTrue(testZip.exists(), "ERROR!! Test ZIP file is missing! File expected at: " + testZip.getAbsolutePath());
        extractedTestFilePath = QTI_ImportManager.extractQTIFile(TestZIP_PATH);
    }
    
    /**
     * Take-down Method:
     * - Deletes the temporary directory and the extracted files
     */
    @AfterEach
    void takeDown() {
        if (extractedTestFilePath != null) {
            deleteTempDirectory(extractedTestFilePath.toFile());
        }
    }
    
    /**
     * Test Method:
     * - Ensures that the path to the temporary directory is valid
     * - Checks that the temporary directory exists
     * - Ensures that the extracted files are in the temporary directory 
     */

    @Test
    void testZipExtraction() {
        assertNotNull(extractedTestFilePath, "ERROR!! Temporary directory path does NOT exist.");
        
        File temporary_directory = extractedTestFilePath.toFile();
        assertTrue(temporary_directory.exists(), "ERROR!! Temporary directory does NOT exist.");
        assertTrue(temporary_directory.isDirectory(), "ERROR!! The extracted path should be a directory.");

        File[] extracted_Files = temporary_directory.listFiles();
        
        assertNotNull(extracted_Files, "ERROR!! No files were extracted.");
        assertTrue(extracted_Files.length > 0, "ERROR!! Temporary directory is EMPTY (contains zero files).");
        
        System.out.println("List of Extracted Files:");
        for (File file : extracted_Files) {
            System.out.println(" - " + file.getName());
        }
    }

    // Deletes the created temporary directory
    private void deleteTempDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteTempDirectory(file);
                }
            }
            directory.delete();
        }
    }
}
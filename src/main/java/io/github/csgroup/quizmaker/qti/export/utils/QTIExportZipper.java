package io.github.csgroup.quizmaker.qti.export.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to zip the QTI files
 * 
 * @author Sarah Singhirunnuson
 */
public class QTIExportZipper 
{
	private static final Logger logger = LoggerFactory.getLogger(QTIExportZipper.class);

	/**
	 * Compresses the given folder and writes the ZIP file to the specified output path.
	 * 
	 * @param sourceFolder the folder to zip
	 * @param outputZip the destination zip file path
	 */
	public static void zipFolder(Path sourceFolder, Path outputZip) throws Exception
	{
		logger.info("Zipping QTI export folder: {}", sourceFolder.toAbsolutePath());

		try (FileOutputStream fos = new FileOutputStream(outputZip.toFile());
			 ZipOutputStream zos = new ZipOutputStream(fos))
		{
			File baseDir = sourceFolder.toFile();
			addFolderToZip(zos, baseDir, baseDir.getAbsolutePath().length() + 1);
		}

		logger.info("QTI export ZIP created at: {}", outputZip.toAbsolutePath());
	}

	
	// Recursively adds files and subdirectories to the zip output.
	private static void addFolderToZip(ZipOutputStream zos, File folder, int basePathLength) throws Exception
	{
		for (File file : folder.listFiles())
		{
			if (file.isDirectory())
			{
				addFolderToZip(zos, file, basePathLength);
			}
			else
			{
				String zipEntryName = file.getAbsolutePath().substring(basePathLength).replace(File.separatorChar, '/');
				zos.putNextEntry(new ZipEntry(zipEntryName));

				try (FileInputStream fis = new FileInputStream(file))
				{
					byte[] buffer = new byte[4096];
					int len;
					while ((len = fis.read(buffer)) > 0)
					{
						zos.write(buffer, 0, len);
					}
				}

				zos.closeEntry();
			}
		}
	}
}

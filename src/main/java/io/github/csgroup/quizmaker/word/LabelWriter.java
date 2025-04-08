package io.github.csgroup.quizmaker.word;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Label;

/**
 * Responsible for writing {@link Label Labels} to .docx files <br>
 * This currently supports plain text and html text with elements including pictures, tables, and lists
 * 
 * @author Samuel Garcia 
 */
public class LabelWriter
{
	public static final Logger logger = LoggerFactory.getLogger(LabelWriter.class);
	
	private final XWPFDocument document;
	
	
	/**
     * Constructs a LabelWriter for the given document.
     * @param document The XWPFDocument to which labels will be written.
     */
	public LabelWriter(XWPFDocument document) 
	{
		this.document = document;
	}
	
	/**
	 * Splits each label depending on plain text or HTML, and prepares <br>
	 * it to be written to a document
	 * @param label The label to be written.
	 * @throws IOException If an error occurs while processing the label.
	 */
	public void write(Label label) throws IOException
	{
		if (label.getType() == Label.Type.html) {
			org.jsoup.nodes.Document doc = Jsoup.parse(label.asText());
			processLabel(doc.body());
		} else {
			XWPFParagraph paragraph = document.createParagraph();
			processPlainText(label.asText(), paragraph);
		}
	}

	/**
	 * Writes a {@link Label} into an existing {@link XWPFParagraph}, preserving formatting. <p>
	 * This is an inline variant of the standard {@link #write(Label)} method that does not
	 * create a new paragraph in the document, allowing for multiple labels to be written on the same line.
	 * 
	 * @param label The label to write into the given paragraph.
	 * @param paragraph The existing paragraph where the label will be written.
	 * @throws IOException If an error occurs while processing or writing the label.
	 */
	public void writeInline(Label label, XWPFParagraph paragraph) throws IOException 
	{
		if(label.getType() == Label.Type.html)
		{
			org.jsoup.nodes.Document doc = Jsoup.parse(label.asText());
			processLabel(doc.body());
		}
		else
		{
			String[] lines = label.asText().split("\n", -1);
			for(int i = 0; i < lines.length; i++)
			{
				XWPFRun run = paragraph.createRun();
				run.setText(lines[i]);
				if (i < lines.length - 1) run.addBreak();
			}
		}
	}
	
	/**
	 * Processes the root HTML node of a {@link Label} and writes its content into the Word document,
	 * while carefully managing paragraph creation to avoid unwanted spacing between block and inline elements.
	 * <p>
	 * This method is only called internally by {@link #write(Label)} for labels of type {@code html}.
	 *
	 * @param root The root HTML node parsed from the label's HTML content (typically {@code <body>}).
	 * @throws IOException If an error occurs during processing.
	 */
	private void processLabel(Node root) throws IOException {
	    List<Node> children = root.childNodes();
	    XWPFParagraph activeParagraph = null;

	    for (Node node : children) {
	        if (node instanceof TextNode textNode) {
	            if (activeParagraph == null) 
	            {
	                activeParagraph = document.createParagraph();
	            }
	            processHtmlText(textNode.text(), activeParagraph, false, false, false);
	        } else if (node instanceof Element element) {
	            String tag = element.tagName();
	            if (tag.equals("table") || tag.equals("ul") || tag.equals("ol")) 
	            {
	                activeParagraph = null;
	            }

	            processHtmlElement(element, activeParagraph, false, false, false);
	            if (tag.equals("table") || tag.equals("ul") || tag.equals("ol")) 
	            {
	                activeParagraph = null;
	            }
	        }
	    }
	}

	
	/**
	 * Recursively processes HTML elements, applying styles to each processed element
	 * @param nodes The list of HTML nodes to process.
	 * @param paragraph The paragraph where the text will be added.
	 * @param bold Whether the text should be bold.
	 * @param italic Whether the text should be italicized.
	 * @param underline Whether the text should be underlined.
	 * @throws IOException If an error occurs while processing elements.
	 */
	private void processElements(List<Node> nodes, XWPFParagraph paragraph, boolean bold, boolean italic, boolean underline) throws IOException {
		for (Node node : nodes) {
			if (node instanceof TextNode textNode) {
				processHtmlText(textNode.text(), paragraph, bold, italic, underline);
			} else if (node instanceof Element element) {
				processHtmlElement(element, paragraph, bold, italic, underline);
			}
		}
	}
	
	/**
	 * Processes plain text to be written into a paragraph
	 * @param text The text to be written.
	 * @param paragraph The paragraph where the text will be added.
	 */
	private void processPlainText(String text, XWPFParagraph paragraph) {
		String[] lines = text.split("\n", -1); // -1 ensures trailing empty lines are preserved
		for (int i = 0; i < lines.length; i++) {
			XWPFRun run = paragraph.createRun();
			run.setText(lines[i]);
			if (i < lines.length - 1) {
				run.addBreak(); // Add a break for new lines
			}
		}
	}
    
	/**
	 * Processes a plain text node inside an HTML document, applying styles as needed.
	 * @param text The text content.
	 * @param paragraph The paragraph where the text will be added.
	 * @param bold Whether the text should be bold.
	 * @param italic Whether the text should be italicized.
	 * @param underline Whether the text should be underlined.
	 */
	private void processHtmlText(String text, XWPFParagraph paragraph, boolean bold, boolean italic, boolean underline) {
		if (paragraph == null) {
			paragraph = document.createParagraph();
		}
		XWPFRun run = paragraph.createRun();
		run.setText(text);
		run.setBold(bold);
		run.setItalic(italic);
		if (underline) run.setUnderline(UnderlinePatterns.SINGLE);
	}

	/**
	 *  Processes an individual HTML element and applies the correct formatting
	 * @param element The HTML element.
	 * @param paragraph The paragraph where the content will be added.
	 * @param bold Whether the text should be bold.
	 * @param italic Whether the text should be italicized.
	 * @param underline Whether the text should be underlined.
	 * @throws IOException If an error occurs while processing the element.
	 */
	private void processHtmlElement(Element element, XWPFParagraph paragraph, boolean bold, boolean italic, boolean underline) throws IOException {
		switch (element.tagName()) {
				case "b", "strong" -> processElements(element.childNodes(), paragraph, true, italic, underline);
				case "i", "em" -> processElements(element.childNodes(), paragraph, bold, true, underline);
				case "u" -> processElements(element.childNodes(), paragraph, bold, italic, true);
				case "br" -> paragraph.createRun().addBreak();
				case "p" -> {
					XWPFParagraph newParagraph = document.createParagraph();
					processElements(element.childNodes(), newParagraph, bold, italic, underline);
				}
				case "ul", "ol" -> processList(element, element.tagName().equals("ol"));
				case "table" -> processTable(element);
				case "img" -> processImage(element);
				default -> processElements(element.childNodes(), paragraph, bold, italic, underline);
		}
	}

	/**
	 * Processes an unordered/ordered list
	 * @param listElement The HTML list element.
	 * @param isOrdered Whether the list is ordered.
	 */
	private void processList(Element listElement, boolean isOrdered) {
	    int counter = 1;

	    for (Element listItem : listElement.children()) {
	        if (!listItem.tagName().equals("li")) continue;

	        XWPFParagraph paragraph;

	        if (counter == 1) {
	            // Try to reuse last paragraph *only* if it's truly empty
	            if (!document.getParagraphs().isEmpty()) {
	                XWPFParagraph last = document.getLastParagraph();
	                boolean isEmpty = last.getRuns().isEmpty() || last.getRuns().stream().allMatch(run -> run.text().isBlank());

	                if (isEmpty) {
	                    paragraph = last;
	                    clearParagraph(paragraph);
	                } else {
	                    paragraph = document.createParagraph();
	                }
	            } else {
	                paragraph = document.createParagraph();
	            }
	        } else {
	            paragraph = document.createParagraph();
	        }

	        XWPFRun run = paragraph.createRun();
	        run.setText((isOrdered ? counter + ". " : "- ") + listItem.text());

	        counter++;
	    }
	}
	
	/**
	 * Clears text in a paragraph so that it is empty
	 * @param paragraph The paragraph to clear text from
	 */
	private void clearParagraph(XWPFParagraph paragraph) {
	    int runCount = paragraph.getRuns().size();
	    for (int i = runCount - 1; i >= 0; i--) {
	        paragraph.removeRun(i);
	    }
	}


	/**
	 * Processes an HTML table and adds it directly to the Word document
	 * @param tableElement The table element to process.
	 */
	private void processTable(Element tableElement) {
		Elements rows = tableElement.select("tr");
		if (rows.isEmpty()) return;
		int columnCount = rows.get(0).select("td, th").size(); // Determine column count based on the first row
		if (columnCount == 0) return;
		XWPFTable table = document.createTable(rows.size(), columnCount);
		// Process each row
		for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
			XWPFTableRow row = table.getRow(rowIndex);
			Elements cells = rows.get(rowIndex).select("td, th");
			for (int colIndex = 0; colIndex < cells.size(); colIndex++) {
				XWPFTableCell cell = row.getCell(colIndex);
				if (cell == null) cell = row.addNewTableCell();
				XWPFParagraph cellParagraph = cell.getParagraphs().get(0);
				XWPFRun cellRun = cellParagraph.createRun();
				cellRun.setText(cells.get(colIndex).text());
			}
		}
	}

	/**
	 * Processes an image and adds it to the document
	 * @param imgElement The image element.
	 * @throws IOException If an error occurs while processing the image.
	 */
	private void processImage(Element imgElement) throws IOException {
		String src = imgElement.attr("src");
		if (src.startsWith("http") || src.startsWith("www")) {
			addImageFromUrl(src);
		} else {
			addImageFromFile(src);
		}
	}

	/**
	 * Adds an image from a local file
	 * @param filePath The file path of the image.
	 * @param IOException If the image cannot be loaded.
	 */
	private void addImageFromFile(String filePath) throws IOException {
		if(new File(filePath).isFile()) {
			try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
				addImageToDocument(is, filePath, XWPFDocument.PICTURE_TYPE_PNG); //Might have to change this, works for most images though 
			} 
		}
		else { 
			logger.error("Could not load image from file: {}", filePath);
		} 
	}

	/**
	 * Adds an image from a URL
	 * @param imageURL The URL of the image.
	 * @throws IOException If the image cannot be loaded.
	 */
	private void addImageFromUrl(String imageUrl) throws IOException {
		try (InputStream is = new URL(imageUrl).openStream()) {
			addImageToDocument(is, imageUrl, XWPFDocument.PICTURE_TYPE_PNG);
		} catch (IOException e) {
			logger.error("Could not load image from URL: {}", imageUrl);
			throw e; //Web filestreams are weird, also this has to be a try-catch for raw data
		}
	}

	/**
	 * Inserts an image into the Word document
	 * @param imageStream InputStream of the image.
	 * @param imageName Name of the image file.
	 * @param imageType Type of the image (e.g., PNG, JPEG).
	 * @throws IOException If an error occurs while adding the image.
	 */
	private void addImageToDocument(InputStream imageStream, String imageName, int imageType) throws IOException {
		try {
			XWPFParagraph paragraph = document.getParagraphs().isEmpty()
					? document.createParagraph()
					: document.getLastParagraph();
			XWPFRun run = paragraph.createRun();
			run.addPicture(imageStream, imageType, imageName, Units.toEMU(150), Units.toEMU(200)); //Probably need input from the image to change sizing, just using this for now
		} catch (Exception e) {
			logger.error("Failed to insert image: {}", imageName);
			throw new IOException("Failed to insert image: " + imageName, e);
		}
	}
}
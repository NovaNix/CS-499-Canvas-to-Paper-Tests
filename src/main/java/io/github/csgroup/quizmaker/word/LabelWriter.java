package io.github.csgroup.quizmaker.word;

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

import io.github.csgroup.quizmaker.data.Label;

/**
 * Responsible for writing {@link Label Labels} to docx files
 * 
 * @author 
 */
public class LabelWriter
{
	private final XWPFDocument document;
	// You dont have to adhere too carefully to this skeleton code, this is just a rough outline and some ideas
	// Do what works, and keep things clean and reusable if possible
	
	public LabelWriter(XWPFDocument document) 
	{
        this.document = document; // TODO take in the docx file to write to, along with any other persistent information you might need
	}
	
	// Splits labels based on HTML usage
	public void write(Label label)
	{
		XWPFParagraph paragraph = document.createParagraph();
        if (label.getType() == Label.Type.html) {
            org.jsoup.nodes.Document doc = Jsoup.parse(label.asText());
            processElements(doc.body().childNodes(), paragraph, false, false, false);
        } else {
        	paragraph.createRun().addBreak(); //Needed for consistent spacing with HTML runs
            processPlainText(label.asText(), paragraph);
        }
    }

	// Recursively processes HTML elements, applying styles
    private void processElements(List<Node> nodes, XWPFParagraph paragraph, boolean bold, boolean italic, boolean underline) {
        for (Node node : nodes) {
            if (node instanceof TextNode textNode) {
                processHtmlText(textNode.text(), paragraph, bold, italic, underline);
            } else if (node instanceof Element element) {
                processHtmlElement(element, paragraph, bold, italic, underline);
            }
        }
    }
	
    // Processes plain text
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
    
    // Processes plain text inside an HTML label, ensuring correct formatting
    private void processHtmlText(String text, XWPFParagraph paragraph, boolean bold, boolean italic, boolean underline) {
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(bold);
        run.setItalic(italic);
        if (underline) run.setUnderline(UnderlinePatterns.SINGLE);
    }
    
    // Processes an individual HTML element and applies the correct formatting
	private void processHtmlElement(Element element, XWPFParagraph paragraph, boolean bold, boolean italic, boolean underline) {
        switch (element.tagName()) {
                case "b", "strong" -> processElements(element.childNodes(), paragraph, true, italic, underline);
                case "i", "em" -> processElements(element.childNodes(), paragraph, bold, true, underline);
                case "u" -> processElements(element.childNodes(), paragraph, bold, italic, true);
                case "br" -> paragraph.createRun().addBreak();
                case "p" -> {
                    XWPFParagraph newParagraph = document.createParagraph();
                    processElements(element.childNodes(), newParagraph, bold, italic, underline);
                }
            case "ul", "ol" -> processList(element, paragraph, element.tagName().equals("ol"));
            case "table" -> processTable(element);
            case "img" -> processImage(element);
            default -> processElements(element.childNodes(), paragraph, bold, italic, underline);
        }
    }

    // Processes an unordered/ordered list
    private void processList(Element listElement, XWPFParagraph paragraph, boolean isOrdered) {
        int counter = 1;
        for (Element listItem : listElement.children()) {
            if (listItem.tagName().equals("li")) {
                XWPFParagraph listParagraph = document.createParagraph();
                XWPFRun listRun = listParagraph.createRun();
                listRun.setText((isOrdered ? counter++ + ". " : "- ") + listItem.text());
            }
        }
    }

    // Processes an HTML table and adds it to the Word document
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
        document.createParagraph().createRun().addBreak();
    }

    // Processes an image and adds it to the document
    private void processImage(Element imgElement) {
        String src = imgElement.attr("src");
        if (src.startsWith("http") || src.startsWith("www")) {
            addImageFromUrl(src);
        } else {
            addImageFromFile(src);
        }
    }

    // Adds an image from a local file
    private void addImageFromFile(String filePath) {
        try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
            addImageToDocument(is, filePath, XWPFDocument.PICTURE_TYPE_PNG); //Might have to change this, works for most images though
        } catch (IOException e) {
            System.err.println("Could not load image from file: " + filePath);
            e.printStackTrace();
        }
    }

    // Adds an image from a URL
    private void addImageFromUrl(String imageUrl) {
        try (InputStream is = new URL(imageUrl).openStream()) {
            addImageToDocument(is, imageUrl, XWPFDocument.PICTURE_TYPE_PNG);
        } catch (IOException e) {
            System.err.println("Could not load image from URL: " + imageUrl);
            e.printStackTrace();
        }
    }

    // Inserts an image into the Word document
    private void addImageToDocument(InputStream imageStream, String imageName, int imageType) {
        try {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.addPicture(imageStream, imageType, imageName, Units.toEMU(150), Units.toEMU(200)); //Probably need input from the image to change sizing, just using this for now
        } catch (Exception e) {
            System.err.println("Failed to insert image: " + imageName);
            e.printStackTrace();
        }
    }
}
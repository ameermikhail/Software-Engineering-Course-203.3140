package il.cshaifasweng.OCSFMediatorExample.entities;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TextToWord {
    public void convertToWord(String text, File outputFile) {
        List<String> words = Arrays.asList(text.split("\\s+"));
        createWordDocument(words, outputFile);
    }

    private void createWordDocument(List<String> words, File outputFile) {
        try {
            XWPFDocument document = new XWPFDocument();
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();

            for (String word : words) {
                if (word.equalsIgnoreCase("Question") || word.equalsIgnoreCase("Answer")) {
                    paragraph = document.createParagraph(); // Start a new paragraph
                    run = paragraph.createRun(); // Create a new run
                    run.setText(word + " "); // Add the word to the run
                } else {
                    run.setText(word + " "); // Add the word to the existing run
                }
            }

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            document.write(outputStream);
            outputStream.close();
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
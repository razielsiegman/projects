package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.stage2.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class DocumentImpl implements Document {
    private URI uri;
    private String text;
    private int txtHash;
    private byte[] pdfBytes;

    public DocumentImpl(URI uri, String text, int txtHash){
        this.uri = uri;
        this.text = text;
        this.txtHash = txtHash;
        this.pdfBytes = pdfCreator(text);
    }

    public DocumentImpl(URI uri, String text, int txtHash, byte[] pdfBytes){
        this.uri = uri;
        this.text = text;
        this.txtHash = txtHash;
        this.pdfBytes = pdfBytes;
    }

    public byte[] getDocumentAsPdf() {
        return pdfBytes;
    }

    public String getDocumentAsTxt() {
        return text.trim();
    }

    public int getDocumentTextHashCode() {
        return txtHash;
    }

    public URI getKey() {
        return uri;
    }

    private byte[] pdfCreator(String text){
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        byte[] bytes = null;
        try {
            PDPageContentStream contents = new PDPageContentStream(doc, page);
            contents.beginText();
            contents.setFont(font, 12);
            contents.newLineAtOffset(100, 700);
            contents.showText(text);
            contents.endText();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            contents.close();
            doc.save(output);
            doc.close();
            bytes = output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
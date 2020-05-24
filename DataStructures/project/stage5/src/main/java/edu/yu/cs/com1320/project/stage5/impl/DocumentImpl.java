package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.annotations.Expose;
import edu.yu.cs.com1320.project.stage5.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DocumentImpl implements Document {

    protected URI uri;
    protected String text;
    private int txtHash;
    private byte[] pdfBytes;
    private HashMap hashMap;
    private long lastUseTime;

    private DocumentStoreImpl.DocRef docref;

    public DocumentImpl(URI uri, String text, int txtHash) {
        this.lastUseTime = System.nanoTime();
        this.uri = uri;
        this.text = text;
        this.txtHash = txtHash;
        this.pdfBytes = pdfCreator(text);
        this.hashMap = hashCreator(text);
    }

    public DocumentImpl(URI uri, String text, int txtHash, byte[] pdfBytes) {
        this.lastUseTime = System.nanoTime();
        this.uri = uri;
        this.text = text;
        this.txtHash = txtHash;
        this.pdfBytes = pdfBytes;
        this.hashMap = hashCreator(text);
    }

    protected DocumentImpl(URI uri, String text, int txtHash, HashMap hashMap){
        this.lastUseTime = System.nanoTime();
        this.uri = uri;
        this.text = text;
        this.txtHash = txtHash;
        this.pdfBytes = pdfCreator(text);
        this.hashMap = hashMap;
    }

    protected DocumentStoreImpl.DocRef getDocRef() {
        return this.docref;
    }

    protected void setDocRef(DocumentStoreImpl.DocRef docRef) {
        this.docref = docRef;
    }

    private String textMod(String text) {
        text = text.replaceAll("[^a-zA-Z0-9 ]", "");
        text = text.toUpperCase();
        return text;
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

    public int wordCount(String word) {
        word = this.textMod(word);
        if (hashMap.get(word) == null) {
            return 0;
        } else {
            return (int) hashMap.get(word);
        }
    }

    public long getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(long timeInNano) {
        this.lastUseTime = timeInNano;

    }

    @Override
    public Map<String, Integer> getWordMap() {
        return this.hashMap;
    }

    @Override
    public void setWordMap(Map<String, Integer> wordMap) {
        this.hashMap = (HashMap) wordMap;
    }

    private HashMap hashCreator(String text) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        int firstInput = 1;
        String editedText = this.textMod(text);
        String[] words = editedText.split(" ");
        for (String word : words) {
            if (!hashMap.containsKey(word)) {
                hashMap.put(word, firstInput);
            } else {
                int occurrences = hashMap.get(word);
                hashMap.put(word, ++occurrences);
            }
        }
        return hashMap;
    }

    private byte[] pdfCreator(String text) {
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

    @Override
    public int compareTo(Document o) {
        if ((this.lastUseTime > o.getLastUseTime())) {
            return 1;
        } else {
            return -1;
        }
    }
}
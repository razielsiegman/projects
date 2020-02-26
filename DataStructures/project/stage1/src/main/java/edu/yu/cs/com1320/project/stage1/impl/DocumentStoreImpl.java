package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage1.DocumentStore;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class DocumentStoreImpl implements DocumentStore {

    private HashTableImpl hashTable = new HashTableImpl();

    public DocumentStoreImpl() {
        this.hashTable = new HashTableImpl();
    }

    public int putDocument(InputStream input, URI uri, DocumentFormat format) {
        int oldHashCode = 0;
        int newHashCode = 0;
        if((input == null) || (uri == null) || (format == null)){
            return this.nullInput(input, uri, format);
        }
        String text = null;
        byte[] bytes = this.byteArrayMaker(input);
        if(format == DocumentFormat.TXT) {
            text = new String(bytes);
        }
        if(format == DocumentFormat.PDF){
            text = this.pdfToString(bytes);
        }
        String textTemp = text.trim();
        newHashCode = textTemp.hashCode();
        DocumentImpl doc = null;
        doc = (DocumentImpl)hashTable.get(uri);
        if(doc != null) {
            if (doc.getDocumentTextHashCode() == newHashCode) {
                return newHashCode;
            }
            else{
                oldHashCode = doc.getDocumentTextHashCode();
                doc = this.docBuilder(uri,text,newHashCode,bytes,format);
            }
        }
        else{
            doc = this.docBuilder(uri, text, newHashCode, bytes, format);
            oldHashCode = 0;
        }
        hashTable.put(uri, doc);
        return oldHashCode;
    }

    private byte[] byteArrayMaker(InputStream input){
        byte[] bytes = new byte[16384];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int holder;
        try {
            while ((holder = input.read(bytes, 0, bytes.length)) != -1) {
                buffer.write(bytes, 0, holder);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        bytes = buffer.toByteArray();
        return bytes;
    }

    private DocumentImpl docBuilder(URI uri, String text, int hashCode, byte[] bytes, DocumentFormat format){
        if(format == DocumentFormat.PDF){
            DocumentImpl doc = new DocumentImpl(uri, text, hashCode, bytes);
            return doc;
        }
        if(format == DocumentFormat.TXT){
            DocumentImpl doc = new DocumentImpl(uri, text, hashCode);
            return doc;
        }
        return null;
    }

    private int nullInput(InputStream input, URI uri, DocumentFormat format){
        if((uri == null) || ((input != null) && (format == null))){
            throw new IllegalArgumentException();
        }
        if(hashTable.get(uri) == null){
            return 0;
        }
        else {
            DocumentImpl doc = (DocumentImpl) hashTable.get(uri);
            int hashCode = doc.getDocumentTextHashCode();
            deleteDocument(uri);
            return hashCode;
        }
    }

    private String pdfToString(byte[] bytes){
        try {
            PDFTextStripper stripper = new PDFTextStripper();
            PDDocument pdDoc = PDDocument.load(bytes);
            String text = stripper.getText(pdDoc);
            pdDoc.close();
            text.trim();
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getDocumentAsPdf(URI uri) {
        DocumentImpl doc = (DocumentImpl)hashTable.get(uri);
        if(doc == null){
            throw new IllegalArgumentException("Doc doesn't exist");
        }
        byte[] pdf= doc.getDocumentAsPdf();
        return pdf;
    }

    public String getDocumentAsTxt(URI uri) {
        DocumentImpl doc = (DocumentImpl)hashTable.get(uri);
        if(doc == null){
            throw new IllegalArgumentException("Doc doesn't exist");
        }
        String text = doc.getDocumentAsTxt();
        return text;
    }

    public boolean deleteDocument(URI uri) {
        if(hashTable.get(uri) == null) {
            return false;
        }
        else{
            hashTable.put(uri,null);
            return true;
        }
    }
}
package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore {

    private HashTableImpl hashTable;
    private StackImpl stack;

    public DocumentStoreImpl() {
        this.hashTable = new HashTableImpl();
        this.stack = new StackImpl();
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
        DocumentImpl oldDoc = null;
        doc = (DocumentImpl)hashTable.get(uri);
        if(doc != null) {
            if (doc.getDocumentTextHashCode() == newHashCode) {
                return newHashCode;
            }
            else{
                oldHashCode = doc.getDocumentTextHashCode();
                oldDoc = doc;
                doc = this.docBuilder(uri,text,newHashCode,bytes,format);
            }
        }
        else{
            doc = this.docBuilder(uri, text, newHashCode, bytes, format);
            oldHashCode = 0;
        }
        Command command = this.putCommand(uri, oldDoc);
        hashTable.put(uri, doc);
        return oldHashCode;
    }

    //If original input was the first time using a URI, oldDoc will be null, and the put statement will be interpreted as a delete.
    //If input was an override, the put statement will place previous doc into hashTable.
    private Command putCommand(URI incomingUri, DocumentImpl oldDoc){
        Function<URI,Boolean> undo = (uri) -> {
            hashTable.put(uri, oldDoc);
            return true;
        };
        Command command = new Command(incomingUri, undo);
        stack.push(command);
        return command;
    }

    public boolean deleteDocument(URI uri) {
        DocumentImpl doc = (DocumentImpl)hashTable.get(uri);
        if(doc == null) {
            Command command = this.deleteCommand(uri, null);
            return false;
        }
        else{
            hashTable.put(uri,null);
            Command command = this.deleteCommand(uri, doc);
            return true;
        }
    }

    private Command deleteCommand(URI incomingUri, DocumentImpl doc){
        Function<URI,Boolean> undo = (uri) -> {
            hashTable.put(uri, doc);
            return true;
        };
        Command command = new Command(incomingUri, undo);
        stack.push(command);
        return command;
    }

    protected Document getDocument (URI uri){
        DocumentImpl doc = (DocumentImpl)hashTable.get(uri);
        return doc;
    }

    public void undo() throws IllegalStateException {
        if(stack.size() == 0){
            throw new IllegalStateException();
        }
        else {
            Command command = (Command) stack.pop();
            command.undo();
        }
    }

    public void undo(URI uri) throws IllegalStateException {
        StackImpl tempStack = new StackImpl();
        Command command = (Command)stack.peek();
        while(!(command.getUri().equals(uri))){
            stack.pop();
            tempStack.push(command);
            command = (Command)stack.peek();
            if(command == null){
                throw new IllegalStateException();
            }
        }
        stack.pop();
        command.undo();
        int tempSize = tempStack.size();
        for(int i = 1; i <= tempSize; i++){
            Command restoreCommand = (Command)tempStack.pop();
            stack.push(restoreCommand);
        }
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
        int hashCode = 0;
        if (hashTable.get(uri) != null) {
            DocumentImpl doc = (DocumentImpl) hashTable.get(uri);
            hashCode = doc.getDocumentTextHashCode();
        }
        deleteDocument(uri);
        return hashCode;
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
            return null;
        }
        byte[] pdf= doc.getDocumentAsPdf();
        return pdf;
    }

    public String getDocumentAsTxt(URI uri) {
        DocumentImpl doc = (DocumentImpl)hashTable.get(uri);
        if(doc == null){
            return null;
        }
        String text = doc.getDocumentAsTxt();
        return text;
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
}
package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.stage4.DocumentStore;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore {

    private HashTableImpl hashTable;
    private StackImpl stack;
    private TrieImpl trie;
    private int documentCount;
    private int byteCount;
    private int maxDocumentCount;
    private int maxDocumentBytes;
    private MinHeapImpl heap;

    public DocumentStoreImpl() {
        this.hashTable = new HashTableImpl();
        this.stack = new StackImpl();
        this.trie = new TrieImpl();
        this.heap = new MinHeapImpl();
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
                doc.setLastUseTime(System.nanoTime());
                heap.reHeapify(doc);
                return newHashCode;
            }
            else{
                oldHashCode = doc.getDocumentTextHashCode();
                oldDoc = doc;
                //in case of override, oldDoc is removed from heap and trie
                this.trieRemove(oldDoc);
                this.shrinkHeap(oldDoc);
                doc = this.docBuilder(uri,text,newHashCode,bytes,format);
            }
        }
        else{
            doc = this.docBuilder(uri, text, newHashCode, bytes, format);
            oldHashCode = 0;
        }
        GenericCommand<URI> command = this.putCommand(uri, oldDoc, doc);
        hashTable.put(uri, doc);
        this.trieInsert(doc);
        doc.setLastUseTime(System.nanoTime());
        this.addToHeap(doc);
        return oldHashCode;
    }

    private void addToHeap(DocumentImpl doc){
        if(doc == null){
            return;
        }
        documentCount++;
        byteCount += this.docBytes(doc);
        this.limitCheck();
        heap.insert(doc);
    }

    private void limitCheck(){
        if(maxDocumentCount > 0){
            while(maxDocumentCount < documentCount){
                this.shrinkHeap();
            }
        }
        if(maxDocumentBytes > 0){
            while(maxDocumentBytes < byteCount){
                this.shrinkHeap();
            }
        }
    }

    //To be called when a specific doc has been deleted, and should be removed from heap
    private void shrinkHeap(DocumentImpl doc){
        doc.setLastUseTime(Long.MIN_VALUE);
        heap.reHeapify(doc);
        heap.removeMin();
        documentCount--;
        byteCount -= this.docBytes(doc);
    }

    //To be called when the doc/memory limit is reached, and the least recently used doc must be fully obliterated
    private void shrinkHeap(){
        DocumentImpl doc = (DocumentImpl)heap.removeMin();
        documentCount--;
        byteCount -= this.docBytes(doc);
        this.trieRemove(doc);
        URI key = doc.getKey();
        hashTable.put(key, null);
        this.removeTracesFromStack(key);
    }

    private void removeTracesFromStack(URI uri){
        int stackCount = stack.size();
        StackImpl tempStack = new StackImpl();
        for(int i = 1; i < stackCount; i++){
            Undoable command = (Undoable) stack.peek();
            if(this.removeTracesExec(command, uri) == false) {
                tempStack.push(command);
                stack.pop();
            }
        }
        for(int i = 1; i <= tempStack.size(); i++){
            Undoable restoreCommand = (Undoable)tempStack.pop();
            stack.push(restoreCommand);
        }
    }

    private boolean removeTracesExec(Undoable command, URI uri){
        if(command instanceof GenericCommand){
            GenericCommand genericCommand = (GenericCommand)command;
            if(genericCommand.getTarget().equals(uri)){
                stack.pop();
                return true;
            }
        }
        if(command instanceof CommandSet){
            CommandSet commandSet = (CommandSet) command;
            Iterator iterator = ((CommandSet) command).iterator();
            while(iterator.hasNext()){
                GenericCommand currentCommand = (GenericCommand)iterator.next();
                if(currentCommand.getTarget().equals(uri)){
                    iterator.remove();
                    if(commandSet.size() == 0){
                        stack.pop();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int docBytes(DocumentImpl doc){
        int pdfBytes = doc.getDocumentAsPdf().length;
        int stringBytes = doc.getDocumentAsTxt().getBytes().length;
        return pdfBytes + stringBytes;
    }

    private void trieInsert(DocumentImpl doc){
        if(doc == null){
            return;
        }
        String[] words = this.getDocumentAsWords(doc);
        for(String word : words){
            trie.put(word, doc);
        }
    }

    private void trieRemove(DocumentImpl doc){
        if(doc == null){
            return;
        }
        String[] words = this.getDocumentAsWords(doc);
        for(String word : words){
            trie.delete(word, doc);
        }
    }

    //If original input was the first time using a URI, oldDoc will be null, and the put statement will be interpreted as a delete.
    //If input was an override, the put statement will place previous doc into hashTable.
    private GenericCommand<URI> putCommand(URI incomingUri, DocumentImpl oldDoc, DocumentImpl newDoc){
        Function<URI,Boolean> undo = (uri) -> {
            //when undoing an override, the oldDoc is put back into heap and the newDoc is removed from heap
            this.shrinkHeap(newDoc);
            hashTable.put(uri, oldDoc);
            if(oldDoc != null) {
                oldDoc.setLastUseTime(System.nanoTime());
                this.addToHeap(oldDoc);
            }
            this.trieInsert(oldDoc);
            this.trieRemove(newDoc);
            return true;
        };
        GenericCommand<URI> command = new GenericCommand<URI>(incomingUri, undo);
        stack.push(command);
        return command;
    }

    public boolean deleteDocument(URI uri) {
        DocumentImpl doc = (DocumentImpl)hashTable.get(uri);
        if(doc == null) {
            GenericCommand<URI> command = this.deleteCommand(uri, null);
            stack.push(command);
            return false;
        }
        else{
            hashTable.put(uri,null);
            this.trieRemove(doc);
            this.shrinkHeap(doc);
            GenericCommand<URI> command = this.deleteCommand(uri, doc);
            stack.push(command);
            return true;
        }
    }

    private GenericCommand<URI> deleteCommand(URI incomingUri, DocumentImpl doc){
        Function<URI,Boolean> undo = (uri) -> {
            hashTable.put(uri, doc);
            this.trieInsert(doc);
            if(doc != null) {
                doc.setLastUseTime(System.nanoTime());
                this.addToHeap(doc);
            }
            return true;
        };
        GenericCommand<URI> command = new GenericCommand<URI>(incomingUri, undo);
        return command;
    }

    public void undo() throws IllegalStateException {
        if(stack.size() == 0){
            throw new IllegalStateException();
        }
        else {
            Undoable command = (Undoable) stack.pop();
            if(command instanceof GenericCommand) {
                GenericCommand genericCommand = (GenericCommand)command;
                genericCommand.undo();
            }
            else{
                CommandSet commandSet = (CommandSet)command;
                commandSet.undoAll();
            }
        }
    }

    private boolean undoCommandSet(Undoable command, URI uri){
        if(command instanceof CommandSet){
            CommandSet commandSet = (CommandSet) command;
            Iterator iterator = ((CommandSet) command).iterator();
            while(iterator.hasNext()){
                GenericCommand currentCommand = (GenericCommand)iterator.next();
                if(currentCommand.getTarget().equals(uri)){
                    commandSet.undo(uri);
                    if(commandSet.size() == 0){
                        stack.pop();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean undoGenericCommand(Undoable command, URI uri){
        if(command instanceof GenericCommand){
            GenericCommand genericCommand = (GenericCommand)command;
            if(genericCommand.getTarget().equals(uri)){
                stack.pop();
                genericCommand.undo();
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    public void undo(URI uri) throws IllegalStateException {
        StackImpl tempStack = new StackImpl();
        Undoable command = (Undoable) stack.peek();
        if(command == null){
            throw new IllegalStateException();
        }
        while((this.undoGenericCommand(command, uri) == false) && (this.undoCommandSet(command, uri) == false)){
            tempStack.push(command);
            stack.pop();
            command = (Undoable)stack.peek();
            if(command == null){
                throw new IllegalStateException();
            }
        }
        int tempSize = tempStack.size();
        for(int i = 1; i <= tempSize; i++){
            Undoable restoreCommand = (Undoable)tempStack.pop();
            stack.push(restoreCommand);
        }
    }

    public List<String> search(String keyword) {
        String keywordMod = this.textMod(keyword);
        Comparator<DocumentImpl> comparator = (DocumentImpl doc1, DocumentImpl doc2) ->
                (doc2.wordCount(keywordMod)) - (doc1.wordCount(keywordMod));
        List<DocumentImpl> docList= trie.getAllSorted(keywordMod, comparator);
        List<String> docAsString = new ArrayList<>();
        long time = System.nanoTime();
        for(DocumentImpl doc : docList){
            doc.setLastUseTime(time);
            heap.reHeapify(doc);
            String text = doc.getDocumentAsTxt();
            docAsString.add(text);
        }
        return docAsString;
    }

    public List<byte[]> searchPDFs(String keyword) {
        String keywordMod = this.textMod(keyword);
        Comparator<DocumentImpl> comparator = (DocumentImpl doc1, DocumentImpl doc2) ->
                doc2.wordCount(keywordMod) - doc1.wordCount(keywordMod);
        List<DocumentImpl> docList= trie.getAllSorted(keywordMod, comparator);
        List<byte[]> docAsPdf = new ArrayList<>();
        long time = System.nanoTime();
        for(DocumentImpl doc : docList){
            doc.setLastUseTime(time);
            heap.reHeapify(doc);
            byte[] pdf = doc.getDocumentAsPdf();
            docAsPdf.add(pdf);
        }
        return docAsPdf;
    }

    private int prefixCount(String prefix, DocumentImpl doc){
        int count = 0;
        int letters = prefix.length();
        String[] wordsOne = this.getDocumentAsWords(doc);
        for(String word : wordsOne){
            if(word.length() >= letters){
                if(word.substring(0,letters).equals(prefix)){
                    count++;
                }
            }
        }
        return count;
    }

    public List<String> searchByPrefix(String prefix) {
        String prefixMod = this.textMod(prefix);
        Comparator<DocumentImpl> comparator = (DocumentImpl doc1, DocumentImpl doc2) -> {
            int countOne = this.prefixCount(prefixMod, doc1);
            int countTwo = this.prefixCount(prefixMod, doc2);
            return countTwo - countOne;
        };
        List<DocumentImpl> docList= trie.getAllWithPrefixSorted(prefixMod, comparator);
        List<String> docAsString = new ArrayList<>();
        long time = System.nanoTime();
        for(DocumentImpl doc : docList){
            doc.setLastUseTime(time);
            heap.reHeapify(doc);
            String text = doc.getDocumentAsTxt();
            docAsString.add(text);
        }
        return docAsString;
    }

    public List<byte[]> searchPDFsByPrefix(String prefix) {
        String prefixMod = this.textMod(prefix);
        Comparator<DocumentImpl> comparator = (DocumentImpl doc1, DocumentImpl doc2) -> {
            int countOne = this.prefixCount(prefixMod, doc1);
            int countTwo = this.prefixCount(prefixMod, doc2);
            return countTwo - countOne;
        };
        List<DocumentImpl> docList= trie.getAllWithPrefixSorted(prefixMod, comparator);
        List<byte[]> docAsPdf = new ArrayList<>();
        long time = System.nanoTime();
        for(DocumentImpl doc : docList){
            doc.setLastUseTime(time);
            heap.reHeapify(doc);
            byte[] pdf = doc.getDocumentAsPdf();
            docAsPdf.add(pdf);
        }
        return docAsPdf;
    }

    public Set<URI> deleteAll(String keyword) {
        String keyMod = this.textMod(keyword);
        Comparator<DocumentImpl> comparator = (DocumentImpl doc1, DocumentImpl doc2) ->
                doc2.wordCount(keyMod) - doc1.wordCount(keyMod);
        List<DocumentImpl> docList= trie.getAllSorted(keyMod, comparator);
        List<DocumentImpl> docListTwo = new ArrayList<>();
        CommandSet<URI> commandSet = new CommandSet<URI>();
        Set<URI> uriSet = new HashSet<URI>();
        for(DocumentImpl doc : docList){
            URI uri = doc.getKey();
            uriSet.add(uri);
            if(doc == null) {
                GenericCommand<URI> command = this.deleteCommand(uri, null);
                commandSet.addCommand(command);
            }
            else{
                hashTable.put(uri,null);
                docListTwo.add(doc);
                GenericCommand<URI> command = this.deleteCommand(uri, doc);
                commandSet.addCommand(command);
            }
        }
        for(DocumentImpl doc : docListTwo){
            this.shrinkHeap(doc);
            this.trieRemove(doc);
        }
        stack.push(commandSet);
        return uriSet;
    }

    public Set<URI> deleteAllWithPrefix(String prefix) {
        String prefixMod = this.textMod(prefix);
        Comparator<DocumentImpl> comparator = (DocumentImpl doc1, DocumentImpl doc2) ->
                this.prefixCount(prefixMod, doc2) - this.prefixCount(prefixMod, doc1);
        List<DocumentImpl> docList= trie.getAllWithPrefixSorted(prefixMod, comparator);
        CommandSet<URI> commandSet = new CommandSet<URI>();
        List<DocumentImpl> docListTwo = new ArrayList<>();
        Set<URI> uriSet = new HashSet<URI>();
        for(DocumentImpl doc : docList){
            URI uri = doc.getKey();
            uriSet.add(uri);
            if(doc == null) {
                GenericCommand<URI> command = this.deleteCommand(uri, null);
                commandSet.addCommand(command);
            }
            else{
                hashTable.put(uri,null);
                docListTwo.add(doc);
                GenericCommand<URI> command = this.deleteCommand(uri, doc);
                commandSet.addCommand(command);
            }
        }
        for(DocumentImpl doc : docListTwo){
            this.shrinkHeap(doc);
            trieRemove(doc);
        }
        stack.push(commandSet);
        return uriSet;
    }

    public void setMaxDocumentCount(int limit) {
        if(limit <= 0){
            throw new IllegalArgumentException();
        }
        this.maxDocumentCount = limit;
        this.limitCheck();
    }

    public void setMaxDocumentBytes(int limit) {
        if(limit <= 0){
            throw new IllegalArgumentException();
        }
        this.maxDocumentBytes = limit;
        this.limitCheck();
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
            text = text.trim();
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

    private String[] getDocumentAsWords(DocumentImpl doc){
        String text = doc.getDocumentAsTxt();
        text = text.replaceAll("[^a-zA-Z0-9 ]", "");
        text = text.toUpperCase();
        String[] words = text.split(" ");
        return words;
    }

    private String textMod(String text){
        text = text.replaceAll("[^a-zA-Z0-9]", "");
        text = text.toUpperCase();
        return text;
    }
}
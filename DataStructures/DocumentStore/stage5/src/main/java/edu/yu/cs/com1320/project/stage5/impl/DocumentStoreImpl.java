package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore {

    private BTreeImpl bTree;
    private StackImpl stack;
    private TrieImpl trie;
    private int documentCount;
    private int byteCount;
    private int maxDocumentCount;
    private int maxDocumentBytes;
    private MinHeapImpl heap;
    private File directory;

    public DocumentStoreImpl(File baseDir) {
        this.stack = new StackImpl();
        this.trie = new TrieImpl();
        this.heap = new MinHeapImpl();
        this.directory = baseDir;
        this.createBTree();
    }

    public DocumentStoreImpl() {
        this.stack = new StackImpl();
        this.trie = new TrieImpl();
        this.heap = new MinHeapImpl();
        this.directory = new File(System.getProperty("user.dir"));
        this.createBTree();
    }

    private void createBTree(){
        URI uri = null;
        try {
            uri = new URI("a");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.bTree = new BTreeImpl();
        bTree.put(uri, null);
        PersistenceManager pm = null;
        try {
            pm = new DocumentPersistenceManager(this.directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bTree.setPersistenceManager(pm);
    }

    public int putDocument(InputStream input, URI uri, DocumentFormat format) {
        int oldHashCode = 0;
        int newHashCode = 0;
        if ((input == null) || (uri == null) || (format == null)) {
            return this.nullInput(input, uri, format);
        }
        String text = null;
        byte[] bytes = this.byteArrayMaker(input);
        if (format == DocumentFormat.TXT) {
            text = new String(bytes);
        }
        if (format == DocumentFormat.PDF) {
            text = this.pdfToString(bytes);
        }
        String textTemp = text.trim();
        newHashCode = textTemp.hashCode();
        DocumentImpl doc = null;
        DocumentImpl oldDoc = null;
        boolean onDisk = this.onDisk(uri);
        doc = (DocumentImpl) bTree.get(uri);
        return this.putImpl(uri, oldHashCode, newHashCode, doc, oldDoc, text, bytes, format, onDisk);
    }

        private int putImpl(URI uri, int oldHashCode, int newHashCode, DocumentImpl doc, DocumentImpl oldDoc, String text, byte[] bytes, DocumentFormat format, boolean onDisk){
            if(doc != null) {
            if (doc.getDocumentTextHashCode() == newHashCode) {
                doc.setLastUseTime(System.nanoTime());
                if(onDisk){
                    this.addToHeap(uri, doc);
                }
                else{
                    heap.reHeapify(doc.getDocRef());
                }
                GenericCommand<URI> command = this.putCommand(uri, doc, doc);
                return newHashCode;
            }
            else{
                oldHashCode = doc.getDocumentTextHashCode();
                oldDoc = doc;
                //in case of override, oldDoc is removed from heap and trie
                if(onDisk == false) {
                    this.deleteMemoryManagement(uri);
                }
                this.trieRemove(uri, oldDoc);
                doc.setLastUseTime(Long.MIN_VALUE);
                heap.reHeapify(doc.getDocRef());
                heap.removeMin();
                doc = this.docBuilder(uri,text,newHashCode,bytes,format);
            }
        }
        else{
            doc = this.docBuilder(uri, text, newHashCode, bytes, format);
            oldHashCode = 0;
        }
        GenericCommand<URI> command = this.putCommand(uri, oldDoc, doc);
        bTree.put(uri, doc);
        this.trieInsert(uri, doc);
        doc.setLastUseTime(System.nanoTime());
        this.addToHeap(uri, doc);
        return oldHashCode;
    }

    private void addToHeap(URI uri, DocumentImpl doc){
        if(doc == null){
            return;
        }
        documentCount++;
        byteCount += this.docBytes(doc);
        DocRef docRef = new DocRef(uri);
        doc.setDocRef(docRef);
        heap.insert(docRef);
        this.limitCheck();
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
    private void shrinkHeap(URI uri, DocumentImpl doc){
        doc.setLastUseTime(Long.MIN_VALUE);
        heap.reHeapify(doc.getDocRef());
        heap.removeMin();
        try {
            bTree.put(uri, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //To be called when the doc/memory limit is reached, and the least recently used doc must be fully obliterated
    private void shrinkHeap(){
        DocRef docRef = (DocRef) heap.removeMin();
        DocumentImpl doc = (DocumentImpl) bTree.get(docRef.uri);
        documentCount--;
        byteCount -= this.docBytes(doc);
        URI key = doc.getKey();
        try {
            bTree.moveToDisk(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int docBytes(DocumentImpl doc){
        int pdfBytes = doc.getDocumentAsPdf().length;
        int stringBytes = doc.getDocumentAsTxt().getBytes().length;
        return pdfBytes + stringBytes;
    }

    private void trieInsert(URI uri, DocumentImpl doc){
        if(doc == null){
            return;
        }
        String[] words = this.getDocumentAsWords(doc);
        for(String word : words){
            trie.put(word, uri);
        }
    }

    private void trieRemove(URI uri, DocumentImpl doc){
        if(doc == null){
            return;
        }
        String[] words = this.getDocumentAsWords(doc);
        for(String word : words){
            trie.delete(word, uri);
        }
    }

    //If original input was the first time using a URI, oldDoc will be null, and the put statement will be interpreted as a delete.
    //If input was an override, the put statement will place previous doc into bTree.
    private GenericCommand<URI> putCommand(URI incomingUri, DocumentImpl oldDoc, DocumentImpl newDoc){
        Function<URI,Boolean> undo = (uri) -> {
            //when undoing an override, the oldDoc is put back into heap and the newDoc is removed from heap
            boolean onDisk = this.onDisk(uri);
            if(onDisk == false) {
                this.deleteMemoryManagement(uri);
                this.shrinkHeap(uri, (DocumentImpl) bTree.get(uri));
            }
            bTree.put(uri, oldDoc);
            if(oldDoc != null) {
                oldDoc.setLastUseTime(System.nanoTime());
                this.addToHeap(uri, oldDoc);
            }
            this.trieInsert(uri, oldDoc);
            this.trieRemove(uri, newDoc);
            return true;
        };
        GenericCommand<URI> command = new GenericCommand<URI>(incomingUri, undo);
        stack.push(command);
        return command;
    }

    public boolean deleteDocument(URI uri) {
        boolean onDisk = this.onDisk(uri);
        DocumentImpl doc = (DocumentImpl)bTree.get(uri);
        if(doc == null) {
            GenericCommand<URI> command = this.deleteCommand(uri, null);
            stack.push(command);
            return false;
        }
        else{
            if(onDisk == false){
                this.deleteMemoryManagement(uri);
                this.shrinkHeap(uri, doc);
            }
            this.trieRemove(uri, doc);
            GenericCommand<URI> command = this.deleteCommand(uri, doc);
            stack.push(command);
            bTree.put(uri,null);
            return true;
        }
    }

    private GenericCommand<URI> deleteCommand(URI incomingUri, DocumentImpl doc){
        Function<URI,Boolean> undo = (uri) -> {
            bTree.put(uri, doc);
            this.trieInsert(uri, doc);
            if(doc != null) {
                doc.setLastUseTime(System.nanoTime());
                this.addToHeap(uri, doc);
                this.limitCheck();
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

    //if uri is on disk, add totals to storage
    private void memoryManagement(URI uri){
        if(this.onDisk(uri)){
            DocumentImpl doc = (DocumentImpl)bTree.get(uri);
            documentCount ++;
            byteCount += this.docBytes(doc);
            DocRef docRef = new DocRef(uri);
            doc.setDocRef(docRef);
            heap.insert(docRef);
        }
    }

    //subtract totals from storage
    private void deleteMemoryManagement(URI uri){
        DocumentImpl doc = (DocumentImpl)bTree.get(uri);
        documentCount --;
        byteCount -= this.docBytes(doc);
    }

    public List<String> search(String keyword) {
        String keywordMod = this.textMod(keyword);
        Comparator<URI> comparator = (URI uri1, URI uri2) -> {
            this.memoryManagement(uri1);
            this.memoryManagement(uri2);
            DocumentImpl doc1 = (DocumentImpl) bTree.get(uri1);
            DocumentImpl doc2 = (DocumentImpl) bTree.get(uri2);
            return (doc2.wordCount(keywordMod) - doc1.wordCount(keywordMod));
        };
        List<URI> uriList= trie.getAllSorted(keywordMod, comparator);
        List<String> docAsString = new ArrayList<>();
        long time = System.nanoTime();
        for(URI uri : uriList){
            memoryManagement(uri);
            DocumentImpl doc = (DocumentImpl)bTree.get(uri);
            doc.setLastUseTime(time);
            heap.reHeapify(doc.getDocRef());
            String text = doc.getDocumentAsTxt();
            docAsString.add(text);
        }
        this.limitCheck();
        return docAsString;
    }

    public List<byte[]> searchPDFs(String keyword) {
        String keywordMod = this.textMod(keyword);
        Comparator<URI> comparator = (URI uri1, URI uri2) -> {
            this.memoryManagement(uri1);
            this.memoryManagement(uri2);
            DocumentImpl doc1 = (DocumentImpl) bTree.get(uri1);
            DocumentImpl doc2 = (DocumentImpl) bTree.get(uri2);
            return (doc2.wordCount(keywordMod) - doc1.wordCount(keywordMod));
        };
        List<URI> uriList= trie.getAllSorted(keywordMod, comparator);
        List<byte[]> docAsPdf = new ArrayList<>();
        long time = System.nanoTime();
        for(URI uri : uriList){
            DocumentImpl doc = (DocumentImpl)bTree.get(uri);
            doc.setLastUseTime(time);
            heap.reHeapify(doc.getDocRef());
            byte[] pdf = doc.getDocumentAsPdf();
            docAsPdf.add(pdf);
        }
        this.limitCheck();
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
        Comparator<URI> comparator = (URI uri1, URI uri2) -> {
            this.memoryManagement(uri1);
            this.memoryManagement(uri2);
            DocumentImpl doc1 = (DocumentImpl) bTree.get(uri1);
            DocumentImpl doc2 = (DocumentImpl) bTree.get(uri2);
            return (this.prefixCount(prefixMod, doc2)) - (this.prefixCount(prefixMod, doc1));
        };
        List<URI> uriList= trie.getAllWithPrefixSorted(prefixMod, comparator);
        List<String> docAsString = new ArrayList<>();
        long time = System.nanoTime();
        for(URI uri : uriList){
            DocumentImpl doc = (DocumentImpl) bTree.get(uri);
            doc.setLastUseTime(time);
            heap.reHeapify(doc.getDocRef());
            String text = doc.getDocumentAsTxt();
            docAsString.add(text);
        }
        this.limitCheck();
        return docAsString;
    }

    public List<byte[]> searchPDFsByPrefix(String prefix) {
        String prefixMod = this.textMod(prefix);
        Comparator<URI> comparator = (URI uri1, URI uri2) -> {
            this.memoryManagement(uri1);
            this.memoryManagement(uri2);
            DocumentImpl doc1 = (DocumentImpl) bTree.get(uri1);
            DocumentImpl doc2 = (DocumentImpl) bTree.get(uri2);
            return (this.prefixCount(prefixMod, doc2)) - (this.prefixCount(prefixMod, doc1));
        };
        List<URI> uriList= trie.getAllWithPrefixSorted(prefixMod, comparator);
        List<byte[]> docAsPdf = new ArrayList<>();
        long time = System.nanoTime();
        for(URI uri : uriList){
            DocumentImpl doc = (DocumentImpl)bTree.get(uri);
            doc.setLastUseTime(time);
            heap.reHeapify(doc.getDocRef());
            byte[] pdf = doc.getDocumentAsPdf();
            docAsPdf.add(pdf);
        }
        this.limitCheck();
        return docAsPdf;
    }

    public Set<URI> deleteAll(String keyword) {
        String keyMod = this.textMod(keyword);
        Comparator<URI> comparator = (URI uri1, URI uri2) -> {
            this.memoryManagement(uri1);
            this.memoryManagement(uri2);
            DocumentImpl doc1 = (DocumentImpl) bTree.get(uri1);
            DocumentImpl doc2 = (DocumentImpl) bTree.get(uri2);
            return (doc2.wordCount(keyMod) - doc1.wordCount(keyMod));
        };
        List<URI> uriList = trie.getAllSorted(keyMod, comparator);
        return this.deleteAllExecute(uriList);
    }

    public Set<URI> deleteAllWithPrefix(String prefix) {
        String prefixMod = this.textMod(prefix);
        Comparator<URI> comparator = (URI uri1, URI uri2) -> {
            this.memoryManagement(uri1);
            this.memoryManagement(uri2);
            DocumentImpl doc1 = (DocumentImpl) bTree.get(uri1);
            DocumentImpl doc2 = (DocumentImpl) bTree.get(uri2);
            return (this.prefixCount(prefixMod, doc2)) - (this.prefixCount(prefixMod, doc1));
        };
        List<URI> uriList= trie.getAllWithPrefixSorted(prefixMod, comparator);
        return this.deleteAllExecute(uriList);
    }

    private Set<URI> deleteAllExecute(List<URI> uriList){
        List<URI> uriListTwo = new ArrayList<>();
        CommandSet<URI> commandSet = new CommandSet<URI>();
        Set<URI> uriSet = new HashSet<URI>();
        for(URI uri : uriList) {
            uriSet.add(uri);
            if ((DocumentImpl) bTree.get(uri) == null) {
                GenericCommand<URI> command = this.deleteCommand(uri, null);
                commandSet.addCommand(command);
            } else {
                uriListTwo.add(uri);
            }
        }
        for(URI uriTwo : uriListTwo) {
            this.trieRemove(uriTwo, (DocumentImpl) bTree.get(uriTwo));
            GenericCommand<URI> command = this.deleteCommand(uriTwo, (DocumentImpl)bTree.get(uriTwo));
            commandSet.addCommand(command);
            this.deleteMemoryManagement(uriTwo);
            this.shrinkHeap(uriTwo, (DocumentImpl) bTree.get(uriTwo));
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
        if (bTree.get(uri) != null) {
            DocumentImpl doc = (DocumentImpl) bTree.get(uri);
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
        this.memoryManagement(uri);
        DocumentImpl doc = (DocumentImpl)bTree.get(uri);
        if(doc == null){
            return null;
        }
        byte[] pdf= doc.getDocumentAsPdf();
        this.limitCheck();
        doc.setLastUseTime(System.nanoTime());
        heap.reHeapify(doc.getDocRef());
        return pdf;
    }

    public String getDocumentAsTxt(URI uri) {
        this.memoryManagement(uri);
        DocumentImpl doc = (DocumentImpl)bTree.get(uri);
        if(doc == null){
            return null;
        }
        String text = doc.getDocumentAsTxt();
        this.limitCheck();
        doc.setLastUseTime(System.nanoTime());
        heap.reHeapify(doc.getDocRef());
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

    protected Document getDocument(URI uri){
        DocumentImpl doc;
        if(this.onDisk(uri)){
            return null;
        }
        else{
            doc = (DocumentImpl)bTree.get(uri);
        }
        return doc;
    }

    private boolean onDisk(URI uri){
        File tempFile = new File(directory + File.separator + uri.getRawAuthority() + uri.getRawPath() + ".json");
        return tempFile.exists();
    }

    protected class DocRef implements Comparable<DocRef> {
        private URI uri;
        DocRef(URI uri){
            this.uri = uri;
        }
        @Override
        public int compareTo(DocRef o){
            DocumentImpl doc1 = (DocumentImpl)bTree.get(uri);
            DocumentImpl doc2 = (DocumentImpl)bTree.get(o.uri);
            if((doc1.getLastUseTime() > doc2.getLastUseTime())){
                return 1;
            }
            else{
                return -1;
            }
        }
    }
}
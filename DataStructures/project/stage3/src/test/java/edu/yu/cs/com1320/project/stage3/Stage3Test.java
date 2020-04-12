package edu.yu.cs.com1320.project.stage3;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage3.impl.DocumentStoreImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Stage3Test {

    //variables to hold possible values for doc1
    private URI uri1;
    private String txt1;
    private byte[] pdfData1;
    private String pdfTxt1;

    //variables to hold possible values for doc2
    private URI uri2;
    private String txt2;
    private byte[] pdfData2;
    private String pdfTxt2;

    //variables to hold possible values for doc3
    private URI uri3;
    private String txt3;
    private byte[] pdfData3;
    private String pdfTxt3;

    //variables to hold possible values for doc4
    private URI uri4;
    private String txt4;
    private byte[] pdfData4;
    private String pdfTxt4;

    //variables to hold possible values for doc5
    private URI uri5;
    private String txt5;
    private byte[] pdfData5;
    private String pdfTxt5;

    //variables to hold possible values for doc6
    private URI uri6;
    private String txt6;
    private byte[] pdfData6;
    private String pdfTxt6;

    //variables to hold possible values for doc7
    private URI uri7;
    private String txt7;
    private byte[] pdfData7;
    private String pdfTxt7;

    //variables to hold possible values for doc8
    private URI uri8;
    private String txt8;
    private byte[] pdfData8;
    private String pdfTxt8;

    //variables to hold possible values for doc9
    private URI uri9;
    private String txt9;
    private byte[] pdfData9;
    private String pdfTxt9;

    //variables to hold possible values for doc10
    private URI uri10;
    private String txt10;
    private byte[] pdfData10;
    private String pdfTxt10;

    //variables to hold possible values for doc11
    private URI uri11;
    private String txt11;
    private byte[] pdfData11;
    private String pdfTxt11;

    //variables to hold possible values for doc12
    private URI uri12;
    private String txt12;
    private byte[] pdfData12;
    private String pdfTxt12;

    //variables to hold possible values for doc13
    private URI uri13;
    private String txt13;
    private byte[] pdfData13;
    private String pdfTxt13;

    //variables to hold possible values for doc14
    private URI uri14;
    private String txt14;
    private byte[] pdfData14;
    private String pdfTxt14;

    //variables to hold possible values for doc15
    //Note: uri15 is the same as uri14
    private URI uri15;
    private String txt15;
    private byte[] pdfData15;
    private String pdfTxt15;

    @Before
    public void init() throws Exception {
        //init possible values for doc1
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";
        this.pdfTxt1 = "This is some PDF text for word doc1, hat tip to Adobe. 72";
        this.pdfData1 = Utils.textToPdfData(this.pdfTxt1);

        //init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Text for doc2. A plain old String.";
        this.pdfTxt2 = "PDF content for doc2: PDF word word format was opened in 2008. 72 72 72";
        this.pdfData2 = Utils.textToPdfData(this.pdfTxt2);

        //init possible values for doc3
        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "This is the text of doc3";
        this.pdfTxt3 = "This is some PDF text for word word word doc3, hat tip to Adobe.";
        this.pdfData3 = Utils.textToPdfData(this.pdfTxt3);

        //init possible values for doc4
        this.uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        this.txt4 = "Text for doc4. A plain old String.";
        this.pdfTxt4 = "PDF content for doc4: PDF word word word word format was opened in 2008.";
        this.pdfData4 = Utils.textToPdfData(this.pdfTxt4);

        //init possible values for doc5
        this.uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
        this.txt5 = "This is the text of doc5, in plain text. No fancy file format - just plain old String";
        this.pdfTxt5 = "This is some PDF text for word word word word word doc5, hat tip to Adobe.";
        this.pdfData5 = Utils.textToPdfData(this.pdfTxt5);

        //init possible values for doc6
        this.uri6 = new URI("http://edu.yu.cs/com1320/project/doc6");
        this.txt6 = "Text for doc6. A plain old String.";
        this.pdfTxt6 = "PDF content for doc6: PDF word word word word word word format was opened in 2008.";
        this.pdfData6 = Utils.textToPdfData(this.pdfTxt6);

        //init possible values for doc7
        this.uri7 = new URI("http://edu.yu.cs/com1320/project/doc7");
        this.txt7 = "This is the text of doc7";
        this.pdfTxt7 = "This is some PDF text for word word tee tee word word word word word doc7, hat tip to Adobe.";
        this.pdfData7 = Utils.textToPdfData(this.pdfTxt7);

        //init possible values for doc8
        this.uri8 = new URI("http://edu.yu.cs/com1320/project/doc8");
        this.txt8 = "Text for doc8. A plain old String.";
        this.pdfTxt8 = "PDF content for doc8: PDF word teeshirt teeball word word word word teedb teehetrtr word word word format was opened in 2008.";
        this.pdfData8 = Utils.textToPdfData(this.pdfTxt8);

        //init possible values for doc9
        this.uri9 = new URI("http://edu.yu.cs/com1320/project/doc9");
        this.txt9 = "This is the text of doc9, in plain text. No fancy file format - just plain old String";
        this.pdfTxt9 = "This is some PDF text teeshirt teeball tee for word word word word word word word word word doc9, hat tip to Adobe.";
        this.pdfData9 = Utils.textToPdfData(this.pdfTxt9);

        //init possible values for doc10
        this.uri10 = new URI("http://edu.yu.cs/com1320/project/doc10");
        this.txt10 = "Text for doc10. A plain old String.";
        this.pdfTxt10 = "PDF content for doc10: word word word word t#%Ee t%Ee t#%Ee Tee%# tee t%#ee t:^#ee word word word word word word PDF format was opened in 2008.";
        this.pdfData10 = Utils.textToPdfData(this.pdfTxt10);

        //init possible values for doc11
        this.uri11 = new URI("http://edu.yu.cs/com1320/project/doc11");
        this.txt11 = "This is the text of doc11, in plain text. No fancy file format - just plain old String";
        this.pdfTxt11 = "This is some PDF text for word word word word tip word word word word word word word doc11, hat tip to Adobe.";
        this.pdfData11 = Utils.textToPdfData(this.pdfTxt11);

        //init possible values for doc12
        this.uri12 = new URI("http://edu.yu.cs/com1320/project/doc12");
        this.txt12 = "Text for doc12. A plain old String.";
        this.pdfTxt12 = "PDF content for doc12: PDF word word word word word word word word word word word word format was opened in 2008. PDF";
        this.pdfData12 = Utils.textToPdfData(this.pdfTxt12);

        //init possible values for doc13
        this.uri13 = new URI("http://edu.yu.cs/com1320/project/doc13");
        this.txt13 = "This is the text of doc13";
        this.pdfTxt13 = "This is some PDF text for word word word word word word word word word word word word word doc13, hat tip tip tip to Adobe.";
        this.pdfData13 = Utils.textToPdfData(this.pdfTxt13);

        //init possible values for doc14
        this.uri14 = new URI("http://edu.yu.cs/com1320/project/doc14");
        this.txt14 = "Text for doc14. A plain old String.";
        this.pdfTxt14 = "PDF content for doc14: PDF word word word word word word word word word word word word word word format was opened in 2008.";
        this.pdfData14 = Utils.textToPdfData(this.pdfTxt14);

        //init possible values for doc15
        this.uri15 = new URI("http://edu.yu.cs/com1320/project/doc14");
        this.txt15 = "Text for doc15. A plain old String.";
        this.pdfTxt15 = "PDF content for doc15: PDF format was opened in 2008.";
        this.pdfData15 = Utils.textToPdfData(this.pdfTxt15);
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionTest() {
        DocumentStoreImpl store = new DocumentStoreImpl();
        int returned3 = store.putDocument(new ByteArrayInputStream(this.pdfData3), this.uri3, DocumentStore.DocumentFormat.PDF);
        store.undo(uri1);
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionTestTwo() {
        DocumentStoreImpl store = new DocumentStoreImpl();
        int returned8 = store.putDocument(new ByteArrayInputStream(this.pdfData8), this.uri8, DocumentStore.DocumentFormat.PDF);
        int returned9 = store.putDocument(new ByteArrayInputStream(this.pdfData9), this.uri9, DocumentStore.DocumentFormat.PDF);
        int returned10 = store.putDocument(new ByteArrayInputStream(this.pdfData10), this.uri10, DocumentStore.DocumentFormat.PDF);
        store.undo(uri8);
        store.undo();
        store.undo();
        store.undo();
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThree() {
        DocumentStoreImpl store = new DocumentStoreImpl();
        int returned1 = store.putDocument(null, this.uri1, DocumentStore.DocumentFormat.PDF);
        store.undo();
        store.undo();
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionTestFour() {
        DocumentStoreImpl store = new DocumentStoreImpl();
        int returned8 = store.putDocument(new ByteArrayInputStream(this.pdfData7), this.uri7, DocumentStore.DocumentFormat.PDF);
        int returned9 = store.putDocument(new ByteArrayInputStream(this.pdfData9), this.uri9, DocumentStore.DocumentFormat.PDF);
        int returned10 = store.putDocument(new ByteArrayInputStream(this.pdfData11), this.uri11, DocumentStore.DocumentFormat.PDF);
        store.deleteAll("some");
        store.undo();
        store.undo();
        store.undo();
        store.undo();
        store.undo();
    }

    @Test
    public void noContentInSearch() {
        DocumentStoreImpl store = new DocumentStoreImpl();
        int returned7 = store.putDocument(new ByteArrayInputStream(this.pdfData7), this.uri7, DocumentStore.DocumentFormat.PDF);
        int returned9 = store.putDocument(new ByteArrayInputStream(this.pdfData9), this.uri9, DocumentStore.DocumentFormat.PDF);
        int returned11 = store.putDocument(new ByteArrayInputStream(this.pdfData11), this.uri11, DocumentStore.DocumentFormat.PDF);
        List<String> list= store.search("gibbrish");
        List<byte[]> listPDFs= store.searchPDFs("gibbrish");
        List<String> prefixList= store.searchByPrefix("blah");
        List<byte[]> prefixListPDFs= store.searchPDFsByPrefix("blah");
        assertEquals(0, list.size());
        assertEquals(0, prefixList.size());
        assertEquals(0, listPDFs.size());
        assertEquals(0, prefixListPDFs.size());
    }

    @Test
    public void noContentInDelete(){
        DocumentStoreImpl store = new DocumentStoreImpl();
        int returned7 = store.putDocument(new ByteArrayInputStream(this.pdfData7), this.uri7, DocumentStore.DocumentFormat.PDF);
        int returned9 = store.putDocument(new ByteArrayInputStream(this.pdfData9), this.uri9, DocumentStore.DocumentFormat.PDF);
        int returned11 = store.putDocument(new ByteArrayInputStream(this.pdfData11), this.uri11, DocumentStore.DocumentFormat.PDF);
        Set<URI> set= store.deleteAll("gibbrish");
        Set<URI> prefixSet = store.deleteAllWithPrefix("blah");
        assertEquals(0, set.size());
        assertEquals(0, prefixSet.size());
    }

    @Test
    public void emptyCommand() {
        DocumentStoreImpl store = new DocumentStoreImpl();
        int returned1 = store.putDocument(null, this.uri1, DocumentStore.DocumentFormat.PDF);
        store.undo();
        assertEquals(null, store.getDocumentAsTxt(this.uri1));
    }

    @Test
    public void trieTestWithTxt(){
        DocumentStoreImpl store = new DocumentStoreImpl();
        try {
            int returned1 = store.putDocument(new ByteArrayInputStream(Utils.textToPdfData(this.txt1)), this.uri1, DocumentStore.DocumentFormat.PDF);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> list = store.search("TEXT");
        assertEquals(txt1, list.get(0));
    }

    @Test
    public void trieTest(){
        DocumentStoreImpl store = new DocumentStoreImpl();
        int returned1 = store.putDocument(new ByteArrayInputStream(this.pdfData1), this.uri1, DocumentStore.DocumentFormat.PDF);
        int returned2 = store.putDocument(new ByteArrayInputStream(this.pdfData2), this.uri2, DocumentStore.DocumentFormat.PDF);
        int returned3 = store.putDocument(new ByteArrayInputStream(this.pdfData3), this.uri3, DocumentStore.DocumentFormat.PDF);
        int returned4 = store.putDocument(new ByteArrayInputStream(this.pdfData4), this.uri4, DocumentStore.DocumentFormat.PDF);
        int returned5 = store.putDocument(new ByteArrayInputStream(this.pdfData5), this.uri5, DocumentStore.DocumentFormat.PDF);
        int returned6 = store.putDocument(new ByteArrayInputStream(this.pdfData6), this.uri6, DocumentStore.DocumentFormat.PDF);
        int returned7 = store.putDocument(new ByteArrayInputStream(this.pdfData7), this.uri7, DocumentStore.DocumentFormat.PDF);
        int returned8 = store.putDocument(new ByteArrayInputStream(this.pdfData8), this.uri8, DocumentStore.DocumentFormat.PDF);
        int returned9 = store.putDocument(new ByteArrayInputStream(this.pdfData9), this.uri9, DocumentStore.DocumentFormat.PDF);
        int returned10 = store.putDocument(new ByteArrayInputStream(this.pdfData10), this.uri10, DocumentStore.DocumentFormat.PDF);
        int returned11 = store.putDocument(new ByteArrayInputStream(this.pdfData11), this.uri11, DocumentStore.DocumentFormat.PDF);
        int returned12 = store.putDocument(new ByteArrayInputStream(this.pdfData12), this.uri12, DocumentStore.DocumentFormat.PDF);
        int returned13 = store.putDocument(new ByteArrayInputStream(this.pdfData13), this.uri13, DocumentStore.DocumentFormat.PDF);
        int returned14 = store.putDocument(new ByteArrayInputStream(this.pdfData14), this.uri14, DocumentStore.DocumentFormat.PDF);
    }

    @Test
    public void test() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        int returned1 = store.putDocument(new ByteArrayInputStream(this.pdfData1), this.uri1, DocumentStore.DocumentFormat.PDF);
        int returned2 = store.putDocument(new ByteArrayInputStream(this.pdfData2), this.uri2, DocumentStore.DocumentFormat.PDF);
        int returned3 = store.putDocument(new ByteArrayInputStream(this.pdfData3), this.uri3, DocumentStore.DocumentFormat.PDF);
        store.undo(uri3);
        int returned4 = store.putDocument(new ByteArrayInputStream(this.pdfData4), this.uri4, DocumentStore.DocumentFormat.PDF);
        int returned5 = store.putDocument(new ByteArrayInputStream(this.pdfData5), this.uri5, DocumentStore.DocumentFormat.PDF);
        int returned6 = store.putDocument(new ByteArrayInputStream(this.pdfData6), this.uri6, DocumentStore.DocumentFormat.PDF);
        int returned7 = store.putDocument(new ByteArrayInputStream(this.pdfData7), this.uri7, DocumentStore.DocumentFormat.PDF);
        int returned8 = store.putDocument(new ByteArrayInputStream(this.pdfData8), this.uri8, DocumentStore.DocumentFormat.PDF);
        int returned9 = store.putDocument(new ByteArrayInputStream(this.pdfData9), this.uri9, DocumentStore.DocumentFormat.PDF);
        int returned10 = store.putDocument(new ByteArrayInputStream(this.pdfData10), this.uri10, DocumentStore.DocumentFormat.PDF);
        int returned11 = store.putDocument(new ByteArrayInputStream(this.pdfData11), this.uri11, DocumentStore.DocumentFormat.PDF);
        int returned12 = store.putDocument(new ByteArrayInputStream(this.pdfData12), this.uri12, DocumentStore.DocumentFormat.PDF);
        int returned13 = store.putDocument(new ByteArrayInputStream(this.pdfData13), this.uri13, DocumentStore.DocumentFormat.PDF);
        int returned14 = store.putDocument(new ByteArrayInputStream(this.pdfData14), this.uri14, DocumentStore.DocumentFormat.PDF);
        int returned14new = store.putDocument(new ByteArrayInputStream(this.pdfData15), this.uri15, DocumentStore.DocumentFormat.PDF);
        assertEquals(pdfTxt15, store.getDocumentAsTxt(this.uri14));
        assertEquals(pdfTxt15, store.getDocumentAsTxt(this.uri15));
        assertEquals(pdfTxt5, store.getDocumentAsTxt(this.uri5));
        store.undo(uri15);
        store.undo(uri5);
        store.undo(uri6);
        assertEquals(pdfTxt14, store.getDocumentAsTxt(this.uri14));
        assertEquals(pdfTxt14, store.getDocumentAsTxt(this.uri15));
        assertEquals(pdfTxt1, store.getDocumentAsTxt(this.uri1));
        assertEquals(pdfTxt2, store.getDocumentAsTxt(this.uri2));
        assertEquals(pdfTxt4, store.getDocumentAsTxt(this.uri4));
        assertEquals(null, store.getDocumentAsTxt(this.uri3));
        assertEquals(null, store.getDocumentAsTxt(this.uri5));
        store.undo(uri14);
        assertEquals(null, store.getDocumentAsTxt(this.uri14));
        assertEquals(null, store.getDocumentAsTxt(this.uri15));
        assertEquals(null, store.getDocumentAsTxt(this.uri3));
        store.deleteDocument(uri13);
        store.deleteDocument(uri1);
        store.deleteDocument(uri2);
        assertEquals(null, store.getDocumentAsTxt(this.uri1));
        assertEquals(null, store.getDocumentAsTxt(this.uri2));
        assertEquals(null, store.getDocumentAsTxt(this.uri13));
        store.undo(uri1);
        store.undo();
        assertEquals(pdfTxt1, store.getDocumentAsTxt(this.uri1));
        assertEquals(null, store.getDocumentAsTxt(this.uri13));
        assertEquals(pdfTxt2, store.getDocumentAsTxt(this.uri2));
        //all docs, besides 3,5,6,13,14,15
        //test search
        List<String> list= store.search("WORD");
        assertEquals(9, list.size());
        assertEquals(pdfTxt12, list.get(0));
        assertEquals(pdfTxt11, list.get(1));
        assertEquals(pdfTxt10, list.get(2));
        assertEquals(pdfTxt9, list.get(3));
        assertEquals(pdfTxt8, list.get(4));
        assertEquals(pdfTxt7, list.get(5));
        assertEquals(pdfTxt4, list.get(6));
        assertEquals(pdfTxt2, list.get(7));
        assertEquals(pdfTxt1, list.get(8));
        List<String> numberList= store.search("7# $^2");
        assertEquals(2, numberList.size());
        assertEquals(pdfTxt2, numberList.get(0));
        assertEquals(pdfTxt1, numberList.get(1));
        //test searchPDFs
        List<byte[]> listPdf= store.searchPDFs("WORD");
        assertEquals(9, listPdf.size());
        assertEquals(pdfTxt12, pdfToString(listPdf.get(0)));
        assertEquals(pdfTxt9, pdfToString(listPdf.get(3)));
        assertEquals(pdfTxt4, pdfToString(listPdf.get(6)));
        assertEquals(pdfTxt1, pdfToString(listPdf.get(8)));
        //test searchByPrefix
        List<String> prefixStringList = store.searchByPrefix("TEE   ");
        assertEquals(4, prefixStringList.size());
        assertEquals(pdfTxt10, prefixStringList.get(0));
        assertEquals(pdfTxt8, prefixStringList.get(1));
        assertEquals(pdfTxt9, prefixStringList.get(2));
        assertEquals(pdfTxt7, prefixStringList.get(3));
        //test searchPDFsByPrefix
        List<byte[]> prefixPdfList = store.searchPDFsByPrefix("Te)E");
        assertEquals(4, prefixPdfList.size());
        assertEquals(pdfTxt10, pdfToString(prefixPdfList.get(0)));
        assertEquals(pdfTxt8, pdfToString(prefixPdfList.get(1)));
        assertEquals(pdfTxt9, pdfToString(prefixPdfList.get(2)));
        assertEquals(pdfTxt7, pdfToString(prefixPdfList.get(3)));
        //test deleteAll
        Set<URI> deletedSet = store.deleteAll("tip");
        assertEquals(4, deletedSet.size());
        Set<URI> deletedURIs = new HashSet<URI>();
        deletedURIs.add(this.uri1);
        deletedURIs.add(this.uri7);
        deletedURIs.add(this.uri9);
        deletedURIs.add(this.uri11);
        assertEquals(true, deletedSet.containsAll(deletedURIs));
        assertEquals(null, store.getDocumentAsTxt(this.uri1));
        assertEquals(null, store.getDocumentAsTxt(this.uri7));
        assertEquals(null, store.getDocumentAsTxt(this.uri9));
        assertEquals(null, store.getDocumentAsTxt(this.uri11));
        store.undo();
        assertEquals(pdfTxt1, store.getDocumentAsTxt(this.uri1));
        assertEquals(pdfTxt7, store.getDocumentAsTxt(this.uri7));
        assertEquals(pdfTxt9, store.getDocumentAsTxt(this.uri9));
        assertEquals(pdfTxt11, store.getDocumentAsTxt(this.uri11));
        assertEquals(pdfTxt8, store.getDocumentAsTxt(this.uri8));
        assertEquals(null, store.getDocumentAsTxt(this.uri13));
        //test deleteAllWithPrefix
        store.deleteAllWithPrefix("TE#^#%^E");
        assertEquals(null, store.getDocumentAsTxt(this.uri7));
        assertEquals(null, store.getDocumentAsTxt(this.uri8));
        assertEquals(null, store.getDocumentAsTxt(this.uri9));
        assertEquals(null, store.getDocumentAsTxt(this.uri10));
        store.undo(uri8);
        assertEquals(null, store.getDocumentAsTxt(this.uri7));
        assertEquals(pdfTxt8, store.getDocumentAsTxt(this.uri8));
        assertEquals(null, store.getDocumentAsTxt(this.uri9));
        assertEquals(null, store.getDocumentAsTxt(this.uri10));
        store.undo();
        assertEquals(pdfTxt7, store.getDocumentAsTxt(this.uri7));
        assertEquals(pdfTxt8, store.getDocumentAsTxt(this.uri8));
        assertEquals(pdfTxt9, store.getDocumentAsTxt(this.uri9));
        assertEquals(pdfTxt10, store.getDocumentAsTxt(this.uri10));
        //all docs, besides 3,5,6,13,14,15
        store.undo();
        assertEquals(pdfTxt13, store.getDocumentAsTxt(this.uri13));
    }

    //To carry out test properly, make the following temporarily public, change size of hashArray to 1, and check the results of the printlns:
    //hashTableImpl instance variable of DocumentStoreImpl
    //instance variables of HashTableImpl
    //ManualLinkedList inner class of HashTableImpl
//    @Test
//    public void test(){
//        DocumentStoreImpl store = new DocumentStoreImpl();
//        HashTableImpl hash = store.hashTable;
//        int elementcount = hash.elementCount;
//        int returned1 = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 1: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned2 = store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri2, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 2: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned3 = store.putDocument(new ByteArrayInputStream(this.pdfData3),this.uri3, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 3: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned4 = store.putDocument(new ByteArrayInputStream(this.pdfData4),this.uri4, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 4: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned5 = store.putDocument(new ByteArrayInputStream(this.pdfData5),this.uri5, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 5: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned6 = store.putDocument(new ByteArrayInputStream(this.pdfData6),this.uri6, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 6: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned7 = store.putDocument(new ByteArrayInputStream(this.pdfData7),this.uri7, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 7: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned8 = store.putDocument(new ByteArrayInputStream(this.pdfData8),this.uri8, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 8: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned9 = store.putDocument(new ByteArrayInputStream(this.pdfData9),this.uri9, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 9: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned10 = store.putDocument(new ByteArrayInputStream(this.pdfData10),this.uri10, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 10: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned11 = store.putDocument(new ByteArrayInputStream(this.pdfData11),this.uri11, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 11: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned12 = store.putDocument(new ByteArrayInputStream(this.pdfData12),this.uri12, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 12: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned13 = store.putDocument(new ByteArrayInputStream(this.pdfData13),this.uri13, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 13: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned14 = store.putDocument(new ByteArrayInputStream(this.pdfData14),this.uri14, DocumentStore.DocumentFormat.PDF);
//        System.out.println("post-insertion 14: array length-- " + hash.hashArray.length + ".  Element count-- " + hash.elementCount);
//        int returned14new = store.putDocument(new ByteArrayInputStream(this.pdfData15),this.uri15, DocumentStore.DocumentFormat.PDF);
//        assertTrue(returned1 == 0);
//        assertTrue(returned2 == 0);
//        assertTrue(returned3 == 0);
//        assertTrue(returned4 == 0);
//        assertTrue(returned5 == 0);
//        assertTrue(returned6 == 0);
//        assertTrue(returned7 == 0);
//        assertTrue(returned8 == 0);
//        assertTrue(returned9 == 0);
//        assertTrue(returned10 == 0);
//        assertTrue(returned11 == 0);
//        assertTrue(returned12 == 0);
//        assertTrue(returned13 == 0);
//        assertTrue(returned14 == 0);
//        assertEquals(returned14new, this.pdfTxt14.hashCode());
//        assertEquals("failed to return correct pdf text",this.pdfTxt1,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
//    }

    private String pdfToString(byte[] bytes){
        try {
            PDFTextStripper stripper = new PDFTextStripper();
            PDDocument pdDoc = PDDocument.load(bytes);
            String text = stripper.getText(pdDoc);
            text = text.trim();
            pdDoc.close();
            text.trim();
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

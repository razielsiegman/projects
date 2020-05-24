package edu.yu.cs.com1320.project.stage5;

import edu.yu.cs.com1320.project.BTree;
import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage5.impl.DocumentPersistenceManager;
import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BTreeTest {

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

    @Test
    public void bTreeTest() throws Exception {
        DocumentImpl doc1 = new DocumentImpl(uri1, txt1, txt1.hashCode());
        DocumentImpl doc2 = new DocumentImpl(uri2, txt2, txt2.hashCode());
        PersistenceManager pm = null;
        try {
            pm = new DocumentPersistenceManager(new File(System.getProperty("user.dir")));
        } catch (IOException e) {
            e.printStackTrace();
        }        BTree bTree = new BTreeImpl();
        bTree.setPersistenceManager(pm);
        bTree.put(uri1, doc1);
        assertEquals(doc1, bTree.get(uri1));
        bTree.moveToDisk(uri1);
        DocumentImpl doc1retrieved = (DocumentImpl) bTree.get(uri1);
        assertEquals(doc1retrieved.getDocumentAsTxt(), doc1.getDocumentAsTxt());
        bTree.put(uri2, doc2);
        assertEquals(doc2, bTree.get(uri2));
        bTree.moveToDisk(uri2);
        DocumentImpl doc2retrieved = (DocumentImpl) bTree.get(uri2);
        assertEquals(doc2retrieved.getDocumentAsTxt(), doc2.getDocumentAsTxt());
        DocumentImpl doc1retrievedx2 = (DocumentImpl) bTree.get(uri1);
        assertEquals(doc1retrievedx2.getDocumentAsTxt(), doc1.getDocumentAsTxt());
    }

}

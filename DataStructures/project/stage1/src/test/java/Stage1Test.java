import edu.yu.cs.com1320.project.stage1.DocumentStore;
import edu.yu.cs.com1320.project.stage1.impl.DocumentStoreImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class Stage1Test {

    @Test
    public void simpleTest() throws Exception {
        DocumentStoreImpl docStore = new DocumentStoreImpl();
        String initialString = "tex t";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        targetStream.close();
        String uriString = ("ftp://ftp.is.co.za/rfc/rfc1808.txt");
        URI uri = null;
        try {
            uri = new URI(uriString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        DocumentStore.DocumentFormat format = DocumentStore.DocumentFormat.TXT;
        int initialStringHash = docStore.putDocument(targetStream, uri, format);
        byte[] initialStringAsPdf = docStore.getDocumentAsPdf(uri);
        String initialStringAsText = docStore.getDocumentAsTxt(uri);
        // DocumentImpl doc = new DocumentImpl(uri, initialString, initialString.hashCode());
        // byte[] pdfFormat = doc.getDocumentAsPdf();
        String textFromPdf = this.pdfToString(initialStringAsPdf);
        assertEquals("testing", initialStringAsText, textFromPdf);
    }

    @Test
    public void bigTest() throws Exception{
        DocumentStoreImpl docStore = this.docStore();
        String uriStringTwo = ("https://example.com/path/resource.txt#fragment");
        String uriStringSeven = ("./resource.txt");
        URI uriTwo = null;
        URI uriSeven = null;
        try{
            uriTwo = new URI(uriStringTwo);
            uriSeven = new URI(uriStringSeven);
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
        String textTwoFromString = docStore.getDocumentAsTxt(uriTwo);
        String textSevenFromString = docStore.getDocumentAsTxt(uriSeven);
        byte[] pdfTwo = docStore.getDocumentAsPdf(uriTwo);
        byte[] pdfSeven = docStore.getDocumentAsPdf(uriSeven);
        String textTwoFromPdf = this.pdfToString(pdfTwo);
        String textSevenFromPdf = this.pdfToString(pdfSeven);
        String textTwo = "This is a second random text.Im using it as a second text to make sure that java knows what its doing.And Im also testing out the new line function";
        String textSeven = "Now, I'm going to make a couple strings that won't be inputted as PDFs.";
        assertEquals(textTwo, textTwoFromString);
        assertEquals(textSeven, textSevenFromString);
        assertEquals(textTwo, textTwoFromPdf);
        assertEquals(textSeven, textSevenFromPdf);
    }

    @Test
    public void testWithReplacedUris() throws Exception{
        DocumentStoreImpl docStore = this.docStore();
        String uriStringEight = ("resource.txt");
        String uriStringTen = ("ftp://ftp.is.co.za/rfc/rfc1808.txt");
        URI uriEight = null;
        URI uriTen = null;
        try{
            uriEight = new URI(uriStringEight);
            uriTen = new URI(uriStringTen);
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
        String textEightFromString = docStore.getDocumentAsTxt(uriEight);
        String textTenFromString = docStore.getDocumentAsTxt(uriTen);
        byte[] pdfEight = docStore.getDocumentAsPdf(uriEight);
        byte[] pdfTen = docStore.getDocumentAsPdf(uriTen);
        String textEightFromPdf = this.pdfToString(pdfEight);
        String textTenFromPdf = this.pdfToString(pdfTen);
        String textEight = "Here's one more.";
        String textTen = "text Ten!";
        assertEquals(textEight, textEightFromString);
        assertEquals(textTen, textTenFromString);
        assertEquals(textEight, textEightFromPdf);
        assertEquals(textTen, textTenFromPdf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTesterOne() throws Exception{
        DocumentStoreImpl docStore = this.docStore();
        URI uriThree = new URI("urn:ietf:rfc:2648");
        docStore.deleteDocument(uriThree);
        docStore.getDocumentAsPdf(uriThree);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTesterTwo() throws Exception{
        DocumentStoreImpl docStore = this.docStore();
        URI uriThree = new URI("urn:ietf:rfc:2648");
        docStore.deleteDocument(uriThree);
        docStore.getDocumentAsTxt(uriThree);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTesterUsingPutOne() throws Exception{
        DocumentStoreImpl docStore = this.docStore();
        URI uriNine = new URI("../resource.txt");
        DocumentStore.DocumentFormat docFormat = null;
        InputStream input = null;
        int deletedHash = docStore.putDocument(input, uriNine, docFormat);
        docStore.getDocumentAsPdf(uriNine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTesterUsingPutTwo() throws Exception{
        DocumentStoreImpl docStore = this.docStore();
        URI uriNine = new URI("../resource.txt");
        DocumentStore.DocumentFormat docFormat = null;
        InputStream input = null;
        int deletedHash = docStore.putDocument(input, uriNine, docFormat);
        docStore.getDocumentAsTxt(uriNine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionTesterOne() throws Exception{
        DocumentStoreImpl docStore = new DocumentStoreImpl();
        String initialString = "tex t";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        URI uri = null;
        DocumentStore.DocumentFormat docFormat = DocumentStore.DocumentFormat.TXT;
        docStore.putDocument(targetStream, uri, docFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionTesterTwo() throws Exception{
        DocumentStoreImpl docStore = new DocumentStoreImpl();
        String initialString = "tex t";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        URI uri = new URI("urn:ietf:rfc:2648");;
        DocumentStore.DocumentFormat docFormat = null;
        docStore.putDocument(targetStream, uri, docFormat);
    }

    public DocumentStoreImpl docStore() throws Exception {
        DocumentStoreImpl docStore = new DocumentStoreImpl();
        String textOneAsString = "This is a random text.  Im using it as textOne to make sure that java knows what its doing";
        byte[] textOneAsPdf = this.pdfCreator(textOneAsString);
        String textTwoAsString = "This is a second random text.Im using it as a second text to make sure that java knows what its doing.And Im also testing out the new line function";
        byte[] textTwoAsPdf = this.pdfCreator(textTwoAsString);
        String textThreeAsString = "This is a third random test.By now, statistically, my hashtable should probably be using its separate chaining to store this file";
        byte[] textThreeAsPdf = this.pdfCreator(textThreeAsString);
        String textFourAsString = this.longTextBuilder();
        byte[] textFourAsPdf = this.pdfCreator(textFourAsString);
        String textFiveAsString = "At this point, every value in the hashtable must contain something.";
        byte[] textFiveAsPdf = this.pdfCreator(textFiveAsString);
        String textSixAsString = "Now, separate chaining is certainly being implemented.";
        byte[] textSixAsPdf = this.pdfCreator(textSixAsString);
        String textSevenAsString = "Now, I'm going to make a couple strings that won't be inputted as PDFs.";
        String textEightAsString = "Here's one more.";
        String textNineAsString = "And, a couple more for fun(and to make sure that separate chaining works properly even when needing more than two slots in each array.";
        String textTenAsString = "text Ten!";
        String textElevenAsString = "And....  eleven";
        DocumentStore.DocumentFormat formatOne = DocumentStore.DocumentFormat.PDF;
        DocumentStore.DocumentFormat formatTwo = DocumentStore.DocumentFormat.PDF;
        DocumentStore.DocumentFormat formatThree = DocumentStore.DocumentFormat.PDF;
        DocumentStore.DocumentFormat formatFour = DocumentStore.DocumentFormat.PDF;
        DocumentStore.DocumentFormat formatFive = DocumentStore.DocumentFormat.PDF;
        DocumentStore.DocumentFormat formatSix = DocumentStore.DocumentFormat.PDF;
        DocumentStore.DocumentFormat formatSeven = DocumentStore.DocumentFormat.TXT;
        DocumentStore.DocumentFormat formatEight = DocumentStore.DocumentFormat.TXT;
        DocumentStore.DocumentFormat formatNine = DocumentStore.DocumentFormat.TXT;
        DocumentStore.DocumentFormat formatTen = DocumentStore.DocumentFormat.TXT;
        DocumentStore.DocumentFormat formatEleven = DocumentStore.DocumentFormat.TXT;
        String uriStringOne = ("ftp://ftp.is.co.za/rfc/rfc1808.txt");
        String uriStringTwo = ("https://example.com/path/resource.txt#fragment");
        String uriStringThree = ("urn:ietf:rfc:2648");
        String uriStringFour = ("path/resource.txt");
        String uriStringFive = ("/path/resource.txt");
        String uriStringSix = ("resource.txt");
        String uriStringSeven = ("./resource.txt");
        String uriStringEight = ("resource.txt");
        String uriStringNine = ("#fragment");
        String uriStringTen = ("ftp://ftp.is.co.za/rfc/rfc1808.txt");
        String uriStringEleven = ("../resource.txt");
        //URIs one and six should not be present in hashtable
        URI uriOne = null;
        URI uriTwo = null;
        URI uriThree = null;
        URI uriFour = null;
        URI uriFive = null;
        URI uriSix = null;
        URI uriSeven = null;
        URI uriEight = null;
        URI uriNine = null;
        URI uriTen = null;
        URI uriEleven = null;
        try {
            uriOne = new URI(uriStringOne);
            uriTwo = new URI(uriStringTwo);
            uriThree = new URI(uriStringThree);
            uriFour = new URI(uriStringFour);
            uriFive = new URI(uriStringFive);
            uriSix = new URI(uriStringSix);
            uriSeven = new URI(uriStringSeven);
            uriEight = new URI(uriStringEight);
            uriNine = new URI(uriStringNine);
            uriTen = new URI(uriStringTen);
            uriEleven = new URI(uriStringEleven);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        InputStream streamOne = new ByteArrayInputStream(textOneAsPdf);
        InputStream streamTwo = new ByteArrayInputStream(textTwoAsPdf);
        InputStream streamThree = new ByteArrayInputStream(textThreeAsPdf);
        InputStream streamFour = new ByteArrayInputStream(textFourAsPdf);
        InputStream streamFive = new ByteArrayInputStream(textFiveAsPdf);
        InputStream streamSix = new ByteArrayInputStream(textSixAsPdf);
        InputStream streamSeven = new ByteArrayInputStream(textSevenAsString.getBytes());
        InputStream streamEight = new ByteArrayInputStream(textEightAsString.getBytes());
        InputStream streamNine = new ByteArrayInputStream(textNineAsString.getBytes());
        InputStream streamTen = new ByteArrayInputStream(textTenAsString.getBytes());
        InputStream streamEleven = new ByteArrayInputStream(textElevenAsString.getBytes());
        int textOneHash = docStore.putDocument(streamOne, uriOne, formatOne);
        int textTwoHash = docStore.putDocument(streamTwo, uriTwo, formatTwo);
        int textThreeHash = docStore.putDocument(streamThree, uriThree, formatThree);
        int textFourHash = docStore.putDocument(streamFour, uriFour, formatFour);
        int textFiveHash = docStore.putDocument(streamFive, uriFive, formatFive);
        int textSixHash = docStore.putDocument(streamSix, uriSix, formatSix);
        int textSevenHash = docStore.putDocument(streamSeven, uriSeven, formatSeven);
        int textEightHash = docStore.putDocument(streamEight, uriEight, formatEight);
        int textNineHash = docStore.putDocument(streamNine, uriNine, formatNine);
        int textTenHash = docStore.putDocument(streamTen, uriTen, formatTen);
        int textElevenHash = docStore.putDocument(streamEleven, uriEleven, formatEleven);
        return docStore;
    }


    private String pdfToString(byte[] bytes) {
        try {
            PDFTextStripper stripper = new PDFTextStripper();
            PDDocument pdDoc = PDDocument.load(bytes);
            String text = stripper.getText(pdDoc);
            pdDoc.close();
            return text.trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] pdfCreator(String text) {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDFont font = PDType1Font.TIMES_ROMAN;
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

    private String longTextBuilder(){
        String longText = "This is a fourth text!  Im gonna make it kinda long, just in case!  (and to make sure that the byte array doesn't have issues with longer texts. fbkjjsdb b jib ni bbin isd bnbi sfsbd" +
            " rtn gb  rwr  th nsfsnfh sftg s" +
            "  wths dhshhrhe hehh hhrtr shst ght t n tr hstsh h hst ht ht " +
            " ht ssthht sh hsth er n dherdndg  erdg " +
            " sths hhthf h dsxhhs thsr hsd h h " +
            " sdh sshgh ghgh hgd hgh dh dgg dhgfhdgfgh" +
            "h sdfhggfhgh h hgd fh shg shh fdh gdhfg hs  h hsdhs gh shhhs " +
            " shh hs hhsdf hdfhdf hdhsd hsd hdshfhfdsdhsfhsfdsdh  hhdfhdfdsfh" +
            " sdhsdhdfhfsdshhshsjt  t st t  hhsttyst jsfghjng hstrjjsyfgsjjsf" +
            "s dts thj sjsfjjsnggjgjshjgjs jtjt sr jjt sjtstsj stj jsfj gh j f gsgj ghsh gh hsgghhgghh sfgh hgsh gh gshghs" +
            " hst shh ghgshghs shkh  h h  ks jjgnjgn   s hggjdfgjgjgjdfgg ggfs g ggsd g sg  ggs dgf gf  ggjf  fdijggjgjfjfgj gjg j jgf gjgjgj f gfjgfjfgjfj jg jf jsdf j fsdgjfjfhjstjyihjj  g hghsdhghgshs" +
            " h hd 54695980579869 0e95 9 5673$7@&@%&#$^#^^*&&  jhhio iojdji jionjion " +
            " sdhii  hhdsij ii shijisd hn" +
            " shinsd  ihi shsihishifinh " +
            " shifdsudiuigfbjsdfklisdug  u  ihfibfsdhugbn ug  bb s uun si ui uiiu ui igs duif iuinjbn sui ui uinu budfii" +
            " shid fni  suuif  nuigf isduidfgunifsuifgu  duifuisuisdf ui gui.  That should be enoguh :)";
        return longText;
    }
}
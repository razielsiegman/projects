package edu.yu.cs.com1320.project.stage4;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

public interface DocumentStore
{
    /**
     * the two document formats supported by this document store.
     * Note that TXT means plain text, i.e. a String.
     */
    static enum DocumentFormat{
        TXT,PDF
    };
    /**
     * @param input the document being put
     * @param uri unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return the hashcode of the String version of the document
     */
    int putDocument(InputStream input, URI uri, DocumentFormat format);

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as a PDF, or null if no document exists with that URI
     */
    byte[] getDocumentAsPdf(URI uri);

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as TXT, i.e. a String, or null if no document exists with that URI
     */
    String getDocumentAsTxt(URI uri);

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    boolean deleteDocument(URI uri);

    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    void undo() throws IllegalStateException;

    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    void undo(URI uri) throws IllegalStateException;
    /**
     * Retrieve all documents whose text contains the given keyword.
     * Documents are returned in sorted, descending order, sorted by the number of times the keyword appears in the document.
     * Search is CASE INSENSITIVE.
     * @param keyword
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    List<String> search(String keyword);

    /**
     * same logic as search, but returns the docs as PDFs instead of as Strings
     */
    List<byte[]> searchPDFs(String keyword);

    /**
     * Retrieve all documents whose text starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE INSENSITIVE.
     * @param prefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    List<String> searchByPrefix(String prefix);

    /**
     * same logic as searchByPrefix, but returns the docs as PDFs instead of as Strings
     */
    List<byte[]> searchPDFsByPrefix(String prefix);

    /**
     * delete ALL exact matches for the given key
     * @param key
     * @return a Set of URIs of the documents that were deleted.
     */
    Set<URI> deleteAll(String key);

    /**
     * Delete all matches that contain a String with the given prefix.
     * Search is CASE INSENSITIVE.
     * @param prefix
     * @return a Set of URIs of the documents that were deleted.
     */
    Set<URI> deleteAllWithPrefix(String prefix);
    /**
     * return the last time this document was used, via put/get or via a search result
     */
    long getLastUseTime();
    void setLastUseTime(long timeInMilliseconds);
    /**
     * set maximum number of documents that may be stored
     * @param limit
     */
    void setMaxDocumentCount(int limit);

    /**
     * set maximum number of bytes of memory that may be used by all the compressed documents in memory combined
     * @param limit
     */
    void setMaxDocumentBytes(int limit);
}
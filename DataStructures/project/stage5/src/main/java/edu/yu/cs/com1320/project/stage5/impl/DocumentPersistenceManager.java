package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {

    private File baseDir;

    //baseDir is either the file specified by user, or user.dir, if no specification
    public DocumentPersistenceManager(File baseDir) throws IOException {
        this.baseDir = baseDir;
        File file = new File(String.valueOf(baseDir));
        file.mkdirs();
    }

    @Override
    public void serialize(URI uri, Document val) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(DocumentImpl.class, new DocumentImplSerializer()).enableComplexMapKeySerialization().setPrettyPrinting().create();
        String docAsJson = gson.toJson(val);
        String uriString = uri.getRawAuthority() + uri.getRawPath();
        try {
            File file = new File(baseDir + File.separator + uriString);
            file.mkdirs();
            FileWriter writer = new FileWriter(file + ".json");
            writer.write(docAsJson);
            writer.close();
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Document deserialize(URI uri) throws IOException {
        String uriString = uri.getRawAuthority() + uri.getRawPath();
        String filePath = baseDir + File.separator + uriString + ".json";
        StringBuilder contentBuilder = new StringBuilder();
        Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8);
        stream.forEach(s -> contentBuilder.append(s).append("\n"));
        String jsonString = contentBuilder.toString();
        Gson gson = new GsonBuilder().registerTypeAdapter(DocumentImpl.class, new DocumentImplDeserialiser()).create();
        DocumentImpl doc = gson.fromJson(jsonString, DocumentImpl.class);
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        boolean deleted = file.delete();
        while(deleted == true){
            file = parentFile;
            parentFile = file.getParentFile();
            deleted = file.delete();
        }
        return doc;
    }

    private class DocumentImplSerializer implements JsonSerializer<DocumentImpl> {
        @Override
        public JsonElement serialize(DocumentImpl doc, Type type, JsonSerializationContext context) {
            Map<String, Integer> map = doc.getWordMap();
            JsonObject json = new JsonObject();
            json.addProperty("uri", doc.getKey().toString());
            json.addProperty("text", doc.getDocumentAsTxt());
            String jsonString = new Gson().toJson(map);
            json.addProperty("hashMap", jsonString);
            json.addProperty("txtHash", doc.getDocumentTextHashCode());
            return json;
        }
    }

    public class DocumentImplDeserialiser implements JsonDeserializer<DocumentImpl> {
        @Override
        public DocumentImpl deserialize(JsonElement json, Type typeOf, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new Gson();
            String text = json.getAsJsonObject().get("text").getAsString();
            URI uri = null;
            try {
                uri = new URI(json.getAsJsonObject().get("uri").getAsString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            int txtHash = json.getAsJsonObject().get("txtHash").getAsInt();
            String mapString = json.getAsJsonObject().get("hashMap").getAsString();
            Type empMapType = new TypeToken<HashMap<String, Integer>>() {
            }.getType();
            HashMap<String, Integer> hashMap = gson.fromJson(mapString, empMapType);
            DocumentImpl doc = new DocumentImpl(uri, text, txtHash, hashMap);
            return doc;
        }
    }
}
package persistance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class JsonRepository<T> {
    
    private final Gson gson;
    private final String filePath;
    private final Class<T> type;

    public JsonRepository(String filePath, Class<T> type) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.filePath = filePath;
        this.type = type;
    }
    
    public void writeFile(T data){
        try {
            File file = new File(filePath);
            File carpeta = file.getParentFile();
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(data, writer);
            }
        }catch(Exception e){
                       System.out.println("No se pudo guardar " + filePath + ": " + e);

        }
    }
    
    public T readFile(){
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        
        try(FileReader reader = new FileReader(filePath)){
            return gson.fromJson(reader, type);
        }catch(Exception e){
          System.out.println("No se pudo leer " + filePath + ": " + e);
        }
        return null;
    }
    
    public boolean exists() {
        return new File(filePath).exists();
    }

    public void deleteFile() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
    
}


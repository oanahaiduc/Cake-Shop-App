package cakeApp.repository.file.text;

import cakeApp.domain.Identifiable;
import cakeApp.repository.file.FileRepository;

import java.io.*;

public abstract class TextFileRepository<ID, T extends Identifiable<ID>> extends FileRepository<ID, T> {
    public TextFileRepository(String filename) {
        super(filename);
        ensureFileExists();
    }

    private void ensureFileExists() {
        File file = new File(filename);
        try{
            if(!file.exists()){
                file.createNewFile();
            }
        }catch(IOException e){
            throw new RuntimeException("Error creating file: " + filename, e);
        }
    }

    @Override
    protected void readFromFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                T entity = parseEntity(line);
                super.add(entity);
            }
        }catch(IOException e){
            throw new RuntimeException("Error reading file: " + filename, e);
        }
    }

    @Override
    protected void writeToFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for(T entity : findAll()){
                writer.write(entity.toString()+"\n");
            }
        }catch(IOException e){
            throw new RuntimeException("Error writing to file: " + filename, e);
        }
    }

    protected abstract T parseEntity(String line);
}

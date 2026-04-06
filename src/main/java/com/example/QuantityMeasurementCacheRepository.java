package com.example;

import java.util.*;
import java.io.*;

class AppendableObjectOutputStream extends ObjectOutputStream {

    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException{
        File file=new File(QuantityMeasurementCacheRepository.FILE_NAME);
        if(!file.exists() || file.length()==0) super.writeStreamHeader();
        else reset();
    }
}
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    public static final String FILE_NAME = "quantity_measurement_repo.ser";

    // In-memory cache
    private List<QuantityMeasurementEntity> cache;

    // Singleton instance
    private static QuantityMeasurementCacheRepository instance;

    // Private constructor
    private QuantityMeasurementCacheRepository() {
        cache = new ArrayList<>();
        loadFromDisk();
    }

    // Singleton access
    public static QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    // Save entity
    @Override
    public void save(QuantityMeasurementEntity entity) {
        cache.add(entity);
        saveToDisk(entity);
    }

    // Get all data
    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return cache;
    }

    // ================= FILE STORAGE =================

    private void saveToDisk(QuantityMeasurementEntity entity) {
        try (
                FileOutputStream fos = new FileOutputStream(FILE_NAME, true);
                AppendableObjectOutputStream oos =
                        new AppendableObjectOutputStream(fos)
        ) {
            oos.writeObject(entity);
        } catch (IOException e) {
            System.err.println("Error saving entity: " + e.getMessage());
        }
    }

    private void loadFromDisk() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (
                FileInputStream fis = new FileInputStream(FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            while (true) {
                try {
                    QuantityMeasurementEntity entity =
                            (QuantityMeasurementEntity) ois.readObject();
                    cache.add(entity);
                } catch (EOFException e) {
                    break; // end of file
                }
            }

            System.out.println("Loaded " + cache.size() + " records");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}

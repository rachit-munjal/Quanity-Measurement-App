package com.example.repository;

import com.example.entity.QuantityMeasurementEntity;

import java.util.*;
import java.io.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class AppendableObjectOutputStream extends ObjectOutputStream {
    private final String fileName;

    public AppendableObjectOutputStream(OutputStream out, String fileName) throws IOException {
        super(out);
        this.fileName = fileName;
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) super.writeStreamHeader();
        else reset();
    }
}

public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = Logger.getLogger(QuantityMeasurementCacheRepository.class.getName());
    public static final String FILE_NAME = "quantity_measurement_repo.ser";

    private List<QuantityMeasurementEntity> cache;
    private static QuantityMeasurementCacheRepository instance;

    private QuantityMeasurementCacheRepository() {
        cache = new ArrayList<>();
        loadFromDisk();
    }

    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        cache.add(entity);
        saveToDisk(entity);
        logger.info("Saved measurement to cache and disk: " + entity);
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return new ArrayList<>(cache);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        return cache.stream()
                .filter(e -> e.operation.equalsIgnoreCase(operation))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        return cache.stream()
                .filter(e -> e.thisMeasurementType.equalsIgnoreCase(measurementType))
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalCount() {
        return cache.size();
    }

    @Override
    public void deleteAll() {
        cache.clear();
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        logger.info("Deleted all measurements from cache and disk");
    }

    private void saveToDisk(QuantityMeasurementEntity entity) {
        try (
                FileOutputStream fos = new FileOutputStream(FILE_NAME, true);
                AppendableObjectOutputStream oos = new AppendableObjectOutputStream(fos, FILE_NAME)
        ) {
            oos.writeObject(entity);
        } catch (IOException e) {
            logger.severe("Error saving entity to disk: " + e.getMessage());
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
                    QuantityMeasurementEntity entity = (QuantityMeasurementEntity) ois.readObject();
                    cache.add(entity);
                } catch (EOFException e) {
                    break;
                }
            }
            logger.info("Loaded " + cache.size() + " records from disk");
        } catch (IOException | ClassNotFoundException e) {
            logger.warning("Error loading data from disk: " + e.getMessage());
        }
    }
}

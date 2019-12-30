package pl.edu.agh.gastronomiastosowana.model.importer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import java.lang.reflect.ParameterizedType;

import static java.util.Arrays.asList;
import static java.util.Arrays.binarySearch;

public abstract class AbstractCsvImporter<T> {

    private List<String> headers;
    private List<String> settersNames;
    private List<String> fileLines;
    private List<T> createdEntities;
    private Path importedFile;
    private Class<T> createdInstancesClass;

    public AbstractCsvImporter(Path importedFile) {
        this.importedFile = importedFile;
        this.createdEntities = new LinkedList<>();
        this.createdInstancesClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public List<T> doImport() throws IOException {
        getFileLines();
        populateHeaders();
        headersToSettersNames();
        for (String line : this.fileLines) {
            createdEntities.add(createObjectFromLine(line));
        }
        return createdEntities;
    }

    private void getFileLines() throws IOException {
        this.fileLines = Files.lines(importedFile).collect(Collectors.toList());
    }

    private void populateHeaders() {
        String header = this.fileLines.get(0);
        String[] headerArray = header.split(";");
        this.headers = new LinkedList<>(asList(headerArray));
        this.fileLines.remove(0);
    }

    private void headersToSettersNames() {
        this.settersNames = this.headers.stream()
                .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                .map(s -> "set" + s)
                .collect(Collectors.toList());
    }

    private T createObjectFromLine(String line) {
        T entity = null;
        try { entity = createdInstancesClass.getDeclaredConstructor().newInstance(); }
        catch (ReflectiveOperationException e) { e.printStackTrace(); }

        String[] fieldsNames = line.split(";");
        for(int i = 0; i < fieldsNames.length; i++) {
            int finalI = i;
            Method setterMethod = Arrays.stream(createdInstancesClass.getDeclaredMethods())
                    .filter(aMethod -> aMethod.getName().equals(settersNames.get(finalI)))
                    .collect(Collectors.toList())
                    .get(0);
            String valueToSet = fieldsNames[i];
            Class<?> acceptedSetterType = setterMethod.getParameterTypes()[0];
            String acceptedSetterTypeClassName = acceptedSetterType.getName();
            String acceptedSetterTypeShortClassName = acceptedSetterTypeClassName.substring(acceptedSetterTypeClassName.lastIndexOf('.') + 1);
            try {
                switch(acceptedSetterTypeShortClassName) {
                    case "String":
                        setterMethod.invoke(entity, valueToSet);
                        break;
                    case "int": case "Integer":
                        setterMethod.invoke(entity, Integer.parseInt(valueToSet));
                        break;
                    case "double": case "Double":
                        setterMethod.invoke(entity, Double.parseDouble(valueToSet));
                        break;
                    case "LocalDate":
                        setterMethod.invoke(entity, LocalDate.parse(valueToSet));
                        break;
                }
            } catch (ReflectiveOperationException e) {
                entity = null;
                break;
            }
        }
        return entity;
    }

}

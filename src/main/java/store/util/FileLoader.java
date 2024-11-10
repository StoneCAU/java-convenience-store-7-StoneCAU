package store.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import store.domain.product.ProductFactory;
import store.exception.ErrorMessage;
import store.exception.StoreException;

public class FileLoader {
    public static List<String> loadMarkdownFile(String fileName) {
        try (InputStream inputStream = ProductFactory.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            return reader.lines().toList();

        } catch (IOException e) {
            throw new StoreException(ErrorMessage.INVALID_FILE_CONTENT.getMessage());
        }
    }
}

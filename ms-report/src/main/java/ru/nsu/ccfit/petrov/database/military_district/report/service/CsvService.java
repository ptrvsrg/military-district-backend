package ru.nsu.ccfit.petrov.database.military_district.report.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class CsvService {

    public InputStream convertToCSV(List<Map<String, String>> data) throws IOException {
        if (CollectionUtils.isEmpty(data)) {
            throw new IllegalArgumentException("Data list is empty");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream), CSVFormat.DEFAULT)) {
            // Write headers
            Map<String, String> firstRow = data.get(0);
            for (String key : firstRow.keySet()) {
                csvPrinter.print(key);
            }
            csvPrinter.println();

            // Write data rows
            for (Map<String, String> row : data) {
                for (String value : row.values()) {
                    csvPrinter.print(value);
                }
                csvPrinter.println();
            }
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}

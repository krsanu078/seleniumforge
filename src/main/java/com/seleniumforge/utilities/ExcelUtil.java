package com.seleniumforge.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ExcelUtil reads test data from an Excel file placed under resources (testdata.xlsx).
 * Single responsibility: interacting with Excel test data.
 */
public final class ExcelUtil {

    private static final Logger LOGGER = LogManager.getLogger(ExcelUtil.class);
    private static final String DEFAULT_FILE = "testdata.xlsx";

    private ExcelUtil() {
        // utility
    }

    /**
     * Read a sheet and return rows as list of maps (column header -> cell value).
     *
     * @param sheetName name of the sheet in testdata.xlsx
     * @return list of rows as map
     */
    public static List<Map<String, String>> readSheetAsMap(String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_FILE)) {
            if (is == null) {
                LOGGER.warn("Excel file '{}' not found on classpath", DEFAULT_FILE);
                return data;
            }
            try (Workbook wb = WorkbookFactory.create(is)) {
                Sheet sheet = wb.getSheet(sheetName);
                if (sheet == null) {
                    LOGGER.warn("Sheet '{}' not found in {}", sheetName, DEFAULT_FILE);
                    return data;
                }
                Iterator<Row> rows = sheet.rowIterator();
                if (!rows.hasNext()) {
                    return data;
                }
                Row headerRow = rows.next();
                List<String> headers = new ArrayList<>();
                for (Cell c : headerRow) {
                    headers.add(c.getStringCellValue());
                }
                while (rows.hasNext()) {
                    Row row = rows.next();
                    Map<String, String> rowMap = new HashMap<>();
                    for (int i = 0; i < headers.size(); i++) {
                        Cell cell = row.getCell(i);
                        String value = cell == null ? "" : getCellAsString(cell);
                        rowMap.put(headers.get(i), value);
                    }
                    data.add(rowMap);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to read excel sheet '{}': {}", sheetName, e.getMessage(), e);
        }
        return data;
    }

    private static String getCellAsString(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK -> "";
            default -> cell.toString();
        };
    }
}

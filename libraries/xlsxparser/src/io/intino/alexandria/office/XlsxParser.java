package io.intino.alexandria.office;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.valueOf;

public class XlsxParser {
	private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

	public static Map<String, String> convert(File file) throws IOException {
		return convert(WorkbookFactory.create(file));
	}

	public static Map<String, String> convert(File file, String password) throws IOException {
		return convert(WorkbookFactory.create(file, password));
	}

	private static Map<String, String> convert(Workbook wb) {
		Map<String, String> result = new LinkedHashMap<>();
		for (Sheet sheet : wb)
			result.put(sheet.getSheetName(), convert(sheet));
		close(wb);
		return result;
	}

	private static void close(Workbook wb) {
		try {
			wb.close();
		} catch (IOException ignored) {
		}
	}

	private static String convert(Sheet sheet) {
		StringBuilder data = new StringBuilder();
		for (Row row : sheet) data.append(convert(row));
		return data.toString();
	}

	private static String convert(Row row) {
		StringBuilder data = new StringBuilder();
		for (Cell cell : row) data.append('\t').append(convert(cell));
		data.append('\n');
		return data.substring(1);
	}

	private static String convert(Cell cell) {
		switch (cell.getCellType()) {
			case STRING: return cell.getStringCellValue();
			case BOOLEAN: return valueOf(cell.getBooleanCellValue());
			case NUMERIC: return numericToString(cell);
		}
		return "";
	}

	private static String numericToString(Cell cell) {
		return !DateUtil.isCellDateFormatted(cell) ? valueOf(cell.getNumericCellValue()) : format.format(cell.getDateCellValue());
	}


}

package io.intino.alexandria.office;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

public class XlsxParser {

	public Map<String, String> convert(File file) throws IOException {
		return convert(WorkbookFactory.create(file));
	}

	public Map<String, String> convert(File file, String password) throws IOException {
		return convert(WorkbookFactory.create(file, password));
	}

	private HashMap<String, String> convert(Workbook wb) {
		HashMap<String, String> result = new HashMap<>();
		for (Sheet sheet : wb)
			result.put(sheet.getSheetName(), convert(sheet));
		return result;
	}

	private String convert(Sheet sheet) {
		StringBuilder data = new StringBuilder();
		for (Row row : sheet) data.append(convert(row));
		return data.toString();
	}

	private String convert(Row row) {
		StringBuilder data = new StringBuilder();
		for (Cell cell : row) data.append('\t').append(convert(cell));
		data.append('\n');
		return data.substring(1);
	}

	private String convert(Cell cell) {
		switch (cell.getCellType()) {
			case STRING: return cell.getStringCellValue();
			case BOOLEAN: return valueOf(cell.getBooleanCellValue());
			case NUMERIC: return valueOf(cell.getNumericCellValue());
		}
		return "";
	}


}

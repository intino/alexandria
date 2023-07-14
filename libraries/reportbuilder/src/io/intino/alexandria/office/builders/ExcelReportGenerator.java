package io.intino.alexandria.office.builders;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.office.model.CellValue;
import io.intino.alexandria.office.model.Column;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.awt.Color;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelReportGenerator extends ReportGenerator {
	protected final File template;
	protected final int startColumnsRow;

	public ExcelReportGenerator(File template) {
		this(template, 6);
	}

	public ExcelReportGenerator(File template, int startColumnsRow) {
		this.template = template;
		this.startColumnsRow = startColumnsRow;
	}

	@Override
	InputStream generate(String title) {
		File file = null;
		try {
			file = tempFile("xlsx");
			if (file == null) return null;
			Workbook workbook = new XSSFWorkbook(OPCPackage.open(template));
			addSheets(workbook, title);
			FileOutputStream fileOut = new FileOutputStream(file);
			workbook.write(fileOut);
			fileOut.close();
			return new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
		} catch (IOException | InvalidFormatException e) {
			Logger.error(e);
			return new ByteArrayInputStream(new byte[0]);
		}
		finally {
			if (file != null) file.delete();
		}
	}

	private void addSheets(Workbook workbook, String title) {
		List<Column> dimensions = dimensions();
		Map<String, List<String>> groupedLines = linesGrouped(dimensions.size() > 0 ? firstDimensionIndex() : -1);
		List<String> sheetNames = new ArrayList<>(groupedLines.keySet());
		for (int i = 0; i < sheetNames.size(); i++) {
			String sheetName = sheetNames.get(i);
			addSheet(workbook, i, sheetName, title, groupedLines.get(sheetName));
		}
	}

	private void addSheet(Workbook book, int index, String sheetName, String title, List<String> values) {
		Sheet sheet = sheet(book, index);
		book.setSheetName(index, sheetName);
		cell(row(sheet, 4), 0).setCellValue(title);
		fillColumns(book, sheet);
		fillRows(book, sheet, values);
	}

	private void fillColumns(Workbook book, Sheet sheet) {
		List<Column> columns = skipColumnDimension(this.columns);
		for (int i = 0; i < columns.size(); i++) {
			Column c = columns.get(i);
			fillColumn(book, sheet, columnCell(row(sheet, startColumnsRow-1), i+1), c);
		}
	}

	private void fillColumn(Workbook book, Sheet sheet, Cell cell, Column column) {
		cell.setCellValue(column.label());
		fillColumnStyle(book, cell, cell.getCellStyle(), column);
	}

	private void fillColumnStyle(Workbook book, Cell cell, CellStyle style, Column column) {
		style.setFillForegroundColor(colorOf(book, column.color()));
		if (column.border() != null) {
			style.setBottomBorderColor(colorOf(book, column.border()));
			style.setBorderBottom(BorderStyle.MEDIUM);
		}
		style.setAlignment(alignmentOf(column));
		cell.setCellStyle(style);
	}

	private void fillRows(Workbook book, Sheet sheet, List<String> values) {
		for (int i = 0; i < values.size(); i++)
			fillRow(book, row(sheet, startColumnsRow + i), skipRowDimension(cellsValuesOf(values.get(i))), i);
	}

	private void fillRow(Workbook book, Row row, List<CellValue> cells, int index) {
		List<Column> columns = skipColumnDimension(this.columns);
		cell(row, 0).setCellValue(index+1);
		for (int i=0; i<cells.size(); i++) fillCell(book, cell(row, i+1), columns.get(i), cells.get(i));
	}

	private void fillCell(Workbook book, Cell cell, Column column, CellValue value) {
		if (column.isNumber()) fillCellNumber(cell, value);
		else cell.setCellValue(column.valueOf(value.data()));
		fillCellStyle(book, cell, column, value);
	}

	private static void fillCellNumber(Cell cell, CellValue value) {
		try {
			cell.setCellValue(Double.parseDouble((String) value.data()));
		}
		catch (Throwable ex) {
			cell.setCellValue((String)value.data());
		}
	}

	private void fillCellStyle(Workbook book, Cell cell, Column column, CellValue value) {
		CellStyle style = cell.getCellStyle() != null ? cell.getCellStyle() : book.createCellStyle();
		if (value.color() != null) style.setFillForegroundColor(colorOf(book, value.color()));
		if (value.style() == io.intino.alexandria.office.model.CellValue.Style.Bold) {
			Font font = book.createFont();
			font.setBold(true);
			style.setFont(font);
		}
		style.setAlignment(alignmentOf(column));
		cell.setCellStyle(style);
	}

	private HorizontalAlignment alignmentOf(Column column) {
		if (column.alignment() == Column.Alignment.Left) return HorizontalAlignment.LEFT;
		else if (column.alignment() == Column.Alignment.Center) return HorizontalAlignment.CENTER;
		return HorizontalAlignment.RIGHT;
	}

	private short colorOf(Workbook book, String colorValue) {
		IndexedColorMap indexedColors = ((XSSFWorkbook) book).getStylesSource().getIndexedColors();
		Color color = Color.decode(colorValue);
		byte[] rgb = new byte[3];
		rgb[0] = (byte) color.getRed();
		rgb[1] = (byte) color.getGreen();
		rgb[2] = (byte) color.getBlue();
		return new XSSFColor(rgb, indexedColors).getIndexed();
	}

	Sheet sheet(Workbook book, int index) {
		return book.getNumberOfSheets() > index ? book.getSheetAt(index) : book.cloneSheet(0);
	}

	Row row(Sheet sheet, int index) {
		Row row = sheet.getRow(index);
		return row != null ? row : sheet.createRow(index);
	}

	Cell columnCell(Row row, int index) {
		Cell cell = row.getCell(index);
		if (cell == null) {
			cell = row.createCell(index);
			cell.setCellStyle(row.getCell(0).getCellStyle());
		}
		return cell;
	}

	Cell cell(Row row, int index) {
		Cell cell = row.getCell(index);
		return cell != null ? cell : row.createCell(index);
	}

}

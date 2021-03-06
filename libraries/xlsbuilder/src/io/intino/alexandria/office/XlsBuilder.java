package io.intino.alexandria.office;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class XlsBuilder {
	private final HSSFWorkbook wb;
	private final HSSFCreationHelper helper;
	private final Map<Sheet, HSSFPatriarch> patriarchs = new HashMap<>();
	private final HSSFCellStyle level1;
	private final HSSFCellStyle level2;
	private final HSSFCellStyle boldLeft;
	private final HSSFCellStyle boldRight;
	private final HSSFCellStyle text;
	private final Set<Short> styles = new HashSet<>();

	private XlsBuilder() {
		this.wb = new HSSFWorkbook();
		this.helper = wb.getCreationHelper();

		this.level1 = style(font(14, true));
		this.level2 = style(font(12, true));
		this.boldLeft = style(font(10, true), HorizontalAlignment.LEFT);
		this.boldRight = style(font(10, true), HorizontalAlignment.RIGHT);
		this.text = style(font(10, false), HorizontalAlignment.LEFT);
	}

	public static XlsBuilder create() {
		return new XlsBuilder();
	}

	public XlsBuilder append(String sheetName, File csvFile) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
			create(newSheet(sheetName), reader);
		}
		return this;
	}

	public XlsBuilder append(String sheetName, String csvContent) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())))) {
			create(newSheet(sheetName), reader);
		}
		return this;
	}

	private HSSFSheet newSheet(String sheetName) {
		HSSFSheet sheet = wb.createSheet(sheetName);
		patriarchs.put(sheet, sheet.createDrawingPatriarch());
		return sheet;
	}

	public void save(File file) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(file);
		wb.write(fileOut);
		fileOut.close();
	}

	private HSSFCellStyle style(Font font) {
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);
		styles.add(style.getIndex());
		return style;
	}


	private HSSFFont font(int size, boolean bold) {
		HSSFFont font = wb.createFont();
		font.setBold(bold);
		font.setFontHeightInPoints((short) size);
		return font;
	}

	private HSSFCellStyle style(Font font, HorizontalAlignment alignment) {
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(alignment);
		styles.add(style.getIndex());
		return style;
	}

	private HSSFCellStyle style(short format, HorizontalAlignment alignment) {
		HSSFCellStyle style = wb.createCellStyle();
		style.setDataFormat(format);
		style.setAlignment(alignment);
		style.setFont(font(10, false));
		return style;
	}

	private void create(Sheet sheet, BufferedReader reader) throws IOException {
		for (int i = 0; ; i++) {
			String line = reader.readLine();
			if (line == null) break;
			if (line.startsWith("#define")) {
				define(sheet, line);
			} else {
				create(sheet.createRow(i), line.split(";"));
			}
		}
	}

	private void define(Sheet sheet, String line) {
		String[] split = line.split(" ", 3);
		defineFor(split[1], split[2]).execute(sheet);
	}

	private void create(Row row, String[] data) {
		for (int i = 0; i < data.length; i++)
			fill(row.createCell(i), data[i]);
	}

	private void fill(Cell cell, String data) {
		CellStyle style = styleFor(data);
		if (style != null) cell.setCellStyle(style);
		if (hasComment(data)) createComment(cell, commentOf(data));
		fill(cell, contentOf(withoutComment(data)));
	}

	private boolean hasComment(String data) {
		return data.contains("~");
	}

	private String withoutComment(String data) {
		return data.split("~")[0];
	}

	private String commentOf(String data) {
		return data.split("~")[1];
	}

	private void fill(Cell cell, Object value) {
		if (value instanceof Double) cell.setCellValue((Double) value);
		if (value instanceof String) cell.setCellValue((String) value);
		if (value instanceof HSSFRichTextString) cell.setCellValue((HSSFRichTextString) value);
	}

	private void createComment(Cell cell, String value) {
		HSSFPatriarch patriarch = patriarchs.get(cell.getSheet());
		Comment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		comment.setString(helper.createRichTextString(value));
		cell.setCellComment(comment);
	}

	private CellStyle styleFor(String value) {
		if (value.startsWith("*")) return level1;
		if (value.startsWith("**")) return level2;
		if (value.startsWith("$")) return boldLeft;
		if (value.startsWith("#")) return boldRight;
		if (value.startsWith(" ")) return text;
		return null;
	}

	private Object contentOf(String value) {
		value = value.replaceAll("^[#*$ ]*", "");
		try {
			return parseDouble(value);
		} catch (NumberFormatException e) {
			return value;
		}

	}

	private boolean isStyled(String value) {
		return value.matches("[#*$ ].*");
	}

	private Define defineFor(String variable, String value) {
		if (variable.equalsIgnoreCase("text")) return new DefineFactory().text(value);
		if (variable.equalsIgnoreCase("image")) return new DefineFactory().image(value);
		if (variable.equalsIgnoreCase("column")) return new DefineFactory().column(value);
		if (variable.equalsIgnoreCase("columns")) return new DefineFactory().columns(value);
		if (variable.equalsIgnoreCase("cell")) return new DefineFactory().cell(value);
		if (variable.equalsIgnoreCase("background")) return new DefineFactory().background(value);
		if (variable.equalsIgnoreCase("freezing")) return new DefineFactory().freezing(value);
		if (variable.equalsIgnoreCase("border")) return new DefineFactory().border(value);
		return new DefineFactory().Null();
	}

	private HorizontalAlignment parseAlignment(String data) {
		if (data.equalsIgnoreCase("right")) return HorizontalAlignment.RIGHT;
		if (data.equalsIgnoreCase("center")) return HorizontalAlignment.CENTER;
		return HorizontalAlignment.LEFT;
	}

	private short parseFormat(String data) {
		return wb.createDataFormat().getFormat(data);
	}

	public interface Define {
		void execute(Sheet sheet);
	}

	private class DefineFactory {

		public Define image(String definition) {
			return new Define() {

				private final String[] split = definition.split(":");

				@Override
				public void execute(Sheet sheet) {
					HSSFPicture picture = patriarchs.get(sheet)
							.createPicture(createAnchor(), createPicture());
					if (scaleX() != 1 || scaleY() != 1) picture.resize(scaleX(), scaleY());
					else picture.resize();
				}

				private ClientAnchor createAnchor() {
					ClientAnchor anchor = helper.createClientAnchor();
					anchor.setCol1(col());
					anchor.setRow1(row());
					return anchor;
				}

				private int createPicture() {
					try {
						return wb.addPicture(IOUtils.toByteArray(stream()), Workbook.PICTURE_TYPE_PNG);
					} catch (IOException e) {
						return -1;
					}
				}

				private int row() {
					return parseInt(split[0]);
				}

				private double scaleX() {
					return parseDouble(split[2]);
				}

				private double scaleY() {
					return parseDouble(split[3]);
				}

				private int col() {
					return parseInt(split[1]);
				}

				private InputStream stream() {
					return new ByteArrayInputStream(bytesOf(split[4]));
				}

				private byte[] bytesOf(String data) {
					return Base64.getDecoder().decode(data);
				}
			};
		}

		public Define column(String definition) {
			return new Define() {
				private HSSFCellStyle styleBold;
				private HSSFCellStyle style;
				private final String[] split = definition.split(":");

				@Override
				public void execute(Sheet sheet) {
					sheet.setColumnWidth(col(), size());
					style = style(format(), alignment());
					styleBold = style(format(), alignment());
					styleBold.setFont(font(10, true));
					setColumnStyle(sheet, col(), style);
				}

				private void setColumnStyle(Sheet sheet, int column, CellStyle style) {
					for (Row row : sheet) {
						Cell cell = row.getCell(column);
						if (cell == null) continue;
						CellStyle cellStyle = cell.getCellStyle();
						HSSFFont font = wb.getFontAt(cellStyle.getFontIndex());
						if (styles.contains(cellStyle.getIndex()) && font.getFontHeight() != 10) continue;
						cell.setCellStyle(font.getBold() ? styleBold : style);
					}
				}

				private int col() {
					return parseInt(split[0]);
				}

				private int size() {
					return parseInt(split[1]) * 520;
				}

				private HorizontalAlignment alignment() {
					return parseAlignment(split[2]);
				}

				private short format() {
					return parseFormat(split[3]);
				}

			};
		}

		public Define columns(String definition) {
			return new Define() {
				private HSSFCellStyle styleBold;
				private HSSFCellStyle style;
				private final String[] split = definition.split(":");

				@Override
				public void execute(Sheet sheet) {
					style = style(format(), alignment());
					styleBold = style(format(), alignment());
					styleBold.setFont(font(10, true));
					for (int i = firstCol(); i < lastCol(); i++) {
						sheet.setColumnWidth(i, size());
						setColumnStyle(sheet, i, style);
					}
				}

				private void setColumnStyle(Sheet sheet, int column, CellStyle style) {
					for (Row row : sheet) {
						Cell cell = row.getCell(column);
						if (cell == null) continue;
						CellStyle cellStyle = cell.getCellStyle();
						HSSFFont font = wb.getFontAt(cellStyle.getFontIndex());
						if (styles.contains(cellStyle.getIndex()) && font.getFontHeight() != 10) continue;
						cell.setCellStyle(font.getBold() ? styleBold : style);
					}
				}

				private int firstCol() {
					return parseInt(split[0]);
				}

				private int lastCol() {
					return parseInt(split[1]);
				}

				private int size() {
					return parseInt(split[2]) * 520;
				}

				private HorizontalAlignment alignment() {
					return parseAlignment(split[3]);
				}

				private short format() {
					return parseFormat(split[4]);
				}

			};
		}

		public Define text(String definition) {
			return new Define() {
				private final String[] split = definition.split(":");

				@Override
				public void execute(Sheet sheet) {
					Cell cell = sheet.getRow(row()).createCell(col());
					fill(cell, text());
				}

				private int row() {
					return parseInt(split[0]);
				}

				private int col() {
					return parseInt(split[1]);
				}

				private String text() {
					return split[2];
				}

			};
		}

		public Define cell(String definition) {
			return new Define() {
				private final String[] split = definition.split(":");

				@Override
				public void execute(Sheet sheet) {
					cell(sheet).setCellStyle(style(format(), alignment()));
				}

				private Cell cell(Sheet sheet) {
					return sheet.getRow(row()).getCell(col());
				}

				private int row() {
					return parseInt(split[0]);
				}

				private int col() {
					return parseInt(split[1]);
				}

				private HorizontalAlignment alignment() {
					return parseAlignment(split[2]);
				}

				private short format() {
					return parseFormat(split[3]);
				}

			};

		}

		public Define Null() {
			return sheet -> {
			};
		}

		public Define background(String definition) {
			return new Define() {
				private final String[] split = definition.split(":");
				@Override
				public void execute(Sheet sheet) {
					CellStyle backgroundStyle = sheet.getWorkbook().createCellStyle();
					backgroundStyle.cloneStyleFrom(sheet.getRow(firstRow()).getCell(firstCol()).getCellStyle());
					backgroundStyle.setFillForegroundColor(IndexedColors.valueOf(colorName()).getIndex());
					backgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					for (int i = firstRow(); i <= lastRow() ; i++)
						for (int j = firstCol(); j <= lastCol(); j++)
							sheet.getRow(i).getCell(j).setCellStyle(backgroundStyle);
				}

				private int firstRow() {
					return parseInt(split[0]);
				}
				private int lastRow() {
					return parseInt(split[2]);
				}
				private int firstCol() {
					return parseInt(split[1]);
				}
				private int lastCol() {
					return parseInt(split[3]);
				}
				private String colorName() {
					return split[4];
				}
			};
		}
		public Define freezing(String definition) {
			return new Define() {
				private final String[] split = definition.split(":");
				@Override
				public void execute(Sheet sheet) {
					sheet.createFreezePane(splitCol(), splitRow());
				}

				private int splitRow() {
					return parseInt(split[0]);
				}
				private int splitCol() {
					return parseInt(split[1]);
				}
			};
		}
		public Define border(String definition) {
			return new Define() {
				private final String[] split = definition.split(":");
				@Override
				public void execute(Sheet sheet) {
					CellRangeAddress region = new CellRangeAddress(firstRow(), lastRow(), firstCol(), lastCol());
					BorderStyle border = BorderStyle.valueOf(borderName());
					RegionUtil.setBorderBottom(border, region, sheet);
					RegionUtil.setBorderLeft(border, region, sheet);
					RegionUtil.setBorderTop(border, region, sheet);
					RegionUtil.setBorderRight(border, region, sheet);
				}

				private int firstRow() {
					return parseInt(split[0]);
				}
				private int firstCol() {
					return parseInt(split[1]);
				}
				private int lastRow() {
					return parseInt(split[2]);
				}
				private int lastCol() {
					return parseInt(split[3]);
				}
				private String borderName() {
					return split[4];
				}
			};
		}
	}


}

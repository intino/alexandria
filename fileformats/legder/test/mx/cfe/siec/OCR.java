package mx.cfe.siec;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class OCR {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");


	public static long id(String ocr) {
		return getRPU(ocr) << 24 | getMonths(ocr) << 8 | getTipo(ocr);
	}

	public static String get(long id) {
		return rpu(id) + date(id).format(formatter) + tipo(id);
	}

	public static long rpu(long id) {
		return id >> 24;
	}

	public static LocalDate date(long id) {
		int months = (int) ((id >> 8) & 0xFFFF);
		return LocalDate.of(2000 + months/12,months%12,1);
	}

	public static String tipo(long id) {
		return String.format("%02d", (int) (id & 0xFF));
	}

	private static long getRPU(String ocr) {
		return parseLong(ocr.substring(0,12));
	}

	private static int getMonths(String ocr) {
		return parseInt(ocr.substring(12,14)) * 12 + parseInt(ocr.substring(14,16));
	}

	private static int getTipo(String ocr) {
		return parseInt(ocr.substring(16,18));
	}


}

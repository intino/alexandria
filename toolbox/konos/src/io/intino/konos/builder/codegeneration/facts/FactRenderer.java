package io.intino.konos.builder.codegeneration.facts;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.analytic.SchemaSerialBuilder;
import io.intino.konos.dsl.Axis;
import io.intino.konos.dsl.Cube;
import io.intino.konos.dsl.Cube.Fact.Column;
import io.intino.konos.dsl.SizedData;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.intino.itrules.formatters.StringFormatters.camelCase;
import static java.util.stream.Collectors.toList;

public class FactRenderer {

	private static final Comparator<Column> SORT_COLUMNS_BY_SIZE = (col1, col2) -> {
		if (col1.isVirtual() || col1.isId()) return Integer.MIN_VALUE;
		if (col2.isVirtual() || col2.isId()) return Integer.MAX_VALUE;
		return -Integer.compare(col1.asType().size(), col2.asType().size());
	};

	private final Map<String, Integer> axisSizes;

	public FactRenderer() {
		axisSizes = new HashMap<>();
	}

	public void render(Cube.Virtual virtualCube, FrameBuilder fb) {
		SchemaSerialBuilder serialBuilder = new SchemaSerialBuilder();
		fb.add("id", idOf(virtualCube.main().fact()).name$());
		Offset offset = addAllColumns(virtualCube, fb, serialBuilder);
		fb.add("size", calculateFactSize(offset));
		fb.add("serialUUID", serialBuilder.buildSerialId().toString());
	}

	public void render(Cube cube, FrameBuilder fb) {
		render(cube.name$(), cube, fb);
	}

	public void render(String name, Cube cube, FrameBuilder fb) {
		SchemaSerialBuilder serialBuilder = new SchemaSerialBuilder();
		fb.add("id", idOf(cube.fact()).name$());
		calculateColumnSizes(cube.fact().columnList());
		Offset offset = addAllColumns(name, cube, fb, serialBuilder);
		fb.add("size", calculateFactSize(offset));
		fb.add("serialUUID", serialBuilder.buildSerialId().toString());
	}

	private Offset addAllColumns(String name, Cube cube, FrameBuilder fb, SchemaSerialBuilder serialBuilder) {
		List<Column> columns = cube.fact().columnList().stream().sorted(SORT_COLUMNS_BY_SIZE).collect(toList());
		return addAllColumns(name, columns, fb, serialBuilder);
	}

	private Offset addAllColumns(Cube.Virtual virtualCube, FrameBuilder fb, SchemaSerialBuilder serialBuilder) {
		List<Column> mainColumns = virtualCube.main().fact().columnList().stream().sorted(SORT_COLUMNS_BY_SIZE).collect(toList());
		Set<String> mainColumnNames = mainColumns.stream().map(Column::name$).collect(Collectors.toSet());
		addAllColumns(virtualCube.main().name$(), mainColumns, fb, serialBuilder);
		List<Column> joinColumns = virtualCube.join().fact().columnList(c -> !mainColumnNames.contains(c.name$())).stream().sorted(SORT_COLUMNS_BY_SIZE).collect(toList());
		addAllColumns(virtualCube.join().name$(), joinColumns, fb, serialBuilder);
		return new Offset();
	}

	private Offset addAllColumns(String cube, List<Column> columns, FrameBuilder fb, SchemaSerialBuilder serialBuilder) {
		Offset offset = new Offset();
		for (Column column : columns) {
			if (column.isVirtual())
				addVirtualAttribute(cube, fb, column);
			else
				addFactAttribute(cube, fb, serialBuilder, offset, column);
		}
		return offset;
	}

	private void addVirtualAttribute(String cube, FrameBuilder fb, Column column) {
		final SizedData.Type type = column.asType();
		final String javaType = type(type);

		FrameBuilder columnFB = new FrameBuilder("virtualColumn")
				.add("cube", cube)
				.add("type", javaType)
				.add("name", column.name$())
				.add("defaultValue", defaultValueOf(javaType));

		if (isPrimitive(type)) columnFB.add("primitive");

		fb.add("virtualColumn", columnFB);
	}

	private String defaultValueOf(String javaType) {
		return switch (javaType) {
			case "byte", "short", "int", "long" -> "0";
			case "float" -> "0.0f";
			case "double" -> "0.0";
			default -> "null";
		};
	}

	private void addFactAttribute(String cube, FrameBuilder fb, SchemaSerialBuilder serialBuilder, Offset offset, Column column) {
		final String type = type(column.asType());
		final int columnSize = column.asType().size();
		int position = offset.position;
		final int minimumBytesNeeded = getMinimumBytesFor(position, columnSize);

		if (minimumBytesNeeded > Long.BYTES) position = roundUp2(position, Long.SIZE);

		fb.add("column", columnFrame(column, position, cube));
		serialBuilder.add(column.name$(), type, position, column.asType().size());

		offset.position = position + columnSize;
		offset.maxPosition = Math.max(offset.maxPosition, position + minimumBytesNeeded * Byte.SIZE);
	}

	private int calculateFactSize(Offset offset) {
		final int minSize = (int) Math.ceil(offset.maxPosition / (float) Byte.SIZE);
		final int size = (int) Math.ceil(offset.position / (float) Byte.SIZE);
		return Math.max(minSize, size);
	}

	private FrameBuilder columnFrame(Column column, int offset, String cube) {
		if (column.isCategory()) return processCategoryColumn(column.asCategory(), column.name$(), offset, cube);
		else return processColumn(column, offset, cube);
	}

	private FrameBuilder processColumn(Column column, int offset, String cube) {
		SizedData.Type type = column.asType();
		final String javaType = type(type);
		final String signType = column.isUnsignedInt() || column.isUnsignedLong() ? "unsigned" : "signed";

		FrameBuilder builder = new FrameBuilder("column",
				javaType,
				signType,
				column.core$().is(Cube.Fact.Attribute.class) ? "attribute" : "measure")
				.add("owner", cube)
				.add("name", column.a$(Column.class).name$())
				.add("offset", offset)
				.add("cube", cube)
				.add("type", javaType);

		if (isAligned(javaType, offset, type.size()))
			builder.add("aligned", "Aligned");
		else
			builder.add("bits", type.size());

		builder.add("size", type.size());

		return builder;
	}

	private FrameBuilder processCategoryColumn(SizedData.Category column, String name, int offset, String cube) {
		Axis.Categorical axis = column.axis().asCategorical();
		return new FrameBuilder("column", "categorical")
				.add("owner", cube)
				.add("name", name)
				.add("type", camelCase().format(axis.name$()).toString())
				.add("offset", offset)
				.add("cube", cube)
				.add("bits", column.asSizedData().asType().size());
	}

	private String type(SizedData.Type type) {
		if (type.i$(SizedData.Category.class)) return type.name$();
		return isPrimitive(type) ? asJavaType(type) : type.type();
	}

	private String asJavaType(SizedData.Type type) {
		final String primitive = unboxed(type.primitive());
		if (primitive.equals("int")) {
			if (type.size() <= Byte.SIZE) return "byte";
			if (type.size() <= Short.SIZE) return "short";
		} else if (primitive.equals("double") && type.size() <= Float.SIZE) {
			return "float";
		}
		return primitive;
	}

	private String unboxed(String primitive) {
		return switch (primitive) {
			case "Byte" -> "byte";
			case "Short" -> "short";
			case "Integer" -> "int";
			case "Long", "LongInteger" -> "long";
			case "Float" -> "float";
			case "Double" -> "double";
			default -> primitive;
		};
	}

	private int sizeOf(Axis.Categorical axis) {
		try {
			if (!axisSizes.containsKey(axis.name$()))
				axisSizes.put(axis.name$(), (int) Math.ceil(log2(countLines(axis) + 1)));
			return axisSizes.get(axis.name$());
		} catch (IOException e) {
			return 0;
		}
	}

	private boolean isAligned(String javaType, int offset, int size) {
		if (offset % size != 0) return false;
		return javaType.equals("int") && size == Integer.SIZE
				|| javaType.equals("short") && size == Short.SIZE
				|| javaType.equals("char") && size == Character.SIZE
				|| javaType.equals("byte") && size == Byte.SIZE
				|| javaType.equals("boolean") && size == Byte.SIZE
				|| javaType.equals("long") && size == Long.SIZE
				|| javaType.equals("float") && size == Float.SIZE
				|| javaType.equals("double") && size == Double.SIZE;
	}

	private boolean isPrimitive(SizedData.Type attribute) {
		SizedData data = attribute.asSizedData();
		return data.isBool() || data.isInteger() || data.isLongInteger() || data.isId() || data.isReal();
	}

	private void calculateColumnSizes(List<Column> columns) {
		for (Column column : columns) {
			if (column.isVirtual()) continue;
			if (column.isCategory()) {
				column.asType().size(sizeOf(column.asCategory().axis().asCategorical()));
			}
		}
	}

	private int getMinimumBytesFor(int bitIndex, int bitCount) {
		final int bitOffset = offsetOf(bitIndex);
		final int numBytes = (int) Math.ceil((bitOffset + bitCount) / 8.0);
		return roundSize(numBytes);
	}

	private int roundSize(int n) {
		if (n == 1 || n == 2 || n == 4 || n == 8) return n;
		if (n < 4) return 4;
		return Math.max(n, 8);
	}

	private int offsetOf(int bitIndex) {
		return bitIndex % Byte.SIZE;
	}

	private int roundUp2(int n, int multiple) {
		return (n + multiple - 1) & (-multiple);
	}

	private int countLines(Axis.Categorical axis) throws IOException {
		return (int) new BufferedReader(new InputStreamReader(resource(axis))).lines().count();
	}

	public FileInputStream resource(Axis.Categorical axis) throws FileNotFoundException {
		return new FileInputStream(axis.tsv().getFile());
	}

	public double log2(int N) {
		return (Math.log(N) / Math.log(2));
	}

	public Column idOf(Cube.Fact fact) {
		return fact.columnList().stream().filter(SizedData::isId).findFirst().orElse(null);
	}

	private static class Offset {

		public int position;
		public int maxPosition;

		public int bytesNeeded() {
			return maxPosition / Byte.SIZE;
		}
	}
}

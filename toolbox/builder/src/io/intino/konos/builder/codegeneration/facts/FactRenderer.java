package io.intino.konos.builder.codegeneration.facts;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.analytic.SchemaSerialBuilder;
import io.intino.konos.model.graph.Axis;
import io.intino.konos.model.graph.Cube;
import io.intino.konos.model.graph.Cube.Fact.Column;
import io.intino.konos.model.graph.SizedData;
import io.intino.magritte.framework.Concept;
import io.intino.magritte.framework.Predicate;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;
import static java.util.stream.Collectors.toList;

public class FactRenderer {

    private static final Comparator<Column> COMPARATOR = (col1, col2) -> {
        if(col1.isVirtual() || col1.isId()) return Integer.MIN_VALUE;
        if(col2.isVirtual() || col2.isId()) return Integer.MAX_VALUE;
        return -Integer.compare(col1.asType().size(), col2.asType().size());
    };

    private final Map<String, Integer> axisSizes;

    public FactRenderer() {
        axisSizes = new HashMap<>();
    }

    public void render(Cube cube, FrameBuilder fb) {
        SchemaSerialBuilder serialBuilder = new SchemaSerialBuilder();
        fb.add("id", idOf(cube.fact()).name$());
        calculateColumnSizes(cube.fact().columnList());
        final int lastOffset = addAllColumns(cube, fb, serialBuilder);
        fb.add("size", calculateFactSize(lastOffset));
        fb.add("serialUUID", serialBuilder.buildSerialId().toString());
    }

    private int addAllColumns(Cube cube, FrameBuilder fb, SchemaSerialBuilder serialBuilder) {
        int offset = 0;
        List<Column> columns = cube.fact().columnList().stream().sorted(COMPARATOR).collect(toList());
        for (Column column : columns) {
            if(column.isVirtual())
                addVirtualAttribute(cube, fb, column);
            else
                offset = addFactAttribute(cube, fb, serialBuilder, offset, column);
        }
        return offset;
    }

    private void addVirtualAttribute(Cube cube, FrameBuilder fb, Column column) {
        final SizedData.Type type = column.asType();
        final String javaType = type(type);

        FrameBuilder columnFB = new FrameBuilder("virtualColumn")
                .add("cube", cube.name$())
                .add("type", javaType)
                .add("name", column.name$())
                .add("defaultValue", defaultValueOf(javaType));

        fb.add("virtualColumn", columnFB);
    }

    private String defaultValueOf(String javaType) {
        switch (javaType) {
            case "byte":
            case "short":
            case "int":
            case "long":
                return "0";
            case "float":
                return "0.0f";
            case "double":
                return "0.0";
            default:
                return "null";
        }
    }

    private int addFactAttribute(Cube cube, FrameBuilder fb, SchemaSerialBuilder serialBuilder, int offset, Column column) {
        final int columnSize = column.asType().size();
        if(getMinimumBytesFor(offset, columnSize) > Long.BYTES)
            offset = roundUp2(offset, Long.SIZE);
        fb.add("column", columnFrame(column, offset, cube.name$()));
        serialBuilder.add(column.name$(), type(column.asType()), offset, column.asType().size());
        return offset + columnSize;
    }

    private int calculateFactSize(int offset) {
        return (int) Math.ceil(offset / (float)Byte.SIZE);
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
                .add("type", snakeCaseToCamelCase().format(axis.name$()).toString())
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
        if(primitive.equals("int")) {
            if(type.size() <= Byte.SIZE) return "byte";
            if(type.size() <= Short.SIZE) return "short";
        } else if(primitive.equals("double") && type.size() <= Float.SIZE) {
            return "float";
        }
        return primitive;
    }

    private String unboxed(String primitive) {
        switch (primitive) {
            case "Byte":
                return "byte";
            case "Short":
                return "short";
            case "Integer":
                return "int";
            case "Long":
            case "LongInteger":
                return "long";
            case "Float":
                return "float";
            case "Double":
                return "double";
        }
        return primitive;
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
        if(offset % size != 0) return false;
        return     javaType.equals("int") && size == Integer.SIZE
                || javaType.equals("short") && size == Short.SIZE
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
            if(column.isVirtual()) continue;
            if (column.isCategory())
                column.asType().size(sizeOf(column.asCategory().axis().asCategorical()));
        }
    }

    private int getMinimumBytesFor(int bitIndex, int bitCount) {
        final int bitOffset = offsetOf(bitIndex);
        final int numBytes = (int)Math.ceil((bitOffset + bitCount) / 8.0);
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
}

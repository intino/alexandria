package io.intino.konos.alexandria.activity.box.displays.adapters.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.intino.konos.alexandria.activity.box.model.dialog.Form;
import io.intino.konos.alexandria.activity.box.model.dialog.Value;
import io.intino.konos.alexandria.activity.box.schemas.Resource;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FormAdapter extends TypeAdapter<Form> {

    @Override
    public void write(JsonWriter out, Form form) throws IOException {
        out.beginObject();
        form.inputs().entrySet().forEach(entry -> {
            try {
                write(out, entry.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        out.endObject();
    }

    @Override
    public Form read(JsonReader jsonReader) throws IOException {
        return null;
    }

    private void write(JsonWriter out, List<Form.Input> inputList) throws IOException {
        if (inputList.size() <= 0) return;

        out.name(inputList.get(0).name());
        if (inputList.size() == 1) {
            write(out, inputList.get(0));
            return;
        }

        out.beginArray();
        inputList.forEach(child -> {
            try {
                write(out, child);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        out.endArray();
    }

    private void write(JsonWriter out, Form.Input input) throws IOException {
        out.beginObject();
        out.name("type").value(input.type());

        writeSection(out, input);
        writeValues(out, input);

        out.endObject();
    }

    private void writeSection(JsonWriter out, Form.Input input) {
        if (!(input instanceof Form.Section)) return;

        Form.Section section = (Form.Section) input;
        section.inputs().entrySet().forEach(entry -> {
            try {
                write(out, entry.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void writeValues(JsonWriter out, Form.Input input) throws IOException {
        List<Object> values = input.values().stream().map(Value::asObject).collect(toList());
        if (values.size() <= 0) return;

        if (values.size() == 1) {
            out.name("value").value(valueOf(values.get(0)));
            return;
        }

        out.name("values");
        out.beginArray();
        values.forEach(v -> {
            try {
                out.value(valueOf(v));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        out.endArray();
    }

    private String valueOf(Object value) {
        if (value instanceof String) return (String) value;
        if (value instanceof Resource) return ((Resource)value).value();
        return String.valueOf(value);
    }
}

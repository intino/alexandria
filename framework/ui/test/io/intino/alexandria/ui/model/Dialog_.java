package io.intino.alexandria.ui.model;

public class Dialog_ {

//    @Test
//    public void should_load_section_values() throws Exception {
//        Map<String, Object> paths = new HashMap() {{ put("Jobs.0.Tipo", "Screen On"); put("Jobs.1.Tipo", "Screen Off"); }};
//        Form form = Form.fromMap(paths, typeResolver());
//        Dialog dialog = createTest1Dialog(form);
//
//        assertEquals(dialog.input("Jobs").values().size(), 2);
//        assertEquals(dialog.input("Jobs").values().asStructure().get(0).get("Tipo").asString(), "Screen On");
//        assertEquals(dialog.input("Jobs").values().get(0).asStructure().get("Tipo").asString(), "Screen On");
//        assertEquals(dialog.input("Jobs.0.Tipo").value().asString(), "Screen On");
//        assertEquals(dialog.input("Jobs.0").value().asStructure().get("Tipo").asString(), "Screen On");
//        assertNull(dialog.input("Jobs.0.NotFoundField"));
//    }
//
//    @Test
//    public void should_load_deep_section_values() throws Exception {
//        Map<String, Object> paths = new HashMap() {{ put("Section 1.0.Section 2.0.Campo 1", "A"); put("Section 1.0.Section 2.1.Campo 1", "B"); }};
//        Form form = Form.fromMap(paths, typeResolver());
//        Dialog dialog = createTest2Dialog(form);
//
//        assertEquals(dialog.input("Section 1").values().size(), 1);
//        assertEquals(dialog.input("Section 1.0.Section 2").values().size(), 2);
//        assertEquals(dialog.input("Section 1.0.Section 2").values().asStructure().get(0).get("Campo 1").asString(), "A");
//        assertEquals(dialog.input("Section 1.0.Section 2").values().asStructure().get(1).get("Campo 1").asString(), "B");
//        assertEquals(dialog.input("Section 1.0.Section 2.0.Campo 1").value().asString(), "A");
//        assertEquals(dialog.input("Section 1.0.Section 2.1.Campo 1").value().asString(), "B");
//    }
//
//    @Test
//    public void should_set_multiple_section_value() throws Exception {
//        Dialog dialog = createTest2Dialog(null);
//
//        dialog.input("Section 1.0.Section 2.0.Campo 1").value("A");
//        assertEquals(dialog.input("Section 1.0.Section 2.0.Campo 1").value().asString(), "A");
//
//        dialog.input("Section 1.0.Section 2.1.Campo 1").value("B");
//        assertEquals(dialog.input("Section 1.0.Section 2.1.Campo 1").value().asString(), "B");
//    }
//
//    private Dialog createTest1Dialog(Form form) {
//        Dialog dialog = form != null ? new Dialog(form) : new Dialog();
//        createTest1Tab(dialog);
//        return dialog;
//    }
//
//    private Dialog.Tab createTest1Tab(Dialog dialog) {
//        Dialog.Tab tab = dialog.createTab("Jobs pantalla");
//        Dialog.Tab.Section section = tab.createSection();
//        section.multiple(-1,-1).label("Jobs");
//        section.createComboBox().source(input -> new ArrayList<String>() {{ add("Screen on"); add("Screen off"); }}).label("Tipo");
//        section.createText().label("Patrón");
//        return tab;
//    }
//
//    private Dialog createTest2Dialog(Form form) {
//        Dialog dialog = form != null ? new Dialog(form) : new Dialog();
//        createTest2Tab(dialog);
//        return dialog;
//    }
//
//    private Dialog.Tab createTest2Tab(Dialog dialog) {
//        Dialog.Tab tab = dialog.createTab("Tab 1");
//        Dialog.Tab.Section section = tab.createSection();
//        section.multiple(-1,-1).label("Section 1");
//        Dialog.Tab.Section section2 = section.createSection();
//        section2.multiple(-1,-1).label("Section 2");
//        section2.createComboBox().source(input -> new ArrayList<String>() {{ add("A"); add("B"); }}).label("Campo 1");
//        section.createText().label("Campo 2");
//        return tab;
//    }
//
//    private Form.TypeResolver typeResolver() {
//        return new Form.TypeResolver() {
//            @Override
//            public String type(Form.Input input) {
//                return type(input.name());
//            }
//
//            @Override
//            public String type(String inputName) {
//                return inputName.contains("Jobs") || inputName.contains("Section") ? "section" : "text";
//            }
//        };
//    }

}
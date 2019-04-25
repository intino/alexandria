package io.intino.konos.builder.actions;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.ui.StripeTable;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import io.intino.konos.model.graph.Service;
import io.intino.plugin.IntinoIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;
import static javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN;

public class SelectServicesDialog {
	private static final Object[] FIELDS = {"Name", "Select"};
	private JPanel servicesPanel;
	private JBTable table;
	private Window parent;
	private final List<Service> services;

	public SelectServicesDialog(Window parent, List<Service> services) {
		this.parent = parent;
		this.services = services;
		createUIComponents();
	}

	List<Service> showAndGet() {
		final List[] services = new List[]{new ArrayList<>()};
		final Application application = ApplicationManager.getApplication();
		application.invokeAndWait(() -> {
			String[] options = new String[]{"Cancel", "Accept"};
			int option = JOptionPane.showOptionDialog(parent, servicesPanel, "Select services to distribute",
					YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, IntinoIcons.INTINO_80, options, options[1]);
			services[0] = option == 1 ? selectedServices() : emptyList();
		}, ModalityState.any());
		return services[0];
	}

	private List<Service> selectedServices() {
		List<Service> list = new ArrayList<>();
		for (int i = 0; i < table.getModel().getRowCount(); i++)
			if ((boolean) table.getModel().getValueAt(i, 1))
				list.add(findService(table.getModel().getValueAt(i, 0).toString()));
		return list;
	}

	private Service findService(String value) {
		return this.services.stream().filter(service -> service.name$().equalsIgnoreCase(value)).findFirst().orElse(null);
	}

	private void createUIComponents() {
		servicesPanel = new JPanel();
		final DefaultTableModel tableModel = new DefaultTableModel(servicesData(), FIELDS) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column != 0 && checkExist(this.getValueAt(row, 0).toString());
			}
		};
		tableModel.setColumnIdentifiers(FIELDS);
		table = newTable(tableModel);
		table.setEnableAntialiasing(true);
		table.getEmptyText().setText("No Services");
		table.setAutoResizeMode(AUTO_RESIZE_LAST_COLUMN);
		table.getColumn(FIELDS[0]).setPreferredWidth(150);
		table.getColumn(FIELDS[1]);
		servicesPanel = ToolbarDecorator.createDecorator(table).disableUpAction().disableDownAction().createPanel();
		servicesPanel.setMaximumSize(new Dimension(400, 200));
		servicesPanel.setMinimumSize(new Dimension(400, 200));
		table.setMaximumSize(new Dimension(400, 200));
		table.setMinimumSize(new Dimension(400, 200));
		table.setPreferredSize(new Dimension(400, 200));
		servicesPanel.setPreferredSize(new Dimension(400, 200));
	}

	private boolean checkExist(String value) {
		return services.stream().anyMatch(service -> service.name$().equalsIgnoreCase(value));
	}

	@NotNull
	private StripeTable newTable(DefaultTableModel tableModel) {
		return new StripeTable(tableModel) {
			private static final long serialVersionUID = 1L;

			public Class getColumnClass(int column) {
				switch (column) {
					case 0:
						return String.class;
					default:
						return Boolean.class;
				}
			}
		};
	}

	private Object[][] servicesData() {
		Object[][] objects = new Object[services.size()][3];
		for (int i = 0; i < services.size(); i++)
			objects[i] = new Object[]{services.get(i).name$(), false};
		return objects;
	}

}
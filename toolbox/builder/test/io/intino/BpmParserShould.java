package io.intino;

import io.intino.konos.builder.codegeneration.bpm.parser.BpmnParser;
import io.intino.konos.builder.codegeneration.bpm.parser.Link;
import io.intino.konos.builder.codegeneration.bpm.parser.State;
import org.junit.Test;

import java.util.List;

import static java.lang.System.out;

public class BpmParserShould {

	@Test
	public void parse_corregir_adeudo_process() {
		final String path = "/bpmn/CorregirAdeudoProcess.bpmn";
		BpmnParser parser = new BpmnParser(this.getClass().getResourceAsStream(path));
		List<State> states = parser.states();
		check(states);
	}

	@Test
	public void parse_gestionar_facturacion_process() {
		final String path = "/bpmn/GestionarFacturacionProcess.bpmn";
		BpmnParser parser = new BpmnParser(this.getClass().getResourceAsStream(path));
		List<State> states = parser.states();
		check(states);
	}

	@Test
	public void parse_procesar_domiciliaciones() {
		final String path = "/bpmn/ProcesarDomiciliacionesProcess.bpmn";
		BpmnParser parser = new BpmnParser(this.getClass().getResourceAsStream(path));
		List<State> states = parser.states();
		check(states);
	}


	@Test
	public void parse_ingreso_asociado_process() {
		final String path = "/bpmn/IngresoAsociadoProcess.bpmn";
		BpmnParser parser = new BpmnParser(this.getClass().getResourceAsStream(path));
		List<State> states = parser.states();
		check(states);
	}

	private void check(List<State> states) {
		for (State state : states) {
			out.println(state.label() + " : " + state.taskType() + "(" + state.type() + ")" + " comment: " + state.comment());
			if (state.links().isEmpty()) continue;
			out.println("\tlinked with: ");
			for (Link link : state.links())
				out.println("\t\t" + link.to() + " " + link.type() + (link.isDefault() ? " default" : ""));
			out.println();
		}
	}

}

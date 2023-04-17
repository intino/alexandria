package io.intino.alexandria.datalake.tests;

import io.intino.alexandria.FS;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.event.message.MessageEventWriter;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.LegacyMessageReader;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class MigrateEventsToNewDatalake {

	// TODO: hay algunos tipos de eventos, como MedicionFacturacion, que tienen como ss identificadores unicos, lo que multiplica
	// el numero de subdirectorios en el tanque de forma bestial. Hay que abordar este problema al migrarlo. Quizas el ss ponerlo como
	// default y el identificador almacenarlo como un atributo dentro del mensaje

	// TODO: si el ss de un evento esta en un evento de trace
	//  si es enzyme => ss = digester
	//  si es feeder => ss = feeder

	// ui?<old_ss>
	// unknown
	// digester
	// feeder
	// eventos de timbrado -> api
	// remesa servicio solicitada -> api
	// todos los unknown en un fichero de texto
	public static void main(String[] args) throws Exception {
//		eventsWithBadSSNames();
//		ssHistogram();
		new MigrateEventsToNewDatalake().run();
	}

	private static void eventsWithBadSSNames() throws Exception {
		File oldDatalakeRoot = new File("temp/cm/old_datalake/events");
		long count = 0;
		Set<String> set = new HashSet<>();
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(oldDatalakeRoot.getParentFile().getParentFile(), "ss_with_bad_chars.tsv")))) {
			for(File file : FS.allFilesIn(oldDatalakeRoot, f -> f.getName().endsWith(".zim")).collect(Collectors.toList())) {
				boolean found = false;
				System.out.println(file);
				try(MessageReader reader = new MessageReader(new GZIPInputStream(new FileInputStream(file)))) {
					while(reader.hasNext()) {
						Message m = reader.next();
						String ss = m.contains("ss") ? m.get("ss").asString() : "";
						if(ss.contains(":")) {
							String id = m.type() + "\t" + ss;
							if(set.add(id)) {
								writer.write(id);
								writer.newLine();
								found = true;
								++count;
							}
						}
					}
				}
				if(found) {
					writer.newLine();
				}
			}
		}
		System.out.println("Done: " + count);
	}

	private static void ssHistogram() throws Exception {
		Map<String, Integer> histogram = new HashMap<>();
		File oldDatalakeRoot = new File("temp/cm/old_datalake/events");
		for(File file : FS.allFilesIn(oldDatalakeRoot, f -> f.getName().endsWith(".zim")).collect(Collectors.toList())) {
			try(MessageReader reader = new MessageReader(new GZIPInputStream(new FileInputStream(file)))) {
				while(reader.hasNext()) {
					Message m = reader.next();
					String ss = m.contains("ss") ? m.get("ss").asString() : "";
					if(ss.length() > 20) histogram.compute(m.type(), (k, v) -> v == null ? 1 : v + 1);
				}
			}
		}

		histogram.entrySet().stream()
				.filter(e -> e.getValue() <= 5)
				.forEach(e -> System.out.println(e.getKey()));

//				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//				.forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

	}

	private final AtomicInteger count = new AtomicInteger();

	public void run() throws IOException {
		File oldDatalakeRoot = new File("temp/cm/old_datalake/events");
		File newDatalakeRoot = new File("temp/cm/new_datalake/messages");
		reset(newDatalakeRoot);

		long start = System.currentTimeMillis();

		List<File> directories = FS.directoriesIn(oldDatalakeRoot).collect(Collectors.toList());
		int total = (int) FS.allFilesIn(oldDatalakeRoot, f -> f.getName().endsWith(".zim")).count();
		AtomicInteger tankCount = new AtomicInteger();

		directories.stream()
//				.parallel()
//				.filter(tank -> tank.getName().equals("comercial.cuentamaestra.FacturacionServicioRechazada"))
				.sorted(Comparator.comparing(FileUtils::sizeOfDirectory))
				.forEach(tank -> {
					try {
						var zims = FS.allFilesIn(tank, f -> f.getName().endsWith(".zim")).collect(Collectors.toList());
						System.out.println("Tank: " + tank.getName() + " (" + tankCount.incrementAndGet() + "/" + directories.size() + ") with " + zims.size() + " .zim, " + (FileUtils.sizeOfDirectory(tank)/1024.0f/1024.0f) + " MB");
						Map<File, EventWriter<MessageEvent>> writers = new ConcurrentHashMap<>();
						zims.forEach(zim -> migrate(zim, newDatalakeRoot, total));
						close(writers);
					} catch (IOException e) {
						Logger.error(e);
					}
				});

		System.out.println("Complete (" + count.get() + " / " + total + "): " + ((System.currentTimeMillis() - start) / 1000.0f) + " seconds");
	}

	private static void reset(File newDatalakeRoot) throws IOException {
		if(newDatalakeRoot.exists()) {
			File dest = new File(newDatalakeRoot.getAbsolutePath() + System.currentTimeMillis());
			if(newDatalakeRoot.renameTo(dest))
				new Thread(() -> {
					try {
						FileUtils.deleteDirectory(dest);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}).start();
			else
				FileUtils.deleteDirectory(newDatalakeRoot);
		}
		newDatalakeRoot.mkdirs();
	}

	private void close(Map<File, EventWriter<MessageEvent>> writers) {
		for(EventWriter<MessageEvent> writer : writers.values()) {
			try {
				writer.close();
			} catch (Exception e) {
				Logger.error(e);
			}
		}
		writers.clear();
	}

	private void migrate(File zim, File newDatalakeRoot, int total) {
		try(LegacyMessageReader reader = new LegacyMessageReader(open(zim))) {

			String tankName = zim.getParentFile().getName();
			Map<String, List<MessageEvent>> events = new HashMap<>();

			while(reader.hasNext()) {
				Message message = reader.next();
				if(!message.contains("ss")) {
					message.set("ss", defaultSSOf(message.type()));
				} else if(message.get("ss").asString().contains(":")) {
					String oldSS = message.get("ss").asString();
					message.set("ss", defaultSSOf(message.type()));
					message.set("old_ss", oldSS);
				}
				String ss = message.get("ss").asString();
				MessageEvent event = new MessageEvent(message);
				events.computeIfAbsent(ss, k -> new ArrayList<>()).add(event);
			}

			for(var entry : events.entrySet()) {
				File file = new File(newDatalakeRoot, tankName + "/" + entry.getKey() + "/" + timetag(zim) + ".zim");
				file.getParentFile().mkdirs();
				try(MessageEventWriter writer = new MessageEventWriter(file, true)) {
					writer.write(entry.getValue());
				}
				count.incrementAndGet();
			}

//			int c = count.incrementAndGet();
//			if(c % 100 == 0) System.out.println(">> " + String.format("%.02f%%", (c / (float)total) * 100));
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	public static String defaultSSOf(String type) {
		switch(type) {
			case "ContratoEliminado":
			case "Contactos":
				return "contratacion";

			case "GestionAnticipo":
			case "Consignacion":
			case "Reintegro":
			case "ExpedienteCobro":
			case "ExpedienteCobroTerminado":
			case "GestionCobro":
			case "GestionCobroTerminada":
			case "GestionCobroEliminada":
			case "Adeudo":
			case "AdeudosRechazados":
			case "Cobro":
			case "Ingreso":
			case "IngresoAsociado":
			case "IngresoDesasociado":
			case "IngresoEliminado":
			case "IngresoEjecutado":
			case "Asiento":
			case "FacturacionServicioCobrada":
			case "FacturacionServicioRechazada":
			case "PolizaDispersada":
			case "Poliza":
			case "CfdiReemplazo":
				return "cuentamaestra";

			case "ExpedientePago":
			case "EstadoCuentaAprobado":
			case "EstadoCuentaRechazado":
			case "ComprobantesPago":
			case "MedicionGeneracion":
			case "Pml":
				return "generadores";

			case "RemesaServicioCargada":
			case "RemesaServicioBorrada":
			case "RemesaServicioRevisada":
			case "RemesaServicioSolicitada":
			case "FacturacionGeneradorSolicitada":
			case "Contrato":
				return "expedicion";

			case "AbortoTimbradoSolicitado":
			case "TimbradoRechazado":
			case "TimbradoFinalizado":
				return "timbrado";

			case "SolicitudDomiciliacion":
			case "EstadoDomiciliaciones":
			case "SolicitudAcuseSolicitudDomiciliacion":
			case "Domiciliaciones":
			case "SolicitudCobroDomiciliado":
			case "PagosRecibidos":
			case "Contracargo":
			case "AcusesCobro":
			case "InfoGestor":
			case "PagosRecurrentes":
			case "SolicitudSicom":
				return "cobel";

			case "FacturaConciliacion":
			case "ComplementoPagoConciliacion":
			case "AnticipoConciliacion":
			case "NotaCreditoConciliacion":
			case "AnalisisConciliacion":
				return "conciliacion";
		}

		return "unknown";
	}

	private String normalize(String ss) {
		if(ss.length() > 20) return "default"; // Suspicious ss, lets set it to default
		return ss.replaceAll("[:]", "-");
	}

	private String timetag(File zim) {
		return Timetag.of(zim.getName().replace(".zim", "")).value();
	}

	private InputStream open(File zim) throws IOException {
		return new GZIPInputStream(new BufferedInputStream(new FileInputStream(zim)));
	}
}

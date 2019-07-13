package io.intino.alexandria.datahub.datalake;

import io.intino.alexandria.datahub.model.Configuration;
import io.intino.alexandria.logger.Logger;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.sftp.SFTPFileTransfer;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.intino.alexandria.datahub.model.Configuration.Tanks.Tank.Type.Event;
import static io.intino.alexandria.datahub.model.Configuration.Tanks.Tank.Type.Set;
import static java.io.File.separator;


public class DatalakeCloner {
	private final Configuration.Tanks tanks;
	private final Configuration.Mirror conf;

	public DatalakeCloner(Configuration.Mirror conf, Configuration.Tanks tanks) {
		this.conf = conf;
		this.tanks = tanks;
	}

	public void execute() {
		Logger.info("Mirroring datalake from " + conf.originUrl() + " to " + conf.datalakeDestinationPath());
		Logger.includeLevel(Logger.Level.TRACE);
		SSHClient ssh = new SSHClient();
		String[] urlAndPort = conf.originUrl().split(":");
		try {
			ssh.loadKnownHosts();
			ssh.connect(urlAndPort[0], Integer.parseInt(urlAndPort[1]));
			ssh.authPublickey("root", System.getProperty("user.home") + separator + ".ssh" + separator + "id_rsa");
			SFTPClient sftpClient = ssh.newSFTPClient();
			cloneEventTanks(sftpClient);
			cloneSetTanks(sftpClient);
		} catch (IOException e) {
			Logger.error(e);
		}
		Logger.info("Mirroring finished");
	}

	private void cloneEventTanks(SFTPClient client) throws IOException {
		SFTPFileTransfer fileTransfer = client.getFileTransfer();
		for (Configuration.Tanks.Tank tank : eventTanks()) {
			File destinationTank = new File(conf.datalakeDestinationPath() + separator + "events" + separator + tank.name());
			destinationTank.mkdirs();
			List<RemoteResourceInfo> zims = client.ls(conf.datalakeOriginPath() + "/events/" + tank.name());
			zims.sort(Comparator.comparing(RemoteResourceInfo::getName));
			downloadZims(fileTransfer, zims, destinationTank);
		}
	}

	private void downloadZims(SFTPFileTransfer fileTransfer, List<RemoteResourceInfo> zims, File destinationTank) throws IOException {
		String startingTimetag = conf.startingTimetag() + ".zim";
		for (RemoteResourceInfo zim : zims)
			if (zim.getName().replace(".zim", "").compareTo(startingTimetag) >= 0 && !new File(destinationTank, zim.getName()).exists())
				fileTransfer.download(zim.getPath(), new File(destinationTank, zim.getName()).getPath());
	}

	private void cloneSetTanks(SFTPClient client) throws IOException {
		SFTPFileTransfer fileTransfer = client.getFileTransfer();
		for (String tank : setTanks()) {
			File destinationTank = new File(conf.datalakeDestinationPath() + separator + "sets" + separator + tank + separator);
			destinationTank.mkdirs();
			List<RemoteResourceInfo> tubs = client.ls(conf.datalakeOriginPath() + "/sets/" + tank);
			tubs.sort(Comparator.comparing(RemoteResourceInfo::getName));
			downloadZets(fileTransfer, destinationTank, tubs);
		}
	}

	private void downloadZets(SFTPFileTransfer fileTransfer, File destinationTank, List<RemoteResourceInfo> tubs) throws IOException {
		String startingTimetag = conf.startingTimetag();
		for (RemoteResourceInfo tub : tubs)
			if (tub.getName().compareTo(startingTimetag) >= 0 && !new File(destinationTank, tub.getName()).exists())
				fileTransfer.download(tub.getPath(), new File(destinationTank, tub.getName()).getPath());
	}

	private List<Configuration.Tanks.Tank> eventTanks() {
		return tanks.tanks().stream().filter(t -> t.type().equals(Event)).collect(Collectors.toList());
	}

	private List<String> setTanks() {
		return tanks.tanks().stream().filter(t -> t.type().equals(Set)).map(this::namesOf).flatMap(Collection::stream).collect(Collectors.toList());
	}

	private List<String> namesOf(Configuration.Tanks.Tank tank) {
		if (tank.split() == null) return Collections.singletonList(tank.name());
		Configuration.Tanks.Split split = tanks.splits().stream().filter(s -> s.name().equals(tank.split())).findFirst().orElse(null);
		if (split == null) return Collections.singletonList(tank.name());
		return split.values().stream().map(v -> v + "." + tank.name()).collect(Collectors.toList());
	}
}

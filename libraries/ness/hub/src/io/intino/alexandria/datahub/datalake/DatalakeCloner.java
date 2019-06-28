package io.intino.alexandria.datahub.datalake;

import com.jcraft.jsch.*;
import io.intino.alexandria.datahub.model.Configuration;
import io.intino.alexandria.logger.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import static io.intino.alexandria.datahub.model.Configuration.Tanks.Tank.Type.Event;
import static io.intino.alexandria.datahub.model.Configuration.Tanks.Tank.Type.Set;


public class DatalakeCloner {
	private final Configuration.Tanks tanks;
	private final Configuration.Mirror mirror;

	public DatalakeCloner(Configuration.Mirror mirror, Configuration.Tanks tanks) {
		this.mirror = mirror;
		this.tanks = tanks;
	}

	public void execute() {
		Logger.info("Cloning datalake from " + mirror.originUrl() + " to " + mirror.datalakeDestinationPath());
		JSch jsch = new JSch();
		String[] split = mirror.originUrl().split(":");
		try {
			Session session = jsch.getSession(mirror.user(), mirror.originUrl().split(":")[0], split.length == 2 ? Integer.parseInt(split[1]) : 22);
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp c = (ChannelSftp) channel;
			cloneEventTanks(c);
			cloneSetTanks(c);
		} catch (JSchException e) {
			Logger.error(e);
		}
	}

	private void cloneEventTanks(ChannelSftp c) {
		String startingTimetag = mirror.startingTimetag() + ".zim";
		tanks.tanks().stream().filter(t -> t.type().equals(Event)).forEach(tank -> {
			try {
				c.lcd(mirror.datalakeDestinationPath());
				c.cd(mirror.datalakeOriginPath() + "/events/" + tank.name());
				Vector<ChannelSftp.LsEntry> list = c.ls(".");
				for (ChannelSftp.LsEntry item : list)
					if (item.getFilename().compareTo(startingTimetag) >= 0) c.get(item.getFilename(), item.getFilename());
			} catch (SftpException e) {
				Logger.error(e);
			}
		});
	}

	private void cloneSetTanks(ChannelSftp c) {
		String startingTimetag = mirror.startingTimetag();
		tanks.tanks().stream().filter(t -> t.type().equals(Set)).map(this::namesOf).flatMap(Collection::stream).forEach(tank -> {
			try {
				c.lcd(mirror.datalakeDestinationPath());
				c.cd(mirror.datalakeOriginPath() + "/sets/" + tank);
				cloneSetTank(c, startingTimetag);
			} catch (SftpException e) {
				Logger.error(e);
			}
		});
	}

	private List<String> namesOf(Configuration.Tanks.Tank tank) {
		if (tank.split() == null) return Collections.singletonList(tank.name());
		Configuration.Tanks.Split split = tanks.splits().stream().filter(s -> s.name().equals(tank.split())).findFirst().orElse(null);
		if (split == null) return Collections.singletonList(tank.name());
		return split.values().stream().map(v -> v + "." + tank.name()).collect(Collectors.toList());
	}

	private void cloneSetTank(ChannelSftp c, String startingTimetag) throws SftpException {
		Vector<ChannelSftp.LsEntry> list = c.ls(".");
		for (ChannelSftp.LsEntry item : list)
			if (item.getAttrs().isDir() && item.getFilename().compareTo(startingTimetag) >= 0)
				c.get(item.getFilename(), item.getFilename());
	}
}

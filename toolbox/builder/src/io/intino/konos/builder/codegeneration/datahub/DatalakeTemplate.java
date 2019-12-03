package io.intino.konos.builder.codegeneration.datahub;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class DatalakeTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("datalake", "nfs"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(";\nimport io.intino.alexandria.logger.Logger;\n\nimport java.io.File;\nimport java.io.IOException;\nimport java.util.Collection;\nimport java.util.Collections;\nimport java.util.Comparator;\nimport java.util.List;\nimport java.util.stream.Collectors;\n\npublic class Datalake extends io.intino.alexandria.datalake.file.FileDatalake {\n\n\tprivate DatalakeCloner cloner;\n\n\tDatalake(File root, String originDatalakePath, String startingTimetag) {\n\t\tsuper(root);\n\t\tthis.cloner = new DatalakeCloner(root, originDatalakePath, startingTimetag, eventTanks(), setTanks());\n\t}\n\n\tvoid init() {\n\t\tcloner.execute();\n\t}\n\n\tpublic List<String> eventTanks() {\n\t\treturn java.util.Arrays.asList(")).output(mark("eventTank", "quoted").multiple(", ")).output(literal(");\n\t}\n\n\tpublic List<String> setTanks() {\n\t\treturn java.util.Arrays.asList(")).output(mark("setTank", "quoted").multiple(", ")).output(literal(");\n\t}\n\n\tprivate class DatalakeCloner {\n\t\tprivate final File destination;\n\t\tprivate final String originDatalakePath;\n\t\tprivate final String startingTimetag;\n\t\tprivate final List<String> eventTanks;\n\t\tprivate final List<String> setTanks;\n\n\t\tpublic DatalakeCloner(File destination, String originDatalakePath, String startingTimetag, List<String> eventTanks, List<String> setTanks) {\n\t\t\tthis.destination = destination;\n\t\t\tthis.originDatalakePath = originDatalakePath;\n\t\t\tthis.startingTimetag = startingTimetag;\n\t\t\tthis.eventTanks = eventTanks;\n\t\t\tthis.setTanks = setTanks;\n\t\t}\n\n\t\tpublic void execute() {\n\t\t\tLogger.info(\"Mirroring datalake from \" + this.originDatalakePath + \" to \" + destination);\n\t\t\ttry {\n\t\t\t\tcloneEventTanks();\n\t\t\t\tcloneSetTanks();\n\t\t\t\tLogger.info(\"Mirroring finished\");\n\t\t\t} catch (IOException e) {\n\t\t\t\tLogger.error(e);\n\t\t\t}\n\t\t}\n\n\t\tprivate void cloneEventTanks() throws IOException {\n\t\t\tfor (String tank : this.eventTanks) {\n\t\t\t\tFile destinationTank = new File(destination + File.separator + \"events\" + File.separator + tank);\n\t\t\t\tdestinationTank.mkdirs();\n\t\t\t\tFile file = new File(this.originDatalakePath + \"/events/\" + tank);\n\t\t\t\tif (!file.exists()) continue;\n\t\t\t\tList<File> eventFiles = java.util.Arrays.asList(file.listFiles(f-> f.getName().endsWith(\".zim\")));\n\t\t\t\teventFiles.sort(Comparator.comparing(File::getName));\n\t\t\t\tcopyEventFiles(eventFiles, destinationTank);\n\t\t\t}\n\t\t}\n\n\t\tprivate void cloneSetTanks() throws IOException {\n\t\t\tfor (String tank : this.setTanks) {\n\t\t\t\tFile destinationTank = new File(destination + File.separator + \"sets\" + File.separator + tank + File.separator);\n\t\t\t\tdestinationTank.mkdirs();\n\t\t\t\tFile file = new File(this.originDatalakePath + \"/sets/\" + tank);\n\t\t\t\tif (!file.exists()) continue;\n\t\t\t\tList<File> tubs = java.util.Arrays.asList(file.listFiles(f-> f.isDirectory()));\n\t\t\t\ttubs.sort(Comparator.comparing(File::getName));\n\t\t\t\tcopyZets(tubs, destinationTank);\n\t\t\t}\n\t\t}\n\n\t\tprivate void copyEventFiles(List<File> zims, File destinationTank) throws IOException {\n\t\t\tString startingTimetag = this.startingTimetag + \".zim\";\n\t\t\tfor (File zim : zims)\n\t\t\t\tif (zim.getName().replace(\".zim\", \"\").compareTo(startingTimetag) >= 0 && !new File(destinationTank, zim.getName()).exists())\n\t\t\t\t\tjava.nio.file.Files.copy(zim.toPath(), new File(destinationTank, zim.getName()).toPath());\n\t\t}\n\n\t\tprivate void copyZets(List<File> tubs, File destinationTank) throws IOException {\n\t\t\tfor (File tub : tubs)\n\t\t\t\tif (tub.getName().compareTo(this.startingTimetag) >= 0 && !new File(destinationTank, tub.getName()).exists())\n\t\t\t\t\tcopyDirectory(tub, new File(destinationTank, tub.getName()));\n\t\t}\n\n\t\tprivate void copyDirectory(File from, File to) throws IOException {\n\t\t\tto.mkdirs();\n\t\t\tfor (File zet : java.util.Objects.requireNonNull(from.listFiles())) {\n\t\t\t\tjava.nio.file.Files.copy(zet.toPath(), new File(to, zet.getName()).toPath());\n\t\t\t}\n\t\t}\n\t}\n}\n")),
				rule().condition((allTypes("datalake", "ssh"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(";\n\nimport io.intino.alexandria.logger.Logger;\nimport net.schmizz.sshj.SSHClient;\nimport net.schmizz.sshj.sftp.RemoteResourceInfo;\nimport net.schmizz.sshj.sftp.SFTPClient;\nimport net.schmizz.sshj.sftp.SFTPFileTransfer;\n\nimport java.io.File;\nimport java.io.IOException;\nimport java.util.Collection;\nimport java.util.Collections;\nimport java.util.Comparator;\nimport java.util.List;\nimport java.util.stream.Collectors;\n\npublic class Datalake extends io.intino.alexandria.datalake.file.FileDatalake {\n\n\tprivate DatalakeCloner cloner;\n\n\tDatalake(File root, String url, String originDatalakePath, String user, String password, String startingTimetag) {\n\t\tsuper(root);\n\t\tthis.cloner = new DatalakeCloner(url, originDatalakePath, user, password, startingTimetag, root, eventTanks(), setTanks());\n\t}\n\n\tvoid init() {\n\t\tcloner.execute();\n\t}\n\n\tprivate List<String> eventTanks() {\n\t\treturn java.util.Arrays.asList(")).output(mark("eventTank", "quoted").multiple(", ")).output(literal(");\n\t}\n\n\tprivate List<String> setTanks() {\n\t\treturn java.util.Arrays.asList(")).output(mark("setTank", "quoted").multiple(", ")).output(literal(");\n\t}\n\n\tprivate class DatalakeCloner {\n\t\tprivate final String url;\n\t\tprivate final String user;\n\t\tprivate final String password;\n\t\tprivate final String originDatalakePath;\n\t\tprivate final String startingTimetag;\n\t\tprivate final String destination;\n\t\tprivate final List<String> eventTanks;\n\t\tprivate final List<String> setTanks;\n\n\t\tpublic DatalakeCloner(String url, String originDatalakePath, String user, String password, String startingTimetag, File destination, List<String> eventTanks, List<String> setTanks) {\n\t\t\tthis.url = url;\n\t\t\tthis.originDatalakePath = originDatalakePath;\n\t\t\tthis.user = user;\n\t\t\tthis.password = password;\n\t\t\tthis.startingTimetag = startingTimetag;\n\t\t\tthis.destination = destination.getPath();\n\t\t\tthis.eventTanks = eventTanks;\n\t\t\tthis.setTanks = setTanks;\n\t\t}\n\n\t\tpublic void execute() {\n\t\t\tLogger.info(\"Mirroring datalake from \" + this.url + \" to \" + destination);\n\t\t\tSSHClient ssh = new SSHClient();\n\t\t\tString[] urlAndPort = this.url.split(\":\");\n\t\t\ttry {\n\t\t\t\tssh.loadKnownHosts();\n\t\t\t\tssh.connect(urlAndPort[0], Integer.parseInt(urlAndPort[1]));\n\t\t\t\tssh.authPublickey(user, System.getProperty(\"user.home\") + File.separator + \".ssh\" + File.separator + \"id_rsa\");\n\t\t\t\tSFTPClient sftpClient = ssh.newSFTPClient();\n\t\t\t\tcloneEventTanks(sftpClient);\n\t\t\t\tcloneSetTanks(sftpClient);\n\t\t\t\tLogger.info(\"Mirroring finished\");\n\t\t\t} catch (IOException e) {\n\t\t\t\tLogger.error(e);\n\t\t\t}\n\t\t}\n\n\t\tprivate void cloneEventTanks(SFTPClient client) throws IOException {\n\t\t\tSFTPFileTransfer fileTransfer = client.getFileTransfer();\n\t\t\tfor (String tank : this.eventTanks) {\n\t\t\t\tFile destinationTank = new File(destination + File.separator + \"events\" + File.separator + tank);\n\t\t\t\tdestinationTank.mkdirs();\n\t\t\t\tList<RemoteResourceInfo> zims = client.ls(this.originDatalakePath + \"/events/\" + tank);\n\t\t\t\tzims.sort(Comparator.comparing(RemoteResourceInfo::getName));\n\t\t\t\tdownloadZims(fileTransfer, zims, destinationTank);\n\t\t\t}\n\t\t}\n\n\t\tprivate void cloneSetTanks(SFTPClient client) throws IOException {\n\t\t\tSFTPFileTransfer fileTransfer = client.getFileTransfer();\n\t\t\tfor (String tank : this.setTanks) {\n\t\t\t\tFile destinationTank = new File(destination + File.separator + \"sets\" + File.separator + tank + File.separator);\n\t\t\t\tdestinationTank.mkdirs();\n\t\t\t\tList<RemoteResourceInfo> tubs = client.ls(this.originDatalakePath + \"/sets/\" + tank);\n\t\t\t\ttubs.sort(Comparator.comparing(RemoteResourceInfo::getName));\n\t\t\t\tdownloadZets(fileTransfer, destinationTank, tubs);\n\t\t\t}\n\t\t}\n\n\t\tprivate void downloadZims(SFTPFileTransfer fileTransfer, List<RemoteResourceInfo> zims, File destinationTank) throws IOException {\n\t\t\tString startingTimetag = this.startingTimetag + \".zim\";\n\t\t\tfor (RemoteResourceInfo zim : zims)\n\t\t\t\tif (zim.getName().replace(\".zim\", \"\").compareTo(startingTimetag) >= 0 && !new File(destinationTank, zim.getName()).exists())\n\t\t\t\t\tfileTransfer.download(zim.getPath(), new File(destinationTank, zim.getName()).getPath());\n\t\t}\n\n\t\tprivate void downloadZets(SFTPFileTransfer fileTransfer, File destinationTank, List<RemoteResourceInfo> tubs) throws IOException {\n\t\t\tfor (RemoteResourceInfo tub : tubs)\n\t\t\t\tif (tub.getName().compareTo(this.startingTimetag) >= 0 && !new File(destinationTank, tub.getName()).exists())\n\t\t\t\t\tfileTransfer.download(tub.getPath(), new File(destinationTank, tub.getName()).getPath());\n\t\t}\n\t}\n}"))
		);
	}
}
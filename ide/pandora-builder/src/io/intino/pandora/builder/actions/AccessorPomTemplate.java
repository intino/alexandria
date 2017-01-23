package io.intino.pandora.builder.actions;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AccessorPomTemplate extends Template {

	protected AccessorPomTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AccessorPomTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "pom"))).add(literal("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n<modelVersion>4.0.0</modelVersion>\n\n<groupId>")).add(mark("group", "lowercase")).add(literal("</groupId>\n<artifactId>")).add(mark("artifact", "lowercase")).add(literal("</artifactId>\n<version>")).add(mark("version")).add(literal("</version>\n")).add(expression().add(literal("<licenses>")).add(literal("\n")).add(literal("\t")).add(mark("license").multiple("\n")).add(literal("\n")).add(literal("</licenses>"))).add(literal("\n<properties>\n\t<maven.compiler.source>1.8</maven.compiler.source>\n\t<maven.compiler.target>1.8</maven.compiler.target>\n</properties>\n\n<build>\n    <sourceDirectory>src</sourceDirectory>\n\t<outputDirectory>out/production/")).add(mark("artifact", "lowercase")).add(literal("</outputDirectory>\n\t<testOutputDirectory>out/test/")).add(mark("artifact", "lowercase")).add(literal("</testOutputDirectory>\n\t<directory>out/build/")).add(mark("artifact", "lowercase")).add(literal("</directory>\n\t<plugins>\n\t\t<plugin>\n\t\t\t<groupId>org.apache.maven.plugins</groupId>\n\t\t\t<artifactId>maven-source-plugin</artifactId>\n\t\t\t<version>3.0.1</version>\n\t\t\t<executions>\n\t\t\t\t<execution>\n\t\t\t\t\t<id>attach-sources</id>\n\t\t\t\t\t<goals>\n\t\t\t\t\t\t<goal>jar-no-fork</goal>\n\t\t\t\t\t</goals>\n\t\t\t\t</execution>\n\t\t\t</executions>\n\t\t</plugin>\n\t\t<plugin>\n\t\t\t<groupId>org.apache.maven.plugins</groupId>\n\t\t\t<artifactId>maven-javadoc-plugin</artifactId>\n\t\t\t<version>2.9.1</version>\n\t\t\t<executions>\n\t\t\t\t<execution>\n\t\t\t\t\t<id>attach-javadocs</id>\n\t\t\t\t\t<goals>\n\t\t\t\t\t\t<goal>jar</goal>\n\t\t\t\t\t</goals>\n\t\t\t\t</execution>\n\t\t\t</executions>\n\t\t</plugin>\n    </plugins>\n</build>\n\n\n<repositories>\n\t")).add(mark("repository", "release").multiple("\n")).add(literal("\n\t<repository>\n        <id>maven2-repository.dev.java.net</id>\n        <name>Java.net Repository for Maven</name>\n        <url>http://download.java.net/maven/2/</url>\n        <layout>default</layout>\n    </repository>\n</repositories>\n\n<distributionManagement>\n\t")).add(mark("repository", "distribution").multiple("\n")).add(literal("\n</distributionManagement>\n\n<dependencies>\n    <dependency>\n        <groupId>io.intino.pandora</groupId>\n        <artifactId>pandora-rest-accessor-java</artifactId>\n        <version>[1.0.0, 2.0.0)</version>\n\t</dependency>\n\t<dependency>\n\t\t<groupId>io.intino.pandora</groupId>\n\t\t<artifactId>pandora-jmx</artifactId>\n\t\t<version>[1.0.0, 2.0.0)</version>\n\t</dependency>\n\t<dependency>\n\t\t<groupId>io.intino.pandora</groupId>\n\t\t<artifactId>pandora-jms</artifactId>\n\t\t<version>[1.0.0, 2.0.0)</version>\n\t</dependency>\n</dependencies>\n</project>")),
			rule().add((condition("type", "repository")), (condition("type", "Distribution")), (condition("trigger", "distribution"))).add(literal("<repository>\n\t<id>")).add(mark("name")).add(literal("</id>\n\t<name>")).add(mark("name")).add(literal("</name>\n\t<url>")).add(mark("url")).add(literal("</url>\n</repository>")),
			rule().add((condition("trigger", "distribution"))),
			rule().add((condition("type", "repository")), not(condition("type", "distribution")), (condition("trigger", "release"))).add(literal("<repository>\n\t<id>")).add(mark("name")).add(literal("</id>\n\t<url>")).add(mark("url")).add(literal("</url>\n</repository>")),
			rule().add((condition("type", "GPL")), (condition("trigger", "license"))).add(literal("<license>\n\t<name>The GNU General Public License v3.0</name>\n\t<url>https://www.gnu.org/licenses/gpl-3.0.txt</url>\n</license>")),
			rule().add((condition("type", "BSD")), (condition("trigger", "license"))).add(literal("<license>\n\t<name>BSD 3-Clause License</name>\n\t<url>https://opensource.org/licenses/BSD-3-Clause</url>\n</license>"))
		);
		return this;
	}
}
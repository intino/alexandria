package io.intino.pandora.plugin.actions;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.CRLF;

public class PomTemplate extends Template {

	protected PomTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new PomTemplate(Locale.ENGLISH, CRLF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "pom"))).add(literal("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n<modelVersion>4.0.0</modelVersion>\n\n<groupId>")).add(mark("group", "lowercase")).add(literal("</groupId>\n<artifactId>")).add(mark("artifact", "lowercase")).add(literal("</artifactId>\n<version>")).add(mark("version")).add(literal("</version>\n\n<properties>\n\t<maven.compiler.source>1.8</maven.compiler.source>\n\t<maven.compiler.target>1.8</maven.compiler.target>\n</properties>\n\n<build>\n    <sourceDirectory>src</sourceDirectory>\n\t<outputDirectory>out/production/")).add(mark("artifact", "lowercase")).add(literal("</outputDirectory>\n\t<testOutputDirectory>out/test/")).add(mark("artifact", "lowercase")).add(literal("</testOutputDirectory>\n\t<directory>out/build/")).add(mark("artifact", "lowercase")).add(literal("</directory>\n\t<plugins>\n        <plugin>\n            <groupId>org.apache.maven.plugins</groupId>\n            <artifactId>maven-source-plugin</artifactId>\n            <executions>\n                <execution>\n                    <id>attach-sources</id>\n                    <goals>\n                        <goal>jar</goal>\n                    </goals>\n                </execution>\n            </executions>\n        </plugin>\n    </plugins>\n</build>\n\n<repositories>\n\t<repository>\n\t\t<id>siani-maven</id>\n\t\t<name>siani-maven-releases</name>\n\t\t<url>http://artifactory.siani.es/artifactory/libs-release</url>\n\t</repository>\n\t<repository>\n\t\t<id>siani-maven-snapshot</id>\n\t\t<name>siani-maven-snapshot</name>\n\t\t<url>http://artifactory.siani.es/artifactory/libs-snapshot</url>\n\t</repository>\n</repositories>\n\n<distributionManagement>\n\t<repository>\n\t\t<id>siani-maven</id>\n\t\t<name>siani-maven-releases</name>\n\t\t<url>https://artifactory.siani.es/artifactory/libs-release-local</url>\n\t</repository>\n</distributionManagement>\n\n<dependencies>\n    <dependency>\n        <groupId>org.siani.pandora</groupId>\n        <artifactId>accessor-java</artifactId>\n        <version>[1.0.0, 2.0.0)</version>\n\t</dependency>\n</dependencies>\n</project>"))
		);
		return this;
	}
}
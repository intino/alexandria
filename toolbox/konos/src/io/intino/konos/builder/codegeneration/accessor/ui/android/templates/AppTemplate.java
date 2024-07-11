package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.outputs.Outputs.*;

public class AppTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("manifest")).output(literal("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\">\n\n    <uses-permission android:name=\"android.permission.INTERNET\" />\n    <uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\" />\n    <uses-permission android:name=\"android.permission.READ_EXTERNAL_STORAGE\" />\n    <uses-permission android:name=\"com.google.android.providers.gsf.permission.READ_GSERVICES\"/>\n\n    <application\n        android:label=\"@string/app_name\"\n        android:allowBackup=\"false\"\n        android:supportsRtl=\"true\"\n        android:theme=\"@style/AppTheme\"\n        android:usesCleartextTraffic=\"true\"\n        android:requestLegacyExternalStorage=\"true\">\n\n        ")).output(placeholder("resource").multiple("\n")).output(literal("\n\n        <provider\n            android:name=\"androidx.core.content.FileProvider\"\n            android:authorities=\"${applicationId}.provider\"\n            android:exported=\"false\"\n            android:grantUriPermissions=\"true\">\n            <meta-data\n                android:name=\"android.support.FILE_PROVIDER_PATHS\"\n                android:resource=\"@xml/provider_paths\" />\n        </provider>\n    </application>\n\n</manifest>")));
		rules.add(rule().condition(allTypes("resource", "main")).output(literal("<activity\n    android:name=\".pages.")).output(placeholder("name", "PascalCase")).output(literal("Activity\"\n    android:exported=\"true\"\n    android:windowSoftInputMode=\"adjustNothing\">\n    <intent-filter>\n        <action android:name=\"android.intent.action.MAIN\" />\n        <category android:name=\"android.intent.category.LAUNCHER\" />\n    </intent-filter>\n</activity>")));
		rules.add(rule().condition(allTypes("resource")).output(literal("<activity\n    android:name=\".pages.")).output(placeholder("name", "PascalCase")).output(literal("Activity\"\n    android:exported=\"true\"\n    android:windowSoftInputMode=\"adjustNothing\">\n</activity>")));
		rules.add(rule().condition(allTypes("gradle", "settings")).output(literal("enableFeaturePreview(\"TYPESAFE_PROJECT_ACCESSORS\")\n\npluginManagement {\n    repositories {\n        google()\n        gradlePluginPortal()\n        mavenCentral()\n    }\n}\n\ndependencyResolutionManagement {\n    repositories {\n        google()\n        mavenCentral()\n    }\n}\n\nrootProject.name = \"")).output(placeholder("project")).output(literal("\"\ninclude(\":android\")\ninclude(\":android-library\")\ninclude(\":shared\")")));
		rules.add(rule().condition(allTypes("gradle", "android")).output(literal("plugins {\n    alias(libs.plugins.androidApplication)\n    alias(libs.plugins.kotlinAndroid)\n}\n\nandroid {\n    namespace = \"")).output(placeholder("package")).output(literal(".mobile.android\"\n    compileSdk = 34\n    defaultConfig {\n        applicationId = \"")).output(placeholder("package")).output(literal(".mobile.android\"\n        minSdk = 32\n        targetSdk = 34\n        versionCode = 1\n        versionName = \"")).output(placeholder("version")).output(literal("\"\n        setProperty(\"archivesBaseName\", \"")).output(placeholder("project")).output(literal("-")).output(placeholder("version")).output(literal("\")\n    }\n}\n\ndependencies {\n    implementation(projects.androidLibrary)\n    implementation(projects.shared)\n}")));
		rules.add(rule().condition(allTypes("gradle", "androidLibrary")).output(literal("plugins {\n    alias(libs.plugins.androidLibrary)\n    alias(libs.plugins.kotlinAndroid)\n    alias(libs.plugins.compose.compiler)\n}\n\nandroid {\n    namespace = \"")).output(placeholder("package")).output(literal(".mobile.android\"\n    compileSdk = 34\n    defaultConfig {\n        minSdk = 32\n        version = \"")).output(placeholder("version")).output(literal("\"\n        setProperty(\"archivesBaseName\", \"")).output(placeholder("project")).output(literal("-")).output(placeholder("version")).output(literal("\")\n    }\n    buildFeatures {\n        compose = true\n    }\n    packaging {\n        resources {\n            excludes += \"/META-INF/{AL2.0,LGPL2.1}\"\n        }\n    }\n    buildTypes {\n        getByName(\"release\") {\n            isMinifyEnabled = false\n        }\n    }\n    compileOptions {\n        sourceCompatibility = JavaVersion.VERSION_18\n        targetCompatibility = JavaVersion.VERSION_18\n    }\n    kotlinOptions {\n        jvmTarget = JavaVersion.VERSION_18.getMajorVersion()\n    }\n}\n\ndependencies {\n    implementation(projects.shared)\n    implementation(libs.android.material)\n    implementation(libs.android.flexbox)\n    implementation(libs.kotlinx.datetime)\n    implementation(libs.androidx.activity.compose)\n    implementation(libs.androidx.databinding)\n    implementation(libs.caverock)\n    implementation(libs.http)\n    implementation(libs.android.maps)\n    implementation(libs.material.icons)\n    implementation(files(\"./libs/alexandria-5.1.25-release.aar\"))\n    implementation(files(\"./libs/alexandria-shared-5.1.25-release.aar\"))\n}")));
		rules.add(rule().condition(allTypes("gradle", "shared")).output(literal("plugins {\n    alias(libs.plugins.kotlinMultiplatform)\n    alias(libs.plugins.androidLibrary)\n    alias(libs.plugins.kotlinSerialization)\n}\n\nkotlin {\n    android()\n\n    listOf(\n        iosX64(),\n        iosArm64(),\n        iosSimulatorArm64()\n    ).forEach {\n        it.binaries.framework {\n            baseName = \"shared\"\n            isStatic = true\n        }\n    }\n\n    sourceSets {\n        commonMain.dependencies {\n            implementation(libs.kotlinx.serialization.json)\n            implementation(libs.kotlinx.coroutines.core)\n            implementation(libs.kotlin.stdlib)\n            implementation(libs.kotlinx.datetime)\n        }\n        androidMain.dependencies {\n            implementation(libs.android.material)\n            implementation(libs.android.flexbox)\n            implementation(libs.http)\n            implementation(libs.volley)\n        }\n    }\n}\n\nandroid {\n    namespace = \"")).output(placeholder("package")).output(literal(".mobile\"\n    compileSdk = 34\n    defaultConfig {\n        minSdk = 24\n        version = \"")).output(placeholder("version")).output(literal("\"\n        setProperty(\"archivesBaseName\", \"")).output(placeholder("project")).output(literal("-shared-")).output(placeholder("version")).output(literal("\")\n    }\n    compileOptions {\n        sourceCompatibility = JavaVersion.VERSION_18\n        targetCompatibility = JavaVersion.VERSION_18\n    }\n}")));
		rules.add(rule().condition(allTypes("properties")).output(literal("<resources>\n    ")).output(placeholder("component").multiple("\n")).output(literal("\n</resources>")));
		rules.add(rule().condition(allTypes("strings")).output(literal("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n    <string name=\"app_name\">")).output(placeholder("project")).output(literal("</string>\n</resources>")));
		rules.add(rule().condition(allTypes("component")).output(literal("<declare-styleable name=\"")).output(placeholder("name", "firstUpperCase")).output(literal("\"")).output(expression().output(literal(" parent=\"")).output(placeholder("parent", "firstUpperCase")).output(literal("\""))).output(literal(">\n    ")).output(placeholder("attribute").multiple("\n")).output(literal("\n</declare-styleable>")));
		rules.add(rule().condition(allTypes("attribute", "root")).output(literal("<attr name=\"")).output(placeholder("name", "lowerCase")).output(literal("\" format=\"string\" />")));
		rules.add(rule().condition(allTypes("attribute")).output(literal("<attr name=\"")).output(placeholder("component", "camelCaseToSnakeCase", "lowerCase")).output(literal("_")).output(placeholder("name", "lowerCase")).output(literal("\" format=\"string\" />")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}
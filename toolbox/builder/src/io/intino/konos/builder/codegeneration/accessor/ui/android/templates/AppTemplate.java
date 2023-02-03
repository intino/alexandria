package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AppTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("manifest"))).output(literal("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\">\n\n    <application\n        android:allowBackup=\"false\"\n        android:supportsRtl=\"true\"\n        android:theme=\"@style/AppTheme\"\n        android:usesCleartextTraffic=\"true\">\n        <activity\n            android:name=\".pages.")).output(mark("main", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Activity\"\n            android:exported=\"true\">\n            <intent-filter>\n                <action android:name=\"android.intent.action.MAIN\" />\n                <category android:name=\"android.intent.category.LAUNCHER\" />\n            </intent-filter>\n        </activity>\n    </application>\n\n    <uses-permission android:name=\"android.permission.INTERNET\" />\n\n</manifest>")),
			rule().condition((allTypes("gradle","settings"))).output(literal("pluginManagement {\n    repositories {\n        google()\n        gradlePluginPortal()\n        mavenCentral()\n    }\n}\n\ndependencyResolutionManagement {\n    repositories {\n        google()\n        mavenCentral()\n    }\n}\n\nrootProject.name = \"")).output(mark("project")).output(literal("\"\ninclude(\":android\")\ninclude(\":shared\")")),
			rule().condition((allTypes("gradle","android"))).output(literal("plugins {\n    id(\"com.android.application\")\n    kotlin(\"android\")\n}\n\nandroid {\n    namespace = \"")).output(mark("package")).output(literal(".mobile.android\"\n    compileSdk = 33\n    defaultConfig {\n        applicationId = \"")).output(mark("package")).output(literal(".mobile.android\"\n        minSdk = 24\n        targetSdk = 33\n        versionCode = 1\n        versionName = \"1.0\"\n    }\n    buildFeatures {\n        compose = true\n    }\n    composeOptions {\n        kotlinCompilerExtensionVersion = \"1.3.0\"\n    }\n    packagingOptions {\n        resources {\n            excludes += \"/META-INF/{AL2.0,LGPL2.1}\"\n        }\n    }\n    buildTypes {\n        getByName(\"release\") {\n            isMinifyEnabled = false\n        }\n    }\n}\n\ndependencies {\n    implementation(project(\":shared\"))\n    implementation(\"androidx.compose.ui:ui:1.2.1\")\n    implementation(\"androidx.compose.ui:ui-tooling:1.2.1\")\n    implementation(\"androidx.compose.ui:ui-tooling-preview:1.2.1\")\n    implementation(\"androidx.compose.foundation:foundation:1.2.1\")\n    implementation(\"androidx.compose.material:material:1.2.1\")\n    implementation(\"androidx.activity:activity-compose:1.5.1\")\n    implementation(\"androidx.constraintlayout:constraintlayout:2.1.4\")\n    implementation(\"com.google.android.material:material:1.7.0\")\n    implementation(\"org.jetbrains.kotlinx:kotlinx-datetime:0.4.0\")\n}")),
			rule().condition((allTypes("gradle","shared"))).output(literal("plugins {\n    kotlin(\"multiplatform\")\n    id(\"org.jetbrains.kotlin.plugin.serialization\")\n    id(\"com.android.library\")\n}\n\nkotlin {\n    android()\n\n    listOf(\n        iosX64(),\n        iosArm64(),\n        iosSimulatorArm64()\n    ).forEach {\n        it.binaries.framework {\n            baseName = \"shared\"\n        }\n    }\n\n    sourceSets {\n        val commonMain by getting {\n            dependencies {\n                implementation(\"org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1\")\n                implementation(\"org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4\")\n                implementation(\"org.jetbrains.kotlin:kotlin-stdlib:1.7.21\")\n                implementation(\"org.jetbrains.kotlinx:kotlinx-datetime:0.4.0\")\n            }\n        }\n        val commonTest by getting {\n            dependencies {\n                implementation(kotlin(\"test\"))\n            }\n        }\n        val androidMain by getting {\n            dependencies {\n                implementation(\"com.google.android.material:material:1.7.0\")\n                implementation(\"com.squareup.okhttp3:okhttp:4.9.0\")\n                implementation(\"com.android.volley:volley:1.2.1\")\n            }\n        }\n        val androidTest by getting\n        val iosX64Main by getting\n        val iosArm64Main by getting\n        val iosSimulatorArm64Main by getting\n        val iosMain by creating {\n            dependsOn(commonMain)\n            iosX64Main.dependsOn(this)\n            iosArm64Main.dependsOn(this)\n            iosSimulatorArm64Main.dependsOn(this)\n        }\n        val iosX64Test by getting\n        val iosArm64Test by getting\n        val iosSimulatorArm64Test by getting\n        val iosTest by creating {\n            dependsOn(commonTest)\n            iosX64Test.dependsOn(this)\n            iosArm64Test.dependsOn(this)\n            iosSimulatorArm64Test.dependsOn(this)\n        }\n    }\n}\n\nandroid {\n    namespace = \"")).output(mark("package")).output(literal(".mobile\"\n    compileSdk = 33\n    defaultConfig {\n        minSdk = 24\n        targetSdk = 33\n    }\n}")),
			rule().condition((type("properties"))).output(literal("<resources>\n    ")).output(mark("component").multiple("\n")).output(literal("\n</resources>")),
			rule().condition((type("component"))).output(literal("<declare-styleable name=\"")).output(mark("name", "firstUpperCase")).output(literal("\"")).output(expression().output(literal(" parent=\"")).output(mark("parent", "firstUpperCase")).output(literal("\""))).output(literal(">\n    ")).output(mark("attribute").multiple("\n")).output(literal("\n</declare-styleable>")),
			rule().condition((allTypes("attribute","root"))).output(literal("<attr name=\"")).output(mark("name", "lowerCase")).output(literal("\" format=\"string\" />")),
			rule().condition((type("attribute"))).output(literal("<attr name=\"")).output(mark("component", "camelCaseToUnderscoreCase", "lowerCase")).output(literal("_")).output(mark("name", "lowerCase")).output(literal("\" format=\"string\" />"))
		);
	}
}
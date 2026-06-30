plugins {
	java
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
}

tasks.jar {
	manifest {
		attributes["Main-Class"] = "piggecutlet.Main"
	}
}

// 「エンコーディング windows-31j にマップできません」を防ぐ
tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.0-M1"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.0"
	kotlin("plugin.spring") version "1.6.0"
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	mainClass.set("monster.loli.lolidate.LolidateApplicationKt")
}
group = "monster.loli"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	maven { url = uri("https://maven.aliyun.com/repository/public/") }
	maven { url = uri("https://maven.aliyun.com/repository/spring") }
	maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
	maven { url = uri("https://maven.aliyun.com/repository/central") }
	maven { url = uri("https://maven.aliyun.com/repository/spring-plugin") }
	maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("junit:junit:4.13.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("com.squareup.okhttp3:okhttp:4.9.3")
	implementation ("com.google.code.gson:gson:2.9.0")
	// https://mvnrepository.com/artifact/org.apache.commons/commons-compress
	implementation("org.apache.commons:commons-compress:1.21")
	// https://mvnrepository.com/artifact/org.tukaani/xz
	implementation("org.tukaani:xz:1.9")
	compileOnly ("org.projectlombok:lombok:1.18.22")
	annotationProcessor ("org.projectlombok:lombok:1.18.22")

	testCompileOnly ("org.projectlombok:lombok:1.18.22")
	testAnnotationProcessor ("org.projectlombok:lombok:1.18.22")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

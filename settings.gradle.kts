pluginManagement {
	repositories {
		maven { url = uri("https://maven.aliyun.com/repository/public/") }
		maven { url = uri("https://maven.aliyun.com/repository/spring") }
		maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
		maven { url = uri("https://maven.aliyun.com/repository/central") }
		maven { url = uri("https://maven.aliyun.com/repository/spring-plugin") }
		maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
		maven { url = uri("https://repo.spring.io/milestone") }
		gradlePluginPortal()
	}
}
rootProject.name = "lolidate"

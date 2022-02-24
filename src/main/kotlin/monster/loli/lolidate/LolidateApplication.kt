package monster.loli.lolidate

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
@EnableAutoConfiguration
@SpringBootApplication
class LolidateApplication

fun main(args: Array<String>) {
	runApplication<LolidateApplication>(*args)
}

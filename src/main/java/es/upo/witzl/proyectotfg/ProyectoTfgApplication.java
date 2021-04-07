package es.upo.witzl.proyectotfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;

@ServletComponentScan
@SpringBootApplication
public class ProyectoTfgApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoTfgApplication.class, args);
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
}

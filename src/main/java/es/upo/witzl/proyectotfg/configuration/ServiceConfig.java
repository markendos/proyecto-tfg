package es.upo.witzl.proyectotfg.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"es.upo.witzl.proyectotfg.users.service"})
public class ServiceConfig {
}

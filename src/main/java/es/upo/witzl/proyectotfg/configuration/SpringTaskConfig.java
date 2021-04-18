package es.upo.witzl.proyectotfg.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({"es.upo.witzl.proyectotfg.users.task"})
public class SpringTaskConfig {

}

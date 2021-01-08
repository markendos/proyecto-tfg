package es.upo.witzl.proyectotfg.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({ "es.upo.witzl.proyectotfg.task" })
public class SpringTaskConfig {

}

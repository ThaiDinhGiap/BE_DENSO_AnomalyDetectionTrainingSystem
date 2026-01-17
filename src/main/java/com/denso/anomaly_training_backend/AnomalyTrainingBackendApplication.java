package com.denso.anomaly_training_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = { RabbitAutoConfiguration.class })
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class AnomalyTrainingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnomalyTrainingBackendApplication.class, args);
	}

}

package com.mercadolibre.projetofinal;

import com.mercadolibre.projetofinal.config.SpringConfig;
import com.mercadolibre.projetofinal.util.PopulateDatabase;
import com.mercadolibre.projetofinal.util.ScopeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

	@Autowired
	private PopulateDatabase populateDatabase;

	public static void main(String[] args) {
		ScopeUtils.calculateScopeSuffix();
		new SpringApplicationBuilder(SpringConfig.class).registerShutdownHook(true)
				.run(args);
	}

	@PostConstruct
	public void init(){
//		populateDatabase.populate();
	}
}

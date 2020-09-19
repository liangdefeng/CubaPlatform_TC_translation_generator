package com.github.liangdefeng.cuba.translation;

import com.github.liangdefeng.cuba.translation.service.TraditionalChineseConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TranslationApplication implements CommandLineRunner {

	private TraditionalChineseConvertService service;

	public TranslationApplication(TraditionalChineseConvertService service) {
		this.service = service;
	}

	@Override
	public void run(String... args) {
		service.process();
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TranslationApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
}

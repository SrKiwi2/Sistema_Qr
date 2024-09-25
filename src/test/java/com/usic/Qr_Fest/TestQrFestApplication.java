package com.usic.Qr_Fest;

import org.springframework.boot.SpringApplication;

import com.usic.qr_fest.QrFestApplication;

public class TestQrFestApplication {

	public static void main(String[] args) {
		SpringApplication.from(QrFestApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

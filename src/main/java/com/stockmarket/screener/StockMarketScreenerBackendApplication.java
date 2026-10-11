package com.stockmarket.screener;

import com.stockmarket.screener.excel.ExcelImportService;
import com.stockmarket.screener.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StockMarketScreenerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMarketScreenerBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner initialImportRunner(ExcelImportService excelImportService,
												 CompanyRepository companyRepository,
												 @Value("${app.import-on-startup:true}") boolean importOnStartup) {
		return args -> {
			if (importOnStartup && companyRepository.count() == 0) {
				System.out.println("Starting initial Excel dataset import from Data directory...");
				var result = excelImportService.importAllWorkbooks();
				System.out.println("Initial import result: " + result);
			}
		};
	}
}

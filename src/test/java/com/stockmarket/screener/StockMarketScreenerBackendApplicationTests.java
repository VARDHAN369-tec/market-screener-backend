package com.stockmarket.screener;

import com.stockmarket.screener.excel.PoiUtils;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StockMarketScreenerBackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void adminImportIsForbiddenWithoutAdminRole() throws Exception {
		mockMvc.perform(post("/api/admin/import"))
				.andExpect(status().isForbidden());
	}

	@Test
	void testExcelReading() throws Exception {
		File file = new File("../Data/Adani Enterp.xlsx");
		if (!file.exists()) {
			file = new File("Data/Adani Enterp.xlsx");
		}
		assertTrue(file.exists(), "Adani Enterp.xlsx should exist");

		try (FileInputStream fis = new FileInputStream(file);
			 Workbook wb = WorkbookFactory.create(fis)) {
			Sheet sheet = wb.getSheet("Profit & Loss");
			assertNotNull(sheet);
			Row dateRow = sheet.getRow(2); // row 3
			assertNotNull(dateRow);
			Cell b3 = dateRow.getCell(1);
			assertNotNull(b3);

			DataFormatter formatter = new DataFormatter();
			String formatted = formatter.formatCellValue(b3);
			System.out.println("B3 formatCellValue: " + formatted);
			System.out.println("B3 cellType: " + b3.getCellType());
			if (b3.getCellType() == CellType.FORMULA) {
				System.out.println("B3 cachedType: " + b3.getCachedFormulaResultType());
				System.out.println("B3 numericCellValue: " + b3.getNumericCellValue());
				System.out.println("DateUtil.getJavaDate: " + DateUtil.getJavaDate(b3.getNumericCellValue()));
				System.out.println("isCellDateFormatted: " + DateUtil.isCellDateFormatted(b3));
				LocalDate date = PoiUtils.getDateValue(b3);
				System.out.println("PoiUtils.getDateValue(b3): " + date);
				assertEquals(LocalDate.of(2017, 3, 31), date);
			}
		}
	}

	@org.springframework.beans.factory.annotation.Autowired
	private com.stockmarket.screener.excel.WorkbookParserService workbookParserService;

	@org.springframework.beans.factory.annotation.Autowired
	private com.stockmarket.screener.financial.FinancialAnalysisService financialAnalysisService;

	@org.springframework.beans.factory.annotation.Autowired
	private com.stockmarket.screener.repository.CompanyRepository companyRepository;

	@org.springframework.beans.factory.annotation.Autowired
	private com.stockmarket.screener.repository.FinancialMetricRepository financialMetricRepository;

	@org.springframework.beans.factory.annotation.Autowired
	private com.stockmarket.screener.repository.ProfitLossRepository profitLossRepository;

	@Test
	void testParseAndCalculateMetrics() throws Exception {
		File file = new File("../Data/Adani Enterp.xlsx");
		if (!file.exists()) {
			file = new File("Data/Adani Enterp.xlsx");
		}
		assertTrue(file.exists());

		var result = workbookParserService.processWorkbook(file);
		assertTrue(result.success, "Parsing should succeed: " + result.errorMessage);
		System.out.println("Parsed company: " + result.companyName);
		System.out.println("Records processed: " + result.recordsProcessed);
		assertTrue(result.recordsProcessed > 0, "Should have processed financial records");

		var company = companyRepository.findByCompanyNameIgnoreCase(result.companyName).orElseThrow();
		financialAnalysisService.calculateAndPersistMetrics(company);

		var metrics = financialMetricRepository.findByCompany_CompanyIdOrderByPeriod_PeriodDateAsc(company.getCompanyId());
		assertFalse(metrics.isEmpty(), "Metrics should have been saved for company");
		System.out.println("Saved metrics count: " + metrics.size());

		var pls = profitLossRepository.findByCompanyOrderByPeriod_PeriodDateAsc(company);
		assertFalse(pls.isEmpty(), "ProfitLoss records should exist");
		System.out.println("Saved P&L records: " + pls.size());
	}

	@org.springframework.beans.factory.annotation.Autowired
	private com.stockmarket.screener.excel.ExcelImportService excelImportService;

	@Test
	void testBulkImportAllWorkbooks() {
		var summary = excelImportService.importAllWorkbooks();
		System.out.println("Bulk import summary: " + summary);
		assertTrue((Boolean) summary.get("success"));
		System.out.println("Success count: " + summary.get("successCount"));
		System.out.println("Failure count: " + summary.get("failureCount"));
		System.out.println("Total records: " + summary.get("totalRecords"));
		assertTrue((Integer) summary.get("successCount") > 0);
	}
}

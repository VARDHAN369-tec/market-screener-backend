package com.stockmarket.screener.excel;

import com.stockmarket.screener.entity.Company;
import com.stockmarket.screener.entity.MarketData;
import com.stockmarket.screener.repository.CompanyRepository;
import com.stockmarket.screener.repository.MarketDataRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;

@Service
public class WorkbookParserService {

    private static final Logger log = LoggerFactory.getLogger(WorkbookParserService.class);

    private final CompanyRepository companyRepository;
    private final MarketDataRepository marketDataRepository;
    private final DataSheetParser dataSheetParser;
    private final ProfitLossParser profitLossParser;
    private final QuarterParser quarterParser;
    private final BalanceSheetParser balanceSheetParser;
    private final CashFlowParser cashFlowParser;
    private final WorksheetDataParser worksheetDataParser;

    public WorkbookParserService(CompanyRepository companyRepository,
                                 MarketDataRepository marketDataRepository,
                                 DataSheetParser dataSheetParser,
                                 ProfitLossParser profitLossParser,
                                 QuarterParser quarterParser,
                                 BalanceSheetParser balanceSheetParser,
                                 CashFlowParser cashFlowParser,
                                 WorksheetDataParser worksheetDataParser) {
        this.companyRepository = companyRepository;
        this.marketDataRepository = marketDataRepository;
        this.dataSheetParser = dataSheetParser;
        this.profitLossParser = profitLossParser;
        this.quarterParser = quarterParser;
        this.balanceSheetParser = balanceSheetParser;
        this.cashFlowParser = cashFlowParser;
        this.worksheetDataParser = worksheetDataParser;
    }

    public static class ParseResult {
        public boolean success;
        public String companyName;
        public int recordsProcessed;
        public int recordsInserted;
        public int recordsUpdated;
        public String errorMessage;
    }

    @Transactional
    public ParseResult processWorkbook(File file) {
        ParseResult result = new ParseResult();
        if (!file.exists() || !file.canRead()) {
            result.success = false;
            result.errorMessage = "File does not exist or cannot be read: " + file.getName();
            return result;
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            // Validate required sheets
            Sheet plSheet = workbook.getSheet("Profit & Loss");
            Sheet qSheet = workbook.getSheet("Quarters");
            Sheet bsSheet = workbook.getSheet("Balance Sheet");
            Sheet cfSheet = workbook.getSheet("Cash Flow");
            Sheet dataSheet = workbook.getSheet("Data Sheet");
            if (dataSheet == null) {
                dataSheet = workbook.getSheet("Data");
            }

            if (plSheet == null || qSheet == null || bsSheet == null || cfSheet == null || dataSheet == null) {
                StringBuilder missing = new StringBuilder();
                if (plSheet == null) missing.append("Profit & Loss; ");
                if (qSheet == null) missing.append("Quarters; ");
                if (bsSheet == null) missing.append("Balance Sheet; ");
                if (cfSheet == null) missing.append("Cash Flow; ");
                if (dataSheet == null) missing.append("Data Sheet; ");
                result.success = false;
                result.errorMessage = "Missing required worksheets: " + missing.toString().trim();
                return result;
            }

            // Parse metadata from Data Sheet
            DataSheetParser.ParsedCompanyMeta meta = dataSheetParser.parseMetadata(dataSheet);
            String name = (meta != null && meta.companyName != null && !meta.companyName.trim().isEmpty())
                    ? meta.companyName.trim()
                    : file.getName().replace(".xlsx", "").replace(".xls", "").trim();

            result.companyName = name;

            String sector = (meta != null && meta.sector != null) ? meta.sector.trim() : null;
            String industry = (meta != null && meta.industry != null) ? meta.industry.trim() : null;

            // Find or create Company
            Company company = companyRepository.findByCompanyNameIgnoreCase(name)
                    .orElseGet(() -> new Company(name, sector, industry));

            if (company.getCompanyId() != null) {
                if (sector != null) company.setSector(sector);
                if (industry != null) company.setIndustry(industry);
                company.setUpdatedAt(LocalDateTime.now());
                result.recordsUpdated++;
            } else {
                result.recordsInserted++;
            }
            company = companyRepository.save(company);

            // Save Market Data
            if (meta != null) {
                Company finalCompany = company;
                MarketData md = marketDataRepository.findByCompany(company)
                        .orElseGet(() -> {
                            MarketData newMd = new MarketData();
                            newMd.setCompany(finalCompany);
                            return newMd;
                        });
                md.setCurrentPrice(meta.currentPrice);
                md.setMarketCapitalization(meta.marketCap);
                md.setNumberOfShares(meta.numberOfShares);
                md.setFaceValue(meta.faceValue);
                md.setUpdatedAt(LocalDateTime.now());
                marketDataRepository.save(md);
            }

            // Parse Financial Statements
            int plCount = profitLossParser.parseProfitLoss(plSheet, company).size();
            int qCount = quarterParser.parseQuarters(qSheet, company).size();
            int bsCount = balanceSheetParser.parseBalanceSheet(bsSheet, company).size();
            int cfCount = cashFlowParser.parseCashFlow(cfSheet, company).size();

            // Parse Worksheet Data for the 5-tab viewer
            worksheetDataParser.parseAndSaveSheetData(plSheet, "Profit & Loss", company);
            worksheetDataParser.parseAndSaveSheetData(qSheet, "Quarters", company);
            worksheetDataParser.parseAndSaveSheetData(bsSheet, "Balance Sheet", company);
            worksheetDataParser.parseAndSaveSheetData(cfSheet, "Cash Flow", company);
            worksheetDataParser.parseAndSaveSheetData(dataSheet, "Data Sheet", company);

            result.recordsProcessed = plCount + qCount + bsCount + cfCount;
            result.recordsInserted += result.recordsProcessed;
            result.success = true;
            return result;

        } catch (Exception e) {
            log.error("Failed to parse workbook: {}", file.getName(), e);
            result.success = false;
            result.errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            return result;
        }
    }
}

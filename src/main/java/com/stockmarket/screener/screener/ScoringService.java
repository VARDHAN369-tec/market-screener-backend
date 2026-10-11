package com.stockmarket.screener.screener;

import com.stockmarket.screener.entity.FinancialMetric;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ScoringService {

    public static class ScoreResult {
        public BigDecimal totalScore;
        public Map<String, Object> keyMetrics = new LinkedHashMap<>();
        public Map<String, Object> breakdown = new LinkedHashMap<>();
    }

    public ScoreResult calculateProfitabilityScore(FinancialMetric m) {
        ScoreResult result = new ScoreResult();
        if (m == null) {
            result.totalScore = BigDecimal.ZERO;
            return result;
        }

        BigDecimal roe = m.getRoe();
        BigDecimal roce = m.getRoce();
        BigDecimal opm = m.getOperatingProfitMargin();
        BigDecimal npm = m.getNetProfitMargin();
        BigDecimal tax = m.getEffectiveTaxRate();

        result.keyMetrics.put("roe", roe != null ? roe + "%" : "N/A");
        result.keyMetrics.put("roce", roce != null ? roce + "%" : "N/A");
        result.keyMetrics.put("operatingMargin", opm != null ? opm + "%" : "N/A");
        result.keyMetrics.put("netMargin", npm != null ? npm + "%" : "N/A");
        result.keyMetrics.put("taxRate", tax != null ? tax + "%" : "N/A");

        // ROE max 25
        double roePts = 0;
        if (roe != null) {
            double v = roe.doubleValue();
            if (v >= 25) roePts = 25;
            else if (v >= 20) roePts = 22;
            else if (v >= 15) roePts = 18;
            else if (v >= 10) roePts = 14;
            else if (v >= 5) roePts = 10;
            else if (v > 0) roePts = 5;
        }
        result.breakdown.put("ROE", roePts + " / 25");

        // ROCE max 25
        double rocePts = 0;
        if (roce != null) {
            double v = roce.doubleValue();
            if (v >= 25) rocePts = 25;
            else if (v >= 20) rocePts = 22;
            else if (v >= 15) rocePts = 18;
            else if (v >= 10) rocePts = 14;
            else if (v >= 5) rocePts = 10;
            else if (v > 0) rocePts = 5;
        }
        result.breakdown.put("ROCE", rocePts + " / 25");

        // OPM max 20
        double opmPts = 0;
        if (opm != null) {
            double v = opm.doubleValue();
            if (v >= 25) opmPts = 20;
            else if (v >= 20) opmPts = 17;
            else if (v >= 15) opmPts = 14;
            else if (v >= 10) opmPts = 10;
            else if (v >= 5) opmPts = 6;
            else if (v > 0) opmPts = 3;
        }
        result.breakdown.put("Operating Margin", opmPts + " / 20");

        // NPM max 20
        double npmPts = 0;
        if (npm != null) {
            double v = npm.doubleValue();
            if (v >= 20) npmPts = 20;
            else if (v >= 15) npmPts = 17;
            else if (v >= 10) npmPts = 14;
            else if (v >= 5) npmPts = 10;
            else if (v > 0) npmPts = 5;
        }
        result.breakdown.put("Net Margin", npmPts + " / 20");

        // Tax Efficiency max 10
        double taxPts = 5;
        if (tax != null) {
            double v = tax.doubleValue();
            if (v >= 20 && v <= 30) taxPts = 10;
            else if (v >= 15 && v <= 35) taxPts = 8;
            else taxPts = 5;
        }
        result.breakdown.put("Tax Efficiency", taxPts + " / 10");

        double total = roePts + rocePts + opmPts + npmPts + taxPts;
        result.totalScore = BigDecimal.valueOf(total).setScale(1, RoundingMode.HALF_UP);
        result.breakdown.put("Total", result.totalScore + " / 100");
        return result;
    }

    public ScoreResult calculateGrowthScore(FinancialMetric m) {
        ScoreResult result = new ScoreResult();
        if (m == null) {
            result.totalScore = BigDecimal.ZERO;
            return result;
        }

        BigDecimal sg = m.getSalesGrowth();
        BigDecimal opg = m.getOperatingProfitGrowth();
        BigDecimal npg = m.getNetProfitGrowth();

        result.keyMetrics.put("salesGrowth", sg != null ? sg + "%" : "N/A");
        result.keyMetrics.put("operatingProfitGrowth", opg != null ? opg + "%" : "N/A");
        result.keyMetrics.put("netProfitGrowth", npg != null ? npg + "%" : "N/A");

        // Sales Growth max 35
        double sgPts = 0;
        if (sg != null) {
            double v = sg.doubleValue();
            if (v >= 25) sgPts = 35;
            else if (v >= 20) sgPts = 30;
            else if (v >= 15) sgPts = 25;
            else if (v >= 10) sgPts = 20;
            else if (v >= 5) sgPts = 15;
            else if (v > 0) sgPts = 10;
        }
        result.breakdown.put("Sales Growth", sgPts + " / 35");

        // Operating Profit Growth max 35
        double opgPts = 0;
        if (opg != null) {
            double v = opg.doubleValue();
            if (v >= 25) opgPts = 35;
            else if (v >= 20) opgPts = 30;
            else if (v >= 15) opgPts = 25;
            else if (v >= 10) opgPts = 20;
            else if (v >= 5) opgPts = 15;
            else if (v > 0) opgPts = 10;
        }
        result.breakdown.put("Operating Profit Growth", opgPts + " / 35");

        // Net Profit Growth max 30
        double npgPts = 0;
        if (npg != null) {
            double v = npg.doubleValue();
            if (v >= 25) npgPts = 30;
            else if (v >= 20) npgPts = 25;
            else if (v >= 15) npgPts = 20;
            else if (v >= 10) npgPts = 15;
            else if (v >= 5) npgPts = 10;
            else if (v > 0) npgPts = 5;
        }
        result.breakdown.put("Net Profit Growth", npgPts + " / 30");

        double total = sgPts + opgPts + npgPts;
        result.totalScore = BigDecimal.valueOf(total).setScale(1, RoundingMode.HALF_UP);
        result.breakdown.put("Total", result.totalScore + " / 100");
        return result;
    }

    public ScoreResult calculateFinancialStrengthScore(FinancialMetric m) {
        ScoreResult result = new ScoreResult();
        if (m == null) {
            result.totalScore = BigDecimal.ZERO;
            return result;
        }

        BigDecimal de = m.getDebtToEquity();
        BigDecimal ic = m.getInterestCoverage();
        BigDecimal cr = m.getCurrentRatio();

        result.keyMetrics.put("debtToEquity", de != null ? de.toString() : "N/A");
        result.keyMetrics.put("interestCoverage", ic != null ? ic.toString() : "N/A");
        result.keyMetrics.put("currentRatio", cr != null ? cr.toString() : "N/A");

        // Debt to Equity max 40 (Lower is better)
        double dePts = 0;
        if (de != null) {
            double v = de.doubleValue();
            if (v <= 0.1) dePts = 40;
            else if (v <= 0.5) dePts = 35;
            else if (v <= 1.0) dePts = 28;
            else if (v <= 1.5) dePts = 20;
            else if (v <= 2.0) dePts = 12;
            else dePts = 5;
        }
        result.breakdown.put("Debt-to-Equity", dePts + " / 40");

        // Interest Coverage max 35 (Higher is better)
        double icPts = 0;
        if (ic != null) {
            double v = ic.doubleValue();
            if (v >= 10) icPts = 35;
            else if (v >= 6) icPts = 30;
            else if (v >= 4) icPts = 25;
            else if (v >= 2) icPts = 18;
            else if (v >= 1) icPts = 10;
        }
        result.breakdown.put("Interest Coverage", icPts + " / 35");

        // Current Ratio max 25
        double crPts = 0;
        if (cr != null) {
            double v = cr.doubleValue();
            if (v >= 2.0) crPts = 25;
            else if (v >= 1.5) crPts = 22;
            else if (v >= 1.2) crPts = 18;
            else if (v >= 1.0) crPts = 14;
            else if (v >= 0.8) crPts = 8;
        }
        result.breakdown.put("Current Ratio", crPts + " / 25");

        double total = dePts + icPts + crPts;
        result.totalScore = BigDecimal.valueOf(total).setScale(1, RoundingMode.HALF_UP);
        result.breakdown.put("Total", result.totalScore + " / 100");
        return result;
    }

    public ScoreResult calculateCashFlowScore(FinancialMetric m) {
        ScoreResult result = new ScoreResult();
        if (m == null) {
            result.totalScore = BigDecimal.ZERO;
            return result;
        }

        BigDecimal ocf = m.getOcfEfficiency();
        BigDecimal fcf = m.getFreeCashFlow();

        result.keyMetrics.put("ocfEfficiency", ocf != null ? ocf.toString() : "N/A");
        result.keyMetrics.put("freeCashFlow", fcf != null ? "₹" + fcf + " Cr" : "N/A");

        // OCF Efficiency max 50
        double ocfPts = 0;
        if (ocf != null) {
            double v = ocf.doubleValue();
            if (v >= 1.2) ocfPts = 50;
            else if (v >= 1.0) ocfPts = 45;
            else if (v >= 0.8) ocfPts = 38;
            else if (v >= 0.5) ocfPts = 28;
            else if (v > 0) ocfPts = 15;
        }
        result.breakdown.put("Operating Cash Flow Efficiency", ocfPts + " / 50");

        // FCF max 50
        double fcfPts = 10;
        if (fcf != null) {
            double v = fcf.doubleValue();
            if (v > 1000) fcfPts = 50;
            else if (v > 200) fcfPts = 42;
            else if (v > 0) fcfPts = 35;
            else fcfPts = 10;
        }
        result.breakdown.put("Free Cash Flow", fcfPts + " / 50");

        double total = ocfPts + fcfPts;
        result.totalScore = BigDecimal.valueOf(total).setScale(1, RoundingMode.HALF_UP);
        result.breakdown.put("Total", result.totalScore + " / 100");
        return result;
    }

    public ScoreResult calculateValuationScore(FinancialMetric m) {
        ScoreResult result = new ScoreResult();
        if (m == null) {
            result.totalScore = BigDecimal.ZERO;
            return result;
        }

        BigDecimal pe = m.getPeRatio();
        BigDecimal pb = m.getPbRatio();
        BigDecimal dpr = m.getDividendPayoutRatio();

        result.keyMetrics.put("peRatio", pe != null ? pe.toString() : "N/A");
        result.keyMetrics.put("pbRatio", pb != null ? pb.toString() : "N/A");
        result.keyMetrics.put("dividendPayoutRatio", dpr != null ? dpr + "%" : "N/A");

        // PE max 40
        double pePts = 5;
        if (pe != null) {
            double v = pe.doubleValue();
            if (v > 0 && v <= 15) pePts = 40;
            else if (v <= 25) pePts = 32;
            else if (v <= 40) pePts = 24;
            else if (v <= 60) pePts = 15;
            else pePts = 5;
        }
        result.breakdown.put("P/E Ratio", pePts + " / 40");

        // PB max 35
        double pbPts = 5;
        if (pb != null) {
            double v = pb.doubleValue();
            if (v > 0 && v <= 2.0) pbPts = 35;
            else if (v <= 4.0) pbPts = 28;
            else if (v <= 7.0) pbPts = 20;
            else if (v <= 10.0) pbPts = 12;
            else pbPts = 5;
        }
        result.breakdown.put("P/B Ratio", pbPts + " / 35");

        // Dividend Payout max 25
        double dprPts = 5;
        if (dpr != null) {
            double v = dpr.doubleValue();
            if (v >= 30) dprPts = 25;
            else if (v >= 20) dprPts = 20;
            else if (v >= 10) dprPts = 15;
            else if (v > 0) dprPts = 10;
        }
        result.breakdown.put("Dividend Payout Ratio", dprPts + " / 25");

        double total = pePts + pbPts + dprPts;
        result.totalScore = BigDecimal.valueOf(total).setScale(1, RoundingMode.HALF_UP);
        result.breakdown.put("Total", result.totalScore + " / 100");
        return result;
    }

    public ScoreResult calculateQuarterlyScore(FinancialMetric m) {
        ScoreResult result = new ScoreResult();
        if (m == null) {
            result.totalScore = BigDecimal.ZERO;
            return result;
        }

        BigDecimal qsg = m.getQoqSalesGrowth();
        BigDecimal qopg = m.getQoqOperatingProfitGrowth();
        BigDecimal qnpg = m.getQoqNetProfitGrowth();

        result.keyMetrics.put("qoqSalesGrowth", qsg != null ? qsg + "%" : "N/A");
        result.keyMetrics.put("qoqOperatingProfitGrowth", qopg != null ? qopg + "%" : "N/A");
        result.keyMetrics.put("qoqNetProfitGrowth", qnpg != null ? qnpg + "%" : "N/A");

        double qsgPts = 0;
        if (qsg != null) {
            double v = qsg.doubleValue();
            if (v >= 15) qsgPts = 35;
            else if (v >= 10) qsgPts = 30;
            else if (v >= 5) qsgPts = 22;
            else if (v > 0) qsgPts = 15;
            else qsgPts = 5;
        }
        result.breakdown.put("QoQ Sales Growth", qsgPts + " / 35");

        double qopgPts = 0;
        if (qopg != null) {
            double v = qopg.doubleValue();
            if (v >= 15) qopgPts = 35;
            else if (v >= 10) qopgPts = 30;
            else if (v >= 5) qopgPts = 22;
            else if (v > 0) qopgPts = 15;
            else qopgPts = 5;
        }
        result.breakdown.put("QoQ Operating Profit Growth", qopgPts + " / 35");

        double qnpgPts = 0;
        if (qnpg != null) {
            double v = qnpg.doubleValue();
            if (v >= 15) qnpgPts = 30;
            else if (v >= 10) qnpgPts = 25;
            else if (v >= 5) qnpgPts = 18;
            else if (v > 0) qnpgPts = 12;
            else qnpgPts = 5;
        }
        result.breakdown.put("QoQ Net Profit Growth", qnpgPts + " / 30");

        double total = qsgPts + qopgPts + qnpgPts;
        result.totalScore = BigDecimal.valueOf(total).setScale(1, RoundingMode.HALF_UP);
        result.breakdown.put("Total", result.totalScore + " / 100");
        return result;
    }
}

package com.stockmarket.screener.financial;

import com.stockmarket.screener.entity.QuarterlyResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class QuarterlyAnalysisService {

    /**
     * QoQ Sales Growth = (Sales[t] - Sales[t-1]) / Sales[t-1] * 100
     */
    public BigDecimal calculateQoqSalesGrowth(QuarterlyResult current, QuarterlyResult previous) {
        if (current == null || previous == null) return null;
        return FinancialCalculatorUtils.safeGrowth(current.getSales(), previous.getSales());
    }

    /**
     * QoQ Operating Profit Growth = (OpProfit[t] - OpProfit[t-1]) / OpProfit[t-1] * 100
     */
    public BigDecimal calculateQoqOperatingProfitGrowth(QuarterlyResult current, QuarterlyResult previous) {
        if (current == null || previous == null) return null;
        return FinancialCalculatorUtils.safeGrowth(current.getOperatingProfit(), previous.getOperatingProfit());
    }

    /**
     * QoQ Net Profit Growth = (NetProfit[t] - NetProfit[t-1]) / NetProfit[t-1] * 100
     */
    public BigDecimal calculateQoqNetProfitGrowth(QuarterlyResult current, QuarterlyResult previous) {
        if (current == null || previous == null) return null;
        return FinancialCalculatorUtils.safeGrowth(current.getNetProfit(), previous.getNetProfit());
    }
}

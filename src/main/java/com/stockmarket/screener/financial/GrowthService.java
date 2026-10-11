package com.stockmarket.screener.financial;

import com.stockmarket.screener.entity.ProfitLoss;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GrowthService {

    /**
     * Sales Growth = (Sales[t] - Sales[t-1]) / Sales[t-1] * 100
     */
    public BigDecimal calculateSalesGrowth(ProfitLoss current, ProfitLoss previous) {
        if (current == null || previous == null) return null;
        return FinancialCalculatorUtils.safeGrowth(current.getSales(), previous.getSales());
    }

    /**
     * Operating Profit Growth = (OpProfit[t] - OpProfit[t-1]) / OpProfit[t-1] * 100
     */
    public BigDecimal calculateOperatingProfitGrowth(ProfitLoss current, ProfitLoss previous) {
        if (current == null || previous == null) return null;
        return FinancialCalculatorUtils.safeGrowth(current.getOperatingProfit(), previous.getOperatingProfit());
    }

    /**
     * Net Profit Growth = (NetProfit[t] - NetProfit[t-1]) / NetProfit[t-1] * 100
     */
    public BigDecimal calculateNetProfitGrowth(ProfitLoss current, ProfitLoss previous) {
        if (current == null || previous == null) return null;
        return FinancialCalculatorUtils.safeGrowth(current.getNetProfit(), previous.getNetProfit());
    }
}

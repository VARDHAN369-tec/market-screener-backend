package com.stockmarket.screener.financial;

import com.stockmarket.screener.entity.ProfitLoss;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProfitabilityService {

    /**
     * Operating Profit Margin = (Operating Profit / Sales) * 100
     */
    public BigDecimal calculateOperatingProfitMargin(ProfitLoss pl) {
        if (pl == null) return null;
        return FinancialCalculatorUtils.safePercentage(pl.getOperatingProfit(), pl.getSales());
    }

    /**
     * Net Profit Margin = (Net Profit / Sales) * 100
     */
    public BigDecimal calculateNetProfitMargin(ProfitLoss pl) {
        if (pl == null) return null;
        return FinancialCalculatorUtils.safePercentage(pl.getNetProfit(), pl.getSales());
    }

    /**
     * Effective Tax Rate = (Tax / Profit Before Tax) * 100
     */
    public BigDecimal calculateEffectiveTaxRate(ProfitLoss pl) {
        if (pl == null) return null;
        return FinancialCalculatorUtils.safePercentage(pl.getTax(), pl.getProfitBeforeTax());
    }
}

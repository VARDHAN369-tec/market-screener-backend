package com.stockmarket.screener.financial;

import com.stockmarket.screener.entity.BalanceSheet;
import com.stockmarket.screener.entity.ProfitLoss;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LiquidityService {

    /**
     * Current Ratio = Other Assets (current portion) / Other Liabilities (current portion)
     */
    public BigDecimal calculateCurrentRatio(BalanceSheet bs) {
        if (bs == null) return null;
        return FinancialCalculatorUtils.safeDivide(bs.getOtherAssets(), bs.getOtherLiabilities(), 2);
    }

    /**
     * Debtor Days = (Debtors / Sales) * 365
     */
    public BigDecimal calculateDebtorDays(BalanceSheet bs, ProfitLoss pl) {
        if (bs == null || pl == null || pl.getSales() == null || pl.getSales().compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        if (bs.getDebtors() == null) return null;
        BigDecimal ratio = bs.getDebtors().divide(pl.getSales(), 6, RoundingMode.HALF_UP);
        return ratio.multiply(BigDecimal.valueOf(365)).setScale(1, RoundingMode.HALF_UP);
    }

    /**
     * Inventory Turnover = Sales / Inventory
     */
    public BigDecimal calculateInventoryTurnover(BalanceSheet bs, ProfitLoss pl) {
        if (bs == null || pl == null || bs.getInventory() == null) return null;
        return FinancialCalculatorUtils.safeDivide(pl.getSales(), bs.getInventory(), 2);
    }
}

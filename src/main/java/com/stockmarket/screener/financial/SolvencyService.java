package com.stockmarket.screener.financial;

import com.stockmarket.screener.entity.BalanceSheet;
import com.stockmarket.screener.entity.ProfitLoss;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SolvencyService {

    /**
     * Debt-to-Equity = Borrowings / (Equity Share Capital + Reserves)
     */
    public BigDecimal calculateDebtToEquity(BalanceSheet bs) {
        if (bs == null || bs.getBorrowings() == null) return null;
        BigDecimal equity = BigDecimal.ZERO;
        if (bs.getEquityShareCapital() != null) equity = equity.add(bs.getEquityShareCapital());
        if (bs.getReserves() != null) equity = equity.add(bs.getReserves());
        return FinancialCalculatorUtils.safeDivide(bs.getBorrowings(), equity, 2);
    }

    /**
     * Interest Coverage = Operating Profit / Interest
     */
    public BigDecimal calculateInterestCoverage(ProfitLoss pl) {
        if (pl == null || pl.getOperatingProfit() == null || pl.getInterest() == null) return null;
        return FinancialCalculatorUtils.safeDivide(pl.getOperatingProfit(), pl.getInterest(), 2);
    }
}

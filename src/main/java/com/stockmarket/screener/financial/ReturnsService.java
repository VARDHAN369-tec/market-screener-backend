package com.stockmarket.screener.financial;

import com.stockmarket.screener.entity.BalanceSheet;
import com.stockmarket.screener.entity.ProfitLoss;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReturnsService {

    /**
     * ROE = (Net Profit / (Equity Share Capital + Reserves)) * 100
     */
    public BigDecimal calculateRoe(ProfitLoss pl, BalanceSheet bs) {
        if (pl == null || bs == null || pl.getNetProfit() == null) return null;
        BigDecimal equity = BigDecimal.ZERO;
        if (bs.getEquityShareCapital() != null) equity = equity.add(bs.getEquityShareCapital());
        if (bs.getReserves() != null) equity = equity.add(bs.getReserves());
        return FinancialCalculatorUtils.safePercentage(pl.getNetProfit(), equity);
    }

    /**
     * ROCE = ((PBT + Interest) / (Equity Share Capital + Reserves + Borrowings)) * 100
     */
    public BigDecimal calculateRoce(ProfitLoss pl, BalanceSheet bs) {
        if (pl == null || bs == null) return null;
        BigDecimal ebit = BigDecimal.ZERO;
        if (pl.getProfitBeforeTax() != null) ebit = ebit.add(pl.getProfitBeforeTax());
        if (pl.getInterest() != null) ebit = ebit.add(pl.getInterest());

        BigDecimal capitalEmployed = BigDecimal.ZERO;
        if (bs.getEquityShareCapital() != null) capitalEmployed = capitalEmployed.add(bs.getEquityShareCapital());
        if (bs.getReserves() != null) capitalEmployed = capitalEmployed.add(bs.getReserves());
        if (bs.getBorrowings() != null) capitalEmployed = capitalEmployed.add(bs.getBorrowings());

        return FinancialCalculatorUtils.safePercentage(ebit, capitalEmployed);
    }
}

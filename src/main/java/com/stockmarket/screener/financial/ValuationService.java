package com.stockmarket.screener.financial;

import com.stockmarket.screener.entity.BalanceSheet;
import com.stockmarket.screener.entity.MarketData;
import com.stockmarket.screener.entity.ProfitLoss;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ValuationService {

    /**
     * BVPS = (Equity Share Capital + Reserves) / Number of Shares
     */
    public BigDecimal calculateBvps(BalanceSheet bs, MarketData md) {
        if (bs == null || md == null || md.getNumberOfShares() == null) return null;
        BigDecimal equity = BigDecimal.ZERO;
        if (bs.getEquityShareCapital() != null) equity = equity.add(bs.getEquityShareCapital());
        if (bs.getReserves() != null) equity = equity.add(bs.getReserves());
        return FinancialCalculatorUtils.safeDivide(equity, md.getNumberOfShares(), 2);
    }

    /**
     * P/E = Current Price / EPS
     */
    public BigDecimal calculatePeRatio(MarketData md, ProfitLoss pl) {
        if (md == null || pl == null || md.getCurrentPrice() == null || pl.getEps() == null) return null;
        return FinancialCalculatorUtils.safeDivide(md.getCurrentPrice(), pl.getEps(), 2);
    }

    /**
     * P/B = Current Price / BVPS
     */
    public BigDecimal calculatePbRatio(MarketData md, BigDecimal bvps) {
        if (md == null || md.getCurrentPrice() == null || bvps == null) return null;
        return FinancialCalculatorUtils.safeDivide(md.getCurrentPrice(), bvps, 2);
    }

    /**
     * Dividend Payout Ratio = (Dividend Amount / Net Profit) * 100
     */
    public BigDecimal calculateDividendPayoutRatio(ProfitLoss pl) {
        if (pl == null || pl.getDividendAmount() == null || pl.getNetProfit() == null) return null;
        return FinancialCalculatorUtils.safePercentage(pl.getDividendAmount(), pl.getNetProfit());
    }
}

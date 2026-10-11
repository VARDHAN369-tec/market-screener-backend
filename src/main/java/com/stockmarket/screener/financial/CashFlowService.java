package com.stockmarket.screener.financial;

import com.stockmarket.screener.entity.BalanceSheet;
import com.stockmarket.screener.entity.CashFlow;
import com.stockmarket.screener.entity.ProfitLoss;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CashFlowService {

    /**
     * Operating Cash Flow Efficiency = Cash from Operating Activity / Operating Profit
     */
    public BigDecimal calculateOcfEfficiency(CashFlow cf, ProfitLoss pl) {
        if (cf == null || pl == null || cf.getCashFromOperatingActivity() == null || pl.getOperatingProfit() == null) {
            return null;
        }
        return FinancialCalculatorUtils.safeDivide(cf.getCashFromOperatingActivity(), pl.getOperatingProfit(), 2);
    }

    /**
     * Free Cash Flow = Cash from Operating Activity - Capital Expenditures
     * CapEx estimated from change in Net Block + CWIP or Investing Activity
     */
    public BigDecimal calculateFreeCashFlow(CashFlow cf, BalanceSheet currentBs, BalanceSheet previousBs, ProfitLoss pl) {
        if (cf == null || cf.getCashFromOperatingActivity() == null) return null;

        BigDecimal capex = BigDecimal.ZERO;
        if (currentBs != null && previousBs != null && currentBs.getNetBlock() != null && previousBs.getNetBlock() != null) {
            BigDecimal netBlockDiff = currentBs.getNetBlock().subtract(previousBs.getNetBlock());
            BigDecimal depr = (pl != null && pl.getDepreciation() != null) ? pl.getDepreciation() : BigDecimal.ZERO;
            capex = netBlockDiff.add(depr);

            if (currentBs.getCapitalWorkInProgress() != null && previousBs.getCapitalWorkInProgress() != null) {
                capex = capex.add(currentBs.getCapitalWorkInProgress().subtract(previousBs.getCapitalWorkInProgress()));
            }
        } else if (cf.getCashFromInvestingActivity() != null) {
            capex = cf.getCashFromInvestingActivity().abs();
        }

        return cf.getCashFromOperatingActivity().subtract(capex);
    }
}

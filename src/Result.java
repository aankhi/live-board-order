import java.math.BigDecimal;
import java.util.Map;

public class Result {
    Map<BigDecimal, Double> buySummary;
    Map<BigDecimal, Double> sellSummary;

    protected Result(Map<BigDecimal, Double> buySummary, Map<BigDecimal, Double> sellSummary) {
        this.buySummary = buySummary;
        this.sellSummary = sellSummary;
    }

    public Map<BigDecimal, Double> getBuySummary() {
        return buySummary;
    }

    public Map<BigDecimal, Double> getSellSummary() {
        return sellSummary;
    }
}

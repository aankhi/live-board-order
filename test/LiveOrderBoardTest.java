import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LiveOrderBoardTest {

    private LiveOrderBoard liveOrderBoard = new LiveOrderBoard();
    private Order[] sampleBuyOrders = new Order[4];
    private Order[] sampleSellOrders = new Order[4];

    @BeforeEach
    void setUp() {
        //fill out test set for arrays of orders
        sampleBuyOrders[0] = new Order(1, "user1", 3.5, BigDecimal.valueOf(307), OrderType.valueOf("BUY"));
        sampleBuyOrders[1] = new Order(2, "user2", 1.2, BigDecimal.valueOf(310), OrderType.valueOf("BUY"));
        sampleBuyOrders[2] = new Order(3, "user3", 1.5, BigDecimal.valueOf(306), OrderType.valueOf("BUY"));
        sampleBuyOrders[3] = new Order(4, "user4", 2, BigDecimal.valueOf(307), OrderType.valueOf("BUY"));

        sampleSellOrders[0] = new Order(5, "user1", 3.5, BigDecimal.valueOf(307), OrderType.valueOf("SELL"));
        sampleSellOrders[1] = new Order(6, "user2", 1.2, BigDecimal.valueOf(310), OrderType.valueOf("SELL"));
        sampleSellOrders[2] = new Order(7, "user3", 1.5, BigDecimal.valueOf(306), OrderType.valueOf("SELL"));
        sampleSellOrders[3] = new Order(8, "user4", 2, BigDecimal.valueOf(307), OrderType.valueOf("SELL"));
    }

    // Here we test if ONE trade for each buy and sell is being added correctly,
    // we are not checking for correctness of result summary.
    @Test
    void registerNewOrdersForBuyAndSell() {
        //Arrange.
        Order buyOrder = sampleBuyOrders[0],
                sellOrder = sampleSellOrders[0];

        //Act - we don't really use the orderId.
        liveOrderBoard.registerNewOrder(buyOrder.getUserId(), buyOrder.getQty(), buyOrder.getPricePerUnit(),
                buyOrder.getType());
        liveOrderBoard.registerNewOrder(sellOrder.getUserId(), sellOrder.getQty(), sellOrder.getPricePerUnit(),
                sellOrder.getType());

        Result result = liveOrderBoard.getSummaryOfLiveOrders();

        //Assert
        //check if buy is correct.
        Map<BigDecimal, Double> buySummary = result.getBuySummary();
        assertEquals(1, buySummary.size());
        assertEquals(buyOrder.getQty(), buySummary.get(buyOrder.getPricePerUnit()));

        //check if sell if correct.
        Map<BigDecimal, Double> sellSummary = result.getSellSummary();
        assertEquals(1, sellSummary.size());
        assertEquals(sellOrder.getQty(), buySummary.get(sellOrder.getPricePerUnit()));
    }

    //We only test for buy, the same logic is used for sell.
    @Test
    void cancelBuyOrder() {
        //Arrange
        Order buyOrder1 = sampleBuyOrders[0],
                buyOrder2 = sampleBuyOrders[1];

        //Act
        String[] tradeIds = new String[2];

        //Act - we don't really use the orderId.
        tradeIds[0] = liveOrderBoard.registerNewOrder(buyOrder1.getUserId(), buyOrder1.getQty(),
                buyOrder1.getPricePerUnit(), buyOrder1.getType());
        tradeIds[1] = liveOrderBoard.registerNewOrder(buyOrder2.getUserId(), buyOrder2.getQty(),
                buyOrder2.getPricePerUnit(), buyOrder2.getType());

        liveOrderBoard.cancelOrder(tradeIds[0]);

        Result result = liveOrderBoard.getSummaryOfLiveOrders();
        Map<BigDecimal, Double> buySummary = result.getBuySummary();
        Map<BigDecimal, Double> sellSummary = result.getSellSummary();

        //Assert
        //the cancelled one is not even added to the buySummary because it is inactive.
        assertEquals(1, buySummary.size());
        assertEquals(0, sellSummary.size());
        assertEquals(buySummary.get(buyOrder2.getPricePerUnit()), buyOrder2.getQty());
    }

    @Test
    void checkCorrectnessOfSummaryOfOrders() {
        //Arrange expected results --
        Map<BigDecimal, Double> expectedBuySummary = new TreeMap<>(Collections.reverseOrder());
        expectedBuySummary.put(BigDecimal.valueOf(310), 1.2);
        expectedBuySummary.put(BigDecimal.valueOf(306), 1.5);
        expectedBuySummary.put(BigDecimal.valueOf(307), 5.5);

        Map<BigDecimal, Double> expectedSellSummary = new TreeMap<>();
        expectedSellSummary.put(BigDecimal.valueOf(307), 5.5);
        expectedSellSummary.put(BigDecimal.valueOf(310), 1.2);
        expectedSellSummary.put(BigDecimal.valueOf(306), 1.5);

        //Act
        for(Order sampleBuyOrder : sampleBuyOrders) {
            //we don't really use the orderId.
            liveOrderBoard.registerNewOrder(sampleBuyOrder.getUserId(), sampleBuyOrder.getQty(),
                    sampleBuyOrder.getPricePerUnit(), sampleBuyOrder.getType());
        }

        for(Order sampleSellOrder : sampleSellOrders) {
            //we don't really use the orderId.
            liveOrderBoard.registerNewOrder(sampleSellOrder.getUserId(), sampleSellOrder.getQty(),
                    sampleSellOrder.getPricePerUnit(), sampleSellOrder.getType());
        }

        Result result = liveOrderBoard.getSummaryOfLiveOrders();

        //Assert
        assertEquals(result.getBuySummary(), expectedBuySummary);
        assertEquals(result.getSellSummary(), expectedBuySummary);
    }
}
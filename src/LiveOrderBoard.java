import java.math.BigDecimal;
import java.util.*;

//multi-threading? Too unnecessarily complex - but look into theoretical design.
public class LiveOrderBoard {
    private List<Order> orderList = new ArrayList<>();
    private long lastTimeMillis = 0;

    //Creating an orderId gives us a unique key to identify specific trades, useful for trade data retrieval.
    //we return the orderId so the client can track the order. We do not use this in the test/demo.
    public String registerNewOrder(String userId, double qty, BigDecimal pricePerUnit, OrderType type) {
        //long orderId = generateOrderId();
        //        Order newOrder = new Order(orderId, userId, qty, pricePerUnit, type);
        String orderId = java.util.UUID.randomUUID().toString();
        Order newOrder = new Order(orderId, userId, qty, pricePerUnit, type);
        orderList.add(newOrder);
        return orderId;
    }

    //helper function -
    // We can generate ID in a few different ways, some I have contemplated are -
    // 1. Using a counter which simplifies the programme - kind of follows a SQL-like pattern,
    // 2. Using current time in milliseconds - sample implementation provided.
    //      We track last used time and check last used time to ensure unique id's.
    // 3. Generating random UUID - java.util.UUID.randomUUID();
    //implementation of #2 -
    private long generateOrderId() {
        long currentTimeMillis =  System.currentTimeMillis();

        if(lastTimeMillis == currentTimeMillis) {
            currentTimeMillis++;
        }

        lastTimeMillis = currentTimeMillis;
        return currentTimeMillis;
    }

    //Ideally, we should not delete a trade for the sake of record keeping - as common sense dictates.
    //Therefore, when we 'cancel' a trade, we merely *disable* it.

    //If userId is Long --->
    // Searching is linear i.e. O(n) which is not ideal.
    // We could optimise this by applying a search algorithm such as binary search - O(log n)
    //      as the datastructure used is an arraylist which will maintain insertion order -
    //      and insertion order is based on time i.e. natural order of numerical id.
    // However, this adds an additional layer of assumptions and is possibly unnecessary to demonstrate in this instance.
    // This would not be an issue for a SQL query.
    public void cancelOrder(String orderId) {
        for(Order order : orderList) {
            if(orderId.equals(order.getOrderId())) {
                order.setActive(false);
            }
        }
    }

    //1. We merge quantity by 'price' i.e. price is unique.
    //2. For SELL, arrange by ascending order. For BUY, descending order.
    // A map is used because we want (key,value) pairs where key -> price (unique), value -> total value.
    // Furthermore, a map is best when data retrieval and insertion are heavily used.
    // The chosen implementation for Map that is used is TreeMap as we want results to be sorted by key (price).
    public Result getSummaryOfLiveOrders() {
        Map<BigDecimal, Double> buySummary = new TreeMap<>(Collections.reverseOrder());
        Map<BigDecimal, Double> sellSummary = new TreeMap<>();

        for(Order order : orderList) {
            if(order.isActive()) {
                if (order.getType() == OrderType.BUY) {
                    if (buySummary.containsKey(order.getPricePerUnit())) {
                        buySummary.put(order.getPricePerUnit(), buySummary.get(order.getPricePerUnit()) + order.getQty());
                    } else {
                        buySummary.put(order.getPricePerUnit(), order.getQty());
                    }
                } else if (order.getType() == OrderType.SELL) {
                    if (sellSummary.containsKey(order.getPricePerUnit())) {
                        sellSummary.put(order.getPricePerUnit(), sellSummary.get(order.getPricePerUnit()) + order.getQty());
                    } else {
                        sellSummary.put(order.getPricePerUnit(), order.getQty());
                    }
                }
            }
        }

        return new Result(buySummary, sellSummary);
    }

    //in this function, I have briefly demonstrated how we expect the library to be used by the client.
    public static void main(String[] args) {
        List<String> listOfIds = new ArrayList<>();
        LiveOrderBoard liveOrderBoard = new LiveOrderBoard();
        listOfIds.add(liveOrderBoard.registerNewOrder("user1", 3.5, BigDecimal.valueOf(306), OrderType.valueOf("SELL")));
        listOfIds.add(liveOrderBoard.registerNewOrder("user2", 1.2, BigDecimal.valueOf(310), OrderType.valueOf("SELL")));
        listOfIds.add(liveOrderBoard.registerNewOrder("user2", 1.2, BigDecimal.valueOf(310), OrderType.valueOf("BUY")));
        listOfIds.add(liveOrderBoard.registerNewOrder("user3", 1.5, BigDecimal.valueOf(307), OrderType.valueOf("SELL")));
        listOfIds.add(liveOrderBoard.registerNewOrder("user3", 1.5, BigDecimal.valueOf(307), OrderType.valueOf("BUY")));
        listOfIds.add(liveOrderBoard.registerNewOrder("user4", 5, BigDecimal.valueOf(306), OrderType.valueOf("BUY")));
        listOfIds.add(liveOrderBoard.registerNewOrder("user4", 2, BigDecimal.valueOf(306), OrderType.valueOf("SELL")));
        listOfIds.add(liveOrderBoard.registerNewOrder("user1", 3.5, BigDecimal.valueOf(306), OrderType.valueOf("BUY")));

        liveOrderBoard.cancelOrder(listOfIds.get(0));

        Result result = liveOrderBoard.getSummaryOfLiveOrders();

        for (Map.Entry<BigDecimal, Double> entry : result.getBuySummary().entrySet()) {
            System.out.println("BUY: " + entry.getValue() + " kg for £" + entry.getKey());
        }

        for (Map.Entry<BigDecimal, Double> entry : result.getSellSummary().entrySet()) {
            System.out.println("SELL: " + entry.getValue() + " kg for £" + entry.getKey());
        }
    }
}


//Suggested extensions/modifications:
//  - Obvious addition of a database which would take care of a primary key.
//  - Writing and reading trades from a file to offer persistence.
//  - Making the public functions available through API calls.
import java.math.BigDecimal;

//talk about how you would do this with SQL DB -
//    - save user details
//    - index on pricePerUnit + type?
public class Order {
    private long orderIDLong;
    private String orderId;
    private String userId;
    private double qty; //assumed to be in kg, can be extended with enum for supported types
    private BigDecimal pricePerUnit; //supports £ now, can add extension in the future with an enum for supported types
    private OrderType type;
    private boolean isActive;

    public Order(String  orderId, String userId, double qty, BigDecimal pricePerUnit, OrderType type) {
        this.orderId = orderId;
        this.userId = userId;
        this.qty = qty;
        this.pricePerUnit = pricePerUnit;
        this.type = type;
        this.isActive = true;
    }

    public Order(long  orderIDLong, String userId, double qty, BigDecimal pricePerUnit, OrderType type) {
        this.orderIDLong = orderIDLong;
        this.userId = userId;
        this.qty = qty;
        this.pricePerUnit = pricePerUnit;
        this.type = type;
        this.isActive = true;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

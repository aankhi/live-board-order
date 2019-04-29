# live-board-order

This is an implementation for a live order board library with the following public functions -

**public String registerNewOrder(String userId, double qty, BigDecimal pricePerUnit, OrderType type)**

**public void cancelOrder(String orderId)**

**public Result getSummaryOfLiveOrders()**

The usage has been demonstrated using the *main* function.

Design decisions have been listed in the *comments*.


**Some suggested extensions/modifications to the presented solution:**
- Addition of a database which would take care of a primary key.
- Writing and reading trades from a file to offer persistence.
- Making the public functions available through API calls.
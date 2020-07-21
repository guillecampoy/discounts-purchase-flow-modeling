package discounts.purchase.flow.modeling;

public class PaymentTypeDiscountConstraint implements Constraint {
    private final String value;

    public PaymentTypeDiscountConstraint(String paymentType) {
        this.value = paymentType;
    }
}

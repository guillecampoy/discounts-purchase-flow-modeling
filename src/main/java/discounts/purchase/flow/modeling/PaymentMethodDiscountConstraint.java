package discounts.purchase.flow.modeling;

public class PaymentMethodDiscountConstraint implements Constraint {
    private String value;
    public PaymentMethodDiscountConstraint(String paymentMethod) {
        this.value = paymentMethod;
    }
}

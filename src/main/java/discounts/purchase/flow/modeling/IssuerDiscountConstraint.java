package discounts.purchase.flow.modeling;

public class IssuerDiscountConstraint implements Constraint{
    private final int value;

    public IssuerDiscountConstraint(int issuer) {
        this.value = issuer;
    }
}

package discounts.purchase.flow.modeling;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AVistaDiscount implements DiscountProvider {
    private final boolean combinable;
    private final CouponEntyty couponEntyty;
    private BigDecimal amount;
    //this is a decition, but non of a vista discounts type should have an empty contrait list...
    //we fix this business definition as a default construction definition
    private List<Constraint> constrains = new ArrayList<>();

    public AVistaDiscount(BigDecimal couponAmount, List<Constraint> discountConstraints, boolean combine, CouponEntyty couponEntyty) {
        this.amount = couponAmount;
        this.constrains.addAll(discountConstraints);
        this.combinable = combine;
        this.couponEntyty = couponEntyty;
    }

    @Override
    public BigDecimal getDiscountAmount() {
        return amount;
    }

    @Override
    public List<Constraint> getConstraints() {
        return this.constrains;
    }

    @Override
    public CouponEntyty getCouponEntity() {
        return this.couponEntyty;
    }

    @Override
    public Boolean getCombine() {
        return this.combinable;
    }

    @Override
    public Boolean validateContext(DiscountContext discountContext) {
        DiscountValidationContext discountValidationContext = new DiscountValidationContext(this.getConstraints(), discountContext);
        return discountValidationContext.applyConstraints();
    }
}

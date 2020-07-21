package discounts.purchase.flow.modeling;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RebateDiscount implements DiscountProvider {
    private final CouponEntyty couponEntity;
    private final boolean combinable;
    private BigDecimal amount;

    //empty inicialization for constraints list
    private List<Constraint> constrains = new ArrayList<>();


    public RebateDiscount(BigDecimal amount, CouponEntyty couponEntyty, boolean combinable) {
        this.amount = amount;
        this.couponEntity= couponEntyty;
        this.combinable = combinable;
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
        return this.couponEntity;
    }

    @Override
    public Boolean getCombine() {
        return this.combinable;
    }

    @Override
    public Boolean validateContext(DiscountContext discountContext) {
        return true;
    }
}

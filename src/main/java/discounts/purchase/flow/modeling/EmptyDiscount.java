package discounts.purchase.flow.modeling;

import java.math.BigDecimal;
import java.util.List;

public class EmptyDiscount implements DiscountProvider {
    @Override
    public BigDecimal getDiscountAmount() {
        return new BigDecimal(0);
    }

    @Override
    public List<Constraint> getConstraints() {
        return null;
    }

    @Override
    public CouponEntyty getCouponEntity() {
        return null;
    }

    @Override
    public Boolean getCombine() {
        return null;
    }

    @Override
    public Boolean validateContext(DiscountContext discountContext) {
        return true;
    }
}

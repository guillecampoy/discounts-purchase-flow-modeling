package discounts.purchase.flow.modeling;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountProvider {
    BigDecimal getDiscountAmount();
    List<Constraint> getConstraints();
    CouponEntyty getCouponEntity();
    Boolean getCombine();

    Boolean validateContext(DiscountContext discountContext);
}

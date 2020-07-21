package discounts.purchase.flow.modeling;

import java.util.Objects;

public class ItemCouponEntity implements CouponEntyty {
    private final String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCouponEntity that = (ItemCouponEntity) o;
        return Objects.equals(value, that.value);
    }

    public ItemCouponEntity(String value) {
        this.value = value;
    }
}

package discounts.purchase.flow.modeling;

import java.util.ArrayList;
import java.util.List;

public class DiscountContext {
    private List<DiscountContextApplier> discountContextApplierList = new ArrayList<>();

    public DiscountContext(List<DiscountContextApplier> discountContextApplierList) {
        this.discountContextApplierList.addAll(discountContextApplierList);
    }

    public DiscountContextApplier getIndex(int index) {
        return discountContextApplierList.get(index);
    }
}

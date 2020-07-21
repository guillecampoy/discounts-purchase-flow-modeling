package discounts.purchase.flow.modeling;

import java.util.Objects;

public class InstallmentAmountDiscountConstraint implements Constraint{
    private int installmentQTY;

    //Auto generated for java +7
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstallmentAmountDiscountConstraint that = (InstallmentAmountDiscountConstraint) o;
        return installmentQTY == that.installmentQTY;
    }

    public InstallmentAmountDiscountConstraint(int instalmentQTY) {
        this.installmentQTY = instalmentQTY;
    }
}

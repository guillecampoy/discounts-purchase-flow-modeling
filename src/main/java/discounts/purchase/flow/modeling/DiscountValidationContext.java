package discounts.purchase.flow.modeling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DiscountValidationContext {
    private List<Constraint> constraints;
    private DiscountContext discountContext;

    public DiscountValidationContext(List<Constraint> constraints, DiscountContext discountContext) {
        this.constraints = constraints;
        this.discountContext = discountContext;
    }

    public Boolean applyConstraints() {
        if (constraints.isEmpty()) return true;

        else {
            //Villerada TIME!!! This all represents the available constrints
            //This supports a list of element context, now the context is represented by the payment it self
            //actually our flow supports a list of payments, split cases and partial account money scenarios

            Collection<Constraint> contextoApplyRestrictions = new ArrayList<>();
            contextoApplyRestrictions.add(discountContext.getIndex(0).getInstallmentAmountConstraint());
            contextoApplyRestrictions.add(discountContext.getIndex(0).getIssuerConstraint());
            contextoApplyRestrictions.add(discountContext.getIndex(0).getPaymentMethodConstraint());
            contextoApplyRestrictions.add(discountContext.getIndex(0).getPaymentTypeConstraint());

            if (contextoApplyRestrictions.containsAll(constraints)) {
                return true;
            }
            return false;
        }

    }
}

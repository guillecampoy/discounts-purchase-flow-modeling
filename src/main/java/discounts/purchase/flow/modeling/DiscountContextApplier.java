package discounts.purchase.flow.modeling;

public class DiscountContextApplier {
    private PaymentMethodDiscountConstraint paymentMethod;
    private PaymentTypeDiscountConstraint  paymentType;
    private InstallmentAmountDiscountConstraint installmentAmount;
    private IssuerDiscountConstraint issuer;

    public DiscountContextApplier(String paymentMethod, String paymentType, int installmentAmount, int issuer) {
        this.paymentMethod = new PaymentMethodDiscountConstraint(paymentMethod);
        this.paymentType = new PaymentTypeDiscountConstraint(paymentType);
        this.installmentAmount = new InstallmentAmountDiscountConstraint(installmentAmount);
        this.issuer = new IssuerDiscountConstraint(issuer);
    }

    PaymentMethodDiscountConstraint getPaymentMethodConstraint() {
        return paymentMethod;
    }

    PaymentTypeDiscountConstraint getPaymentTypeConstraint() {
       return paymentType;
    }

    InstallmentAmountDiscountConstraint getInstallmentAmountConstraint() {
        return installmentAmount;
    }

    IssuerDiscountConstraint getIssuerConstraint() {
        return issuer;
    }
}

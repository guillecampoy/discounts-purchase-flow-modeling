package discounts.purchase.flow.modeling;

import org.junit.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiscountsTet {

    //Core business class


    // Construction init tests

    /**
     * Basic data provided for each discount from external service, then adapted by our existent external layer
     * BigDecimal: amount
     * String: discountType, posible values a_vista | rebate
     * Constraints:  Structured data List
     *              [
     *                  installment: [
     *                                  amount:1
     *                              ],
     *              PaymentType: List [DEBIT, CREDIT], (could be empty and applies for all types)
     *              Combine: boolean,
     *              entity: Item | payment | shippment | GAREX | PACK // the concept that discounts applies
     *              ]
     * */

    /**
     * Case 01
     * External service don`t provide a discount structure
     * The internal application mappers and adapters should build an empty business object
     */
    @Test
    public void noDiscountsGenerated () {
        //setup
        DiscountProvider emptyDiscount = new EmptyDiscount();

        //then
        assert emptyDiscount.getDiscountAmount().equals(new BigDecimal(0));
        assert emptyDiscount.getCombine() == null;
        assert emptyDiscount.getCouponEntity() == null;
        assert emptyDiscount.getConstraints() == null;
    }

    /**
     * Case 02
     * External service provide a "a VISTA" discount type structure
     * The internal application mappers and adapters should build an "A VISTA" business object
     * A VISTA object have a constraints structure, in this case only apply for 1x installment amount, future
     * constraints are imminent like payment method or type will come.
     */
    @Test
    public void aVistaDiscountGenerated(){
        //setup
        DiscountProvider aVistaDiscount = generateAvistaBasicRestrictionDiscount();

        //then
        assert aVistaDiscount.getConstraints().contains(new InstallmentAmountDiscountConstraint(1));
        assert aVistaDiscount.getDiscountAmount().equals(new BigDecimal(45));
        assert aVistaDiscount.getCouponEntity().equals(new ItemCouponEntity("MLA12345"));
        assert aVistaDiscount.getCombine().equals(true);
    }

    /**
     * Case 03
     * External service provide a "REBATE" discount type structure
     * The internal application mappers and adapters should build a "REBATE" business object
     * REBATE object have not got constraints structure
     */
    @Test
    public void rebateDiscountGenerated(){
        //setup
        DiscountProvider rebateDiscount = getrebateDiscountCase03();
        //then
        assert rebateDiscount.getConstraints().isEmpty();
        assert rebateDiscount.getDiscountAmount().equals(new BigDecimal(67));
        assert rebateDiscount.getCouponEntity().equals(new ItemCouponEntity("MLA999887"));
        assert rebateDiscount.getCombine().equals(true);
    }

    /**
     * Case 04
     * External service provide a "a VISTA" discount type structure
     * The internal application mappers and adapters should build an "A VISTA" business object
     * A VISTA object have a constraints structure, in this case only apply for 1x installment amount
     * and VISA as payment method
     */
    @Test
    public void aVistaVISADiscountGenerated(){
        //setup
        CouponEntyty couponEntyty = new ItemCouponEntity("MLA999887"); //itemID
        Constraint installmentConstraint = new InstallmentAmountDiscountConstraint(1);
        Constraint paymentMethodConstraint = new PaymentMethodDiscountConstraint("VISA");
        List<Constraint> constraints = Stream.of(installmentConstraint, paymentMethodConstraint)
                .collect(Collectors.toList());

        DiscountProvider aVistaDiscount = new AVistaDiscount(new BigDecimal(67),
                                                             constraints,
                                                            true,
                                                             couponEntyty);
        //then
        assert aVistaDiscount.getConstraints().size() == 2;
        assert aVistaDiscount.getDiscountAmount().equals(new BigDecimal(67));
        assert aVistaDiscount.getCouponEntity().equals(new ItemCouponEntity("MLA999887"));
    }

    /**
     * Case 05
     * External service provide a "a VISTA" discount type structure
     * The internal application mappers and adapters should build an "A VISTA" business object
     * A VISTA object have a constraints structure, in this case only apply for 1x installment amount
     * and VISA as payment method
     * and CRECIT_CARD only
     */
    @Test
    public void aVistaVISACreditCardDiscountGenerated(){
        //setup
        CouponEntyty couponEntyty = new ItemCouponEntity("MLA999887"); //itemID
        Constraint installmentConstraint = new InstallmentAmountDiscountConstraint(1);
        Constraint paymentMethodConstraint = new PaymentMethodDiscountConstraint("VISA");
        Constraint paymentTypeContraint = new PaymentTypeDiscountConstraint("CREDIT_CARD");
        List<Constraint> constraints = Stream.of(installmentConstraint,
                                                paymentMethodConstraint,
                                                paymentTypeContraint)
                .collect(Collectors.toList());

        DiscountProvider aVistaDiscount = new AVistaDiscount(new BigDecimal(67),
                constraints,
                true,
                couponEntyty);
        //then
        assert aVistaDiscount.getConstraints().size() == 3;
        assert aVistaDiscount.getDiscountAmount().equals(new BigDecimal(67));
        assert aVistaDiscount.getCouponEntity().equals(new ItemCouponEntity("MLA999887"));
        assert aVistaDiscount.getCombine().equals(true);
    }

    /**Validations criteria cases
     *From previous discounts constructions we simmulate from a context if de discounts aplies or not
     * Considerations purchase context is provided for the actual buyingflow flow, so we do not test this methods
     * only care how or object manage that input
    */

    /**
     * Case 06
     * Empty discount context validation
     * We need to define a context that our discounts contrsints care or should be applied
     * A first aproximation should be
     * Context: List of (more than one payment in current selection)
     *  PaymentMethod // String
     *  PaymentType   //  String
     *  InstallmentAmount // integer
     *  Issuer // integer
     */
    @Test
    public void emptyDiscountContextValidation () {
        //setup
        DiscountProvider emptyDiscount = new EmptyDiscount();

        String paymentMethodSelected = "VISA";
        String paymentTypeSelection = "DEBITCARD";
        int installmentAmount = 1;
        int issuer = 87;
        DiscountContextApplier discountContextApplier = new DiscountContextApplier(paymentMethodSelected,
                paymentTypeSelection, installmentAmount, issuer);

        List<DiscountContextApplier> discountContextApplierList = Stream.of(discountContextApplier)
                .collect(Collectors.toList());

        DiscountContext discountContext = new DiscountContext(discountContextApplierList);

        //when
        Boolean expected = emptyDiscount.validateContext(discountContext);

        //then
        assert expected.equals(true); //allways applies
    }

    /**
     * Case 07
     * External service provide a "a VISTA" discount type structure
     * The internal application mappers and adapters should build an "A VISTA" business object
     * A VISTA object have a constraints structure, in this case only apply for 1x installment amount, future
     * constraints are imminent like payment method or type will come.
     * We intent to validate from a context if applies or not
     */
    @Test
    public void aVistaDiscountContextValidation(){
        //setup
        String paymentMethodSelected = "VISA";
        String paymentTypeSelection = "DEBITCARD";
        int installmentAmount = 1;
        int issuer = 87;
        DiscountContextApplier discountContextApplier = new DiscountContextApplier(paymentMethodSelected, paymentTypeSelection,
                installmentAmount, issuer);

        List<DiscountContextApplier> discountContextApplierList = Stream.of(discountContextApplier)
                .collect(Collectors.toList());

        DiscountContext discountContext = new DiscountContext(discountContextApplierList);
        DiscountProvider aVistaDiscount = generateAvistaBasicRestrictionDiscount();

        //where
        Boolean expected = aVistaDiscount.validateContext(discountContext);

        //then
        assert expected.equals(true);
    }

    /**
     * Case 08
     * External service provide a "a VISTA" discount type structure
     * The internal application mappers and adapters should build an "A VISTA" business object
     * We intent to validate from a context if applies or not as the context
     */
    @Test
    public void aVistaDiscountContexInstallmentValidationFails(){
        //setup
        String paymentMethodSelected = "VISA";
        String paymentTypeSelection = "DEBITCARD";
        int installmentAmount = 2;
        int issuer = 87;
        DiscountContextApplier discountContextApplier = new DiscountContextApplier(paymentMethodSelected, paymentTypeSelection,
                installmentAmount, issuer);

        List<DiscountContextApplier> discountContextApplierList = Stream.of(discountContextApplier)
                .collect(Collectors.toList());

        DiscountContext discountContext = new DiscountContext(discountContextApplierList);
        DiscountProvider aVistaDiscount = generateAvistaBasicRestrictionDiscount();

        //where
        Boolean expected = aVistaDiscount.validateContext(discountContext);

        //then
        assert expected.equals(false);
    }

    /**
     * Case 09
     * External service provide a "REBATE" discount type structure
     * The internal application mappers and adapters should build an "A VISTA" business object
     * We intent to validate from a context if applies or not as the context
     * As a particular case the "REBATE" discount has no constraints so, always applies
     */
    @Test
    public void rebateDiscountContexAlwaysOK(){
        //setup
        String paymentMethodSelected = "VISA";
        String paymentTypeSelection = "DEBITCARD";
        int installmentAmount = 2;
        int issuer = 87;
        DiscountContextApplier discountContextApplier = new DiscountContextApplier(paymentMethodSelected, paymentTypeSelection,
                installmentAmount, issuer);

        List<DiscountContextApplier> discountContextApplierList = Stream.of(discountContextApplier)
                .collect(Collectors.toList());

        DiscountContext discountContext = new DiscountContext(discountContextApplierList);
        DiscountProvider rebate = getrebateDiscountCase03();

        //where
        Boolean expected = rebate.validateContext(discountContext);

        //then
        assert expected.equals(true);
    }

    /**
     * Case 10
     * External service provide a "a VISTA" discount type structure
     * The internal application mappers and adapters should build an "A VISTA" business object
     * We intent to validate from a context if applies or not as the context
     */
    @Test
    public void aVistaDiscountContexIssuerValidationFails(){
        //setup
        String paymentMethodSelected = "VISA";
        String paymentTypeSelection = "DEBITCARD";
        int installmentAmount = 1;
        int issuer = 87;
        DiscountContextApplier discountContextApplier = new DiscountContextApplier(paymentMethodSelected, paymentTypeSelection,
                installmentAmount, issuer);

        List<DiscountContextApplier> discountContextApplierList = Stream.of(discountContextApplier)
                .collect(Collectors.toList());

        DiscountContext discountContext = new DiscountContext(discountContextApplierList);
        DiscountProvider aVistaDiscount = generateAvistaDiscountIssuerRestriction();

        //where
        Boolean expected = aVistaDiscount.validateContext(discountContext);

        //then
        assert expected.equals(false);
    }

    /**
     * Case 11
     * External service provide a "a VISTA" discount type structure
     * The internal application mappers and adapters should build an "A VISTA" business object
     * We intent to validate from a context if applies or not as the context
     */
    @Test
    public void aVistaDiscountContexPaymentTypeValidationFails(){
        //setup
        String paymentMethodSelected = "VISA";
        String paymentTypeSelection = "DEBITCARD";
        int installmentAmount = 1;
        int issuer = 87;
        DiscountContextApplier discountContextApplier = new DiscountContextApplier(paymentMethodSelected, paymentTypeSelection,
                installmentAmount, issuer);

        List<DiscountContextApplier> discountContextApplierList = Stream.of(discountContextApplier)
                .collect(Collectors.toList());

        DiscountContext discountContext = new DiscountContext(discountContextApplierList);
        DiscountProvider aVistaDiscount = generateAvistaDiscountPaymentTypeRestriction();

        //where
        Boolean expected = aVistaDiscount.validateContext(discountContext);

        //then
        assert expected.equals(false);
    }

    /**
     * Case 12
     * External service provide a "a VISTA" discount type structure
     * The internal application mappers and adapters should build an "A VISTA" business object
     * We intent to validate from a context if applies or not as the context
     */
    @Test
    public void aVistaDiscountContexPaymentMethodValidationFails(){
        //setup
        String paymentMethodSelected = "VISA";
        String paymentTypeSelection = "DEBITCARD";
        int installmentAmount = 1;
        int issuer = 87;
        DiscountContextApplier discountContextApplier = new DiscountContextApplier(paymentMethodSelected, paymentTypeSelection,
                installmentAmount, issuer);

        List<DiscountContextApplier> discountContextApplierList = Stream.of(discountContextApplier)
                .collect(Collectors.toList());

        DiscountContext discountContext = new DiscountContext(discountContextApplierList);
        DiscountProvider aVistaDiscount = generateAvistaDiscountPaymentMethodRestriction();

        //where
        Boolean expected = aVistaDiscount.validateContext(discountContext);

        //then
        assert expected.equals(false);
    }

    /**
     * Case 13
     * External service provide a "a VISTA" discount type structure
     * The internal application mappers and adapters should build an "A VISTA" business object
     * We intent to validate from a context if applies or not as the context
     * This special case introduce a new constraint, a list o paymnet methods allowed
     * Methods not supported for example could be Crédits and PayPal, so we provide now a whitelist constraint for paymentMethods
     */
    @Test
    public void aVistaDiscountContexPaymentMethodValidationFailsForCredits(){
        //setup
        String paymentMethodSelected = "CREDITS";
        String paymentTypeSelection = "DIGITAL_CURRENCY";
        int installmentAmount = 1;
        int issuer = 87;
        DiscountContextApplier discountContextApplier = new DiscountContextApplier(paymentMethodSelected, paymentTypeSelection,
                installmentAmount, issuer);

        List<DiscountContextApplier> discountContextApplierList = Stream.of(discountContextApplier)
                .collect(Collectors.toList());

        DiscountContext discountContext = new DiscountContext(discountContextApplierList);
        DiscountProvider aVistaDiscount = generateAvistaDiscountPaymentMethodRestriction();

        //where
        Boolean expected = aVistaDiscount.validateContext(discountContext);

        //then
        assert expected.equals(false);
    }

    /**
     * Case 14
     * External service provide a "a VISTA" discount type structure
     * The internal application mappers and adapters should build an "A VISTA" business object
     * We intent to validate from a context if applies or not as the context
     * This special case introduce a new constraint, a list o paymnet methods allowed
     * Methods not supported for example could be Crédits and PayPal so we provide now a whitelist constraint for paymentMethods
     */
    @Test
    public void aVistaDiscountContexPaymentMethodValidationFailsForPaypal(){
        //setup
        String paymentMethodSelected = "PAYPAL";
        String paymentTypeSelection = "PAYPAL";
        int installmentAmount = 1;
        int issuer = 87;
        DiscountContextApplier discountContextApplier = new DiscountContextApplier(paymentMethodSelected, paymentTypeSelection,
                installmentAmount, issuer);

        List<DiscountContextApplier> discountContextApplierList = Stream.of(discountContextApplier)
                .collect(Collectors.toList());

        DiscountContext discountContext = new DiscountContext(discountContextApplierList);
        DiscountProvider aVistaDiscount = generateAvistaDiscountPaymentMethodRestriction();

        //where
        Boolean expected = aVistaDiscount.validateContext(discountContext);

        //then
        assert expected.equals(false);
    }


    //Discounts creation
    private DiscountProvider getrebateDiscountCase03() {
        CouponEntyty couponEntyty = new ItemCouponEntity("MLA999887"); //itemID
        return new RebateDiscount(new BigDecimal(67),
                couponEntyty,
                true);
    }

    /**
    A vista Basic restriction only cares about installment amount
     */
    private DiscountProvider generateAvistaBasicRestrictionDiscount() {
        CouponEntyty couponEntyty = new ItemCouponEntity("MLA12345"); //itemID
        Constraint installmentConstraint = new InstallmentAmountDiscountConstraint(1);
        List<Constraint> constraints = Stream.of(installmentConstraint)
                .collect(Collectors.toList());

        return new AVistaDiscount(new BigDecimal(45),
                constraints,
                true,
                couponEntyty);
    }

    private DiscountProvider generateAvistaDiscountIssuerRestriction() {
        CouponEntyty couponEntyty = new ItemCouponEntity("MLA12345"); //itemID
        Constraint installmentConstraint = new InstallmentAmountDiscountConstraint(1);
        Constraint issuerConstraint = new IssuerDiscountConstraint(90);
        List<Constraint> constraints = Stream.of(installmentConstraint, issuerConstraint)
                .collect(Collectors.toList());

        return new AVistaDiscount(new BigDecimal(45),
                constraints,
                true,
                couponEntyty);
    }

    private DiscountProvider generateAvistaDiscountPaymentTypeRestriction() {
        CouponEntyty couponEntyty = new ItemCouponEntity("MLA12345"); //itemID
        Constraint installmentConstraint = new InstallmentAmountDiscountConstraint(1);
        Constraint paymentType = new PaymentTypeDiscountConstraint("CREDIT_CARD");
        List<Constraint> constraints = Stream.of(installmentConstraint, paymentType)
                .collect(Collectors.toList());

        return new AVistaDiscount(new BigDecimal(45),
                constraints,
                true,
                couponEntyty);
    }

    private DiscountProvider generateAvistaDiscountPaymentMethodRestriction() {
        CouponEntyty couponEntyty = new ItemCouponEntity("MLA12345"); //itemID
        Constraint installmentConstraint = new InstallmentAmountDiscountConstraint(1);
        Constraint paymentMethod = new PaymentMethodDiscountConstraint("ELO");
        List<Constraint> constraints = Stream.of(installmentConstraint, paymentMethod)
                .collect(Collectors.toList());

        return new AVistaDiscount(new BigDecimal(45),
                constraints,
                true,
                couponEntyty);
    }

}

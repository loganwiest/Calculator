import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * Controller class.
 *
 * @author Logan Wiest
 */
public final class NNCalcController1 implements NNCalcController {

    /**
     * Model object.
     */
    private final NNCalcModel model;

    /**
     * View object.
     */
    private final NNCalcView view;

    /**
     * Useful constants.
     */
    private static final NaturalNumber TWO = new NaturalNumber2(2),
            INT_LIMIT = new NaturalNumber2(Integer.MAX_VALUE);

    /**
     * Updates this.view to display this.model, and to allow only operations
     * that are legal given this.model.
     *
     * @param model
     *            the model
     * @param view
     *            the view
     * @ensures [view has been updated to be consistent with model]
     */
    private static void updateViewToMatchModel(NNCalcModel model,
            NNCalcView view) {
        //Initialize booleans, assume the buttons appear
        boolean subtract = true;
        boolean divide = true;
        boolean power = true;
        boolean root = true;
        //Update top ansd bottom text areas with appropriate numbers
        view.updateTopDisplay(model.top());
        view.updateBottomDisplay(model.bottom());
        //Refer to javaDoc contracts in NNCalcController if the button should be usable or not.
        if (model.bottom().compareTo(model.top()) <= 0) {
            view.updateSubtractAllowed(subtract);
        } else {
            view.updateSubtractAllowed(false);
        }
        if (!model.bottom().isZero()) {
            view.updateDivideAllowed(divide);
        } else {
            view.updateDivideAllowed(false);
        }
        if (model.bottom().compareTo(INT_LIMIT) <= 0) {
            view.updatePowerAllowed(power);
        } else {
            view.updatePowerAllowed(false);
        }
        if (model.bottom().compareTo(TWO) >= 0
                && model.bottom().compareTo(INT_LIMIT) <= 0) {
            view.updateRootAllowed(root);
        } else {
            view.updateRootAllowed(false);
        }
    }

    /**
     * Constructor.
     *
     * @param model
     *            model to connect to
     * @param view
     *            view to connect to
     */
    public NNCalcController1(NNCalcModel model, NNCalcView view) {
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processClearEvent() {
        /*
         * Get alias to bottom from model
         */
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        bottom.clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processSwapEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        NaturalNumber temp = top.newInstance();
        temp.transferFrom(top);
        top.transferFrom(bottom);
        bottom.transferFrom(temp);
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processEnterEvent() {
        //Top and bottom should have same value when enter is used
        this.model.top().copyFrom(this.model.bottom());
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processAddEvent() {
        this.model.bottom().add(this.model.top());
        this.model.top().clear();
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processSubtractEvent() {
        this.model.top().subtract(this.model.bottom());
        this.model.bottom().transferFrom(this.model.top());
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processMultiplyEvent() {
        this.model.top().multiply(this.model.bottom());
        this.model.bottom().transferFrom(this.model.top());
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processDivideEvent() {
        NaturalNumber r = this.model.top().divide(this.model.bottom());
        this.model.bottom().transferFrom(this.model.top());
        //top text box should display remainder
        this.model.top().transferFrom(r);
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processPowerEvent() {
        int i = this.model.bottom().toInt();
        this.model.top().power(i);
        this.model.bottom().transferFrom(this.model.top());
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processRootEvent() {
        int r = this.model.bottom().toInt();
        this.model.top().root(r);
        this.model.bottom().transferFrom(this.model.top());
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processAddNewDigitEvent(int digit) {
        //Appends the digit to the end of bottom NN
        this.model.bottom().multiplyBy10(digit);
        updateViewToMatchModel(this.model, this.view);
    }

}

public class ProductionRule {

    private String leftHand;
    private String rightHand;

    public ProductionRule(String leftHand, String rightHand) {
        this.leftHand = leftHand;
        this.rightHand = rightHand;
    }

    public String getLeftHand() {
        return leftHand;
    }

    public void setLeftHand(String leftHand) {
        this.leftHand = leftHand;
    }

    public String getRightHand() {
        return rightHand;
    }

    public void setRightHand(String rightHand) {
        this.rightHand = rightHand;
    }

    @Override
    public String toString() {
        return leftHand + "->" + rightHand;
    }
}

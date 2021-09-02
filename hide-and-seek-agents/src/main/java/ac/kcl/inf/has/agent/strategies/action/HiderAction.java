package ac.kcl.inf.has.agent.strategies.action;

public class HiderAction extends Action{

    private HiderActionType type;

    public void setType(HiderActionType type) {
        this.type = type;
    }

    public HiderActionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "HiderAction{" +
                "type=" + type +
                ", payload=" + getPayload() +
                '}';
    }
}

package ac.kcl.inf.has.agent.strategies.action;

public class SeekerAction extends Action{

    private SeekerActionType type;

    public void setType(SeekerActionType type) {
        this.type = type;
    }

    public SeekerActionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SeekerAction{" +
                "type=" + type +
                ", payload=" + getPayload() +
                '}';
    }
}

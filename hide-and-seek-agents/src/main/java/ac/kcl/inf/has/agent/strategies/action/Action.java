package ac.kcl.inf.has.agent.strategies.action;

public abstract class Action {

    private Object payload;

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }

}

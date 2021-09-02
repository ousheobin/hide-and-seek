package ac.kcl.inf.has.agent.player.hider;

import ac.kcl.inf.has.agent.player.Agent;
import ac.kcl.inf.has.agent.strategies.hider.HidingStrategy;

/**
 * Hider interface
 *
 * @author Shaobin Ou
 *
 */
public interface Hider extends Agent {

    /**
     * Perform the hide task.
     */
    void performHide(HidingStrategy strategy);

    /**
     * Decide hiding at current node or not
     * @return
     */
    boolean hideAtCurrentNode();


    void setHideNumber(int hideNumber);

}

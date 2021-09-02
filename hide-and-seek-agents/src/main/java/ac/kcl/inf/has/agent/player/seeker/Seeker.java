package ac.kcl.inf.has.agent.player.seeker;

import ac.kcl.inf.has.agent.player.Agent;
import ac.kcl.inf.has.agent.strategies.seeker.SeekingStrategy;

/**
 * Seeker interface
 *
 * @author Shaobin Ou
 *
 */
public interface Seeker extends Agent {

    void performSearch(SeekingStrategy strategy);

    boolean discoverAtCurrentNode();

}

package ac.kcl.inf.has.agent.player;

import ac.kcl.inf.has.agent.execution.ExecutionPermits;
import ac.kcl.inf.has.env.controller.GraphController;
import ac.kcl.inf.has.env.graph.Edge;
import ac.kcl.inf.has.env.graph.Vertex;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Agent interface is an abstract of common behaviours of agents.
 *
 * Inspired from the Hands(https://github.com/martinchapman/hands) developed by Dr.Martin Chapman.
 *
 * @author Shaobin Ou
 *
 */
public interface Agent extends Runnable{

    /**
     * Get the graph controller.
     * @return Graph Controller instance
     */
    GraphController getGraphController();

    /**
     * Get the set of visited nodes
     * @return Set of visited nodes
     */
    Set<Vertex> getUniqueVisitedNodeSet();

    /**
     * Get the visited edges set
     * @return Set of visited edges
     */
    Set<Edge> getUniqueVisitedEdgeSet();

    /**
     * Get the hidden locations.
     * It's used to marked the objects agents hide or found.
     * @return Set of hidden vertexs.
     */
    Set<Vertex> getHiddenLocations();

    /**
     * The agent can move or not.
     * It indicates whether the agent can play or not.
     * @return true if able to move.
     */
    boolean canMove();

    /**
     * Notify the agent whether it can move or not.
     * @param move ture if able to move
     */
    void setCanMove(boolean move);

    /**
     * Check whether a round of game is ended.
     * @return true if the round reaches an end.
     */
    boolean roundIsEnded();

    /**
     * Check whether a game is ended.
     * @return true if the game reaches an end.
     */
    boolean gameIsEnded();

    /**
     * The node to be visited for next.
     * @return the vertex
     */
    Vertex nextNode();

    /**
     * The node agent is situating.
     * @return the node
     */
    Vertex currentNode();

    /**
     * Initial for a new round.
     */
    void initForNewRound();

    /**
     * Set the countdown latch of the game controller.
     * @param gameControllerLatch the latch of game controller.
     */
    void setGameControllerLatch(CountDownLatch gameControllerLatch);

    /**
     * Bind a new execution permits.
     * @param executionPermits The permits.
     */
    void bindPermits(ExecutionPermits executionPermits);

    public int getPathLength();

    /**
     * Get the name of the agent.
     * @return
     */
    String getAgentName();

}

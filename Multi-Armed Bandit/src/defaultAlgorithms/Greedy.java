package defaultAlgorithms;

import core.Agent;

import java.util.ArrayList;
import java.util.List;

/**
 * Greedy algorithm.
 *
 * @author Sam Beckmann, Nate Beckemeyer. Thanks to Anton Ridgeway for original implementations.
 */
public class Greedy implements core.IAlgorithm
{
    @Override
    public String getName()
    {
        return "Greedy";
    }

    /**
     * A special case of the eFirst algorithm when epsilon <= totalCost/totalBudget so that
     * each arm can be pulled a maximum of once.
     *
     * @param curAgent        The current agent employing this algorithm.
     * @param inputParameters Null for this algorithm.
     */
    @Override
    public void run(Agent curAgent, List<Double> inputParameters)
    {
        EFirst eFirst = new EFirst();
        inputParameters = new ArrayList<Double>(); // add correct epsilon for Greedy.
        inputParameters.add(curAgent.getTotalCost() / curAgent.getBudget());
        eFirst.run(curAgent, inputParameters);
    }
}
package defaultAlgorithms;

import core.Agent;
import core.Arm;
import core.ArmMemory;
import utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Survival of the Above Average (sOAAv) algorithm.
 *
 * @author Sam Beckmann, Nate Beckemeyer. Thanks to Anton Ridgeway for original implementations.
 */
public class SOAAv implements core.IAlgorithm
{
    private static final boolean debugSOAAv = false;

    @Override
    public String getName()
    {
        return "SOAAv";
    }

    @Override
    public void run(Agent curAgent, List<Double> inputParameters)
    {
        double xValue = inputParameters.get(0);

        // Initialize variables
        Arm[] arms = curAgent.getArms();
        ArmMemory[] memories = curAgent.getMemories();
        ArrayList<Integer> activeArms = new ArrayList<Integer>();
        double passAverageRatio;

        // Add all arms to list to be pulled for first iteration
        for (int i = 0; i < arms.length; i++)
            activeArms.add(i);

        // Loop until budget exhausted
        while (curAgent.getBudget() >= curAgent.getMinCost())
        {
            passAverageRatio = 0;

            List<Integer> indices = new ArrayList<Integer>();
            Utilities.generateIndices(indices, activeArms.size());

            while (!indices.isEmpty())
            {
                int armToPull = activeArms.get(Utilities.randomIndex(indices));

                if (arms[armToPull].getCost() <= curAgent.getBudget())
                {
                    curAgent.pull(armToPull);
                    if (debugSOAAv) System.out.println(Utilities.getPullResult(
                            getName(), armToPull, arms[armToPull], memories[armToPull]));

                }
            }

            for (Integer activeArm : activeArms)
                passAverageRatio += memories[activeArm].getRatio() / arms[activeArm].getCost();

            passAverageRatio = passAverageRatio / activeArms.size();
            activeArms.clear();

            // Update activeArms for next iteration
            for (int i = 0; i < arms.length; i++)
            {
                if (arms[i].getCost() <= curAgent.getBudget()
                        && memories[i].getRatio() >= (1 + xValue) * passAverageRatio)
                {
                    activeArms.add(i);
                }
            }
            if (activeArms.size() == 0)
            {
                activeArms.add(Utilities.getBestArm(curAgent));
            }
        }
        if (debugSOAAv) System.out.println("[" + getName() + "] Budget Exhausted. Trial complete.");
    }
}

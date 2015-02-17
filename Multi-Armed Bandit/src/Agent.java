import java.util.Random;

public class Agent {
    /**
     * The random generator for Agent.
     */
    private Random rnd;

    /**
     * The arms that the agent pulls.
     */
    private static Arm arms[];

    /**
     * The minimum cost to pull an arm.
     */
    private static double minCost;

    /**
     * The total cost to pull all of the arms.
     */
    private static double totalCost = 0;

    /**
     * Has minCost been initialized yet? It only needs to happen once. Optimization variable.
     */
    private static boolean initialized = false;

    /**
     *  The memories that the Agent has of the arm pulls.
     */
    private ArmMemory memories[];

    /**
     * The index of the current best arm in terms of benefit/cost ratio.
     */
    private int bestArm;

    /**
     * The index of the next best arm in terms of benefit/cost ratio, in case the current base drops behind.
     */
    private int nextBestArm;

    /**
     * The total current budget of the agent.
     */
    private int budget;

    /**
     * Sets the minCost and totalCost variables
     */
    public void findCosts() {
        double min =  arms[0].getCost();
        for (Arm current : arms) {
            double  curCost = current.getCost();
            if (curCost < min)
                min = curCost;
            totalCost += curCost;
        }
    }

    public int getFirstOrSecondBest(boolean firstIfTrue) {
        int best = 0;
        int secondBest = 1;

        for (int j = 2; j < memories.length; j++)
        {
            if (memories[best].getCost() > budget && memories[j].getCost() <= budget) // Is it comparably usable?
                best = j;
            else if (memories[secondBest].getCost() > budget && memories[j].getCost() <= budget) // How about this one?
                secondBest = j;

            if ((memories[j].getRatio() > memories[best].getRatio() && memories[j].getCost() <= budget))
            {
                secondBest = best;
                best = j;
            } else if (memories[j].getRatio() > memories[secondBest].getRatio() && memories[j].getCost() <= budget)
            {
                secondBest = j;
            }
        }
        if (firstIfTrue)
            return best;

        return secondBest;
    }

    /**
     * The constructor for the agent. Pass a budget and the arms array.
     */
    public Agent(int initBudget, Arm armRefs[]) {
        budget = initBudget;
        arms = armRefs;
        int count = 0;
        for (Arm current : armRefs)
            memories[count++] = new ArmMemory(current.getCost());

        if (!initialized)
        {
            findCosts();
            initialized = true;
        }
    }


    public void pull(int toPull) {
        // TODO: Constant rewards, data tracking, etc.
        Arm current = arms[toPull];
        if (budget >= current.getCost())
        {
            budget -= current.getCost();
            memories[toPull].addPull(current.getReward());
        }
    }

    /**
     * @return
     *      the budget remaining for the agent
     */
    public int getBudget() {
        return budget;
    }

    /**
     * @return
     *      the array of arms of the agent
     */
    public Arm[] getArms() {
        return arms;
    }

    public double getMinCost() {
        return minCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public ArmMemory[] getMemories() {
        return memories;
    }
}

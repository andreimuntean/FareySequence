import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Computes the kth element of the Farey sequence of a specified order.
 *
 * @author Andrei Muntean
 * @author Miriam Costan
 */
public class Farey
{
    // Counts the number of fractions which are smaller than the specified
    // fraction and which have their denominators smaller than or equal to the
    // specified denominator.
    private static int countFractionsUpTo(int numerator, int denominator, boolean irreducibleOnly)
    {
        int result = 0;

        // fractionCount[index] = the number of fractions with the denominator
        // "index" which are smaller than the specified fraction.
        int[] fractionCount = new int[denominator + 1];

        // The maximum numerator of each fraction with the denominator "i" is
        // "i". Therefore there are Math.floor(i * numerator / denominator)
        // fractions which are smaller than the specified fraction and have
        // the denominator equal to "i".
        // 
        // Example. Given "4/8":
        // i = 2:  1/2 -- fractionCount[2] = 1.
        // i = 3:  1/3 -- fractionCount[3] = 1.
        // i = 4:  1/4 2/4 -- fractionCount[4] = 2.
        // i = 5:  1/5 2/5 -- fractionCount[5] = 2.
        // i = 6:  1/6 2/6 3/6 -- fractionCount[6] = 3.
        // i = 7:  1/7 2/7 3/7 -- fractionCount[7] = 3.
        // i = 8:  1/8 2/8 3/8 4/8 -- fractionCount[8] = 4.
        for (int currentDenominator = 2; currentDenominator <= denominator; ++currentDenominator)
        {
            fractionCount[currentDenominator] = currentDenominator * numerator / denominator;
        }

        // Determines whether the fractions which can be reduced should be forgotten.
        if (irreducibleOnly)
        {
            for (int currentDenominator = 2; currentDenominator <= denominator; ++currentDenominator)
            {
                for (int multiplier = 2 * currentDenominator; multiplier <= denominator; multiplier += currentDenominator)
                {
                    // In fractionCount[multiplier], forgets the fractions from
                    // fractionCount[currentDenominator] since they are repeating
                    // themselves.
                    fractionCount[multiplier] -= fractionCount[currentDenominator];
                }
            }
        }

        for (int currentDenominator = 2; currentDenominator <= denominator; ++currentDenominator)
        {
            result += fractionCount[currentDenominator];
        }

        return result;
    }

    // Gets a nominator from the Farey sequence such that the kth fraction is in the
    // interval [neighbor / order, (neighbor + 1) / order].
    private static int findNeighbor(int k, int order)
    {
        int left = 1;
        int right = order - 1;

        while (left <= right)
        {
            int middle = (left + right) / 2;

            if (k > countFractionsUpTo(middle, order, true))
            {
                left = middle + 1;
            }
            else
            {
                right = middle - 1;
            }
        }

        // "right" is now the "neighbor" described above.
        return right;
    }

    // Computes the kth element of the Farey sequence of a specified order.
    public static Fraction get(int k, int order)
    {
        if (--k == 0)
        {
            return new Fraction(0, 1);
        }

        int neighbor = findNeighbor(k, order);

        // Represents the list of fractions from the interval
        // [neighbor / order, (neighbor + 1) / order].
        TreeSet<Fraction> fractions = new TreeSet<Fraction>();

        // K becomes significantly smaller as it is reduced to the elements
        // from the interval [neighbor / order, (neighbor + 1) / order].
        k -= countFractionsUpTo(neighbor, order, true);

        // Knowing that each fraction has a unique denominator, finds every
        // fraction from the specified interval.
        for (int currentDenominator = 2; currentDenominator <= order; ++currentDenominator)
        {
            int numerator = currentDenominator * (neighbor + 1) / order;

            if (currentDenominator * neighbor < numerator * order)
            {
                // Sorts the fractions and eliminates duplicates by storing
                // them in a tree set.
                fractions.add(new Fraction(numerator, currentDenominator));
            }
        }

        // Gets the kth element from the [neighbor / order, (neighbor + 1) / order]
        // interval of distinct fractions.
        for (Fraction fraction : fractions)
        {
            if (--k == 0)
            {
                return fraction;
            }
        }

        return null;
    }

    // Generates the Farey sequence of a specified order.
    public static ArrayList<Fraction> generate(int order)
    {
        ArrayList<Fraction> sequence = new ArrayList<Fraction>(10 * order);
        Fraction currentElement = new Fraction(0, 1);

        // Adds the first value: 0/1.
        sequence.add(currentElement);

        while (!currentElement.isUnit())
        {
            int nextNominator = 1;
            int nextDenominator = 1;

            for (int denominator = order; denominator >= 1; --denominator)
            {
                int ad = currentElement.getNominator() * denominator;
                
                for (int nominator = 1; nominator <= denominator; ++nominator)
                {
                    int bc = currentElement.getDenominator() * nominator;

                    // Given a/b and c/d two Farey neighbors, ad - bc is -1.
                    if (ad - bc == -1)
                    {
                        // Determines whether the currently stored potential neighbor is greater than
                        // the new neighbor that can be created.
                        if (nextNominator * denominator > nextDenominator * nominator)
                        {
                            // The actual Farey neighbor is the smallest possible neighbor.
                            nextNominator = nominator;
                            nextDenominator = denominator;
                        }
                    }
                }
            }

            sequence.add(currentElement = new Fraction(nextNominator, nextDenominator));
        }

        return sequence;
    }

    // Computes the kth element of the Farey sequence of a specified order;
    public static void main(String[] args) throws IOException
    {
        int order;
        int k;

        if (args.length == 2)
        {
            order = Integer.parseInt(args[0]);
            k = Integer.parseInt(args[1]);
        }
        else
        {
            try (Scanner scanner = new Scanner(System.in))
            {
                order = scanner.nextInt();
                k = scanner.nextInt();
            }
        }

        // Outputs the kth element of the Farey sequence of the specified order.
        System.out.printf("%s", get(k, order));
    }
}
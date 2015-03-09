import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Computes the kth element of the Farey sequence of order n.
 *
 * @author Andrei Muntean
 */
public class Farey
{
	// Generates the Farey sequence of the specified order.
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

	// Computes the kth element of the Farey sequence of order n.
	// To be used if the other elements do not need to be stored.
	public static Fraction get(int k, int order)
	{
		int currentNominator = 0;
		int currentDenominator = 1;

		while (k-- > 0)
		{
			int nextNominator = 1;
			int nextDenominator = 1;

			for (int denominator = order; denominator >= 1; --denominator)
			{
				int ad = currentNominator * denominator;
				
				for (int nominator = 1; nominator <= denominator; ++nominator)
				{
					int bc = currentDenominator * nominator;

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

			currentNominator = nextNominator;
			currentDenominator = nextDenominator;
		}

		return new Fraction(currentNominator, currentDenominator);
	}

	// Computes the kth element of the Farey sequence of order n.
	public static void main(String[] args) throws IOException
	{
		int n;
		int k;
		int index = 0;

		try (Scanner scanner = new Scanner(new FileReader("farey.in")))
		{
			n = scanner.nextInt();
			k = scanner.nextInt();
		}

		Fraction kthElement = get(k, n);

		try (PrintWriter printWriter = new PrintWriter(new FileWriter("farey.out")))
		{
			printWriter.printf("%s ", kthElement);
		}
	}
}
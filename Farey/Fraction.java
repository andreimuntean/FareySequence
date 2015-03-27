/**
 * Represents a fraction.
 *
 * @author Andrei Muntean
 */
public class Fraction implements Comparable<Fraction>
{
    private final int nominator;
    private final int denominator;

    /**
     * Constructs a fraction.
     *
     * @param nominator The nominator.
     * @param denominator The denominator.
     */
    public Fraction(int nominator, int denominator) throws IllegalArgumentException
    {
        if (denominator == 0)
        {
            throw new IllegalArgumentException("Denominator cannot be zero.");
        }

        this.nominator = nominator;
        this.denominator = denominator;
    }

    /**
     * Gets the nominator.
     *
     * @return The nominator.
     */
    public int getNominator()
    {
        return nominator;
    }

    /**
     * Gets the denominator.
     *
     * @return The denominator.
     */
    public int getDenominator()
    {
        return denominator;
    }

    /**
     * Determines whether this fraction is equal to 1.
     *
     * @return True if this fraction is equal to 1.
     */
    public boolean isUnit()
    {
        return nominator == denominator;
    }

    /**
     * Compares this fraction with the specified fraction.
     *
     * @param A fraction.
     *
     * @return A negative integer, zero or a positive integer if this fraction is
     * less than, equal to or greater than the specified fraction.
     */
    public int compareTo(Fraction fraction)
    {
        return compareTo(fraction.getNominator(), fraction.getDenominator());
    }

    /**
     * Compares this fraction with the specified integers which represent a fraction.
     *
     * @param The nominator of a fraction.
     * @param The denominator of a fraction.
     *
     * @return A negative integer, zero or a positive integer if this fraction is
     * less than, equal to or greater than the specified fraction.
     */
    public int compareTo(int nominator, int denominator)
    {
        // (a/b <relation> c/d) iff (ad <relation> bc)
        long ad = this.nominator * denominator;
        long bc = this.denominator * nominator;

        if (ad < bc)
        {
            return -1;
        }
        else if (ad == bc)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    public boolean equals(int nominator, int denominator)
    {
        return this.nominator == nominator && this.denominator == denominator;
    }

    @Override
    public boolean equals(Object obj)
    {
        Fraction fraction = (Fraction)obj;

        return nominator == fraction.getNominator() && denominator == fraction.getDenominator();
    }

    @Override
    public String toString()
    {
        return nominator + "/" + denominator;
    }
}
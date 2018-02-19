package selenium.boot.core.matchers;


import org.hamcrest.Factory;
import org.hamcrest.Matcher;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
class OrderingComparison
{
    //region initialization and constructors section

    private OrderingComparison()
    {
        super();
    }

    //endregion

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * equal to the specified value, as reported by the <code>compareTo</code> method of the
     * <b>examined</b> object.
     * For example:
     * <pre>assertThat(1, comparesEqualTo(1))</pre>
     *
     * @param value the value which, when passed to the compareTo method of the examined object, should return zero
     */
    @Factory
    public static <T extends Comparable<T>> Matcher<T> comparesEqualTo( T value )
    {
        return ComparatorMatcherBuilder.<T> usingNaturalOrdering().comparesEqualTo( value );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * greater than the specified value, as reported by the <code>compareTo</code> method of the
     * <b>examined</b> object.
     * For example:
     * <pre>assertThat(2, greaterThan(1))</pre>
     *
     * @param value the value which, when passed to the compareTo method of the examined object, should return greater
     *              than zero
     */
    @Factory
    public static <T extends Comparable<T>> Matcher<T> greaterThan( T value )
    {
        return ComparatorMatcherBuilder.<T> usingNaturalOrdering().greaterThan( value );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * greater than or equal to the specified value, as reported by the <code>compareTo</code> method
     * of the <b>examined</b> object.
     * For example:
     * <pre>assertThat(1, greaterThanOrEqualTo(1))</pre>
     *
     * @param value the value which, when passed to the compareTo method of the examined object, should return greater
     *              than or equal to zero
     */
    @Factory
    public static <T extends Comparable<T>> Matcher<T> greaterThanOrEqualTo( T value )
    {
        return ComparatorMatcherBuilder.<T> usingNaturalOrdering().greaterThanOrEqualTo( value );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * less than the specified value, as reported by the <code>compareTo</code> method of the
     * <b>examined</b> object.
     * For example:
     * <pre>assertThat(1, lessThan(2))</pre>
     *
     * @param value the value which, when passed to the compareTo method of the examined object, should return less
     *              than zero
     */
    @Factory
    public static <T extends Comparable<T>> Matcher<T> lessThan( T value )
    {
        return ComparatorMatcherBuilder.<T> usingNaturalOrdering().lessThan( value );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * less than or equal to the specified value, as reported by the <code>compareTo</code> method
     * of the <b>examined</b> object.
     * For example:
     * <pre>{@code assertThat(1, lessThanOrEqualTo(1))}</pre>
     *
     * @param value the value which, when passed to the compareTo method of the examined object, should return less
     *              than or equal to zero
     */
    @Factory
    public static <T extends Comparable<T>> Matcher<T> lessThanOrEqualTo( T value )
    {
        return ComparatorMatcherBuilder.<T> usingNaturalOrdering().lessThanOrEqualTo( value );
    }
}

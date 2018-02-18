package selenium.boot.core.matchers;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
class IsNaN extends TypeSafeMatcher<Double>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private IsNaN()
    {
        super();
    }

    //endregion

    /**
     * Creates a matcher of {@link Double}s that matches when an examined double is not a number.
     * For example:
     * <pre>assertThat(Double.NaN, is(notANumber()))</pre>
     */
    static Matcher<Double> notANumber()
    {
        return new IsNaN();
    }

    @Override
    public boolean matchesSafely( Double item )
    {
        return Double.isNaN( item );
    }

    @Override
    public void describeMismatchSafely( Double item, Description mismatchDescription )
    {
        mismatchDescription.appendText( "was " ).appendValue( item );
    }

    @Override
    public void describeTo( Description description )
    {
        description.appendText( "a double value of NaN" );
    }
}

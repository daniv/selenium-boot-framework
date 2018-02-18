package selenium.boot.core.matchers;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;



/**
 * Matcher tha can have it's describing behaviour overridden programmatically.
 * source: shiver.me.timbers.matchers.DescribingMatcher
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public abstract class DescribingMatcher<T> extends TypeSafeMatcher<T>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private DescribeTo describeTo;

    private DescribeMismatch describeMismatch;

    DescribingMatcher()
    {
        super();
    }

    //endregion

    @Override
    public final void describeTo( Description description )
    {
        describeTo.describeTo( description );
    }

    @Override
    protected final void describeMismatchSafely( T item, Description mismatchDescription )
    {
        describeMismatch.describeMismatch( mismatchDescription );
    }

    protected final void assignDescribers( DescribingMatcher.DescribeTo describeTo,
                                           DescribingMatcher.DescribeMismatch describeMismatch )
    {
        this.describeTo = describeTo;
        this.describeMismatch = describeMismatch;
    }


    protected interface DescribeTo
    {
        void describeTo( Description description );
    }


    protected interface DescribeMismatch
    {
        void describeMismatch( Description description );
    }
}

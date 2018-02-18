package selenium.boot.core.matchers;


import com.google.common.base.Objects;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
class BetweenMatcher<T extends Comparable<T>> extends TypeSafeMatcher<T>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final T low;

    private final T high;

    private BetweenMatcher( T low, T high )
    {
        this.low = low;
        this.high = high;
    }


    //endregion

    @Factory
    static <T extends Comparable<T>> Matcher<T> between( T low, T high )
    {
        return new BetweenMatcher<>( low, high );
    }

    @Override
    protected boolean matchesSafely( T number )
    {
        return Matchers.greaterThanOrEqualTo( this.low ).matches( number ) && Matchers.lessThanOrEqualTo( this.high ).matches( number );
    }

    @Override
    public void describeTo( Description description )
    {
        description.appendText( "value between " ).appendValue( this.low ).appendText( " and " ).appendValue( this.high ).appendText( " inclusive." );
    }


    @Override
    public int hashCode()
    {
        return Objects.hashCode( this );
    }

    @Override
    public boolean equals( Object object )
    {
        return Objects.equal(  this, object  );
    }
}

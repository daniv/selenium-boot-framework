package selenium.boot.core.matchers;


import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinResults;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import selenium.boot.utils.text.StringUtils;



/**
 * Is the value equal to another value, as tested by the {@link Object#equals} invokedMethod?
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
class ExtendedIsEqualMatcher<T> extends IsEqual<T>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final Object expectedValue;

    private static final LevenshteinDetailedDistance detailedDistance = new LevenshteinDetailedDistance();

    private ExtendedIsEqualMatcher( T equalArg )
    {
        super( equalArg );
        this.expectedValue = equalArg;
    }

    //endregion



    @Override
    public void describeMismatch( @Nullable Object item, Description mismatchDescription )
    {
        Class<?> cls = getClassType();
        if( String.class.isAssignableFrom( cls ) )
        {
            if( item == null )
            {
                super.describeMismatch( item, mismatchDescription );
            }
            else
            {
                mismatchDescription.appendText( "was " ).appendValue( item );
                LevenshteinResults results = detailedDistance.apply( item.toString(), expectedValue.toString() );
                mismatchDescription.appendText( rightPad( "deletes" ) ).appendValue( results.getDeleteCount() );
                mismatchDescription.appendText( rightPad( "inserts" ) ).appendValue( results.getInsertCount() );
                mismatchDescription.appendText( rightPad( "substitutions" ) ).appendValue( results.getSubstituteCount() );
                mismatchDescription.appendText( rightPad( "Levenshtein distance" ) ).appendValue( results.getDistance() );
            }
        }
        else
        {
            super.describeMismatch( item, mismatchDescription );
        }
    }

    @SuppressWarnings( "unchecked" )
    private Class<? extends T> getClassType()
    {
        return ( Class<? extends T> ) ResolvableType.forInstance( expectedValue ).resolve();
    }

    private String rightPad( String desc )
    {
        String out =  StringUtils.padEnd( desc , 18, ' ' );
        return "\n" +  out + ": ";
    }

    /**
     * Creates a matcher that matches when the examined object is logically equal to the specified
     * {@code operand}, as determined by calling the {@link Object#equals} method on
     * the <b>examined</b> object.
     * <p>
     * If the specified operand is {@code null} then the created matcher will only match if
     * the examined object's {@code equals} method returns {@code true} when passed a
     * {@code null} (which would be a violation of the {@code equals} contract), unless the
     * examined object itself is {@code null}, in which case the matcher will return a positive match.
     * <p>
     * The created matcher provides a special behaviour when examining {@code Array}s, whereby
     * it will match if both the operand and the examined object are arrays of the same length and
     * contain items that are equal to each other (according to the above rules) <b>in the same indexes</b>
     * For example:
     * <pre>
     * assertThat("foo", equalTo("foo"));
     * assertThat(new String[] {"foo", "bar"}, equalTo(new String[] {"foo", "bar"}));
     * </pre>
     */
    @Factory
    public static <T> Matcher<T> equalTo( T operand )
    {
        return new ExtendedIsEqualMatcher<>( operand );
    }

    /**
     * Creates an {@link org.hamcrest.core.IsEqual} matcher that does not enforce the values being
     * compared to be of the same static type.
     */
    @Factory
    public static Matcher<Object> equalToObject( Object operand )
    {
        return new ExtendedIsEqualMatcher<>( operand );
    }
}

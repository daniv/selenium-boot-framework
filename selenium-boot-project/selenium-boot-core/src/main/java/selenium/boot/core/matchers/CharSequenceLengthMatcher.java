package selenium.boot.core.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.equalTo;



/**
 * Tests if the argument is a {@link CharSequence} that matches a specified length.
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
class CharSequenceLengthMatcher extends TypeSafeMatcher<CharSequence>
{

    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final Matcher<? super Integer> lengthMatcher;

    CharSequenceLengthMatcher( Matcher<? super Integer> lengthMatcher )
    {
        this.lengthMatcher = lengthMatcher;
    }

    //endregion


    @Override
    protected boolean matchesSafely( CharSequence text )
    {
        return lengthMatcher.matches( text != null ? text.length() : 0 );
    }

    @Override
    public void describeMismatchSafely( CharSequence item, Description mismatchDescription )
    {
        mismatchDescription.appendText( "was length \"" ).appendText( item == null ? null : String.valueOf( item.length() ) ).appendText( "\"" );
    }

    @Override
    public void describeTo( Description description )
    {
        description.appendText( "has string length " ).appendDescriptionOf( lengthMatcher );
    }

    /**
     * Creates a matcher of {@link CharSequence} that matches when the examined
     * {@link CharSequence} has the specified length.
     *
     * For example:
     * <pre>assertThat("myStringOfNote", length(10))</pre>
     *
     * @param length the length that the returned matcher will expect any examined string to have
     */
    @Factory
    public static CharSequenceLengthMatcher length( int length )
    {
        return length( equalTo( length ) );
    }

    /**
     * Creates a matcher of {@link CharSequence} that matches when the examined
     * {@link CharSequence} has the specified length, determined by the specified matcher.
     *
     * For example:
     * <pre>assertThat("myStringOfNote", length(not(equalTo(10))))</pre>
     * <pre>assertThat("myStringOfNote", length(lessThan(15))))</pre>
     *
     * @param lengthMatcher the matcher to apply to the examined {@link CharSequence}
     */
    @Factory
    public static CharSequenceLengthMatcher length( Matcher<? super Integer> lengthMatcher )
    {
        return new CharSequenceLengthMatcher( lengthMatcher );
    }

}

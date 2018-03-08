package selenium.boot.core.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;



/**
 * Matches blank Strings (and null).
 * 
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
class IsBlankString extends TypeSafeMatcher<String>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private static final IsBlankString BLANK_INSTANCE = new IsBlankString();

    private static final Pattern REGEX_WHITESPACE = Pattern.compile( "\\s*");

    @SuppressWarnings( "unchecked" )
    private static final Matcher<String> NULL_OR_BLANK_INSTANCE = CoreMatchers.anyOf( CoreMatchers.nullValue(), BLANK_INSTANCE );

    private IsBlankString()
    {
        super();
    }

    //endregion

    /**
     * Creates a matcher of {@link String} that matches when the examined string contains
     * zero or more whitespace characters and nothing else.
     * For example:
     * <pre>assertThat("  ", is(blankString()))</pre>
     */
    public static Matcher<String> blankString()
    {
        return BLANK_INSTANCE;
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string is {@code null}, or
     * contains zero or more whitespace characters and nothing else.
     * For example:
     * <pre>assertThat(((String)null), is(blankOrNullString()))</pre>
     */
    @Factory
    static Matcher<String> blankOrNullString()
    {
        return NULL_OR_BLANK_INSTANCE;
    }

    @Override
    public boolean matchesSafely( String item )
    {
        return REGEX_WHITESPACE.matcher( item ).matches();
    }

    @Override
    public void describeTo( Description description )
    {
        description.appendText( "a blank string" );
    }
}

package selenium.boot.core.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
class MatchesPattern extends TypeSafeMatcher<String>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final Pattern pattern;

    public MatchesPattern( Pattern pattern )
    {
        this.pattern = pattern;
    }

    //endregion

    /**
     * Creates a matcher of {@link java.lang.String} that matches when the examined string
     * exactly matches the given {@link java.util.regex.Pattern}.
     */
    @Factory
    static Matcher<String> matchesPattern( Pattern pattern )
    {
        return new MatchesPattern( pattern );
    }

    /**
     * Creates a matcher of {@link java.lang.String} that matches when the examined string
     * exactly matches the given regular expression, treated as a {@link java.util.regex.Pattern}.
     */
    @Factory
    static Matcher<String> matchesPattern( String regex )
    {
        return new MatchesPattern( Pattern.compile( regex ) );
    }

    @Override
    protected boolean matchesSafely( String item )
    {
        return pattern.matcher( item ).matches();
    }

    @Override
    public void describeTo( Description description )
    {
        description.appendText( "a string matching the pattern '" + pattern + "'" );
    }
}

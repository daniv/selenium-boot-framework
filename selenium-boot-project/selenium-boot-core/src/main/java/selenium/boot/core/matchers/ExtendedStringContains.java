package selenium.boot.core.matchers;


import org.hamcrest.Matcher;



/**
 *  Tests if the argument is a string that contains a substring.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public class ExtendedStringContains extends ExtendedSubstringMatcher
{
    //region initialization and constructors section

    public ExtendedStringContains( boolean ignoringCase, String substring )
    {
        super( "containing", ignoringCase, substring );
    }

    //endregion

    /**
     * Creates a matcher that matches if the examined {@link String} contains the specified {@link String} anywhere.
     * For example:
     * <pre>assertThat("myStringOfNote", containsString("ring"))</pre>
     *
     * @param substring the substring that the returned matcher will expect to find within any examined string
     */
    public static Matcher<String> containsString( String substring )
    {
        return new ExtendedStringContains( false, substring );
    }

    /**
     * Creates a matcher that matches if the examined {@link String} contains the specified {@link String} anywhere, ignoring case.
     * For example:
     * <pre>assertThat("myStringOfNote", containsStringIgnoringCase("Ring"))</pre>
     *
     * @param substring the substring that the returned matcher will expect to find within any examined string
     */
    public static Matcher<String> containsStringIgnoringCase( String substring )
    {
        return new ExtendedStringContains( true, substring );
    }

    @Override
    protected boolean evalSubstringOf( String s )
    {
        return converted( s ).contains( converted( substring ) );
    }

}

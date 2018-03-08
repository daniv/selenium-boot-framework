package selenium.boot.core.matchers;


import org.hamcrest.Matcher;



/**
 *  Tests if the argument is a string that contains a substring.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public class ExtendedStringEndsWith extends ExtendedSubstringMatcher
{
    //region initialization and constructors section

    public ExtendedStringEndsWith( boolean ignoringCase, String substring )
    {
        super( "ending with", ignoringCase, substring );
    }


    //endregion

    /**
     * Creates a matcher that matches if the examined {@link String} ends with the specified
     * {@link String}.
     * For example:
     * <pre>assertThat("myStringOfNote", endsWith("Note"))</pre>
     *
     * @param suffix the substring that the returned matcher will expect at the end of any examined string
     */
    public static Matcher<String> endsWith( String suffix )
    {
        return new ExtendedStringEndsWith( false, suffix );
    }

    /**
     * Creates a matcher that matches if the examined {@link String} ends with the specified
     * {@link String}, ignoring case.
     * For example:
     * <pre>assertThat("myStringOfNote", endsWithIgnoringCase("note"))</pre>
     *
     * @param suffix the substring that the returned matcher will expect at the end of any examined string
     */
    public static Matcher<String> endsWithIgnoringCase( String suffix )
    {
        return new ExtendedStringEndsWith( true, suffix );
    }

    @Override
    protected boolean evalSubstringOf( String s )
    {
        return converted( s ).endsWith( converted( substring ) );
    }

}

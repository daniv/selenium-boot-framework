package selenium.boot.core.matchers;


import org.hamcrest.Factory;
import org.hamcrest.Matcher;



/**
 * Extends the existing {@link org.hamcrest.core.StringStartsWith}  to suppoort
 * {@linkplain #startsWithIgnoringCase}
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
class ExtendedStringStartsWith extends ExtendedSubstringMatcher
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private ExtendedStringStartsWith( boolean ignoringCase, String substring )
    {
        super( "starting with", ignoringCase, substring );
    }

    //endregion

    /**
     * <p>
     * Creates a matcher that matches if the examined {@link String} starts with the specified
     * {@link String}.
     * </p>
     * For example:
     * <pre>assertThat("myStringOfNote", startsWith("my"))</pre>
     *
     * @param prefix the substring that the returned matcher will expect at the start of any examined string
     */
    @Factory
    static Matcher<String> startsWith( String prefix )
    {
        return new ExtendedStringStartsWith( false, prefix );
    }

    /**
     * <p>
     * Creates a matcher that matches if the examined {@link String} starts with the specified
     * {@link String}, ignoring case
     * </p>
     * For example:
     * <pre>assertThat("myStringOfNote", startsWithIgnoringCase("My"))</pre>
     *
     * @param prefix the substring that the returned matcher will expect at the start of any examined string
     */
    @Factory
    static Matcher<String> startsWithIgnoringCase( String prefix )
    {
        return new ExtendedStringStartsWith( true, prefix );
    }

    @Override
    protected boolean evalSubstringOf( String s )
    {
        return converted( s ).startsWith( converted( substring ) );
    }

}

package selenium.boot.core.matchers;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */

abstract class ExtendedSubstringMatcher extends TypeSafeMatcher<String>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final String relationship;

    private final boolean ignoringCase;

    protected final String substring;

    protected ExtendedSubstringMatcher( String relationship, boolean ignoringCase, String substring )
    {
        this.relationship = relationship;
        this.ignoringCase = ignoringCase;
        this.substring = substring;
        if( null == substring )
        {
            throw new IllegalArgumentException( "missing substring" );
        }
    }

    //endregion

    @Override
    public boolean matchesSafely( String item )
    {
        return evalSubstringOf( ignoringCase ? item.toLowerCase() : item );
    }

    protected abstract boolean evalSubstringOf( String string );

    @Override
    public void describeMismatchSafely( String item, Description mismatchDescription )
    {
        mismatchDescription.appendText( "was \"" ).appendText( item ).appendText( "\"" );
    }

    @Override
    public void describeTo( Description description )
    {
        description.appendText( "a string " )
                .appendText( relationship )
                .appendText( " " )
                .appendValue( substring );
        if( ignoringCase )
        {
            description.appendText( " ignoring case" );
        }
    }

    protected String converted( String arg ) { return ignoringCase ? arg.toLowerCase() : arg; }

}

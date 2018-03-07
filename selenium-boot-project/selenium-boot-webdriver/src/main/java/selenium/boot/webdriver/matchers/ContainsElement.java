package selenium.boot.webdriver.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
class ContainsElement extends TypeSafeMatcher<SearchContext>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private By by;

    protected ContainsElement( By by )
    {
        this.by = by;
    }

    //endregion

    @Factory
    static Matcher<SearchContext> canFindElement( By by )
    {
        return new ContainsElement( by );
    }

    @Override
    protected boolean matchesSafely( SearchContext wd )
    {
        try
        {
            wd.findElement( by );
            return true;
        }
        catch( NoSuchElementException e )
        {
            return false;
        }
    }

    @Override
    protected void describeMismatchSafely( SearchContext wd, Description mismatchDescription )
    {
        mismatchDescription.appendText( "no any element with selector " ).appendValue( by ).appendText( " found" );
    }

    @Override
    public void describeTo( Description description )
    {
        description.appendText( "element on page " ).appendValue( by );
    }
}

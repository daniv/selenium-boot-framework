package selenium.boot.webdriver.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebDriver;
import selenium.boot.framework.matchers.StringMatchers;



/**
 * Matcher for matching title content within {@link org.openqa.selenium.WebElement}s.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see org.openqa.selenium.WebDriver#getTitle() 
 * @since 1.0
 */
public class HasTitleMatcher extends TypeSafeMatcher<WebDriver>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private Matcher<String> matcher;

    protected HasTitleMatcher( Matcher<String> matcher )
    {
        this.matcher = matcher;
    }


    //endregion

    @Factory
    static Matcher<WebDriver> titleOnCurrentPageContains( String text )
    {
        return titleOnCurrentPage( StringMatchers.containsString( text ) );
    }

    @Factory
    static Matcher<WebDriver> titleOnCurrentPage( Matcher<String> text )
    {
        return new HasTitleMatcher( text );
    }

    @Override
    protected boolean matchesSafely( WebDriver wd )
    {
        return matcher.matches( wd.getTitle() );
    }

    @Override
    protected void describeMismatchSafely( WebDriver item,
                                           Description mismatchDescription )
    {
        matcher.describeMismatch( item.getTitle(), mismatchDescription );
    }

    @Override
    public void describeTo( Description description )
    {
        description.appendText( "webdriver has title " ).appendDescriptionOf( matcher );
    }
}

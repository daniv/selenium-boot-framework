package selenium.boot.webdriver.matchers;


import com.google.common.collect.Lists;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.List;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
class HasCookieMatcher extends TypeSafeMatcher<WebDriver>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private String name;

    HasCookieMatcher( String name )
    {
        this.name = name;
    }

    //endregion

    @Factory
    static Matcher<WebDriver> hasCookie( String name )
    {
        return new HasCookieMatcher( name );
    }

    @Override
    protected boolean matchesSafely( WebDriver wDriver )
    {
        Cookie cookie = wDriver.manage().getCookieNamed( name );
        return cookie != null;
    }

    @Override
    protected void describeMismatchSafely( WebDriver item, Description mismatchDescription )
    {
        mismatchDescription.appendText( "was only " );
        List<String> cookNames = Lists.newLinkedList();
        for( Cookie cookie : item.manage().getCookies() )
        {
            cookNames.add( cookie.getName() );
        }
        mismatchDescription.appendValueList( "[", ", ", "]", cookNames );
    }

    @Override
    public void describeTo( Description description )
    {
        description.appendText( "cookie named " ).appendValue( name );
    }
}

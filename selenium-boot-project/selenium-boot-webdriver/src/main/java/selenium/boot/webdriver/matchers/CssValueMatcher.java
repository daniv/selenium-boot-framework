package selenium.boot.webdriver.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selenium.boot.framework.matchers.CoreMatchers;



/**
 * source:
 * <a
 * href='https://github.com/yandex-qatools/matchers-java/blob/master/webdriver-matchers/src/main/java/ru/yandex/qatools/matchers/webdriver/CssValueMatcher.java'>
 * Yandex CssValueMatcher</a>
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
class CssValueMatcher extends TypeSafeMatcher<WebElement>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger( CssValueMatcher.class );

    private final String name;

    private final Matcher<String> matcher;

    CssValueMatcher( String name, Matcher<String> matcher )
    {
        this.matcher = matcher;
        this.name = name;
    }

    //endregion

    @Factory
    static CssValueMatcher cssValue( final String name, final String value )
    {
        return cssValue( name, CoreMatchers.is( value ) );
    }

    @Factory
    static CssValueMatcher css( final String name, final Matcher<String> valueMatcher )
    {
        return cssValue( name, valueMatcher );
    }

    @Factory
    static CssValueMatcher cssValue( final String name, final Matcher<String> valueMatcher )
    {
        return new CssValueMatcher( name, valueMatcher );
    }

    @Factory
    static CssValueMatcher css( final String name, final String value )
    {
        return css( name, CoreMatchers.is( value ) );
    }

    @Override
    public boolean matchesSafely( WebElement item )
    {
        return matcher.matches( item.getCssValue( name ) );
    }

    @Override
    protected void describeMismatchSafely( WebElement item, Description mismatchDescription )
    {
        mismatchDescription.appendValue( item )
                .appendText( " css property " )
                .appendValue( name )
                .appendText( " is " )
                .appendValue( item.getCssValue( name ) );
    }

    public void describeTo( Description description )
    {
        description.appendText( "element css property " )
                .appendValue( name )
                .appendText( " is " )
                .appendDescriptionOf( matcher );
    }
}

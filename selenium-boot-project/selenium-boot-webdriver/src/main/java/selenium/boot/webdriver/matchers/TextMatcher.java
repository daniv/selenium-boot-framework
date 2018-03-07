package selenium.boot.webdriver.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.boot.framework.matchers.CoreMatchers;
import selenium.boot.utils.SeleniumBootStringUtils;



/**
 * source:
 * <a
 * href='https://github.com/yandex-qatools/matchers-java/blob/master/webdriver-matchers/src/main/java/ru/yandex/qatools/matchers/webdriver/TextMatcher.java'>
 * Yandex TextMatcher</a>
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
class TextMatcher extends TypeSafeMatcher<SearchContext>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final Matcher<String> textMatcher;

    TextMatcher( Matcher<String> textMatcher )
    {
        this.textMatcher = textMatcher;
    }

    //endregion

    @Factory
    public static TextMatcher text( String value )
    {
        return text( CoreMatchers.is( value ) );
    }

    @Factory
    public static TextMatcher text( Matcher<String> valueMatcher )
    {
        return new TextMatcher( valueMatcher );
    }

    @Factory
    static TextMatcher pageSource( String value )
    {
        return text( CoreMatchers.is( value ) );
    }

    @Factory
    static TextMatcher pageSource( Matcher<String> valueMatcher )
    {
        return new TextMatcher( valueMatcher );
    }

    @Override
    public boolean matchesSafely( SearchContext item )
    {
        if( item instanceof WebElement )
        {
            return textMatcher.matches( ( ( WebElement ) item ).getText() );
        }
        else
        {
            return textMatcher.matches( ( ( WebDriver ) item ).getPageSource() );
        }
    }

    @Override
    protected void describeMismatchSafely( SearchContext item, Description mismatchDescription )
    {
        mismatchDescription.appendValue( item )
                .appendText( " text is " );
        if( item instanceof WebElement )
        {
            mismatchDescription.appendValue( ( ( WebElement ) item ).getText() );
        }
        else
        {
            String str = ( ( WebDriver ) item ).getPageSource();
            mismatchDescription.appendValue( SeleniumBootStringUtils.abbreviate( str, 50 ) );
        }
    }

    public void describeTo( Description description )
    {
        description.appendText( "element/page text/source " )
                .appendDescriptionOf( textMatcher );
    }

}

package selenium.boot.webdriver.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;



/**
 * source:
 * <a
 * href='https://github.com/yandex-qatools/matchers-java/blob/master/webdriver-matchers/src/main/java/ru/yandex/qatools/matchers/webdriver/TagNameMatcher.java'>
 * Yandex TagNameMatcher</a>
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
class ExistMatcher extends TypeSafeMatcher<WebElement>
{
    @Factory
    static ExistMatcher exists()
    {
        return new ExistMatcher();
    }

    @Override
    protected boolean matchesSafely( WebElement element )
    {
        try
        {
            element.findElement( By.xpath( "self::*" ) );
        }
        catch( WebDriverException e )
        {
            return false;
        }
        return true;
    }

    @Override
    public void describeMismatchSafely( WebElement element, Description mismatchDescription )
    {
        mismatchDescription.appendValue( element )
                .appendText( " not existing on page" );
    }

    public void describeTo( Description description )
    {
        description.appendText( "element existing on page" );
    }

}

package selenium.boot.webdriver.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;



/**
 * Indicates whether {@link org.openqa.selenium.WebElement} is displayed or not.
 *
 * source:
 * <a
 * href='https://github.com/yandex-qatools/matchers-java/blob/master/webdriver-matchers/src/main/java/ru/yandex/qatools/matchers/webdriver/TagNameMatcher.java'>
 * Yandex TagNameMatcher</a>
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see org.openqa.selenium.WebElement#isDisplayed()
 * @since 1.0
 */
class DisplayedMatcher extends TypeSafeMatcher<WebElement>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------


    //endregion

    @Factory
    static DisplayedMatcher displayed()
    {
        return new DisplayedMatcher();
    }

    @Override
    protected boolean matchesSafely( WebElement element )
    {
        try
        {
            return element.isDisplayed();
        }
        catch( WebDriverException e )
        {
            return false;
        }
    }

    @Override
    public void describeMismatchSafely( WebElement element, Description mismatchDescription )
    {
        mismatchDescription.appendValue( element )
                .appendText( " is not displayed on page" );
    }

    public void describeTo( Description description )
    {
        description.appendText( "element is displayed on page" );
    }
}

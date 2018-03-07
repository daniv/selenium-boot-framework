package selenium.boot.webdriver.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebElement;



/**
 * Indicates whether {@link org.openqa.selenium.WebElement} is enabled or not
 *
 * source:
 * <a
 * href='https://github.com/yandex-qatools/matchers-java/blob/master/webdriver-matchers/src/main/java/ru/yandex/qatools/matchers/webdriver/TagNameMatcher.java'>
 * Yandex TagNameMatcher</a>
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see org.openqa.selenium.WebElement#isEnabled()
 * @since 1.0
 */
class EnabledMatcher extends TypeSafeMatcher<WebElement>
{
    @Factory
    static Matcher<WebElement> enabled()
    {
        return new EnabledMatcher();
    }

    @Override
    protected boolean matchesSafely( WebElement element )
    {
        return element.isEnabled();
    }

    @Override
    public void describeMismatchSafely( WebElement element, Description mismatchDescription )
    {
        mismatchDescription.appendValue( element )
                .appendText( " is not enabled on page" );
    }

    public void describeTo( Description description )
    {
        description.appendText( "element is enabled on page" );
    }
}

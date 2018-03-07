package selenium.boot.webdriver.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebElement;



/**
 * Indicates whether {@link org.openqa.selenium.WebElement} is selected or not.
 *
 * source:
 * <a
 * href='https://github.com/yandex-qatools/matchers-java/blob/master/webdriver-matchers/src/main/java/ru/yandex/qatools/matchers/webdriver/SelectedMatcher.java'>
 * Yandex SelectedMatcher</a>
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see org.openqa.selenium.WebElement#isSelected()
 * @since 1.0
 */
class SelectedMatcher extends TypeSafeMatcher<WebElement>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    //endregion

    @Factory
    static SelectedMatcher selected()
    {
        return new SelectedMatcher();
    }

    @Override
    protected boolean matchesSafely( WebElement element )
    {
        return element.isSelected();
    }

    @Override
    public void describeMismatchSafely( WebElement element, Description mismatchDescription )
    {
        mismatchDescription.appendValue( element )
                .appendText( " is not selected on page" );
    }

    public void describeTo( Description description )
    {
        description.appendText( "element is selected on page" );
    }

}

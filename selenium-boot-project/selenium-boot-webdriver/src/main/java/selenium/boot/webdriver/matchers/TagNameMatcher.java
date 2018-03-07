package selenium.boot.webdriver.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebElement;
import selenium.boot.core.matchers.CoreMatchers;



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
class TagNameMatcher extends TypeSafeMatcher<WebElement>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final Matcher<String> textMatcher;

    TagNameMatcher( Matcher<String> textMatcher )
    {
        this.textMatcher = textMatcher;
    }

    //endregion

    @Factory
    public static TagNameMatcher tagName( String value )
    {
        return tagName( CoreMatchers.is( value ) );
    }

    @Factory
    public static TagNameMatcher tagName( Matcher<String> valueMatcher )
    {
        return new TagNameMatcher( valueMatcher );
    }

    @Override
    public boolean matchesSafely( WebElement item )
    {
        return textMatcher.matches( item.getTagName() );
    }

    @Override
    protected void describeMismatchSafely( WebElement item, Description mismatchDescription )
    {
        mismatchDescription.appendValue( item )
                .appendText( " tag name is not " )
                .appendValue( item.getTagName() );
    }

    public void describeTo( Description description )
    {
        description.appendText( "element tag name is " )
                .appendDescriptionOf( textMatcher );
    }

}

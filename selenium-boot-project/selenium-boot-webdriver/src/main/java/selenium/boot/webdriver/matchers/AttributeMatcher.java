package selenium.boot.webdriver.matchers;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebElement;
import selenium.boot.framework.matchers.CoreMatchers;



/**
 * Matcher for matching attribute values of {@link org.openqa.selenium.WebElement}s with specified matcher.
 * source:
 * <a
 * href='https://github.com/yandex-qatools/matchers-java/blob/master/webdriver-matchers/src/main/java/ru/yandex/qatools/matchers/webdriver/AttributeMatcher.java'>
 * Yandex AttributeMatcher</a>
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see org.openqa.selenium.WebElement#getAttribute(String)
 * @since 1.0
 */
class AttributeMatcher extends TypeSafeMatcher<WebElement>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final String name;

    private final Matcher<String> matcher;

    AttributeMatcher( String name, Matcher<String> matcher )
    {
        this.matcher = matcher;
        this.name = name;
    }

    //endregion

    @Factory
    static AttributeMatcher id( final Matcher<String> valueMatcher )
    {
        return attr( "id", valueMatcher );
    }

    @Factory
    static AttributeMatcher attr( final String name, final Matcher<String> valueMatcher )
    {
        return attribute( name, valueMatcher );
    }

    @Factory
    static AttributeMatcher attribute( final String name, final Matcher<String> valueMatcher )
    {
        return new AttributeMatcher( name, valueMatcher );
    }

    @Factory
    static AttributeMatcher id( final String value )
    {
        return id( CoreMatchers.is( value ) );
    }

    @Factory
    static AttributeMatcher value( final Matcher<String> valueMatcher )
    {
        return attr( "value", valueMatcher );
    }

    @Factory
    static AttributeMatcher value( final String value )
    {
        return value( CoreMatchers.is( value ) );
    }

    @Factory
    static AttributeMatcher name( final Matcher<String> valueMatcher )
    {
        return attr( "name", valueMatcher );
    }

    @Factory
    static AttributeMatcher name( final String value )
    {
        return name( CoreMatchers.is( value ) );
    }

    @Factory
    static AttributeMatcher className( final Matcher<String> valueMatcher )
    {
        return attr( "class", valueMatcher );
    }

    @Factory
    static AttributeMatcher className( final String value )
    {
        return className( CoreMatchers.is( value ) );
    }

    @Factory
    static AttributeMatcher href( final Matcher<String> valueMatcher )
    {
        return attr( "href", valueMatcher );
    }

    @Factory
    static AttributeMatcher href( final String value )
    {
        return href( CoreMatchers.is( value ) );
    }

    @Factory
    static AttributeMatcher src( final Matcher<String> valueMatcher )
    {
        return attr( "src", valueMatcher );
    }

    @Factory
    static AttributeMatcher src( final String value )
    {
        return src( CoreMatchers.is( value ) );
    }

    @Factory
    static AttributeMatcher alt( final Matcher<String> valueMatcher )
    {
        return attr( "alt", valueMatcher );
    }

    @Factory
    static AttributeMatcher alt( final String value )
    {
        return alt( CoreMatchers.is( value ) );
    }

    @Factory
    static AttributeMatcher title( final String value )
    {
        return title( CoreMatchers.is( value ) );
    }

    @Factory
    static AttributeMatcher title( final Matcher<String> valueMatcher )
    {
        return attr( "title", valueMatcher );
    }

    @Factory
    static AttributeMatcher target( final String value )
    {
        return target( CoreMatchers.is( value ) );
    }

    @Factory
    static AttributeMatcher target( final Matcher<String> valueMatcher )
    {
        return attr( "target", valueMatcher );
    }

    @Factory
    static AttributeMatcher action( final String value )
    {
        return action( CoreMatchers.is( value ) );
    }

    @Factory
    static AttributeMatcher action( final Matcher<String> valueMatcher )
    {
        return attr( "action", valueMatcher );
    }

    @Factory
    static AttributeMatcher placeholder( final String value )
    {
        return placeholder( CoreMatchers.is( value ) );
    }

    @Factory
    static AttributeMatcher placeholder( final Matcher<String> valueMatcher )
    {
        return attr( "placeholder", valueMatcher );
    }

    @Factory
    static AttributeMatcher size( final Matcher<String> valueMatcher )
    {
        return attr( "size", valueMatcher );
    }

    @Factory
    static AttributeMatcher size( final String value )
    {
        return size( CoreMatchers.is( value ) );
    }

    @Factory
    static Matcher<WebElement> checked()
    {
        return attr( "checked", "true" );
    }

    @Factory
    static AttributeMatcher attr( final String name, final String value )
    {
        return attribute( name, value );
    }

    @Factory
    static AttributeMatcher attribute( final String name, final String value )
    {
        return new AttributeMatcher( name, CoreMatchers.is( value ) );
    }

    @Factory
    static Matcher<WebElement> selected()
    {
        return attr( "selected", "true" );
    }

    @Factory
    static Matcher<WebElement> focused()
    {
        return attr( "focused", "true" );
    }

    @Override
    public boolean matchesSafely( WebElement item )
    {
        return matcher.matches( item.getAttribute( name ) );
    }

    @Override
    protected void describeMismatchSafely( WebElement item, Description mismatchDescription )
    {
        mismatchDescription.appendValue( item ).
                                                       appendText( " " ).
                                                                                appendValue( name ).
                                                                                                           appendText( " attribute is " ).
                                                                                                                                                 appendValue( item.getAttribute(
                                                                                                                                                         name ) );
    }

    public void describeTo( Description description )
    {
        description.appendText( "element " ).
                                                    appendValue( name ).
                                                                               appendText( " attribute " ).
                                                                                                                  appendDescriptionOf( matcher );
    }

}

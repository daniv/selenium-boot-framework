package selenium.boot.webdriver.matchers;


import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public final class WebDriverMatchers
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private WebDriverMatchers()
    {
        super();
    }

    //endregion

    /**
     * Creates matcher that checks if element currently exists on page.
     */
    public static Matcher<WebElement> exists()
    {
        return ExistMatcher.exists();
    }

    /**
     * Creates matcher that checks if element is currently displayed on page.
     */
    public static Matcher<WebElement> isDisplayed()
    {
        return DisplayedMatcher.displayed();
    }

    /**
     * Creates matcher that checks if element is currently enabled.
     */
    public static Matcher<WebElement> isEnabled()
    {
        return EnabledMatcher.enabled();
    }


    /**
     * Creates matcher that checks if element is currently selected.
     */
    public static Matcher<WebElement> isSelected()
    {
        return SelectedMatcher.selected();
    }

    /**
     * Creates matcher that checks if element text equals to the given text.
     *
     * @param tag Expected tag of element.
     */
    public static Matcher<WebElement> hasTagName( final String tag )
    {
        return TagNameMatcher.tagName( tag );
    }

    /**
     * Creates matcher that matches element text tag given matcher.
     *
     * @param matcher Matcher to match element tag with.
     */
    public static Matcher<WebElement> hasTagName( final Matcher<String> matcher )
    {
        return TagNameMatcher.tagName( matcher );
    }

    /**
     * Creates matcher that matches element text with given matcher.
     *
     * @param matcher Matcher to match element text with.
     */
    public static Matcher<WebElement> hasText( final Matcher<String> matcher )
    {
        return null;//TextMatcher.text( matcher );
    }

    /**
     * Creates matcher that checks if element text equals to the given text.
     *
     * @param text Expected text of element.
     */
    public static Matcher<WebElement> hasText( final String text )
    {
        return null;//TextMatcher.text( text );
    }

    /**
     * Creates matcher that matches value of specified attribute with the given matcher.
     *
     * @param attribute    Name of matched attribute.
     * @param valueMatcher Matcher to match attribute value with.
     */
    public static Matcher<WebElement> hasAttribute( final String attribute, final Matcher<String> valueMatcher )
    {
        return AttributeMatcher.attribute( attribute, valueMatcher );
    }

    /**
     * Creates matcher that checks if value of specified element attribute equals to the given value.
     *
     * @param attribute Name of matched attribute.
     * @param value     Expected attribute value.
     */
    public static Matcher<WebElement> hasAttribute( final String attribute, final String value )
    {
        return AttributeMatcher.attribute( attribute, value );
    }

    /**
     * Creates matcher that matches the 'class' attribute of specified element with given matcher.
     *
     * @param matcher Matcher to match the 'class' attribute with.
     */
    public static Matcher<WebElement> hasClass( final Matcher<String> matcher )
    {
        return AttributeMatcher.className( matcher );
    }

    /**
     * Creates matcher that checks if the 'class' attribute of specified element has expected value.
     *
     * @param value Expected value of the 'class' attribute.
     */
    public static Matcher<WebElement> hasClass( final String value )
    {
        return AttributeMatcher.className( value );
    }

    /**
     * Creates matcher that matches the 'name' attribute of specified element with given matcher.
     *
     * @param matcher Matcher to match the 'name' attribute with.
     */
    public static Matcher<WebElement> hasName( final Matcher<String> matcher )
    {
        return AttributeMatcher.name( matcher );
    }

    /**
     * Creates matcher that checks if the 'name' attribute of specified element has expected value.
     *
     * @param value Expected value of the 'name' attribute.
     */
    public static Matcher<WebElement> hasName( final String value )
    {
        return AttributeMatcher.name( value );
    }

    /**
     * Creates matcher that matches the 'id' attribute of specified element with given matcher.
     *
     * @param matcher Matcher to match the 'id' attribute with.
     */
    public static Matcher<WebElement> hasId( final Matcher<String> matcher )
    {
        return AttributeMatcher.id( matcher );
    }

    /**
     * Creates matcher that checks if the 'id' attribute of specified element has expected value.
     *
     * @param value Expected value of the 'id' attribute.
     */
    public static Matcher<WebElement> hasId( final String value )
    {
        return AttributeMatcher.id( value );
    }

    /**
     * Creates matcher that matches the 'value' attribute of specified element with given matcher.
     *
     * @param matcher Matcher to match the 'value' attribute with.
     */
    public static Matcher<WebElement> hasValue( final Matcher<String> matcher )
    {
        return AttributeMatcher.value( matcher );
    }

    /**
     * Creates matcher that checks if the 'value' attribute of specified element has expected value.
     *
     * @param value Expected value of the 'value' attribute.
     */
    public static Matcher<WebElement> hasValue( final String value )
    {
        return AttributeMatcher.value( value );
    }



//    /**
//     * Creates matcher that tests if {@link CheckBox} is checked.
//     */
//    public static Matcher<CheckBox> isSelected() {
//        return IsCheckBoxSelectedMatcher.isSelected();
//    }
//
//    /**
//     * Creates matcher that tests if {@link Radio} has selected button matching the specified matcher.
//     *
//     * @param buttonMatcher Matcher to match selected radio button with.
//     */
//    public static Matcher<Radio> hasSelectedRadioButton( final Matcher<WebElement> buttonMatcher) {
//        return HasSelectedRadioButtonMatcher.hasSelectedRadioButton( buttonMatcher);
//    }


}

package selenium.boot.core.matchers;


import org.hamcrest.Matcher;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringEndsWith;
import org.hamcrest.core.StringStartsWith;
import org.hamcrest.internal.ReflectiveTypeFinder;
import org.hamcrest.text.IsEmptyString;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.hamcrest.text.StringContainsInOrder;

import java.util.regex.Pattern;



/**
 * Convenience util for matching strings
 * most of the methods uses {@link CharSequence} instance to allow for easy interopability between
 * String, StringBuffer, StringBuilder, CharBuffer, etc .
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see org.hamcrest.Matcher
 * @since 1.0
 */
public final class StringMatchers
{

    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private static final ReflectiveTypeFinder TYPE_FINDER =
            new ReflectiveTypeFinder( "matchesSafely", 1, 0 );

    private StringMatchers()
    {
        super();
    }

    //endregion

    /**
     * Creates a matcher that matches if the examined {@code String} contains the specified {@code String} anywhere.
     * <p>
     * For example:
     * <pre>assertThat("myStringOfNote", containsString("ring"))</pre>
     *
     * @param substring the substring that the returned matcher will expect to find within any examined string
     */
    public static Matcher<String> containsString( String substring )
    {
        return StringContains.containsString( substring );
    }

    /**
     * Creates a matcher that matches if the examined {@code String} contains the specified {@code String} anywhere, ignoring case.
     * <p>
     * For example:
     * <pre>assertThat("myStringOfNote", containsString("ring"))</pre>
     *
     * @param substring the substring that the returned matcher will expect to find within any examined string
     */
    public static Matcher<String> containsStringIgnoringCase( String substring )
    {
        return ExtendedStringContains.containsStringIgnoringCase( substring );
    }

    /**
     * Creates a matcher of {@code String} that matches when the examined string is {@code null}, or has zero length.
     * <p>
     * For example:
     * <pre>assertThat(((String)null), is(emptyOrNullString()))</pre>
     */
    public static Matcher<String> emptyOrNullString()
    {
        return IsEmptyString.isEmptyOrNullString();
    }

    /**
     * Creates a matcher that matches if the examined {@code String} starts with the specified {@code String}.
     * <p>
     * For example:
     * <pre>assertThat("myStringOfNote", startsWith("my"))</pre>
     *
     * @param prefix the substring that the returned matcher will expect at the start of any examined string
     */
    public static Matcher<String> startsWith( String prefix )
    {
        return StringStartsWith.startsWith( prefix );
    }

    /**
     * Creates a matcher that matches if the examined {@code String} starts with the specified {@code String}, ignoring case
     * <p>
     * For example:
     * <pre>assertThat("myStringOfNote", startsWith("my"))</pre>
     *
     * @param prefix the substring that the returned matcher will expect at the start of any examined string
     */
    public static Matcher<String> startsWithIgnoringCase( String prefix )
    {
        return ExtendedStringStartsWith.startsWithIgnoringCase( prefix );
    }

    /**
     * Creates a matcher that matches if the examined {@code String} ends with the specified {@code String}.
     * <p>
     * For example:
     * <pre>assertThat("myStringOfNote", endsWith("Note"))</pre>
     *
     * @param suffix the substring that the returned matcher will expect at the end of any examined string
     */
    public static Matcher<String> endsWith( String suffix )
    {
        return StringEndsWith.endsWith( suffix );
    }

    /**
     * Creates a matcher that matches if the examined {@code String} ends with the specified {@code String}, ignoring case.
     * <p>
     * For example:
     * <pre>assertThat("myStringOfNote", endsWith("Note"))</pre>
     *
     * @param suffix the substring that the returned matcher will expect at the end of any examined string
     */
    public static Matcher<String> endsWithIgnoringCase( String suffix )
    {
        return ExtendedStringEndsWith.endsWithIgnoringCase( suffix );
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string is equal to the specified expectedString, ignoring case.
     * <p>
     * For example:
     * <pre>assertThat("Foo", equalToIgnoringCase("FOO"))</pre>
     *
     * @param expectedString the expected value of matched strings
     */
    public static Matcher<String> equalToIgnoringCase( String expectedString )
    {
        return IsEqualIgnoringCase.equalToIgnoringCase( expectedString );
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string is equal to
     * the specified expectedString, when whitespace differences are (mostly) ignored.
     * To be exact, the following whitespace rules are applied:
     * <ul>
     *     <li>all leading and trailing whitespace of both the expectedString and the examined string are ignored</li>
     *     <li>any remaining whitespace, appearing within either string, is collapsed to a single space before comparison</li>
     * </ul>
     * <p>
     * For example:
     * <pre>assertThat("   my\tfoo  bar ", equalToIgnoringWhiteSpace(" my  foo bar"))</pre>
     *
     * @param expectedString the expected value of matched strings
     */
    public static Matcher<String> equalToIgnoringWhiteSpace( String expectedString )
    {
        return IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace( expectedString );
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string contains
     * zero or more whitespace characters and nothing else.
     * <p>
     * For example:
     * <pre>assertThat("  ", is(blankString()))</pre>
     */
    public static Matcher<String> blankString()
    {
        return IsBlankString.blankString();
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string
     * exactly matches the given {@link java.util.regex.Pattern}.
     */
    public static Matcher<String> matchesPattern( Pattern pattern )
    {
        return MatchesPattern.matchesPattern( pattern );
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string
     * exactly matches the given regular expression, treated as a {@link java.util.regex.Pattern}.
     */
    public static Matcher<String> matchesPattern( String regex )
    {
        return MatchesPattern.matchesPattern( regex );
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string contains all of
     * the specified substrings, regardless of the order of their appearance.
     * <p>
     * For example:
     * <pre>assertThat("myfoobarbaz", stringContainsInOrder(Arrays.asList("bar", "foo")))</pre>
     *
     * @param substrings the substrings that must be contained within matching strings
     */
    public static Matcher<String> stringContainsInOrder( Iterable<String> substrings )
    {
        return StringContainsInOrder.stringContainsInOrder( substrings );
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string contains all of
     * the specified substrings, regardless of the order of their appearance.
     * <p>
     * For example:
     * <pre>assertThat("myfoobarbaz", stringContainsInOrder("bar", "foo"))</pre>
     *
     * @param substrings the substrings that must be contained within matching strings
     */
//    public static Matcher<String> stringContainsInOrder( String... substrings )
//    {
//        return StringContainsInOrder.stringContainsInOrder( substrings );
//    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string has zero length.
     * <p>
     * For example:
     * <pre>assertThat("", is(emptyString()))</pre>
     */
    public static Matcher<String> emptyString()
    {
        return IsEmptyString.isEmptyString();
    }

    /**
     * Creates a matcher of {@link String} that matches when the examined string is {@code null}, or
     * contains zero or more whitespace characters and nothing else.
     * <p>
     * For example:
     * <pre>assertThat(((String)null), is(blankOrNullString()))</pre>
     */
    public static Matcher<String> blankOrNullString()
    {
        return IsBlankString.blankOrNullString();
    }

    /**
     * Creates a matcher of {@link CharSequence} that matches when the examined
     * {@link CharSequence} has the specified length.
     *
     * For example:
     * <pre>assertThat("myStringOfNote", length(10))</pre>
     *
     * @param length the length that the returned matcher will expect any examined string to have
     */
    public static Matcher<CharSequence> length( int length )
    {
        return CharSequenceLengthMatcher.length( length );
    }

    /**
     * Creates a matcher of {@link CharSequence} that matches when the examined
     * {@link CharSequence} has the specified length, determined by the specified matcher.
     *
     * For example:
     * <pre>assertThat("myStringOfNote", length(not(equalTo(10))))</pre>
     * <pre>assertThat("myStringOfNote", length(lessThan(15))))</pre>
     *
     * @param lengthMatcher the matcher to apply to the examined {@link CharSequence}
     */
    public static Matcher<CharSequence> length( Matcher<? super Integer> lengthMatcher )
    {
        return CharSequenceLengthMatcher.length( lengthMatcher );
    }
}

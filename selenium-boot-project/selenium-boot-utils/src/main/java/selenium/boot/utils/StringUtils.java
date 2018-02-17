package selenium.boot.utils;


import ch.qos.logback.core.CoreConstants;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import javax.annotation.CheckForNull;
import javax.annotation.CheckForSigned;
import javax.annotation.Nonnegative;
import java.beans.Introspector;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;



/**
 * This class offers a "one-place" string utils manipulation methods.
 * sources:
 * <ul>
 *     <li>org.springframework.util.StringUtils</li>
 *     <li>org.apache.commons.lang3.StringUtils</li>
 *     <li>org.apache.commons.text.WordUtils</li>
 *     <li>org.apache.commons.text.WordUtils</li>
 *     <li>org.codehaus.plexus.util.StringUtils</li>
 *     <li>com.google.common.base.Strings</li>
 * </ul>
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public final class StringUtils
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    /**
     * Represents a failed index search.
     */
    private static final int INDEX_NOT_FOUND = -1;

    public static final char NUL = '\u0000';

    private static final char[] HEX_CHARS = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f',
            };

    private StringUtils()
    {
        super();
    }

    //endregion


    //---------------------------------------------------------------------
    // Empty checks
    //---------------------------------------------------------------------

    /**
     * Checks if a CharSequence is empty ("") or null.
     * <p>
     * <pre>{@code
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * }</pre>
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if the CharSequence is empty or null
     */
    public static boolean isEmpty( @Nullable final CharSequence cs )
    {
        return ( cs == null ) || cs.toString().isEmpty();
    }

    /**
     * Checks if a String is non {@code null} and is not empty ({@code length > 0}).
     *
     * <pre>{@code
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * }</pre>
     *
     * @param str the String to check
     *
     * @return true if the String is non-null, and not length zero
     */
    public static boolean isNotEmpty( @Nullable CharSequence str )
    {
        return BooleanUtils.isFalse( isEmpty( str ) );
    }

    /**
     * Checks if any of the CharSequences are empty ("") or null.
     * <p>
     * <pre>{@code
     * StringUtils.isAnyEmpty(null)             = true
     * StringUtils.isAnyEmpty(null, "foo")      = true
     * StringUtils.isAnyEmpty("", "bar")        = true
     * StringUtils.isAnyEmpty("bob", "")        = true
     * StringUtils.isAnyEmpty("  bob  ", null)  = true
     * StringUtils.isAnyEmpty(" ", "bar")       = false
     * StringUtils.isAnyEmpty("foo", "bar")     = false
     * StringUtils.isAnyEmpty(new String[]{})   = false
     * StringUtils.isAnyEmpty(new String[]{""}) = true
     * }</pre>
     *
     * @param css the CharSequences to check, may be null or empty
     *
     * @return {@code true} if any of the CharSequences are empty or null
     */
    public static boolean isAnyEmpty( final CharSequence... css )
    {
        if( ArrayUtils.isEmpty( css ) )
        {
            return false;
        }
        for( final CharSequence cs : css )
        {
            if( isEmpty( cs ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if none of the CharSequences are empty ("") or null.
     * <p>
     * <pre>{@code
     * StringUtils.isNoneEmpty(null)             = false
     * StringUtils.isNoneEmpty(null, "foo")      = false
     * StringUtils.isNoneEmpty("", "bar")        = false
     * StringUtils.isNoneEmpty("bob", "")        = false
     * StringUtils.isNoneEmpty("  bob  ", null)  = false
     * StringUtils.isNoneEmpty(new String[] {})  = true
     * StringUtils.isNoneEmpty(new String[]{""}) = false
     * StringUtils.isNoneEmpty(" ", "bar")       = true
     * StringUtils.isNoneEmpty("foo", "bar")     = true
     * }</pre>
     *
     * @param css the CharSequences to check, may be null or empty
     *
     * @return {@code true} if none of the CharSequences are empty or null
     */
    public static boolean isNoneEmpty( final CharSequence... css )
    {
        return !isAnyEmpty( css );
    }

    /**
     * Checks if all of the CharSequences are empty ("") or null.
     * <p>
     * <pre>{@code
     * StringUtils.isAllEmpty(null)             = true
     * StringUtils.isAllEmpty(null, "")         = true
     * StringUtils.isAllEmpty(new String[] {})  = true
     * StringUtils.isAllEmpty(null, "foo")      = false
     * StringUtils.isAllEmpty("", "bar")        = false
     * StringUtils.isAllEmpty("bob", "")        = false
     * StringUtils.isAllEmpty("  bob  ", null)  = false
     * StringUtils.isAllEmpty(" ", "bar")       = false
     * StringUtils.isAllEmpty("foo", "bar")     = false
     * }</pre>
     *
     * @param css the CharSequences to check, may be null or empty
     *
     * @return {@code true} if all of the CharSequences are empty or null
     */
    public static boolean isAllEmpty( final CharSequence... css )
    {
        if( ArrayUtils.isEmpty( css ) )
        {
            return true;
        }
        for( final CharSequence cs : css )
        {
            if( isNotEmpty( cs ) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the given string if it is non-null; the empty string otherwise.
     *
     * @param string the string to test and possibly return
     *
     * @return {@code string} itself if it is non-null; {@code ""} if it is null
     */
    public static String nullToEmpty( @Nullable String string )
    {
        return (string == null) ? CoreConstants.EMPTY_STRING : string;
    }

    /**
     * Returns the given string if it is nonempty; {@code null} otherwise.
     *
     * @param string the string to test and possibly return
     *
     * @return {@code string} itself if it is nonempty; {@code null} if it is empty or null
     */
    @Nullable
    public static String emptyToNull( @Nullable String string )
    {
        return Strings.emptyToNull( string );
    }

    /**
     * Check whether the given {@code CharSequence} contains actual <em>text</em>.
     * <p>
     *  More specifically, this method returns {@code true} if the
     * {@code CharSequence} is not {@code null}, its length is greater than 0, and it contains at least one non-whitespace character.
     *
     * @param cs the {@code CharSequence} to check (may be {@code null})
     *
     * @return {@code true} if the {@code CharSequence} is not {@code null}, its length is greater than 0, and it does not contain whitespace only
     *
     * @see Character#isWhitespace
     * @see #hasText(String)
     */
    public static boolean hasText( @Nullable CharSequence cs )
    {
        return org.springframework.util.StringUtils.hasText( cs );
    }

    /**
     * Check whether the given {@code String} contains actual <em>text</em>.
     * <p>
     * More specifically, this method returns {@code true} if the
     * {@code String} is not {@code null}, its length is greater than 0,
     * and it contains at least one non-whitespace character.
     *
     * @param str the {@code String} to check (may be {@code null})
     *
     * @return {@code true} if the {@code String} is not {@code null},
     *         its length is greater than 0, and it does not contain whitespace only
     *
     * @see #hasText(CharSequence)
     */
    public static boolean hasText( @Nullable String str )
    {
        return org.springframework.util.StringUtils.hasText( str );
    }

    /**
     * Check that the given {@code CharSequence} is neither {@code null} nor of length 0.
     * <p>
     * Note: this method returns {@code true} for a {@code String} that purely consists of whitespace.
     *
     * @param ch the {@code CharSequence} to check (may be {@code null})
     *
     * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
     *
     * @see #hasText(String)
     */
    public static boolean hasLength( @Nullable CharSequence ch )
    {
        return org.springframework.util.StringUtils.hasLength( ch );
    }

    /**
     * Check that the given {@code String} is neither {@code null} nor of length 0.
     * <p>
     * Note: this method returns {@code true} for a {@code String} that purely consists of whitespace.
     *
     * @param str the {@code String} to check (may be {@code null})
     *
     * @return {@code true} if the {@code String} is not {@code null} and has length
     *
     * @see #hasLength(CharSequence)
     * @see #hasText(String)
     */
    public static boolean hasLength( @Nullable String str )
    {
        return org.springframework.util.StringUtils.hasLength( str );
    }


    //---------------------------------------------------------------------
    // Trimming methods
    //---------------------------------------------------------------------

    /**
     * Removes control characters, including whitespace, from both ends of this String, handling {@code null} by returning
     * {@code null}.
     * <p>
     * <pre>{@code
     * StringUtils.trim(null)          = null
     * StringUtils.trim("")            = ""
     * StringUtils.trim("     ")       = ""
     * StringUtils.trim("abc")         = "abc"
     * StringUtils.trim("    abc    ") = "abc"
     * }</pre>
     *
     * @param str the String to check
     *
     * @return the trimmed text (or {@code null})
     *
     * @see java.lang.String#trim()
     */
    @Nullable
    public static String trim( @Nullable String str )
    {
        return ( str == null ? null : str.trim() );
    }

    /**
     * Trim <i>all</i> whitespace from the given {@code String}:
     * leading, trailing, and in between characters.
     *
     * @param str the {@code String} to check
     *
     * @return the trimmed {@code String} without whitespaces
     *
     * @see com.google.common.base.CharMatcher#whitespace()
     * @see com.google.common.base.CharMatcher#removeFrom(CharSequence)
     */
    @Nullable
    public static String trimAllWhitespace( @Nullable String str )
    {
        if( !hasLength( str ) )
        {
            return str;
        }

        return CharMatcher.whitespace().removeFrom( str );
    }

    /**
     * Deletes all whitespaces from a String.
     * <p>
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     *
     * @param str String target to delete whitespace from
     *
     * @return the String without whitespaces
     *
     * @throws NullPointerException in case that {@code str} is null
     */
    public static String deleteWhitespace( String str )
    {
        StringBuilder buffer = new StringBuilder();
        int sz = str.length();
        for( int i = 0; i < sz; i++ )
        {
            if( !Character.isWhitespace( str.charAt( i ) ) )
            {
                buffer.append( str.charAt( i ) );
            }
        }
        return buffer.toString();
    }
    

    //---------------------------------------------------------------------
    // Equals methods
    //---------------------------------------------------------------------


    /**
     * Compares two Strings, returning {@code true} if they are equal.
     * <p>
     * {@code null}s are handled without exceptions. Two {@code null}
     * references are considered to be equal. The comparison is case sensitive.
     *
     * @param str1 the first string
     * @param str2 the second string
     *
     * @return {@code true} if the Strings are equal, case sensitive, or both {@code null}
     *
     * @see java.lang.String#equals(Object)
     * @see #equalsIgnoreCase(String, String)
     */
    public static boolean equals( @Nullable String str1, @Nullable String str2 )
    {
        return ( str1 == null ? str2 == null : str1.equals( str2 ) );
    }

    /**
     * Compares two Strings, returning {@code true} if they are equal ignoring
     * the case.
     * <p>
     * {@code Nulls} are handled without exceptions. Two {@code null} references are considered equal.
     * Comparison is case insensitive.
     *
     * @param str1 the first string
     * @param str2 the second string
     *
     * @return {@code true} if the Strings are equal, case insensitive, or both {@code null}
     *
     * @see java.lang.String#equalsIgnoreCase(String)
     * @see #equals(String, String)
     */
    public static boolean equalsIgnoreCase( @Nullable String str1, @Nullable String str2 )
    {
        return ( str1 == null ? str2 == null : str1.equalsIgnoreCase( str2 ) );
    }


    //---------------------------------------------------------------------
    // IndexOf methods
    //---------------------------------------------------------------------

    /**
     * <p>Finds the first index in the {@code CharSequence} that matches the
     * specified character.</p>
     *
     * @param cs         the {@code CharSequence} to be processed, not null
     * @param searchChar the char to be searched for
     * @param start      the start index, negative starts at the string start
     *
     * @return the index where the search char was found, -1 if not found
     */
    private static int charSeqIndexOf( final CharSequence cs, final int searchChar, int start )
    {
        if( cs instanceof String )
        {
            return ( ( String ) cs ).indexOf( searchChar, start );
        }
        final int sz = cs.length();
        if( start < 0 )
        {
            start = 0;
        }
        for( int i = start; i < sz; i++ )
        {
            if( cs.charAt( i ) == searchChar )
            {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Find the first index of any of a set of potential substrings.
     * <p>
     * {@code null} String will return {@code -1}.
     *
     * @param str        the String to check
     * @param searchStrs the Strings to search for
     *
     * @return the first index of any of the searchStrs in str
     *
     * @throws NullPointerException if any of searchStrs[i] is {@code null}
     */
    @CheckForSigned
    public static int indexOfAny( @Nullable String str, @Nullable String[] searchStrs )
    {
        if( ( str == null ) || ( searchStrs == null ) )
        {
            return INDEX_NOT_FOUND;
        }

        int sz = searchStrs.length;
        int ret = Integer.MAX_VALUE;

        int tmp;
        for( String searchStr : searchStrs )
        {
            tmp = str.indexOf( searchStr );
            if( tmp == -1 )
            {
                continue;
            }

            if( tmp < ret )
            {
                ret = tmp;
            }
        }

        return ( ret == Integer.MAX_VALUE ) ? -1 : ret;
    }

    /**
     * Search a CharSequence to find the first index of any character in the given set of characters.
     * <p>
     * A {@code null} String will return {@code -1}.
     * A {@code null} or zero length search array will return {@code -1}.
     *
     * @param cs          the CharSequence to check, may be null
     * @param searchChars the chars to search for, may be null
     *
     * @return the index of any of the chars, -1 if no match or null input
     */
    @CheckForSigned
    public static int indexOfAny( @Nullable  final CharSequence cs, final char... searchChars )
    {
        if( isEmpty( cs ) || ArrayUtils.isEmpty( searchChars ) )
        {
            return INDEX_NOT_FOUND;
        }
        final int csLen = cs.length();
        final int csLast = csLen - 1;
        final int searchLen = searchChars.length;
        final int searchLast = searchLen - 1;
        for( int i = 0; i < csLen; i++ )
        {
            final char ch = cs.charAt( i );
            for( int j = 0; j < searchLen; j++ )
            {
                if( searchChars[ j ] == ch )
                {
                    if( i < csLast && j < searchLast && Character.isHighSurrogate( ch ) )
                    {
                        if( searchChars[ j + 1 ] == cs.charAt( i + 1 ) )
                        {
                            return i;
                        }
                    }
                    else
                    {
                        return i;
                    }
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Compares two CharSequences, and returns the index at which the CharSequences begin to differ.
     * <p>
     * For example, {@code indexOfDifference("i am a machine", "i am a robot") -> 7}
     *
     * @param str1 the first String, may be null
     * @param str2 the second String, may be null
     *
     * @return the index where cs1 and cs2 begin to differ; -1 if they are equal
     */
    @CheckForSigned
    public static int indexOfDifference( @Nullable final String str1, @Nullable final String str2 )
    {
        if( str1 == null || str2 == null )
        {
            return 0;
        }
        if( str1.equals( str2 ) )
        {
            return INDEX_NOT_FOUND;
        }

        int i;
        for( i = 0; i < str1.length() && i < str2.length(); ++i )
        {
            if( str1.charAt( i ) != str2.charAt( i ) )
            {
                break;
            }
        }
        if( i < str2.length() || i < str1.length() )
        {
            return i;
        }
        return INDEX_NOT_FOUND;
    }

    //---------------------------------------------------------------------
    // Substrings
    //---------------------------------------------------------------------


    /**
     * Gets a substring from the specified string avoiding exceptions.
     *
     * A negative start position can be used to start {@code n} characters from the end of the String.
     *
     * @param str the String to get the substring from
     * @param start the position to start from, negative means count back from the end of the String by this many characters
     * @return substring from start position
     */
    @Nullable
    public static String substring( @Nullable String str, int start )
    {
        if ( str == null )
        {
            return null;
        }

        // handle negatives, which means last n characters
        if ( start < 0 )
        {
            start = str.length() + start; // remember start is negative
        }

        if ( start < 0 )
        {
            start = 0;
        }
        if ( start > str.length() )
        {
            return "";
        }

        return str.substring( start );
    }

    /**
     * Gets a substring from the specified String avoiding exceptions.
     *
     * <p>A negative start position can be used to start/end {@code n}
     * characters from the end of the String.
     *
     * @param str the String to get the substring from
     * @param start the position to start from, negative means count back from the end of the string by this many characters
     * @param end the position to end at (exclusive), negative means count back from the end of the String by this many characters
     * @return substring from start position to end position
     */
    @Nullable
    public static String substring( @Nullable String str, int start, int end )
    {
        if ( str == null )
        {
            return null;
        }

        if ( end < 0 )
        {
            end = str.length() + end; // remember end is negative
        }
        if ( start < 0 )
        {
            start = str.length() + start; // remember start is negative
        }

        if ( end > str.length() )
        {
            end = str.length();
        }

        if ( start > end )
        {
            return CoreConstants.EMPTY_STRING;
        }

        if ( start < 0 )
        {
            start = 0;
        }

        if ( end < 0 )
        {
            end = 0;
        }

        return str.substring( start, end );
    }

    /**
     * Gets the leftmost {@code n} characters of a String.
     * <p>
     * If {@code n} characters are not available, or the
     * String is {@code null}, the String will be returned without an exception.
     *
     * @param str the String to get the leftmost characters from
     * @param len the length of the required String
     *
     * @return the leftmost characters
     *
     * @throws java.lang.IllegalArgumentException if len is less than zero
     */
    @Nullable
    public static String left( @Nullable String str, int len )
    {
        Assert.isFalse( len < 0, () ->"Requested String length " + len + " is less than zero" );
        if ( ( str == null ) || ( str.length() <= len ) )
        {
            return str;
        }
        else
        {
            return str.substring( 0, len );
        }
    }

    /**
     * Gets the rightmost {@code n} characters of a String.
     * <p>
     * If {@code n} characters are not available, or the String
     * is {@code null}, the String will be returned without an exception.
     *
     * @param str the String to get the rightmost characters from
     * @param len the length of the required String
     *
     * @return the rightmost characters
     *
     * @throws java.lang.IllegalArgumentException if len is less than zero
     */
    @Nullable
    public static String right( @Nullable String str, int len )
    {
        Assert.isFalse( len < 0, () ->"Requested String length " + len + " is less than zero" );
        if ( ( str == null ) || ( str.length() <= len ) )
        {
            return str;
        }
        else
        {
            return str.substring( str.length() - len );
        }
    }

    /**
     * Gets {@code n} characters from the middle of a String.
     * <p>
     * If {@code n} characters are not available, the remainder of the String will be returned without an exception.
     * If the String is {@code null}, {@code null} will be returned.
     *
     * @param str the String to get the characters from
     * @param pos the position to start from
     * @param len the length of the required String
     *
     * @return the leftmost characters
     *
     * @throws IndexOutOfBoundsException if pos is out of bounds
     * @throws IllegalArgumentException  if len is less than zero
     */
    @Nullable
    public static String mid( @Nullable String str, int pos, int len )
    {
        if( ( pos < 0 ) || ( ( str != null ) && ( pos > str.length() ) ) )
        {
            throw new StringIndexOutOfBoundsException( "String index " + pos + " is out of bounds" );
        }

        Assert.isFalse( len < 0, () -> "Requested String length " + len + " is less than zero" );

        if( str == null )
        {
            return null;
        }
        if( str.length() <= ( pos + len ) )
        {
            return str.substring( pos );
        }
        else
        {
            return str.substring( pos, pos + len );
        }
    }


    //---------------------------------------------------------------------
    // Splitting and Joining
    //---------------------------------------------------------------------


    /**
     * Splits the provided text into a array, using whitespace as the separator.
     *
     * The separator is not included in the returned String array.
     *
     * @param str the String to parse
     * @return an array of parsed Strings
     */
    public static String[] split( String str )
    {
        return split( str, null, -1 );
    }

    /**
     * @see #split(String, String, int)
     */
    public static String[] split( String text, String separator )
    {
        return split( text, separator, -1 );
    }

    /**
     * Splits the provided text into a array, based on a given separator.
     *
     * The separator is not included in the returned String array.
     * The maximum number of splits to perform can be controlled.
     * A {@code null} separator will cause parsing to be on whitespace.
     *
     * This is useful for quickly splitting a String directly into
     * an array of tokens, instead of an enumeration of tokens (as {@code StringTokenizer} does).
     *
     * @param str The string to parse.
     * @param separator Characters used as the delimiters. If {@code null}, splits on whitespace.
     * @param max The maximum number of elements to include in the array.  A zero or negative value implies no limit.
     * @return an array of parsed Strings
     */
    public static String[] split( String str, @Nullable String separator, int max )
    {
        StringTokenizer tok;
        if ( separator == null )
        {
            // ==============================================================
            // Null separator means we're using StringTokenizer's default
            // delimiter, which comprises all whitespace characters.
            // ==============================================================

            tok = new StringTokenizer( str );
        }
        else
        {
            tok = new StringTokenizer( str, separator );
        }

        int listSize = tok.countTokens();
        if ( ( max > 0 ) && ( listSize > max ) )
        {
            listSize = max;
        }

        String[] list = new String[listSize];
        int i = 0;
        int lastTokenBegin;
        int lastTokenEnd = 0;
        while ( tok.hasMoreTokens() )
        {
            if ( ( max > 0 ) && ( i == listSize - 1 ) )
            {
                String endToken = tok.nextToken();
                lastTokenBegin = str.indexOf( endToken, lastTokenEnd );
                list[i] = str.substring( lastTokenBegin );
                break;
            }
            else
            {
                list[i] = tok.nextToken();
                lastTokenBegin = str.indexOf( list[i], lastTokenEnd );
                lastTokenEnd = lastTokenBegin + list[i].length();
            }
            i++;
        }
        return list;
    }

    /**
     * Take an array of strings and split each element based on the given delimiter.
     * A {@code Properties} instance is then generated, with the left of the
     * delimiter providing the key, and the right of the delimiter providing the value.
     * <p>Will trim both the key and value before adding them to the
     * {@code Properties} instance.
     *
     * @param array     the array to process
     * @param delimiter to split each element using (typically the equals symbol)
     *
     * @return a {@code Properties} instance representing the array contents,
     *         or {@code null} if the array to process was {@code null} or empty
     */
    @Nullable
    public static Properties splitArrayElementsIntoProperties( String[] array,
                                                               String delimiter )
    {
        return splitArrayElementsIntoProperties( array, delimiter, null );
    }

    /**
     * Take an array of strings and split each element based on the given delimiter.
     * A {@code Properties} instance is then generated, with the left of the delimiter providing the key, and the right of the delimiter providing the value.
     * <p>
     * Will trim both the key and value before adding them to the {@code Properties} instance.
     *
     * @param array         the array to process
     * @param delimiter     to split each element using (typically the equals symbol)
     * @param charsToDelete one or more characters to remove from each element
     *                      prior to attempting the split operation (typically the quotation mark symbol), or {@code null} if no removal should occur
     *
     * @return a {@code Properties} instance representing the array contents,
     *         or {@code null} if the array to process was {@code null} or empty
     */
    @Nullable
    public static Properties splitArrayElementsIntoProperties( String[] array,
                                                               String delimiter,
                                                               @Nullable String charsToDelete )
    {

        if( ObjectUtils.isEmpty( array ) )
        {
            return null;
        }

        Properties result = new Properties();
        for( String element : array )
        {
            if( charsToDelete != null )
            {
                element = deleteAny( element, charsToDelete );
            }
            String[] splittedElement = split( element, delimiter );
            if( splittedElement == null )
            {
                continue;
            }
            result.setProperty( splittedElement[ 0 ].trim(), splittedElement[ 1 ].trim() );
        }
        return result;
    }

    /**
     * Inserts spaces between words in a CamelCase name.
     *
     * @param name a name in camel-case
     *
     * @return the name with spaces instead of underscores
     */
    public static String splitCamelCase( final String name )
    {
        Assert.notNull( name, () -> "Provided string is null" );
        StringBuilder splitWords = new StringBuilder();

        StringBuilder currentWord = new StringBuilder();
        for( int index = 0; index < name.length(); index++ )
        {
            if( onWordBoundary( name, index ) )
            {
                splitWords.append( lowercaseOrAcronym( currentWord.toString() ) ).append( " " );
                currentWord = new StringBuilder( String.valueOf( name.charAt( index ) ) );
            }
            else
            {
                currentWord.append( name.charAt( index ) );
            }
        }
        splitWords.append( lowercaseOrAcronym( currentWord.toString() ) );

        return splitWords.toString().trim();
    }


    //---------------------------------------------------------------------
    // Replacing methods
    //---------------------------------------------------------------------

    /**
     * Replace a char with another char inside a larger String, once.
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * @param text text to search and replace in
     * @param repl char to search for
     * @param with char to replace with
     *
     * @return the text with any replacements processed
     *
     * @see #replace(String text, char repl, char with, int max)
     */
    @Nullable
    public static String replaceOnce( @Nullable String text, char repl, char with )
    {
        return replace( text, repl, with, 1 );
    }

    /**
     * Replace a char with another char inside a larger String,
     * for the first {@code max} values of the search char.
     * <p>
     * <p>A {@code null} reference passed to this method is a no-op.
     *
     * @param text text to search and replace in
     * @param repl char to search for
     * @param with char to replace with
     * @param max  maximum number of values to replace, or {@code -1} if no maximum
     *
     * @return the text with any replacements processed
     */
    @Nullable
    public static String replace( @Nullable String text, char repl, char with, int max )
    {
        return replace( text, String.valueOf( repl ), String.valueOf( with ), max );
    }

    /**
     * Replace all occurrences a char within another char.
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * @param text text to search and replace in
     * @param repl char to search for
     * @param with char to replace with
     *
     * @return the text with any replacements processed
     *
     * @see #replace(String text, char repl, char with, int max)
     */
    @Nullable
    public static String replace( @Nullable String text, char repl, char with )
    {
        return replace( text, repl, with, -1 );
    }

    /**
     * Replace a String with another String inside a larger String, once.
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * @param text text to search and replace in
     * @param repl String to search for
     * @param with String to replace with
     *
     * @return the text with any replacements processed
     *
     * @see #replace(String text, String repl, String with, int max)
     */
    @Nullable
    public static String replaceOnce( @Nullable String text,
                                      @Nullable String repl,
                                      @Nullable String with )
    {
        return replace( text, repl, with, 1 );
    }

    /**
     * Replace all occurrences of a substring within a string with another string.
     *
     * @param inString   {@code String} to examine
     * @param oldPattern {@code String} to replace
     * @param newPattern {@code String} to insert
     *
     * @return a {@code String} with the replacements
     */
    public static String replace( String inString, String oldPattern, @Nullable String newPattern )
    {
        return org.springframework.util.StringUtils.replace( inString, oldPattern, newPattern );
    }

    /**
     * Replace a String with another String inside a larger String,
     * for the first {@code max} values of the search String.
     * <p>
     * <p>A {@code null} reference passed to this method is a no-op.
     *
     * @param text text to search and replace in
     * @param repl String to search for
     * @param with String to replace with
     * @param max  maximum number of values to replace, or {@code -1} if no maximum
     *
     * @return the text with any replacements processed
     */
    @Nullable
    public static String replace( @Nullable String text,
                                  @Nullable String repl,
                                  @Nullable String with, int max )
    {
        if( ( text == null ) || ( repl == null ) || ( with == null ) || ( repl.length() == 0 ) )
        {
            return text;
        }

        StringBuilder buf = new StringBuilder( text.length() );
        int start = 0, end;
        while( ( end = text.indexOf( repl, start ) ) != -1 )
        {
            buf.append( text, start, end ).append( with );
            start = end + repl.length();

            if( --max == 0 )
            {
                break;
            }
        }
        buf.append( text, start, text.length() );
        return buf.toString();
    }

    /**
     * Removes each substring of the source String that matches the given regular expression using the DOTALL option.
     * <p>
     * This call is a {@code null} safe equivalent to:
     * <ul>
     * <li>{@code source.replaceAll(&quot;(?s)&quot; + regex, StringUtils.EMPTY)}</li>
     * <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(source).replaceAll(StringUtils.EMPTY)}</li>
     * </ul>
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * @param source the source string
     * @param regex  the regular expression to which this string is to be matched
     *
     * @return The resulting {@code String}
     *
     * @see #replacePattern(String, String, String)
     * @see String#replaceAll(String, String)
     * @see java.util.regex.Pattern#DOTALL
     */
    @Nullable
    public static String removePattern( @Nullable final String source, @Nullable final String regex )
    {
        return replacePattern(source, regex, CoreConstants.EMPTY_STRING );
    }

    /**
     * Replaces each substring of the source String that matches the given regular expression with the given
     * replacement using the {@link java.util.regex.Pattern#DOTALL} option. DOTALL is also know as single-line mode in Perl.
     * <p>
     * This call is a {@code null} safe equivalent to:
     * <ul>
     * <li>{@code source.replaceAll(&quot;(?s)&quot; + regex, replacement)}</li>
     * <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(source).replaceAll(replacement)}</li>
     * </ul>
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * @param source      the source string
     * @param regex       the regular expression to which this string is to be matched
     * @param replacement the string to be substituted for each match
     *
     * @return The resulting {@code String}
     *
     * @see #replaceAll(String, String, String)
     * @see String#replaceAll(String, String)
     * @see java.util.regex.Pattern#DOTALL
     */
    @Nullable
    public static String replacePattern( @Nullable final String source,
                                         @Nullable final String regex,
                                         @Nullable final String replacement )
    {
        if( source == null || regex == null || replacement == null )
        {
            return source;
        }
        return Pattern.compile( regex, Pattern.DOTALL ).matcher( source ).replaceAll( replacement );
    }

    /**
     * Case insensitively replaces all occurrences of a String within another String.
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for (case insensitive), may be null
     * @param replacement  the String to replace it with, may be null
     *
     * @return the text with any replacements processed, {@code null} if null String input
     *
     * @see #replaceIgnoreCase(String text, String searchString, String replacement, int max)
     */
    @Nullable
    public static String replaceIgnoreCase( final String text, final String searchString, final String replacement )
    {
        return replaceIgnoreCase( text, searchString, replacement, -1 );
    }

    /**
     * Case insensitively replaces a String with another String inside a larger String,
     * for the first {@code max} values of the search String.
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for (case insensitive), may be null
     * @param replacement  the String to replace it with, may be null
     * @param max          maximum number of values to replace, or {@code -1} if no maximum
     *
     * @return the text with any replacements processed, {@code null} if null String input
     */
    @Nullable
    public static String replaceIgnoreCase( final String text, final String searchString, final String replacement, final int max )
    {
        return replace( text, searchString, replacement, max, true );
    }

    /**
     * Replaces a String with another String inside a larger String,
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for (case insensitive), may be null
     * @param replacement  the String to replace it with, may be null
     * @param max          maximum number of values to replace, or {@code -1} if no maximum
     * @param ignoreCase   if true replace is case insensitive, otherwise case sensitive
     *
     * @return the text with any replacements processed, {@code null} if null String input
     */
    private static String replace( String text, String searchString, @Nullable String replacement, int max, boolean ignoreCase )
    {
        if( isEmpty( text ) || isEmpty( searchString ) || replacement == null || max == 0 )
        {
            return text;
        }
        String searchText = text;
        if( ignoreCase )
        {
            searchText = text.toLowerCase();
            searchString = searchString.toLowerCase();
        }
        int start = 0;
        int end = searchText.indexOf( searchString, start );
        if( end == INDEX_NOT_FOUND )
        {
            return text;
        }

        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        final StringBuilder buf = new StringBuilder( text.length() + increase );
        while( end != INDEX_NOT_FOUND )
        {
            buf.append( text.substring( start, end ) ).append( replacement );
            start = end + replLength;
            if( --max == 0 )
            {
                break;
            }
            end = searchText.indexOf( searchString, start );
        }

        buf.append( text.substring( start ) );
        return buf.toString();
    }

    /**
     * Removes a substring only if it is at the beginning of a source string, otherwise returns the source string.
     * <p>
     * A {@code null} source string will return {@code null}.
     * An empty ("") source string will return the empty string.
     * A {@code null} search string will return the source string.
     *
     * @param str    the source String to search, may be null
     * @param remove the String to search for and remove, may be null
     *
     * @return the substring with the string removed if found, {@code null} if null String input
     */
    public static String removeStart( final String str, final String remove )
    {
        if( isEmpty( str ) || isEmpty( remove ) )
        {
            return str;
        }
        if( str.startsWith( remove ) )
        {
            return str.substring( remove.length() );
        }
        return str;
    }

    /**
     * Case insensitive removal of a substring if it is at the beginning of a source string,
     * otherwise returns the source string.
     * <p>
     * A {@code null} source string will return {@code null}.
     * An empty ("") source string will return the empty string.
     * A {@code null} search string will return the source string.
     *
     * @param str    the source String to search, may be null
     * @param remove the String to search for (case insensitive) and remove, may be null
     *
     * @return the substring with the string removed if found, {@code null} if null String input
     */
    public static String removeStartIgnoreCase( final String str, final String remove )
    {
        if( isEmpty( str ) || isEmpty( remove ) )
        {
            return str;
        }
        if( str.startsWith( remove ) )
        {
            return str.substring( remove.length() );
        }
        return str;
    }

    /**
     * Removes a substring only if it is at the end of a source string, otherwise returns the source string.
     * <p>
     * A {@code null} source string will return {@code null}.
     * An empty ("") source string will return the empty string.
     * A {@code null} search string will return the source string.
     *
     * @param str    the source String to search, may be null
     * @param remove the String to search for and remove, may be null
     *
     * @return the substring with the string removed if found, {@code null} if null String input
     */
    public static String removeEnd( final String str, final String remove )
    {
        if( isEmpty( str ) || isEmpty( remove ) )
        {
            return str;
        }
        if( str.endsWith( remove ) )
        {
            return str.substring( 0, str.length() - remove.length() );
        }
        return str;
    }

    /**
     * Case insensitive removal of a substring if it is at the end of a source string, otherwise returns the source string.
     * <p>
     * A {@code null} source string will return {@code null}.
     * An empty ("") source string will return the empty string.
     * A {@code null} search string will return the source string.
     *
     * @param str    the source String to search, may be null
     * @param remove the String to search for (case insensitive) and remove, may be null
     *
     * @return the substring with the string removed if found, {@code null} if null String input
     */
    public static String removeEndIgnoreCase( final String str, final String remove )
    {
        if( isEmpty( str ) || isEmpty( remove ) )
        {
            return str;
        }
        if( endsWithIgnoreCase( str, remove ) )
        {
            return str.substring( 0, str.length() - remove.length() );
        }
        return str;
    }

    /**
     * Delete all occurrences of the given substring.
     *
     * @param inString the original {@code String}
     * @param pattern  the pattern to delete all occurrences of
     *
     * @return the resulting {@code String}
     */
    public static String delete( String inString, String pattern )
    {
        return org.springframework.util.StringUtils.delete( inString, pattern );
    }

    /**
     * Delete any character in a given {@code String}.
     *
     * @param inString      the original {@code String}
     * @param charsToDelete a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new lines.
     *
     * @return the resulting {@code String}
     */
    public static String deleteAny( String inString, @Nullable String charsToDelete )
    {
        return org.springframework.util.StringUtils.deleteAny( inString, charsToDelete );
    }

    /**
     * Removes control characters, including whitespace, from both ends of this String, handling {@code null} by returning
     * an empty String.
     *
     * @param str the String to check
     *
     * @return the trimmed text (never {@code null})
     *
     * @see java.lang.String#trim()
     */
    public static String clean( @Nullable String str )
    {
        return ( str == null ? "" : str.trim() );
    }

    /**
     * Overlay a part of a String with another String.
     *
     * @param text    String to do overlaying in
     * @param overlay String to overlay
     * @param start   int to start overlaying at
     * @param end     int to stop overlaying before
     *
     * @return String with overlay text
     *
     * @throws NullPointerException if text or overlay is {@code null}
     */
    public static String overlayString( String text, String overlay, int start, int end )
    {
        return new StringBuffer( start + overlay.length() + text.length() - end + 1 )
                       .append( text, 0, start )
                       .append( overlay )
                       .append( text, end, text.length() )
                       .toString();
    }


    //---------------------------------------------------------------------
    // Centering methods
    //---------------------------------------------------------------------

    /**
     * Center a String in a larger String of size {@code n}.
     * <p>
     * Uses spaces as the value to buffer the String with.
     * Equivalent to {@code center(str, size, " ")}.
     * <p>
     * <pre>{@code
     * StringUtils.center(null, *)   = null
     * StringUtils.center("", 4)     = "    "
     * StringUtils.center("ab", -1)  = "ab"
     * StringUtils.center("ab", 4)   = " ab "
     * StringUtils.center("abcd", 2) = "abcd"
     * StringUtils.center("a", 4)    = " a  "
     * }</pre>
     *
     * @param str  String to center
     * @param size int size of new String
     *
     * @return String containing centered String
     *
     * @throws java.lang.NullPointerException if str is {@code null}
     */
    public static String center( @Nullable String str, int size )
    {
        return center( str, size, " " );
    }

    /**
     * Center a String in a larger String of size {@code n}.
     * <p>
     * Uses a supplied String as the value to buffer the String with.
     *
     * @param str       String to center
     * @param size      int size of new String
     * @param delimeter String to buffer the new String with
     *
     * @return String containing centered String
     *
     * @throws java.lang.NullPointerException if str or delim is {@code null}
     * @throws java.lang.ArithmeticException  if delimeter is the empty String
     */
    public static String center( @Nullable String str, int size, String delimeter )
    {
        int sz = str.length();
        int p = size - sz;
        if( p < 1 )
        {
            return str;
        }
        str = padStart( str, sz + p / 2, delimeter );
        str = padEnd( str, size, delimeter );
        return str;
    }

    /**
     * Centers a String in a larger String of size {@code size}.
     * Uses a supplied character as the value to pad the String with.
     * <p>
     * If the size is less than the String length, the String is returned.
     * A {@code null} String returns {@code null}.
     * A negative size is treated as zero.
     * <p>
     * <pre>{@code
     * StringUtils.center(null, *, *)     = null
     * StringUtils.center("", 4, ' ')     = "    "
     * StringUtils.center("ab", -1, ' ')  = "ab"
     * StringUtils.center("ab", 4, ' ')   = " ab "
     * StringUtils.center("abcd", 2, ' ') = "abcd"
     * StringUtils.center("a", 4, ' ')    = " a  "
     * StringUtils.center("a", 4, 'y')    = "yayy"
     * }</pre>
     *
     * @param str     the String to center, may be null
     * @param size    the int size of new String, negative treated as zero
     * @param padChar the character to pad the new String with
     *
     * @return centered String, {@code null} if null String input
     */
    public static String center( @Nullable String str, final int size, final char padChar )
    {
        if( str == null || size <= 0 )
        {
            return str;
        }
        final int strLen = str.length();
        final int pads = size - strLen;
        if( pads <= 0 )
        {
            return str;
        }
        str = padStart( str, strLen + pads / 2, padChar );
        str = padEnd( str, size, padChar );
        return str;
    }


    //---------------------------------------------------------------------
    // Chomping methods
    //---------------------------------------------------------------------


    /**
     * Remove the last newline, and everything after it from a String.
     *
     * @param str String to chomp the newline from
     *
     * @return String without chomped newline
     *
     * @throws java.lang.NullPointerException if str is {@code null}
     */
    public static String chomp( String str )
    {
        return chomp( str, "\n" );
    }

    /**
     * Remove the last value of a supplied String, and everything after it from a String.
     *
     * @param str String to chomp from
     * @param sep String to chomp
     *
     * @return String without chomped ending
     *
     * @throws java.lang.NullPointerException if str or sep is {@code null}
     */
    public static String chomp( String str, String sep )
    {
        int idx = str.lastIndexOf( sep );
        if( idx != -1 )
        {
            return str.substring( 0, idx );
        }
        else
        {
            return str;
        }
    }

    /**
     * Remove a newline if and only if it is at the end of the supplied String.
     *
     * @param str String to chomp from
     *
     * @return String without chomped ending
     *
     * @throws java.lang.NullPointerException if str is {@code null}
     */
    public static String chompLast( String str )
    {
        return chompLast( str, "\n" );
    }

    /**
     * Remove a value if and only if the String ends with that value.
     *
     * @param str String to chomp from
     * @param sep String to chomp
     *
     * @return String without chomped ending
     *
     * @throws NullPointerException if str or sep is {@code null}
     */
    public static String chompLast( String str, String sep )
    {
        if( str.length() == 0 )
        {
            return str;
        }
        String sub = str.substring( str.length() - sep.length() );
        if( sep.equals( sub ) )
        {
            return str.substring( 0, str.length() - sep.length() );
        }
        else
        {
            return str;
        }
    }

    /**
     * Remove everything and return the last value of a supplied String, and everything after it from a String.
     *
     * @param str String to chomp from
     * @param sep String to chomp
     *
     * @return String chomped
     *
     * @throws NullPointerException if str or sep is {@code null}
     */
    public static String getChomp( String str, String sep )
    {
        int idx = str.lastIndexOf( sep );
        if( idx == str.length() - sep.length() )
        {
            return sep;
        }
        else if( idx != INDEX_NOT_FOUND )
        {
            return str.substring( idx );
        }
        else
        {
            return CoreConstants.EMPTY_STRING;
        }
    }

    /**
     * Remove the first value of a supplied String, and everything before it from a String.
     *
     * @param str String to chomp from
     * @param sep String to chomp
     *
     * @return String without chomped beginning
     *
     * @throws NullPointerException if str or sep is {@code null}
     */
    public static String preChomp( String str, String sep )
    {
        int idx = str.indexOf( sep );
        if( idx != INDEX_NOT_FOUND )
        {
            return str.substring( idx + sep.length() );
        }
        else
        {
            return str;
        }
    }

    /**
     * <p>Remove and return everything before the first value of a
     * supplied String from another String.
     *
     * @param str String to chomp from
     * @param sep String to chomp
     *
     * @return String prechomped
     *
     * @throws NullPointerException if str or sep is {@code null}
     */
    public static String getPreChomp( String str, String sep )
    {
        int idx = str.indexOf( sep );
        if( idx != INDEX_NOT_FOUND )
        {
            return str.substring( 0, idx + sep.length() );
        }
        else
        {
            return CoreConstants.EMPTY_STRING;
        }
    }


    //---------------------------------------------------------------------
    // Chopping methods
    //---------------------------------------------------------------------

    /**
     * Remove the last character from a String.
     * <p>
     * If the String ends in {@code \r\n}, then remove both of them.
     *
     * @param str String to chop last character from
     *
     * @return String without last character
     *
     * @throws java.lang.NullPointerException if str is {@code null}
     */
    public static String chop( String str )
    {
        if( CoreConstants.EMPTY_STRING.equals( str ) )
        {
            return CoreConstants.EMPTY_STRING;
        }
        if( str.length() == 1 )
        {
            return CoreConstants.EMPTY_STRING;
        }
        int lastIdx = str.length() - 1;
        String ret = str.substring( 0, lastIdx );
        char last = str.charAt( lastIdx );
        if( last == '\n' )
        {
            if( ret.charAt( lastIdx - 1 ) == '\r' )
            {
                return ret.substring( 0, lastIdx - 1 );
            }
        }
        return ret;
    }

    /**
     * Remove {@code \n} from end of a String if it's there.
     * If a {@code \r} precedes it, then remove that too.
     *
     * @param str String to chop a newline from
     *
     * @return String without newline
     *
     * @throws NullPointerException if str is {@code null}
     */
    public static String chopNewline( String str )
    {
        int lastIdx = str.length() - 1;
        char last = str.charAt( lastIdx );
        if( last == '\n' )
        {
            if( str.charAt( lastIdx - 1 ) == '\r' )
            {
                lastIdx --;
            }
        }
        else
        {
            lastIdx ++;
        }
        return str.substring( 0, lastIdx );
    }

    //---------------------------------------------------------------------
    // Repeat methods
    //---------------------------------------------------------------------

    /**
     * Repeat a String {@code repeat} times to form a new String, with a String separator injected each time.
     *
     * @param str       the String to repeat, may be null
     * @param separator the String to inject, may be null
     * @param repeat    number of times to repeat str, negative treated as zero
     *
     * @return a new String consisting of the original String repeated, {@code null} if null String input
     */
    @Nullable
    public static String repeat( @Nullable final String str, @Nullable final String separator, final int repeat )
    {
        if( str == null || separator == null )
        {
            return repeat( str, repeat );
        }

        return String.join( separator, Collections.nCopies( repeat, str ) );
    }

    /**
     * Repeat a String {@code repeat} times to form a new String.
     *
     * @param str    the String to repeat, may be null
     * @param repeat number of times to repeat str, negative treated as zero
     *
     * @return a new String consisting of the original String repeated, {@code null} if null String input
     */
    @Nullable
    public static String repeat( @Nullable final String str, final int repeat )
    {
        if( str == null )
        {
            return null;
        }
        if( repeat <= 0 )
        {
            return CoreConstants.EMPTY_STRING;
        }

        final int inputLength = str.length();
        if( repeat == 1 || inputLength == 0 )
        {
            return str;
        }

        return String.join( CoreConstants.EMPTY_STRING, Collections.nCopies( repeat, str ) );
    }

    //---------------------------------------------------------------------
    // Padding methods
    //---------------------------------------------------------------------


    /**
     * Returns a string, of length at least {@code minLength}, consisting of {@code string} prepended
     * with as many copies of {@code padChar} as are necessary to reach that length. For example,
     * <ul>
     * <li>{@code padStart("7", 3, '0')} returns {@code "007"}
     * <li>{@code padStart("2010", 3, '0')} returns {@code "2010"}
     * </ul>
     * <p>
     * See {@link java.util.Formatter} for a richer set of formatting capabilities.
     *
     * @param string    the string which should appear at the end of the result
     * @param minLength the minimum length the resulting string must have.
     *                  Can be zero or negative, in which case the input string is always returned.
     * @param padChar   the character to insert at the beginning of the result until the minimum length is reached
     *
     * @return the padded string
     *
     * @see com.google.common.base.Strings#padStart(String, int, char)
     */
    @NonNull
    public static String padStart( @NonNull String string, int minLength, char padChar )
    {
        return Strings.padStart( string, minLength, padChar );
    }

    /**
     * Left pad a String with spaces (' ').
     * The String is padded to the size of {@code size}.
     *
     * @param string    the String to pad out, may be null
     * @param minLength the size to pad to
     *
     * @return left padded String or original String if no padding is necessary, {@code null} if null String input
     */
    @NonNull
    public static String padStart( @NonNull String string, int minLength )
    {
        return Strings.padStart( string, minLength, ' ' );
    }


    /**
     * Left pad a String with a specified String.
     * Pad to a size of {@code size}.
     *
     * @param string the String to pad out, may be null
     * @param size   the size to pad to
     * @param padStr the String to pad with, null or empty treated as single space
     *
     * @return left padded String or original String if no padding is necessary,
     *         {@code null} if null String input
     */
    public static String padStart( final String string, final int size, String padStr )
    {
        Assert.notNull( string, () -> "String cannot be null" );
        return org.apache.commons.lang3.StringUtils.leftPad( string, size, padStr );
    }

    /**
     * Returns a string, of length at least {@code minLength}, consisting of {@code string} appended
     * with as many copies of {@code padChar} as are necessary to reach that length. For example,
     * <p>
     * <ul>
     * <li>{@code padEnd("4.", 5, '0')} returns {@code "4.000"}
     * <li>{@code padEnd("2010", 3, '!')} returns {@code "2010"}
     * </ul>
     * <p>
     * See {@link java.util.Formatter} for a richer set of formatting capabilities.
     *
     * @param string    the string which should appear at the beginning of the result
     * @param minLength the minimum length the resulting string must have.
     *                  Can be zero or negative, in which case the input string is always returned.
     * @param padChar   the character to append to the end of the result until the minimum length is reached
     *
     * @return the padded string
     *
     * @see com.google.common.base.Strings#padEnd(String, int, char)
     */
    @NonNull
    public static String padEnd( @NonNull String string, int minLength, char padChar )
    {
        return Strings.padEnd( string, minLength, padChar );
    }

    /**
     * Right pad a String with spaces (' ').
     * The String is padded to the size of {@code size}.
     *
     * @param string    the String to pad out, may be null
     * @param minLength the size to pad to
     *
     * @return right padded String or original String if no padding is necessary, {@code null} if null String input
     */
    @NonNull
    public static String padEnd( @NonNull String string, int minLength )
    {
        return Strings.padEnd( string, minLength, ' ' );
    }

    /**
     * Right pad a String with a specified String.
     * The String is padded to the size of {@code size}.
     *
     * @param string the String to pad out, may be null
     * @param size   the size to pad to
     * @param padStr the String to pad with, null or empty treated as single space
     *
     * @return right padded String or original String if no padding is necessary,
     *         {@code null} if null String input
     */
    public static String padEnd( final String string, final int size, String padStr )
    {
        Assert.notNull( string, () -> "String cannot be null" );
        return org.apache.commons.lang3.StringUtils.rightPad( string, size, padStr );
    }

    //---------------------------------------------------------------------
    // Stripping methods
    //---------------------------------------------------------------------


    /**
     * Strips whitespace from the start and end of a String.
     * <p>
     * This is similar to {@linkplain #trim(String)} but removes whitespace.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * <p>
     * A {@code null} input String returns {@code null}.
     *
     * @param str the String to remove whitespace from, may be null
     *
     * @return the stripped String, {@code null} if null String input
     */
    @Nullable
    public static String strip( final String str )
    {
        return strip( str, null );
    }

    /**
     * Strips any of a set of characters from the start and end of a String.
     * This is similar to {@link String#trim()} but allows the characters  to be stripped to be controlled.
     * <p>
     * A {@code null} input String returns {@code null}.
     * An empty string ("") input returns the empty string.
     * <p>
     * If the stripChars String is {@code null}, whitespace is stripped as defined by {@link Character#isWhitespace(char)}.
     * Alternatively use {@link #strip(String)}.
     *
     * @param str        the String to remove characters from, may be null
     * @param stripChars the characters to remove, null treated as whitespace
     *
     * @return the stripped String, {@code null} if null String input
     */
    @Nullable
    public static String strip( @Nullable String str, @Nullable final String stripChars )
    {
        str = stripStart( str, stripChars );
        return stripEnd( str, stripChars );
    }

    /**
     * Strips whitespace from the start and end of every String in an array.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * <p>
     * A new array is returned each time, except for length zero.
     * A {@code null} array will return {@code null}.
     * An empty array will return itself.
     * A {@code null} array entry will be ignored.
     *
     * @param strings the array to remove whitespace from, may be null
     *
     * @return the stripped Strings, {@code null} if null array input
     */
    @Nullable
    public static String[] stripAll( @Nullable final String... strings )
    {
        return stripAll( strings, null );
    }

    /**
     * Strips any of a set of characters from the start and end of every String in an array.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * <p>
     * A new array is returned each time, except for length zero.
     * A {@code null} array will return {@code null}.
     * An empty array will return itself.
     * A {@code null} array entry will be ignored.
     * A {@code null} stripChars will strip whitespace as defined by {@linkplain Character#isWhitespace(char)}.
     *
     * @param array      the array to remove characters from, may be null
     * @param stripChars the characters to remove, null treated as whitespace
     *
     * @return the stripped Strings, {@code null} if null array input
     */
    @Nullable
    public static String[] stripAll( @Nullable final String[] array, @Nullable final String stripChars )
    {
        if( array == null )
        {
            return array;
        }
        if( array.length == 0 )
        {
            return array;
        }
        int sz = array.length;
        String[] newArr = new String[ sz ];
        for( int i = 0; i < sz; i++ )
        {
            newArr[ i ] = strip( array[ i ], stripChars );
        }
        return newArr;
    }

    /**
     * Strips any of a set of characters from the start of a String.
     * <p>
     * A {@code null} input String returns {@code null}.
     * An empty string ("") input returns the empty string.
     * <p>
     * If the stripChars String is {@code null}, whitespace is stripped as defined by {@link Character#isWhitespace(char)}.
     *
     * @param str        the String to remove characters from, may be null
     * @param stripChars the characters to remove, null treated as whitespace
     *
     * @return the stripped String, {@code null} if null String input
     */
    @Nullable
    public static String stripStart( @Nullable final String str,
                                     @Nullable final String stripChars )
    {

        if ( str == null )
        {
            return null;
        }

        int start = 0;

        int sz = str.length();

        if ( stripChars == null )
        {
            while ( ( start != sz ) && Character.isWhitespace( str.charAt( start ) ) )
            {
                start++;
            }
        }
        else
        {
            while ( ( start != sz ) && ( stripChars.indexOf( str.charAt( start ) ) != -1 ) )
            {
                start++;
            }
        }
        return str.substring( start );
    }

    /**
     * Strips any of a set of characters from the end of a String.
     * <p>
     * A {@code null} input String returns {@code null}.
     * An empty string ("") input returns the empty string.
     * <p>
     * If the stripChars String is {@code null}, whitespace is stripped as defined by {@link Character#isWhitespace(char)}.
     *
     * @param str        the String to remove characters from, may be null
     * @param stripChars the set of characters to remove, null treated as whitespace
     *
     * @return the stripped String, {@code null} if null String input
     */
    @Nullable
    public static String stripEnd( @Nullable final String str,
                                   @Nullable final String stripChars )
    {
        if ( str == null )
        {
            return null;
        }
        int end = str.length();

        if ( stripChars == null )
        {
            while ( ( end != 0 ) && Character.isWhitespace( str.charAt( end - 1 ) ) )
            {
                end--;
            }
        }
        else
        {
            while ( ( end != 0 ) && ( stripChars.indexOf( str.charAt( end - 1 ) ) != -1 ) )
            {
                end--;
            }
        }
        return str.substring( 0, end );
    }


    //---------------------------------------------------------------------
    // Conversion and Case Conversion methods
    //---------------------------------------------------------------------


    /**
     * Capitalize a {@code String}, changing the first letter to upper case as per {@linkplain Character#toUpperCase(char)}.
     * No other letters are changed.
     *
     * @param str the {@code String} to capitalize
     *
     * @return the capitalized {@code String}
     */
    public static String capitalize( String str )
    {
        return org.springframework.util.StringUtils.capitalize( str );
    }

    /**
     * Uncapitalize a {@code String}, changing the first letter to lower case as per {@link Character#toLowerCase(char)}.
     * No other letters are changed.
     *
     * @param str the {@code String} to uncapitalize
     *
     * @return the uncapitalized {@code String}
     */
    public static String uncapitalize( String str )
    {
        return org.springframework.util.StringUtils.uncapitalize( str );
    }

    /**
     * Utility method to take a string and convert it to normal Java variable
     * name capitalization.  This normally means converting the first
     * character from upper case to lower case, but in the (unusual) special
     * case when there is more than one character and both the first and
     * second characters are upper case, we leave it alone.
     * <p>
     * Thus "FooBah" becomes "fooBah" and "X" becomes "x", but "URL" stays
     * as "URL".
     *
     * @param name The string to be decapitalized.
     *
     * @return The decapitalized version of the string.
     */
    @Nullable
    public static String decapitalize( @Nullable String name )
    {
        return Introspector.decapitalize( name );
    }

    /**
     * Swaps the case of String.
     * <p>
     * Properly looks after making sure the start of words are Title-case and not Uppercase.
     * {@code null} is returned as {@code null}.
     *
     * @param str the String to swap the case of
     *
     * @return the modified String
     */
    @Nullable
    public static String swapCase( @Nullable String str )
    {
        if ( str == null )
        {
            return null;
        }
        int sz = str.length();
        StringBuilder buffer = new StringBuilder( sz );

        boolean whitespace = false;
        char ch;
        char tmp;

        for ( int i = 0; i < sz; i++ )
        {
            ch = str.charAt( i );
            if ( Character.isUpperCase( ch ) )
            {
                tmp = Character.toLowerCase( ch );
            }
            else if ( Character.isTitleCase( ch ) )
            {
                tmp = Character.toLowerCase( ch );
            }
            else if ( Character.isLowerCase( ch ) )
            {
                if ( whitespace )
                {
                    tmp = Character.toTitleCase( ch );
                }
                else
                {
                    tmp = Character.toUpperCase( ch );
                }
            }
            else
            {
                tmp = ch;
            }
            buffer.append( tmp );
            whitespace = Character.isWhitespace( ch );
        }
        return buffer.toString();
    }

    /**
     * Capitalise all the words in a String.
     * <p>
     * Uses {@link Character#isWhitespace(char)} as a separator between words.
     * <p>
     * {@code null} will return {@code null.
     *
     * @param str the String to capitalise
     *
     * @return capitalised String
     */
    @Nullable
    public static String capitaliseAllWords( @Nullable String str )
    {
        if( str == null )
        {
            return null;
        }
        int sz = str.length();
        StringBuilder buffer = new StringBuilder( sz );
        boolean space = true;
        for( int i = 0; i < sz; i++ )
        {
            char ch = str.charAt( i );
            if( Character.isWhitespace( ch ) )
            {
                buffer.append( ch );
                space = true;
            }
            else if( space )
            {
                buffer.append( Character.toTitleCase( ch ) );
                space = false;
            }
            else
            {
                buffer.append( ch );
            }
        }
        return buffer.toString();
    }

    /**
     * Uncapitalise all the words in a string.
     * <p>
     * Uses {@link Character#isWhitespace(char)} as a separator between words.
     * <p>
     * {@code null} will return {@code null}.
     *
     * @param str the string to uncapitalise
     *
     * @return uncapitalized string
     */
    @Nullable
    public static String uncapitaliseAllWords( @Nullable String str )
    {
        if( str == null )
        {
            return null;
        }
        int sz = str.length();
        StringBuilder buffer = new StringBuilder( sz );
        boolean space = true;
        for( int i = 0; i < sz; i++ )
        {
            char ch = str.charAt( i );
            if( Character.isWhitespace( ch ) )
            {
                buffer.append( ch );
                space = true;
            }
            else if( space )
            {
                buffer.append( Character.toLowerCase( ch ) );
                space = false;
            }
            else
            {
                buffer.append( ch );
            }
        }
        return buffer.toString();
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g., CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     *
     * @param collection the {@code Collection} to convert
     *
     * @return the delimited {@code String}
     */
    public static String collectionToCommaDelimitedString( Collection<?> collection )
    {
        return collectionToDelimitedString( collection, "," );
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     *
     * @param collection the {@code Collection} to convert
     * @param delimiter  the delimiter to use (typically a ",")
     *
     * @return the delimited {@code String}
     */
    public static String collectionToDelimitedString( @Nullable Collection<?> collection,
                                                      String delimiter )
    {
        return collectionToDelimitedString( collection, delimiter,
                                            CoreConstants.EMPTY_STRING, CoreConstants.EMPTY_STRING
        );
    }

    /**
     * Convert a {@link java.util.Collection} to a delimited {@code String} (e.g. CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     *
     * @param collection the {@code Collection} to convert
     * @param delimiter  the delimiter to use (typically a ",")
     * @param prefix     the {@code String} to start each element with
     * @param suffix     the {@code String} to end each element with
     *
     * @return the delimited {@code String}
     */
    public static String collectionToDelimitedString( @Nullable Collection<?> collection,
                                                      String delimiter,
                                                      String prefix,
                                                      String suffix )
    {
        return org.springframework.util.StringUtils.collectionToDelimitedString( collection, delimiter, prefix, suffix );
    }

    /**
     * Convert a {@code String} array into a comma delimited {@code String}
     * (i.e., CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param arr the array to display
     *
     * @return the delimited {@code String}
     */
    public static String arrayToCommaDelimitedString( @Nullable Object[] arr )
    {
        return arrayToDelimitedString( arr, "," );
    }

    /**
     * Convert a {@code String} array into a delimited {@code String} (e.g. CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param arr       the array to display
     * @param delimiter the delimiter to use (typically a ",")
     *
     * @return the delimited {@code String}
     */
    public static String arrayToDelimitedString( @Nullable Object[] arr, String delimiter )
    {
        return org.springframework.util.StringUtils.arrayToDelimitedString( arr, delimiter );
    }

    /**
     * Convert a delimited list (e.g., a row from a CSV file) into a set.
     * <p>
     *
     * @param delimiter the literal, nonempty string to recognize as a separator
     * @param str       the sequence of characters to split
     *
     * @return a set of {@code String} entries in the list
     */
    public static Set<String> delimitedListToSet( String delimiter, @Nullable String str )
    {
        return delimitedListToSet( delimiter, str, true );
    }

    /**
     * Convert a delimited list (e.g., a row from a CSV file) into a set.
     * <p>
     *
     * @param delimiter        the literal, nonempty string to recognize as a separator
     * @param str              the sequence of characters to split
     * @param omitEmptyStrings Automatically omits empty strings from the results if set to {@code true}
     *
     * @return an iteration over the segments split from the parameter
     */
    public static Set<String> delimitedListToSet( String delimiter,
                                                  @Nullable String str, boolean omitEmptyStrings )
    {
        Set<String> set = new LinkedHashSet<>();
        if( null == str ) return set;


        Iterable<String> iterable = omitEmptyStrings
                                            ? Splitter.on( delimiter ).omitEmptyStrings().trimResults().split( str )
                                            : Splitter.on( delimiter ).trimResults().split( str );
        iterable.forEach( set:: add );

        return set;
    }

    //---------------------------------------------------------------------
    // Nested Extraction
    //---------------------------------------------------------------------


    /**
     * Get the String that is nested in between two instances of the same String.
     * <p>
     * If {@code str} is {@code null}, will return {@code null}
     *
     * @param str the String containing nested-string
     * @param tag the String before and after nested-string
     *
     * @return the String that was nested, or {@code null}
     *
     * @throws java.lang.NullPointerException if tag is {@code null}
     */
    @Nullable
    public static String getNestedString( @Nullable String str, String tag )
    {
        return getNestedString( str, tag, tag );
    }

    /**
     * Get the String that is nested in between two Strings
     *
     * @param str   the String containing nested-string
     * @param open  the String before nested-string
     * @param close the String after nested-string
     *
     * @return the String that was nested, or {@code null}
     *
     * @throws java.lang.NullPointerException if open or close is {@code null}
     */
    @Nullable
    public static String getNestedString( @Nullable String str, String open, String close )
    {
        if( str == null )
        {
            return null;
        }
        int start = str.indexOf( open );
        if( start != -1 )
        {
            int end = str.indexOf( close, start + open.length() );
            if( end != -1 )
            {
                return str.substring( start + open.length(), end );
            }
        }
        return null;
    }

    /**
     * Count the occurrences of the substring {@code sub} in string {@code str}.
     *
     * @param str string to search in
     * @param sub string to search for
     */
    public static int countOccurrencesOf( String str, String sub )
    {
        return org.springframework.util.StringUtils.countOccurrencesOf( str, sub );
    }

    /**
     * Convert a comma delimited list (e.g., a row from a CSV file) into a set.
     * <p>Note that this will suppress duplicates, and as of 4.2, the elements in
     * the returned set will preserve the original order in a {@link java.util.LinkedHashSet}.
     *
     * @param str the input {@code String}
     *
     * @return a set of {@code String} entries in the list
     *
     * @see #removeDuplicateStrings(String[])
     */
    public static Set<String> commaDelimitedListToSet( @Nullable String str )
    {
        Set<String> set = Sets.newLinkedHashSet();

        String[] tokens = commaDelimitedListToStringArray( str );
        Collections.addAll( set, tokens );
        return set;
    }

    /**
     * Convert a comma delimited list (e.g., a row from a CSV file) into an
     * array of strings.
     *
     * @param str the input {@code String}
     *
     * @return an array of strings, or the empty array in case of empty input
     */
    public static String[] commaDelimitedListToStringArray( @Nullable String str )
    {
        return delimitedListToStringArray( str, "," );
    }

    /**
     * Take a {@code String} that is a delimited list and convert it into a {@code String} array.
     *
     * A single {@code delimiter} may consist of more than one character,
     * but it will still be considered as a single delimiter string, rather
     * than as bunch of potential delimiter characters, in contrast to {@link #tokenizeToStringArray}.
     *
     * @param str       the input {@code String}
     * @param delimiter the delimiter between elements (this is a single delimiter,
     *                  rather than a bunch individual delimiter characters)
     *
     * @return an array of the tokens in the list
     *
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray( @Nullable String str,
                                                       @Nullable String delimiter )
    {
        return delimitedListToStringArray( str, delimiter, null );
    }

    /**
     * Take a {@code String} that is a delimited list and convert it into a {@code String} array.
     *
     * A single {@code delimiter} may consist of more than one character,
     * but it will still be considered as a single delimiter string, rather
     * than as bunch of potential delimiter characters, in contrast to {@linkplain #tokenizeToStringArray}.
     *
     * @param str           the input {@code String}
     * @param delimiter     the delimiter between elements (this is a single delimiter,
     *                      rather than a bunch individual delimiter characters)
     * @param charsToDelete a set of characters to delete; useful for deleting unwanted
     *                      line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a {@code String}
     *
     * @return an array of the tokens in the list
     *
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray( @Nullable String str,
                                                       @Nullable String delimiter,
                                                       @Nullable String charsToDelete )
    {

        return org.springframework.util.StringUtils.delimitedListToStringArray( str, delimiter, charsToDelete );
    }

    //---------------------------------------------------------------------
    // Defaults methods
    //---------------------------------------------------------------------


    /**
     * Returns either the passed in String, or if the String is {@code null}, the value of {@code defaultStr}.
     * <p>
     * <pre>{@code
     * StringUtils.defaultString(null, "NULL")  = "NULL"
     * StringUtils.defaultString("", "NULL")    = ""
     * StringUtils.defaultString("bat", "NULL") = "bat"
     * </pre>}
     *
     * @param str        the String to check, may be null
     * @param defaultStr the default String to return if the input is {@code null}, may be null
     *
     * @return the passed in String, or the default if it was {@code null}
     *
     * @see String#valueOf(Object)
     */
    public static String defaultString( @Nullable final String str, final String defaultStr )
    {
        return str == null ? defaultStr : str;
    }

    /**
     * Returns either the passed in String, or if the String is {@code null}, an empty String ("").
     * <p>
     * <pre>{@code
     * StringUtils.defaultString(null)  = ""
     * StringUtils.defaultString("")    = ""
     * StringUtils.defaultString("bat") = "bat"
     * </pre>
     * }
     *
     * @param str the String to check, may be null
     *
     * @return the passed in String, or the empty String if it was {@code null}
     *
     * @see String#valueOf(Object)
     */
    public static String defaultString( @Nullable final String str )
    {
        return defaultString( str, CoreConstants.EMPTY_STRING );
    }

    /**
     * Returns either the passed in {@code Object} as a String,
     * or, if the {@code Object} is {@code null}, an empty String.
     *
     * @param obj the Object to check
     *
     * @return the passed in Object's toString, or blank if it was {@code null}
     */
    public static String defaultString( @Nullable Object obj )
    {
        return defaultString( obj, CoreConstants.EMPTY_STRING );
    }

    /**
     * Returns either the passed in {@code Object} as a String,
     * or, if the {@code Object} is {@code null}, a passed
     * in default String.
     *
     * @param obj           the Object to check
     * @param defaultString the default String to return if str is {@code null}
     *
     * @return the passed in string, or the default if it was {@code null}
     */
    public static String defaultString( @Nullable Object obj, String defaultString )
    {
        return ( obj == null ) ? defaultString : obj.toString();
    }

    /**
     * Returns either the passed in CharSequence, or if the CharSequence is
     * whitespace, empty ("") or {@code null}, the value of {@code defaultStr}.
     * <p>
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * <p>
     * <pre>{@code
     * StringUtils.defaultIfBlank(null, "NULL")  = "NULL"
     * StringUtils.defaultIfBlank("", "NULL")    = "NULL"
     * StringUtils.defaultIfBlank(" ", "NULL")   = "NULL"
     * StringUtils.defaultIfBlank("bat", "NULL") = "bat"
     * StringUtils.defaultIfBlank("", null)      = null
     * </pre>}
     *
     * @param <T>        the specific kind of CharSequence
     * @param str        the CharSequence to check, may be null
     * @param defaultStr the default CharSequence to return
     *                   if the input is whitespace, empty ("") or {@code null}, may be null
     *
     * @return the passed in CharSequence, or the default
     *
     * @see StringUtils#defaultString(String, String)
     */
    public static <T extends CharSequence> T defaultIfBlank( final T str, final T defaultStr )
    {
        return isBlank( str ) ? defaultStr : str;
    }

    /**
     * Returns either the passed in CharSequence, or if the CharSequence is
     * empty or {@code null}, the value of {@code defaultStr}.
     * <p>
     * <pre>{@code
     * StringUtils.defaultIfEmpty(null, "NULL")  = "NULL"
     * StringUtils.defaultIfEmpty("", "NULL")    = "NULL"
     * StringUtils.defaultIfEmpty(" ", "NULL")   = " "
     * StringUtils.defaultIfEmpty("bat", "NULL") = "bat"
     * StringUtils.defaultIfEmpty("", null)      = null
     * </pre>}
     *
     * @param <T>        the specific kind of CharSequence
     * @param str        the CharSequence to check, may be null
     * @param defaultStr the default CharSequence to return
     *                   if the input is empty ("") or {@code null}, may be null
     *
     * @return the passed in CharSequence, or the default
     *
     * @see StringUtils#defaultString(String, String)
     */
    public static <T extends CharSequence> T defaultIfEmpty( final T str, final T defaultStr )
    {
        return isEmpty( str ) ? defaultStr : str;
    }


    //---------------------------------------------------------------------
    // Character Tests
    //---------------------------------------------------------------------


    /**
     * Checks if the CharSequence contains only lowercase characters.
     * <p>
     * {@code null} will return {@code false}.
     * An empty CharSequence (length()=0) will return {@code false}.
     * <p>
     * <pre>{@code
     * StringUtils.isAllLowerCase(null)   = false
     * StringUtils.isAllLowerCase("")     = false
     * StringUtils.isAllLowerCase("  ")   = false
     * StringUtils.isAllLowerCase("abc")  = true
     * StringUtils.isAllLowerCase("abC")  = false
     * StringUtils.isAllLowerCase("ab c") = false
     * StringUtils.isAllLowerCase("ab1c") = false
     * StringUtils.isAllLowerCase("ab/c") = false
     * }</pre>
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if only contains lowercase characters, and is non-null
     */
    public static boolean isAllLowerCase( @Nullable final CharSequence cs )
    {
        if( cs == null || isNotNullOrEmpty( cs.toString() ) )
        {
            return false;
        }
        final int sz = cs.length();
        for( int i = 0; i < sz; i++ )
        {
            if( !Character.isLowerCase( cs.charAt( i ) ) )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains only uppercase characters.
     * <p>
     * {@code null} will return {@code false}.
     * An empty String (length()=0) will return {@code false}.
     * <p>
     * <pre>{@code
     * StringUtils.isAllUpperCase(null)   = false
     * StringUtils.isAllUpperCase("")     = false
     * StringUtils.isAllUpperCase("  ")   = false
     * StringUtils.isAllUpperCase("ABC")  = true
     * StringUtils.isAllUpperCase("aBC")  = false
     * StringUtils.isAllUpperCase("A C")  = false
     * StringUtils.isAllUpperCase("A1C")  = false
     * StringUtils.isAllUpperCase("A/C")  = false
     * }</pre>
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if only contains uppercase characters, and is non-null
     */
    public static boolean isAllUpperCase( @Nullable final CharSequence cs )
    {
        if( cs == null || isEmpty( cs ) )
        {
            return false;
        }
        final int sz = cs.length();
        for( int i = 0; i < sz; i++ )
        {
            if( !Character.isUpperCase( cs.charAt( i ) ) )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains only Unicode letters.
     * <p>
     * {@code null} will return {@code false}.
     * An empty CharSequence (length()=0) will return {@code false}.
     * <p>
     * <pre>{@code
     * StringUtils.isAlpha(null)   = false
     * StringUtils.isAlpha("")     = false
     * StringUtils.isAlpha("  ")   = false
     * StringUtils.isAlpha("abc")  = true
     * StringUtils.isAlpha("ab2c") = false
     * StringUtils.isAlpha("ab-c") = false
     * }</pre>
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if only contains letters, and is non-null
     */
    public static boolean isAlpha( @Nullable final CharSequence cs )
    {
        if( isEmpty( cs ) )
        {
            return false;
        }
        final int sz = cs.length();
        for( int i = 0; i < sz; i++ )
        {
            if( !Character.isLetter( cs.charAt( i ) ) )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains only Unicode letters and space (' ').
     * <p>
     * {@code null} will return {@code false} An empty CharSequence (length()=0) will return {@code true}.
     * <p>
     * <pre>{@code
     * StringUtils.isAlphaSpace(null)   = false
     * StringUtils.isAlphaSpace("")     = true
     * StringUtils.isAlphaSpace("  ")   = true
     * StringUtils.isAlphaSpace("abc")  = true
     * StringUtils.isAlphaSpace("ab c") = true
     * StringUtils.isAlphaSpace("ab2c") = false
     * StringUtils.isAlphaSpace("ab-c") = false
     * }</pre>
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if only contains letters and space, and is non-null
     */
    public static boolean isAlphaSpace( @Nullable final CharSequence cs )
    {
        if( cs == null )
        {
            return false;
        }
        final int sz = cs.length();
        for( int i = 0; i < sz; i++ )
        {
            if( !Character.isLetter( cs.charAt( i ) ) && cs.charAt( i ) != ' ' )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains only Unicode letters or digits.
     * <p>
     * {@code null} will return {@code false}.
     * An empty CharSequence (length()=0) will return {@code false}.
     * <p>
     * <pre>{@code
     * StringUtils.isAlphanumeric(null)   = false
     * StringUtils.isAlphanumeric("")     = false
     * StringUtils.isAlphanumeric("  ")   = false
     * StringUtils.isAlphanumeric("abc")  = true
     * StringUtils.isAlphanumeric("ab c") = false
     * StringUtils.isAlphanumeric("ab2c") = true
     * StringUtils.isAlphanumeric("ab-c") = false
     * }</pre>
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if only contains letters or digits, and is non-null
     */
    public static boolean isAlphanumeric( @Nullable final CharSequence cs )
    {
        if( isEmpty( cs ) )
        {
            return false;
        }
        final int sz = cs.length();
        for( int i = 0; i < sz; i++ )
        {
            if( !Character.isLetterOrDigit( cs.charAt( i ) ) )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains only Unicode letters, digits
     * or space ({@code ' '}).
     * <p>
     * {@code null} will return {@code false}.
     * An empty CharSequence (length()=0) will return {@code true}.
     * <p>
     * <pre>{@code
     * StringUtils.isAlphanumericSpace(null)   = false
     * StringUtils.isAlphanumericSpace("")     = true
     * StringUtils.isAlphanumericSpace("  ")   = true
     * StringUtils.isAlphanumericSpace("abc")  = true
     * StringUtils.isAlphanumericSpace("ab c") = true
     * StringUtils.isAlphanumericSpace("ab2c") = true
     * StringUtils.isAlphanumericSpace("ab-c") = false
     * }</pre>
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if only contains letters, digits or space, and is non-null
     */
    public static boolean isAlphanumericSpace( @Nullable final CharSequence cs )
    {
        if( cs == null )
        {
            return false;
        }
        final int sz = cs.length();
        for( int i = 0; i < sz; i++ )
        {
            if( !Character.isLetterOrDigit( cs.charAt( i ) ) && cs.charAt( i ) != ' ' )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains only Unicode digits.
     * A decimal point is not a Unicode digit and returns false.
     * <p>
     * {@code null} will return {@code false}.
     * An empty CharSequence (length()=0) will return {@code false}.
     * <p>
     * Note that the method does not allow for a leading sign, either positive or negative.
     * Also, if a String passes the numeric test, it may still generate a NumberFormatException
     * when parsed by Integer.parseInt or Long.parseLong, e.g. if the value is outside the range
     * for int or long respectively.
     * <p>
     * <pre>{@code
     * StringUtils.isNumeric(null)   = false
     * StringUtils.isNumeric("")     = false
     * StringUtils.isNumeric("  ")   = false
     * StringUtils.isNumeric("123")  = true
     * StringUtils.isNumeric("\u0967\u0968\u0969")  = true
     * StringUtils.isNumeric("12 3") = false
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = false
     * StringUtils.isNumeric("-123") = false
     * StringUtils.isNumeric("+123") = false
     * }</pre>
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if only contains digits, and is non-null
     */
    public static boolean isNumeric( @Nullable final CharSequence cs )
    {
        if( isEmpty( cs ) )
        {
            return false;
        }
        final int sz = cs.length();
        for( int i = 0; i < sz; i++ )
        {
            if( !Character.isDigit( cs.charAt( i ) ) )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains only Unicode digits or space
     * ({@code ' '}).
     * A decimal point is not a Unicode digit and returns false.
     * <p>
     * <p>{@code null} will return {@code false}.
     * An empty CharSequence (length()=0) will return {@code true}.
     * <p>
     * <pre>{@code
     * StringUtils.isNumericSpace(null)   = false
     * StringUtils.isNumericSpace("")     = true
     * StringUtils.isNumericSpace("  ")   = true
     * StringUtils.isNumericSpace("123")  = true
     * StringUtils.isNumericSpace("12 3") = true
     * StringUtils.isNumeric("\u0967\u0968\u0969")  = true
     * StringUtils.isNumeric("\u0967\u0968 \u0969")  = true
     * StringUtils.isNumericSpace("ab2c") = false
     * StringUtils.isNumericSpace("12-3") = false
     * StringUtils.isNumericSpace("12.3") = false
     * }</pre>
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if only contains digits or space, and is non-null
     */
    public static boolean isNumericSpace( @Nullable final CharSequence cs )
    {
        if( cs == null )
        {
            return false;
        }
        final int sz = cs.length();
        for( int i = 0; i < sz; i++ )
        {
            if( !Character.isDigit( cs.charAt( i ) ) && cs.charAt( i ) != ' ' )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains only whitespace.
     * <p>
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * <p>
     * {@code null} will return {@code false}.
     * An empty CharSequence (length()=0) will return {@code true}.
     * <p>
     * <pre>{@code
     * StringUtils.isWhitespace(null)   = false
     * StringUtils.isWhitespace("")     = true
     * StringUtils.isWhitespace("  ")   = true
     * StringUtils.isWhitespace("abc")  = false
     * StringUtils.isWhitespace("ab2c") = false
     * StringUtils.isWhitespace("ab-c") = false
     * }</pre>
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if only contains whitespace, and is non-null
     */
    public static boolean isWhitespace( @Nullable final CharSequence cs )
    {
        if( cs == null )
        {
            return false;
        }
        final int sz = cs.length();
        for( int i = 0; i < sz; i++ )
        {
            if( !Character.isWhitespace( cs.charAt( i ) ) )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains mixed casing of both uppercase and lowercase characters.
     * <p>
     * {@code null} will return {@code false}. An empty CharSequence ({@code length()=0}) will return {@code false}.
     * <p>
     * <pre>{@code
     * StringUtils.isMixedCase(null)    = false
     * StringUtils.isMixedCase("")      = false
     * StringUtils.isMixedCase("ABC")   = false
     * StringUtils.isMixedCase("abc")   = false
     * StringUtils.isMixedCase("aBc")   = true
     * StringUtils.isMixedCase("A c")   = true
     * StringUtils.isMixedCase("A1c")   = true
     * StringUtils.isMixedCase("a/C")   = true
     * StringUtils.isMixedCase("aC\t")  = true
     * }</pre>
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if the CharSequence contains both uppercase and lowercase characters
     */
    public static boolean isMixedCase( @Nullable final CharSequence cs )
    {
        if( isEmpty( cs ) || cs.length() == 1 )
        {
            return false;
        }
        boolean containsUppercase = false;
        boolean containsLowercase = false;
        final int sz = cs.length();
        for( int i = 0; i < sz; i++ )
        {
            if( containsUppercase && containsLowercase )
            {
                return true;
            }
            else if( Character.isUpperCase( cs.charAt( i ) ) )
            {
                containsUppercase = true;
            }
            else if( Character.isLowerCase( cs.charAt( i ) ) )
            {
                containsLowercase = true;
            }
        }
        return containsUppercase && containsLowercase;
    }


    //---------------------------------------------------------------------
    // Reversing methods
    //---------------------------------------------------------------------


    /**
     * Reverses a String as per {@link StringBuilder#reverse()}.
     * <p>
     * A {@code null} String returns {@code null}.
     * <p>
     * <pre>{@code
     * StringUtils.reverse(null)  = null
     * StringUtils.reverse("")    = ""
     * StringUtils.reverse("bat") = "tab"
     * }</pre>
     *
     * @param str the String to reverse, may be null
     *
     * @return the reversed String, {@code null} if null String input
     */
    @Nullable
    public static String reverse( @Nullable final String str )
    {
        if( str == null )
        {
            return null;
        }
        return new StringBuffer( str ).reverse().toString();
    }

    /**
     * Reverses a String that is delimited by a specific character.
     * <p>
     * The Strings between the delimiters are not reversed.
     * Thus java.lang.String becomes String.lang.java (if the delimiter is {@code '.'}).
     * <p>
     * <pre>{@code
     * StringUtils.reverseDelimited(null, *)      = null
     * StringUtils.reverseDelimited("", *)        = ""
     * StringUtils.reverseDelimited("a.b.c", 'x') = "a.b.c"
     * StringUtils.reverseDelimited("a.b.c", ".") = "c.b.a"
     * }</pre>
     *
     * @param str       the String to reverse, may be null
     * @param separator the separator character to use
     *
     * @return the reversed String, {@code null} if null String input
     */
    public static String reverseDelimitedString( final String str, final String separator )
    {
        String[] strings = split( str, separator );
        reverseArray( strings );
        return Joiner.on( separator ).join( strings );
    }

    /**
     * Reverses an array.
     * <p>
     * TAKEN FROM CollectionsUtils.
     *
     * @param array the array to reverse
     */
    private static void reverseArray( Object[] array )
    {
        int i = 0;
        int j = array.length - 1;
        Object tmp;

        while( j > i )
        {
            tmp = array[ j ];
            array[ j ] = array[ i ];
            array[ i ] = tmp;
            j--;
            i++;
        }
    }


    //---------------------------------------------------------------------
    // Abbreviating methods
    //---------------------------------------------------------------------

    
    /**
     * Abbreviates a String using ellipses. This will turn
     * "Now is the time for all good men" into "Now is the time for..."
     * <p>
     * Specifically:
     * <ul>
     *     <li>If the number of characters in {@code str} is less than or equal to {@code maxWidth}, return {@code str}.</li>
     *     <li>Else abbreviate it to {@code (substring(str, 0, max-3) + "...")}.</li>
     *     <li>If {@code maxWidth} is less than {@code 4}, throw an {@code IllegalArgumentException}.</li>
     *     <li>In no case will it return a String of length greater than {@code maxWidth}.</li>
     * </ul>
     *
     * @param str      the String to check, may be null
     * @param maxWidth maximum length of result String, must be at least 4
     *
     * @return abbreviated String, {@code null} if null String input
     *
     * @throws IllegalArgumentException if the width is too small
     */
    @Nullable
    public static String abbreviate( @Nullable final String str, final int maxWidth )
    {
        return abbreviate( str, 0, maxWidth );
    }

    /**
     * Abbreviates a String using ellipses.
     * This will turn "Now is the time for all good men" into "...is the time for..."
     * <p>
     * Works like {@code abbreviate(String, int)}, but allows you to specify a "left edge" offset.
     * Note that this left edge is not necessarily going to be the leftmost character in the result, or the first character following the
     * ellipses, but it will appear somewhere in the result.
     *
     * @param str      the String to check, may be null
     * @param offset   left edge of source String
     * @param maxWidth maximum length of result String, must be at least 4
     *
     * @return abbreviated String, {@code null} if null String input
     *
     * @throws IllegalArgumentException if the width is too small
     */
    @Nullable
    public static String abbreviate( @Nullable final String str, int offset, final int maxWidth )
    {
        if( str == null ) return null;
        Assert.isTrue( maxWidth >= 4, () -> "Minimum abbreviation width is 4" );
        if ( str.length() <= maxWidth )
        {
            return str;
        }
        if ( offset > str.length() )
        {
            offset = str.length();
        }
        if ( ( str.length() - offset ) < ( maxWidth - 3 ) )
        {
            offset = str.length() - ( maxWidth - 3 );
        }
        if ( offset <= 4 )
        {
            return str.substring( 0, maxWidth - 3 ) + "...";
        }

        Assert.state( maxWidth < 7, () -> "Minimum abbreviation width with offset is 7" );
        if ( ( offset + ( maxWidth - 3 ) ) < str.length() )
        {
            return "..." + abbreviate( str.substring( offset ), maxWidth - 3 );
        }
        return "..." + str.substring( str.length() - ( maxWidth - 3 ) );
    }

    /**
     * Abbreviates a String using another given String as replacement marker. This will turn
     * "Now is the time for all good men" into "Now is the time for..." if "..." was defined as the replacement marker.
     * <p>
     * Specifically:
     * <ul>
     *   <li>If the number of characters in {@code str} is less than or equal to
     *       {@code maxWidth}, return {@code str}.</li>
     *   <li>Else abbreviate it to {@code (substring(str, 0, max-abbrevMarker.length) + abbrevMarker)}.</li>
     *   <li>If {@code maxWidth} is less than {@code abbrevMarker.length + 1}, throw an
     *       {@code IllegalArgumentException}.</li>
     *   <li>In no case will it return a String of length greater than
     *       {@code maxWidth}.</li>
     * </ul>
     * <p>
     * <pre>{@code
     * StringUtils.abbreviate(null, "...", *)      = null
     * StringUtils.abbreviate("abcdefg", null, *)  = "abcdefg"
     * StringUtils.abbreviate("", "...", 4)        = ""
     * StringUtils.abbreviate("abcdefg", ".", 5)   = "abcd."
     * StringUtils.abbreviate("abcdefg", ".", 7)   = "abcdefg"
     * StringUtils.abbreviate("abcdefg", ".", 8)   = "abcdefg"
     * StringUtils.abbreviate("abcdefg", "..", 4)  = "ab.."
     * StringUtils.abbreviate("abcdefg", "..", 3)  = "a.."
     * StringUtils.abbreviate("abcdefg", "..", 2)  = IllegalArgumentException
     * StringUtils.abbreviate("abcdefg", "...", 3) = IllegalArgumentException
     * }</pre>
     *
     * @param str          the String to check, may be null
     * @param abbrevMarker the String used as replacement marker
     * @param maxWidth     maximum length of result String, must be at least {@code abbrevMarker.length + 1}
     *
     * @return abbreviated String, {@code null} if null String input
     *
     * @throws java.lang.IllegalArgumentException if the width is too small
     */
    public static String abbreviate( final String str, final String abbrevMarker, final int maxWidth )
    {
        return abbreviate( str, abbrevMarker, 0, maxWidth );
    }

    /**
     * Abbreviates a String using a given replacement marker. This will turn
     * "Now is the time for all good men" into "...is the time for..." if "..." was defined
     * as the replacement marker.
     * <p>
     * Works like {@code abbreviate(String, String, int)}, but allows you to specify
     * a "left edge" offset.  Note that this left edge is not necessarily going to
     * be the leftmost character in the result, or the first character following the
     * replacement marker, but it will appear somewhere in the result.
     * <p>
     * In no case will it return a String of length greater than {@code maxWidth}.
     * <p>
     * <pre>{@code
     * StringUtils.abbreviate(null, null, *, *)                 = null
     * StringUtils.abbreviate("abcdefghijklmno", null, *, *)    = "abcdefghijklmno"
     * StringUtils.abbreviate("", "...", 0, 4)                  = ""
     * StringUtils.abbreviate("abcdefghijklmno", "---", -1, 10) = "abcdefg---"
     * StringUtils.abbreviate("abcdefghijklmno", ",", 0, 10)    = "abcdefghi,"
     * StringUtils.abbreviate("abcdefghijklmno", ",", 1, 10)    = "abcdefghi,"
     * StringUtils.abbreviate("abcdefghijklmno", ",", 2, 10)    = "abcdefghi,"
     * StringUtils.abbreviate("abcdefghijklmno", "::", 4, 10)   = "::efghij::"
     * StringUtils.abbreviate("abcdefghijklmno", "...", 6, 10)  = "...ghij..."
     * StringUtils.abbreviate("abcdefghijklmno", "*", 9, 10)    = "*ghijklmno"
     * StringUtils.abbreviate("abcdefghijklmno", "'", 10, 10)   = "'ghijklmno"
     * StringUtils.abbreviate("abcdefghijklmno", "!", 12, 10)   = "!ghijklmno"
     * StringUtils.abbreviate("abcdefghij", "abra", 0, 4)       = IllegalArgumentException
     * StringUtils.abbreviate("abcdefghij", "...", 5, 6)        = IllegalArgumentException
     * }</pre>
     *
     * @param str          the String to check, may be null
     * @param abbrevMarker the String used as replacement marker
     * @param offset       left edge of source String
     * @param maxWidth     maximum length of result String, must be at least 4
     *
     * @return abbreviated String, {@code null} if null String input
     *
     * @throws java.lang.IllegalArgumentException if the width is too small
     */
    public static String abbreviate( final String str, final String abbrevMarker, int offset, final int maxWidth )
    {
        if( isEmpty( str ) || isEmpty( abbrevMarker ) )
        {
            return str;
        }

        final int abbrevMarkerLength = abbrevMarker.length();
        final int minAbbrevWidth = abbrevMarkerLength + 1;
        final int minAbbrevWidthOffset = abbrevMarkerLength + abbrevMarkerLength + 1;

        Assert.isFalse( maxWidth < minAbbrevWidth,
                        () -> String.format( "Minimum abbreviation width is %d", minAbbrevWidth ) );

        if( str.length() <= maxWidth )
        {
            return str;
        }
        if( offset > str.length() )
        {
            offset = str.length();
        }
        if( str.length() - offset < maxWidth - abbrevMarkerLength )
        {
            offset = str.length() - ( maxWidth - abbrevMarkerLength );
        }
        if( offset <= abbrevMarkerLength + 1 )
        {
            return str.substring( 0, maxWidth - abbrevMarkerLength ) + abbrevMarker;
        }

        Assert.isFalse( maxWidth < minAbbrevWidthOffset,
                        () -> String.format(  "Minimum abbreviation width with offset is %d", minAbbrevWidthOffset ) );
        if( offset + maxWidth - abbrevMarkerLength < str.length() )
        {
            return abbrevMarker + abbreviate( str.substring( offset ), abbrevMarker, maxWidth - abbrevMarkerLength );
        }
        return abbrevMarker + str.substring( str.length() - ( maxWidth - abbrevMarkerLength ) );
    }

    /**
     * Abbreviates a String to the length passed, replacing the middle characters with the supplied
     * replacement String.
     * <p>
     * This abbreviation only occurs if the following criteria is met:
     * <ul>
     *     <li>Neither the String for abbreviation nor the replacement String are null or empty </li>
     *     <li>The length to truncate to is less than the length of the supplied String</li>
     *     <li>The length to truncate to is greater than 0</li>
     *     <li>The abbreviated String will have enough room for the length supplied replacement String
     *         and the first and last characters of the supplied String for abbreviation</li>
     * </ul>
     * <p>
     * Otherwise, the returned String will be the same as the supplied String for abbreviation.
     * <p>
     * <pre>{@code
     * StringUtils.abbreviateMiddle(null, null, 0)      = null
     * StringUtils.abbreviateMiddle("abc", null, 0)      = "abc"
     * StringUtils.abbreviateMiddle("abc", ".", 0)      = "abc"
     * StringUtils.abbreviateMiddle("abc", ".", 3)      = "abc"
     * StringUtils.abbreviateMiddle("abcdef", ".", 4)     = "ab.f"
     * }</pre>
     *
     * @param str    the String to abbreviate, may be null
     * @param middle the String to replace the middle characters with, may be null
     * @param length the length to abbreviate {@code str} to.
     *
     * @return the abbreviated String if the above criteria is met, or the original String supplied for abbreviation.
     */
    @Nullable
    public static String abbreviateMiddle( @Nullable final String str, @Nullable final String middle, final int length )
    {
        if( isEmpty( str ) || isEmpty( middle ) )
        {
            return str;
        }

        if( length >= str.length() || length < middle.length() + 2 )
        {
            return str;
        }

        final int targetSting = length - middle.length();
        final int startOffset = targetSting / 2 + targetSting % 2;
        final int endOffset = str.length() - targetSting / 2;

        return str.substring( 0, startOffset ) +
                       middle +
                       str.substring( endOffset );
    }


    //---------------------------------------------------------------------
    // Difference methods
    //---------------------------------------------------------------------

    
    /**
     * Compare two strings, and return the portion where they differ.
     * (More precisely, return the remainder of the second string,
     * starting from where it's different from the first.)
     * <p>
     * <pre>{@code  strdiff("i am a machine", "i am a robot") -> "robot"}</pre>
     *
     * @return the portion of s2 where it differs from s1; returns the empty string ("") if they are equal
     **/
    public static String difference( String str1, String str2 )
    {
        int at = indexOfDifference( str1, str2 );
        if( at == -1 )
        {
            return "";
        }
        return str2.substring( at );
    }

    /**
     * Compares two CharSequences, and returns the index at which the CharSequences begin to differ.
     * <p>
     * For example,
     * {@code indexOfDifference("i am a machine", "i am a robot") -> 7}</p>
     * <p>
     * <pre>{@code
     * StringUtils.indexOfDifference(null, null) = -1
     * StringUtils.indexOfDifference("", "") = -1
     * StringUtils.indexOfDifference("", "abc") = 0
     * StringUtils.indexOfDifference("abc", "") = 0
     * StringUtils.indexOfDifference("abc", "abc") = -1
     * StringUtils.indexOfDifference("ab", "abxyz") = 2
     * StringUtils.indexOfDifference("abcde", "abxyz") = 2
     * StringUtils.indexOfDifference("abcde", "xyz") = 0
     * }</pre>
     *
     * @param cs1 the first CharSequence, may be null
     * @param cs2 the second CharSequence, may be null
     *
     * @return the index where cs1 and cs2 begin to differ; -1 if they are equal
     **/
    @CheckForSigned
    public static int indexOfDifference( CharSequence cs1, CharSequence cs2 )
    {
        int i;
        for( i = 0; ( i < cs1.length() ) && ( i < cs2.length() ); ++i )
        {
            if( cs1.charAt( i ) != cs2.charAt( i ) )
            {
                break;
            }
        }
        if( ( i < cs2.length() ) || ( i < cs1.length() ) )
        {
            return i;
        }
        return INDEX_NOT_FOUND;
    }


    //---------------------------------------------------------------------
    // Wrapping methods
    //---------------------------------------------------------------------


    /**
     * Wraps a string with a char.
     * <p>
     * <pre>{@code
     * StringUtils.wrap(null, *)        = null
     * StringUtils.wrap("", *)          = ""
     * StringUtils.wrap("ab", '\0')     = "ab"
     * StringUtils.wrap("ab", 'x')      = "xabx"
     * StringUtils.wrap("ab", '\'')     = "'ab'"
     * StringUtils.wrap("\"ab\"", '\"') = "\"\"ab\"\""
     * }</pre>
     *
     * @param str      the string to be wrapped, may be {@code null}
     * @param wrapWith the char that will wrap {@code str}
     *
     * @return the wrapped string, or {@code null} if {@code str==null}
     */
    @Nullable
    public static String wrap( @Nullable final String str, final char wrapWith )
    {
        if( isEmpty( str ) || wrapWith == NUL )
        {
            return str;
        }

        return wrapWith + str + wrapWith;
    }

    /**
     * Wraps a String with another String.
     * <p>
     * A {@code null} input String returns {@code null}.
     * <p>
     * <pre>{@code
     * StringUtils.wrap(null, *)         = null
     * StringUtils.wrap("", *)           = ""
     * StringUtils.wrap("ab", null)      = "ab"
     * StringUtils.wrap("ab", "x")       = "xabx"
     * StringUtils.wrap("ab", "\"")      = "\"ab\""
     * StringUtils.wrap("\"ab\"", "\"")  = "\"\"ab\"\""
     * StringUtils.wrap("ab", "'")       = "'ab'"
     * StringUtils.wrap("'abcd'", "'")   = "''abcd''"
     * StringUtils.wrap("\"abcd\"", "'") = "'\"abcd\"'"
     * StringUtils.wrap("'abcd'", "\"")  = "\"'abcd'\""
     * }</pre>
     *
     * @param str      the String to be wrapper, may be null
     * @param wrapWith the String that will wrap str
     *
     * @return wrapped String, {@code null} if null String input
     */
    @Nullable
    public static String wrap( @Nullable final String str, final String wrapWith )
    {
        if( isEmpty( str ) || isEmpty( wrapWith ) )
        {
            return str;
        }

        return wrapWith.concat( str ).concat( wrapWith );
    }

    /**
     * Wraps a string with a char if that char is missing from the start or end of the given string.
     * <p>
     * <pre>{@code
     * StringUtils.wrap(null, *)        = null
     * StringUtils.wrap("", *)          = ""
     * StringUtils.wrap("ab", '\0')     = "ab"
     * StringUtils.wrap("ab", 'x')      = "xabx"
     * StringUtils.wrap("ab", '\'')     = "'ab'"
     * StringUtils.wrap("\"ab\"", '\"') = "\"ab\""
     * StringUtils.wrap("/", '/')  = "/"
     * StringUtils.wrap("a/b/c", '/')  = "/a/b/c/"
     * StringUtils.wrap("/a/b/c", '/')  = "/a/b/c/"
     * StringUtils.wrap("a/b/c/", '/')  = "/a/b/c/"
     * }</pre>
     *
     * @param str      the string to be wrapped, may be {@code null}
     * @param wrapWith the char that will wrap {@code str}
     *
     * @return the wrapped string, or {@code null} if {@code str==null}
     */
    @Nullable
    public static String wrapIfMissing( @Nullable final String str, final char wrapWith )
    {
        if( isEmpty( str ) || wrapWith == NUL )
        {
            return str;
        }
        final StringBuilder builder = new StringBuilder( str.length() + 2 );
        if( str.charAt( 0 ) != wrapWith )
        {
            builder.append( wrapWith );
        }
        builder.append( str );
        if( str.charAt( str.length() - 1 ) != wrapWith )
        {
            builder.append( wrapWith );
        }
        return builder.toString();
    }

    /**
     * Wraps a string with a string if that string is missing from the start or end of the given string.
     * <p>
     * <pre>{@code
     * StringUtils.wrap(null, *)         = null
     * StringUtils.wrap("", *)           = ""
     * StringUtils.wrap("ab", null)      = "ab"
     * StringUtils.wrap("ab", "x")       = "xabx"
     * StringUtils.wrap("ab", "\"")      = "\"ab\""
     * StringUtils.wrap("\"ab\"", "\"")  = "\"ab\""
     * StringUtils.wrap("ab", "'")       = "'ab'"
     * StringUtils.wrap("'abcd'", "'")   = "'abcd'"
     * StringUtils.wrap("\"abcd\"", "'") = "'\"abcd\"'"
     * StringUtils.wrap("'abcd'", "\"")  = "\"'abcd'\""
     * StringUtils.wrap("/", "/")  = "/"
     * StringUtils.wrap("a/b/c", "/")  = "/a/b/c/"
     * StringUtils.wrap("/a/b/c", "/")  = "/a/b/c/"
     * StringUtils.wrap("a/b/c/", "/")  = "/a/b/c/"
     * }</pre>
     *
     * @param str      the string to be wrapped, may be {@code null}
     * @param wrapWith the char that will wrap {@code str}
     *
     * @return the wrapped string, or {@code null} if {@code str==null}
     */
    @Nullable
    public static String wrapIfMissing( @Nullable final String str, final String wrapWith )
    {
        if( isEmpty( str ) || isEmpty( wrapWith ) )
        {
            return str;
        }

        final StringBuilder builder = new StringBuilder( str.length() + wrapWith.length() + wrapWith.length() );
        if( !str.startsWith( wrapWith ) )
        {
            builder.append( wrapWith );
        }
        builder.append( str );
        if( !str.endsWith( wrapWith ) )
        {
            builder.append( wrapWith );
        }
        return builder.toString();
    }


    //---------------------------------------------------------------------
    // Contains methods
    //---------------------------------------------------------------------


    /**
     * Checks if String contains a search character, handling {@code null}.
     * This method uses {@link String#indexOf(int)}.
     * <p>
     * A {@code null} or empty ("") String will return {@code false}.
     * <p>
     * <pre>{@code
     * StringUtils.contains(null, *)    = false
     * StringUtils.contains("", *)      = false
     * StringUtils.contains("abc", 'a') = true
     * StringUtils.contains("abc", 'z') = false
     * }</pre>
     *
     * @param str        the String to check, may be null
     * @param searchChar the character to find
     *
     * @return true if the String contains the search character, false if not or {@code null} string input
     */
    public static boolean contains( @Nullable String str, char searchChar )
    {
        return !isEmpty( str ) && str.indexOf( searchChar ) >= 0;
    }

    /**
     * Checks if String contains a search String, handling {@code null}.
     * This method uses {@link String#indexOf(int)}.
     * <p>
     * A {@code null} String will return {@code false}.
     * <p>
     * <pre>{@code
     * StringUtils.contains(null, *)     = false
     * StringUtils.contains(*, null)     = false
     * StringUtils.contains("", "")      = true
     * StringUtils.contains("abc", "")   = true
     * StringUtils.contains("abc", "a")  = true
     * StringUtils.contains("abc", "z")  = false
     * }</pre>
     *
     * @param str       the String to check, may be null
     * @param searchStr the String to find, may be null
     *
     * @return true if the String contains the search String, false if not or {@code null} string input
     */
    public static boolean contains( @Nullable String str, @Nullable String searchStr )
    {
        if( str == null || searchStr == null )
        {
            return false;
        }
        return str.contains( searchStr );
    }

    /**
     * Checks if CharSequence contains a search CharSequence irrespective of case,
     * handling {@code null}. Case-insensitivity is defined as by {@link String#equalsIgnoreCase(String)}.
     * <p>
     * A {@code null} CharSequence will return {@code false}.
     * <p>
     * <pre>{@code
     * StringUtils.containsIgnoreCase(null, *) = false
     * StringUtils.containsIgnoreCase(*, null) = false
     * StringUtils.containsIgnoreCase("", "") = true
     * StringUtils.containsIgnoreCase("abc", "") = true
     * StringUtils.containsIgnoreCase("abc", "a") = true
     * StringUtils.containsIgnoreCase("abc", "z") = false
     * StringUtils.containsIgnoreCase("abc", "A") = true
     * StringUtils.containsIgnoreCase("abc", "Z") = false
     * }</pre>
     *
     * @param str       the CharSequence to check, may be null
     * @param searchStr the CharSequence to find, may be null
     *
     * @return true if the CharSequence contains the search CharSequence irrespective of
     *         case or false if not or {@code null} string input
     */
    public static boolean containsIgnoreCase( @Nullable final CharSequence str, @Nullable final CharSequence searchStr )
    {
        if( str == null || searchStr == null )
        {
            return false;
        }

        final int len = searchStr.length();
        final int max = str.length() - len;
        for( int i = 0; i <= max; i++ )
        {
            if( regionMatches( str, true, i, searchStr, 0, len ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Green implementation of regionMatches.
     *
     * @param cs         the {@code CharSequence} to be processed
     * @param ignoreCase whether or not to be case insensitive
     * @param thisStart  the index to start on the {@code cs} CharSequence
     * @param substring  the {@code CharSequence} to be looked for
     * @param start      the index to start on the {@code substring} CharSequence
     * @param length     character length of the region
     *
     * @return whether the region matched
     */
    private static boolean regionMatches( final CharSequence cs, final boolean ignoreCase, final int thisStart,
                                          final CharSequence substring, final int start, final int length )
    {
        if( cs instanceof String && substring instanceof String )
        {
            return ( ( String ) cs ).regionMatches( ignoreCase, thisStart, ( String ) substring, start, length );
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        // Extract these first so we detect NPEs the same as the java.lang.String version
        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length() - start;

        // Check for invalid parameters
        if( thisStart < 0 || start < 0 || length < 0 )
        {
            return false;
        }

        // Check that the regions are long enough
        if( srcLen < length || otherLen < length )
        {
            return false;
        }

        while( tmpLen-- > 0 )
        {
            final char c1 = cs.charAt( index1++ );
            final char c2 = substring.charAt( index2++ );

            if( c1 == c2 )
            {
                continue;
            }

            if( !ignoreCase )
            {
                return false;
            }

            // The same check as in String.regionMatches():
            if( Character.toUpperCase( c1 ) != Character.toUpperCase( c2 )
                        && Character.toLowerCase( c1 ) != Character.toLowerCase( c2 ) )
            {
                return false;
            }
        }

        return true;
    }


    //---------------------------------------------------------------------
    // StartsWith and EndsWith methods
    //---------------------------------------------------------------------

    /**
     * Check if a CharSequence starts with a specified prefix.
     * <p>
     * {@code null}s are handled without exceptions. Two {@code null}
     * references are considered to be equal. The comparison is case sensitive.
     * <p>
     * <pre>{@code
     * StringUtils.startsWith(null, null)      = true
     * StringUtils.startsWith(null, "abc")     = false
     * StringUtils.startsWith("abcdef", null)  = false
     * StringUtils.startsWith("abcdef", "abc") = true
     * StringUtils.startsWith("ABCDEF", "abc") = false
     * }</pre>
     *
     * @param str    the CharSequence to check, may be null
     * @param prefix the prefix to find, may be null
     *
     * @return {@code true} if the CharSequence starts with the prefix, case sensitive, or both {@code null}
     *
     * @see java.lang.String#startsWith(String)
     */
    public static boolean startsWith( final CharSequence str, final CharSequence prefix )
    {
        return startsWith( str, prefix, false );
    }

    /**
     * Test if the given {@code String} starts with the specified prefix, ignoring upper/lower case.
     *
     * <pre>{@code
     * StringUtils.startsWithIgnoreCase(null, null)      = true
     * StringUtils.startsWithIgnoreCase(null, "abc")     = false
     * StringUtils.startsWithIgnoreCase("abcdef", null)  = false
     * StringUtils.startsWithIgnoreCase("abcdef", "abc") = true
     * StringUtils.startsWithIgnoreCase("ABCDEF", "abc") = true
     * }</pre>
     *
     * @param str    the {@code String} to check
     * @param prefix the prefix to look for
     *
     * @see String#startsWith
     */
    public static boolean startsWithIgnoreCase( @Nullable String str, String prefix )
    {
        return startsWith( str, prefix, true );
    }

    /**
     * Check if a CharSequence starts with a specified prefix (optionally case insensitive).
     *
     * @param str        the CharSequence to check, may be null
     * @param prefix     the prefix to find, may be null
     * @param ignoreCase indicates whether the compare should ignore case (case insensitive) or not.
     *
     * @return {@code true} if the CharSequence starts with the prefix or both {@code null}
     *
     * @see java.lang.String#startsWith(String)
     */
    private static boolean startsWith( @Nullable final CharSequence str,
                                       @Nullable final CharSequence prefix,
                                       final boolean ignoreCase )
    {
        if( str == null || prefix == null )
        {
            return str == prefix;
        }
        if( prefix.length() > str.length() )
        {
            return false;
        }
        return regionMatches( str, ignoreCase, 0, prefix, 0, prefix.length() );
    }

    /**
     * Check if a CharSequence starts with any of the provided case-sensitive prefixes.
     * <p>
     * <pre>{@code
     * StringUtils.startsWithAny(null, null)      = false
     * StringUtils.startsWithAny(null, new String[] {"abc"})  = false
     * StringUtils.startsWithAny("abcxyz", null)     = false
     * StringUtils.startsWithAny("abcxyz", new String[] {""}) = true
     * StringUtils.startsWithAny("abcxyz", new String[] {"abc"}) = true
     * StringUtils.startsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
     * StringUtils.startsWithAny("abcxyz", null, "xyz", "ABCX") = false
     * StringUtils.startsWithAny("ABCXYZ", null, "xyz", "abc") = false
     * }</pre>
     *
     * @param sequence      the CharSequence to check, may be null
     * @param searchStrings the case-sensitive CharSequence prefixes, may be empty or contain {@code null}
     *
     * @return {@code true} if the input {@code sequence} is {@code null} AND no {@code searchStrings} are provided, or
     *         the input {@code sequence} begins with any of the provided case-sensitive {@code searchStrings}.
     *
     * @see StringUtils#startsWith(CharSequence, CharSequence)
     */
    public static boolean startsWithAny( @Nullable final CharSequence sequence, final CharSequence... searchStrings )
    {
        if( isEmpty( sequence ) || ArrayUtils.isEmpty( searchStrings ) )
        {
            return false;
        }
        for( final CharSequence searchString : searchStrings )
        {
            if( startsWith( sequence, searchString ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a CharSequence ends with a specified suffix.
     * <p>
     * {@code null}s are handled without exceptions. Two {@code null}
     * references are considered to be equal. The comparison is case sensitive.
     * <p>
     * <pre>{@code
     * StringUtils.endsWith(null, null)      = true
     * StringUtils.endsWith(null, "def")     = false
     * StringUtils.endsWith("abcdef", null)  = false
     * StringUtils.endsWith("abcdef", "def") = true
     * StringUtils.endsWith("ABCDEF", "def") = false
     * StringUtils.endsWith("ABCDEF", "cde") = false
     * StringUtils.endsWith("ABCDEF", "")    = true
     * }</pre>
     *
     * @param str    the CharSequence to check, may be null
     * @param suffix the suffix to find, may be null
     *
     * @return {@code true} if the CharSequence ends with the suffix, case sensitive, or both {@code null}
     *
     * @see java.lang.String#endsWith(String)
     */
    public static boolean endsWith( @Nullable final CharSequence str,
                                    @Nullable final CharSequence suffix )
    {
        return endsWith( str, suffix, false );
    }

    /**
     * Case insensitive check if a CharSequence ends with a specified suffix.
     * <p>
     * {@code null}s are handled without exceptions. Two {@code null}
     * references are considered to be equal. The comparison is case insensitive.
     * <p>
     * <pre>{@code
     * StringUtils.endsWithIgnoreCase(null, null)      = true
     * StringUtils.endsWithIgnoreCase(null, "def")     = false
     * StringUtils.endsWithIgnoreCase("abcdef", null)  = false
     * StringUtils.endsWithIgnoreCase("abcdef", "def") = true
     * StringUtils.endsWithIgnoreCase("ABCDEF", "def") = true
     * StringUtils.endsWithIgnoreCase("ABCDEF", "cde") = false
     * }</pre>
     *
     * @param str    the CharSequence to check, may be null
     * @param suffix the suffix to find, may be null
     *
     * @return {@code true} if the CharSequence ends with the suffix, case insensitive, or both {@code null}
     *
     * @see java.lang.String#endsWith(String)
     */
    public static boolean endsWithIgnoreCase( @Nullable final CharSequence str,
                                              @Nullable final CharSequence suffix )
    {
        return endsWith( str, suffix, true );
    }

    /**
     * Check if a CharSequence ends with a specified suffix (optionally case insensitive).
     *
     * @param str        the CharSequence to check, may be null
     * @param suffix     the suffix to find, may be null
     * @param ignoreCase indicates whether the compare should ignore case (case insensitive) or not.
     *
     * @return {@code true} if the CharSequence starts with the prefix or both {@code null}
     *
     * @see java.lang.String#endsWith(String)
     */
    private static boolean endsWith( @Nullable final CharSequence str,
                                     @Nullable final CharSequence suffix, final boolean ignoreCase )
    {
        if( str == null || suffix == null )
        {
            return str == suffix;
        }
        if( suffix.length() > str.length() )
        {
            return false;
        }
        final int strOffset = str.length() - suffix.length();
        return regionMatches( str, ignoreCase, strOffset, suffix, 0, suffix.length() );
    }


    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------

    //---------------------------------------------------------------------













    /**
     * Returns {@code true} if the given string is null or is the empty string.
     * <p>
     * <p>
     * Consider normalizing your string references with {@link #nullToEmpty}.
     * If you do, you can use {@link String#isEmpty()} instead of this method, and you won't need special null-safe forms
     * of methods like {@link String#toUpperCase} either.
     * Or, if you'd like to normalize "in the other direction," converting empty strings
     * to {@code null}, you can use {@link #emptyToNull}.
     *
     * @param string a string reference to check
     *
     * @return {@code true} if the string is null or is the empty string
     */
    public static boolean isNullOrEmpty( @Nullable String string )
    {
        return Strings.isNullOrEmpty( string );
    }


    //endregion


    //region Character checks

    //---------------------------------------------------------------------
    // Character checks
    //---------------------------------------------------------------------



    /**
     * Checks if a String is not empty ("") and not null.
     *
     * @param str the String to check, may be null
     *
     * @return {@code true} if the String is not empty and not null
     */
    public static boolean isNotNullOrEmpty( @Nullable final String str )
    {
        return !isNullOrEmpty( str );
    }



    //endregion





    /**
     * Turn the given Object into a {@code String} with single quotes
     * if it is a {@code String}; keeping the Object as-is else.
     *
     * @param obj the input Object (e.g. "myString")
     *
     * @return the quoted {@code String} (e.g. "'myString'"), or the input object as-is if not a {@code String}
     */
    @Nullable
    @CheckForNull
    public static Object quoteIfString( @Nullable Object obj )
    {
        return ( obj instanceof String ? quote( ( String ) obj ) : obj );
    }

    /**
     * Quote the given {@code String} with single quotes.
     *
     * @param str the input {@code String} (e.g. "myString")
     *
     * @return the quoted {@code String} (e.g. "'myString'"), or {@code null} if the input was {@code null}
     */
    @Nullable
    @CheckForNull
    public static String quote( @Nullable String str )
    {
        return ( str != null ? "'" + str + "'" : null );
    }

    //    /**
    //     * Unwraps a given string from anther string.
    //     *
    //     * @param str       the String to be unwrapped, can be null
    //     * @param wrapToken the String used to unwrap
    //     *
    //     * @return unwrapped String or the original string if it is not quoted properly with the wrapToken
    //     */
    //    @Nullable
    //    public static String unwrap( @Nullable final String str, @Nullable final String wrapToken )
    //    {
    //        return org.apache.commons.lang3.StringUtils.unwrap( str, wrapToken );
    //    }
    //
    //    /**
    //     * Unwraps a given string from a character.
    //     *
    //     * @param str      the String to be unwrapped, can be null
    //     * @param wrapChar the character used to unwrap
    //     *
    //     * @return unwrapped String or the original string if it is not quoted properly with the wrapChar
    //     */
    //    @Nullable
    //    public static String unwrap( @Nullable final String str, final char wrapChar )
    //    {
    //        return org.apache.commons.lang3.StringUtils.unwrap( str, wrapChar );
    //    }

    /**
     * Trim leading and trailing whitespace from the given {@code String}.
     *
     * @param str the {@code String} to check
     *
     * @return the trimmed {@code String}
     *
     * @see Character#isWhitespace
     * @see org.springframework.util.StringUtils#trimWhitespace(String)
     */
    public static String trimWhitespace( @Nullable String str )
    {
        return org.springframework.util.StringUtils.trimWhitespace( str );
    }

    /**
     * Trim leading whitespace from the given {@code String}.
     *
     * @param str the {@code String} to check
     *
     * @return the trimmed {@code String}
     *
     * @see org.springframework.util.StringUtils#trimLeadingWhitespace(String)
     */
    public static String trimLeadingWhitespace( String str )
    {
        return org.springframework.util.StringUtils.trimLeadingWhitespace( str );
    }

    /**
     * Trim trailing whitespace from the given {@code String}.
     *
     * @param str the {@code String} to check
     *
     * @return the trimmed {@code String}
     *
     * @see org.springframework.util.StringUtils#trimTrailingWhitespace(String)
     */
    public static String trimTrailingWhitespace( String str )
    {
        return org.springframework.util.StringUtils.trimTrailingWhitespace( str );
    }

    /**
     * Removes control characters (char &lt;= 32) from both ends of this String returning {@code null} if the String is
     * empty ("") after the trim or if it is {@code null}.
     * <p>The String is trimmed using {@link String#trim()}.
     * Trim removes start and end characters &lt;= 32. To strip whitespace use {@link #stripToNull(String)}.
     *
     * @param str the String to be trimmed, may be null
     *
     * @return the trimmed String, {@code null} if only chars &lt;= 32, empty or null String input
     */
    @Nullable
    public static String trimToNull( @Nullable final String str )
    {
        if( null == str ) return null;
        String trim = str.trim();
        return emptyToNull( trim );
    }



    /**
     * Removes control characters (char &lt;= 32) from both ends of this String returning an empty String ("") if the String
     * is empty ("") after the trim or if it is {@code null}.
     * <p>
     * <p>The String is trimmed using {@link String#trim()}.
     * Trim removes start and end characters &lt;= 32.
     * To strip whitespace use {@link #stripToEmpty(String)}.
     *
     * @param str the String to be trimmed, may be null
     *
     * @return the trimmed String, or an empty String if {@code null} input
     */
    public static String trimToEmpty( @Nullable final String str )
    {
        return org.apache.commons.lang3.StringUtils.trimToEmpty( str );
    }

    /**
     * Trim all occurrences of the supplied leading character from the given {@code String}.
     *
     * @param str              the {@code String} to check
     * @param leadingCharacter the leading character to be trimmed
     *
     * @return the trimmed {@code String}
     */
    @SuppressWarnings( "ConstantConditions" )
    public static String trimLeadingCharacter( @Nullable String str, char leadingCharacter )
    {
        return org.springframework.util.StringUtils.trimLeadingCharacter( str, leadingCharacter );
    }

    /**
     * Trim all occurrences of the supplied trailing character from the given {@code String}.
     *
     * @param str               the {@code String} to check
     * @param trailingCharacter the trailing character to be trimmed
     *
     * @return the trimmed {@code String}
     */
    @SuppressWarnings( "ConstantConditions" )
    @Nullable
    public static String trimTrailingCharacter( @Nullable String str, char trailingCharacter )
    {
        return org.springframework.util.StringUtils.trimTrailingCharacter( str, trailingCharacter );
    }

    /**
     * Truncates a String.
     * This will turn "Now is the time for all good men" into "is the time for all".
     * <p>
     * Works like {@code truncate(String, int)}, but allows you to specify a "left edge" offset.
     * <p>Specifically:
     * <ul>
     * <li>If {@code str} is less than {@code maxWidth} characters long, return it.</li>
     * <li>Else truncate it to {@code substring(str, offset, maxWidth)}.</li>
     * <li>If {@code maxWidth} is less than {@code 0}, throw an {@code IllegalArgumentException}.</li>
     * <li>If {@code offset} is less than {@code 0}, throw an {@code IllegalArgumentException}.</li>
     * <li>In no case will it return a String of length greater than {@code maxWidth}.</li>
     * </ul>
     *
     * @param str      the String to check, may be null
     * @param offset   left edge of source String
     * @param maxWidth maximum length of result String, must be positive
     *
     * @return truncated String, {@code null} if null String input
     */
    @Nullable
    public static String truncate( @Nullable final String str, final int offset, final int maxWidth )
    {
        return org.apache.commons.lang3.StringUtils.truncate( str, offset, maxWidth );
    }


    /**
     * Strips whitespace from the start and end of a String  returning {@code null} if the String is empty ("") after the strip.
     * <p>
     * This is similar to {@linkplain #trimToNull(String)} but removes whitespace.
     * Whitespace is defined by {@linkplain Character#isWhitespace(char)}.
     *
     * @param str the String to be stripped, may be null
     *
     * @return the stripped String, {@code null} if whitespace, empty or null String input
     */
    @Nullable
    public static String stripToNull( @Nullable String str )
    {
        return org.apache.commons.lang3.StringUtils.stripToNull( str );
    }

    /**
     * Strips whitespace from the start and end of a String  returning an empty String if {@code null} input.
     * <p>
     * This is similar to {@link #trimToEmpty(String)} but removes whitespace.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     *
     * @param str the String to be stripped, may be null
     *
     * @return the trimmed String, or an empty String if {@code null} input
     */
    public static String stripToEmpty( @Nullable final String str )
    {
        return org.apache.commons.lang3.StringUtils.stripToEmpty( str );
    }








    /**
     * Case in-sensitive find of the first index within a CharSequence.
     * <p>
     * A {@code null} CharSequence will return {@code -1}.
     * A negative start position is treated as zero.
     * An empty ("") search CharSequence always matches.
     * A start position greater than the string length only matches an empty search CharSequence.
     *
     * @param str       the CharSequence to check, may be null
     * @param searchStr the CharSequence to find, may be null
     *
     * @return the first index of the search CharSequence, -1 if no match or {@code null} string input
     */
    public static int indexOfIgnoreCase( final CharSequence str, final CharSequence searchStr )
    {
        return indexOfIgnoreCase( str, searchStr, 0 );
    }

    /**
     * Case in-sensitive find of the first index within a CharSequence from the specified position.
     * <p>
     * A {@code null} CharSequence will return {@code -1}.
     * A negative start position is treated as zero.
     * An empty ("") search CharSequence always matches.
     * A start position greater than the string length only matches an empty search CharSequence.
     *
     * @param str       the CharSequence to check, may be null
     * @param searchStr the CharSequence to find, may be null
     * @param startPos  the start position, negative treated as zero
     *
     * @return the first index of the search CharSequence (always &ge; startPos), -1 if no match or {@code null} string input
     */
    public static int indexOfIgnoreCase( @Nullable final CharSequence str,
                                         @Nullable final CharSequence searchStr,
                                         int startPos )
    {
        return org.apache.commons.lang3.StringUtils.indexOfIgnoreCase( str, searchStr, startPos );
    }

    /**
     * Returns the index within {@code seq} of the last occurrence of the specified character.
     * For values of {@code searchChar} in the range from 0 to 0xFFFF (inclusive), the index (in Unicode code units) returned is the largest value <i>k</i> such that:
     * <blockquote><pre>
     * this.charAt(<i>k</i>) == searchChar
     * re></blockquote>
     * is true. For other values of {@code searchChar}, it is the largest value <i>k</i> such that:
     * <blockquote><pre>
     * this.codePointAt(<i>k</i>) == searchChar
     * re></blockquote>
     * is true.  In either case, if no such character occurs in this string, then {@code -1} is returned.
     * Furthermore, a {@code null} or empty ("") {@code CharSequence} will return {@code -1}. The
     * {@code seq} {@code CharSequence} object is searched backwards  starting at the last character.
     *
     * @param seq        the {@code CharSequence} to check, may be null
     * @param searchChar the character to find
     *
     * @return the last index of the search character, -1 if no match or {@code null} string input
     */
    public static int lastIndexOf( @Nullable final CharSequence seq, final int searchChar )
    {
        return org.apache.commons.lang3.StringUtils.lastIndexOf( seq, searchChar );
    }

    /**
     * Returns the index within {@code seq} of the last occurrence of the specified character, searching backward starting at the specified index.
     * For values of {@code searchChar} in the range from 0 to 0xFFFF (inclusive), the index returned is the largest
     * value <i>k</i> such that:
     * <blockquote><pre>
     * (this.charAt(<i>k</i>) == searchChar) &amp;&amp; (<i>k</i> &lt;= startPos)
     * re></blockquote>
     * is true. For other values of {@code searchChar}, it is the largest value <i>k</i> such that:
     * <blockquote><pre>
     * (this.codePointAt(<i>k</i>) == searchChar) &amp;&amp; (<i>k</i> &lt;= startPos)
     * re></blockquote> is true.
     * In either case, if no such character occurs in {@code seq} at or before position {@code startPos}, then {@code -1} is returned.
     * Furthermore, a {@code null} or empty ("") {@code CharSequence} will return {@code -1}.
     * A start position greater than the string length searches the whole string.
     * The search starts at the {@code startPos} and works backwards;
     * matches starting after the start position are ignored.
     * <p>
     * All indices are specified in {@code char} values ( Unicode code units ).
     *
     * @param seq        the CharSequence to check, may be null
     * @param searchChar the character to find
     * @param startPos   the start position
     *
     * @return the last index of the search character (always &le; startPos), -1 if no match or {@code null} string input
     */
    public static int lastIndexOf( @Nullable final CharSequence seq,
                                   final int searchChar,
                                   final int startPos )
    {
        return org.apache.commons.lang3.StringUtils.lastIndexOf( seq, searchChar, startPos );
    }

    //    /**
    //     * <p>Removes control characters (char &lt;= 32) from both
    //     * ends of this String returning an empty String ("") if the String
    //     * is empty ("") after the trim or if it is {@code null}.
    //     *
    //     * <p>The String is trimmed using {@link String#trim()}.
    //     * Trim removes start and end characters &lt;= 32.
    //     * To strip whitespace use {@link #stripToEmpty(String)}.
    //     *
    //     * <pre>
    //     * StringUtils.trimToEmpty(null)          = ""
    //     * StringUtils.trimToEmpty("")            = ""
    //     * StringUtils.trimToEmpty("     ")       = ""
    //     * StringUtils.trimToEmpty("abc")         = "abc"
    //     * StringUtils.trimToEmpty("    abc    ") = "abc"
    //     * re>
    //     *
    //     * @param str  the String to be trimmed, may be null
    //     * @return the trimmed String, or an empty String if {@code null} input
    //     * @since 2.0
    //     */
    //    public static String trimToEmpty(final String str) {
    //        return str == null ? EMPTY : str.trim();
    //    }

    //endregion

    /**
     * Finds the last index within a CharSequence, handling {@code null}.
     * This method uses {@link String#lastIndexOf(String)} if possible.
     * <p>
     * A {@code null} CharSequence will return {@code -1}.
     *
     * @param seq       the CharSequence to check, may be {@code null}
     * @param searchSeq the CharSequence to find, may be {@code null}
     *
     * @return the last index of the search String, -1 if no match or {@code null} string input
     */
    public static int lastIndexOf( @Nullable final CharSequence seq, final CharSequence searchSeq )
    {
        return org.apache.commons.lang3.StringUtils.lastIndexOf( seq, searchSeq );
    }

    /**
     * Finds the n-th last index within a String, handling {@code null}.
     * This method uses {@link String#lastIndexOf(String)}.
     * <p>
     * A {@code null} String will return {@code -1}.
     *
     * @param str       the CharSequence to check, may be null
     * @param searchStr the CharSequence to find, may be null
     * @param ordinal   the n-th last {@code searchStr} to find
     *
     * @return the n-th last index of the search CharSequence, {@code -1} ({@code INDEX_NOT_FOUND}) if no match or {@code null} string input
     */
    public static int lastOrdinalIndexOf( @Nullable final CharSequence str, final CharSequence searchStr, final int ordinal )
    {
        return org.apache.commons.lang3.StringUtils.lastOrdinalIndexOf( str, searchStr, ordinal );
    }

    /**
     * Finds the last index within a CharSequence, handling {@code null}.
     * This method uses {@link String#lastIndexOf(String, int)} if possible.
     * <p>
     * A {@code null} CharSequence will return {@code -1}.
     * A negative start position returns {@code -1}.
     * An empty ("") search CharSequence always matches unless the start position is negative.
     * A start position greater than the string length searches the whole string.
     * The search starts at the startPos and works backwards; matches starting after the start position are ignored.
     *
     * @param seq       the CharSequence to check, may be null
     * @param searchSeq the CharSequence to find, may be null
     * @param startPos  the start position, negative treated as zero
     *
     * @return the last index of the search CharSequence (always &le; startPos), -1 if no match or {@code null} string input
     */
    public static int lastIndexOf( @Nullable final CharSequence seq, final CharSequence searchSeq, final int startPos )
    {
        return org.apache.commons.lang3.StringUtils.lastIndexOf( seq, searchSeq, startPos );
    }

    /**
     * Case in-sensitive find of the last index within a CharSequence.
     * <p>
     * A {@code null} CharSequence will return {@code -1}.
     * A negative start position returns {@code -1}.
     * An empty ("") search CharSequence always matches unless the start position is negative.
     * A start position greater than the string length searches the whole string.
     *
     * @param str       the CharSequence to check, may be null
     * @param searchStr the CharSequence to find, may be null
     *
     * @return the first index of the search CharSequence, -1 if no match or {@code null} string input
     */
    public static int lastIndexOfIgnoreCase( @Nullable final CharSequence str, final CharSequence searchStr )
    {
        return org.apache.commons.lang3.StringUtils.lastIndexOfIgnoreCase( str, searchStr );
    }

    /**
     * Case in-sensitive find of the last index within a CharSequence from the specified position.
     * <p>
     * A {@code null} CharSequence will return {@code -1}.
     * A negative start position returns {@code -1}.
     * An empty ("") search CharSequence always matches unless the start position is negative.
     * A start position greater than the string length searches the whole string.
     * The search starts at the startPos and works backwards; matches starting after the start position are ignored.
     *
     * @param str       the CharSequence to check, may be null
     * @param searchStr the CharSequence to find, may be null
     * @param startPos  the start position
     *
     * @return the last index of the search CharSequence (always &le; startPos), -1 if no match or {@code null} input
     */
    public static int lastIndexOfIgnoreCase( @Nullable final CharSequence str, @Nullable final CharSequence searchStr, int startPos )
    {
        return org.apache.commons.lang3.StringUtils.lastIndexOfIgnoreCase( str, searchStr, startPos );
    }


    /**
     * Test whether the given string matches the given substring at the given index.
     *
     * @param str       the original string (or StringBuilder)
     * @param index     the index in the original string to start matching against
     * @param substring the substring to match at the given index
     */
    public static boolean substringMatch( @Nullable CharSequence str, int index, CharSequence substring )
    {
        return org.springframework.util.StringUtils.substringMatch( str, index, substring );
    }


    //endregion





    /**
     * Case insensitive removal of all occurrences of a substring from within the source string.
     * <p>
     * A {@code null} source string will return {@code null}.
     * An empty ("") source string will return the empty string. A {@code null} remove string
     * will return the source string. An empty ("") remove string will return the source string.
     *
     * @param str    the source String to search, may be null
     * @param remove the String to search for (case insensitive) and remove, may be  null
     *
     * @return the substring with the string removed if found, {@code null} if null String input
     */
    public static String removeIgnoreCase( final String str, final String remove )
    {
        return org.apache.commons.lang3.StringUtils.removeEndIgnoreCase( str, remove );
    }


    /**
     * Case insensitively replaces a String with another String inside a larger String, once.
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for (case insensitive), may be null
     * @param replacement  the String to replace with, may be null
     *
     * @return the text with any replacements processed, {@code null} if null String input
     *
     * @see #replaceIgnoreCase(String text, String searchString, String replacement, int max)
     */
    public static String replaceOnceIgnoreCase( final String text, final String searchString, final String replacement )
    {
        return org.apache.commons.lang3.StringUtils.replaceIgnoreCase( text, searchString, replacement );
    }



    /**
     * Replaces each substring of the text String that matches the given regular expression with the given replacement.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     * <li>{@code text.replaceAll(regex, replacement)}</li>
     * <li>{@code Pattern.compile(regex).matcher(text).replaceAll(replacement)}</li>
     * </ul>
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * @param text        text to search and replace in, may be null
     * @param regex       the regular expression to which this string is to be matched
     * @param replacement the string to be substituted for each match
     *
     * @return the text with any replacements processed, {@code null} if null String input
     *
     * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
     * @see #replacePattern(String, String, String)
     * @see String#replaceAll(String, String)
     * @see java.util.regex.Pattern
     * @see java.util.regex.Pattern#DOTALL
     */
    @Nullable
    public static String replaceAll( @Nullable final String text,
                                     @Nullable final String regex,
                                     @Nullable final String replacement )
    {
        if( text == null || regex == null || replacement == null )
        {
            return text;
        }
        return text.replaceAll( regex, replacement );
    }

    /**
     * Replaces the first substring of the text string that matches the given regular expression with the given replacement.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     * <li>{@code text.replaceFirst(regex, replacement)}</li>
     * <li>{@code Pattern.compile(regex).matcher(text).replaceFirst(replacement)}</li>
     * </ul>
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     * <p>
     * The {@link java.util.regex.Pattern#DOTALL} option is NOT automatically added.
     *
     * @param text        text to search and replace in, may be null
     * @param regex       the regular expression to which this string is to be matched
     * @param replacement the string to be substituted for the first match
     *
     * @return the text with the first replacement processed, {@code null} if null String input
     *
     * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
     * @see String#replaceFirst(String, String)
     * @see java.util.regex.Pattern
     * @see java.util.regex.Pattern#DOTALL
     */
    @Nullable
    public static String replaceFirst( @Nullable final String text, @Nullable final String regex, @Nullable final String replacement )
    {
        return org.apache.commons.lang3.StringUtils.replaceFirst( text, regex, replacement );
    }

    /**
     * Replaces all occurrences of Strings within another String.
     * <p>
     * A {@code null} reference passed to this method is a no-op, or if any "search string" or "string to replace" is null, that replace will be
     * ignored. This will not repeat. For repeating replaces, call the overloaded method.
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     *
     * @return the text with any replacements processed, {@code null} if null String input
     *
     * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok, and/or size 0)
     */
    public static String replaceEach( final String text, final String[] searchList, final String[] replacementList )
    {
        return org.apache.commons.lang3.StringUtils.replaceEach( text, searchList, replacementList );
    }

    /**
     * Replaces all occurrences of Strings within another String.
     * <p>
     * A {@code null} reference passed to this method is a no-op, or if any "search string" or "string to replace" is null, that replace will be ignored.
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     *
     * @return the text with any replacements processed, {@code null} if null String input
     *
     * @throws IllegalStateException    if the search is repeating and there is an endless loop due
     *                                  to outputs of one being inputs to another
     * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok, and/or size 0)
     */
    public static String replaceEachRepeatedly( final String text, final String[] searchList, final String[] replacementList )
    {
        return org.apache.commons.lang3.StringUtils.replaceEachRepeatedly( text, searchList, replacementList );
    }

    /**
     * <p>
     * Replace all occurrences of Strings within another String.
     * This is a private recursive helper method for {@link #replaceEachRepeatedly(String, String[], String[])} and
     * {@link #replaceEach(String, String[], String[])}
     * <p>
     * <p>
     * <p>
     * A {@code null} reference passed to this method is a no-op, or if
     * any "search string" or "string to replace" is null, that replace will be
     * ignored.
     * <p>
     * <p>
     * <pre>
     *  StringUtils.replaceEach(null, *, *, *, *) = null
     *  StringUtils.replaceEach("", *, *, *, *) = ""
     *  StringUtils.replaceEach("aba", null, null, *, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[0], null, *, *) = "aba"
     *  StringUtils.replaceEach("aba", null, new String[0], *, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, null, *, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, new String[]{""}, *, >=0) = "b"
     *  StringUtils.replaceEach("aba", new String[]{null}, new String[]{"a"}, *, >=0) = "aba"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *, >=0) = "wcte"
     *  (example of how it repeats)
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false, >=0) = "dcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true, >=2) = "tcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, *, *) = IllegalStateException
     * re>
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @param repeat          if true, then replace repeatedly
     *                        until there are no more possible replacements or timeToLive < 0
     * @param timeToLive      if less than 0 then there is a circular reference and endless
     *                        loop
     *
     * @return the text with any replacements processed, {@code null} if
     *         null String input
     *
     * @throws IllegalStateException    if the search is repeating and there is an endless loop due
     *                                  to outputs of one being inputs to another
     * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok,
     *                                  and/or size 0)
     * @since 2.4
     */
    private static String replaceEach( final String text, final String[] searchList, final String[] replacementList, final boolean repeat, final int timeToLive )
    {

        // mchyzer Performance note: This creates very few new objects (one major goal)
        // let me know if there are performance requests, we can create a harness to measure

        if( text == null || text.isEmpty() || searchList == null ||
                    searchList.length == 0 || replacementList == null || replacementList.length == 0 )
        {
            return text;
        }

        // if recursing, this shouldn't be less than 0
        if( timeToLive < 0 )
        {
            throw new IllegalStateException( "Aborting to protect against StackOverflowError - " +
                                                     "output of one loop is the input of another" );
        }

        final int searchLength = searchList.length;
        final int replacementLength = replacementList.length;

        // make sure lengths are ok, these need to be equal
        if( searchLength != replacementLength )
        {
            throw new IllegalArgumentException( "Search and Replace array lengths don't match: "
                                                        + searchLength
                                                        + " vs "
                                                        + replacementLength );
        }

        // keep track of which still have matches
        final boolean[] noMoreMatchesForReplIndex = new boolean[ searchLength ];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for( int i = 0; i < searchLength; i++ )
        {
            if( noMoreMatchesForReplIndex[ i ] || searchList[ i ] == null ||
                        searchList[ i ].isEmpty() || replacementList[ i ] == null )
            {
                continue;
            }
            tempIndex = text.indexOf( searchList[ i ] );

            // see if we need to keep searching for this
            if( tempIndex == -1 )
            {
                noMoreMatchesForReplIndex[ i ] = true;
            }
            else
            {
                if( textIndex == -1 || tempIndex < textIndex )
                {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if( textIndex == -1 )
        {
            return text;
        }

        int start = 0;

        // get a good guess on the size of the result buffer so it doesn't have to double if it goes over a bit
        int increase = 0;

        // count the replacement text elements that are larger than their corresponding text being replaced
        for( int i = 0; i < searchList.length; i++ )
        {
            if( searchList[ i ] == null || replacementList[ i ] == null )
            {
                continue;
            }
            final int greater = replacementList[ i ].length() - searchList[ i ].length();
            if( greater > 0 )
            {
                increase += 3 * greater; // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min( increase, text.length() / 5 );

        final StringBuilder buf = new StringBuilder( text.length() + increase );

        while( textIndex != -1 )
        {

            for( int i = start; i < textIndex; i++ )
            {
                buf.append( text.charAt( i ) );
            }
            buf.append( replacementList[ replaceIndex ] );

            start = textIndex + searchList[ replaceIndex ].length();

            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            // find the next earliest match
            // NOTE: logic mostly duplicated above START
            for( int i = 0; i < searchLength; i++ )
            {
                if( noMoreMatchesForReplIndex[ i ] || searchList[ i ] == null ||
                            searchList[ i ].isEmpty() || replacementList[ i ] == null )
                {
                    continue;
                }
                tempIndex = text.indexOf( searchList[ i ], start );

                // see if we need to keep searching for this
                if( tempIndex == -1 )
                {
                    noMoreMatchesForReplIndex[ i ] = true;
                }
                else
                {
                    if( textIndex == -1 || tempIndex < textIndex )
                    {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
            // NOTE: logic duplicated above END

        }
        final int textLength = text.length();
        for( int i = start; i < textLength; i++ )
        {
            buf.append( text.charAt( i ) );
        }
        final String result = buf.toString();
        if( !repeat )
        {
            return result;
        }

        return replaceEach( result, searchList, replacementList, repeat, timeToLive - 1 );
    }











    //endregion


    //region Splitting

    //---------------------------------------------------------------------
    // Splitting
    //---------------------------------------------------------------------

    private static boolean onWordBoundary( String name, int index )
    {
        return ( uppercaseLetterAt( name, index )
                         && ( lowercaseLetterAt( name, index - 1 ) || lowercaseLetterAt( name, index + 1 ) ) );
    }

    private static String lowercaseOrAcronym( String word )
    {
        if( isAllUpperCase( word ) && word.length() > 1 )
        {
            return word;
        }
        else
        {
            return org.apache.commons.lang3.StringUtils.lowerCase( word );
        }
    }

    private static boolean uppercaseLetterAt( String name, int index )
    {
        return CharUtils.isAsciiAlphaUpper( name.charAt( index ) );
    }

    private static boolean lowercaseLetterAt( String name, int index )
    {
        return ( index >= 0 )
                       && ( index < name.length() )
                       && CharUtils.isAsciiAlphaLower( name.charAt( index ) );
    }









    //endregion







    //    /**
    //     * Abbreviates a String using another given String as replacement marker.
    //     * This will turn "Now is the time for all good men" into "Now is the time for..." if "..." was defined
    //     * as the replacement marker.
    //     * <p>Specifically:
    //     * <ul>
    //     * <li>If the number of characters in {@code str} is less than or equal to
    //     * {@code maxWidth}, return {@code str}.</li>
    //     * <li>Else abbreviate it to {@code (substring(str, 0, max-abbrevMarker.length) + abbrevMarker)}.</li>
    //     * <li>If {@code maxWidth} is less than {@code abbrevMarker.length + 1}, throw an
    //     * {@code IllegalArgumentException}.</li>
    //     * <li>In no case will it return a String of length greater than
    //     * {@code maxWidth}.</li>
    //     * </ul>
    //     *
    //     * @param str          the String to check, may be null
    //     * @param abbrevMarker the String used as replacement marker
    //     * @param maxWidth     maximum length of result String, must be at least {@code abbrevMarker.length + 1}
    //     *
    //     * @return abbreviated String, {@code null} if null String input
    //     *
    //     * @throws IllegalArgumentException if the width is too small
    //     */
    //    @Nullable
    //    public static String abbreviate( @Nullable final String str,
    //                                     final String abbreviate,
    //                                     final int maxWidth )
    //    {
    //        return org.apache.commons.lang3.StringUtils.abbreviate( str, abbreviate, maxWidth );
    //    }

    //    /**
    //     * Abbreviates a String using a given replacement marker.
    //     * This will turn "Now is the time for all good men" into "...is the time for..." if "..." was defined
    //     * as the replacement marker.
    //     * <p>
    //     * Works like {@code abbreviate(String, String, int)}, but allows you to specify a "left edge" offset.
    //     * Note that this left edge is not necessarily going to be the leftmost character in the result, or the first character following the
    //     * replacement marker, but it will appear somewhere in the result.
    //     *
    //     * @param str          the String to check, may be null
    //     * @param abbrevMarker the String used as replacement marker
    //     * @param offset       left edge of source String
    //     * @param maxWidth     maximum length of result String, must be at least 4
    //     *
    //     * @return abbreviated String, {@code null} if null String input
    //     *
    //     * @throws IllegalArgumentException if the width is too small
    //     * @implNote In no case will it return a String of length greater than {@code maxWidth}.
    //     */
    //    @Nullable
    //    public static String abbreviate( @Nullable final String str, final String abbrevMarker, int offset, final int maxWidth )
    //    {
    //        return org.apache.commons.lang3.StringUtils.abbreviate( str, abbrevMarker, offset, maxWidth );
    //    }

    //endregion


    //region Default Value

    //---------------------------------------------------------------------
    // Default Value
    //---------------------------------------------------------------------

    //    /**
    //     * Abbreviates a String to the length passed, replacing the middle characters with the supplied replacement String.
    //     * <p>
    //     * This abbreviation only occurs if the following criteria is met:
    //     * <ul>
    //     * <li>Neither the String for abbreviation nor the replacement String are null or empty </li>
    //     * <li>The length to truncate to is less than the length of the supplied String</li>
    //     * <li>The length to truncate to is greater than 0</li>
    //     * <li>The abbreviated String will have enough room for the length supplied replacement String
    //     * and the first and last characters of the supplied String for abbreviation
    //     * </li>
    //     * </ul>
    //     * <p>
    //     * <p>
    //     * Otherwise, the returned String will be the same as the supplied String for abbreviation.
    //     *
    //     * @param str    the String to abbreviate, may be null
    //     * @param middle the String to replace the middle characters with, may be null
    //     * @param length the length to abbreviate {@code str} to.
    //     *
    //     * @return the abbreviated String if the above criteria is met, or the original String supplied for abbreviation.
    //     */
    //    public static String abbreviateMiddle( final String str, final String middle, final int length )
    //    {
    //        return org.apache.commons.lang3.StringUtils.abbreviate( str, middle, length );
    //    }

    /**
     * Checks if a CharSequence is empty (""), null or whitespace only.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     *
     * @param cs the CharSequence to check, may be null
     *
     * @return {@code true} if the CharSequence is null, empty or whitespace only
     */
    public static boolean isBlank( @Nullable final CharSequence cs )
    {
        return org.apache.commons.lang3.StringUtils.isBlank( cs );
    }





    //---------------------------------------------------------------------
    // Contains
    //---------------------------------------------------------------------



    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given {@code CharSequence} contains any whitespace characters.
     *
     * @param cs the {@code CharSequence} to check (may be {@code null})
     *
     * @return {@code true} if the {@code CharSequence} is not empty and contains at least 1 whitespace character
     *
     * @see com.google.common.base.CharMatcher#whitespace()
     */
    public static boolean containsWhitespace( @Nullable CharSequence cs )
    {
        return hasLength( cs ) && CharMatcher.whitespace().countIn( cs ) > 0;
    }

    /**
     * Check whether the given {@code String} contains any whitespace characters.
     *
     * @param str the {@code String} to check (may be {@code null})
     *
     * @return {@code true} if the {@code String} is not empty and contains at least 1 whitespace character
     *
     * @see #containsWhitespace(CharSequence)
     */
    public static boolean containsWhitespace( @Nullable String str )
    {
        return containsWhitespace( ( CharSequence ) str );
    }





    //endregion


    //region Convenience methods for working with formatted Strings

    //---------------------------------------------------------------------
    // Convenience methods for working with formatted Strings
    //---------------------------------------------------------------------

    public static List<String> splitAndTrim( String delimiter, String str )
    {
        return Pattern.compile( delimiter )
                       .splitAsStream( str )
                       .map( String:: trim )
                       .collect( Collectors.toList() );
    }




    /**
     * Rotate (circular shift) a String of {@code shift} characters.
     * <ul>
     * <li>If {@code shift > 0}, right circular shift (ex : ABCDEF =&gt; FABCDE)</li>
     * <li>If {@code shift < 0}, left circular shift (ex : ABCDEF =&gt; BCDEFA)</li>
     * </ul>
     *
     * @param str   the String to rotate, may be null
     * @param shift number of time to shift (positive : right shift, negative : left shift)
     *
     * @return the rotated String, or the original String if {@code shift == 0}, or {@code null} if null String input
     */
    @Nullable
    public static String rotate( @Nullable final String str, @Nonnegative final int shift )
    {
        return org.apache.commons.lang3.StringUtils.rotate( str, shift );
    }


    //endregion










    /**
     * Tokenize the given {@code String} into a {@code String} array via a
     * {@link java.util.StringTokenizer}.
     * <p>Trims tokens and omits empty tokens.
     * <p>The given {@code delimiters} string can consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using {@link #delimitedListToStringArray}.
     *
     * @param str        the {@code String} to tokenize
     * @param delimiters the delimiter characters, assembled as a {@code String}
     *                   (each of the characters is individually considered as a delimiter)
     *
     * @return an array of the tokens
     *
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray( @Nullable String str, String delimiters )
    {
        return tokenizeToStringArray( str, delimiters, true, true );
    }

    /**
     * Tokenize the given {@code String} into a {@code String} array via a
     * {@link java.util.StringTokenizer}.
     * <p>The given {@code delimiters} string can consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using {@link #delimitedListToStringArray}.
     *
     * @param str               the {@code String} to tokenize
     * @param delimiters        the delimiter characters, assembled as a {@code String}
     *                          (each of the characters is individually considered as a delimiter)
     * @param trimTokens        trim the tokens via {@link String#trim()}
     * @param ignoreEmptyTokens omit empty tokens from the result array
     *                          (only applies to tokens that are empty after trimming; StringTokenizer
     *                          will not consider subsequent delimiters as token in the first place).
     *
     * @return an array of the tokens
     *
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray( @Nullable String str,
                                                  String delimiters,
                                                  boolean trimTokens,
                                                  boolean ignoreEmptyTokens )
    {

        if( str == null )
        {
            return new String[ 0 ];
        }

        StringTokenizer st = new StringTokenizer( str, delimiters );
        List<String> tokens = new ArrayList<>();
        while( st.hasMoreTokens() )
        {
            String token = st.nextToken();
            if( trimTokens )
            {
                token = token.trim();
            }
            if( !ignoreEmptyTokens || token.length() > 0 )
            {
                tokens.add( token );
            }
        }
        return org.springframework.util.StringUtils.toStringArray( tokens );
    }



    public static List<String> commaDelimitedListToList( @Nullable String str )
    {
        if( str == null )
        {
            return Lists.newArrayListWithExpectedSize( 0 );
        }

        String commaSeparated = trimAllWhitespace( str );
        return Stream.of( commaSeparated.split( "," ) )
                       .collect( Collectors.toList() );
    }




    //
    //
    //    public Response toResponse(ConstraintViolationException exception) {
    //        LOGGER.debug("Validation constraint violation {}", exception.getConstraintViolations());
    //        ValidationMessage validationMessage = new ValidationMessage();
    //        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
    //        Multimap<String, String> errors = ArrayListMultimap.create();
    //        for (ConstraintViolation<?> cv : violations) {
    //            String name = StreamSupport.stream(cv.getPropertyPath().spliterator(), false).map(Path.Node::getName).reduce(( first,  second) -> second).orElseGet(() -> cv.getPropertyPath().toString());
    //            errors.put(name, cv.getMessage());
    //        }
    //        validationMessage.setErrors(errors.asMap());
    //        return Response.status(Response.Status.BAD_REQUEST).entity(validationMessage).build();
    //    }



    /**
     * Know if a string is a valid e-mail.
     *
     * @param str a string
     *
     * @return true if {@code str} is syntactically a valid e-mail address
     *
     * @since 2.1
     */
    public static boolean isEmail( String str )
    {
        return str.matches(
                "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+((\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)?)+@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9\\-]*[a-zA-Z0-9])?$" );
    }

    /**
     * Convert a String to MD5.
     *
     * @param toEncode string concerned
     *
     * @return md5 corresponding
     *
     * @throws IllegalStateException if could not found algorithm MD5
     */
    //    public static String encodeMD5( String toEncode )
    //    {
    //
    //        byte[] uniqueKey = toEncode.getBytes();
    //        byte[] hash;
    //        // on récupère un objet qui permettra de crypter la chaine
    //        hash = MD5InputStream.getMD5Digest().digest( uniqueKey );
    //        //        hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
    //
    //        StringBuilder hashString = new StringBuilder();
    //        for( byte aHash : hash )
    //        {
    //            String hex = Integer.toHexString( aHash );
    //            if( hex.length() == 1 )
    //            {
    //                hashString.append( "0" );
    //                hashString.append( hex.charAt( hex.length() - 1 ) );
    //            }
    //            else
    //            {
    //                hashString.append( hex.substring( hex.length() - 2 ) );
    //            }
    //        }
    //        return hashString.toString();
    //    }

    /**
     * Convert a String to SHA1.
     *
     * @param toEncode string to encode
     *
     * @return sha1 corresponding
     *
     * @throws IllegalStateException if could not found algorithm SHA1
     */
    public static String encodeSHA1( String toEncode )
    {
        String result;

        try
        {
            MessageDigest sha1Md = MessageDigest.getInstance( "SHA-1" );

            byte[] digest = sha1Md.digest( toEncode.getBytes() );
            result = asHex( digest );
        }
        catch( NoSuchAlgorithmException ex )
        {
            throw new IllegalStateException( "Can't find SHA-1 message digest algorithm", ex );
        }

        return result;
    }

    /**
     * Turns array of bytes into string representing each byte as
     * unsigned hex number.
     *
     * @param hash Array of bytes to convert to hex-string
     *
     * @return Generated hex string
     */
    public static String asHex( byte hash[] )
    {
        char buf[] = new char[ hash.length * 2 ];
        for( int i = 0, x = 0; i < hash.length; i++ )
        {
            buf[ x++ ] = HEX_CHARS[ hash[ i ] >>> 4 & 0xf ];
            buf[ x++ ] = HEX_CHARS[ hash[ i ] & 0xf ];
        }
        return new String( buf );
    }

    /**
     * Constructs a new {@code String} by decoding the specified array of bytes using the UTF-8 charset.
     *
     * @param bytes The bytes to be decoded into characters
     *
     * @return A new {@code String} decoded from the specified array of bytes using the UTF-8 charset,
     *         or {@code null} if the input byte array was {@code null}.
     */
    public static String newStringUtf8( final byte[] bytes )
    {
        return newString( bytes, StandardCharsets.UTF_8 );
    }

    /**
     * Constructs a new {@code String} by decoding the specified array of bytes using the given charset.
     *
     * @param bytes   The bytes to be decoded into characters
     * @param charset The {@link java.nio.charset.Charset} to encode the {@code String}
     *
     * @return A new {@code String} decoded from the specified array of bytes using the given charset,
     *         or {@code null} if the input byte array was {@code null}.
     */
    private static String newString( final byte[] bytes, final Charset charset )
    {
        return bytes == null ? null : new String( bytes, charset );
    }

    /**
     * Find the Levenshtein distance between two Strings.
     * <p>
     * This is the number of changes needed to change one String into another, where each change is a single character modification
     * (deletion, insertion or substitution).
     *
     * @param s the first String, must not be null
     * @param t the second String, must not be null
     *
     * @return result distance
     */
    public static int getLevenshteinDistance( CharSequence s, CharSequence t )
    {
        LevenshteinDistance ld = new LevenshteinDistance();
        return ld.apply( s, t );
    }

    /**
     * Find the Levenshtein distance between two Strings if it's less than or equal to a given threshold.
     * <p>
     * This is the number of changes needed to change one String into another, where each change is a single character modification
     * (deletion, insertion or substitution).
     *
     * @param s         the first String, must not be null
     * @param t         the second String, must not be null
     * @param threshold the target threshold, must not be negative
     *
     * @return result distance, or {@code -1} if the distance would be greater than the threshold
     */
    public static int getLevenshteinDistance( CharSequence s, CharSequence t, final int threshold )
    {
        LevenshteinDistance ld = new LevenshteinDistance( threshold );
        return ld.apply( s, t );
    }

    //    /**
    //     * Parse the given {@code localeString} value into a {@link Locale}.
    //     * <p>This is the inverse operation of {@link Locale#toString Locale's toString}.
    //     *
    //     * @param localeValue the locale value: following either {@code Locale's}
    //     *                    {@code toString()} format ("en", "en_UK", etc), also accepting spaces as
    //     *                    separators (as an alternative to underscores), or BCP 47 (e.g. "en-UK")
    //     *                    as specified by {@link Locale#forLanguageTag} on Java 7+
    //     *
    //     * @return a corresponding {@code Locale} instance, or {@code null} if none
    //     *
    //     * @throws IllegalArgumentException in case of an invalid locale specification
    //     * @see #parseLocaleString
    //     * @see Locale#forLanguageTag
    //     * @since 5.0.4
    //     */
    //    @Nullable
    //    public static Locale parseLocale( String localeValue )
    //    {
    //        String[] tokens = tokenizeLocaleSource( localeValue );
    //        if( tokens.length == 1 )
    //        {
    //            return Locale.forLanguageTag( localeValue );
    //        }
    //        return parseLocaleTokens( localeValue, tokens );
    //    }
    //
    //    private static String[] tokenizeLocaleSource( String localeSource )
    //    {
    //        return tokenizeToStringArray( localeSource, "_ ", false, false );
    //    }
    //
    //    @Nullable
    //    private static Locale parseLocaleTokens(String localeString, String[] tokens) {
    //        String language = (tokens.length > 0 ? tokens[0] : "");
    //        String country = (tokens.length > 1 ? tokens[1] : "");
    //        validateLocalePart(language);
    //        validateLocalePart(country);
    //
    //        String variant = "";
    //        if (tokens.length > 2) {
    //            // There is definitely a variant, and it is everything after the country
    //            // code sans the separator between the country code and the variant.
    //            int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
    //            // Strip off any leading '_' and whitespace, what's left is the variant.
    //            variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
    //            if (variant.startsWith("_")) {
    //                variant = trimLeadingCharacter(variant, '_');
    //            }
    //        }
    //        return (language.length() > 0 ? new Locale(language, country, variant) : null);
    //    }
}

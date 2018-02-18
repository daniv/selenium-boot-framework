package selenium.boot.core.matchers;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.*;
import org.hamcrest.core.CombinableMatcher.CombinableBothMatcher;
import org.hamcrest.core.CombinableMatcher.CombinableEitherMatcher;
import org.hamcrest.number.BigDecimalCloseTo;
import org.hamcrest.number.IsCloseTo;
import org.hamcrest.number.OrderingComparison;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Optional;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public final class CoreMatchers
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------
    
    private CoreMatchers()
    {
        super();
    }

    //endregion

    /**
     * A shortcut to the frequently used {@code is(equalTo(x))}.
     * <p>
     * For example:
     * <pre>assertThat(cheese, is(smelly))</pre>
     * instead of:
     * <pre>assertThat(cheese, is(equalTo(smelly)))</pre>
     */
    public static <T> Matcher<T> is( T value )
    {
        return Is.is( value );
    }
    
    /**
     * Decorates another Matcher, retaining its behaviour, but allowing tests to be slightly more expressive.
     * <p>
     * For example:
     * <pre>assertThat(cheese, is(equalTo(smelly)))</pre>
     * instead of:
     * <pre>assertThat(cheese, equalTo(smelly))</pre>
     */
    public static <T> Matcher<T> is( Matcher<T> matcher )
    {
        return Is.is( matcher );
    }

    /**
     * A shortcut to the frequently used {@code is(instanceOf(SomeClass.class))}.
     * <p>
     * For example:
     * <pre>assertThat(cheese, isA(Cheddar.class))</pre>
     * instead of:
     * <pre>assertThat(cheese, is(instanceOf(Cheddar.class)))</pre>
     */
    public static <T> Matcher<T> isA( Class<T> type )
    {
        return Is.isA( type );
    }

    /**
     * Creates a matcher that matches when the examined object is an instance of the specified {@code type},
     * as determined by calling the {@link Class#isInstance(Object)} method on that type, passing the the examined object.
     * <p>
     * The created matcher assumes no relationship between specified type and the examined object.</p>
     * <p>
     * For example:
     * <pre>assertThat(new Canoe(), instanceOf(Paddlable.class));</pre>
     */
    public static <T> Matcher<T> instanceOf( Class<?> type )
    {
        return IsInstanceOf.instanceOf( type );
    }

    /**
     * Creates a matcher that wraps an existing matcher, but inverts the logic by which it will match.
     * <p>
     * For example:
     * <pre>assertThat(cheese, is(not(equalTo(smelly))))</pre>
     *
     * @param matcher the matcher whose sense should be inverted
     */
    public static <T> Matcher<T> not( Matcher<T> matcher )
    {
        return IsNot.not( matcher );
    }

    /**
     * A shortcut to the frequently used {@code not(equalTo(x))}.
     * <p>
     * For example:
     * <pre>assertThat(cheese, is(not(smelly)))</pre>
     * instead of:
     * <pre>assertThat(cheese, is(not(equalTo(smelly))))</pre>
     *
     * @param value the value that any examined object should <b>not</b> equal
     */
    public static <T> Matcher<T> not( T value )
    {
        return IsNot.not( value );
    }

    /**
     * A shortcut to the frequently used {@code not(nullValue(X.class)).
     * Accepts a single dummy argument to facilitate type inference.}.
     * For example:
     * <pre>assertThat(cheese, is(notNullValue(X.class)))</pre>
     * instead of:
     * <pre>assertThat(cheese, is(not(nullValue(X.class))))</pre>
     *
     * @param type dummy parameter used to infer the generic type of the returned matcher
     */
    public static <T> Matcher<T> notNullValue( Class<T> type )
    {
        return IsNull.notNullValue( type );
    }

    /**
     * A shortcut to the frequently used {@code not(nullValue())}.
     * <p>
     * For example:
     * <pre>assertThat(cheese, is(notNullValue()))</pre>
     * instead of:
     * <pre>assertThat(cheese, is(not(nullValue())))</pre>
     */
    public static Matcher<Object> notNullValue()
    {
        return IsNull.notNullValue();
    }

    /**
     * Creates a matcher that matches if examined object is {@code null}.
     * For example:
     * <pre>assertThat(cheese, is(nullValue())</pre>
     */
    public static Matcher<Object> nullValue()
    {
        return IsNull.nullValue();
    }

    /**
     * Creates a matcher that matches if examined object is {@code null}.
     * Accepts a single dummy argument to facilitate type inference.
     * For example:
     * <pre>assertThat(cheese, is(nullValue(Cheese.class))</pre>
     *
     * @param type dummy parameter used to infer the generic type of the returned matcher
     */
    public static <T> Matcher<T> nullValue( Class<T> type )
    {
        return IsNull.nullValue( type );
    }

    /**
     * Creates a matcher that matches when the examined object is logically equal to the specified {@code operand},
     * as determined by calling the {@link Object#equals} method on the <b>examined</b> object.
     * <p>
     * If the specified operand is {@code null} then the created matcher will only match if
     * the examined object's {@code equals} method returns {@code true} when passed a
     * {@code null} ( which would be a violation of the {@code equals} contract ), unless the
     * examined object itself is {@code null}, in which case the matcher will return a positive match.
     * <p>
     * The created matcher provides a special behaviour when examining {@code Array}s, whereby
     * it will match if both the operand and the examined object are arrays of the same length and
     * contain items that are equal to each other (according to the above rules) <b>in the same indexes</b>.
     * <p>
     * For example:
     * <pre>
     * assertThat("foo", equalTo("foo"));
     * assertThat(new String[] {"foo", "bar"}, equalTo(new String[] {"foo", "bar"}));
     * </pre>
     */
    public static <T> Matcher<T> equalTo( @Nullable T operand )
    {
        return ExtendedIsEqualMatcher.equalTo( operand );
    }

    /**
     * Creates an {@link selenium.boot.core.matchers.ExtendedIsEqualMatcher}
     * similar to {@link org.hamcrest.core.IsEqual} matcher that does not enforce the values being
     * compared to be of the same static type.
     */
    public static Matcher<Object> equalToObject( Object operand )
    {
        return ExtendedIsEqualMatcher.equalTo( operand );
    }

    /**
     * Creates a matcher of {@link Double}s that matches when an examined double is equal
     * to the specified {@code operand}, within a range of +/- {@code error}.
     * <p>
     * For example:
     * <pre>assertThat(1.03, is(closeTo(1.0, 0.03)))</pre>
     *
     * @param operand the expected value of matching doubles
     * @param error   the delta (+/-) within which matches will be allowed
     */
    public static Matcher<Double> closeTo( double operand, double error )
    {
        return IsCloseTo.closeTo( operand, error );
    }

    /**
     * Creates a matcher of {@link Double}s that matches when an examined double is not a number.
     * <p>
     * For example:
     * <pre>assertThat(Double.NaN, is(notANumber()))</pre>
     */
    public static Matcher<Double> notANumber()
    {
        return IsNaN.notANumber();
    }

    /**
     * Creates a matcher of {@link java.math.BigDecimal}s that matches when an examined BigDecimal is equal
     * to the specified {@code operand}, within a range of +/- {@code error}. The comparison for equality
     * is done by BigDecimals {@link java.math.BigDecimal#compareTo(java.math.BigDecimal)} method.
     * <p>
     * For example:
     * <pre>assertThat(new BigDecimal("1.03"), is(closeTo(new BigDecimal("1.0"), new BigDecimal("0.03"))))</pre>
     *
     * @param operand the expected value of matching BigDecimals
     * @param error   the delta (+/-) within which matches will be allowed
     */
    public static Matcher<BigDecimal> closeTo( BigDecimal operand, BigDecimal error )
    {
        return BigDecimalCloseTo.closeTo( operand, error );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * equal to the specified value, as reported by the {@code compareTo} method of the
     * <b>examined</b> object.
     * <p>
     * For example:
     * <pre>assertThat(1, comparesEqualTo(1))</pre>
     *
     * @param value the value which, when passed to the compareTo method of the examined object, should return zero
     */
    public static <T extends Comparable<T>> Matcher<T> comparesEqualTo( T value )
    {
        return OrderingComparison.comparesEqualTo( value );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * greater than the specified value, as reported by the {@code compareTo} method of the <b>examined</b> object.
     * <p>
     * For example:
     * <pre>assertThat(2, greaterThan(1))</pre>
     *
     * @param value the value which, when passed to the compareTo method of the examined object, should return greater
     *              than zero
     */
    public static <T extends Comparable<T>> Matcher<T> greaterThan( T value )
    {
        return OrderingComparison.greaterThan( value );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * greater than or equal to the specified value, as reported by the {@code compareTo} method of the <b>examined</b> object.
     * <p>
     * For example:
     * <pre>assertThat(1, greaterThanOrEqualTo(1))</pre>
     *
     * @param value the value which, when passed to the compareTo method of the examined object, should return greater
     *              than or equal to zero
     */
    public static <T extends Comparable<T>> Matcher<T> greaterThanOrEqualTo( T value )
    {
        return OrderingComparison.greaterThanOrEqualTo( value );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * less than the specified value, as reported by the {@code compareTo} method of the <b>examined</b> object.
     * <p>
     * For example:
     * <pre>assertThat(1, lessThan(2))</pre>
     *
     * @param value the value which, when passed to the compareTo method of the examined object, should return less
     *              than zero
     */
    public static <T extends Comparable<T>> Matcher<T> lessThan( T value )
    {
        return OrderingComparison.lessThan( value );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * less than or equal to the specified value, as reported by the {@code compareTo} method of the <b>examined</b> object.
     * <p>
     * For example:
     * <pre>assertThat(1, lessThanOrEqualTo(1))</pre>
     *
     * @param value the value which, when passed to the compareTo method of the examined object, should return less
     *              than or equal to zero
     */
    public static <T extends Comparable<T>> Matcher<T> lessThanOrEqualTo( T value )
    {
        return OrderingComparison.lessThanOrEqualTo( value );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * equal to the specified value, as reported by the {@code compareTo} method of the
     * <b>examined</b> object.
     * <p>
     * For example:
     * <pre>assertThat(0, isZero())</pre>
     */
    public static <T extends Comparable<? extends Number>> Matcher<? extends Number> isZero()
    {
        return is( 0 );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * equal to the specified value, as reported by the {@code compareTo} method of the
     * <b>examined</b> object.
     * <p>
     * For example:
     * <pre>assertThat(-3L, isNegative())</pre>
     */
    public static <T extends Comparable<? extends Number>> Matcher<? extends Number> isNegative()
    {
        return lessThan( 0 );
    }

    /**
     * Creates a matcher of {@link Comparable} object that matches when the examined object is
     * equal to the specified value, as reported by the {@code compareTo} method of the
     * <b>examined</b> object.
     * <p>
     * For example:
     * <pre>assertThat(1.0, isPositive())</pre>
     */
    public static <T extends Comparable<? extends Number>> Matcher<? extends Number> isPositive()
    {
        return greaterThan( 0 );
    }

    /**
     * Wraps an existing matcher, overriding its description with that specified.
     * All other functions are delegated to the decorated matcher, including its mismatch description.
     * <p>
     * For example:
     * <pre>describedAs("a big decimal equal to %0", equalTo(myBigDecimal), myBigDecimal.toPlainString())</pre>
     *
     * @param description the new description for the wrapped matcher
     * @param matcher     the matcher to wrap
     * @param values      optional values to insert into the tokenised description
     */
    public static <T> Matcher<T> describedAs( String description,
                                              Matcher<T> matcher, 
                                              Object... values )
    {
        return DescribedAs.describedAs( description, matcher, values );
    }

    /**
     * Creates a matcher for {@link Iterable}s that only matches when a single pass over the
     * examined {@link Iterable} yields items that are all matched by the specified {@code itemMatcher}.
     * <p>
     * For example:
     * <pre>assertThat(Arrays.asList("bar", "baz"), everyItem(startsWith("ba")))</pre>
     *
     * @param itemMatcher the matcher to apply to every item provided by the examined {@link Iterable}
     */
    public static <U> Matcher<Iterable<U>> everyItem( final Matcher<U> itemMatcher )
    {
        return Every.everyItem( itemMatcher );
    }

    /**
     * Creates a matcher for {@link Iterable}s that only matches when a single pass over the
     * examined {@link Iterable} yields at least one item that is matched by the specified
     * {@code itemMatcher}.  Whilst matching, the traversal of the examined {@link Iterable}
     * will stop as soon as a matching item is found.
     * <p>
     * For example:
     * <pre>assertThat(Arrays.asList("foo", "bar"), hasItem(startsWith("ba")))</pre>
     *
     * @param itemMatcher the matcher to apply to items provided by the examined {@link Iterable}
     */
    public static <T> Matcher<Iterable<? super T>> hasItem( Matcher<? super T> itemMatcher )
    {
        return IsCollectionContaining.hasItem( itemMatcher );
    }

    /**
     * Creates a matcher for {@link Iterable}s that only matches when a single pass over the
     * examined {@link Iterable} yields at least one item that is equal to the specified {@code item}.
     * Whilst matching, the traversal of the examined {@link Iterable} will stop as soon as a matching item is found.
     * <p>
     * For example:
     * <pre>assertThat(Arrays.asList("foo", "bar"), hasItem("bar"))</pre>
     *
     * @param item the item to compare against the items provided by the examined {@link Iterable}
     */
    public static <T> Matcher<Iterable<? super T>> hasItem( T item )
    {
        return IsCollectionContaining.hasItem( item );
    }

    /**
     * Creates a matcher for {@link Iterable}s that matches when consecutive passes over the
     * examined {@code Iterable} yield at least one item that is matched by the corresponding matcher
     * from the specified {@code itemMatchers}.
     * Whilst matching, each traversal of the examined {@code Iterable} will stop as soon as a matching item is found.
     * <p>
     * For example:
     * <pre>assertThat(Arrays.asList("foo", "bar", "baz"), hasItems(endsWith("z"), endsWith("o")))</pre>
     *
     * @param itemMatchers the matchers to apply to items provided by the examined {@link Iterable}
     */
    @SafeVarargs
    public static <T> Matcher<Iterable<T>> hasItems( Matcher<? super T>... itemMatchers )
    {
        return IsCollectionContaining.hasItems( itemMatchers );
    }

    /**
     * Creates a matcher for {@link Iterable}s that matches when consecutive passes over the
     * examined {@code Iterable} yield at least one item that is equal to the corresponding item from the specified {@code items}.
     * Whilst matching, each traversal of the examined {@code Iterable} will stop as soon as a matching item is found.
     * <p>
     * For example:
     * <pre>assertThat(Arrays.asList("foo", "bar", "baz"), hasItems("baz", "foo"))</pre>
     *
     * @param items the items to compare against the items provided by the examined {@link Iterable}
     */
    @SafeVarargs
    public static <T> Matcher<Iterable<T>> hasItems( T... items )
    {
        return IsCollectionContaining.hasItems( items );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static <T> Matcher<T> allOf( Iterable<Matcher<? super T>> matchers )
    {
        return AllOf.allOf( matchers );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    @SafeVarargs
    public static <T> Matcher<T> allOf( Matcher<? super T>... matchers )
    {
        return AllOf.allOf( matchers );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static <T> Matcher<T> allOf( Matcher<? super T> first, Matcher<? super T> second )
    {
        return AllOf.allOf( first, second );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static <T> Matcher<T> allOf( Matcher<? super T> first,
                                        Matcher<? super T> second,
                                        Matcher<? super T> third )
    {
        return AllOf.allOf( first, second, third );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static <T> Matcher<T> allOf( Matcher<? super T> first,
                                        Matcher<? super T> second,
                                        Matcher<? super T> third,
                                        Matcher<? super T> fourth )
    {
        return AllOf.allOf( first, second, third, fourth );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static <T> Matcher<T> allOf( Matcher<? super T> first,
                                        Matcher<? super T> second,
                                        Matcher<? super T> third,
                                        Matcher<? super T> fourth,
                                        Matcher<? super T> fifth )
    {
        return AllOf.allOf( first, second, third, fourth, fifth );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ALL</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", allOf(startsWith("my"), containsString("Val")))</pre>
     */
    public static <T> Matcher<T> allOf( Matcher<? super T> first,
                                        Matcher<? super T> second,
                                        Matcher<? super T> third,
                                        Matcher<? super T> fourth,
                                        Matcher<? super T> fifth,
                                        Matcher<? super T> sixth )
    {
        return AllOf.allOf( first, second, third, fourth, fifth, sixth );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ANY</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", anyOf(startsWith("foo"), containsString("Val")))</pre>
     */
    public static <T> AnyOf<T> anyOf( Iterable<Matcher<? super T>> matchers )
    {
        return AnyOf.anyOf( matchers );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ANY</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", anyOf(startsWith("foo"), containsString("Val")))</pre>
     */
    public static <T> AnyOf<T> anyOf( Matcher<? super T>... matchers )
    {
        return AnyOf.anyOf( matchers );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ANY</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", anyOf(startsWith("foo"), containsString("Val")))</pre>
     */
    public static <T> AnyOf<T> anyOf( Matcher<T> first, Matcher<? super T> second )
    {
        return AnyOf.anyOf( first, second );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ANY</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", anyOf(startsWith("foo"), containsString("Val")))</pre>
     */
    public static <T> AnyOf<T> anyOf( Matcher<T> first,
                                      Matcher<? super T> second,
                                      Matcher<? super T> third )
    {
        return AnyOf.anyOf( first, second, third );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ANY</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", anyOf(startsWith("foo"), containsString("Val")))</pre>
     */
    public static <T> AnyOf<T> anyOf( Matcher<T> first,
                                      Matcher<? super T> second,
                                      Matcher<? super T> third,
                                      Matcher<? super T> fourth )
    {
        return AnyOf.anyOf( first, second, third, fourth );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ANY</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", anyOf(startsWith("foo"), containsString("Val")))</pre>
     */
    public static <T> AnyOf<T> anyOf( Matcher<T> first,
                                      Matcher<? super T> second,
                                      Matcher<? super T> third,
                                      Matcher<? super T> fourth,
                                      Matcher<? super T> fifth )
    {
        return AnyOf.anyOf( first, second, third, fourth, fifth );
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ANY</b> of the specified matchers.
     * <p>
     * For example:
     * <pre>assertThat("myValue", anyOf(startsWith("foo"), containsString("Val")))</pre>
     */
    public static <T> AnyOf<T> anyOf( Matcher<T> first,
                                      Matcher<? super T> second,
                                      Matcher<? super T> third,
                                      Matcher<? super T> fourth,
                                      Matcher<? super T> fifth,
                                      Matcher<? super T> sixth )
    {
        return AnyOf.anyOf( first, second, third, fourth, fifth, sixth );
    }

    /**
     * Creates a matcher that matches when both of the specified matchers match the examined object.
     * <p>
     * For example:
     * <pre>assertThat("fab", both(containsString("a")).and(containsString("b")))</pre>
     */
    public static <LHS> CombinableBothMatcher<LHS> both( Matcher<? super LHS> matcher )
    {
        return CombinableMatcher.both( matcher );
    }

    /**
     * Creates a matcher that matches when either of the specified matchers match the examined object.
     * <p>
     * For example:
     * <pre>assertThat("fan", either(containsString("a")).or(containsString("b")))</pre>
     */
    public static <LHS> CombinableEitherMatcher<LHS> either( Matcher<? super LHS> matcher )
    {
        return CombinableMatcher.either( matcher );
    }

    public static Matcher<Optional<?>> isPresent()
    {
        return new PresenceMatcher();
    }

    public static Matcher<Optional<?>> isEmpty()
    {
        return new EmptyMatcher();
    }

    public static <T> Matcher<Optional<T>> isPresentAndIs( T operand )
    {
        return new HasValue( IsEqual.equalTo( operand ) );
    }

    public static <T> Matcher<Optional<T>> isPresentAnd( Matcher<? super T> matcher )
    {
        return new HasValue( matcher );
    }

    public static <T extends Comparable<T>> Matcher<T> between( T low, T high ) { return BetweenMatcher.between( low, high ); }


    private static class HasValue<T> extends TypeSafeMatcher<Optional<T>>
    {
        private Matcher<? super T> matcher;

        public HasValue( Matcher<? super T> matcher )
        {
            this.matcher = matcher;
        }

        @Override
        public void describeTo( Description description )
        {
            description.appendText( "has value that is " );
            this.matcher.describeTo( description );
        }

        @Override
        protected boolean matchesSafely( Optional<T> item )
        {
            return item.isPresent() && this.matcher.matches( item.get() );
        }

        @Override
        protected void describeMismatchSafely( Optional<T> item, Description mismatchDescription )
        {
            if( item.isPresent() )
            {
                mismatchDescription.appendText( "value " );
                this.matcher.describeMismatch( item.get(), mismatchDescription );
            }
            else
            {
                mismatchDescription.appendText( "was <Empty>" );
            }

        }
    }

    private static class EmptyMatcher extends TypeSafeMatcher<Optional<?>>
    {
        private EmptyMatcher()
        {
        }

        public void describeTo( Description description )
        {
            description.appendText( "is <Empty>" );
        }

        protected boolean matchesSafely( Optional<?> item )
        {
            return !item.isPresent();
        }

        protected void describeMismatchSafely( Optional<?> item, Description mismatchDescription )
        {
            mismatchDescription.appendText( "had value " );
            mismatchDescription.appendValue( item.get() );
        }
    }

    private static class PresenceMatcher extends TypeSafeMatcher<Optional<?>>
    {
        private PresenceMatcher()
        {
        }

        @Override
        public void describeTo( Description description )
        {
            description.appendText( "is <Present>" );
        }

        @Override
        protected boolean matchesSafely( Optional<?> item )
        {
            return item.isPresent();
        }

        @Override
        protected void describeMismatchSafely( Optional<?> item, Description mismatchDescription )
        {
            mismatchDescription.appendText( "was <Empty>" );
        }
    }

}

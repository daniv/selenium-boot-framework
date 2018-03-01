package selenium.boot.utils.measure.storage;


import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

//@formatter:off

/**
 * Abstract base class for all storage units. Provides common functionality for unit conversion, hashCode(), equals(),
 * compareTo(), toString(), doubleValue(), floatValue(), intValue() and longValue().
 * source:  de.xn__ho_hia.storage_unit
 *
 * @param <T> The type of this storage unit.
 * <table class="vertical-navbox" style="text-align: left; float:right; clear:right; margin-left:1em; border:1px #aaa solid; font-size:88%;">
 *     <tbody>
 *     <tr>
 *         <th colspan="2" style="padding-left:3em;padding-right:1em;text-align:center;">
 *         <span style="font-size:120%;">Multiples of bytes</span></th>
 *     </tr>
 *     <tr>
 *         <td style="padding:0">
 *             <table style="border: 1px #aaa solid">
 *                 <tbody>
 *                     <tr><th colspan="3"><a href="/wiki/Decimal">Decimal</a></th></tr>
 *                     <tr><th style="padding:0">Value</th>
 *                         <th colspan="2" style="padding:0"><a href="/wiki/Metric_prefix">Metric</a></th>
 *                     </tr>
 *                     <tr><td>1000</td><td>kB</td> <td><a href="/wiki/Kilobyte">kilobyte</a></td></tr>
 *                     <tr><td>1000<sup>2</sup></td><td>MB</td><td><a href="/wiki/Megabyte">megabyte</a></td></tr>
 *                     <tr><td>1000<sup>3</sup></td><td>GB</td><td><a href="/wiki/Gigabyte">gigabyte</a></td></tr>
 *                     <tr><td>1000<sup>4</sup></td><td>TB</td><td><a href="/wiki/Terabyte">terabyte</a></td></tr>
 *                     <tr><td>1000<sup>5</sup></td><td>PB</td><td><a href="/wiki/Petabyte">petabyte</a></td></tr>
 *                     <tr><td>1000<sup>6</sup></td><td>EB</td><td><a href="/wiki/Exabyte">exabyte</a></td></tr>
 *                     <tr><td>1000<sup>7</sup></td><td>ZB</td><td><a href="/wiki/Zettabyte">zettabyte</a></td></tr>
 *                     <tr><td>1000<sup>8</sup></td><td>YB</td><td><a href="/wiki/Yottabyte">yottabyte</a></td></tr>
 *                </tbody></table>
 *                </td><td style="padding:0">
 *                    <table style="border: 1px #aaa solid"><tbody>
 *                        <tr><th colspan="5" class="navbox-title"><a href="/wiki/Binary_prefix" title="Binary prefix">Binary</a></th></tr>
 *                        <tr><th style="padding:0">Value</th>
 *                        <th colspan="2" style="padding:0"><a href="/wiki/IEC_80000-13" class="mw-redirect" title="IEC 80000-13">IEC</a></th>
 *                        <th colspan="2" style="padding:0"><a href="/wiki/JEDEC_memory_standards#Unit_prefixes_for_semiconductor_storage_capacity">JEDEC</a></th></tr>
 *                        <tr><td>1024</td><td>KiB</td><td><a href="/wiki/Kibibyte"kibibyte</a></td><td>KB</td><td>kilobyte</td></tr>
 *                        <tr><td>1024<sup>2</sup></td><td>MiB</td><td><a href="/wiki/Mebibyte">mebibyte</a></td><td>MB</td><td>megabyte</td></tr>
 *                        <tr><td>1024<sup>3</sup></td><td>GiB</td><td><a href="/wiki/Gibibyte">gibibyte</a></td><td>GB</td><td>gigabyte</td></tr>
 *                        <tr><td>1024<sup>4</sup></td><td>TiB</td><td><a href="/wiki/Tebibyte">tebibyte</a></td><td colspan="2" style="text-align: center;">–</td></tr>
 *                        <tr><td>1024<sup>5</sup></td><td>PiB</td><td><a href="/wiki/Pebibyte">pebibyte</a></td><td colspan="2" style="text-align: center;">–</td></tr>
 *                        <tr><td>1024<sup>6</sup></td><td>EiB</td><td><a href="/wiki/Exbibyte">exbibyte</a></td><td colspan="2" style="text-align: center;">–</td></tr>
 *                        <tr><td>1024<sup>7</sup></td><td>ZiB</td><td><a href="/wiki/Zebibyte">zebibyte</a></td><td colspan="2" style="text-align: center;">–</td></tr>
 *                        <tr><td>1024<sup>8</sup></td><td>YiB</td><td><a href="/wiki/Yobibyte">yobibyte</a></td><td colspan="2" style="text-align: center;">–</td></tr>
 *                        </tbody>
 *                    </table>
 *                </td></tr>
 *         <tr><td colspan="5"><a href="/wiki/Orders_of_magnitude_(data)">Orders of magnitude of data</a></td></tr>
 *     </tbody>
 * </table>
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public abstract class StorageUnit<T extends StorageUnit<T>> extends Number implements Comparable<StorageUnit<?>>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    /**
     * Default number format used within the library.
     */

    static final String DEFAULT_FORMAT_PATTERN = "0.00";

    /**
     * The storage unit base for binary numbers. Each step between the units dimensions is done with this base value.
     */
    static final BigInteger BINARY_UNIT_BASE = BigInteger.valueOf( 1_024L );

    static final BigInteger BYTES_IN_A_KIBIBYTE = BigInteger.ONE.multiply( StorageUnit.BINARY_UNIT_BASE );

    static final BigInteger BYTES_IN_A_MEBIBYTE = StorageUnit.BYTES_IN_A_KIBIBYTE.multiply( StorageUnit.BINARY_UNIT_BASE );

    static final BigInteger BYTES_IN_A_GIBIBYTE = StorageUnit.BYTES_IN_A_MEBIBYTE.multiply( StorageUnit.BINARY_UNIT_BASE );

    static final BigInteger BYTES_IN_A_TEBIBYTE = StorageUnit.BYTES_IN_A_GIBIBYTE.multiply( StorageUnit.BINARY_UNIT_BASE );

    static final BigInteger BYTES_IN_A_PEBIBYTE = StorageUnit.BYTES_IN_A_TEBIBYTE.multiply( StorageUnit.BINARY_UNIT_BASE );

    static final BigInteger BYTES_IN_A_EXBIBYTE = StorageUnit.BYTES_IN_A_PEBIBYTE.multiply( StorageUnit.BINARY_UNIT_BASE );

    static final BigInteger BYTES_IN_A_ZEBIBYTE = StorageUnit.BYTES_IN_A_EXBIBYTE.multiply( StorageUnit.BINARY_UNIT_BASE );

    static final BigInteger BYTES_IN_A_YOBIBYTE = StorageUnit.BYTES_IN_A_ZEBIBYTE.multiply( StorageUnit.BINARY_UNIT_BASE );

    /**
     * The storage unit base for decimal numbers. Each step between the units dimensions is done with this base value.
     */
    static final BigInteger DECIMAL_UNIT_BASE = BigInteger.valueOf( 1000 );

    static final BigInteger BYTES_IN_A_KILOBYTE = BigInteger.ONE.multiply( StorageUnit.DECIMAL_UNIT_BASE );

    static final BigInteger BYTES_IN_A_MEGABYTE = StorageUnit.BYTES_IN_A_KILOBYTE.multiply( StorageUnit.DECIMAL_UNIT_BASE );

    static final BigInteger BYTES_IN_A_GIGABYTE = StorageUnit.BYTES_IN_A_MEGABYTE.multiply( StorageUnit.DECIMAL_UNIT_BASE );

    static final BigInteger BYTES_IN_A_TERABYTE = StorageUnit.BYTES_IN_A_GIGABYTE.multiply( StorageUnit.DECIMAL_UNIT_BASE );

    static final BigInteger BYTES_IN_A_PETABYTE = StorageUnit.BYTES_IN_A_TERABYTE.multiply( StorageUnit.DECIMAL_UNIT_BASE );

    static final BigInteger BYTES_IN_A_EXABYTE = StorageUnit.BYTES_IN_A_PETABYTE.multiply( StorageUnit.DECIMAL_UNIT_BASE );

    static final BigInteger BYTES_IN_A_ZETTABYTE = StorageUnit.BYTES_IN_A_EXABYTE.multiply( StorageUnit.DECIMAL_UNIT_BASE );

    static final BigInteger BYTES_IN_A_YOTTABYTE = StorageUnit.BYTES_IN_A_ZETTABYTE.multiply( StorageUnit.DECIMAL_UNIT_BASE );

    private static final int DEFAULT_SCALE = 24;

    protected final BigInteger bytes;

    protected StorageUnit( final BigInteger bytes )
    {
        this.bytes = bytes;
    }

    //endregion

    /**
     * @return This storage unit as the best matching binary unit.
     */
    public final StorageUnit<?> asBestMatchingBinaryUnit()
    {
        return StorageUnits.binaryValueOf( this.bytes );
    }

    /**
     * @return This storage unit as the best matching decimal unit.
     */
    public final StorageUnit<?> asBestMatchingDecimalUnit()
    {
        return StorageUnits.decimalValueOf( this.bytes );
    }

    /**
     * @return This storage unit as the best matching common unit.
     */
    public final StorageUnit<?> asBestMatchingCommonUnit()
    {
        return StorageUnits.commonValueOf( this.bytes );
    }

    /**
     * @return This storage unit as the best matching unit, while keeping the current type (binary, decimal, common).
     */

    public final StorageUnit<?> asBestMatchingUnit()
    {
        return converter().apply( this.bytes );
    }

    protected abstract Function<BigInteger, StorageUnit<?>> converter();

    /**
     * @return This storage unit as bytes.
     */

    public final ByteUnit asByte()
    {
        return new ByteUnit( this.bytes );
    }

    /**
     * @return This storage unit as a kibibyte.
     */

    public final Kibibyte asKibibyte()
    {
        return new Kibibyte( this.bytes );
    }

    /**
     * @return This storage unit as a mebibyte.
     */
    public final Mebibyte asMebibyte()
    {
        return new Mebibyte( this.bytes );
    }

    /**
     * @return This storage unit as a gibibyte.
     */
    public final Gibibyte asGibibyte()
    {
        return new Gibibyte( this.bytes );
    }

    /**
     * @return This storage unit as a tebibyte.
     */
    public final Tebibyte asTebibyte()
    {
        return new Tebibyte( this.bytes );
    }

    /**
     * @return This storage unit as a pebibyte.
     */
    public final Pebibyte asPebibyte()
    {
        return new Pebibyte( this.bytes );
    }

    /**
     * @return This storage unit as a exbibyte.
     */
    public final Exbibyte asExbibyte()
    {
        return new Exbibyte( this.bytes );
    }

    /**
     * @return This storage unit as a zebibyte.
     */
    public final Zebibyte asZebibyte()
    {
        return new Zebibyte( this.bytes );
    }

    /**
     * @return This storage unit as a yobibyte.
     */
    public final Yobibyte asYobibyte()
    {
        return new Yobibyte( this.bytes );
    }

    /**
     * @return This storage unit as a kilobyte.
     */
    public final Kilobyte asKilobyte()
    {
        return new Kilobyte( this.bytes );
    }

    /**
     * @return This storage unit as a megabyte.
     */
    public final Megabyte asMegabyte()
    {
        return new Megabyte( this.bytes );
    }

    /**
     * @return This storage unit as a gigabyte.
     */
    public final Gigabyte asGigabyte()
    {
        return new Gigabyte( this.bytes );
    }

    /**
     * @return This storage unit as a terabyte.
     */
    public final Terabyte asTerabyte()
    {
        return new Terabyte( this.bytes );
    }

    /**
     * @return This storage unit as a petabyte.
     */
    public final Petabyte asPetabyte()
    {
        return new Petabyte( this.bytes );
    }

    /**
     * @return This storage unit as a exabyte.
     */
    public final Exabyte asExabyte()
    {
        return new Exabyte( this.bytes );
    }

    /**
     * @return This storage unit as a zettabyte.
     */
    public final Zettabyte asZettabyte()
    {
        return new Zettabyte( this.bytes );
    }

    /**
     * @return This storage unit as a yottabyte.
     */
    public final Yottabyte asYottabyte()
    {
        return new Yottabyte( this.bytes );
    }

    /**
     * @return This storage unit as a non-standard kilobyte.
     */
    public final CommonKilobyte asCommonKilobyte()
    {
        return new CommonKilobyte( this.bytes );
    }

    /**
     * @return This storage unit as a non-standard megabyte.
     */
    public final CommonMegabyte asCommonMegabyte()
    {
        return new CommonMegabyte( this.bytes );
    }

    /**
     * @return This storage unit as a non-standard gigabyte.
     */
    public final CommonGigabyte asCommonGigabyte()
    {
        return new CommonGigabyte( this.bytes );
    }

    /**
     * @return This storage unit as a non-standard terabyte.
     */
    public final CommonTerabyte asCommonTerabyte()
    {
        return new CommonTerabyte( this.bytes );
    }

    /**
     * @return This storage unit as a non-standard petabyte.
     */
    public final CommonPetabyte asCommonPetabyte()
    {
        return new CommonPetabyte( this.bytes );
    }

    /**
     * @return This storage unit as a non-standard exabyte.
     */
    public final CommonExabyte asCommonExabyte()
    {
        return new CommonExabyte( this.bytes );
    }

    /**
     * @return This storage unit as a non-standard zettabyte.
     */
    public final CommonZettabyte asCommonZettabyte()
    {
        return new CommonZettabyte( this.bytes );
    }

    /**
     * @return This storage unit as a non-standard yottabyte.
     */
    public final CommonYottabyte asCommonYottabyte()
    {
        return new CommonYottabyte( this.bytes );
    }

    /**
     * @return The amount of bytes this storage unit encompasses.
     */
    public final BigInteger inByte()
    {
        return this.bytes;
    }

    /**
     * @return This storage unit quantified as kibibyte.
     */
    public final BigDecimal inKibibyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_KIBIBYTE );
    }


    private final BigDecimal calculate( final BigInteger base )
    {
        return new BigDecimal( this.bytes.toString() )
                       .divide( new BigDecimal( base.toString() ), StorageUnit.DEFAULT_SCALE, RoundingMode.CEILING );
    }

    /**
     * @return This storage unit quantified as mebibyte.
     */
    public final BigDecimal inMebibyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_MEBIBYTE );
    }

    /**
     * @return This storage unit quantified as gibibyte.
     */
    public final BigDecimal inGibibyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_GIBIBYTE );
    }

    /**
     * @return This storage unit quantified as tebibyte.
     */
    public final BigDecimal inTebibyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_TEBIBYTE );
    }

    /**
     * @return This storage unit quantified as pebibyte.
     */
    public final BigDecimal inPebibyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_PEBIBYTE );
    }

    /**
     * @return This storage unit quantified as exbibyte.
     */
    public final BigDecimal inExbibyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_EXBIBYTE );
    }

    /**
     * @return This storage unit quantified as zebibyte.
     */
    public final BigDecimal inZebibyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_ZEBIBYTE );
    }

    /**
     * @return This storage unit quantified as yobibyte.
     */
    public final BigDecimal inYobibyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_YOBIBYTE );
    }

    /**
     * @return This storage unit quantified as kilobyte.
     */
    public final BigDecimal inKilobyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_KILOBYTE );
    }

    /**
     * @return This storage unit quantified as megabyte.
     */

    public final BigDecimal inMegabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_MEGABYTE );
    }

    /**
     * @return This storage unit quantified as gigabyte.
     */

    public final BigDecimal inGigabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_GIGABYTE );
    }

    /**
     * @return This storage unit quantified as terabyte.
     */

    public final BigDecimal inTerabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_TERABYTE );
    }

    /**
     * @return This storage unit quantified as petabyte.
     */

    public final BigDecimal inPetabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_PETABYTE );
    }

    /**
     * @return This storage unit quantified as exabyte.
     */

    public final BigDecimal inExabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_EXABYTE );
    }

    /**
     * @return This storage unit quantified as zettabyte.
     */

    public final BigDecimal inZettabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_ZETTABYTE );
    }

    /**
     * @return This storage unit quantified as yottabyte.
     */

    public final BigDecimal inYottabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_YOTTABYTE );
    }

    /**
     * @return This storage unit quantified as common kibibyte.
     */

    public final BigDecimal inCommonKilobyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_KIBIBYTE );
    }

    /**
     * @return This storage unit quantified as common mebibyte.
     */

    public final BigDecimal inCommonMegabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_MEBIBYTE );
    }

    /**
     * @return This storage unit quantified as common gibibyte.
     */

    public final BigDecimal inCommonGigabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_GIBIBYTE );
    }

    /**
     * @return This storage unit quantified as common tebibyte.
     */

    public final BigDecimal inCommonTerabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_TEBIBYTE );
    }

    /**
     * @return This storage unit quantified as common pebibyte.
     */

    public final BigDecimal inCommonPetabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_PEBIBYTE );
    }

    /**
     * @return This storage unit quantified as common exbibyte.
     */
    public final BigDecimal inCommonExabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_EXBIBYTE );
    }

    /**
     * @return This storage unit quantified as common zebibyte.
     */
    public final BigDecimal inCommonZettabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_ZEBIBYTE );
    }

    /**
     * @return This storage unit quantified as common yottabyte.
     */
    public final BigDecimal inCommonYottabyte()
    {
        return this.calculate( StorageUnit.BYTES_IN_A_YOBIBYTE );
    }

    /**
     * Formats this storage unit according to the given pattern in a specific {@link Locale}.
     *
     * @param pattern The {@link Format} pattern to apply.
     * @param locale  The locale to use.
     *
     * @return The formatted representation of this storage unit.
     */
    public final String toString( final String pattern, final Locale locale )
    {
        return this.toString( UnitFormat.asFormat( pattern, locale ) );
    }

    /**
     * Formats this storage unit according to a specified {@link java.text.Format}.
     * The storage unit's symbol will be automatically added at the end of the formatted string together with a single whitespace character in front of it.
     * Use the {@code asOtherUnit} methods before printing in order to change the symbol.
     *
     * @param format The custom format to use.
     *
     * @return The formatted representation of this storage unit.
     */
    public final String toString( final Format format )
    {
        final BigDecimal amount = this.calculate( this.getNumberOfBytesPerUnit() );
        final String formattedAmount = format.format( amount );
        return formattedAmount + " " + getSymbol();
    }

    protected abstract BigInteger getNumberOfBytesPerUnit();

    final int calculateBuilderCapacity( final String formattedAmount )
    {
        return formattedAmount.length() + getSymbol().length() + 2;
    }

    protected abstract String getSymbol();

    @Override
    public final int hashCode()
    {
        return Objects.hashCode( this.bytes );
    }

    @Override
    public final boolean equals( @Nullable final Object other )
    {
        if( other instanceof StorageUnit<?> )
        {
            final StorageUnit<?> that = ( StorageUnit<?> ) other;

            return Objects.equals( this.bytes, that.bytes );
        }

        return false;
    }


    @Override
    public final String toString()
    {
        return this.toString( DEFAULT_FORMAT_PATTERN );
    }

    /**
     * Formats this storage unit according to the given pattern.
     *
     * @param pattern The {@link Format} pattern to apply.
     *
     * @return The formatted representation of this storage unit.
     */
    public final String toString( final String pattern )
    {
        return this.toString( new DecimalFormat( pattern ) );
    }

    @Override
    public final int compareTo( final StorageUnit<?> that )
    {
        return this.bytes.compareTo( that.bytes );
    }

    @Override
    public final int intValue()
    {
        return this.bytes.intValue();
    }

    @Override
    public final long longValue()
    {
        return this.bytes.longValue();
    }

    @Override
    public final float floatValue()
    {
        return this.bytes.floatValue();
    }

    @Override
    public final double doubleValue()
    {
        return this.bytes.doubleValue();
    }

    /**
     * @param bytesToAdd The amount of bytes to add.
     *
     * @return The new amount of storage in the appropriate type.
     */
    public abstract T add( long bytesToAdd );

    /**
     * @param storageAmount The amount of storage to add.
     *
     * @return The new amount of storage in the appropriate type.
     */
    public abstract T add( StorageUnit<?> storageAmount );

    /**
     * @param divisor The divisor to apply.
     *
     * @return The new amount of storage in the appropriate type.
     */
    public abstract T divide( long divisor );

    /**
     * @param factor The factor to apply.
     *
     * @return The new amount of storage in the appropriate type.
     */
    public abstract T multiply( long factor );

    /**
     * @param bytesToSubtract The amount of bytes to subtract.
     *
     * @return The new amount of storage in the appropriate type.
     */
    public abstract T subtract( long bytesToSubtract );

    /**
     * @param storageAmount The amount of storage to subtract.
     *
     * @return The new amount of storage in the appropriate type.
     */
    public abstract T subtract( StorageUnit<?> storageAmount );
}

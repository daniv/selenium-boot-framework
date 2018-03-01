package selenium.boot.utils.measure.storage;



import de.xn__ho_hia.quality.null_analysis.Nullsafe;
import selenium.boot.utils.Assert;

import java.math.BigInteger;

import static selenium.boot.utils.measure.storage.StorageUnit.*;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public final class StorageUnits
{
    //region initialization and constructors section

    private StorageUnits()
    {
        super();
    }

    //endregion

    private static boolean inBetween( final BigInteger start, final BigInteger value, final BigInteger endExclusive )
    {
        return greaterThanEquals( value, start ) && value.compareTo( endExclusive ) < 0;
    }

    private static boolean greaterThanEquals( final BigInteger value, final BigInteger comparison )
    {
        return value.compareTo( comparison ) >= 0;
    }

    /**
     * @param bytes The amount to bytes to represent.
     *
     * @return The appropriate binary-prefixed unit for the given amount of bytes.
     */
    public static StorageUnit<?> binaryValueOf( final long bytes )
    {
        return binaryValueOf( BigInteger.valueOf( bytes ) );
    }

    /**
     * @param bytes The amount to bytes to represent.
     *
     * @return The appropriate binary-prefixed unit for the given amount of bytes.
     */
    public static StorageUnit<?> binaryValueOf( final BigInteger bytes )
    {
        Assert.notNull( bytes, "BigInteger bytes cannot be null" );

        StorageUnit<?> unit = ByteUnit.valueOf( bytes );
        final BigInteger positiveNumberOfBytes = bytes.signum() == -1 ? bytes.negate() : bytes;

        if( inBetween( BYTES_IN_A_KIBIBYTE, positiveNumberOfBytes, BYTES_IN_A_MEBIBYTE ) )
        {
            unit = unit.asKibibyte();
        }
        else if( inBetween( BYTES_IN_A_MEBIBYTE, positiveNumberOfBytes, BYTES_IN_A_GIBIBYTE ) )
        {
            unit = unit.asMebibyte();
        }
        else if( inBetween( BYTES_IN_A_GIBIBYTE, positiveNumberOfBytes, BYTES_IN_A_TEBIBYTE ) )
        {
            unit = unit.asGibibyte();
        }
        else if( inBetween( BYTES_IN_A_TEBIBYTE, positiveNumberOfBytes, BYTES_IN_A_PEBIBYTE ) )
        {
            unit = unit.asTebibyte();
        }
        else if( inBetween( BYTES_IN_A_PEBIBYTE, positiveNumberOfBytes, BYTES_IN_A_EXBIBYTE ) )
        {
            unit = unit.asPebibyte();
        }
        else if( inBetween( BYTES_IN_A_EXBIBYTE, positiveNumberOfBytes, BYTES_IN_A_ZEBIBYTE ) )
        {
            unit = unit.asExbibyte();
        }
        else if( inBetween( BYTES_IN_A_ZEBIBYTE, positiveNumberOfBytes, BYTES_IN_A_YOBIBYTE ) )
        {
            unit = unit.asZebibyte();
        }
        else if( greaterThanEquals( positiveNumberOfBytes, BYTES_IN_A_YOBIBYTE ) )
        {
            unit = unit.asYobibyte();
        }

        return unit;
    }

    /**
     * @param bytes The amount of bytes to represent.
     *
     * @return The appropriate decimal unit for the given amount of bytes.
     */
    public static StorageUnit<?> decimalValueOf( final long bytes )
    {
        return decimalValueOf( BigInteger.valueOf( bytes ) );
    }

    /**
     * @param bytes The amount of bytes to represent.
     *
     * @return The appropriate decimal unit for the given amount of bytes.
     */
    public static StorageUnit<?> decimalValueOf( final BigInteger bytes )
    {

        Assert.notNull( bytes, "BigInteger bytes cannot be null" );
        StorageUnit<?> unit = ByteUnit.valueOf( bytes );
        final BigInteger positiveNumberOfBytes = bytes.signum() == -1 ?  bytes.negate() : bytes;

        if( inBetween( StorageUnit.BYTES_IN_A_KILOBYTE, positiveNumberOfBytes, BYTES_IN_A_MEGABYTE ) )
        {
            unit = unit.asKilobyte();
        }
        else if( inBetween( BYTES_IN_A_MEGABYTE, positiveNumberOfBytes, BYTES_IN_A_GIGABYTE ) )
        {
            unit = unit.asMegabyte();
        }
        else if( inBetween( BYTES_IN_A_GIGABYTE, positiveNumberOfBytes, BYTES_IN_A_TERABYTE ) )
        {
            unit = unit.asGigabyte();
        }
        else if( inBetween( BYTES_IN_A_TERABYTE, positiveNumberOfBytes, BYTES_IN_A_PETABYTE ) )
        {
            unit = unit.asTerabyte();
        }
        else if( inBetween( BYTES_IN_A_PETABYTE, positiveNumberOfBytes, BYTES_IN_A_EXABYTE ) )
        {
            unit = unit.asPetabyte();
        }
        else if( inBetween( BYTES_IN_A_EXABYTE, positiveNumberOfBytes, BYTES_IN_A_ZETTABYTE ) )
        {
            unit = unit.asExabyte();
        }
        else if( inBetween( BYTES_IN_A_ZETTABYTE, positiveNumberOfBytes, BYTES_IN_A_YOTTABYTE ) )
        {
            unit = unit.asZettabyte();
        }
        else if( greaterThanEquals( positiveNumberOfBytes, BYTES_IN_A_YOTTABYTE ) )
        {
            unit = unit.asYottabyte();
        }

        return unit;
    }

    /**
     * @param bytes The amount to bytes to represent.
     *
     * @return The appropriate common unit for the given amount of bytes.
     */
    public static StorageUnit<?> commonValueOf( final long bytes )
    {
        return commonValueOf( BigInteger.valueOf( bytes ) );
    }

    /**
     * @param bytes The amount to bytes to represent.
     *
     * @return The appropriate common unit for the given amount of bytes.
     */
    public static StorageUnit<?> commonValueOf( final BigInteger bytes )
    {
        StorageUnit<?> unit = ByteUnit.valueOf( bytes );
        final BigInteger positiveNumberOfBytes = bytes.signum() == -1 ?  bytes.negate() : bytes;

        if( inBetween( BYTES_IN_A_KIBIBYTE, positiveNumberOfBytes, BYTES_IN_A_MEBIBYTE ) )
        {
            unit = unit.asCommonKilobyte();
        }
        else if( inBetween( BYTES_IN_A_MEBIBYTE, positiveNumberOfBytes, BYTES_IN_A_GIBIBYTE ) )
        {
            unit = unit.asCommonMegabyte();
        }
        else if( inBetween( BYTES_IN_A_GIBIBYTE, positiveNumberOfBytes, BYTES_IN_A_TEBIBYTE ) )
        {
            unit = unit.asCommonGigabyte();
        }
        else if( inBetween( BYTES_IN_A_TEBIBYTE, positiveNumberOfBytes, BYTES_IN_A_PEBIBYTE ) )
        {
            unit = unit.asCommonTerabyte();
        }
        else if( inBetween( BYTES_IN_A_PEBIBYTE, positiveNumberOfBytes, BYTES_IN_A_EXBIBYTE ) )
        {
            unit = unit.asCommonPetabyte();
        }
        else if( inBetween( BYTES_IN_A_EXBIBYTE, positiveNumberOfBytes, BYTES_IN_A_ZEBIBYTE ) )
        {
            unit = unit.asCommonExabyte();
        }
        else if( inBetween( BYTES_IN_A_ZEBIBYTE, positiveNumberOfBytes, BYTES_IN_A_YOBIBYTE ) )
        {
            unit = unit.asCommonZettabyte();
        }
        else if( greaterThanEquals( positiveNumberOfBytes, BYTES_IN_A_YOBIBYTE ) )
        {
            unit = unit.asCommonYottabyte();
        }

        return unit;
    }

    /**
     * @param numberOfBytes The amount of bytes to create.
     *
     * @return A new unit representing the given amount of bytes.
     */
    public static ByteUnit bytes( final long numberOfBytes )
    {
        return bytes( BigInteger.valueOf( numberOfBytes ) );
    }

    /**
     * @param numberOfBytes The amount of bytes to create.
     *
     * @return A new unit representing the given amount of bytes.
     */
    public static ByteUnit bytes( final BigInteger numberOfBytes )
    {
        return new ByteUnit( numberOfBytes );
    }

    /**
     * @param numberOfKibibytes The amount of kibibytes to create.
     *
     * @return A new unit representing the given amount of kibibytes.
     */
    public static Kibibyte kibibyte( final long numberOfKibibytes )
    {
        return kibibyte( BigInteger.valueOf( numberOfKibibytes ) );
    }

    /**
     * @param numberOfKibibytes The amount of kibibytes to create.
     *
     * @return A new unit representing the given amount of kibibytes.
     */
    public static Kibibyte kibibyte( final BigInteger numberOfKibibytes )
    {
        return new Kibibyte( NullSafe.multiplyNullsafe( BYTES_IN_A_KIBIBYTE, numberOfKibibytes ) );
    }

    /**
     * @param numberOfMebibytes The amount of mebibytes to create.
     *
     * @return A new unit representing the given amount of mebibytes.
     */
    public static Mebibyte mebibyte( final long numberOfMebibytes )
    {
        return mebibyte( BigInteger.valueOf( numberOfMebibytes ) );
    }

    /**
     * @param numberOfMebibytes The amount of mebibytes to create.
     *
     * @return A new unit representing the given amount of mebibytes.
     */
    public static Mebibyte mebibyte( final BigInteger numberOfMebibytes )
    {
        return new Mebibyte( NullSafe.multiplyNullsafe( BYTES_IN_A_MEBIBYTE, numberOfMebibytes ) );
    }

    /**
     * @param numberOfGibibytes The amount of gibibytes to create.
     *
     * @return A new unit representing the given amount of gibibytes.
     */
    public static Gibibyte gibibyte( final long numberOfGibibytes )
    {
        return gibibyte( BigInteger.valueOf( numberOfGibibytes ) );
    }

    /**
     * @param numberOfGibibytes The amount of gibibytes to create.
     *
     * @return A new unit representing the given amount of gibibytes.
     */
    public static Gibibyte gibibyte( final BigInteger numberOfGibibytes )
    {
        return new Gibibyte( NullSafe.multiplyNullsafe( BYTES_IN_A_GIBIBYTE, numberOfGibibytes ) );
    }

    /**
     * @param numberOfTebibytes The amount of tebibytes to create.
     *
     * @return A new unit representing the given amount of tebibytes.
     */
    public static Tebibyte tebibyte( final long numberOfTebibytes )
    {
        return tebibyte( BigInteger.valueOf( numberOfTebibytes ) );
    }

    /**
     * @param numberOfTebibytes The amount of tebibytes to create.
     *
     * @return A new unit representing the given amount of tebibytes.
     */
    public static Tebibyte tebibyte( final BigInteger numberOfTebibytes )
    {
        return new Tebibyte( NullSafe.multiplyNullsafe( BYTES_IN_A_TEBIBYTE, numberOfTebibytes ) );
    }

    /**
     * @param numberOfPebibytes The amount of pebibytes to create.
     *
     * @return A new unit representing the given amount of pebibytes.
     */
    public static Pebibyte pebibyte( final long numberOfPebibytes )
    {
        return pebibyte( BigInteger.valueOf( numberOfPebibytes ) );
    }

    /**
     * @param numberOfPebibytes The amount of pebibytes to create.
     *
     * @return A new unit representing the given amount of pebibytes.
     */
    public static Pebibyte pebibyte( final BigInteger numberOfPebibytes )
    {
        return new Pebibyte( NullSafe.multiplyNullsafe( BYTES_IN_A_PEBIBYTE, numberOfPebibytes ) );
    }

    /**
     * @param numberOfExbibytes The amount of exbibytes to create.
     *
     * @return A new unit representing the given amount of exbibytes.
     */
    public static Exbibyte exbibyte( final long numberOfExbibytes )
    {
        return exbibyte( BigInteger.valueOf( numberOfExbibytes ) );
    }

    /**
     * @param numberOfExbibytes The amount of exbibytes to create.
     *
     * @return A new unit representing the given amount of exbibytes.
     */
    public static Exbibyte exbibyte( final BigInteger numberOfExbibytes )
    {
        return new Exbibyte( NullSafe.multiplyNullsafe( BYTES_IN_A_EXBIBYTE, numberOfExbibytes ) );
    }

    /**
     * @param numberOfZebibytes The amount of zebibytes to create.
     *
     * @return A new unit representing the given amount of zebibytes.
     */
    public static Zebibyte zebibyte( final long numberOfZebibytes )
    {
        return zebibyte( BigInteger.valueOf( numberOfZebibytes ) );
    }

    /**
     * @param numberOfZebibytes The amount of zebibytes to create.
     *
     * @return A new unit representing the given amount of zebibytes.
     */
    public static Zebibyte zebibyte( final BigInteger numberOfZebibytes )
    {
        return new Zebibyte( NullSafe.multiplyNullsafe( BYTES_IN_A_ZEBIBYTE, numberOfZebibytes ) );
    }

    /**
     * @param numberOfYobibytes The amount of yobibytes to create.
     *
     * @return A new unit representing the given amount of yobibytes.
     */
    public static Yobibyte yobibyte( final long numberOfYobibytes )
    {
        return yobibyte( BigInteger.valueOf( numberOfYobibytes ) );
    }

    /**
     * @param numberOfYobibytes The amount of yobibytes to create.
     *
     * @return A new unit representing the given amount of yobibytes.
     */
    public static Yobibyte yobibyte( final BigInteger numberOfYobibytes )
    {
        return new Yobibyte( NullSafe.multiplyNullsafe( BYTES_IN_A_YOBIBYTE, numberOfYobibytes ) );
    }

    /**
     * @param numberOfKilobytes The number of kilobytes to create.
     *
     * @return A new unit representing the given amount of kilobytes.
     */
    public static Kilobyte kilobyte( final long numberOfKilobytes )
    {
        return kilobyte( BigInteger.valueOf( numberOfKilobytes ) );
    }

    /**
     * @param numberOfKilobytes The number of kilobytes to create.
     *
     * @return A new unit representing the given amount of kilobytes.
     */
    public static Kilobyte kilobyte( final BigInteger numberOfKilobytes )
    {
        return new Kilobyte( NullSafe.multiplyNullsafe( BYTES_IN_A_KILOBYTE, numberOfKilobytes ) );
    }

    /**
     * @param numberOfMegabytes The number of megabytes to create.
     *
     * @return A new unit representing the given amount of megabytes.
     */
    public static Megabyte megabyte( final long numberOfMegabytes )
    {
        return megabyte( BigInteger.valueOf( numberOfMegabytes ) );
    }

    /**
     * @param numberOfMegabytes The number of megabytes to create.
     *
     * @return A new unit representing the given amount of megabytes.
     */
    public static Megabyte megabyte( final BigInteger numberOfMegabytes )
    {
        return new Megabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_MEGABYTE, numberOfMegabytes ) );
    }

    /**
     * @param numberOfGigabytes The number of gigabytes to create.
     *
     * @return A new unit representing the given amount of gigabytes.
     */
    public static Gigabyte gigabyte( final long numberOfGigabytes )
    {
        return gigabyte( BigInteger.valueOf( numberOfGigabytes ) );
    }

    /**
     * @param numberOfGigabytes The number of gigabytes to create.
     *
     * @return A new unit representing the given amount of gigabytes.
     */
    public static Gigabyte gigabyte( final BigInteger numberOfGigabytes )
    {
        return new Gigabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_GIGABYTE, numberOfGigabytes ) );
    }

    /**
     * @param numberOfTerabytes The number of terabytes to create.
     *
     * @return A new unit representing the given amount of terabytes.
     */
    public static Terabyte terabyte( final long numberOfTerabytes )
    {
        return terabyte( BigInteger.valueOf( numberOfTerabytes ) );
    }

    /**
     * @param numberOfTerabytes The number of terabytes to create.
     *
     * @return A new unit representing the given amount of terabytes.
     */
    public static Terabyte terabyte( final BigInteger numberOfTerabytes )
    {
        return new Terabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_TERABYTE, numberOfTerabytes ) );
    }

    /**
     * @param numberOfPetabytes The number of petabytes to create.
     *
     * @return A new unit representing the given amount of petabytes.
     */
    public static Petabyte petabyte( final long numberOfPetabytes )
    {
        return petabyte( BigInteger.valueOf( numberOfPetabytes ) );
    }

    /**
     * @param numberOfPetabytes The number of petabytes to create.
     *
     * @return A new unit representing the given amount of petabytes.
     */
    public static Petabyte petabyte( final BigInteger numberOfPetabytes )
    {
        return new Petabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_PETABYTE, numberOfPetabytes ) );
    }

    /**
     * @param numberOfExabytes The number of exabytes to create.
     *
     * @return A new unit representing the given amount of exabytes.
     */
    public static Exabyte exabyte( final long numberOfExabytes )
    {
        return exabyte( BigInteger.valueOf( numberOfExabytes ) );
    }

    /**
     * @param numberOfExabytes The number of exabytes to create.
     *
     * @return A new unit representing the given amount of exabytes.
     */
    public static Exabyte exabyte( final BigInteger numberOfExabytes )
    {
        return new Exabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_EXABYTE, numberOfExabytes ) );
    }

    /**
     * @param numberOfZettabytes The number of zettabytes to create.
     *
     * @return A new unit representing the given amount of zettabytes.
     */
    public static Zettabyte zettabyte( final long numberOfZettabytes )
    {
        return zettabyte( BigInteger.valueOf( numberOfZettabytes ) );
    }

    /**
     * @param numberOfZettabytes The number of zettabytes to create.
     *
     * @return A new unit representing the given amount of zettabytes.
     */
    public static Zettabyte zettabyte( final BigInteger numberOfZettabytes )
    {
        return new Zettabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_ZETTABYTE, numberOfZettabytes ) );
    }

    /**
     * @param numberOfYottabytes The number of yottabytes to create.
     *
     * @return A new unit representing the given amount of yottabytes.
     */
    public static Yottabyte yottabyte( final long numberOfYottabytes )
    {
        return yottabyte( BigInteger.valueOf( numberOfYottabytes ) );
    }

    /**
     * @param numberOfYottabytes The number of yottabytes to create.
     *
     * @return A new unit representing the given amount of yottabytes.
     */
    public static Yottabyte yottabyte( final BigInteger numberOfYottabytes )
    {
        return new Yottabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_YOTTABYTE, numberOfYottabytes ) );
    }

    /**
     * @param numberOfKilobytes The number of kilobytes to create.
     *
     * @return A new unit representing the given amount of kilobytes.
     */
    public static CommonKilobyte commonKilobyte( final long numberOfKilobytes )
    {
        return commonKilobyte( BigInteger.valueOf( numberOfKilobytes ) );
    }

    /**
     * @param numberOfKilobytes The number of kilobytes to create.
     *
     * @return A new unit representing the given amount of kilobytes.
     */
    public static CommonKilobyte commonKilobyte( final BigInteger numberOfKilobytes )
    {
        return new CommonKilobyte( NullSafe.multiplyNullsafe( BYTES_IN_A_KIBIBYTE, numberOfKilobytes ) );
    }

    /**
     * @param numberOfMegabytes The number of megabytes to create.
     *
     * @return A new unit representing the given amount of megabytes.
     */
    public static CommonMegabyte commonMegabyte( final long numberOfMegabytes )
    {
        return commonMegabyte( BigInteger.valueOf( numberOfMegabytes ) );
    }

    /**
     * @param numberOfMegabytes The number of megabytes to create.
     *
     * @return A new unit representing the given amount of megabytes.
     */
    public static CommonMegabyte commonMegabyte( final BigInteger numberOfMegabytes )
    {
        return new CommonMegabyte( Nullsafe.multiplyNullsafe( BYTES_IN_A_MEBIBYTE, numberOfMegabytes ) );
    }

    /**
     * @param numberOfGigabytes The number of gigabytes to create.
     *
     * @return A new unit representing the given amount of gigabytes.
     */
    public static CommonGigabyte commonGigabyte( final long numberOfGigabytes )
    {
        return commonGigabyte( BigInteger.valueOf( numberOfGigabytes ) );
    }

    /**
     * @param numberOfGigabytes The number of gigabytes to create.
     *
     * @return A new unit representing the given amount of gigabytes.
     */
    public static CommonGigabyte commonGigabyte( final BigInteger numberOfGigabytes )
    {
        return new CommonGigabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_GIBIBYTE, numberOfGigabytes ) );
    }

    /**
     * @param numberOfTerabytes The number of terabytes to create.
     *
     * @return A new unit representing the given amount of terabytes.
     */
    public static CommonTerabyte commonTerabyte( final long numberOfTerabytes )
    {
        return commonTerabyte( BigInteger.valueOf( numberOfTerabytes ) );
    }

    /**
     * @param numberOfTerabytes The number of terabytes to create.
     *
     * @return A new unit representing the given amount of terabytes.
     */
    public static CommonTerabyte commonTerabyte( final BigInteger numberOfTerabytes )
    {
        return new CommonTerabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_TEBIBYTE, numberOfTerabytes ) );
    }

    /**
     * @param numberOfPetabytes The number of petabytes to create.
     *
     * @return A new unit representing the given amount of petabytes.
     */
    public static CommonPetabyte commonPetabyte( final long numberOfPetabytes )
    {
        return commonPetabyte( BigInteger.valueOf( numberOfPetabytes ) );
    }

    /**
     * @param numberOfPetabytes The number of petabytes to create.
     *
     * @return A new unit representing the given amount of petabytes.
     */
    public static CommonPetabyte commonPetabyte( final BigInteger numberOfPetabytes )
    {
        return new CommonPetabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_PEBIBYTE, numberOfPetabytes ) );
    }

    /**
     * @param numberOfExabytes The number of exabytes to create.
     *
     * @return A new unit representing the given amount of exabytes.
     */
    public static CommonExabyte commonExabyte( final long numberOfExabytes )
    {
        return commonExabyte( BigInteger.valueOf( numberOfExabytes ) );
    }

    /**
     * @param numberOfExabytes The number of exabytes to create.
     *
     * @return A new unit representing the given amount of exabytes.
     */
    public static CommonExabyte commonExabyte( final BigInteger numberOfExabytes )
    {
        return new CommonExabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_EXBIBYTE, numberOfExabytes ) );
    }

    /**
     * @param numberOfZettabytes The number of zettabytes to create.
     *
     * @return A new unit representing the given amount of zettabytes.
     */
    public static CommonZettabyte commonZettabyte( final long numberOfZettabytes )
    {
        return commonZettabyte( BigInteger.valueOf( numberOfZettabytes ) );
    }

    /**
     * @param numberOfZettabytes The number of zettabytes to create.
     *
     * @return A new unit representing the given amount of zettabytes.
     */
    public static CommonZettabyte commonZettabyte( final BigInteger numberOfZettabytes )
    {
        return new CommonZettabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_ZEBIBYTE, numberOfZettabytes ) );
    }

    /**
     * @param numberOfYottabytes The number of yottabytes to create.
     *
     * @return A new unit representing the given amount of yottabytes.
     */
    public static CommonYottabyte commonYottabyte( final long numberOfYottabytes )
    {
        return commonYottabyte( BigInteger.valueOf( numberOfYottabytes ) );
    }

    /**
     * @param numberOfYottabytes The number of yottabytes to create.
     *
     * @return A new unit representing the given amount of yottabytes.
     */
    public static CommonYottabyte commonYottabyte( final BigInteger numberOfYottabytes )
    {
        return new CommonYottabyte( NullSafe.multiplyNullsafe( BYTES_IN_A_YOBIBYTE, numberOfYottabytes ) );
    }
}

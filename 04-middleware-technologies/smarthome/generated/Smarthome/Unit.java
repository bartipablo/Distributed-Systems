//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.10
//
// <auto-generated>
//
// Generated from file `smarthome.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package Smarthome;

/**
 * FRIDGE
 **/
public enum Unit implements java.io.Serializable
{
    GRAM(0),
    KILOGRAM(1),
    LITRE(2),
    MILLILITRE(3);

    public int value()
    {
        return _value;
    }

    public static Unit valueOf(int v)
    {
        switch(v)
        {
        case 0:
            return GRAM;
        case 1:
            return KILOGRAM;
        case 2:
            return LITRE;
        case 3:
            return MILLILITRE;
        }
        return null;
    }

    private Unit(int v)
    {
        _value = v;
    }

    public void ice_write(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeEnum(_value, 3);
    }

    public static void ice_write(com.zeroc.Ice.OutputStream ostr, Unit v)
    {
        if(v == null)
        {
            ostr.writeEnum(Smarthome.Unit.GRAM.value(), 3);
        }
        else
        {
            ostr.writeEnum(v.value(), 3);
        }
    }

    public static Unit ice_read(com.zeroc.Ice.InputStream istr)
    {
        int v = istr.readEnum(3);
        return validate(v);
    }

    public static void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<Unit> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    public static void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, Unit v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.Size))
        {
            ice_write(ostr, v);
        }
    }

    public static java.util.Optional<Unit> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.Size))
        {
            return java.util.Optional.of(ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static Unit validate(int v)
    {
        final Unit e = valueOf(v);
        if(e == null)
        {
            throw new com.zeroc.Ice.MarshalException("enumerator value " + v + " is out of range");
        }
        return e;
    }

    private final int _value;
}
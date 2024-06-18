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

public interface MowerPrx extends DevicePrx
{
    default Position getPosition()
        throws DevicesIsInStandbyMode
    {
        return getPosition(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default Position getPosition(java.util.Map<String, String> context)
        throws DevicesIsInStandbyMode
    {
        try
        {
            return _iceI_getPositionAsync(context, true).waitForResponseOrUserEx();
        }
        catch(DevicesIsInStandbyMode ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<Position> getPositionAsync()
    {
        return _iceI_getPositionAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Position> getPositionAsync(java.util.Map<String, String> context)
    {
        return _iceI_getPositionAsync(context, false);
    }

    /**
     * @hidden
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Position> _iceI_getPositionAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Position> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "getPosition", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_getPosition);
        f.invoke(true, context, null, null, istr -> {
                     Position ret;
                     ret = Position.ice_read(istr);
                     return ret;
                 });
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_getPosition =
    {
        DevicesIsInStandbyMode.class
    };

    default void setSpeed(int speed)
        throws BatteryLvlLow,
               DevicesIsInStandbyMode,
               InputSpeedOutOfRange
    {
        setSpeed(speed, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default void setSpeed(int speed, java.util.Map<String, String> context)
        throws BatteryLvlLow,
               DevicesIsInStandbyMode,
               InputSpeedOutOfRange
    {
        try
        {
            _iceI_setSpeedAsync(speed, context, true).waitForResponseOrUserEx();
        }
        catch(BatteryLvlLow ex)
        {
            throw ex;
        }
        catch(DevicesIsInStandbyMode ex)
        {
            throw ex;
        }
        catch(InputSpeedOutOfRange ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<Void> setSpeedAsync(int speed)
    {
        return _iceI_setSpeedAsync(speed, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Void> setSpeedAsync(int speed, java.util.Map<String, String> context)
    {
        return _iceI_setSpeedAsync(speed, context, false);
    }

    /**
     * @hidden
     * @param iceP_speed -
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<Void> _iceI_setSpeedAsync(int iceP_speed, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<Void> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "setSpeed", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_setSpeed);
        f.invoke(true, context, null, ostr -> {
                     ostr.writeInt(iceP_speed);
                 }, null);
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_setSpeed =
    {
        BatteryLvlLow.class,
        DevicesIsInStandbyMode.class,
        InputSpeedOutOfRange.class
    };

    default int getSpeed()
        throws DevicesIsInStandbyMode
    {
        return getSpeed(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default int getSpeed(java.util.Map<String, String> context)
        throws DevicesIsInStandbyMode
    {
        try
        {
            return _iceI_getSpeedAsync(context, true).waitForResponseOrUserEx();
        }
        catch(DevicesIsInStandbyMode ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<java.lang.Integer> getSpeedAsync()
    {
        return _iceI_getSpeedAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.Integer> getSpeedAsync(java.util.Map<String, String> context)
    {
        return _iceI_getSpeedAsync(context, false);
    }

    /**
     * @hidden
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<java.lang.Integer> _iceI_getSpeedAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.Integer> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "getSpeed", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_getSpeed);
        f.invoke(true, context, null, null, istr -> {
                     int ret;
                     ret = istr.readInt();
                     return ret;
                 });
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_getSpeed =
    {
        DevicesIsInStandbyMode.class
    };

    default double getBatteryLevel()
        throws DevicesIsInStandbyMode
    {
        return getBatteryLevel(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default double getBatteryLevel(java.util.Map<String, String> context)
        throws DevicesIsInStandbyMode
    {
        try
        {
            return _iceI_getBatteryLevelAsync(context, true).waitForResponseOrUserEx();
        }
        catch(DevicesIsInStandbyMode ex)
        {
            throw ex;
        }
        catch(com.zeroc.Ice.UserException ex)
        {
            throw new com.zeroc.Ice.UnknownUserException(ex.ice_id(), ex);
        }
    }

    default java.util.concurrent.CompletableFuture<java.lang.Double> getBatteryLevelAsync()
    {
        return _iceI_getBatteryLevelAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.Double> getBatteryLevelAsync(java.util.Map<String, String> context)
    {
        return _iceI_getBatteryLevelAsync(context, false);
    }

    /**
     * @hidden
     * @param context -
     * @param sync -
     * @return -
     **/
    default com.zeroc.IceInternal.OutgoingAsync<java.lang.Double> _iceI_getBatteryLevelAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.Double> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "getBatteryLevel", com.zeroc.Ice.OperationMode.Idempotent, sync, _iceE_getBatteryLevel);
        f.invoke(true, context, null, null, istr -> {
                     double ret;
                     ret = istr.readDouble();
                     return ret;
                 });
        return f;
    }

    /** @hidden */
    static final Class<?>[] _iceE_getBatteryLevel =
    {
        DevicesIsInStandbyMode.class
    };

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static MowerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, ice_staticId(), MowerPrx.class, _MowerPrxI.class);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static MowerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, context, ice_staticId(), MowerPrx.class, _MowerPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static MowerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, ice_staticId(), MowerPrx.class, _MowerPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static MowerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, context, ice_staticId(), MowerPrx.class, _MowerPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @return A proxy for this type.
     **/
    static MowerPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, MowerPrx.class, _MowerPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type.
     **/
    static MowerPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, facet, MowerPrx.class, _MowerPrxI.class);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the per-proxy context.
     * @param newContext The context for the new proxy.
     * @return A proxy with the specified per-proxy context.
     **/
    @Override
    default MowerPrx ice_context(java.util.Map<String, String> newContext)
    {
        return (MowerPrx)_ice_context(newContext);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the adapter ID.
     * @param newAdapterId The adapter ID for the new proxy.
     * @return A proxy with the specified adapter ID.
     **/
    @Override
    default MowerPrx ice_adapterId(String newAdapterId)
    {
        return (MowerPrx)_ice_adapterId(newAdapterId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoints.
     * @param newEndpoints The endpoints for the new proxy.
     * @return A proxy with the specified endpoints.
     **/
    @Override
    default MowerPrx ice_endpoints(com.zeroc.Ice.Endpoint[] newEndpoints)
    {
        return (MowerPrx)_ice_endpoints(newEndpoints);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator cache timeout.
     * @param newTimeout The new locator cache timeout (in seconds).
     * @return A proxy with the specified locator cache timeout.
     **/
    @Override
    default MowerPrx ice_locatorCacheTimeout(int newTimeout)
    {
        return (MowerPrx)_ice_locatorCacheTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the invocation timeout.
     * @param newTimeout The new invocation timeout (in seconds).
     * @return A proxy with the specified invocation timeout.
     **/
    @Override
    default MowerPrx ice_invocationTimeout(int newTimeout)
    {
        return (MowerPrx)_ice_invocationTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for connection caching.
     * @param newCache <code>true</code> if the new proxy should cache connections; <code>false</code> otherwise.
     * @return A proxy with the specified caching policy.
     **/
    @Override
    default MowerPrx ice_connectionCached(boolean newCache)
    {
        return (MowerPrx)_ice_connectionCached(newCache);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoint selection policy.
     * @param newType The new endpoint selection policy.
     * @return A proxy with the specified endpoint selection policy.
     **/
    @Override
    default MowerPrx ice_endpointSelection(com.zeroc.Ice.EndpointSelectionType newType)
    {
        return (MowerPrx)_ice_endpointSelection(newType);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for how it selects endpoints.
     * @param b If <code>b</code> is <code>true</code>, only endpoints that use a secure transport are
     * used by the new proxy. If <code>b</code> is false, the returned proxy uses both secure and
     * insecure endpoints.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default MowerPrx ice_secure(boolean b)
    {
        return (MowerPrx)_ice_secure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the encoding used to marshal parameters.
     * @param e The encoding version to use to marshal request parameters.
     * @return A proxy with the specified encoding version.
     **/
    @Override
    default MowerPrx ice_encodingVersion(com.zeroc.Ice.EncodingVersion e)
    {
        return (MowerPrx)_ice_encodingVersion(e);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its endpoint selection policy.
     * @param b If <code>b</code> is <code>true</code>, the new proxy will use secure endpoints for invocations
     * and only use insecure endpoints if an invocation cannot be made via secure endpoints. If <code>b</code> is
     * <code>false</code>, the proxy prefers insecure endpoints to secure ones.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default MowerPrx ice_preferSecure(boolean b)
    {
        return (MowerPrx)_ice_preferSecure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the router.
     * @param router The router for the new proxy.
     * @return A proxy with the specified router.
     **/
    @Override
    default MowerPrx ice_router(com.zeroc.Ice.RouterPrx router)
    {
        return (MowerPrx)_ice_router(router);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator.
     * @param locator The locator for the new proxy.
     * @return A proxy with the specified locator.
     **/
    @Override
    default MowerPrx ice_locator(com.zeroc.Ice.LocatorPrx locator)
    {
        return (MowerPrx)_ice_locator(locator);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for collocation optimization.
     * @param b <code>true</code> if the new proxy enables collocation optimization; <code>false</code> otherwise.
     * @return A proxy with the specified collocation optimization.
     **/
    @Override
    default MowerPrx ice_collocationOptimized(boolean b)
    {
        return (MowerPrx)_ice_collocationOptimized(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses twoway invocations.
     * @return A proxy that uses twoway invocations.
     **/
    @Override
    default MowerPrx ice_twoway()
    {
        return (MowerPrx)_ice_twoway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses oneway invocations.
     * @return A proxy that uses oneway invocations.
     **/
    @Override
    default MowerPrx ice_oneway()
    {
        return (MowerPrx)_ice_oneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch oneway invocations.
     * @return A proxy that uses batch oneway invocations.
     **/
    @Override
    default MowerPrx ice_batchOneway()
    {
        return (MowerPrx)_ice_batchOneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses datagram invocations.
     * @return A proxy that uses datagram invocations.
     **/
    @Override
    default MowerPrx ice_datagram()
    {
        return (MowerPrx)_ice_datagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch datagram invocations.
     * @return A proxy that uses batch datagram invocations.
     **/
    @Override
    default MowerPrx ice_batchDatagram()
    {
        return (MowerPrx)_ice_batchDatagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, except for compression.
     * @param co <code>true</code> enables compression for the new proxy; <code>false</code> disables compression.
     * @return A proxy with the specified compression setting.
     **/
    @Override
    default MowerPrx ice_compress(boolean co)
    {
        return (MowerPrx)_ice_compress(co);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection timeout setting.
     * @param t The connection timeout for the proxy in milliseconds.
     * @return A proxy with the specified timeout.
     **/
    @Override
    default MowerPrx ice_timeout(int t)
    {
        return (MowerPrx)_ice_timeout(t);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection ID.
     * @param connectionId The connection ID for the new proxy. An empty string removes the connection ID.
     * @return A proxy with the specified connection ID.
     **/
    @Override
    default MowerPrx ice_connectionId(String connectionId)
    {
        return (MowerPrx)_ice_connectionId(connectionId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except it's a fixed proxy bound
     * the given connection.@param connection The fixed proxy connection.
     * @return A fixed proxy bound to the given connection.
     **/
    @Override
    default MowerPrx ice_fixed(com.zeroc.Ice.Connection connection)
    {
        return (MowerPrx)_ice_fixed(connection);
    }

    static String ice_staticId()
    {
        return "::Smarthome::Mower";
    }
}
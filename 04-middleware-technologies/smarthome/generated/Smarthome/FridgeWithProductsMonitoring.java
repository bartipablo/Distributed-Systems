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

public interface FridgeWithProductsMonitoring extends Fridge
{
    Product[] getProducts(com.zeroc.Ice.Current current)
        throws DevicesIsInStandbyMode;

    Product getProduct(int id, com.zeroc.Ice.Current current)
        throws DevicesIsInStandbyMode,
               ProductDoesNotExist;

    Product[] getExpiredProducts(com.zeroc.Ice.Current current)
        throws DevicesIsInStandbyMode;

    void addProduct(Product product, com.zeroc.Ice.Current current)
        throws DevicesIsInStandbyMode,
               InvalidDate;

    void addProducts(Product[] products, com.zeroc.Ice.Current current)
        throws DevicesIsInStandbyMode,
               InvalidDate;

    void removeProduct(int productId, com.zeroc.Ice.Current current)
        throws DevicesIsInStandbyMode,
               ProductDoesNotExist;

    /** @hidden */
    static final String[] _iceIds =
    {
        "::Ice::Object",
        "::Smarthome::Device",
        "::Smarthome::Fridge",
        "::Smarthome::FridgeWithProductsMonitoring"
    };

    @Override
    default String[] ice_ids(com.zeroc.Ice.Current current)
    {
        return _iceIds;
    }

    @Override
    default String ice_id(com.zeroc.Ice.Current current)
    {
        return ice_staticId();
    }

    static String ice_staticId()
    {
        return "::Smarthome::FridgeWithProductsMonitoring";
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getProducts(FridgeWithProductsMonitoring obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(com.zeroc.Ice.OperationMode.Idempotent, current.mode);
        inS.readEmptyParams();
        Product[] ret = obj.getProducts(current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ProductListHelper.write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getProduct(FridgeWithProductsMonitoring obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(com.zeroc.Ice.OperationMode.Idempotent, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        int iceP_id;
        iceP_id = istr.readInt();
        inS.endReadParams();
        Product ret = obj.getProduct(iceP_id, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        Product.ice_write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getExpiredProducts(FridgeWithProductsMonitoring obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(com.zeroc.Ice.OperationMode.Idempotent, current.mode);
        inS.readEmptyParams();
        Product[] ret = obj.getExpiredProducts(current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ProductListHelper.write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_addProduct(FridgeWithProductsMonitoring obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(com.zeroc.Ice.OperationMode.Idempotent, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        Product iceP_product;
        iceP_product = Product.ice_read(istr);
        inS.endReadParams();
        obj.addProduct(iceP_product, current);
        return inS.setResult(inS.writeEmptyParams());
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_addProducts(FridgeWithProductsMonitoring obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(com.zeroc.Ice.OperationMode.Idempotent, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        Product[] iceP_products;
        iceP_products = ProductListHelper.read(istr);
        inS.endReadParams();
        obj.addProducts(iceP_products, current);
        return inS.setResult(inS.writeEmptyParams());
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_removeProduct(FridgeWithProductsMonitoring obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(com.zeroc.Ice.OperationMode.Idempotent, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        int iceP_productId;
        iceP_productId = istr.readInt();
        inS.endReadParams();
        obj.removeProduct(iceP_productId, current);
        return inS.setResult(inS.writeEmptyParams());
    }

    /** @hidden */
    final static String[] _iceOps =
    {
        "addProduct",
        "addProducts",
        "getExpiredProducts",
        "getId",
        "getMode",
        "getProduct",
        "getProducts",
        "getTemperature",
        "getTemperatureRange",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "removeProduct",
        "setMode",
        "setTemperature"
    };

    /** @hidden */
    @Override
    default java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceDispatch(com.zeroc.IceInternal.Incoming in, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        int pos = java.util.Arrays.binarySearch(_iceOps, current.operation);
        if(pos < 0)
        {
            throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return _iceD_addProduct(this, in, current);
            }
            case 1:
            {
                return _iceD_addProducts(this, in, current);
            }
            case 2:
            {
                return _iceD_getExpiredProducts(this, in, current);
            }
            case 3:
            {
                return Device._iceD_getId(this, in, current);
            }
            case 4:
            {
                return Device._iceD_getMode(this, in, current);
            }
            case 5:
            {
                return _iceD_getProduct(this, in, current);
            }
            case 6:
            {
                return _iceD_getProducts(this, in, current);
            }
            case 7:
            {
                return Fridge._iceD_getTemperature(this, in, current);
            }
            case 8:
            {
                return Fridge._iceD_getTemperatureRange(this, in, current);
            }
            case 9:
            {
                return com.zeroc.Ice.Object._iceD_ice_id(this, in, current);
            }
            case 10:
            {
                return com.zeroc.Ice.Object._iceD_ice_ids(this, in, current);
            }
            case 11:
            {
                return com.zeroc.Ice.Object._iceD_ice_isA(this, in, current);
            }
            case 12:
            {
                return com.zeroc.Ice.Object._iceD_ice_ping(this, in, current);
            }
            case 13:
            {
                return _iceD_removeProduct(this, in, current);
            }
            case 14:
            {
                return Device._iceD_setMode(this, in, current);
            }
            case 15:
            {
                return Fridge._iceD_setTemperature(this, in, current);
            }
        }

        assert(false);
        throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
    }
}

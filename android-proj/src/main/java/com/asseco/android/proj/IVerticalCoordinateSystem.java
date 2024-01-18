package com.asseco.android.proj;


/**
 * A one-dimensional coordinate system suitable for vertical measurements.
 */
public interface IVerticalCoordinateSystem extends ICoordinateSystem {
    /**
     * Gets the vertical datum, which indicates the measurement method
     *
     * @return the vertical datum
     * @throws Exception the exception
     */
    IVerticalDatum getVerticalDatum() throws Exception;

    /**
     * Sets vertical datum.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setVerticalDatum(IVerticalDatum value) throws Exception;

    /**
     * Gets the units used along the vertical axis.
     *
     * @return the vertical unit
     * @throws Exception the exception
     */
    ILinearUnit getVerticalUnit() throws Exception;

    /**
     * Sets vertical unit.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setVerticalUnit(ILinearUnit value) throws Exception;

}



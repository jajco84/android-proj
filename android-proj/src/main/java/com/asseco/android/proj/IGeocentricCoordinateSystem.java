package com.asseco.android.proj;

/**
 * A 3D coordinate system, with its origin at the center of the Earth.
 */
public interface IGeocentricCoordinateSystem extends ICoordinateSystem {
    /**
     * Returns the HorizontalDatum. The horizontal datum is used to determine where
     * the centre of the Earth is considered to be. All coordinate points will be
     * measured from the centre of the Earth, and not the surface.
     *
     * @return the horizontal datum
     * @throws Exception the exception
     */
    IHorizontalDatum getHorizontalDatum() throws Exception;

    /**
     * Sets horizontal datum.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setHorizontalDatum(IHorizontalDatum value) throws Exception;

    /**
     * Gets the units used along all the axes.
     *
     * @return the linear unit
     * @throws Exception the exception
     */
    ILinearUnit getLinearUnit() throws Exception;

    /**
     * Sets linear unit.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setLinearUnit(ILinearUnit value) throws Exception;

    /**
     * Returns the PrimeMeridian.
     *
     * @return the prime meridian
     * @throws Exception the exception
     */
    IPrimeMeridian getPrimeMeridian() throws Exception;

    /**
     * Sets prime meridian.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setPrimeMeridian(IPrimeMeridian value) throws Exception;

}



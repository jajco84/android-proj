package com.asseco.android.proj;

/**
 * The IGeographicCoordinateSystem interface is a subclass of IGeodeticSpatialReference and
 * defines the standard information stored with geographic coordinate system objects.
 */
public interface IGeographicCoordinateSystem extends IHorizontalCoordinateSystem {
    /**
     * Gets or sets the angular units of the geographic coordinate system.
     *
     * @return the unit
     * @throws Exception the exception
     */
    IAngularUnit getangularUnit() throws Exception;

    /**
     * Sets unit.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setangularUnit(IAngularUnit value) throws Exception;

    /**
     * Gets or sets the prime meridian of the geographic coordinate system.
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

    /**
     * Gets the number of available conversions to WGS84 coordinates.
     *
     * @return the num conversion to wgs 84
     * @throws Exception the exception
     */
    int getNumConversionToWGS84() throws Exception;

    /**
     * Gets details on a conversion to WGS84.
     *
     * @param index the index
     * @return the wgs 84 conversion info
     * @throws Exception the exception
     */
    Wgs84ConversionInfo getWgs84ConversionInfo(int index) throws Exception;

}



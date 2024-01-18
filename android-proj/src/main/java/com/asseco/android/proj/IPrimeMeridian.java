package com.asseco.android.proj;

/**
 * The IPrimeMeridian interface defines the standard information stored with prime
 * meridian objects. Any prime meridian object must implement this interface as
 * well as the ISpatialReferenceInfo interface.
 */
public interface IPrimeMeridian extends IInfo {
    /**
     * Gets or sets the longitude of the prime meridian (relative to the Greenwich prime meridian).
     *
     * @return the longitude
     * @throws Exception the exception
     */
    double getLongitude() throws Exception;

    /**
     * Sets longitude.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setLongitude(double value) throws Exception;

    /**
     * Gets or sets the AngularUnits.
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

}



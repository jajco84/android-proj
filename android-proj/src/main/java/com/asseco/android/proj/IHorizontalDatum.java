package com.asseco.android.proj;


/**
 * Procedure used to measure positions on the surface of the Earth.
 */
public interface IHorizontalDatum extends IDatum {
    /**
     * Gets or sets the ellipsoid of the datum.
     *
     * @return the ellipsoid
     * @throws Exception the exception
     */
    IEllipsoid getEllipsoid() throws Exception;

    /**
     * Sets ellipsoid.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setEllipsoid(IEllipsoid value) throws Exception;

    /**
     * Gets preferred parameters for a Bursa Wolf transformation into WGS84. The 7 returned values
     * correspond to (dx,dy,dz) in meters, (ex,ey,ez) in arc-seconds, and scaling in parts-per-million.
     *
     * @return the wgs 84 parameters
     * @throws Exception the exception
     */
    Wgs84ConversionInfo getWgs84Parameters() throws Exception;

    /**
     * Sets wgs 84 parameters.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setWgs84Parameters(Wgs84ConversionInfo value) throws Exception;

}



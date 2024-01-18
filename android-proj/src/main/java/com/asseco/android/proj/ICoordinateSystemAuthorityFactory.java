package com.asseco.android.proj;

/**
 * Creates spatial reference objects using codes.
 *
 * The codes are maintained by an external authority. A commonly used authority is EPSG, which is also used in the GeoTIFF standard and in SharpMap.
 */
public interface ICoordinateSystemAuthorityFactory {
    /**
     * Returns the authority name for this factory (e.g., "EPSG" or "POSC").
     *
     * @return the authority
     * @throws Exception the exception
     */
    String getAuthority() throws Exception;

    /**
     * Returns a projected coordinate system object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The projected coordinate system object with the given code.
     * @throws Exception the exception
     */
    IProjectedCoordinateSystem createProjectedCoordinateSystem(long code) throws Exception;

    /**
     * Returns a geographic coordinate system object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The geographic coordinate system object with the given code.
     * @throws Exception the exception
     */
    IGeographicCoordinateSystem createGeographicCoordinateSystem(long code) throws Exception;

    /**
     * Returns a horizontal datum object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The horizontal datum object with the given code.
     * @throws Exception the exception
     */
    IHorizontalDatum createHorizontalDatum(long code) throws Exception;

    /**
     * Returns an ellipsoid object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The ellipsoid object with the given code.
     * @throws Exception the exception
     */
    IEllipsoid createEllipsoid(long code) throws Exception;

    /**
     * Returns a prime meridian object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The prime meridian object with the given code.
     * @throws Exception the exception
     */
    IPrimeMeridian createPrimeMeridian(long code) throws Exception;

    /**
     * Returns a linear unit object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The linear unit object with the given code.
     * @throws Exception the exception
     */
    ILinearUnit createLinearUnit(long code) throws Exception;

    /**
     * Returns an
     * {@link IAngularUnit}
     * object corresponding to the given code.
     *
     * @param code The identification code.
     * @return The angular unit object for the given code.
     * @throws Exception the exception
     */
    IAngularUnit createAngularUnit(long code) throws Exception;

    /**
     * Creates a
     * {@link IVerticalDatum}
     * from a code.
     *
     * @param code Authority code
     * @return Vertical datum for the given code
     * @throws Exception the exception
     */
    IVerticalDatum createVerticalDatum(long code) throws Exception;

    /**
     * Create a
     * {@link IVerticalCoordinateSystem}
     * from a code.
     *
     * @param code Authority code
     * @return vertical coordinate system
     * @throws Exception the exception
     */
    IVerticalCoordinateSystem createVerticalCoordinateSystem(long code) throws Exception;

    /**
     * Creates a 3D coordinate system from a code.
     *
     * @param code Authority code
     * @return Compound coordinate system for the given code
     * @throws Exception the exception
     */
    ICompoundCoordinateSystem createCompoundCoordinateSystem(long code) throws Exception;

    /**
     * Creates a
     * {@link IHorizontalCoordinateSystem}
     * from a code.
     * The horizontal coordinate system could be geographic or projected.
     *
     * @param code Authority code
     * @return Horizontal coordinate system for the given code
     * @throws Exception the exception
     */
    IHorizontalCoordinateSystem createHorizontalCoordinateSystem(long code) throws Exception;

    /**
     * Gets a description of the object corresponding to a code.
     *
     * @return the description text
     * @throws Exception the exception
     */
    String getDescriptionText() throws Exception;

    /**
     * Gets the Geoid code from a WKT name.
     *
     * In the OGC definition of WKT horizontal datums, the geoid is referenced
     * by a quoted string, which is used as a key value. This method converts
     * the key value string into a code recognized by this authority.
     *
     * @param wkt the wkt
     * @return string
     * @throws Exception the exception
     */
    String geoidFromWktName(String wkt) throws Exception;

    /**
     * Gets the WKT name of a Geoid.
     *
     * In the OGC definition of WKT horizontal datums, the geoid is referenced by
     * a quoted string, which is used as a key value. This method gets the OGC WKT
     * key value from a geoid code.
     *
     * @param geoid the geoid
     * @return string
     * @throws Exception the exception
     */
    String wktGeoidName(String geoid) throws Exception;

}



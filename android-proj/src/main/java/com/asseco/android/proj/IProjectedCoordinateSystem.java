package com.asseco.android.proj;


/**
 * The IProjectedCoordinateSystem interface defines the standard information stored with
 * projected coordinate system objects. A projected coordinate system is defined using a
 * geographic coordinate system object and a projection object that defines the
 * coordinate transformation from the geographic coordinate system to the projected
 * coordinate systems. The instances of a single ProjectedCoordinateSystem COM class can
 * be used to model different projected coordinate systems (e.g., UTM Zone 10, Albers)
 * by associating the ProjectedCoordinateSystem instances with Projection instances
 * belonging to different Projection COM classes (Transverse Mercator and Albers,
 * respectively).
 */
public interface IProjectedCoordinateSystem extends IHorizontalCoordinateSystem {
    /**
     * Gets or sets the geographic coordinate system associated with the projected
     * coordinate system.
     *
     * @return the geographic coordinate system
     * @throws Exception the exception
     */
    IGeographicCoordinateSystem getGeographicCoordinateSystem() throws Exception;

    /**
     * Sets geographic coordinate system.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setGeographicCoordinateSystem(IGeographicCoordinateSystem value) throws Exception;

    /**
     * Gets or sets the linear (projected) units of the projected coordinate system.
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
     * Gets or sets the projection for the projected coordinate system.
     *
     * @return the projection
     * @throws Exception the exception
     */
    IProjection getProjection() throws Exception;

    /**
     * Sets projection.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setProjection(IProjection value) throws Exception;

}



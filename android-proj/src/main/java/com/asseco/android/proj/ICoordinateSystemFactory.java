package com.asseco.android.proj;

import java.util.List;

/**
 * Builds up complex objects from simpler objects or values.
 * ICoordinateSystemFactory allows applications to make coordinate systems that
 * cannot be created by a
 * {ICoordinateSystemAuthorityFactory}
 * . This factory is very
 * flexible, whereas the authority factory is easier to use.So
 * {ICoordinateSystemAuthorityFactory}
 * can be used to make 'standard' coordinate
 * systems, and
 * {ICoordinateSystemFactory}
 * can be used to make 'special'
 * coordinate systems.For example, the EPSG authority has codes for USA state plane coordinate systems
 * using the NAD83 datum, but these coordinate systems always use meters. EPSG does not
 * have codes for NAD83 state plane coordinate systems that use feet units. This factory
 * lets an application create such a hybrid coordinate system.
 */
public interface ICoordinateSystemFactory {
    /**
     * Creates a
     * {ICompoundCoordinateSystem}
     * .
     *
     * @param name Name of compound coordinate system.
     * @param head Head coordinate system
     * @param tail Tail coordinate system
     * @return Compound coordinate system
     * @throws Exception the exception
     */
    ICompoundCoordinateSystem createCompoundCoordinateSystem(String name, ICoordinateSystem head, ICoordinateSystem tail) throws Exception;

    /**
     * Creates an
     * {IEllipsoid}
     * from radius values.
     *
     * CreateFlattenedSphere
     *
     * @param name          Name of ellipsoid
     * @param semiMajorAxis the semi major axis
     * @param semiMinorAxis the semi minor axis
     * @param linearUnit    the linear unit
     * @return Ellipsoid ellipsoid
     * @throws Exception the exception
     */
    IEllipsoid createEllipsoid(String name, double semiMajorAxis, double semiMinorAxis, ILinearUnit linearUnit) throws Exception;

    /**
     * Creates a
     * {IFittedCoordinateSystem}
     * .
     * The units of the axes in the fitted coordinate system will be
     * inferred from the units of the base coordinate system. If the affine map
     * performs a rotation, then any mixed axes must have identical units. For
     * example, a (lat_deg,lon_deg,height_feet) system can be rotated in the
     * (lat,lon) plane, since both affected axes are in degrees. But you
     * should not rotate this coordinate system in any other plane.
     *
     * @param name                 Name of coordinate system
     * @param baseCoordinateSystem Base coordinate system
     * @param toBaseWkt            the to base wkt
     * @param arAxes               the ar axes
     * @return Fitted coordinate system
     * @throws Exception the exception
     */
    IFittedCoordinateSystem createFittedCoordinateSystem(String name, ICoordinateSystem baseCoordinateSystem, String toBaseWkt, List<AxisInfo> arAxes) throws Exception;

    /**
     * Creates an
     * {IEllipsoid}
     * from an major radius, and inverse flattening.
     *
     * CreateEllipsoid
     *
     * @param name              Name of ellipsoid
     * @param semiMajorAxis     Semi major-axis
     * @param inverseFlattening Inverse flattening
     * @param linearUnit        Linear unit
     * @return Ellipsoid ellipsoid
     * @throws Exception the exception
     */
    IEllipsoid createFlattenedSphere(String name, double semiMajorAxis, double inverseFlattening, ILinearUnit linearUnit) throws Exception;

    /**
     * Creates a coordinate system object from an XML string.
     *
     * @param xml XML representation for the spatial reference
     * @return The resulting spatial reference object
     * @throws Exception the exception
     */
    ICoordinateSystem createFromXml(String xml) throws Exception;

    /**
     * Creates a spatial reference object given its Well-known text representation.
     * The output object may be either a
     * {IGeographicCoordinateSystem}
     * or
     * a
     * {IProjectedCoordinateSystem}
     * .
     *
     * @param WKT The Well-known text representation for the spatial reference
     * @return The resulting spatial reference object
     * @throws Exception the exception
     */
    ICoordinateSystem createFromWkt(String WKT) throws Exception;

    /**
     * Creates a
     * {IGeographicCoordinateSystem}
     * , which could be Lat/Lon or Lon/Lat.
     *
     * @param name          Name of geographical coordinate system
     * @param angularUnit   Angular units
     * @param datum         Horizontal datum
     * @param primeMeridian Prime meridian
     * @param axis0         First axis
     * @param axis1         Second axis
     * @return Geographic coordinate system
     * @throws Exception the exception
     */
    IGeographicCoordinateSystem createGeographicCoordinateSystem(String name, IAngularUnit angularUnit, IHorizontalDatum datum, IPrimeMeridian primeMeridian, AxisInfo axis0, AxisInfo axis1) throws Exception;

    /**
     * Creates
     * {IHorizontalDatum}
     * from ellipsoid and Bursa-World parameters.
     *
     * since this method contains a set of Bursa-Wolf parameters, the created
     * datum will always have a relationship to WGS84. If you wish to create a
     * horizontal datum that has no relationship with WGS84, then you can
     * either specify a
     * {DatumType}
     * of
     * {DatumType.HD_Other}
     * , or create it via WKT.
     *
     * @param name      Name of ellipsoid
     * @param datumType Type of datum
     * @param ellipsoid Ellipsoid
     * @param toWgs84   Wgs84 conversion parameters
     * @return Horizontal datum
     * @throws Exception the exception
     */
    IHorizontalDatum createHorizontalDatum(String name, DatumType datumType, IEllipsoid ellipsoid, Wgs84ConversionInfo toWgs84) throws Exception;

    /**
     * Creates a
     * {ILocalCoordinateSystem}
     * .
     *
     * The dimension of the local coordinate system is determined by the size of
     * the axis array. All the axes will have the same units. If you want to make
     * a coordinate system with mixed units, then you can make a compound
     * coordinate system from different local coordinate systems.
     *
     * @param name  Name of local coordinate system
     * @param datum Local datum
     * @param unit  Units
     * @param axes  Axis info
     * @return Local coordinate system
     * @throws Exception the exception
     */
    ILocalCoordinateSystem createLocalCoordinateSystem(String name, ILocalDatum datum, IUnit unit, List<AxisInfo> axes) throws Exception;

    /**
     * Creates a
     * {ILocalDatum}
     * .
     *
     * @param name      Name of datum
     * @param datumType Datum type
     * @return local datum
     * @throws Exception the exception
     */
    ILocalDatum createLocalDatum(String name, DatumType datumType) throws Exception;

    /**
     * Creates a
     * {IPrimeMeridian}
     * , relative to Greenwich.
     *
     * @param name        Name of prime meridian
     * @param angularUnit Angular unit
     * @param longitude   Longitude
     * @return Prime meridian
     * @throws Exception the exception
     */
    IPrimeMeridian createPrimeMeridian(String name, IAngularUnit angularUnit, double longitude) throws Exception;

    /**
     * Creates a
     * {IProjectedCoordinateSystem}
     * using a projection object.
     *
     * @param name       Name of projected coordinate system
     * @param gcs        Geographic coordinate system
     * @param projection Projection
     * @param linearUnit Linear unit
     * @param axis0      Primary axis
     * @param axis1      Secondary axis
     * @return Projected coordinate system
     * @throws Exception the exception
     */
    IProjectedCoordinateSystem createProjectedCoordinateSystem(String name, IGeographicCoordinateSystem gcs, IProjection projection, ILinearUnit linearUnit, AxisInfo axis0, AxisInfo axis1) throws Exception;

    /**
     * Creates a
     * {IProjection}
     * .
     *
     * @param name               Name of projection
     * @param wktProjectionClass Projection class
     * @param Parameters         Projection parameters
     * @return Projection projection
     * @throws Exception the exception
     */
    IProjection createProjection(String name, String wktProjectionClass, List<ProjectionParameter> Parameters) throws Exception;

    /**
     * Creates a
     * {@link IVerticalCoordinateSystem}
     * from a
     * {@link IVerticalDatum}
     * and
     * {@link ILinearUnit}
     * .
     *
     * @param name         Name of vertical coordinate system
     * @param datum        Vertical datum
     * @param verticalUnit Unit
     * @param axis         Axis info
     * @return Vertical coordinate system
     * @throws Exception the exception
     */
    IVerticalCoordinateSystem createVerticalCoordinateSystem(String name, IVerticalDatum datum, ILinearUnit verticalUnit, AxisInfo axis) throws Exception;

    /**
     * Creates a
     * {@link IVerticalDatum}
     * from an enumerated type value.
     *
     * @param name      Name of datum
     * @param datumType Type of datum
     * @return Vertical datum
     * @throws Exception the exception
     */
    IVerticalDatum createVerticalDatum(String name, DatumType datumType) throws Exception;

}



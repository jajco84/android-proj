package com.asseco.android.proj;

import java.util.ArrayList;
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
 * {@link #CoordinateSystemFactory}
 * can be used to make 'special'
 * coordinate systems.For example, the EPSG authority has codes for USA state plane coordinate systems
 * using the NAD83 datum, but these coordinate systems always use meters. EPSG does not
 * have codes for NAD83 state plane coordinate systems that use feet units. This factory
 * lets an application create such a hybrid coordinate system.
 */
public class CoordinateSystemFactory implements ICoordinateSystemFactory {
    //  private Encoding __Encoding = new Encoding();
    // public Encoding getEncoding() {
    //    return __Encoding;
    // }

    //public void setEncoding(Encoding value) {
    //    __Encoding = value;
    // }

    /**
     * Instantiates a new Coordinate system factory.
     *
     * @throws Exception the exception
     */
    public CoordinateSystemFactory() throws Exception {

    }

    /*public CoordinateSystemFactory(Encoding encoding) throws Exception {
        if (encoding == null)
            throw new ArgumentNullException("encoding");
         
        setEncoding(encoding);
    }*/

    /**
     * Creates a coordinate system object from an XML string.
     *
     * @param xml XML representation for the spatial reference
     * @return The resulting spatial reference object
     */
    public ICoordinateSystem createFromXml(String xml) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a spatial reference object given its Well-known text representation.
     * The output object may be either a
     * IGeographicCoordinateSystem
     * or
     * a
     * IProjectedCoordinateSystem
     * .
     *
     * @param WKT The Well-known text representation for the spatial reference
     * @return The resulting spatial reference object
     */
    public ICoordinateSystem createFromWkt(String WKT) throws Exception {
        IInfo info = CoordinateSystemWktReader.parse(WKT);
        return info instanceof ICoordinateSystem ? (ICoordinateSystem) info : (ICoordinateSystem) null;
    }

    /**
     * Creates a
     * {ICompoundCoordinateSystem}
     * [NOT IMPLEMENTED].
     *
     * @param name Name of compound coordinate system.
     * @param head Head coordinate system
     * @param tail Tail coordinate system
     * @return Compound coordinate system
     */
    public ICompoundCoordinateSystem createCompoundCoordinateSystem(String name, ICoordinateSystem head, ICoordinateSystem tail) throws Exception {
        throw new UnsupportedOperationException();
    }

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
     * @param toBaseWkt
     * @param arAxes
     * @return Fitted coordinate system
     */
    public IFittedCoordinateSystem createFittedCoordinateSystem(String name, ICoordinateSystem baseCoordinateSystem, String toBaseWkt, List<AxisInfo> arAxes) throws Exception {
        throw new UnsupportedOperationException();
    }

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
     */
    public ILocalCoordinateSystem createLocalCoordinateSystem(String name, ILocalDatum datum, IUnit unit, List<AxisInfo> axes) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates an
     * {Ellipsoid}
     * from radius values.
     *
     * CreateFlattenedSphere
     *
     * @param name          Name of ellipsoid
     * @param semiMajorAxis
     * @param semiMinorAxis
     * @param linearUnit
     * @return Ellipsoid
     */
    public IEllipsoid createEllipsoid(String name, double semiMajorAxis, double semiMinorAxis, ILinearUnit linearUnit) throws Exception {
        double ivf = 0;
        if (semiMajorAxis != semiMinorAxis)
            ivf = semiMajorAxis / (semiMajorAxis - semiMinorAxis);

        return new Ellipsoid(semiMajorAxis, semiMinorAxis, ivf, false, linearUnit, name, "", -1, "", "", "");
    }

    /**
     * Creates an
     * {Ellipsoid}
     * from an major radius, and inverse flattening.
     *
     * CreateEllipsoid
     *
     * @param name              Name of ellipsoid
     * @param semiMajorAxis     Semi major-axis
     * @param inverseFlattening Inverse flattening
     * @param linearUnit        Linear unit
     * @return Ellipsoid
     */
    public IEllipsoid createFlattenedSphere(String name, double semiMajorAxis, double inverseFlattening, ILinearUnit linearUnit) throws Exception {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid name");

        return new Ellipsoid(semiMajorAxis, -1, inverseFlattening, true, linearUnit, name, "", -1, "", "", "");
    }

    /**
     * Creates a
     * {ProjectedCoordinateSystem}
     * using a projection object.
     *
     * @param name       Name of projected coordinate system
     * @param gcs        Geographic coordinate system
     * @param projection Projection
     * @param linearUnit Linear unit
     * @param axis0      Primary axis
     * @param axis1      Secondary axis
     * @return Projected coordinate system
     */
    public IProjectedCoordinateSystem createProjectedCoordinateSystem(String name, IGeographicCoordinateSystem gcs, IProjection projection, ILinearUnit linearUnit, AxisInfo axis0, AxisInfo axis1) throws Exception {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid name");

        if (gcs == null)
            throw new IllegalArgumentException("Geographic coordinate system was null");

        if (projection == null)
            throw new IllegalArgumentException("Projection was null");

        if (linearUnit == null)
            throw new IllegalArgumentException("Linear unit was null");

        List<AxisInfo> info = new ArrayList<AxisInfo>(2);
        info.add(axis0);
        info.add(axis1);
        return new ProjectedCoordinateSystem(null, gcs, linearUnit, projection, info, name, "", -1, "", "", "");
    }

    /**
     * Creates a
     * {Projection}
     * .
     *
     * @param name               Name of projection
     * @param wktProjectionClass Projection class
     * @param parameters         Projection parameters
     * @return Projection
     */
    public IProjection createProjection(String name, String wktProjectionClass, List<ProjectionParameter> parameters) throws Exception {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid name");

        if (parameters == null || parameters.size() == 0)
            throw new IllegalArgumentException("Invalid projection parameters");

        return new Projection(wktProjectionClass, parameters, name, "", -1, "", "", "");
    }

    /**
     * Creates
     * HorizontalDatum
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
     */
    public IHorizontalDatum createHorizontalDatum(String name, DatumType datumType, IEllipsoid ellipsoid, Wgs84ConversionInfo toWgs84) throws Exception {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid name");

        if (ellipsoid == null)
            throw new IllegalArgumentException("Ellipsoid was null");

        return new HorizontalDatum(ellipsoid, toWgs84, datumType, name, "", -1, "", "", "");
    }

    /**
     * Creates a
     * {PrimeMeridian}
     * , relative to Greenwich.
     *
     * @param name        Name of prime meridian
     * @param angularUnit Angular unit
     * @param longitude   Longitude
     * @return Prime meridian
     */
    public IPrimeMeridian createPrimeMeridian(String name, IAngularUnit angularUnit, double longitude) throws Exception {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid name");

        return new PrimeMeridian(longitude, angularUnit, name, "", -1, "", "", "");
    }

    /**
     * Creates a
     * {GeographicCoordinateSystem}
     * , which could be Lat/Lon or Lon/Lat.
     *
     * @param name          Name of geographical coordinate system
     * @param angularUnit   Angular units
     * @param datum         Horizontal datum
     * @param primeMeridian Prime meridian
     * @param axis0         First axis
     * @param axis1         Second axis
     * @return Geographic coordinate system
     */
    public IGeographicCoordinateSystem createGeographicCoordinateSystem(String name, IAngularUnit angularUnit, IHorizontalDatum datum, IPrimeMeridian primeMeridian, AxisInfo axis0, AxisInfo axis1) throws Exception {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid name");

        List<AxisInfo> info = new ArrayList<AxisInfo>(2);
        info.add(axis0);
        info.add(axis1);
        return new GeographicCoordinateSystem(angularUnit, datum, primeMeridian, info, name, "", -1, "", "", "");
    }

    /**
     * Creates a
     * {ILocalDatum}
     * .
     *
     * @param name      Name of datum
     * @param datumType Datum type
     * @return
     */
    public ILocalDatum createLocalDatum(String name, DatumType datumType) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a
     * {IVerticalDatum}
     * from an enumerated type value.
     *
     * @param name      Name of datum
     * @param datumType Type of datum
     * @return Vertical datum
     */
    public IVerticalDatum createVerticalDatum(String name, DatumType datumType) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a
     * {IVerticalCoordinateSystem}
     * from a
     * {IVerticalDatum}
     * and
     * {LinearUnit}
     * .
     *
     * @param name         Name of vertical coordinate system
     * @param datum        Vertical datum
     * @param verticalUnit Unit
     * @param axis         Axis info
     * @return Vertical coordinate system
     */
    public IVerticalCoordinateSystem createVerticalCoordinateSystem(String name, IVerticalDatum datum, ILinearUnit verticalUnit, AxisInfo axis) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a
     * {CreateGeocentricCoordinateSystem}
     * from a
     * {IHorizontalDatum}
     * ,
     * <p>
     * {ILinearUnit}
     * and
     * {IPrimeMeridian}
     * .
     *
     * @param name          Name of geocentric coordinate system
     * @param datum         Horizontal datum
     * @param linearUnit    Linear unit
     * @param primeMeridian Prime meridian
     * @return Geocentric Coordinate System
     * @throws Exception the exception
     */
    public IGeocentricCoordinateSystem createGeocentricCoordinateSystem(String name, IHorizontalDatum datum, ILinearUnit linearUnit, IPrimeMeridian primeMeridian) throws Exception {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid name");

        List<AxisInfo> info = new ArrayList<AxisInfo>(3);
        info.add(new AxisInfo("X", AxisOrientationEnum.Other));
        info.add(new AxisInfo("Y", AxisOrientationEnum.Other));
        info.add(new AxisInfo("Z", AxisOrientationEnum.Other));
        return new GeocentricCoordinateSystem(datum, linearUnit, primeMeridian, info, name, "", -1, "", "", "");
    }

}



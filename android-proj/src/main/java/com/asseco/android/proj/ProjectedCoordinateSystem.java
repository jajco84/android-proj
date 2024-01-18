package com.asseco.android.proj;

import java.util.ArrayList;
import java.util.List;


/**
 * A 2D cartographic coordinate system.
 */
public class ProjectedCoordinateSystem extends HorizontalCoordinateSystem implements IProjectedCoordinateSystem {
    private IGeographicCoordinateSystem _GeographicCoordinateSystem;
    private ILinearUnit _LinearUnit;
    private IProjection _Projection;

    /**
     * Initializes a new instance of a projected coordinate system
     *
     * @param datum                      Horizontal datum
     * @param geographicCoordinateSystem Geographic coordinate system
     * @param linearUnit                 Linear unit
     * @param projection                 Projection
     * @param AxisInfo                   Axis info
     * @param name                       Name
     * @param authority                  Authority name
     * @param code                       Authority-specific identification code.
     * @param alias                      Alias
     * @param remarks                    Provider-supplied remarks
     * @param abbreviation               Abbreviation
     * @throws Exception the exception
     */
    public ProjectedCoordinateSystem(IHorizontalDatum datum, IGeographicCoordinateSystem geographicCoordinateSystem, ILinearUnit linearUnit, IProjection projection, List<AxisInfo> AxisInfo, String name, String authority, long code, String alias, String remarks, String abbreviation) throws Exception {
        super(datum, AxisInfo, name, authority, code, alias, abbreviation, remarks);
        _GeographicCoordinateSystem = geographicCoordinateSystem;
        _LinearUnit = linearUnit;
        _Projection = projection;
    }

    /**
     * Universal Transverse Mercator - WGS84
     *
     * @param zone        UTM zone
     * @param zoneIsNorth true of Northern hemisphere, false if southern
     * @return UTM /WGS84 coordsys
     * @throws Exception the exception
     */
    public static IProjectedCoordinateSystem wGS84_UTM(int zone, boolean zoneIsNorth) throws Exception {
        List<ProjectionParameter> pInfo = new ArrayList<ProjectionParameter>();
        pInfo.add(new ProjectionParameter("latitude_of_origin", 0));
        pInfo.add(new ProjectionParameter("central_meridian", zone * 6 - 183));
        pInfo.add(new ProjectionParameter("scale_factor", 0.9996));
        pInfo.add(new ProjectionParameter("false_easting", 500000));
        pInfo.add(new ProjectionParameter("false_northing", zoneIsNorth ? 0 : 10000000));
        //IProjection projection = cFac.CreateProjection("UTM" + Zone.ToString() + (ZoneIsNorth ? "N" : "S"), "Transverse_Mercator", parameters);
        Projection proj = new Projection("Transverse_Mercator", pInfo, "UTM" + zone + (zoneIsNorth ? "N" : "S"), "EPSG", 32600 + zone + (zoneIsNorth ? 0 : 100), "", "", "");
        List<AxisInfo> axes = new ArrayList<AxisInfo>();
        axes.add(new AxisInfo("East", AxisOrientationEnum.East));
        axes.add(new AxisInfo("North", AxisOrientationEnum.North));
        return new ProjectedCoordinateSystem(HorizontalDatum.getWGS84(), GeographicCoordinateSystem.getWGS84(), LinearUnit.getMetre(), proj, axes, "WGS 84 / UTM zone " + zone + (zoneIsNorth ? "N" : "S"), "EPSG", 32600 + zone + (zoneIsNorth ? 0 : 100), "", "Large and medium scale topographic mapping and engineering survey.", "");
    }

    /**
     * Gets a WebMercator coordinate reference system
     *
     * @return the web mercator
     * @throws Exception the exception
     */
    public static IProjectedCoordinateSystem getWebMercator() throws Exception {
        /*
                                new ProjectionParameter("semi_major", 6378137.0),
                                new ProjectionParameter("semi_minor", 6378137.0),
                                new ProjectionParameter("scale_factor", 1.0),
                                 */
        List<ProjectionParameter> pInfo = new ArrayList<ProjectionParameter>();
        pInfo.add(new ProjectionParameter("latitude_of_origin", 0.0));
        pInfo.add(new ProjectionParameter("central_meridian", 0.0));
        pInfo.add(new ProjectionParameter("false_easting", 0.0));
        pInfo.add(new ProjectionParameter("false_northing", 0.0));
        Projection proj = new Projection("Popular Visualisation Pseudo-Mercator", pInfo, "Popular Visualisation Pseudo-Mercator", "EPSG", 3856, "Pseudo-Mercator", "", "");
        List<AxisInfo> axes = new ArrayList<AxisInfo>();
        axes.add(new AxisInfo("East", AxisOrientationEnum.East));
        axes.add(new AxisInfo("North", AxisOrientationEnum.North));
        return new ProjectedCoordinateSystem(HorizontalDatum.getWGS84(), GeographicCoordinateSystem.getWGS84(), LinearUnit.getMetre(), proj, axes, "WGS 84 / Pseudo-Mercator", "EPSG", 3857, "WGS 84 / Popular Visualisation Pseudo-Mercator", "Certain Web mapping and visualisation applications." + "Uses spherical development of ellipsoidal coordinates. Relative to an ellipsoidal development errors of up to 800 metres in position and 0.7 percent in scale may arise. It is not a recognised geodetic system: see WGS 84 / World Mercator (CRS code 3395).", "WebMercator");
    }

    /**
     * Gets or sets the GeographicCoordinateSystem.
     */
    public IGeographicCoordinateSystem getGeographicCoordinateSystem() throws Exception {
        return _GeographicCoordinateSystem;
    }

    public void setGeographicCoordinateSystem(IGeographicCoordinateSystem value) throws Exception {
        _GeographicCoordinateSystem = value;
    }

    /**
     * Gets or sets the
     * {@link LinearUnit}
     * . The linear unit must be the same as the
     *CoordinateSystem
     * units.
     */
    public ILinearUnit getLinearUnit() throws Exception {
        return _LinearUnit;
    }

    public void setLinearUnit(ILinearUnit value) throws Exception {
        _LinearUnit = value;
    }

    /**
     * Gets units for dimension within coordinate system. Each dimension in
     * the coordinate system has corresponding units.
     *
     * @param dimension Dimension
     * @return Unit
     */
    public IUnit getUnits(int dimension) throws Exception {
        return _LinearUnit;
    }

    /**
     * Gets or sets the projection
     */
    public IProjection getProjection() throws Exception {
        return _Projection;
    }

    public void setProjection(IProjection value) throws Exception {
        _Projection = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public String getWKT() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("PROJCS[\"%s\", %s, %s, %s", getName(), getGeographicCoordinateSystem().getWKT(), getLinearUnit().getWKT(), getProjection().getWKT()));
        for (int i = 0; i < getProjection().getNumParameters(); i++)
            sb.append(String.format(", %s", getProjection().getParameter(i).getWKT()));
        //sb.AppendFormat(", {0}", LinearUnit.WKT);
        //Skip authority and code if not defined
        if (getAuthority() != null && getAuthority() != "" && getAuthorityCode() > 0)
            sb.append(String.format(", AUTHORITY[\"%s\", \"%d\"]", getAuthority(), getAuthorityCode()));

        //Skip axis info if they contain default values
        if (getAxisInfo().size() != 2 || !getAxisInfo().get(0).getName().equals("X") || getAxisInfo().get(0).getOrientation() != AxisOrientationEnum.East || !getAxisInfo().get(1).getName().equals("Y") || getAxisInfo().get(1).getOrientation() != AxisOrientationEnum.North)
            for (int i = 0; i < getAxisInfo().size(); i++)
                sb.append(String.format(", %s", getAxis(i).getWKT()));

        //if (!String.IsNullOrEmpty(Authority) && AuthorityCode > 0)
        //    sb.AppendFormat(", AUTHORITY[\"{0}\", \"{1}\"]", Authority, AuthorityCode);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object.
     */
    public String getXML() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<CS_CoordinateSystem Dimension=\"%d\"><CS_ProjectedCoordinateSystem>%s", this.getDimension(), getInfoXml()));
        for (Object __dummyForeachVar0 : this.getAxisInfo()) {
            AxisInfo ai = (AxisInfo) __dummyForeachVar0;
            sb.append(ai.getXML());
        }
        sb.append(String.format("%s%s%s</CS_ProjectedCoordinateSystem></CS_CoordinateSystem>", getGeographicCoordinateSystem().getXML(), getLinearUnit().getXML(), getProjection().getXML()));
        return sb.toString();
    }

    /**
     * Checks whether the values of this instance is equal to the values of another instance.
     * Only parameters used for coordinate system are used for comparison.
     * Name, abbreviation, authority, alias and remarks are ignored in the comparison.
     *
     * @param obj
     * @return True if equal
     */
    public boolean equalParams(Object obj) throws Exception {
        if (!(obj instanceof ProjectedCoordinateSystem))
            return false;

        ProjectedCoordinateSystem pcs = obj instanceof ProjectedCoordinateSystem ? (ProjectedCoordinateSystem) obj : null;
        if (pcs != null) {
            if (pcs.getDimension() != this.getDimension())
                return false;

            for (int i = 0; i < pcs.getDimension(); i++) {
                if (pcs.getAxis(i).getOrientation() != this.getAxis(i).getOrientation())
                    return false;

                if (!pcs.getUnits(i).equalParams(this.getUnits(i)))
                    return false;

            }
            return pcs.getGeographicCoordinateSystem().equalParams(this.getGeographicCoordinateSystem()) && pcs.getHorizontalDatum().equalParams(this.getHorizontalDatum()) && pcs.getLinearUnit().equalParams(this.getLinearUnit()) && pcs.getProjection().equalParams(this.getProjection());
        }
        return false;
    }

}



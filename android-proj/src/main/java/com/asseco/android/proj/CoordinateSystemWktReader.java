package com.asseco.android.proj;

import com.asseco.android.proj.transformations.IMathTransform;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates an object based on the supplied Well Known Text (WKT).
 */
public class CoordinateSystemWktReader {
    /**
     * Reads and parses a WKT-formatted projection string.
     *
     * @param wkt String containing WKT.
     * @return Object representation of the WKT.
     * @throws Exception If a token is not recognised.
     */
    public static IInfo parse(String wkt) throws Exception {
        if (wkt == null || wkt == "")
            throw new Exception("empty wkt");


        try {

            StringReader reader = new StringReader(wkt);
            try {

                WktStreamTokenizer tokenizer = new WktStreamTokenizer(reader);
                tokenizer.nextToken();
                String objectName = tokenizer.getStringValue();
                if (objectName.equals("UNIT")) {
                    return readUnit(tokenizer);
                } else if (objectName.equals("SPHEROID")) {
                    return readEllipsoid(tokenizer);
                } else if (objectName.equals("DATUM")) {
                    return readHorizontalDatum(tokenizer);
                } else if (objectName.equals("PRIMEM")) {
                    return readPrimeMeridian(tokenizer);
                } else if (objectName.equals("VERT_CS") || objectName.equals("GEOGCS") || objectName.equals("PROJCS") || objectName.equals("COMPD_CS") || objectName.equals("GEOCCS") || objectName.equals("FITTED_CS") || objectName.equals("LOCAL_CS")) {
                    return readCoordinateSystem(wkt, tokenizer);
                } else {
                    throw new IllegalArgumentException(String.format("'%s' is not recognized.", objectName));
                }

            } finally {
                //  if (reader != null)
                //      Disposable.mkDisposable(reader).dispose();

            }

        } finally {
            // if (stream != null)
            //     Disposable.mkDisposable(stream).dispose();

        }
    }

    /**
     * Returns a IUnit given a piece of WKT.
     *
     * @param tokenizer WktStreamTokenizer that has the WKT.
     * @return An object that implements the IUnit interface.
     */
    private static IUnit readUnit(WktStreamTokenizer tokenizer) throws Exception {
        tokenizer.readToken("[");
        String unitName = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        double unitsPerUnit = tokenizer.getNumericValue();
        String authority = "";
        long authorityCode = -1;
        tokenizer.nextToken();
        if (tokenizer.getStringValue().equals(",")) {

            WktStreamTokenizer.Authority auth = tokenizer.readAuthority();
            authority = auth.authority;
            authorityCode = auth.authorityCode;
            tokenizer.readToken("]");
        }

        return new Unit(unitsPerUnit, unitName, authority, authorityCode, "", "", "");
    }

    /**
     * Returns a
     * {LinearUnit}
     * given a piece of WKT.
     *
     * @param tokenizer WktStreamTokenizer that has the WKT.
     * @return An object that implements the IUnit interface.
     */
    private static ILinearUnit readLinearUnit(WktStreamTokenizer tokenizer) throws Exception {
        tokenizer.readToken("[");
        String unitName = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        double unitsPerUnit = tokenizer.getNumericValue();
        String authority = "";
        long authorityCode = -1;
        tokenizer.nextToken();
        if (tokenizer.getStringValue().equals(",")) {
            WktStreamTokenizer.Authority auth = tokenizer.readAuthority();
            authority = auth.authority;
            authorityCode = auth.authorityCode;
            tokenizer.readToken("]");
        }

        return new LinearUnit(unitsPerUnit, unitName, authority, authorityCode, "", "", "");
    }

    /**
     * Returns a
     * {AngularUnit}
     * given a piece of WKT.
     *
     * @param tokenizer WktStreamTokenizer that has the WKT.
     * @return An object that implements the IUnit interface.
     */
    private static IAngularUnit readAngularUnit(WktStreamTokenizer tokenizer) throws Exception {
        tokenizer.readToken("[");
        String unitName = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        double unitsPerUnit = tokenizer.getNumericValue();
        String authority = "";
        long authorityCode = -1;
        tokenizer.nextToken();
        if (tokenizer.getStringValue().equals(",")) {
            WktStreamTokenizer.Authority auth = tokenizer.readAuthority();
            authority = auth.authority;
            authorityCode = auth.authorityCode;
            tokenizer.readToken("]");
        }

        return new AngularUnit(unitsPerUnit, unitName, authority, authorityCode, "", "", "");
    }

    /**
     * Returns a
     * {Axisinfo}
     * given a piece of WKT.
     *
     * @param tokenizer WktStreamTokenizer that has the WKT.
     * @return An Axisinfo object.
     */
    private static AxisInfo readAxis(WktStreamTokenizer tokenizer) throws Exception {
        if (!tokenizer.getStringValue().equals("AXIS"))
            tokenizer.readToken("AXIS");

        tokenizer.readToken("[");
        String axisName = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        String unitname = tokenizer.getStringValue().toUpperCase();
        tokenizer.readToken("]");
        if (unitname.equals("DOWN")) {
            return new AxisInfo(axisName, AxisOrientationEnum.Down);
        } else if (unitname.equals("EAST")) {
            return new AxisInfo(axisName, AxisOrientationEnum.East);
        } else if (unitname.equals("NORTH")) {
            return new AxisInfo(axisName, AxisOrientationEnum.North);
        } else if (unitname.equals("OTHER")) {
            return new AxisInfo(axisName, AxisOrientationEnum.Other);
        } else if (unitname.equals("SOUTH")) {
            return new AxisInfo(axisName, AxisOrientationEnum.South);
        } else if (unitname.equals("UP")) {
            return new AxisInfo(axisName, AxisOrientationEnum.Up);
        } else if (unitname.equals("WEST")) {
            return new AxisInfo(axisName, AxisOrientationEnum.West);
        } else {
            throw new IllegalArgumentException("Invalid axis name '" + unitname + "' in WKT");
        }
    }

    private static ICoordinateSystem readCoordinateSystem(String coordinateSystem, WktStreamTokenizer tokenizer) throws Exception {
        String cstype = tokenizer.getStringValue();
        if (cstype.equals("GEOGCS")) {
            return readGeographicCoordinateSystem(tokenizer);
        } else if (cstype.equals("PROJCS")) {
            return readProjectedCoordinateSystem(tokenizer);
        } else if (cstype.equals("FITTED_CS")) {
            return readFittedCoordinateSystem(tokenizer);
        } else if (cstype.equals("COMPD_CS") || cstype.equals("VERT_CS") || cstype.equals("GEOCCS") || cstype.equals("LOCAL_CS")) {
            throw new Exception(String.format("%s coordinate system is not supported.", coordinateSystem));
        } else {
            throw new Exception(String.format("%s coordinate system is not recognized.", coordinateSystem));
        }
    }

    // Reads either 3, 6 or 7 parameter Bursa-Wolf values from TOWGS84 token
    private static Wgs84ConversionInfo readWGS84ConversionInfo(WktStreamTokenizer tokenizer) throws Exception {
        //TOWGS84[0,0,0,0,0,0,0]
        tokenizer.readToken("[");
        Wgs84ConversionInfo info = new Wgs84ConversionInfo();
        tokenizer.nextToken();
        info.Dx = tokenizer.getNumericValue();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        info.Dy = tokenizer.getNumericValue();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        info.Dz = tokenizer.getNumericValue();
        tokenizer.nextToken();
        if (tokenizer.getStringValue().equals(",")) {
            tokenizer.nextToken();
            info.Ex = tokenizer.getNumericValue();
            tokenizer.readToken(",");
            tokenizer.nextToken();
            info.Ey = tokenizer.getNumericValue();
            tokenizer.readToken(",");
            tokenizer.nextToken();
            info.Ez = tokenizer.getNumericValue();
            tokenizer.nextToken();
            if (tokenizer.getStringValue().equals(",")) {
                tokenizer.nextToken();
                info.Ppm = tokenizer.getNumericValue();
            }

        }

        if (!tokenizer.getStringValue().equals("]"))
            tokenizer.readToken("]");

        return info;
    }

    private static IEllipsoid readEllipsoid(WktStreamTokenizer tokenizer) throws Exception {
        //SPHEROID["Airy 1830",6377563.396,299.3249646,AUTHORITY["EPSG","7001"]]
        tokenizer.readToken("[");
        String name = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        double majorAxis = tokenizer.getNumericValue();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        double e = tokenizer.getNumericValue();
        tokenizer.nextToken();
        String authority = "";
        long authorityCode = -1;
        if (tokenizer.getStringValue().equals(",")) {
            //Read authority
            WktStreamTokenizer.Authority auth = tokenizer.readAuthority();
            authority = auth.authority;
            authorityCode = auth.authorityCode;
            tokenizer.readToken("]");
        }

        IEllipsoid ellipsoid = new Ellipsoid(majorAxis, 0.0, e, true, LinearUnit.getMetre(), name, authority, authorityCode, "", "", "");
        return ellipsoid;
    }

    private static IProjection readProjection(WktStreamTokenizer tokenizer) throws Exception {
        if (!tokenizer.getStringValue().equals("PROJECTION"))
            tokenizer.readToken("PROJECTION");

        tokenizer.readToken("[");
        //[
        String projectionName = tokenizer.readDoubleQuotedWord();
        String authority = "";
        long authorityCode = -1L;
        tokenizer.nextToken(true);
        if (tokenizer.getStringValue().equals(",")) {
            WktStreamTokenizer.Authority auth = tokenizer.readAuthority();
            authority = auth.authority;
            authorityCode = auth.authorityCode;
            tokenizer.readToken("]");
        }

        tokenizer.readToken(",");
        //,
        tokenizer.readToken("PARAMETER");
        List<ProjectionParameter> paramList = new ArrayList<ProjectionParameter>();
        while (tokenizer.getStringValue().equals("PARAMETER")) {
            tokenizer.readToken("[");
            String paramName = tokenizer.readDoubleQuotedWord();
            tokenizer.readToken(",");
            tokenizer.nextToken();
            double paramValue = tokenizer.getNumericValue();
            tokenizer.readToken("]");
            tokenizer.readToken(",");
            paramList.add(new ProjectionParameter(paramName, paramValue));
            tokenizer.nextToken();
        }
        IProjection projection = new Projection(projectionName, paramList, projectionName, authority, authorityCode, "", "", "");
        return projection;
    }

    private static IProjectedCoordinateSystem readProjectedCoordinateSystem(WktStreamTokenizer tokenizer) throws Exception {
        /*PROJCS[
                        "OSGB 1936 / British National Grid",
                        GEOGCS[
                            "OSGB 1936",
                            DATUM[...]
                            PRIMEM[...]
                            AXIS["Geodetic latitude","NORTH"]
                            AXIS["Geodetic longitude","EAST"]
                            AUTHORITY["EPSG","4277"]
                        ],
                        PROJECTION["Transverse Mercator"],
                        PARAMETER["latitude_of_natural_origin",49],
                        PARAMETER["longitude_of_natural_origin",-2],
                        PARAMETER["scale_factor_at_natural_origin",0.999601272],
                        PARAMETER["false_easting",400000],
                        PARAMETER["false_northing",-100000],
                        AXIS["Easting","EAST"],
                        AXIS["Northing","NORTH"],
                        AUTHORITY["EPSG","27700"]
                    ]
                    */
        tokenizer.readToken("[");
        String name = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.readToken("GEOGCS");
        IGeographicCoordinateSystem geographicCS = readGeographicCoordinateSystem(tokenizer);
        tokenizer.readToken(",");
        IProjection projection = null;
        IUnit unit = null;
        List<AxisInfo> axes = new ArrayList<AxisInfo>(2);
        String authority = "";
        long authorityCode = -1;
        TokenType ct = tokenizer.nextToken();
        while (ct != TokenType.Eol && ct != TokenType.Eof) {
            String token = tokenizer.getStringValue();
            if (token.equals(",") || token.equals("]")) {
            } else if (token.equals("PROJECTION")) {
                projection = readProjection(tokenizer);
                ct = tokenizer.getTokenType();
                continue;
            } else //break;
                if (token.equals("UNIT")) {
                    unit = readLinearUnit(tokenizer);
                } else if (token.equals("AXIS")) {
                    axes.add(readAxis(tokenizer));
                    tokenizer.nextToken();
                } else if (token.equals("AUTHORITY")) {
                    WktStreamTokenizer.Authority auth = tokenizer.readAuthority();
                    authority = auth.authority;
                    authorityCode = auth.authorityCode;
                }

            //tokenizer.ReadToken("]");
            ct = tokenizer.nextToken();
        }
        //This is default axis values if not specified.
        if (axes.size() == 0) {
            axes.add(new AxisInfo("X", AxisOrientationEnum.East));
            axes.add(new AxisInfo("Y", AxisOrientationEnum.North));
        }

        IProjectedCoordinateSystem projectedCS = new ProjectedCoordinateSystem(geographicCS.getHorizontalDatum(), geographicCS, unit instanceof LinearUnit ? (LinearUnit) unit : (LinearUnit) null, projection, axes, name, authority, authorityCode, "", "", "");
        return projectedCS;
    }

    private static IGeographicCoordinateSystem readGeographicCoordinateSystem(WktStreamTokenizer tokenizer) throws Exception {
        /*
                    GEOGCS["OSGB 1936",
                    DATUM["OSGB 1936",SPHEROID["Airy 1830",6377563.396,299.3249646,AUTHORITY["EPSG","7001"]],TOWGS84[0,0,0,0,0,0,0],AUTHORITY["EPSG","6277"]]
                    PRIMEM["Greenwich",0,AUTHORITY["EPSG","8901"]]
                    AXIS["Geodetic latitude","NORTH"]
                    AXIS["Geodetic longitude","EAST"]
                    AUTHORITY["EPSG","4277"]
                    ]
                    */
        tokenizer.readToken("[");
        String name = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.readToken("DATUM");
        IHorizontalDatum horizontalDatum = readHorizontalDatum(tokenizer);
        tokenizer.readToken(",");
        tokenizer.readToken("PRIMEM");
        IPrimeMeridian primeMeridian = readPrimeMeridian(tokenizer);
        tokenizer.readToken(",");
        tokenizer.readToken("UNIT");
        IAngularUnit angularUnit = readAngularUnit(tokenizer);
        String authority = "";
        long authorityCode = -1;
        tokenizer.nextToken();
        List<AxisInfo> info = new ArrayList<AxisInfo>(2);
        if (tokenizer.getStringValue().equals(",")) {
            tokenizer.nextToken();
            while (tokenizer.getStringValue().equals("AXIS")) {
                info.add(readAxis(tokenizer));
                tokenizer.nextToken();
                if (tokenizer.getStringValue().equals(","))
                    tokenizer.nextToken();

            }
            if (tokenizer.getStringValue().equals(","))
                tokenizer.nextToken();

            if (tokenizer.getStringValue().equals("AUTHORITY")) {
                WktStreamTokenizer.Authority auth = tokenizer.readAuthority();
                authority = auth.authority;
                authorityCode = auth.authorityCode;
                tokenizer.readToken("]");
            }

        }

        //This is default axis values if not specified.
        if (info.size() == 0) {
            info.add(new AxisInfo("Lon", AxisOrientationEnum.East));
            info.add(new AxisInfo("Lat", AxisOrientationEnum.North));
        }

        IGeographicCoordinateSystem geographicCS = new GeographicCoordinateSystem(angularUnit, horizontalDatum, primeMeridian, info, name, authority, authorityCode, "", "", "");
        return geographicCS;
    }

    private static IHorizontalDatum readHorizontalDatum(WktStreamTokenizer tokenizer) throws Exception {
        //DATUM["OSGB 1936",SPHEROID["Airy 1830",6377563.396,299.3249646,AUTHORITY["EPSG","7001"]],TOWGS84[0,0,0,0,0,0,0],AUTHORITY["EPSG","6277"]]
        Wgs84ConversionInfo wgsinfo = null;
        String authority = "";
        long authorityCode = -1;
        tokenizer.readToken("[");
        String name = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.readToken("SPHEROID");
        IEllipsoid ellipsoid = readEllipsoid(tokenizer);
        tokenizer.nextToken();
        while (tokenizer.getStringValue().equals(",")) {
            tokenizer.nextToken();
            if (tokenizer.getStringValue().equals("TOWGS84")) {
                wgsinfo = readWGS84ConversionInfo(tokenizer);
                tokenizer.nextToken();
            } else if (tokenizer.getStringValue().equals("AUTHORITY")) {
                WktStreamTokenizer.Authority auth = tokenizer.readAuthority();
                authority = auth.authority;
                authorityCode = auth.authorityCode;
                tokenizer.readToken("]");
            }

        }
        // make an assumption about the datum type.
        IHorizontalDatum horizontalDatum = new HorizontalDatum(ellipsoid, wgsinfo, DatumType.HD_Geocentric, name, authority, authorityCode, "", "", "");
        return horizontalDatum;
    }

    private static IPrimeMeridian readPrimeMeridian(WktStreamTokenizer tokenizer) throws Exception {
        //PRIMEM["Greenwich",0,AUTHORITY["EPSG","8901"]]
        tokenizer.readToken("[");
        String name = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.nextToken();
        double longitude = tokenizer.getNumericValue();
        tokenizer.nextToken();
        String authority = "";
        long authorityCode = -1;
        if (tokenizer.getStringValue().equals(",")) {
            WktStreamTokenizer.Authority auth = tokenizer.readAuthority();
            authority = auth.authority;
            authorityCode = auth.authorityCode;
            tokenizer.readToken("]");
        }

        // make an assumption about the Angular units - degrees.
        IPrimeMeridian primeMeridian = new PrimeMeridian(longitude, AngularUnit.getDegrees(), name, authority, authorityCode, "", "", "");
        return primeMeridian;
    }

    private static IFittedCoordinateSystem readFittedCoordinateSystem(WktStreamTokenizer tokenizer) throws Exception {
        /*
                     FITTED_CS[
                         "Local coordinate system MNAU (based on Gauss-Krueger)",
                         PARAM_MT[
                            "Affine",
                            PARAMETER["num_row",3],
                            PARAMETER["num_col",3],
                            PARAMETER["elt_0_0", 0.883485346527455],
                            PARAMETER["elt_0_1", -0.468458794848877],
                            PARAMETER["elt_0_2", 3455869.17937689],
                            PARAMETER["elt_1_0", 0.468458794848877],
                            PARAMETER["elt_1_1", 0.883485346527455],
                            PARAMETER["elt_1_2", 5478710.88035753],
                            PARAMETER["elt_2_2", 1],
                         ],
                         PROJCS["DHDN / Gauss-Kruger zone 3", GEOGCS["DHDN", DATUM["Deutsches_Hauptdreiecksnetz", SPHEROID["Bessel 1841", 6377397.155, 299.1528128, AUTHORITY["EPSG", "7004"]], TOWGS84[612.4, 77, 440.2, -0.054, 0.057, -2.797, 0.525975255930096], AUTHORITY["EPSG", "6314"]], PRIMEM["Greenwich", 0, AUTHORITY["EPSG", "8901"]], UNIT["degree", 0.0174532925199433, AUTHORITY["EPSG", "9122"]], AUTHORITY["EPSG", "4314"]], UNIT["metre", 1, AUTHORITY["EPSG", "9001"]], PROJECTION["Transverse_Mercator"], PARAMETER["latitude_of_origin", 0], PARAMETER["central_meridian", 9], PARAMETER["scale_factor", 1], PARAMETER["false_easting", 3500000], PARAMETER["false_northing", 0], AUTHORITY["EPSG", "31467"]]
                         AUTHORITY["CUSTOM","12345"]
                     ]
                    */
        tokenizer.readToken("[");
        String name = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        tokenizer.readToken("PARAM_MT");
        IMathTransform toBaseTransform = MathTransformWktReader.readMathTransform(tokenizer);
        tokenizer.readToken(",");
        tokenizer.nextToken();
        ICoordinateSystem baseCS = readCoordinateSystem(null, tokenizer);
        String authority = "";
        long authorityCode = -1;
        TokenType ct = tokenizer.nextToken();
        while (ct != TokenType.Eol && ct != TokenType.Eof) {
            String token = tokenizer.getStringValue();
            if (token.equals(",") || token.equals("]")) {
            } else if (token.equals("AUTHORITY")) {
                WktStreamTokenizer.Authority auth = tokenizer.readAuthority();
                authority = auth.authority;
                authorityCode = auth.authorityCode;
            }

            //tokenizer.ReadToken("]");
            ct = tokenizer.nextToken();
        }
        IFittedCoordinateSystem fittedCS = new FittedCoordinateSystem(baseCS, toBaseTransform, name, authority, authorityCode, "", "", "");
        return fittedCS;
    }

}



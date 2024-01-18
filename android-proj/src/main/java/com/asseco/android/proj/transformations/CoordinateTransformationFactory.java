package com.asseco.android.proj.transformations;

import com.asseco.android.proj.CoordinateSystemFactory;
import com.asseco.android.proj.FittedCoordinateSystem;
import com.asseco.android.proj.GeocentricCoordinateSystem;
import com.asseco.android.proj.ICoordinateSystem;
import com.asseco.android.proj.IEllipsoid;
import com.asseco.android.proj.IFittedCoordinateSystem;
import com.asseco.android.proj.IGeocentricCoordinateSystem;
import com.asseco.android.proj.IGeographicCoordinateSystem;
import com.asseco.android.proj.ILinearUnit;
import com.asseco.android.proj.IProjectedCoordinateSystem;
import com.asseco.android.proj.IProjection;
import com.asseco.android.proj.LinearUnit;
import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.projections.AlbersProjection;
import com.asseco.android.proj.projections.CassiniSoldnerProjection;
import com.asseco.android.proj.projections.HotineObliqueMercatorProjection;
import com.asseco.android.proj.projections.KrovakProjection;
import com.asseco.android.proj.projections.LambertConformalConic2SP;
import com.asseco.android.proj.projections.Mercator;
import com.asseco.android.proj.projections.ObliqueMercatorProjection;
import com.asseco.android.proj.projections.ObliqueStereographicProjection;
import com.asseco.android.proj.projections.PolyconicProjection;
import com.asseco.android.proj.projections.PseudoMercator;
import com.asseco.android.proj.projections.TransverseMercator;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates coordinate transformations.
 */
public class CoordinateTransformationFactory implements ICoordinateTransformationFactory {
    private static ICoordinateTransformation geog2Geoc(IGeographicCoordinateSystem source, IGeocentricCoordinateSystem target) throws Exception {
        IMathTransform geocMathTransform = createCoordinateOperation(target);
        if (source.getPrimeMeridian().equalParams(target.getPrimeMeridian())) {
            return new CoordinateTransformation(source, target, TransformType.Conversion, geocMathTransform, "", "", -1, "", "");
        } else {
            ConcatenatedTransform ct = new ConcatenatedTransform();
            ct.getCoordinateTransformationList().add(new CoordinateTransformation(source, target, TransformType.Transformation, new PrimeMeridianTransform(source.getPrimeMeridian(), target.getPrimeMeridian()), "", "", -1, "", ""));
            ct.getCoordinateTransformationList().add(new CoordinateTransformation(source, target, TransformType.Conversion, geocMathTransform, "", "", -1, "", ""));
            return new CoordinateTransformation(source, target, TransformType.Conversion, ct, "", "", -1, "", "");
        }
    }

    //if (trans.MathTransform is ConcatenatedTransform) {
    //    List<ICoordinateTransformation> MTs = new List<ICoordinateTransformation>();
    //    SimplifyTrans(trans.MathTransform as ConcatenatedTransform, ref MTs);
    //    return new CoordinateTransformation(sourceCS,
    //        targetCS, TransformType.Transformation, new ConcatenatedTransform(MTs),
    //        "", "", -1, "", "");
    //}
  /*  private static void simplifyTrans(ConcatenatedTransform mtrans, RefSupport<List<ICoordinateTransformation>> MTs) throws Exception {
        for (Object __dummyForeachVar0 : mtrans.getCoordinateTransformationList())
        {
            ICoordinateTransformation t = (ICoordinateTransformation)__dummyForeachVar0;
            if (t instanceof ConcatenatedTransform)
            {
                RefSupport<List<ICoordinateTransformation>> refVar___0 = new RefSupport<List<ICoordinateTransformation>>(MTs.getValue());
                SimplifyTrans(t instanceof ConcatenatedTransform ? (ConcatenatedTransform)t : (ConcatenatedTransform)null, refVar___0);
                MTs.setValue(refVar___0.getValue());
            }
            else
                MTs.getValue().Add(t); 
        }
    }*/

    private static ICoordinateTransformation geoc2Geog(IGeocentricCoordinateSystem source, IGeographicCoordinateSystem target) throws Exception {
        IMathTransform geocMathTransform = createCoordinateOperation(source).inverse();
        if (source.getPrimeMeridian().equalParams(target.getPrimeMeridian())) {
            return new CoordinateTransformation(source, target, TransformType.Conversion, geocMathTransform, "", "", -1, "", "");
        } else {
            ConcatenatedTransform ct = new ConcatenatedTransform();
            ct.getCoordinateTransformationList().add(new CoordinateTransformation(source, target, TransformType.Conversion, geocMathTransform, "", "", -1, "", ""));
            ct.getCoordinateTransformationList().add(new CoordinateTransformation(source, target, TransformType.Transformation, new PrimeMeridianTransform(source.getPrimeMeridian(), target.getPrimeMeridian()), "", "", -1, "", ""));
            return new CoordinateTransformation(source, target, TransformType.Conversion, ct, "", "", -1, "", "");
        }
    }

    private static ICoordinateTransformation proj2Proj(IProjectedCoordinateSystem source, IProjectedCoordinateSystem target) throws Exception {
        ConcatenatedTransform ct = new ConcatenatedTransform();
        CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
        //First transform from projection to geographic
        ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source, source.getGeographicCoordinateSystem()));
        //Transform geographic to geographic:
        ICoordinateTransformation geogToGeog = ctFac.createFromCoordinateSystems(source.getGeographicCoordinateSystem(), target.getGeographicCoordinateSystem());
        if (geogToGeog != null)
            ct.getCoordinateTransformationList().add(geogToGeog);

        //Transform to new projection
        ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(target.getGeographicCoordinateSystem(), target));
        return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
    }

    private static ICoordinateTransformation geog2Proj(IGeographicCoordinateSystem source, IProjectedCoordinateSystem target) throws Exception {
        if (source.equalParams(target.getGeographicCoordinateSystem())) {
            IMathTransform mathTransform = createCoordinateOperation(target.getProjection(), target.getGeographicCoordinateSystem().getHorizontalDatum().getEllipsoid(), target.getLinearUnit());
            return new CoordinateTransformation(source, target, TransformType.Transformation, mathTransform, "", "", -1, "", "");
        } else {
            // Geographic coordinatesystems differ - Create concatenated transform
            ConcatenatedTransform ct = new ConcatenatedTransform();
            CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
            ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source, target.getGeographicCoordinateSystem()));
            ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(target.getGeographicCoordinateSystem(), target));
            return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
        }
    }

    private static ICoordinateTransformation proj2Geog(IProjectedCoordinateSystem source, IGeographicCoordinateSystem target) throws Exception {
        if (source.getGeographicCoordinateSystem().equalParams(target)) {
            IMathTransform mathTransform = createCoordinateOperation(source.getProjection(), source.getGeographicCoordinateSystem().getHorizontalDatum().getEllipsoid(), source.getLinearUnit()).inverse();
            return new CoordinateTransformation(source, target, TransformType.Transformation, mathTransform, "", "", -1, "", "");
        } else {
            // Geographic coordinatesystems differ - Create concatenated transform
            ConcatenatedTransform ct = new ConcatenatedTransform();
            CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
            ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source, source.getGeographicCoordinateSystem()));
            ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source.getGeographicCoordinateSystem(), target));
            return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
        }
    }

    /**
     * Geographic to geographic transformation
     * Adds a datum shift if nessesary
     *
     * @param source
     * @param target
     * @return
     */
    private static ICoordinateTransformation createGeog2Geog(IGeographicCoordinateSystem source, IGeographicCoordinateSystem target) throws Exception {
        if (source.getHorizontalDatum().equalParams(target.getHorizontalDatum())) {
            return new CoordinateTransformation(source, target, TransformType.Conversion, new GeographicTransform(source, target), "", "", -1, "", "");
        } else {
            //No datum shift needed
            //Create datum shift
            //Convert to geocentric, perform shift and return to geographic
            CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
            CoordinateSystemFactory cFac = new CoordinateSystemFactory();
            IGeocentricCoordinateSystem sourceCentric = cFac.createGeocentricCoordinateSystem(source.getHorizontalDatum().getName() + " Geocentric", source.getHorizontalDatum(), LinearUnit.getMetre(), source.getPrimeMeridian());
            IGeocentricCoordinateSystem targetCentric = cFac.createGeocentricCoordinateSystem(target.getHorizontalDatum().getName() + " Geocentric", target.getHorizontalDatum(), LinearUnit.getMetre(), source.getPrimeMeridian());
            ConcatenatedTransform ct = new ConcatenatedTransform();
            addIfNotNull(ct, ctFac.createFromCoordinateSystems(source, sourceCentric));
            addIfNotNull(ct, ctFac.createFromCoordinateSystems(sourceCentric, targetCentric));
            addIfNotNull(ct, ctFac.createFromCoordinateSystems(targetCentric, target));
            return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
        }
    }

    private static void addIfNotNull(ConcatenatedTransform concatTrans, ICoordinateTransformation trans) throws Exception {
        if (trans != null)
            concatTrans.getCoordinateTransformationList().add(trans);

    }

    /**
     * Geocentric to Geocentric transformation
     *
     * @param source
     * @param target
     * @return
     */
    private static ICoordinateTransformation createGeoc2Geoc(IGeocentricCoordinateSystem source, IGeocentricCoordinateSystem target) throws Exception {
        ConcatenatedTransform ct = new ConcatenatedTransform();
        //Does source has a datum different from WGS84 and is there a shift specified?
        if (source.getHorizontalDatum().getWgs84Parameters() != null && !source.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly())
            ct.getCoordinateTransformationList().add(new CoordinateTransformation(((target.getHorizontalDatum().getWgs84Parameters() == null || target.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly()) ? target : GeocentricCoordinateSystem.getWGS84()), source, TransformType.Transformation, new DatumTransform(source.getHorizontalDatum().getWgs84Parameters()), "", "", -1, "", ""));

        //Does target has a datum different from WGS84 and is there a shift specified?
        if (target.getHorizontalDatum().getWgs84Parameters() != null && !target.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly())
            ct.getCoordinateTransformationList().add(new CoordinateTransformation(((source.getHorizontalDatum().getWgs84Parameters() == null || source.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly()) ? source : GeocentricCoordinateSystem.getWGS84()), target, TransformType.Transformation, (new DatumTransform(target.getHorizontalDatum().getWgs84Parameters())).inverse(), "", "", -1, "", ""));

        //If we don't have a transformation in this list, return null
        if (ct.getCoordinateTransformationList().size() == 0)
            return null;

        //If we only have one shift, lets just return the datumshift from/to wgs84
        if (ct.getCoordinateTransformationList().size() == 1)
            return new CoordinateTransformation(source, target, TransformType.ConversionAndTransformation, ct.getCoordinateTransformationList().get(0).getMathTransform(), "", "", -1, "", "");

        return new CoordinateTransformation(source, target, TransformType.ConversionAndTransformation, ct, "", "", -1, "", "");
    }

    /**
     * Creates transformation from fitted coordinate system to the target one
     *
     * @param source
     * @param target
     * @return
     */
    private static ICoordinateTransformation fitt2Any(IFittedCoordinateSystem source, ICoordinateSystem target) throws Exception {
        //transform from fitted to base system of fitted (which is equal to target)
        IMathTransform mt = createFittedTransform(source);
        //case when target system is equal to base system of the fitted
        if (source.getBaseCoordinateSystem().equalParams(target)) {
            return createTransform(source, target, TransformType.Transformation, mt);
        }

        //Transform form base system of fitted to target coordinate system
        //Transform form base system of fitted to target coordinate system
        ConcatenatedTransform ct = new ConcatenatedTransform();
        ct.getCoordinateTransformationList().add(createTransform(source, source.getBaseCoordinateSystem(), TransformType.Transformation, mt));
        //Transform form base system of fitted to target coordinate system
        CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
        ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source.getBaseCoordinateSystem(), target));
        return createTransform(source, target, TransformType.Transformation, ct);
    }

    /**
     * Creates transformation from source coordinate system to specified target system which is the fitted one
     *
     * @param source
     * @param target
     * @return
     */
    private static ICoordinateTransformation any2Fitt(ICoordinateSystem source, IFittedCoordinateSystem target) throws Exception {
        //Transform form base system of fitted to target coordinate system - use invered math transform
        IMathTransform invMt = createFittedTransform(target).inverse();
        //case when source system is equal to base system of the fitted
        if (target.getBaseCoordinateSystem().equalParams(source)) {
            return createTransform(source, target, TransformType.Transformation, invMt);
        }

        //Transform form base system of fitted to target coordinate system
        ConcatenatedTransform ct = new ConcatenatedTransform();
        //First transform from source to base system of fitted
        CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
        ct.getCoordinateTransformationList().add(ctFac.createFromCoordinateSystems(source, target.getBaseCoordinateSystem()));
        //Transform form base system of fitted to target coordinate system - use invered math transform
        ct.getCoordinateTransformationList().add(createTransform(target.getBaseCoordinateSystem(), target, TransformType.Transformation, invMt));
        return createTransform(source, target, TransformType.Transformation, ct);
    }

    private static IMathTransform createFittedTransform(IFittedCoordinateSystem fittedSystem) throws Exception {
        //create transform From fitted to base and inverts it
        if (fittedSystem instanceof FittedCoordinateSystem) {
            return ((FittedCoordinateSystem) fittedSystem).getToBaseTransform();
        }

        throw new UnsupportedOperationException();
    }

    /**
     * Creates an instance of CoordinateTransformation as an anonymous transformation without neither autohority nor code defined.
     *
     * @param sourceCS      Source coordinate system
     * @param targetCS      Target coordinate system
     * @param transformType Transformation type
     * @param mathTransform Math transform
     */
    private static CoordinateTransformation createTransform(ICoordinateSystem sourceCS, ICoordinateSystem targetCS, TransformType transformType, IMathTransform mathTransform) throws Exception {
        return new CoordinateTransformation(sourceCS, targetCS, transformType, mathTransform, "", "", -1, "", "");
    }

    //MathTransformFactory mtFac = new MathTransformFactory ();

    /**
     * /create transform From fitted to base and inverts it
     */
    //return mtFac.CreateFromWKT (fittedSystem.ToBase ());
    private static IMathTransform createCoordinateOperation(IGeocentricCoordinateSystem geo) throws Exception {
        List<ProjectionParameter> parameterList = new ArrayList<ProjectionParameter>(2);
        IEllipsoid ellipsoid = geo.getHorizontalDatum().getEllipsoid();
        //var toMeter = ellipsoid.AxisUnit.MetersPerUnit;

        /*
             if (parameterList.Find((p) => p.Name.ToLowerInvariant().Replace(' ', '_').Equals("semi_major")) == null)
                parameterList.Add(new ProjectionParameter("semi_major", ellipsoid.SemiMajorAxis));
        if (parameterList.Find((p) => p.Name.ToLowerInvariant().Replace(' ', '_').Equals("semi_minor")) == null)
        parameterList.Add(new ProjectionParameter("semi_minor", ellipsoid.SemiMinorAxis));
         */

        boolean found = false;
        for (ProjectionParameter p : parameterList) {
            if (p.getName().toLowerCase().replace(' ', '_').equals("semi_major")) {
                found = true;
                break;
            }
        }
        if (!found) {
            parameterList.add(new ProjectionParameter("semi_major", ellipsoid.getSemiMajorAxis()));
        }


        found = false;
        for (ProjectionParameter p : parameterList) {
            if (p.getName().toLowerCase().replace(' ', '_').equals("semi_minor")) {
                found = true;
                break;
            }
        }
        if (!found) {
            parameterList.add(new ProjectionParameter("semi_minor", ellipsoid.getSemiMinorAxis()));
        }


        return new GeocentricTransform(parameterList);
    }

    private static IMathTransform createCoordinateOperation(IProjection projection, IEllipsoid ellipsoid, ILinearUnit unit) throws Exception {
        List<ProjectionParameter> parameterList = new ArrayList<ProjectionParameter>(projection.getNumParameters());


        for (int i = 0; i < projection.getNumParameters(); i++)
            parameterList.add(projection.getParameter(i));


        boolean found = false;
        for (ProjectionParameter p : parameterList) {
            if (p.getName().toLowerCase().replace(' ', '_').equals("semi_major")) {
                found = true;
                break;
            }
        }
        if (!found) {
            parameterList.add(new ProjectionParameter("semi_major", ellipsoid.getSemiMajorAxis()));
        }


        found = false;
        for (ProjectionParameter p : parameterList) {
            if (p.getName().toLowerCase().replace(' ', '_').equals("semi_minor")) {
                found = true;
                break;
            }
        }
        if (!found) {
            parameterList.add(new ProjectionParameter("semi_minor", ellipsoid.getSemiMinorAxis()));
        }


        found = false;
        for (ProjectionParameter p : parameterList) {
            if (p.getName().toLowerCase().replace(' ', '_').equals("unit")) {
                found = true;
                break;
            }
        }
        if (!found) {
            parameterList.add(new ProjectionParameter("unit", unit.getMetersPerUnit()));
        }

        IMathTransform transform = null;
        switch (projection.getClassName().toLowerCase().replace(' ', '_')) {
            case "mercator":
            case "mercator_1sp":
            case "mercator_2sp":
                //1SP
                transform = new Mercator(parameterList);
                break;
            case "pseudo-mercator":
            case "popular_visualisation pseudo-mercator":
            case "google_mercator":
                transform = new PseudoMercator(parameterList);
                break;
            case "transverse_mercator":
                transform = new TransverseMercator(parameterList);
                break;
            case "albers":
            case "albers_conic_equal_area":
                transform = new AlbersProjection(parameterList);
                break;
            case "krovak":
                transform = new KrovakProjection(parameterList);
                break;
            case "polyconic":
                transform = new PolyconicProjection(parameterList);
                break;
            case "lambert_conformal_conic":
            case "lambert_conformal_conic_2sp":
            case "lambert_conic_conformal_(2sp)":
                transform = new LambertConformalConic2SP(parameterList);
                break;
            case "cassini_soldner":
                transform = new CassiniSoldnerProjection(parameterList);
                break;
            case "hotine_oblique_mercator":
                transform = new HotineObliqueMercatorProjection(parameterList);
                break;
            case "oblique_mercator":
                transform = new ObliqueMercatorProjection(parameterList);
                break;
            case "oblique_stereographic":
                transform = new ObliqueStereographicProjection(parameterList);
                break;
            default:
                throw new Exception(String.format("Projection %s is not supported.", projection.getClassName()));
        }

        return transform;
    }

    /**
     * Creates a transformation between two coordinate systems.
     *
     * This method will examine the coordinate systems in order to construct
     * a transformation between them. This method may fail if no path between
     * the coordinate systems is found, using the normal failing behavior of
     * the DCP (e.g. throwing an exception).
     *
     * @param sourceCS Source coordinate system
     * @param targetCS Target coordinate system
     * @return
     */
    public ICoordinateTransformation createFromCoordinateSystems(ICoordinateSystem sourceCS, ICoordinateSystem targetCS) throws Exception {
        ICoordinateTransformation trans;
        if (sourceCS instanceof IProjectedCoordinateSystem && targetCS instanceof IGeographicCoordinateSystem)
            //Projected -> Geographic
            trans = proj2Geog((IProjectedCoordinateSystem) sourceCS, (IGeographicCoordinateSystem) targetCS);
        else if (sourceCS instanceof IGeographicCoordinateSystem && targetCS instanceof IProjectedCoordinateSystem)
            //Geographic -> Projected
            trans = geog2Proj((IGeographicCoordinateSystem) sourceCS, (IProjectedCoordinateSystem) targetCS);
        else if (sourceCS instanceof IGeographicCoordinateSystem && targetCS instanceof IGeocentricCoordinateSystem)
            //Geocentric -> Geographic
            trans = geog2Geoc((IGeographicCoordinateSystem) sourceCS, (IGeocentricCoordinateSystem) targetCS);
        else if (sourceCS instanceof IGeocentricCoordinateSystem && targetCS instanceof IGeographicCoordinateSystem)
            //Geocentric -> Geographic
            trans = geoc2Geog((IGeocentricCoordinateSystem) sourceCS, (IGeographicCoordinateSystem) targetCS);
        else if (sourceCS instanceof IProjectedCoordinateSystem && targetCS instanceof IProjectedCoordinateSystem)
            //Projected -> Projected
            trans = proj2Proj((IProjectedCoordinateSystem) sourceCS, (IProjectedCoordinateSystem) targetCS);
        else if (sourceCS instanceof IGeocentricCoordinateSystem && targetCS instanceof IGeocentricCoordinateSystem)
            //Geocentric -> Geocentric
            trans = createGeoc2Geoc((IGeocentricCoordinateSystem) sourceCS, (IGeocentricCoordinateSystem) targetCS);
        else if (sourceCS instanceof IGeographicCoordinateSystem && targetCS instanceof IGeographicCoordinateSystem)
            //Geographic -> Geographic
            trans = createGeog2Geog((IGeographicCoordinateSystem) sourceCS, (IGeographicCoordinateSystem) targetCS);
        else if (sourceCS instanceof IFittedCoordinateSystem)
            //Fitted -> Any
            trans = fitt2Any((IFittedCoordinateSystem) sourceCS, targetCS);
        else if (targetCS instanceof IFittedCoordinateSystem)
            //Any -> Fitted
            trans = any2Fitt(sourceCS, (IFittedCoordinateSystem) targetCS);
        else
            throw new Exception("No support for transforming between the two specified coordinate systems");
        return trans;
    }

}



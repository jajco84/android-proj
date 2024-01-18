package com.asseco.android.proj;

import com.asseco.android.proj.transformations.IMathTransform;

import java.util.ArrayList;

/**
 * A coordinate system which sits inside another coordinate system. The fitted
 * coordinate system can be rotated and shifted, or use any other math transform
 * to inject itself into the base coordinate system.
 */
public class FittedCoordinateSystem extends CoordinateSystem implements IFittedCoordinateSystem {
    private IMathTransform _ToBaseTransform;
    private ICoordinateSystem _BaseCoordinateSystem;

    /**
     * Creates an instance of FittedCoordinateSystem using the specified parameters
     *
     * @param baseSystem   Underlying coordinate system.
     * @param transform    Transformation from fitted coordinate system to the base one
     * @param name         Name
     * @param authority    Authority name
     * @param code         Authority-specific identification code.
     * @param alias        Alias
     * @param remarks      Provider-supplied remarks
     * @param abbreviation Abbreviation
     * @throws Exception the exception
     */
    public FittedCoordinateSystem(ICoordinateSystem baseSystem, IMathTransform transform, String name, String authority, long code, String alias, String remarks, String abbreviation) throws Exception {
        super(name, authority, code, alias, abbreviation, remarks);
        _BaseCoordinateSystem = baseSystem;
        _ToBaseTransform = transform;
        //get axis infos from the source
        super.setAxisInfo(new ArrayList<AxisInfo>(baseSystem.getDimension()));
        for (int dim = 0; dim < baseSystem.getDimension(); dim++) {
            super.getAxisInfo().add(baseSystem.getAxis(dim));
        }
    }

    /**
     * Represents math transform that injects itself into the base coordinate system.
     *
     * @return the to base transform
     * @throws Exception the exception
     */
    public IMathTransform getToBaseTransform() throws Exception {
        return _ToBaseTransform;
    }

    /**
     * Gets underlying coordinate system.
     */
    public ICoordinateSystem getBaseCoordinateSystem() throws Exception {
        return _BaseCoordinateSystem;
    }

    /**
     * Gets Well-Known Text of a math transform to the base coordinate system.
     * The dimension of this fitted coordinate system is determined by the source
     * dimension of the math transform. The transform should be one-to-one within
     * this coordinate system's domain, and the base coordinate system dimension
     * must be at least as big as the dimension of this coordinate system.
     *
     * @return
     */
    public String toBase() throws Exception {
        return _ToBaseTransform.getWKT();
    }

    /**
     * Returns the Well-known text for this object as defined in the simple features specification.
     */
    public String getWKT() throws Exception {
        //<fitted cs>          = FITTED_CS["<name>", <to base>, <base cs>]
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("FITTED_CS[\"%s\", %s, %s]", getName(), this._ToBaseTransform.getWKT(), this._BaseCoordinateSystem.getWKT()));
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object.
     */
    public String getXML() throws Exception {
        throw new UnsupportedOperationException();
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
        IFittedCoordinateSystem fcs = obj instanceof IFittedCoordinateSystem ? (IFittedCoordinateSystem) obj : (IFittedCoordinateSystem) null;
        if (fcs != null) {
            if (fcs.getBaseCoordinateSystem().equalParams(this.getBaseCoordinateSystem())) {
                String fcsToBase = fcs.toBase();
                String thisToBase = this.toBase();
                if (fcsToBase.equals(thisToBase)) {
                    return true;
                }

            }

        }

        return false;
    }

    /**
     * Gets the units for the dimension within coordinate system.
     * Each dimension in the coordinate system has corresponding units.
     */
    public IUnit getUnits(int dimension) throws Exception {
        return _BaseCoordinateSystem.getUnits(dimension);
    }

}



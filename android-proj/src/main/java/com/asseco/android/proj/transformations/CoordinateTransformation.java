package com.asseco.android.proj.transformations;

import com.asseco.android.proj.ICoordinateSystem;

/**
 * Describes a coordinate transformation. This class only describes a
 * coordinate transformation, it does not actually perform the transform
 * operation on points. To transform points you must use a MathTransform
 * .
 */
public class CoordinateTransformation implements ICoordinateTransformation {
    private String _AreaOfUse = new String();
    private String _Authority = new String();
    private long _AuthorityCode;
    private IMathTransform _MathTransform;
    private String _Name = new String();
    private String _Remarks = new String();
    private ICoordinateSystem _SourceCS;
    private ICoordinateSystem _TargetCS;
    private TransformType _TransformType;

    /**
     * Initializes an instance of a CoordinateTransformation
     *
     * @param sourceCS      Source coordinate system
     * @param targetCS      Target coordinate system
     * @param transformType Transformation type
     * @param mathTransform Math transform
     * @param name          Name of transform
     * @param authority     Authority
     * @param authorityCode Authority code
     * @param areaOfUse     Area of use
     * @param remarks       Remarks
     * @throws Exception the exception
     */
    public CoordinateTransformation(ICoordinateSystem sourceCS, ICoordinateSystem targetCS, TransformType transformType, IMathTransform mathTransform, String name, String authority, long authorityCode, String areaOfUse, String remarks) throws Exception {
        super();
        _TargetCS = targetCS;
        _SourceCS = sourceCS;
        _TransformType = transformType;
        _MathTransform = mathTransform;
        _Name = name;
        _Authority = authority;
        _AuthorityCode = authorityCode;
        _AreaOfUse = areaOfUse;
        _Remarks = remarks;
    }

    /**
     * Human readable description of domain in source coordinate system.
     */
    public String getAreaOfUse() throws Exception {
        return _AreaOfUse;
    }

    /**
     * Authority which defined transformation and parameter values.
     *
     * An Authority is an organization that maintains definitions of Authority Codes. For example the European Petroleum Survey Group (EPSG) maintains a database of coordinate systems, and other spatial referencing objects, where each object has a code number ID. For example, the EPSG code for a WGS84 Lat/Lon coordinate system is 4326
     */
    public String getAuthority() throws Exception {
        return _Authority;
    }

    /**
     * Code used by authority to identify transformation. An empty string is used for no code.
     * The AuthorityCode is a compact string defined by an Authority to reference a particular spatial reference object. For example, the European Survey Group (EPSG) authority uses 32 bit integers to reference coordinate systems, so all their code strings will consist of a few digits. The EPSG code for WGS84 Lat/Lon is 4326.
     */
    public long getAuthorityCode() throws Exception {
        return _AuthorityCode;
    }

    /**
     * Gets math transform.
     */
    public IMathTransform getMathTransform() throws Exception {
        return _MathTransform;
    }

    /**
     * Name of transformation.
     */
    public String getName() throws Exception {
        return _Name;
    }

    /**
     * Gets the provider-supplied remarks.
     */
    public String getRemarks() throws Exception {
        return _Remarks;
    }

    /**
     * Source coordinate system.
     */
    public ICoordinateSystem getSourceCS() throws Exception {
        return _SourceCS;
    }

    /**
     * Target coordinate system.
     */
    public ICoordinateSystem getTargetCS() throws Exception {
        return _TargetCS;
    }

    /**
     * Semantic type of transform. For example, a datum transformation or a coordinate conversion.
     */
    public TransformType getTransformType() throws Exception {
        return _TransformType;
    }

}



package com.asseco.android.proj.projections;



import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.transformations.IMathTransform;

import java.util.List;

/**
 * The type Oblique mercator projection.
 */
public class ObliqueMercatorProjection extends HotineObliqueMercatorProjection {
    /**
     * Instantiates a new Oblique mercator projection.
     *
     * @param parameters the parameters
     * @throws Exception the exception
     */
    public ObliqueMercatorProjection(List<ProjectionParameter> parameters) throws Exception {
        this(parameters, null);
    }

    /**
     * Instantiates a new Oblique mercator projection.
     *
     * @param parameters the parameters
     * @param inverse    the inverse
     * @throws Exception the exception
     */
    public ObliqueMercatorProjection(List<ProjectionParameter> parameters, ObliqueMercatorProjection inverse) throws Exception {
        super(parameters, inverse);
        setAuthorityCode(9815);
        setName("Oblique_Mercator");
    }

    public IMathTransform inverse() throws Exception {
        if (_inverse == null)
            _inverse = new ObliqueMercatorProjection(_Parameters.toProjectionParameter(), this);

        return _inverse;
    }

}



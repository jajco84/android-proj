package com.asseco.android.proj.projections;


import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.transformations.IMathTransform;

import java.util.List;

/**
 * The type Pseudo mercator.
 */
public class PseudoMercator extends Mercator {
    /**
     * Instantiates a new Pseudo mercator.
     *
     * @param parameters the parameters
     * @throws Exception the exception
     */
    public PseudoMercator(List<ProjectionParameter> parameters) throws Exception {
        this(parameters, null);
    }

    /**
     * Instantiates a new Pseudo mercator.
     *
     * @param parameters the parameters
     * @param inverse    the inverse
     * @throws Exception the exception
     */
    protected PseudoMercator(List<ProjectionParameter> parameters, Mercator inverse) throws Exception {
        super(verifyParameters(parameters), inverse);
        setName("Pseudo-Mercator");
        setAuthority("EPSG");
        setAuthorityCode(3856);
    }

    private static List<ProjectionParameter> verifyParameters(List<ProjectionParameter> parameters) throws Exception {
        ProjectionParameterSet p = new ProjectionParameterSet(parameters);
        double semi_major = p.getParameterValue("semi_major");
        p.setParameterValue("semi_minor", semi_major);
        p.setParameterValue("scale_factor", 1);
        return p.toProjectionParameter();
    }

    public IMathTransform inverse() throws Exception {
        if (_inverse == null)
            _inverse = new PseudoMercator(_Parameters.toProjectionParameter(), this);

        return _inverse;
    }

}



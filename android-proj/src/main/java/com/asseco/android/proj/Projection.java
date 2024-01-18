package com.asseco.android.proj;

import java.util.ArrayList;
import java.util.List;


/**
 * The Projection class defines the standard information stored with a projection
 * objects. A projection object implements a coordinate transformation from a geographic
 * coordinate system to a projected coordinate system, given the ellipsoid for the
 * geographic coordinate system. It is expected that each coordinate transformation of
 * interest, e.g., Transverse Mercator, Lambert, will be implemented as a class of
 * type Projection, supporting the IProjection interface.
 */
public class Projection extends Info implements IProjection {
    private List<ProjectionParameter> _parameters = new ArrayList<ProjectionParameter>();
    private String _ClassName = new String();

    /**
     * Instantiates a new Projection.
     *
     * @param className    the class name
     * @param parameters   the parameters
     * @param name         the name
     * @param authority    the authority
     * @param code         the code
     * @param alias        the alias
     * @param remarks      the remarks
     * @param abbreviation the abbreviation
     * @throws Exception the exception
     */
    public Projection(String className, List<ProjectionParameter> parameters, String name, String authority, long code, String alias, String remarks, String abbreviation) throws Exception {
        super(name, authority, code, alias, abbreviation, remarks);
        _parameters = parameters;
        _ClassName = className;
    }

    /**
     * Gets the number of parameters of the projection.
     */
    public int getNumParameters() throws Exception {
        return _parameters.size();
    }

    /**
     * Gets or sets the parameters of the projection
     *
     * @return the parameters
     * @throws Exception the exception
     */
    public List<ProjectionParameter> getParameters() throws Exception {
        return _parameters;
    }

    /**
     * Sets parameters.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setParameters(List<ProjectionParameter> value) throws Exception {
        _parameters = value;
    }

    /**
     * Gets an indexed parameter of the projection.
     *
     * @param index Index of parameter
     * @return n'th parameter
     */
    public ProjectionParameter getParameter(int index) throws Exception {
        return _parameters.get(index);
    }

    /**
     * Gets an named parameter of the projection.
     * The parameter name is case insensitive
     *
     * @param name Name of parameter
     * @return parameter or null if not found
     */
    public ProjectionParameter getParameter(String name) throws Exception {
        for (Object __dummyForeachVar0 : _parameters) {
            ProjectionParameter par = (ProjectionParameter) __dummyForeachVar0;
            if (par.getName().toLowerCase().equals(name.toLowerCase()))
                return par;

        }
        return null;
    }

    /**
     * Gets the projection classification name (e.g. "Transverse_Mercator").
     */
    public String getClassName() throws Exception {
        return _ClassName;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public String getWKT() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("PROJECTION[\"%s\"", getName()));
        if (getAuthority() != null && getAuthority() != "" && getAuthorityCode() > 0)
            sb.append(String.format(", AUTHORITY[\"%s\", \"%d\"]", getAuthority(), getAuthorityCode()));

        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object
     */
    public String getXML() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<CS_Projection Classname=\"%s\">%s", getClassName(), getInfoXml()));
        for (Object __dummyForeachVar1 : getParameters()) {
            ProjectionParameter param = (ProjectionParameter) __dummyForeachVar1;
            sb.append(param.getXML());
        }
        sb.append("</CS_Projection>");
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
        if (!(obj instanceof Projection))
            return false;

        Projection proj = obj instanceof Projection ? (Projection) obj : null;

        if (proj != null) {
            if (proj.getNumParameters() != this.getNumParameters())
                return false;

            for (int i = 0; i < _parameters.size(); i++) {
                ProjectionParameter param = getParameter(proj.getParameter(i).getName());
                if (param == null)
                    return false;

                if (param.getValue() != proj.getParameter(i).getValue())
                    return false;

            }
            return true;
        }
        return false;
    }

}



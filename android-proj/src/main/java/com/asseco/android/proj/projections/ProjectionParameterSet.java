package com.asseco.android.proj.projections;

import com.asseco.android.proj.ProjectionParameter;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;


/**
 * A set of projection parameters
 */
public class ProjectionParameterSet // extends Dictionary<String, double>
{
    private Dictionary<String, String> _originalNames = new Hashtable<>();
    private Dictionary<Integer, String> _originalIndex = new Hashtable<>();
    private Dictionary<String, Double> vals = new Hashtable<>();
    /**
     * Needed for serialzation
     */
  /*  public ProjectionParameterSet(SerializationInfo info, StreamingContext context) throws Exception {
        super(info, context);
    }*/

    /**
     * Creates an instance of this class
     *
     * @param parameters An enumeration of paramters
     * @throws Exception the exception
     */
    public ProjectionParameterSet(List<ProjectionParameter> parameters) throws Exception {
        for (ProjectionParameter pp : parameters) {
            String key = pp.getName().toLowerCase();
            _originalNames.put(key, pp.getName());
            _originalIndex.put(_originalIndex.size(), key);
            vals.put(key, pp.getValue());
        }
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return vals.size();
    }

    /**
     * Function to create an enumeration of
     * ProjectionParameter
     * s of the content of this projection parameter set.
     *
     * @return An enumeration of ProjectionParameter s
     * @throws Exception the exception
     */
    public List<ProjectionParameter> toProjectionParameter() throws Exception {
        //    ProjectionParameter pp = new ProjectionParameter();
        // for (int oi : _originalIndex.){

        //}
        ArrayList<ProjectionParameter> ret = new ArrayList<>();
        Enumeration<Integer> en = _originalIndex.keys();
        while (en.hasMoreElements()) {
            Integer key = en.nextElement();
            ret.add(new ProjectionParameter(_originalNames.get(_originalIndex.get(key)), vals.get(_originalIndex.get(key))));
        }
        return ret;
    }

    /**
     * Function to get the value of a mandatory projection parameter
     *
     * @param parameterName  or any of                      {@code alternateNames}                      is not defined.
     * @param alternateNames the alternate names
     * @return The value of the parameter
     * @throws Exception the exception
     */
    public double getParameterValue(String parameterName, String... alternateNames) throws Exception {
        String name = parameterName.toLowerCase();
        if (vals.get(name) == null) {
            for (String alternateName : alternateNames) {

                if (vals.get(alternateName.toLowerCase()) != null) {
                    return vals.get(alternateName.toLowerCase());
                }
            }
            /* [UNSUPPORTED] 'var' as type is unsupported "var"  sb = new StringBuilder();
           /* sb.AppendFormat("Missing projection parameter '{0}'", parameterName);
            if (alternateNames.Length > 0)
            {
                sb.AppendFormat("\nIt is also not defined as '{0}'", alternateNames[0]);
                for (/* [UNSUPPORTED] 'var' as type is unsupported "var"  i = 1;i < alternateNames.Length;i++)
                    sb.AppendFormat(", '{0}'", alternateNames[i]);
                sb.Append(".");
            }*/

            throw new IllegalArgumentException("parameterName");
        }

        return this.vals.get(name);
    }

    /**
     * Method to check if all mandatory projection parameters are passed
     *
     * @param name           the name
     * @param value          the value
     * @param alternateNames the alternate names
     * @return the optional parameter value
     * @throws Exception the exception
     */
    public double getOptionalParameterValue(String name, double value, String... alternateNames) throws Exception {
        name = name.toLowerCase();
        if (vals.get(name) == null) {
            for (String alternateName : alternateNames) {
                if (vals.get(alternateName.toLowerCase()) != null) {
                    return vals.get(alternateName.toLowerCase());
                }
            }
            return value;
        }

        return vals.get(name);
    }

    //Add(name, value);

    /**
     * Function to find a parameter based on its name
     *
     * @param name The name of the parameter
     * @return The parameter if present, otherwise null
     * @throws Exception the exception
     */
    public ProjectionParameter find(String name) throws Exception {
        String nm = name.toLowerCase();
        return vals.get(nm) != null ? new ProjectionParameter(_originalNames.get(nm), this.vals.get(nm)) : null;
    }

    /**
     * Function to get the parameter at the given index
     *
     * @param index The index
     * @return The parameter
     * @throws Exception the exception
     */
    public ProjectionParameter getAtIndex(int index) throws Exception {
        if (index < 0 || index >= vals.size())
            throw new IllegalArgumentException("index");

        String name = _originalIndex.get(index);
        return new ProjectionParameter(_originalNames.get(name), this.vals.get(name));
    }

    /**
     * Equals boolean.
     *
     * @param other the other
     * @return the boolean
     */
    public boolean equals(ProjectionParameterSet other) {
        try {
            if (other == null)
                return false;

            if (other.vals.size() != vals.size())
                return false;


            // for (String key : this.vals.keys())
            Enumeration<String> en = this.vals.keys();
            while (en.hasMoreElements()) {
                String key = en.nextElement();
                if (other.vals.get(key) == null)
                    return false;

                double otherValue = other.getParameterValue(key);
                if (otherValue != vals.get(key))
                    return false;

            }
            return true;
        } catch (RuntimeException __dummyCatchVar0) {
            throw __dummyCatchVar0;
        } catch (Exception __dummyCatchVar0) {
            throw new RuntimeException(__dummyCatchVar0);
        }

    }

    /**
     * Sets parameter value.
     *
     * @param name  the name
     * @param value the value
     * @throws Exception the exception
     */
    public void setParameterValue(String name, double value) throws Exception {
        String key = name.toLowerCase();

        if (vals.get(key) == null) {
            _originalIndex.put(_originalIndex.size(), key);
            _originalNames.put(key, name);
            vals.put(key, value);
        } else {
            vals.remove(key);
            vals.put(key, value);
        }
    }

}



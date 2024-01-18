package com.asseco.android.proj;

/**
 * A named parameter value.
 */
public class Parameter {
    private String _Name = new String();
    private double _Value;

    /**
     * Creates an instance of a parameter
     * Units are always either meters or degrees.
     *
     * @param name  Name of parameter
     * @param value Value
     * @throws Exception the exception
     */
    public Parameter(String name, double value) throws Exception {
        _Name = name;
        _Value = value;
    }

    /**
     * Parameter name
     *
     * @return the name
     * @throws Exception the exception
     */
    public String getName() throws Exception {
        return _Name;
    }

    /**
     * Sets name.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setName(String value) throws Exception {
        _Name = value;
    }

    /**
     * Parameter value
     *
     * @return the value
     * @throws Exception the exception
     */
    public double getValue() throws Exception {
        return _Value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setValue(double value) throws Exception {
        _Value = value;
    }

}



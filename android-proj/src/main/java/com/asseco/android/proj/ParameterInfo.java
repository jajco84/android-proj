package com.asseco.android.proj;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple class that implements the IParameterInfo interface for providing general set of the parameters.
 * It allows discovering the names, and for setting and getting parameter values.
 */
public class ParameterInfo implements IParameterInfo {
    /**
     * Gets or sets the parameters set for this projection.
     */
    private List<Parameter> __Parameters = new ArrayList<Parameter>();

    /**
     * Gets the number of parameters expected.
     */
    public int getNumParameters() throws Exception {
        if (this.getParameters() != null) {
            return this.getParameters().size();
        }

        return 0;
    }

    public List<Parameter> getParameters() {
        return __Parameters;
    }

    public void setParameters(List<Parameter> value) {
        __Parameters = value;
    }

    /**
     * Returns the default parameters for this projection.
     *
     * @return
     */
    public Parameter[] defaultParameters() throws Exception {
        return new Parameter[0];
    }

    /**
     * Gets the parameter by its name
     *
     * @param name
     * @return
     */
    public Parameter getParameterByName(String name) throws Exception {
        if (this.getParameters() != null) {
            for (Object __dummyForeachVar0 : this.getParameters()) {
                //search parameter collection by name
                Parameter param = (Parameter) __dummyForeachVar0;
                if (param != null && param.getName().equals(name)) {
                    return param;
                }

            }
        }

        return null;
    }

}



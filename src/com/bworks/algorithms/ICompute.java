package com.bworks.algorithms;

import java.util.Map;

import com.bworks.util.ConfigurationManager;

public interface ICompute {

    /**
     * @Description This method shall be used to initialize the Algorithm class. 
     *              If Algorithm class required any configuration from the Audiohub.config file, 
     *              it shall be available during initialization.
     *              'initialize' method shall be invoked by the Algorithm frame work.  
     * @param appConfig
     * @throws Exception
     */
    void initialize(ConfigurationManager appConfig) throws Exception;
    
    /**
     * @Description This method shall be invoked by the algorithm framework to collect computation result.
     * @param computationType - computation type mentioned in configuration file.
     * @param inputData - This is a list of audiogram data as key value pair. 
     *                    List shall be prepared based on the configuration input provided in config file.
     * @return computation result.
     */
    String getComputedResult(String computationType, Map<String, Integer> inputData);
}

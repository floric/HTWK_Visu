package org.htwkvisu.scoring;

import java.util.HashMap;

/*
* Interface for all POIs which have an influence for the scoring.
 */
public interface IScorable {
    HashMap<String, IFallOf> getValues();
}
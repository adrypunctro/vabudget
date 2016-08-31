/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Adrian Simionescu
 */
public class Distribution {
    
    final private int id;
    final private int ownerId;
    final private String label;
    final private Map<Integer, Integer> ratio = new HashMap<>();
    
    public Distribution(int distribId, int ownerId, String label, Map<Integer, Integer> ratio) {
        id = distribId;
        this.ownerId = ownerId;
        this.label = label;
        this.ratio.putAll(ratio);
    }
    
    public int getId() {
        return id;
    }
    
    public int getOwnerId() {
        return ownerId;
    }
    
    public String getLabel() {
        return label;
    }
    
    public Map<Integer, Integer> getRatio() {
        return ratio;
    }
}

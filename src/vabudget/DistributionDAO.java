/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Adrian Simionescu
 */
public interface DistributionDAO {
    public int insertDistribution(int ownerId, String label, Map<Integer, Integer> ratio);
    public boolean deleteDistribution(int distribId);
    public Distribution findDistribution(int distribId);
    public boolean updateDistribution(int distribId);
    public List<Distribution> selectDistributions(int personId);
}

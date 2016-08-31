/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import java.util.List;

/**
 *
 * @author Adrian Simionescu
 */
public interface ShareDAO {
    public boolean insertShare(int cardId, int personId);
    public boolean deleteShare(int cardId, int personId);
    public Share findShare(int cardId, int personId);
    public boolean acceptShare(int cardId, int personId);
    public boolean rejectShare(int cardId, int personId);
    public List<Share> selectShared(int cardId);
}

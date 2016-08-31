/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Adrian Simionescu
 */
public interface CardDAO {
    public int insertCard(int ownerId, String label, BigDecimal initAmount);
    public boolean deleteCard(int cardId);
    public Card findCard(int cardId);
    public boolean updateCardLabel(int cardId, String label);
    public List<Card> selectCards(int ownerId);
}

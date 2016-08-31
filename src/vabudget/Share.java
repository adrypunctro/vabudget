/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

/**
 *
 * @author Adrian Simionescu
 */
public class Share {
    private final int cardId;
    private final int personId;
    private final String status;
    
    public Share(int cardId, int personId, String status) {
        this.cardId = cardId;
        this.personId = personId;
        this.status = status;
    }
    
    public int getCardId() {
        return cardId;
    }
    
    public int getPersonId() {
        return personId;
    }
    
    public String getStatus() {
        return status;
    }
}

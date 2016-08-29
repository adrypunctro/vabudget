package vabudget;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
/**
 *
 * @author Adrian Simionescu
 */
public interface Card {
    
    /**
     *
     * @return
     */
    public int getId();
    
    /**
     *
     * @return
     */
    public String getLabel();

    /**
     *
     * @return
     */
    public BigDecimal getAmount();
    
    
}

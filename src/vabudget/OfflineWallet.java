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
public class OfflineWallet extends Wallet
{
    public OfflineWallet(LocalConfig config)
    {
        model = DAOFactory.getDAOFactory(WalletConnection.MYSQL, config);
    }
    
    @Override
    public boolean connected()
    {
        if (model == null)
        {
            return false;
        }
        
        return model.connected();
    }
}

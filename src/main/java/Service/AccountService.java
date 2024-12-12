package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account){
        return accountDAO.insertAccount(account);
    }

    public Account login(Account account){
        return accountDAO.login(account);
    }

    public Account getAccountByID(int id){
        return accountDAO.getAccountByID(id);
    }


}

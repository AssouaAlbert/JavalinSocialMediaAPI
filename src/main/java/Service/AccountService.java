package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account loginUser(Account user) {
        return accountDAO.loginUser(user);
    }
    public Account registerUser(Account user) {
        return accountDAO.registerUser(user);
    }
}

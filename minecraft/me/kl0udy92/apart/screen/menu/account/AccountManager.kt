package me.kl0udy92.apart.screen.menu.account

import me.kl0udy92.apart.config.implementations.AccountsConfig
import me.kl0udy92.apart.core.Main

class AccountManager {

    val accounts = mutableListOf<Account>()
    var lastAccount: Account? = null

    init {
        this.accounts.clear()
    }

    fun addAccount(account: Account) {
        this.accounts.add(account)
        Main.configManager.write(AccountsConfig())
    }

    fun addAccount(name: String, password: String) {
        this.accounts.add(Account(name, password))
        Main.configManager.write(AccountsConfig())
    }

    fun removeAccount(name: String) {
        this.accounts.remove(this.findAccount(name))
        Main.configManager.write(AccountsConfig())
    }

    fun findAccount(name: String): Account? {
        return this.accounts.stream().filter { it.name.equals(name, true) }.findFirst().orElse(null)
    }

}
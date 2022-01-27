# BetterEconomy
A simple Vault-based economy plugin

## Commands & Permissions
| Command | Permission |
| --- | --- |
| `/eco give <player> <amount>` | bettereconomy.set |
| `/eco take <player> <amount>` | bettereconomy.set |
| `/eco set <player> <amount>` | bettereconomy.set |
| `/eco reload` | bettereconomy.reload |
| `/balance` | bettereconomy.balance<br>bettereconomy.balance.others |
| `/baltop`| bettereconomy.balancetop |
| `/pay <player> <amount>` | bettereconomy.pay |

## Config
```yaml
# The file handler to store the balance
# Allow: file, mysql, sqlite, json
handler-type: file

# The type of the hook. Used to hook an economy core
# Allow: VAULT, TREASURY, NONE
hook-type: VAULT

# Settings on formatting the balance
currency:
  singular: $
  plural: $
  format-fractional-digits: 2

# Settings on balance tasks
balance:
  top-update-period: 100
  file-save-period: 200
  start-amount: 0.0

# Settings on database
database:
  mysql:
    host: localhost
    port: '3306'
    dbname: ''
    username: root
    password: ''
```

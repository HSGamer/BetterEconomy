# BetterEconomy
A simple economy plugin

## Commands & Permissions
| Command                       | Permission                                            |
|-------------------------------|-------------------------------------------------------|
| `/eco give <player> <amount>` | bettereconomy.set                                     |
| `/eco take <player> <amount>` | bettereconomy.set                                     |
| `/eco set <player> <amount>`  | bettereconomy.set                                     |
| `/eco reload`                 | bettereconomy.reload                                  |
| `/balance`                    | bettereconomy.balance<br>bettereconomy.balance.others |
| `/baltop`                     | bettereconomy.balancetop                              |
| `/pay <player> <amount>`      | bettereconomy.pay                                     |

## Config
```yaml
# The file handler to store the balance
# Allow: file, mysql, sqlite, json
handler-type: file

# Enable if the plugin should hook to other economy cores (Vault, Treasury, etc)
hook-enabled: true

# Settings on formatting the balance
currency:
  symbol: $
  singular: $
  plural: $
  format-fractional-digits: 2

# Settings on balance tasks
balance:
  top-update-period: 100
  file-save-period: 200
  start-amount: 0.0
  min-amount: 0.0

# Settings on database
database:
  mysql:
    host: localhost
    port: '3306'
    dbname: ''
    username: root
    password: ''
```

## Placeholders

> Requires [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)

| Placeholder                                      | Description                                                                    |
|--------------------------------------------------|--------------------------------------------------------------------------------|
| `%bettereconomy_balance%`                        | The balance of the player                                                      |
| `%bettereconomy_balance_formatted%`              | The formatted balance of the player                                            |
| `%bettereconomy_top%`                            | The current top position of the player                                         |
| `%bettereconomy_top_name_<number>%`              | The name of the player at the position `<number>` (starts from 1)              |
| `%bettereconomy_top_uuid_<number>%`              | The uuid of the player at the position `<number>` (starts from 1)              |
| `%bettereconomy_top_balance_<number>%`           | The balance of the player at the position `<number>` (starts from 1)           |
| `%bettereconomy_top_balance_formatted_<number>%` | The formatted balance of the player at the position `<number>` (starts from 1) |
